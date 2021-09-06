<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>500_error</title>
<style type="text/css">
	body {
		text-align: center;
	}
	.img_box {
		width: 400px;
    	padding-top: 30px;
    	margin: 0 auto;
    }
</style>
</head>
<body>
	<div class="img_box">
		<img src="${pageContext.request.contextPath}/resources/img/error/500_error.png" style="width: 100%;">
	</div>	
	<h2>Internal Server Error !!!  Oops, something went wrong.</h2>
	<div>
		<h4><a href="home">Home</a></h4>
		<h4><a href="javascript:history.back()">뒤로가기</a></h4>
	</div>
</body>
</html>