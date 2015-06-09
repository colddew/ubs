package edu.ustc.utils;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class RoleOrFilter extends AuthorizationFilter {
	
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		
        Subject subject = getSubject(request, response);
        String[] roles = (String[]) mappedValue;
        
        if (roles == null || roles.length == 0) {
            // 没有角色限制，有权限访问
            return true;
        }
        
        for (String role : roles) {
			if (subject.hasRole(role)) {
				// 若当前用户是roles中的任何一个，则有权限访问
				return true;
			}
		}
        
		return false;
    }
}