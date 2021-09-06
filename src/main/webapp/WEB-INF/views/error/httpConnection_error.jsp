<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>error</title>
<style type="text/css">
	
	body {
		text-align: center;
	}
	
	.wrap {
		margin: 0 auto;
    	width: 1200px;
		overflow : hidden;
		position: relative;
	}
	
	.error_img {
		width: 35%;
	    margin: 0 auto;
	    margin-top: 3%;
	    float: left;
	    padding-left: 150px;
	}
	
	.error_img img{
		width : 100%;
	}
	
	.error_msg {
		color: #6c6e6f8a;
    	position: absolute;
    	bottom: 0;
    	left: 41%;
    	text-decoration: underline;
	}
</style>
</head>
<body>
	<div class="wrap">
		<div class="error_img">
			<img src="${pageContext.request.contextPath}/resources/img/error/not_httpconnection.png">	
		</div>
		<h2 class="error_msg">서비스 오류 !!</h2>	
		
	</div>
	<div>
		<h4><a href="home">Home</a></h4>
		<h4><a href="javascript:history.back()">뒤로가기</a></h4>
	</div>
</body>
</html>