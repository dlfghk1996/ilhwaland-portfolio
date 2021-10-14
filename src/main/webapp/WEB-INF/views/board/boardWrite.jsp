<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>boardWrite</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet"> <!-- summernote 플러그인 -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/boardWrite.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content"> 
			<div class="container">
				<div class="row">
					<form action="postContent" method="POST" id="postContent_form">
						<!-- 비회원 글작성 -->
						<div class="form-group">
							<label for="writer_id">이름</label>
	    					<input type="text" class="form-control" id="writer_id" name="writer_id" maxlength="32" required>
	    					<label for="pwd">비밀번호</label>
	    					<input type="password" class="form-control" id="password" name="password" maxlength="32" required>
				 		</div>
				 		<div class="form-group">
    						<label for="subject">제목</label>
    						<input type="text" class="form-control" id="subject" name="subject" maxlength="32" required>
  						</div>
  						<textarea name="content" id="summernote" class="content" required></textarea>
  						<div class="row">
  							<div class="col-md-1">
								<button type="submit" class="btn btn-success ">등록</button>
							</div>
							<div>
								<button type="button" class="btn btn-primary">목록</button>
							</div>
  						</div>
					</form>
				</div> <!-- .row endTag  -->
			</div> <!-- .container endTag  -->
		</div> <!-- .content endTag  -->
	</div> <!-- .wrapper endTag -->
	
	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script> <!-- summernote 플러그인 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/board/summernote.js"></script>

</body>
</html>