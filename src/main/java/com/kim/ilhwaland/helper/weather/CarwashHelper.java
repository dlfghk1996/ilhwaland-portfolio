package com.kim.ilhwaland.helper.weather;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kim.ilhwaland.dto.Carwash_post;
import com.kim.ilhwaland.helper.WebHelpler;


@Component
public class CarwashHelper {
	
	@Autowired
	private WebHelpler webHelper;
	
	@Autowired
	WeatherSet weatherSet;
	
	//  비오는 요일 리스트
	private List<String> rainyday_list;
	
	/** [Method 01] 사용자가 요청한 키워드로 네이버 블로그 크롤링   */
	public List<Carwash_post> getCarwash_Search(String keyword) throws Exception {	
		
		// return object
		List<Carwash_post> carwash_blog_list = new ArrayList<Carwash_post>(); 
		
		try {
        	keyword = URLEncoder.encode(keyword + " 세차장", "UTF-8");
        
	        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + keyword;    // json 결과
	        
	        Map<String, String> requestHeaders = new HashMap<>();  // 네이버
	
			String clientId = "ZjM3sfQzXXJtAANUew_A"; // 애플리케이션 클라이언트 아이디값
		    String clientSecret = "zVRr2T3LSt";       // 애플리케이션 클라이언트 시크릿값
		    
	        requestHeaders.put("X-Naver-Client-Id", clientId);
	        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
	     
	        JSONObject jsonObject = webHelper.get(apiURL,requestHeaders);

	        if (jsonObject.containsKey("items")) { 
	        	JSONArray jsonArray = (JSONArray) jsonObject.get("items");
	        	
	        	// JsonArray 형식의 스트링객체 => DTO LIST 로 매핑 
		        ObjectMapper mapper = new ObjectMapper(); 
				carwash_blog_list = Arrays.asList(mapper.readValue(jsonArray.toString(), Carwash_post[].class));
	        }
		} catch (UnsupportedEncodingException e) {
            throw new ResourceAccessException(e.getMessage());
        } catch (JsonMappingException e) {
			throw new ResourceAccessException(e.getMessage());
		} catch (JsonProcessingException e) {
			throw new ResourceAccessException(e.getMessage());
		} 
        return carwash_blog_list;
	}
	
	/** [Method 02] 동네예보 조회 데이터를 기반으로 다음 메서드를 구현한다.
	 *  @ReturnValue = 강수확률 60% 이상인 요일, 강수 형태가 비,눈 인 요일  */
	public void checkPopToCarwash(JSONArray jsonArray) throws Exception {
		List<String> day_list = new ArrayList<String>();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject weather = (JSONObject) jsonArray.get(i);
			String category = (String) weather.get("category");

			// 강수확률, 강수 형태 측정값 확인
			if(category.equals("POP") || category.equals("PTY")) {

				String value = (String) weather.get("fcstValue"); // 측정값
				String day = weatherSet.getFcstDate_Desc((String) weather.get("fcstDate")); // 예보 날짜
				
				switch (category) {
					case "POP": if(60 < Integer.parseInt(value)) {day_list.add(day);} break;
					case "PTY": if(!value.equals("0")) {day_list.add(day);} break;
					default:
						break;
				}
			}
		}
		
		// list value값 중복 제거
		day_list = day_list.stream().distinct().collect(Collectors.toList());
		setRainyday_list(day_list);
	}
	
	public List<String> getRainyday_list() {
		return rainyday_list;
	}

	public void setRainyday_list(List<String> list) {
		this.rainyday_list = list;
	}

}
