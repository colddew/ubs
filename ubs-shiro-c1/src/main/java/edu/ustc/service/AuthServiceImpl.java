package edu.ustc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ustc.model.Resource;
import edu.ustc.model.Role;

/**
 * 新增的URL需要添加到admin角色，否则任何角色都可以随意访问
 */
@Service(value = "authService")
public class AuthServiceImpl implements IAuthService {
	
	private static final String CRLF = "\r\n";
	private static final String LAST_AUTH_STR = "/** = authc";
	
	@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;
	
	@Override
	public String loadFilterChainDefinitions() {

		StringBuffer sb = new StringBuffer();
		sb.append(getFixedAuthRule())
			.append(getDynamicAuthRule())
			.append(LAST_AUTH_STR);
		
		return sb.toString();
	}
	
	private List<Role> getRoleList() {
		
		Resource re1 = new Resource();
		re1.setUrl("/portal");
		
		Resource re2 = new Resource();
		re2.setUrl("/portal2");
		
		List<Resource> rs1 = new ArrayList<Resource>();
		rs1.add(re1);
		rs1.add(re2);
		
		List<Resource> rs2 = new ArrayList<Resource>();
		rs2.add(re1);
		
		List<Resource> rs3 = new ArrayList<Resource>();
		rs3.add(re1);
		
		Role r1 = new Role();
		r1.setRoleName("shiro-c1");
		r1.setResources(rs1);
		
		Role r2 = new Role();
		r2.setRoleName("shiro-c2");
		r2.setResources(rs2);
		
		Role r3 = new Role();
		r3.setRoleName("shiro-c3");
		r3.setResources(rs3);
		
		List<Role> rs = new ArrayList<Role>();
		rs.add(r1);
		rs.add(r2);
		rs.add(r3);
		
		return rs;
	}
	
	// 根据角色得到动态权限规则
	private String getDynamicAuthRule() {
	
		StringBuffer sb = new StringBuffer();
		Map<String, Set<String>> rules = new HashMap<String, Set<String>>();
		
		List<Role> roles = getRoleList();
		for (Role role : roles) {
			
			for (Resource resource : role.getResources()) {
				
				String url = resource.getUrl();
				if(StringUtils.isNotBlank(url)) {
					
					if (!url.startsWith("/")) {
						url = "/" + url;
					}
					
					if (!rules.containsKey(url)) {
						rules.put(url, new HashSet<String>());
					}
					
					rules.get(url).add((role.getRoleName()));
				}
			}
		}
		
		for (Map.Entry<String, Set<String>> entry : rules.entrySet()) {
			sb.append(entry.getKey()).append(" = ")
//					.append("authc, roles").append(entry.getValue())
					.append("authc, roleOrFilter").append(entry.getValue())
					.append(CRLF);
		}
		
		return sb.toString();
    }
	
	// 得到固定权限验证规则
	private String getFixedAuthRule() {
		
		StringBuffer sb = new StringBuffer("");
		sb.append("/casFailure = anon").append(CRLF)
			.append("/cas = cas").append(CRLF)
			.append("/logout = logout").append(CRLF);
		
		return sb.toString();
	}
	
	// 此方法加同步锁
	@Override
	public synchronized void reCreateFilterChains() {
		
		AbstractShiroFilter shiroFilter = null;
		try {
			shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
		}
		
		PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
				.getFilterChainResolver();
		DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
				.getFilterChainManager();
		
		// 清空旧的权限控制
		manager.getFilterChains().clear();
		
		shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
		shiroFilterFactoryBean.setFilterChainDefinitions(loadFilterChainDefinitions());
		
		// 重新构建生成
		Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
		for (Map.Entry<String, String> entry : chains.entrySet()) {
			String url = entry.getKey();
			String chainDefinition = entry.getValue().trim().replace(" ", "");
			manager.createChain(url, chainDefinition);
		}
	}
}