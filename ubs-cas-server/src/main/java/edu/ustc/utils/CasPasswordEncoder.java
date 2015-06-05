package edu.ustc.utils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.jasig.cas.authentication.handler.PasswordEncoder;


public class CasPasswordEncoder implements PasswordEncoder {
	
	public static final String ALGORITHM_NAME_MD5 = "md5";
	public static final int HASH_ITERATIONS_ZERO = 0;
	public static final int HASH_ITERATIONS_TWICE = 2;
	
	private String algorithmName;
	private int hashIterationsTimes;
	private String userName;
	private String salt;
	
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	
	public void setHashIterationsTimes(int hashIterationsTimes) {
		this.hashIterationsTimes = hashIterationsTimes;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Override
	public String encode(String password) {
		
		return new SimpleHash(algorithmName, password,
				ByteSource.Util.bytes(userName + salt),
				hashIterationsTimes).toHex();
	}
}
