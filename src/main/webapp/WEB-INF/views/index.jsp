<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>home</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/index.css">
</head>
<body>
	<%@ include file="include/header.jsp" %>
	<%@ include file="include/sidebar.jsp" %>
	<div class="boxwrap">
		<div class="box">
			<div class="banner">
				<img src="${pageContext.request.contextPath}/resources/img/index/banner-dec-left.png" style="width: 100%">
			</div>
			<div style="width: 100%; height: 100%;">
				<h2>메인 기능</h2>
				
				<div class="content">
						<!-- menu 1-->
						<div class="menu_box1">
							<div class="item">
								<section class="home">
									<div class="row features_box">
										<div class="col-md-12">
											<div class="row">
											
											    <!-- 기능 1 -->
												<div class="features">
													<div class="features-item">
														<div class="first-number number">
															<h6>01</h6>
														</div>
														<div class="icon">
															<img src="${pageContext.request.contextPath}/resources/img/index/index_file_icon.png" style="width: 100%">		
														</div>
														<div class="features-content">
															<h4>파일</h4>
															<ul>
																<li><b>라이브러리 : 아파치 POI, openCSV, itextPDF</b></li>
																<li>Excel 가져오기 및 내보내기</li>
																<li>CSV 가져오기 및 내보내기</li>
																<li>PDF 및 다른 파일 형식으로 변환</li>
																<li>웹 뷰어를 통한 미리보기 기능</li>
															</ul>
														</div>
													</div>
												</div>
												
												<!-- 기능 2 -->
												<div class="features">
													<div class="features-item">
														<div class="first-number number">
															<h6>02</h6>
														</div>
														<div class="icon">
															<img src="${pageContext.request.contextPath}/resources/img/index/index_crawler_icon.png" style="width: 100%">
														</div>
														<div class="features-content">
															<h4>크롤링 with 파이썬</h4>
															<ul>	
																<li><b>개발언어 : 파이썬 </b></li>
																<li><b>프레임워크 : Django</b></li>
																<li><b>라이브러리 : Beautifulsoup, WordCloud</b></li>
																<li>실시간으로 언론사별 IT 뉴스 추출</li>
																<li>감성분석 및 워드클라우드 생성</li>
															</ul>
														</div>
													</div>
												</div>
												
												<!-- 기능 3 -->
												<div class="features">
													<div class="features-item">
														<div class="first-number number">
															<h6>03</h6>
														</div>
														<div class="icon">
															<img src="${pageContext.request.contextPath}/resources/img/index/index_scheduler_icon.png" style="width: 100%">
														</div>
														<div class="features-content">
															<h4>스케줄러</h4>
															<ul>	
																<li><b>API : Full Calendar</b></li>
																<li><b>방식 : REST +  Spring Data JPA</b></li>
																<li>팝업을 통한 일정 등록/보기/수정/삭제 </li>
																<li>카테고리 등록/수정/삭제 기능</li>
																<li>카테고리 별 색상 등록 및 일정 필터링</li>
															</ul>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</section>
							</div>
						</div>
					
						<!-- menu 2 -->
						<div class="menu_box2">
							<div class="item">
								<section class="home">
									<div class="row">
										<div class="col-md-12">
											<div class="row">
											
												<!-- 기능 4 -->
												<div class="features">
													<div class="features-item">
														<div class="first-number number">
															<h6>04</h6>
														</div>
														<div class="icon">
															<img src="${pageContext.request.contextPath}/resources/img/index/index_weather_icon.png" style="width: 100%">		
														</div>
														<div class="features-content">
															<h4>날씨 + 다양한 openApi 활용</h4>
															<ul>
																<li><b>API : 기상청 API, KaKaoMaps,다음 주소 </b></li>
																<li>지번, 도로명 찾기  & 사용자의 위치 제공</li>
																<li>현재 위치 정보 기반 실시간 날씨 제공</li>
																<li>날씨 데이터를 이용한 세차장 추천 서비스</li>
															</ul>
														</div>
													</div>
												</div>
												
												<!-- 기능 5 -->
												<div class="features">
													<div class="features-item">
														<div class="first-number number">
															<h6>05</h6>
														</div>
														<div class="icon">
															<img src="${pageContext.request.contextPath}/resources/img/index/index_map_icon.jpg" style="width: 100%">	
														</div>
														<div class="features-content">
															<h4>키워드 장소검색 및 네이버 블로그 검색</h4>
															<ul>	
																<li>
																	<div><b>API : KaKaoMaps & Naver open api</b></div>
																</li>
																<li>키워드 검색 및 드래그로 세차장 검색</li>
																<li>마커 및 목록으로 장소 표시</li>
																<li>키워드에 관한 네이버 블로그 결과를 제공</li>
															</ul>
														</div>
													</div>
												</div>
												
												<!-- 기능 6 -->
												<div class="features">
													<div class="features-item">
														<div class="first-number number">
															<h6>06</h6>
														</div>
														<div class="icon">
															<img src="${pageContext.request.contextPath}/resources/img/index/index_board_icon.png" style="width: 100%">
														</div>
														<div class="features-content">
															<h4>게시판</h4>
															<ul>	
																<li><b>오픈소스 : 썸머노트 에디터</b></li>
																<li>게시판 기본기능  CRUD 작업 적용</li>
																<li>댓글 등록,수정,삭제</li>
																<li>검색 옵션 또는 키워드로 게시글 검색 기능</li>
																<li>검색 키워드 하이라이트 표시</li>
															</ul>
															
													</div>
												</div>
											
											</div>
										</div>
									</div>
								</section>
							</div>
						</div>  <!-- .tab-menu endtag-->
					</div> <!-- .tab-content endtag-->
				</div>
				
			</div> <!-- wrap endtag -->
		</div> <!-- box endtag -->
	</div> <!-- .boxwrap endtag -->
	
	
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>	
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
</body>
</html>