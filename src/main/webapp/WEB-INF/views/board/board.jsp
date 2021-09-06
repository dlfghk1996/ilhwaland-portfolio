<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>board</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/board.css">
<style type="text/css">

</style>
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
	
		<div id="content"> 
			<div class="box">
				<div class="row">
					<h2 style="font-weight: bold;">자유 게시판</h2>
					
					<!-- 게시판 검색 form-->
					<div class="search_form">
						<form method="get" action="board">
							<select name="search_option" class="selectpicker form-control">
        						<option value="writer_id"<c:if test="${board.search_option == 'writer_id'}">selected</c:if>>작성자</option>
								<option value="subject" <c:if test="${board.search_option == 'subject'}">selected</c:if>>제목</option>
								<option value="content" <c:if test="${board.search_option == 'content'}">selected</c:if>>내용</option>
								<option value="all" <c:if test="${board.search_option == 'all'}">selected</c:if>>작성자+내용+제목</option>
    						</select>
							<input type="search" name="keyword" id="keyword" class="form-control" value="${board.keyword}">
							<button type="submit" class="btn btn-primary">검색</button>
						</form>
					</div>
					
					<table class="table table-hover">
				 		<thead class="thead">
				 			<tr>
				 				<th>번호</th>
				 				<th>제목</th>
				 				<th>작성자</th>
				 				<th>작성일자</th>
				 				<th>조회수</th>
							</tr>
				 		</thead>
		 				<tbody class="tbody">
			 				<c:choose>
				 				<c:when test="${empty boardList}">
				 					<tr>
				 						<td>게시판이 비어있습니다.</td>
				 					</tr>
				 				</c:when>
				 				<c:otherwise>
					 			 	<c:forEach var="board_list" items="${boardList}">
					 			 		<c:set var = "writer_id" value="${board_list.writer_id}"/>
					 			 		<c:set var = "subject" value="${board_list.subject}"/>
					 			 		
					 			 		<!-- 검색어가 있다면, if 문 진입 -->
					 			 		<c:if test="${ board.keyword != ''}">
					 			 			<!-- 검색어에 <mark> 태그를 적용하여 형광팬 효과 준비 -->
					 			 			<c:set var = "mark" value="<mark>${board.keyword}</mark>"/>
					 			 			<!-- 검색어와 일치하는 단어를 형광팬 효과로 변경 -->
					 			 			<c:set var = "writer_id" value="${fn:replace(writer_id, board.keyword, mark)}"/>
					 			 			<c:set var = "subject" value="${fn:replace(subject, board.keyword, mark)}"/>
					 			 		</c:if>
						 				<tr>
						 					<td>${board_list.rnum}</td>
						 					<td style="width: 70%;">
						 						<a href="boardView?board_num=${board_list.board_num}"> ${subject} </a>
						 					</td>
						 					<td>${writer_id}</td>
						 					<td>${board_list.register_date}</td>
						 					<td>${board_list.read_num}</td>
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
</body>
</html>