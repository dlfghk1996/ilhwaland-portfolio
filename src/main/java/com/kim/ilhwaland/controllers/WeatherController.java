package com.kim.ilhwaland.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kim.ilhwaland.dto.Carwash_post;
import com.kim.ilhwaland.helper.weather.CarwashHelper;
import com.kim.ilhwaland.helper.weather.WeatherHelper;


@Controller
public class WeatherController {
	
	@Autowired
	private WeatherHelper weatherHelper;
	
	@Autowired
	private CarwashHelper carwashHelper;
	
	
	/** [VIEW] 세차 추천 팝업창  */
	@RequestMapping(value = "weatherService_popup")
	public String weatherService_popup(Model model) {
		// 비오는 요일 리스트를 view에 반환 한다.
		model.addAttribute("rainyday_list", carwashHelper.getRainyday_list()); 
	
		return "carwash/carwash_popup";  
	}
	
	
	/** [Method 01] 내 위치 기반 날씨 알아보기 */
	@RequestMapping(value = "weather")
	public String weather(@RequestParam(required = false) Map<String, String> addressMap, Model model) throws Exception {
		
		// 사용자에게 요정받은 주소에서 추출한 위도와 경도
		if(addressMap.size() != 0) {
			WeatherHelper.longitude = (String)addressMap.get("longitude"); // 경도 (가로선)
			WeatherHelper.latitude = (String)addressMap.get("latitude");   // 위도 (세로선)
		}
		
		// 위도,경도를 격자좌표로 변환
		weatherHelper.getGridXY();
		
		/** 초단기실황조회 : : 매시간 30분마다 발표 10분마다 최신 정보로 업데이트 **/
		Map<String, String> ultraSrtNcstMap = weatherHelper.getUltraSrtNcst("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst"); 
		
		/** 초단기 예보조회 : 1시간씩 6시간 기상을 예보  **/
		Map<String, Map<String, String>> ultraSrtFcstMap = weatherHelper.getUltraSrtFcst("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst"); 
		
		/** 동네예보 : 최대 3일 최소 2일간의 기상을 예보 **/
		Map<String, Map<String,String>> vilageFcstMap = weatherHelper.getNationwideWeather("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst");
		
		/** 경도 & 위도 => 주소로 변환해서 사용자에게 제공 */
		String address = weatherHelper.convertGeolocation();
		
		model.addAttribute("address", address); 				// 사용자가 검색한 주소
		model.addAttribute("ultraSrtNcstMap", ultraSrtNcstMap); // 초단기실황조회
		model.addAttribute("ultraSrtFcstMap", ultraSrtFcstMap); // 초단기 예보조회
		model.addAttribute("vilageFcstMap", vilageFcstMap);     // 동네예보
		
		return "weather/weather";  
	}
	

	/** [Method 02] 내 위치의 날씨 기반 세차장 찾기 */
	@RequestMapping(value = "car_washMap")
	public String getMap(Model model) throws Exception {
		/* 좌표  => 주소 변환(ex.서울특별시 종로구 사직동)
		*  - 날씨 검색할때 사용한 주소
		*  - 사용자가 직접 입력한 주소 */
		String address;
		address = weatherHelper.convertGeolocation(); 
		List<Carwash_post> carwash_post = carwashHelper.getCarwash_Search(address);
		
		model.addAttribute("carwash_post", carwash_post); 
		model.addAttribute("address", address);                   // 날씨 검색 시점에서 사용자가 요청한 주소
		model.addAttribute("latitude", WeatherHelper.latitude);   // 위도
		model.addAttribute("longitude", WeatherHelper.longitude); // 경도
		
		return "carwash/car_washMap";  
	}
	
	/** [Method 03] AJAX Ver.내 위치의 날씨 기반 세차장 찾기 */
	@RequestMapping(value = "doCrawling", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> doCrawling(@RequestParam(name="address") String address) throws Exception {
		
		List<Carwash_post> carwash_post = new ArrayList<Carwash_post>();
		carwash_post = carwashHelper.getCarwash_Search(address);
		
		return new ResponseEntity<List<Carwash_post>>(carwash_post,HttpStatus.OK);
	}
	
}
