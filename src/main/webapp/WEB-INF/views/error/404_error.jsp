<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>404_error</title>
<style type="text/css">
.error_img {
	width: 60%;
    margin: 0 auto;
    margin-top: 3%;
}
.error_img img{
	width : 100%;
}
</style>
</head>
<body>	
	<div class="error_img">
		<img src="${pageContext.request.contextPath}/resources/img/error/404_error.png">	
	</div>
	<div>
		<h3><a href="home">Home</a></h3>
		<h3><a href="javascript:history.back()">뒤로가기</a></h3>
	</div>
</body>
</html>