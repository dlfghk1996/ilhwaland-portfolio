<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>popup</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/carwash/carwash_popup.css">

</head>
<body>
<div class="wrap" style="background-image:url('${pageContext.request.contextPath}/resources/img/carwash/carwash_bg.jpg')">
	<div class="popup_box">
		<div class="box">
			<div class="carwash_info">
				<div class="group">
					<c:choose>
						<c:when test="${empty rainyday_list}">
							<div class="title">오늘은 세차하기 좋은 날</div>
							<img src="${pageContext.request.contextPath}/resources/img/carwash/carwash.png" style="width: 100%">		
						</c:when>
						<c:otherwise>
							<div class="title">
								<div style="position: relative;">
									<c:forEach items="${rainyday_list}" var="list">
									[ ${list} ] 
									</c:forEach>
								</div>
							    <div>요일에  비소식이 있습니다.</div>
							</div>
							<div class="speech-bubble_box">
								<div class="speech-bubble">세차는 좀...</div>
								<img src="${pageContext.request.contextPath}/resources/img/carwash/sadCar.png" style="width: 100%">
							</div>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="group">
					<button class="button">내 주변 세차장 찾기 <i class="fas fa-angle-double-right"></i></button>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>	
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>	
<script src="https://kit.fontawesome.com/58a77f3783.js"></script>
<script type="text/javascript">
	$(function(){
		$('.button').click(function(){
			window.opener.top.location.href='car_washMap';
	        window.close();	
		})
	})

</script>
</body>
</html>