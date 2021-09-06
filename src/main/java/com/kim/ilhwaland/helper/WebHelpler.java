package com.kim.ilhwaland.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component
public class WebHelpler {
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
	
	/** [Method 01] 페이지 네이션 */
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
	
	/** [Method 02-1] HTTP Connection */
	public JSONObject get(String apiUrl, Map<String, String> requestHeaders) throws Exception {
		
		// 1. HttpURLConnection 객체 생성
		HttpURLConnection conn = connect(apiUrl);
		
		try {
			// 2. 요청 방식 설정
			conn.setRequestMethod("GET");
			for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
				// 전달받은 매개변수를  Request Header 값으로 셋팅 
				/** ex. 서버 응답 데이터의  Content-type 설정
				 *  ex. conn.setRequestProperty(Content-type", "application/json) 
				 */
				conn.setRequestProperty(header.getKey(), header.getValue());
	    	}
			
			// 3. 응답 결과 코드에 따른 통신 분기 처리
			int responseCode = conn.getResponseCode(); // 응답 결과 코드
			
			// 통신 성공
			if (responseCode == HttpURLConnection.HTTP_OK) { 
				//  해당 url 경로에서 읽어온 바이트 데이터를 분석하기 위해 파라미터로 넘긴다.
				return readBody(conn.getInputStream());
			
			// 에러 발생
			} else { 
				return readBody(conn.getErrorStream());
			}
	    } catch (IOException e) {
	    	System.out.println(e.getLocalizedMessage());
	    	throw new ResourceAccessException("API 응답을 읽는데 실패했습니다.");
	    } finally {
	    	conn.disconnect();
	    }
	 }
	
	/** [Method 02-2] HTTP Connection 
	 * @throws IOException */
	private static HttpURLConnection connect(String apiUrl) throws Exception {
		try {
			// URL객체를 생성하여 요청 URL에 접속 한다.
			URL url = new URL(apiUrl);
			
			return (HttpURLConnection)url.openConnection();
			 
		} catch (MalformedURLException e) {
			// 프로토콜 오류 (file:, http:, https:)
			throw new ResourceAccessException("잘못된 URL 입니다.");
		} catch (IOException e) {
			throw new ResourceAccessException("");
		}
	}
	
	/** [Method 02-3] HTTP Connection  */
	private static JSONObject readBody(InputStream body) throws Exception {
		
		// 1. 바이트 스트림을 문자 스트림으로 변환한다.
		InputStreamReader streamReader = new InputStreamReader(body);
	    
		// 2. 문자 입력 스트림에서 텍스트를 읽고 문자, 배열 및 행을 효율적으로 읽을 수 있도록 문자를 버퍼링(메모리를 채운다.)합니다.
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			
			// 3. 응답받은 데이터를 담을 StringBuilder 객체 생성
			StringBuilder responseBody = new StringBuilder();
			String line;
			
			// 4. 버퍼에 있는 정보를 라인단위로 읽고 하나의 문자열로 변환한다.
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			// 5. Json parser 객체 생성 (JSON 문자열의 구문을 분석 및 객체 생성) 
			JSONParser jsonParser = new JSONParser(); 
			
			// 6. JSON 형식의 문자열 값을 JSON Object로 변환한다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody.toString());
		
			lineReader.close();
			
			// 7. responseBody Top 레벨 단계의 키를 가지고 데이터를 추출하고 , 그 결과를 메소드를 호출한 곳으로 돌려준다.
			return jsonObject;
		 
		}catch (ParseException e1) {
			System.out.println(e1.getMessage());
			throw new ResourceAccessException("API 응답을 읽는데 실패했습니다.");
		} catch (IOException e) {
			throw new ResourceAccessException("API 응답을 읽는데 실패했습니다.");
		} 
	}
}
