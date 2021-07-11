<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>sidebar</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/index.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
</head>
<body>
   	<!-- Sidebar -->
   	<nav id="sidebar">
       	<div class="sidebar-header">
           	<h3>ILHWA LAND</h3>
       	</div>

        <ul class="list-unstyled components">
            <p>MEMU</p>
            <li class="active">
                <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">File</a>
                <ul class="collapse list-unstyled" id="homeSubmenu">
                    <li>
                        <a href="dataArchive">자료실</a>
                    </li>
                    <li>
                        <a href="fileBoard">fileBoard</a>
                    </li>
                    <li>
                        <a href="fileConvertPage">파일변환프로그램</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#apiSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">About API</a>
                <ul class="collapse list-unstyled" id="apiSubmenu">
                    <li>
                        <a href="#">버스 시간 알아보기</a>
                    </li>
                    <li>
                        <a href="weatherPage">오늘의 날씨</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="board">Board</a>
            </li>
        </ul>
  		</nav>


<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript">

</script>
</body>
</html>