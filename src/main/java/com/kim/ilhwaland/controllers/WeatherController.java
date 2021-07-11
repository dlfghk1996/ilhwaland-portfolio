package com.kim.ilhwaland.controllers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @RestController 
 * @ResponseBody : 자바객체를  XML/JSON 으로 변환해서  BODY 에 실어 전송 .
 * @RequestBody  : 클라이언트가 요청한  XML/JSON을 자바 객체로 변환해서 전달 받을수 있다.
 * ResponseEntity
 * RequestEntity
 * **/
/**
 * REST API 설계
 * 1. URI는 정보의 자원을 표현해야 한다.
 * 2. 자원에 대한 행위는 HTTP Method(Get, POST, PUT, DELETE)로 표현 한다.
 * */
@RestController
public class WeatherController {
	private String service_key = "InCy7oGmpn0fCPzP6PQ9YCFij4MUAtTeJbdvpsNp2D23ON%2FmyczRdo4%2FD1h7k7z398%2FIwvSUA4OWs5O%2Fii4U6w%3D%3D";  // 서비스 인증키 
	

	@PutMapping(value = "weather")
	public ResponseEntity<Object> getWeather2() {
		System.out.println("PUT getWeather2");
		return new ResponseEntity<Object>("통신성공",HttpStatus.OK);  // 데이터와 상태코드 리턴
	}	
		
	/**
	 * getUltraSrtNcst 초단기실황조회 : 매시간 30분마다 발표 10분마다 최신 정보로 업데이트
	 * getUltraSrtNcst 초단기예보조회 : 전국의 읍,면,동 단위로 발표시간 1시간 후부터 최대 6시간까지의 예보를 매시 30분마다 발표합니다. 
	 * getVilageFcst   동네 예보 조회  : 검색 시간에 따라 최대 3일 최소 2일간의 기상을 예보하는 데이터입니다.
	 * getFcstVersion  예보 버전 조회
	 * **/

	/**
	 * 특정 요소의 코드값 및 범주
	 * SKY : 맑음(1), 구름많음(3), 흐림(4) -> 하늘 상태
	 * PTY : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7) -> 강수형태
	 * */
	
	/**
	 * 초단기실황조회 결과값
	 * T1H : 기온
	 * RN1 : 1시간 강수량
	 * UUU : 동서 바람 성분
	 * VVV : 남북 바람 성분
	 * REH : 습도
	 * PTY : 강수 형태
	 * VEC : 풍향 
	 * WSD : 풍속
	 * */
	// 초단기실황조회 : : 매시간 30분마다 발표 10분마다 최신 정보로 업데이트
	// 기온, 습도 , 풍속
	@GetMapping("weather")
	public ResponseEntity<Object> weather() {
		// 오늘 날짜
		
		// 초단기실황조회
		String callBackUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
		
		
		// 웹 요청시 같이 전달될 데이터 = 요청 메시지	
		String requestUrl = callBackUrl 
							+ "?serviceKey="+ service_key
							+ "&dataType=JSON"         // dataType
							+ "&numOfRows=10"          // 한 페이지 결과 수
							+ "&pageNo=1"             // 페이지 번호
							+ "&base_date=20210709"   // 발표 일자
							+ "&base_time=0600"       // 발표 시각 (-매 시각 40분 이후 호출) 
							+ "&nx=60"                // 예보 지점의 X 좌표 값
							+ "&ny=127";              // 예보지점의 Y 좌표 값
		
		// URL 객체 생성
		URL url;
		try {
			/** API 주소를 연결하여 텍스트를  String으로 받아오고 다시 JSONObject로 받아오는 과정*/
			url = new URL(requestUrl);
			// URL 주소의 원격 객체에 접속 한 뒤, 통신할 수 있는 URLConnection 객체 리턴
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET"); // 요청 방식
			conn.setRequestProperty("Content-type", "application/json"); // Request Header 값 세팅
			
			
			BufferedReader bufferedReader;
			/** HTTP상태 코드 분기 처리
			* 100,200(정상요청),300(redirect),400(요청에러)
			*/
			if(conn.getResponseCode()>=200 && conn.getResponseCode()<=300) {
				// 바이트 기반 스트림 -> 보조스트림 -> 문자 기반 스트림
				// InputStreamReader : 문자 변화 보조 스트림
				//                   : 바이트 입력 스트림에 연결되어 문자 입력 스트림인 Reader로 변환는 보조스트림     
				// bufferedReader    : 성능 향상 보조 스트림 (메모리 버퍼 제공)
				bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else {
				bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				System.out.println("오류");
			}

			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			bufferedReader.close();
			conn.disconnect();
			
			String jsonText = stringBuffer.toString();
			
			// JSON Parser를 이용하여 Stirng type의 JSON 형태의 데이터를 JSONObject로 변환
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject)parser.parse(jsonText);
			
			// 데이터 파싱
			JSONObject response = (JSONObject)jsonObject.get("response");
			// response 로 부터 body 찾아옵니다. 
			JSONObject body = (JSONObject) response.get("body"); 
			// body 로 부터 items 받아옵니다. 
			JSONObject items = (JSONObject) body.get("items");
			JSONArray item = (JSONArray) items.get("item");
			JSONObject weather;
			 for (int i = 0; i < item.size(); i++) {
				 weather = (JSONObject) item.get(i);
				 String category = (String)weather.get("category");
				 if(category.equalsIgnoreCase("T1H")) {
					 String obsrValue = (String)weather.get("obsrValue");
					 System.out.println("기온 :" + obsrValue); 
				 } else if(category.equalsIgnoreCase("REH")) {
					 String obsrValue = (String)weather.get("obsrValue");
					 System.out.println("습도 :" + obsrValue);
				 } else if(category.equalsIgnoreCase("WSD")) {
					 String obsrValue = (String)weather.get("obsrValue");
					 System.out.println("풍속 :" + obsrValue);
				 } else if(category.equalsIgnoreCase("PTY")) {
					 String obsrValue = (String)weather.get("obsrValue");
					 System.out.println("강수 형태 : " + obsrValue);
				 }	
			 }

				
			System.out.println(jsonText);
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("잘못된 URL 형식입니다.");
		}catch (ParseException e) {
            System.out.println("변환에 실패");
            e.printStackTrace();
       } catch (IOException e) {
			e.printStackTrace();
		}
	
		return new ResponseEntity<Object>("",HttpStatus.OK);  // 데이터와 상태코드 리턴
	}
//	
//	
//	// 동네예보
//	@GetMapping("restApiGetWeather")
//	@ResponseBody
//	public ResponseEntity<Object> restApiGetWeather() {
//		String callBackUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst/";
//		// 웹 요청시 같이 전달될 데이터 = 요청 메시지	
//		String requestUrl = callBackUrl 
//							+ "?serviceKey="+ service_key
//							+ "&dataType=JSON"         // dataType
//							+ "&numOfRows=10"          // 한 페이지 결과 수
//							+ "&pageNo=1"             // 페이지 번호
//							+ "&base_date=20210706"   // 발표 일자
//							+ "&base_time=0500"       // 발표 시각 (-매 시각 40분 이후 호출) 
//							+ "&nx=60"                // 예보 지점의 X 좌표 값
//							+ "&ny=127";              // 예보지점의 Y 좌표 값
//				
//		return new ResponseEntity<Object>("test",HttpStatus.OK);  // 데이터와 상태코드 리턴
//	}
}
