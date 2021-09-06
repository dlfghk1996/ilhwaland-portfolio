package com.kim.ilhwaland.helper.weather;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.kim.ilhwaland.helper.WebHelpler;

@Component
public class WeatherHelper {

	/**
	 * return object format 
	 * 초단기실황 {category :value, categorys:value}
	 * 초단기예보 {date: {categorys : value}}
	 * 동네예보    {date:{categorys : value}}
	*/

	@Autowired
	WeatherSet weatherSet;
	
	@Autowired
	WebHelpler webHelpler;
	
	@Autowired
	private CarwashHelper carwashHelper;
	
	// default 위치 
	public static String latitude = "37.5717837878322";
	public static String longitude = "126.97648639291874";
	
	// 요청 그리드 위치
	String nx;
	String ny;
	
	// API ServiceKEY
	private String service_key = "InCy7oGmpn0fCPzP6PQ9YCFij4MUAtTeJbdvpsNp2D23ON%2FmyczRdo4%2FD1h7k7z398%2FIwvSUA4OWs5O%2Fii4U6w%3D%3D"; // 서비스
	
	// 추출한 날씨 데이터(하늘상태)를 view에서 이미지로 시각화하기 위한, 이미지파일명으로 매핑
	final static Map<String, String> skyMap = new HashMap<String, String>() { 
		{
			put("맑음", "sunny");
			put("흐림", "cloudy");
			put("구름 많음", "cumulus");
		}
	};
	
	// 추출할 날씨 정보 카테고리
	List<String> categorys = Arrays
			.asList(new String[] { "T1H", "REH", "PTY", "SKY", "POP", "TMN", "TMX", "T3H", "RN1" , "fcstDate"}); 
	
	// 추출한 날씨 데이터를 담는 map
	private Map<String, String> weatherMap = new LinkedHashMap<String, String>();
	
	
	/**
	* 1. 초단기 실황 조회 
	* 2. 추출 카테고리 : REH(습도), PTY(강수형태), T1H(기온)
	* 3. requestParam : base_time(요청 시간) => 30분 이전이면 현재 시보다 1시간 전, 30분 이후면 현재 시간
	**/
	public Map<String, String> getUltraSrtNcst(String callBackUrl) throws Exception {
		
		// 초기 base_time & base_date 설정
		weatherSet.set_standardDate();

		// 요청 url 설정
		String requestUrl = setRequestURL(callBackUrl, 10, weatherSet.getBase_date(), weatherSet.getBase_time());

		JSONArray item = connectionAPI(requestUrl);

		JSONObject weather;
		String category = null;

		for (int i = 0; i < item.size(); i++) {
			weather = (JSONObject) item.get(i);
			category = (String) weather.get("category");

			// 추출할려는 날씨항목 인지 검사 
			if (categorys.contains(category)) {
				String obsrValue = (String) weather.get("obsrValue");

				// 강수 형태 코드 값 변환 및 이미지 파일명 매핑
				if (category.equalsIgnoreCase("PTY")) {
					obsrValue = weatherSet.getPty(obsrValue); // 수치 변환
				}
				
				// view 에 반환하기 위해 map 에 담는다.
				weatherMap.put(category, obsrValue);
			}
		}
		return weatherMap;
	}

	
	/**
	* 1. 초단기 예보조회
	* 2. 추출 카테고리 : SKY(하늘상태), T1H(기온), PTY(강수형태), REH(습도), RN1(1시간 강수량)
	* 3. requestParam : base_time(요청 시간) => 30분 이전이면 현재 시보다 1시간 전, 30분 이후면 현재 시간
	**/
	public Map<String, Map<String, String>> getUltraSrtFcst(String callBackUrl) throws Exception {
		
		// 1. 날씨 메뉴 마다 발표시간을 다르게 주기 위해, WeatherSet 클래스의 필드에 날씨코드값 할당
		WeatherSet.weatherCode = "ultraSrtFcst"; 
		
		// 2. 발표시간 설정
		weatherSet.set_standardDate();

		// 3. 날씨예보 API 요청 
		String requestUrl = setRequestURL(callBackUrl, 40, weatherSet.getBase_date(), weatherSet.getBase_time());

		JSONArray item = connectionAPI(requestUrl);
		JSONObject weather = new JSONObject();

		String category;
		
		// 4. return Object
		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();

		// 5. 반복문을 돌려서 데이터 추출 및 가공
		for (int i = 0; i < item.size(); i++) {

			weather = (JSONObject) item.get(i);
			category = (String) weather.get("category");
			
			// 5-1. 추출할려는 날씨항목 인지 검사
			if (categorys.contains(category)) {

				String fcstTime = (String) weather.get("fcstTime");   // 예보 시간
				String fcstValue = (String) weather.get("fcstValue"); // 예보 값

				// 5-2. 예보 시간 형식 변환 (0000 => 00:00 am/pm)
				fcstTime = weatherSet.getFcstTime_Desc(fcstTime); 
				
				/* 5-3. weatherMap에 해당 시간과 같은 key가 존재하는지 검사 
				   -해당 key의  값으로 추출데이터를 저장하기 위해 key의 value인 map을 구한다. */
				if (map.containsKey(fcstTime)) {
					weatherMap = map.get(fcstTime);
				} else {
					weatherMap = new LinkedHashMap<String, String>();
				}
				
				// 5-4. 하늘 상태 코드 값 변환 및 이미지 파일명 매핑
				if (category.equalsIgnoreCase("SKY")) {
					fcstValue = weatherSet.getSky(fcstValue);
					weatherMap.put("skyImg", skyMap.get(fcstValue));
				}
				
				// 5-5. 강수 형태 코드 값 변환 및 이미지 파일명 매핑
				if (category.equalsIgnoreCase("PTY")) {
					fcstValue = weatherSet.getPty(fcstValue);
				}
				
				// 5-6. 측정값 저장
				weatherMap.put(category, fcstValue);
				map.put(fcstTime, weatherMap);
			}
		}
		return map;
	}

	
	/**
	* 1. 동네예보
	* 2. 추출 카테고리 : SKY(하늘상태), PTY(강수형태), TMX(낮 최저기온), TMN(아침 최고기온), POP(강수확률), REH(습도)
	* 3. requestParam : base_time(요청 시간) => 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300
	**/
	public Map<String, Map<String, String>> getNationwideWeather(String callBackUrl) throws Exception {
		
		// 1. 날씨 메뉴 마다 발표시간을 다르게 주기 위해, WeatherSet 클래스의 필드에 날씨코드값 할당
		WeatherSet.weatherCode = "vilageFcst";
		
		// 2. 발표시간 설정
		weatherSet.set_standardDate();

		// 3. 날씨예보 API 요청 
		String requestUrl = setRequestURL(callBackUrl, 250, weatherSet.getBase_date(), weatherSet.getBase_time());

		JSONArray item = connectionAPI(requestUrl);
		JSONObject weather;
		
		// 4. 세차 알림 서비스를 위해 모든 예보 날짜 강수확률을 체크 하여 비오는 날짜를 확인
		carwashHelper.checkPopToCarwash(item);
	
		// 5. return Object
		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();

		String category = "";
		String fcstValue = "";
		
		// 6. 추출조건 값 설정
		/* 6-1. 예보시간 : 발표시간으로 부터 4시간후의 예보시간의 각 예보요일 날씨예보를 가져온다.
		      (ex. 측정된 여러시간대의 날씨에서 지정된 시간의 날씨예보만 가져온다.) */
		String want_to_fcstTime = weatherSet.getFcstTime();
		
		// 6-2. 오늘 일자로 부터 3일 후 일자
		String date = LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		// 7. 반복문을 돌려서 데이터 추출 및 가공
		for (int i = 0; i < item.size(); i++) {
			weather = (JSONObject) item.get(i);
			
			category = (String) weather.get("category");        // 날씨예보 항목
			String fcstDate = (String) weather.get("fcstDate"); // 예보일자
			String fcstTime = (String) weather.get("fcstTime"); // 예보시간
			
			// 7-1. 예보일자를 요일로 변환하여, key 값으로 사용함으로, 날씨 값을 꺼내오거나, 저장한다.
			String fcstDate_day = weatherSet.getFcstDate_Desc(fcstDate);  
			
			/* 7-2. weatherMap 에 해당 요일과 같은 key가 존재하는지 검사 
			        -해당 key의  값으로 추출데이터를 저장하기 위해 key의 value인 map을 구한다. */
			if(map.containsKey(fcstDate_day)) {
				weatherMap = map.get(fcstDate_day); 
			}else {
				weatherMap = new LinkedHashMap<String, String>(); 
			}
			
			/** 날씨예보 추출 조건 (want_to_fcstTime, 3일후 예보, 최저기온, 최고기온)
			* - 발표시간으로 부터 4시간후
			* - 발표시간으로 부터 4시간후의 예보시간에는 날씨항목이 추출이 안되는 경우
			* 	  1. 오늘 일자로 부터 3일 후 예보  (이유: 예보시간이 일정하지 않기 때문이다.) 
			*     2. 최고 기온, 최저기온             (이유: 특정 시간에만 조회가 가능하기 때문이다. => 06시, 15시 )
			*/
			if (fcstTime.equals(want_to_fcstTime) || fcstDate.equals(date) || category.equals("TMN") || category.equals("TMX")) { 
				
				// 7-3. 추출할려는 날씨항목 인지 검사
				if (categorys.contains(category)) {
					
					// 7-4. 측정값 추출
					fcstValue = (String) weather.get("fcstValue"); 
					
					// 7-5. 하늘상태, 강수 형태 코드값 변환
					switch (category) {
						// 하늘 상태 코드 값 변환 및 이미지 파일명 매핑
						case "SKY":
							fcstValue = weatherSet.getSky(fcstValue);
							weatherMap.put("skyImg", skyMap.get(fcstValue));
							break;
						// 강수 형태 코드 값 변환
						case "PTY": fcstValue = weatherSet.getPty(fcstValue); break;
						default: break;
					}
					
					// 7-6. 측정값 저장
					weatherMap.put(category, fcstValue);
					map.put(fcstDate_day, weatherMap); 
				}
			} 
			
			// 8. 만일 강수 형태가 눈 이나 비면, 기존 측정된 날짜의 value에 덮어 씌운다.
			if(category.equals("PTY")) {
				fcstValue = (String) weather.get("fcstValue"); // 측정값
				if(!fcstValue.equals("0")){
					// 강수 형태 코드 값 변환
					fcstValue = weatherSet.getPty(fcstValue);
					
					weatherMap.put(category, fcstValue);
					map.put(fcstDate_day, weatherMap); 
				}
			}
		}
	
		return map;
	}

	
	/** 위도 ,경도를 격자좌표 변환 **/
	public HashMap<String, Object> getGridXY() throws Exception {

		double RE = 6371.00877; 	// 지구 반경(km)
		double GRID = 5.0; 			// 격자 간격(km)
		double SLAT1 = 30.0; 		// 투영 위도1(degree)
		double SLAT2 = 60.0; 		// 투영 위도2(degree)
		double OLON = 126.0; 		// 기준점 경도(degree)
		double OLAT = 38.0; 		// 기준점 위도(degree)
		double XO = 43; 			// 기준점 X좌표(GRID)
		double YO = 136; 			// 기1준점 Y좌표(GRID)

		HashMap<String, Object> resultMap = new HashMap<>();

		
		double DEGRAD = Math.PI / 180.0;
		double re = RE / GRID;
		double slat1 = SLAT1 * DEGRAD;
		double slat2 = SLAT2 * DEGRAD;
		double olon = OLON * DEGRAD;
		double olat = OLAT * DEGRAD;

		double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
		sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
		double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
		sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
		double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
		ro = re * sf / Math.pow(ro, sn);

		double lat = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);

		double ra = Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5);

		ra = re * sf / Math.pow(ra, sn);

		double theta = lon * DEGRAD - olon;

		if (theta > Math.PI)
			theta -= 2.0 * Math.PI;
		if (theta < -Math.PI)
			theta += 2.0 * Math.PI;

		theta *= sn;

		this.nx =  Integer.toString((int) Math.floor(ra * Math.sin(theta) + XO + 0.5));
		this.ny =  Integer.toString((int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));

		return resultMap;
	}

	
	/** connection http **/
	public JSONArray connectionAPI(String callBackUrl) throws Exception {
		
		// HttpURLConnection url 연결을 시도하는 메서드의 파라미터로 Request Header 값을 넘긴다.
		Map<String, String> requestHeaders = new HashMap<>(); 
	    requestHeaders.put("Content-type", "application/json");
		
	    // 응답받은 데이터에서 Top 레벨 단계인 response 값을 추출한 데이터
	    JSONObject jsonObject;
		
			jsonObject = webHelpler.get(callBackUrl,requestHeaders);
		
		// 순차적으로 response -> body - > items 을 찾는다.
		String[] key = { "response", "body", "items" }; 
		for (int i = 0; i < key.length; i++) {
			jsonObject = (JSONObject) jsonObject.get(key[i]);
		}
		
		// JSONArray를 받아 호출한 곳에 return 한다.
		JSONArray item = (JSONArray) jsonObject.get("item");
		return item;
	}
	
	
	/** requestURL 생성 **/
	public String setRequestURL(String callBackUrl, int numOfRows, String base_date, String base_time) {
	
		// 해당 위치의 날씨 정보를 제공하는 API로 요청 항목을  파라미터로 전달한다.
		 String requestUrl = callBackUrl
				 	+ "?serviceKey=" + service_key   // service_key
				 	+ "&dataType=JSON" 				 // dataType
					+ "&numOfRows=" + numOfRows      // 한 페이지 결과 수
					+ "&pageNo=1" 				     // 페이지 번호
					+ "&base_date=" + base_date      // 발표 날짜
					+ "&base_time=" + base_time      // 발표 시간
					+ "&nx=" + this.nx 				 // 예보지점의 x 좌표 값
					+ "&ny=" + this.ny;				 // 예보지점의 Y 좌표 값
			
		return requestUrl;
	}
	
	
	/** 좌표  => 주소 변환  */
	public String convertGeolocation() throws Exception {
	
		// 애플리케이션 REST API 키를 헤더에 담아 GET으로 요청한다.
		RestTemplate restTemplate = new RestTemplate(); 

		// header 설정
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_JSON); //JSON 변환 
		headers.set("Authorization", "KakaoAK eb641cc07cdc156ef2c72fc2aaa1227f"); //appKey 설정
		
		// HttpEntity : 헤더 정보를 가져오는 객체
		HttpEntity<String> entity = new HttpEntity<String>("Parameter", headers); 
		URI url = URI.create("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + longitude + "&y="+ latitude ); 
		
		// exchange : HTTP 헤더 새로 생성
		ResponseEntity<String> response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		// json 형식의 반환값을  JSON 객체로 변환
		JSONParser jsonParser = new JSONParser(); 
		
		
			String address_name="";
			JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString());
			JSONArray jsonArray = (JSONArray) jsonObject.get("documents");
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject address  = (JSONObject) jsonArray.get(i);
				address_name = (String) address.get("address_name");
			}
			
			return address_name; 
	}
	
	
}
