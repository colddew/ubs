package edu.ustc.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("prototype")
public class CommonAction {
	
	@RequestMapping("/index")
	public String index() throws Exception {
		
		return "/index";
	}
	
	@RequestMapping("/portal")
	public String portal() throws Exception {
		
		return "/portal";
	}
	
	@RequestMapping("/portal2")
	public String portal2() throws Exception {
		
		return "/portal2";
	}
	
	@RequestMapping("/casFailure")
	public String casFailure() throws Exception {
		
		return "/casFailure";
	}
}
