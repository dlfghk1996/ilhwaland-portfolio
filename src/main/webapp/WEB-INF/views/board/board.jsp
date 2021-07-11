<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>board</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
	
		<div id="content"> 
			<div class="container">
				<div class="row">
					<table class="table table-hover">
				 		<thead>
				 			<tr>
				 				<th>번호</th>
				 				<th>제목</th>
				 				<th>작성자</th>
				 				<th>작성일자</th>
				 				<th>조회수</th>
							</tr>
				 		</thead>
		 				<tbody>
			 				<c:choose>
				 				<c:when test="${empty boardList}">
				 					<tr>
				 						<td>게시판이 비어있습니다.</td>
				 					</tr>
				 				</c:when>
				 				<c:otherwise>
					 			 	<c:forEach var="board" items="${boardList}">
						 				<tr>
						 					<td>${board.board_num}</td>
						 					<td>
						 						<a href="boardView?board_num=${board.board_num}"> ${board.subject} </a>
						 					</td>
						 					<td>${board.writer_id}</td>
						 					<td>${board.register_date}</td>
						 					<td>${board.read_num}</td>
						 				</tr>
					 				</c:forEach>
				 				</c:otherwise>
			 				</c:choose>
						</tbody>
					</table>
					<button type="button" class="btn btn-light" onclick="location.href='boardwrite'">글쓰기</button>
					<div>${pagenation}</div>
				</div> <!-- .row endTag -->
			</div> <!-- .container endTag -->
		</div> <!-- .content endTag -->
	</div> <!-- .wrapper endTag -->


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

</body>
</html>