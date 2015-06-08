package edu.ustc.utils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

public class CasRealm extends org.apache.shiro.cas.CasRealm {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthorizationException {
		
		String username = (String) principals.getPrimaryPrincipal();
		System.out.println("#####" + username + "#####");
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        
        List list = principals.asList();
		Map<String, Object> map = (Map<String, Object>) list.get(1);
        authorizationInfo.setRoles(parseToSet((String) map.get("roles")));
        authorizationInfo.setStringPermissions(parseToSet((String) map.get("permissions")));
        
        return authorizationInfo;
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		System.out.println("#####" + token.getPrincipal() + "#####");
		
		return super.doGetAuthenticationInfo(token);
	}
	
	private Set<String> parseToSet(String str) {
		
		if(str.indexOf("[") != -1) {
			str = str.replaceAll("\\[", "");
		}
		
		if(str.indexOf("]") != -1) {
			str = str.replaceAll("\\]", "");
		}
		
		Set<String> set = new HashSet<String>();
		
		if(StringUtils.isNotBlank(str)) {
			
			String[] array = str.split(",");
			for(String item : array) {
				
				if(StringUtils.isNotBlank(item)) {
					set.add(item.trim());
				}
			}
		}
		
		return set;
	}
}