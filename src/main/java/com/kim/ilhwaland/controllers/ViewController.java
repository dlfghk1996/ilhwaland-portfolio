package com.kim.ilhwaland.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {

	// 메인 페이지
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
			return "index";
	}

	@RequestMapping(value = "weatherPage", method = RequestMethod.GET)
	public String weatherPage(HttpServletRequest request) {
		// 서버 접속자의 ip 주소로 접속자의 실시간 위치 추출
		/**
		 * 문제 
		 * 1. 프록시를 거쳐서 들어오는 접속의 경우, 정확한 ip정보를 가져올 수 없다.(대리 통신)
		 * 2. was가 2차 방화벽 안에 있고 클라이언트에서 web server를 통해 호출하는 경우
		 *    getRemoteAddr은 진짜 접속한 클라이언트의 ip가 아닌, 웹서버의 ip 정보를 가져온다.
		 * 3. 클러스터로 구성도외 로드 밸런서 에서 호출되는 경우도 getRemoteAddr은 로드 밸런서의 ip를 가져온다.
		 * */
		
		// X-FORWARDED-FOR
		// HTTP 프록시나 로드 밸런서를 통해 웹 서버에 접속하는 클라이언트의 원 IP 주소를 식별
		String ip = request.getHeader("X-FORWARDED-FOR"); 
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			if( ip.indexOf(",")!=-1 ){
				ip = ip.split(",")[0];
			}
		}
		  
         //proxy 환경일 경우
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	 ip = request.getHeader("Proxy-Client-IP");
         }
         //웹로직 서버일 경우
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("WL-Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
             ip = request.getHeader("HTTP_CLIENT_IP");  
         } 
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
         }  
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	 ip = request.getRemoteAddr();
         }
 
		return "weather/weather";
	}

}
