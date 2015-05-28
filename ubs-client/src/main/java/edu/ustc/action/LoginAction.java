package edu.ustc.action;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("prototype")
public class LoginAction {
	
	@RequestMapping("/login")
	public String login() throws Exception {
		
		return "/login";
	}
	
	@RequestMapping("/index")
	public String index() throws Exception {
		
		return "/index";
	}
}
