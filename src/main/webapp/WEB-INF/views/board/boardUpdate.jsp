<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet"> <!-- summernote 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/boardUpdate.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		
		<div id="content"> 
			<div class="container">
				<div class="modify_form_wrap">
					<h3><b>게시글 수정</b></h3>
					<div>
						<form id="board-form" method="POST" action="updateContent">
						   	<input type="hidden" name="board_num" value="${board.board_num}">
							<div class="form-group">
								<label for="subject" class="col-sm-1 control-label">제목</label>
					    		<div class="col-sm-11">
					    			<input type="text" class="form-control" id="subject" name="subject" maxlength="32" value="${board.subject}">	
					    		</div>
					  		</div>
					  		<textarea name="content" id="summernote" class="content" required>${board.content}</textarea>
							<div class="row">
								<div class="col-md-1">
									<input type="submit" class="btn btn-success" value="수정">
								</div>
							</div>
					   </form>
					</div>
				</div> <!-- .row endTag  -->
			</div> <!-- .container endTag -->
		</div> <!-- .content endTag -->
	</div> <!-- .wrapper endTag -->

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script> <!-- summernote 플러그인 -->
	<script src="${pageContext.request.contextPath}/resources/js/board/summernote.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.min.js"></script>
	<script type="text/javascript">
		$('#board-form').validate({
			rules:{
				subject:{required: true},
				content:{required: true}
			},
			message:{
				subject:{required: '게시물명은 필수 입력입니다.'},
				content:{required: '게시글은 필수 입력입니다.'}
			}
		})
	</script>
	
</body>
</html>