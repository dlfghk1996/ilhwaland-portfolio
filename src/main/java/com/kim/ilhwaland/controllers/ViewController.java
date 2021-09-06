package com.kim.ilhwaland.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
	
	/** [VIEW]  메인 페이지 */
	@RequestMapping(value = {"/", "home"}, method = RequestMethod.GET)
	public String home(HttpServletRequest request, Model model) {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("member_num")==null) {
			session.setAttribute("member_num", 1);//member num 세션에 저장
		}
		return "index";
	}
	
	/** [VIEW] 500 에러 페이지 */
	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String server_error(HttpServletRequest request, Model model) {
		return "error/500_error";
	}
	
	/** [VIEW] 404 에러 페이지 */
	@RequestMapping(value = "notFound_error", method = RequestMethod.GET)
	public String notFound_error(HttpServletRequest request, Model model) {
		return "error/404_error";
	}
	
	/** [VIEW] httpConnection 에러 페이지 */
	@RequestMapping(value = "httpConnection_error", method = RequestMethod.GET)
	public String httpConnection_error(HttpServletRequest request, Model model) {
		return "error/httpConnection_error";
	}
}
