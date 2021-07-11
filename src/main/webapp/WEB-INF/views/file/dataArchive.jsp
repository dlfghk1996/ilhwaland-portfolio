<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">

</head>
<body>
<div class="wrapper">
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/sidebar.jsp" %>
	<div id="content">
		<div class="downloadMenu">
			<ul class="nav nav-tabs">
				<li><a href="csvDownload" class="btn btn-primary">csv</a></li>
    			<li><a href="excelDownload" class="btn btn-primary">excel</a></li>
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

<script type="text/javascript">

</script>
</body>
</html>
	