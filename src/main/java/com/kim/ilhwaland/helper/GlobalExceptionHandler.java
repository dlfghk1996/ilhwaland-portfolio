package com.kim.ilhwaland.helper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

/** 비즈니스 로직을 처리 중 발생한 예외 관리  */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 01. 입출력 오류
	@ExceptionHandler(IOException.class) 
	public void ioeException(IOException e, HttpServletResponse response, HttpServletRequest request) throws IOException { 
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			response.setContentType("text/plain;charset=UTF-8");
			response.sendError(400, "파일을 읽을 수 없습니다.");
		}else {
			printAlert(response, "파일 입출력 오류");
		}
	} 
		
	// 02. API 요청 오류
	@ExceptionHandler(ResourceAccessException.class)
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	public String httpConnectionException(Exception e,HttpServletResponse response,HttpServletRequest request) throws IOException  {
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			response.setContentType("text/plain;charset=UTF-8");
			response.sendError(400, "API 요청 오류");
		}
		return "error/httpConnection_error";
	}
	
	// 03. 서버 오류 & SQL 오류
	@ExceptionHandler({RuntimeException.class, NullPointerException.class})
	public String runtimeException(Model model, Exception e) {
		System.out.println(e.getMessage());
		return "error/500_error";
	}
	
	// 04. 사용자의 잘못된 요청(ex. 비밀번호 불일치 등)
	@ExceptionHandler(BadRequestException.class)
	public void badRequestException(Exception e, HttpServletResponse response) throws IOException {
		printAlert(response, "잘못된 요청입니다.");
	}
	
	// 05. 잘못된 url(ex. 사용자의 존재하지 않는 데이터 요청으로 인한 에러 등)
	@ExceptionHandler(javassist.NotFoundException.class)
	public String notFoundException(Exception e) {
		System.out.println(e.getLocalizedMessage());
        return "error/404_error";
	}
	
	// redirect
	public void printAlert(HttpServletResponse response, String errorMessage) {
	
		try {
			// 가상의 View로 만들기 위한 HTML 태그 구성
			/**String html = "<!doctype html>";
			html += "<html><head>";
			html += "<script type='text/javascript'>alert('"+errorMessage+"');</script>";
			html += "<script type='text/javascript'>history.back();</script>";
			html += "</head><body></body></html>";*/
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>alert('" + errorMessage + "'); history.back();</script>");
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
}
