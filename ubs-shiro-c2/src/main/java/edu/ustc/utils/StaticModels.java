package edu.ustc.utils;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

@SuppressWarnings("deprecation")
public class StaticModels extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 866386618087027870L;
	
	@PostConstruct
	public void setStaticModels() throws Exception {
		
		this.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
		
//		this.put("ManageConstants", getTemplateHashModel("com.enniu.licai.zhizunbao.manage.backstage.enums.ManageConstants"));
	}
	
	public static TemplateHashModel getTemplateHashModel(String clazz) throws Exception {
		
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		
		return(TemplateHashModel) staticModels.get(clazz);
	}
}