package com.kim.ilhwaland.helper.weather;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;


@Component
public class WeatherSet {
	static String weatherCode = ""; // 날씨 메뉴에 따른 발표시간을 설정하기 위한,분기처리 값 ( value = 동네예보 or 초단기 예보) 
	private String sky; 			// 하늘상태
	private String pty; 			// 강수
	private String weatherMsg;      // 하늘상태와 강수 값에 따른 메세지
	private String base_date; 		// 발표 날짜
	private String base_time; 		// 발표 시간
	private String fcstTime;  		// 예보 시간
	private String fcstTime_Desc;   // 초단기 예보조회 예보 시간 format 변경 (00:00)
	private String fcstDate_Desc;   // 동네 예보 예보 날짜 format 변경 (요일)
	
	Calendar cal = Calendar.getInstance();              // Default Calendar 객체
	private int min = cal.get(Calendar.MINUTE);; 		// 현재 분 
	private int hour = cal.get(Calendar.HOUR_OF_DAY); 	// 현재 시간
	private String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 측정 시간, 초기화 값
	
	// 생성자
	public WeatherSet() {}
	
	
	/** 초기 base_time & base_date 설정  **/
	public void set_standardDate() {
		
		/** Calendar 객체의 시간과 날짜를 각 날씨 메뉴의 발표시간/날짜, 예보시간/날짜 에 맞게 변경했기때문에 
		 * set_standardDate() 메서드가 호출될때 마다, 기본값으로 초기화 한다. */ 
		cal.clear(Calendar.HOUR_OF_DAY); // 특정 시간으로 지정한 것  초기화
		base_date = today; 				 // 특정 날짜로 지정한 것  초기화
	
		// 동네예보 발표시간 설정 
		if(weatherCode.equals("vilageFcst")) { 
			if(this.hour == 01 || this.hour== 00 ) {
				// base_date : 1일전날
				base_date = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")); 
				// base_time : 2300
				cal.set(Calendar.HOUR_OF_DAY,23); 
			}else {
				// ex) 현재 시에서 호출할수 있는 발표시간을 계산한다. => 18 -(18+1) % 3 = 17
				cal.set(Calendar.HOUR_OF_DAY, hour - (hour + 1) % 3);
			}
			
		// 초단기 예보조회 &초단기 실황 조회  발표시간 설정 
		} else {
			//  - 30분 이전이면 현재 시보다 1시간 전 'base_time' 을 요청한다 => ( ex. 0014 -> 0013)
			// 	- 30분 이후면 현재 시와 같은 'base_time' 을 요청한다.
			if (min <= 30) {
				// 단. 00:30분 이전이라면 'base_date'는 전날이고 'base_time'은 2300이다.
				if (hour == 0) {
					base_date = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
					cal.set(Calendar.HOUR_OF_DAY, 23);
				}else {
					cal.set(Calendar.HOUR_OF_DAY, hour-1); // 1시간 전
				}
			}
		}
	}
	
	/** @USED 동네예보, 초단기 예보, 실시간 예보
	 * 요청할 발표시간 설정 
	 * */
	public String getBase_time() {
		
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		// 발표시간 format (0100 OR 1200)
		base_time = hh < 10 ? "0"+hh+"00" : hh+"00";
		// 초단기 예보조회일 경우 format 변경
		if(weatherCode.equals("ultraSrtFcst")) {
			StringBuffer sb = new StringBuffer(base_time);
			base_time = sb.replace(2, base_time.length(), "30" ).toString();
		}
		return base_time;
	}
	
	public String getBase_date() {
		return this.base_date;
	}
	
	/** @USED 동네예보
	 * 예보 시간  => 발표시간으로부터 4 시간후 예보시간에대한 측정값
	 * */
	public String getFcstTime() {
		
		// (ex.발표시간 : 1400 => 14 추출)
		int hh = Integer.parseInt(this.base_time.substring(0,2)); 
		
		// set_standardDate() 메서드에서 설정한 발표시간에 4시간을 더한다.
		cal.set(Calendar.HOUR_OF_DAY, hh+4); 
		hh = cal.get(Calendar.HOUR_OF_DAY); 
		fcstTime = hh < 10 ? "0"+hh+"00" : hh+"00";
		
		return fcstTime;
	}
	
	/** 강수 형태 수치 문자화 */
	public String getPty(String pty) {
        switch (pty) {
	        case "1": case "4": case "5":
	            weatherMsg = "rain";
	            break;
	        case "3": case "7":
	        	weatherMsg = "snow";
	            break;
	        case "2": case "6":
	        	weatherMsg = "sleet";
	            break;
	        default:
	        	weatherMsg = "0";
	        	break;
	        }
        
		return weatherMsg;
	}
	
	/** 하늘 상태 수치 문자 화 */
	public String getSky(String sky) {
		 switch (sky) {
		 	case "1":
		 		weatherMsg = "맑음";
		 		break;
	        case "3": 
	        	weatherMsg = "구름 많음";
	            break;
	        case "4":
	        	weatherMsg = "흐림";
	        default:break;
	        }
		 
		return weatherMsg;
	}
	
	/** @USED 초단기 예보조회
	 *  - 예보 시간를 형식 변환
	 *  - 0000 => 00:00 am/pm
	 *  */
	public String getFcstTime_Desc(String fcstTime_Desc) throws Exception {
		try {
			// DateFormat
			DateFormat dateFormat = new SimpleDateFormat("HHmm");
			// String => Date Type 으로 형변환
			Date dates = dateFormat.parse(fcstTime_Desc);
			this.fcstTime_Desc = new SimpleDateFormat("HH:mm a").format(dates);
			
		} catch (ParseException e) {
			System.out.println(e.getLocalizedMessage());
			throw new RuntimeException(e.getMessage());
		} 
		return this.fcstTime_Desc;
	}
	
	/** @USED 동네 예보조회
	 *  - 예보 시간을 요일로 형식 변환
	 *  - 210821 => ?요일
	 *  */
	public String getFcstDate_Desc(String fcstDate_Desc) throws Exception {
		try {
			// DateFormat
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			// String => Date Type 으로 형변환
			Date dates = dateFormat.parse(fcstDate_Desc); 

			// Calendar 객체 생성 
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dates);
			
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			switch(day) { 
				case 1: this.fcstDate_Desc="일"; break; 
				case 2: this.fcstDate_Desc="월"; break; 
				case 3: this.fcstDate_Desc="화"; break; 
				case 4: this.fcstDate_Desc="수"; break; 
				case 5: this.fcstDate_Desc="목"; break; 
				case 6: this.fcstDate_Desc="금"; break; 
				case 7: this.fcstDate_Desc="토"; break; 
			}
		} catch (ParseException e) {
			System.out.println(e.getLocalizedMessage());
			throw new RuntimeException("[WeatherSet_getFcstDate_Desc()] ParseException 에러");
		} 
		return this.fcstDate_Desc;
	}
	
	/** setter */
	public void setBase_date(String base_date) {
		this.base_date = base_date;
	}
	
	public void setBase_time(String base_time) {
		this.base_time = base_time;
	}
	
	public void setFcstTime(String fcstTime) {
		this.fcstTime = fcstTime;
	}
	
	public void setPty(String pty) {
		this.pty = pty;
	}
	
	public void setSky(String sky) {
		this.sky = sky;
	}

	public void setFcstTime_Desc(String fcstTime_Desc) {
		this.fcstTime_Desc = fcstTime_Desc;
	}
	
	public void setFcstDate_Desc(String fcstDate_Desc) {
		this.fcstDate_Desc = fcstDate_Desc;
	}
}
