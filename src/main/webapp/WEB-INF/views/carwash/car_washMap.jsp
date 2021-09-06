<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>map</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/carwash/kakaomap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/carwash/car_washMap.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content" style="height: 100%;">
	        <h3>세차장 찾기</h3>
	        <div>
	        	<img src="${pageContext.request.contextPath}/resources/img/carwash//map/naver_logo.png" style="width:10%">
	        	<i class="fas fa-plus"></i>
	        	<img src="${pageContext.request.contextPath}/resources/img/carwash/map/kakao_logo.png" style="width:10%">
	        	<div id="content_detail">
					<h2>KakaoMapAPI 위치검색과 NaverOpenAPI 네이버 블로그 검색 </h2>
					<p>	
					      주소로 좌표를 검색하고, <br>
					      해당 주소 위치를 기반으로 반경 3km 내 세차장 검색결과를 목록과 마커로 표시합니다. <br> 
					      또한 제공된 세차장 목록에 따른 네이버 블로그 검색결과를 실시간으로 제공합니다. <br>
					    * 목록과 마커에 마우스오버 하면 해당 마커의 장소명을 알수있습니다. *  
					</p>  
				</div>
	        </div>
	        <div class="box">
		        <div class="map_wrap">
		        	
					<div id="menu_wrap" class="bg_white">
						<button type="button" class="btn btn-default setCenter_btn">지도 중심좌표 이동시키기</button> 
				       <div class="option">
				            <!-- 현재 위치 검색-->
							<div class="address_box">
								<div class="form-group">   
									<input class="form-control" placeholder="${empty address?'주소':address}" name="address" id="address" type="text">
						    		<button type="button" class="btn btn-default addressSearch_btn"><i class="fas fa-search"></i></button>                               
								</div>
							</div>
				        </div>
				        <hr>
				        <ul id="placesList"></ul>
				        <div id="pagination"></div>
	    			</div>
	    			<div id="map">
	    				
	    			</div>
				</div>
				<div class="carwash_post_wrap">
					<c:choose>
						<c:when test="${!empty carwash_post}">
							<c:forEach var="carwash_post" items="${carwash_post}" varStatus="status">
								<dl>
									<dt>
										<span class="icon_box">
											<img src="${pageContext.request.contextPath}/resources/img/carwash/map/naver_blog_icon.png" style="width:100%">
										</span>
										<a href="${carwash_post.link}">${carwash_post.title}</a>
									</dt>
									<dd class="description">${carwash_post.description}</dd>
									<dd class="bloggername">${carwash_post.bloggername} 님의 블로그</dd>
								</dl>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div>검색 결과가 없습니다.</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>	
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="https://kit.fontawesome.com/58a77f3783.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=765d0d0dc978d961378d7bae25690eb9&libraries=services"></script> <!-- kakaomap api -->
	
	<script type="text/javascript">
	
	 	var latitude = '<c:out value = "${latitude}"/>'; 
	 	var longitude = '<c:out value = "${longitude}"/>'; 
	 	var address = '<c:out value = "${address}"/>'; 
	</script>
	<script src="${pageContext.request.contextPath}/resources/js/carwash_map/kakaomap.js"></script>
</body>
</html>