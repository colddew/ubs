package edu.ustc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

public class ExtPersonAttributeDao extends StubPersonAttributeDao {
	
	@Override
	public IPersonAttributes getPerson(String uid) {
		
		Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
		
		List<Object> roles = new ArrayList<Object>();
		roles.add("shiro-c1");
		roles.add("shiro-c2");
		attributes.put("roles", roles);
		
		List<Object> permissions = new ArrayList<Object>();
		permissions.add("portal-c1:list");
//		permissions.add("portal-c2:query");
		attributes.put("permissions", permissions);
        
		return new AttributeNamedPersonImpl(attributes);
	}
}
