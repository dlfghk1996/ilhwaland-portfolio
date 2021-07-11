package com.kim.ilhwaland.helper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class WebHelpler {
	// 기본 인코딩 타입 설정
	private int start;
	private int end;
	
	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	
	/** redirect : 권한  제한 */
	public void redirect(String msg) throws IOException {

		ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		//HttpServletRequest request = servletContainer.getRequest();
		HttpServletResponse response = servletContainer.getResponse();
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out;
		out = response.getWriter();
		out.println("<script>alert('" + msg + "'); history.go(-1);</script>");
		out.flush();
	}
	
	/** 페이지 네이션 */
	public String pagenation(String pagename, int nowPage, int totalCount, int listCount, int pageCount) {
		//  Limit 시작 위치
		// 검색 범위의 시작 위치
		this.start = (nowPage - 1) * listCount + 1;
		this.end = nowPage * listCount;
		StringBuffer sb = new StringBuffer();
		// 페이지 네이션 총수
		int totalPage = totalCount / listCount + 1; // 홀수 일경우
		if ((totalCount % listCount) == 0) {
			listCount--; // 짝수일경우
		} 
		// 현재 사용자의 위치의 그룹
		int userGroup = nowPage / pageCount;
		if (nowPage % pageCount == 0) {
			userGroup--;
		};

		// 보여줄 페이지네이션 구성
		sb.append("<ui class=pagination>");
		if (userGroup != 0) {
			sb.append("<li><a href='");
			sb.append(pagename);
			sb.append("?nowPage=");
			int prev = (userGroup - 1) * pageCount + pageCount;
			sb.append(prev);
			sb.append("'>이전</a></li>");
		}

		// 보여줄 페이지네이션 구성
		for (int i = (userGroup * pageCount) + 1; i <= (userGroup * pageCount) + pageCount; i++) {
			sb.append("<li><a href='");
			sb.append(pagename);
			sb.append("?nowPage=");
			sb.append(i);
			sb.append("'>");
			sb.append(i);
			sb.append("</a></li>");
			if (i == totalPage) {
				break;
			}
		}

		// next
		// <a href="GoBorad.onepiece?cp=(userGroup+1)*pageSize+1">다음</a>
		if (userGroup != ((totalPage / pageCount) - (totalPage % pageCount == 0 ? 1 : 0))) {
			sb.append("<li><a href='");
			sb.append(pagename);
			sb.append("?nowPage=");
			int next = (userGroup + 1) * pageCount + 1;
			sb.append(next);
			sb.append("'>다음</a></li>");
		}
		sb.append("</ui>");
		return sb.toString();
	}
}
