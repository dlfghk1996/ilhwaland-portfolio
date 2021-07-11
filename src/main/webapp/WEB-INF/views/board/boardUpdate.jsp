<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<!-- include summernote css -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		
		<div id="content"> 
			<div class="container text-center">
				<div class="row">
					<h1>게시글 수정</h1>
					<form id="board-Form" method="POST" action="updateContent">
					   	<input type="hidden" name="board_num" value="${board.board_num}">
						<div class="form-group">
							<label for="subject">제목</label>
				    		<input type="text" class="form-control" id="subject" name="subject" maxlength="32" value="${board.subject}">
				  		</div>
				  		<textarea name="content" id="summernote" class="content">${board.content}</textarea>
						<div class="row">
							<div class="col-md-1">
								<button type="submit" class="btn btn-success">등록</button>
							</div>
						</div>
				   </form>
				</div> <!-- .row endTag  -->
			</div> <!-- .container endTag -->
		</div> <!-- .content endTag -->
	</div> <!-- .wrapper endTag -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<!-- include summernote js -->
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	  $('#summernote').summernote({
		  	fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
		 	fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
  			lang: 'ko-KR',
  			height: 500,
  			minHeight:null,
  			maxHeight: null,
  			focus: true,
  			placeholder: '글을 입력하세요'
	  });
	});
</script>
</body>
</html>