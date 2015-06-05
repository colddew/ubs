package edu.ustc.utils;

import java.security.GeneralSecurityException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import edu.ustc.model.User;

public class CasQueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
	
	@NotNull
    private String sql;
	
	private String getEncryptedPassword(User user, String password) {
		
		CasPasswordEncoder casPasswordEncoder = (CasPasswordEncoder) getPasswordEncoder();
		casPasswordEncoder.setAlgorithmName(CasPasswordEncoder.ALGORITHM_NAME_MD5);
		casPasswordEncoder.setHashIterationsTimes(CasPasswordEncoder.HASH_ITERATIONS_TWICE);
		casPasswordEncoder.setUserName(user.getUserName());
		casPasswordEncoder.setSalt(user.getSalt());
		
		return casPasswordEncoder.encode(password);
	}
	
    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

        final String username = credential.getUsername();
        final String password = credential.getPassword();
        
        try {
			User user = (User) getJdbcTemplate().queryForObject(this.sql, new Object[] { username }, 
					new BeanPropertyRowMapper<User>(User.class));
        	
            final String dbPassword = user.getPassword();
            String encryptedPassword = getEncryptedPassword(user, password);
            
            if (!dbPassword.equals(encryptedPassword)) {
                throw new FailedLoginException("Password does not match value on record.");
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            } else {
                throw new FailedLoginException("Multiple records found for " + username);
            }
        } catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
        return createHandlerResult(credential, new SimplePrincipal(username), null);
    }
    
    public void setSql(final String sql) {
        this.sql = sql;
    }
}
