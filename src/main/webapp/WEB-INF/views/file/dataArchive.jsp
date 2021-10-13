<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
</head>
<body>
<div class="wrapper">
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/sidebar.jsp" %>
	<div id="content">
		<div id="content_detail">
			<h3><b>DB 데이터를  XLSX, CSV 파일로 내보내기</b></h3>
			<ul>
				<li><i class="far fa-check-square"></i> 라이브러리 : Apache POI, Open CSV</li>
				<li>
					<i class="far fa-check-square"></i> 
					<span>XLXS : Reflection을 이용하여, 렌더링 할 클래스의 변수와 어노테이션에 접근하여 <br>
      						     value와 헤더에 들어갈 이름을 추출하고, Apachi POI의 workbook 객체와 Sheet 객체를 이용하여 XLXS 파일을 생성하고 내보낸다.</span>
				</li>
				<li>
					<i class="far fa-check-square"></i>
					<span>CSV : 고유한 매핑 전략을 사용하여 Annotation 으로 정의한 열 이름과 순서를 적용하여 CSV 를 생성하고 내보낸다.</span>
				</li>
			</ul> 
		</div>
		<div class="downloadMenu">
			<ul class="nav nav-tabs">
				<li><a href="csvDownload" class="btn btn-primary">CSV</a></li>
    			<li><a href="excelDownload" class="btn btn-primary">XLSX</a></li>
			</ul>
		</div>
		<div id="uploadList">
			<h3>UploadList</h3>
			<table class="table table-bordered table-hover">
				<thead>
					<tr>
						<th>번호</th>
						<th>파일명</th>
						<th>파일 확장자</th>
						<th>등록일</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="fileDetail" items="${fileList}">
						<tr>
							<td>${fileDetail.file_num}</td>
							<td>${fileDetail.original_name}</td>
							<td>${fileDetail.filetype}</td>
							<td>${fileDetail.register_date}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div> <!-- .content endTag  -->
</div> <!-- JSON.stringify .wrapper endTag -->



<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/58a77f3783.js"></script>

</body>
</html>
	