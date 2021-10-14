<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>boardView</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/boardView.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content"> 
			<div class="container">
				<div class="content_wrap">
					<h1>자유게시판</h1>
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 style="padding-bottom: 20px;">${board.subject}</h3>
				 			<div class="contentInfo1">
				 				<span> 작성자  ${board.writer}</span>
				 				<span> &nbsp; | &nbsp; ${board.register_date}</span>
				 			</div>
				 			<div class="contentInfo2">
				 				조회  &nbsp;&nbsp;${board.read_num}
				 			</div>
						</div>		 
				 		<!-- 글 내용 -->
      			 		<div class="panel-body">
      			 			<p>${board.content}</p>	
      			 		</div>
      			 		<div class="panel-footer">
      			 			<button type="button" class="btn btn-primary boardPasswordCheckModal_open_btn" data-toggle="modal" data-target="#boardPasswordCheck_modal" value="update">수정</button>
      			 			<button type="button" class="btn btn-danger boardPasswordCheckModal_open_btn" data-toggle="modal" data-target="#boardPasswordCheck_modal" value="delete">삭제</button>
      			 		</div>
					</div>
				  
				  	<!-- 댓글 -->
				  	<div class="reply_form_wrap">
						<form id="reply_form" action="replyWrite" method="GET">
							<input type="hidden" value="${board.board_num}" name="board_num">
							<div class="form-group" style="overflow: hidden;">
								<div class="form-inline">
									<label for="reply_writer_nickname">닉네임 :</label>
					      			<input type="text" class="form-control" id="reply_writer_nickname" placeholder="닉네임" name="reply_writer_nickname" required>
								</div>
								<div class="form-inline">
									<label for="reply_password">Password:</label>
					      			<input type="password" class="form-control" id="reply_password" placeholder="password" name="reply_password" required>
						   		</div>
						   	</div>
							<div class="form-group comment">
					  			<label for="reply">Comment:</label>
					  			<div>
					  				<textarea class="form-control" rows="3" cols="30" id="reply" name="reply" placeholder="댓글을 입력하세요" required></textarea>
					  				<button type="submit" class="btn btn-default" id="reply_btn">등록</button>
					  			</div>
							</div>
						</form>
					</div>
					
					<!-- 댓글 목록 -->
					<div id="reply_container">
						<table class="reply_table table table-dark">
							<c:forEach var="boardReply" items="${boardReplyList}">
								<tr>
									<td class="reply_icon"><i class="far fa-comment-dots"></i></td>
				 					<td class="nickname_${boardReply.reply_num} reply_nickname">${boardReply.reply_writer_nickname}</td>
				 					<td class="reply_${boardReply.reply_num} reply_text">${boardReply.reply}</td>
				 					<td>${boardReply.register_date}</td>
				 					<td><button class="btn btn-secondary password_popover" data-toggle="popover" value="${boardReply.reply_num}" data-reply_option="update">수정</button></td>
				 					<td><button class="btn btn-secondary password_popover" data-toggle="popover" value="${boardReply.reply_num}" data-reply_option="delete">삭제</button></td>
							 	</tr>
							</c:forEach>
						</table>
					</div> 
					
					<!-- 게시글 삭제/ 수정 비밀번호 -->
					<div class="modal fade" id="boardPasswordCheck_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
						<div class="modal-dialog modal-sm" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
									<h4 class="modal-title" id="myModalLabel">비밀번호</h4>
								</div>
								<div class="modal-body">
									<input type="password" class="password form-control" name="password">
									<button type="button" class="btn btn-primary boardPasswordCheck_btn">확인</button>
								</div>
							</div>
						</div>
					</div>
					
					<!-- 댓글 수정/삭제 비밀번호 -->
					<div id ="replyPasswordCheck_popover" class="hide">
						<input type="hidden" name="reply_num" id="reply_num">
						<input type="hidden" name="reply_option" id="reply_option">
						<input type="password" id="reply_password_chk" name="reply_password" class="form-control">
						<button type="button" class="btn btn-warning btn-sm replyPasswordCheck_btn">확인</button>
					</div>
				</div> <!-- .row endTag -->
			</div> <!-- .container endTag -->
		</div> 	 <!-- .content endTag -->
	</div> <!-- .wrapper endTag -->	 
	
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.min.js"></script> 
	<script src="https://kit.fontawesome.com/58a77f3783.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/board/boardView.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/board/boardReply.js"></script>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfobject/2.2.6/pdfobject.min.js"></script> <!-- pdfObject -->
	<script type="text/javascript">
		var boardnum = "<c:out value='${board.board_num}'/>";
		window.getReplyList = getReplyList;
	</script>
</body>
</html>