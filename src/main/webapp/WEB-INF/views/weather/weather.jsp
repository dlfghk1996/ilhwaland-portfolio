<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>weather</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/weather/weather.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content" style="padding:0; height: 100%;">
			<div class="content_detail_box">
	        	<div id="content_detail">
					<h3>공공데이터 open API 를 이용하여 기상청 데이터 수집 및 활용 </h3>
					<h4>사용자가 요청한 주소의 날씨정보를 제공하고, 날씨에 따른 세차 알림 서비스</h4>
					<ul>
						<li><i class="far fa-check-square"></i>제공 데이터 : 동네예보, 실시간 날씨예보, 초단기 예보</li>
						<li>
							<i class="far fa-check-square"></i> 주소 기능 <br>
							<div class="address_api">
								<div><i class="fas fa-square"></i> 다음 주소 API를 이용하여 사용자가 원하는 주소를 직접 검색 할수 있습니다.</div>
								<div><i class="fas fa-square"></i> Geolocation API를 이용하여 사용자의 현재 위치 정보 추출 합니다.</div> 
								<div><i class="fas fa-square"></i> Kakaomap API를 이용하여 사용자로부터 받은 주소를 위도, 경도 좌표로 변환하여 서버로 전달합니다.</div>
							</div>
						</li>
						<li><i class="far fa-check-square"></i>발표 시각에 맞춰서 날씨 예보 조회 서비스를 요청하여 데이터를 응답받고, 가공하여 알아보기 쉬운 UI 로 표현합니다.</li> 
						<li><i class="far fa-check-square"></i>주간 날씨예보 데이터 중 비오는 확률과 강수형태 값을 일정수치를 기준으로 분석하여 세차 알림서비스를 제공합니다.</li>
					</ul>
				</div>
	        </div>
	        
			
			<!-- 동네예보에서 측정한 오늘 하늘상태를  실시간 날씨에 표시하기 위해  'todayWeather'변수에 해당 값을 초기값으로 대입한다. -->
			<c:forEach var="map" items="${vilageFcstMap}" varStatus="status">
				<c:if test="${status.first}">
					<c:set var="todayWeather" value="${map.value}"/>
				</c:if>
			</c:forEach>
			
			<c:set var="sky" value="${ultraSrtNcstMap['PTY'] ne '0' ? ultraSrtNcstMap['PTY'] : todayWeather['skyImg']}"/>
			
			<!-- 날씨 출력  -->
			<div class="weather_container" style="background-image:url('${pageContext.request.contextPath}/resources/img/weather/${sky}.jpg')">
				<!-- 오늘 날씨 -->
				<div class="todayWeather_box">
				
					<!-- 현재 위치 검색-->
					<div class="address_box">
						<form id="address_form" action="weather" method="POST">
							<div class="form-group">             
								<button type="button" class="btn btn-secondary getGeolocation_btn">현재 내 위치<i class="far fa-compass"></i></button>   
								<input class="form-control" placeholder="${empty address?'주소':address}" name="address" id="address" type="text" readonly>
								<input type="hidden" name="longitude">
								<input type="hidden" name="latitude">
				    			<button type="button" class="btn btn-default getAddress_btn">주소 검색<i class="fas fa-search"></i></button>                               
							</div>
						</form>
					</div>
				
					<!-- 실시간 날씨 -->
					<div class="todayWeather">
						<!-- 오늘 날짜  -->
						<div>Today <fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd hh:mm:ss" /></div>
						<div class ="today_img">
							<img src="${pageContext.request.contextPath}/resources/img/weather/icon/${sky}_icon.png" style="width: 100%">		
						</div>
						<div>
							<div class="today_temp">${ultraSrtNcstMap['T1H']} °C</div>
							<div class="today_weather_etc ">
								<div class="humidifier_img"><img src="${pageContext.request.contextPath}/resources/img/weather/icon/humidifier_icon.png" style="width: 100%">	</div>
								<div class="humidifier_value">습도 : ${ultraSrtNcstMap['REH']} %</div>
							</div>
						</div>
					</div>
				
					<!-- 오늘 날씨 시간별 예보 -->
					<div class="timelineWeather_box"> 
						<div class="timeline_slides_prev off">이전</div>
						<div>
							<ul>
								<c:forEach var="items" items="${ultraSrtFcstMap}">
									<li class="timelineWeather">
										<h3>${items.key}</h3>
										<ul class="timelineValue">
											<c:set var="timelineWeather" value="${items.value}"/>
											<li> 
												<c:set var="sky" value="${timelineWeather['PTY'] ne '0' ? timelineWeather['PTY'] : timelineWeather['skyImg']}"/>
												<img src="${pageContext.request.contextPath}/resources/img/weather/icon/${sky}_icon.png" class="${sky}">		
												<span>${timelineWeather['T1H']} °C	 ${timelineWeather['SKY']}</span>					
											</li>
											<li>1시간 강수량:  ${timelineWeather['RN1']}mm</li>
											<li>습도 : ${timelineWeather['REH']} %</li>
			 							</ul>
									</li>
								</c:forEach>
							</ul>
						</div>
						<div class="timeline_slides_next off">다음</div>
					</div>	
					<div class="weekelyWeather_title">
						<div>주간 날씨</div>
						<p>* 날씨 발표 시간으로부터 4시간 후의 날씨를 제공합니다.*</p>
					</div>
				</div> <!-- todayWeather_box end tag -->		
					
				<div class="clear"></div>
				
				<!-- 주간 날씨 -->
				<div class="weekelyWeather_box" style="background-image:url('${pageContext.request.contextPath}/resources/img/weather/weekelyWeather_back.jpg')">
					<!-- 오늘 요일 -->
					<c:set var="now" value="<%=new java.util.Date()%>"/>
					<fmt:formatDate value="${now}" pattern="E" var="today"/>
					<ul>
						<c:forEach var="items" items="${vilageFcstMap}">
							<li style="width : calc(100% / ${fn:length(vilageFcstMap)});">
								<div class="<c:if test="${today eq items.key}">today</c:if> day_of_week">${items.key} 요일</div>
								<div class="weekelyWeatherValue">
									<c:set var="weekelyWeather" value="${items.value}"/>
									<div>
										<div class="weekelyWeather_img_box">
											<c:set var="sky" value="${weekelyWeather['PTY'] ne '0' ? weekelyWeather['PTY'] : weekelyWeather['skyImg']}"/>
											<img src="${pageContext.request.contextPath}/resources/img/weather/icon/${sky}_icon.png" style="width: 100%">		
										</div>
									</div>
									<ul>
										<c:choose>
         									<c:when test="${ empty weekelyWeather['TMX'] || empty weekelyWeather['TMN'] }">
         										<li>${weekelyWeather['T3H']} °C </li>
         									</c:when>
         									<c:otherwise>
									          	<li>${weekelyWeather['TMX']} °C /  ${weekelyWeather['TMN']} °C</li>
									         </c:otherwise>
      									</c:choose>
      									<li>강수 확률 : ${weekelyWeather['POP']} %</li>
										<li>습도 : ${weekelyWeather['REH']} %</li>
									</ul>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div> <!-- .weather_container endTag -->	
		</div> <!-- .content endTag -->	
	</div><!-- .wrapper endTag -->			


<script src="https://code.jquery.com/jquery-3.6.0.js"></script>	
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
<script src="https://kit.fontawesome.com/58a77f3783.js"></script>
<script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b4f0d14c9368a92c0a3c59177f077075&libraries=services,clusterer,drawing"></script> <!-- kakaoMap API -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script> <!-- 주소찾기 daum API -->
<script src="${pageContext.request.contextPath}/resources/js/weather/weather.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		
		// 페이지가 로드되면 세차장 안내 팝업창 open.
		showWeatherServicePopup();
		
		// 해당 위치의 날씨를 기반으로 한 세차 추천 팝업
		function showWeatherServicePopup(){ 
			var popup = window.open('weatherService_popup','weatherService','width=470, height=490, top=10, left=10');
			if(!popup){
				alert('팝업창을 허용해주세요.');
			}else{
				popup.close();
				window.open('weatherService_popup','weatherService','width=470, height=490, top=10, left=10');
			}
		}
		
		// 주소찾기 API
		$('.getAddress_btn').click(function(){
			new daum.Postcode({
				// 여러개의 팝업창이 뜨는 것을 방지하기 위해 팝업창의 Key값을 지정
				popupKey: 'popup1', 
		        oncomplete: function(data) {
		        	// 팝업에서 검색결과 항목을 클릭했을시 실행
		          	// 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
		          	var addr='';
		            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
		                addr = data.roadAddress;
		            	
		            } else { 			
		            	addr = data.jibunAddress;
		            }
		
		            $('[name=address]').val(addr);
		            getGeocode(addr);
		        }
		    }).open();
		})
		
		// 주소  => 위도,경도 좌표로 변환  
		function getGeocode(addr) {
	     	// 좌표 변환 객체 생성
			var geocoder = new kakao.maps.services.Geocoder();
			// 주소로 좌표를 검색 
			geocoder.addressSearch(addr, function(result, status) {
				var coords = new kakao.maps.LatLng(result[0].y, result[0].x); 
				xx = result[0].x; // 위도 latitude
				yy = result[0].y; // 경도 longitude
				
				$('[name=longitude]').val(xx);
				$('[name=latitude]').val(yy);
				$('#address_form').submit();
			})   
		}
		
		// 사용자의 현재 위치 정보 가져오기 (geolocation API)
		$('.getGeolocation_btn').click(function(){
			// HTML5의 geolocation으로 사용할 수 있는지 확인
			if (navigator.geolocation) {
				// GeoLocation을 이용해서 접속 위치를 얻어온다
				navigator.geolocation.getCurrentPosition(showPosition, showError);
			} else {
				swal("현재 웹 브라우저가 geolocation을 지원하지 않습니다.");
			}
		})
		
		// geolocation 성공 처리
		function showPosition(position) {
			var latitude = position.coords.latitude;  // 위도
			var longitude = position.coords.longitude; // 경도
			
			$('[name=longitude]').val(longitude);
			$('[name=latitude]').val(latitude);
			$('#address_form').submit();
			
		}
		
		// geolocation 에러 처리
		function showError(error) {
			switch(error.code) {
			    case error.PERMISSION_DENIED:
			    	swal('현재위치 사용 요청을 허용해주세요!');
			      	break;
			    case error.POSITION_UNAVAILABLE:
			    	swal('해당 위치정보를 사용할 수 없습니다.');
			      	break;
			    case error.TIMEOUT:
			    	swal(' 위치 정보를 가져오기 위한 요청이 허용 시간을 초과했습니다.');
			      	break;
			    case error.UNKNOWN_ERROR:
			    	swal('알 수 없는 오류가 발생했습니다.');
			      	break;
			  }
		}	
		
	 });
</script>
</body>
</html>