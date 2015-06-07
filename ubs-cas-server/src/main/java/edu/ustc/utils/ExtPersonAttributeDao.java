package edu.ustc.utils;

import java.util.ArrayList;
import java.util.Collections;
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
		attributes.put("roles", roles);
		
		attributes.put("permissions", Collections.singletonList((Object) "portal-c2:list"));
        
		return new AttributeNamedPersonImpl(attributes);
	}
}
