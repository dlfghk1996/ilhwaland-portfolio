<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>boardWrite</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<!-- include summernote css -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<style type="text/css">
	.note-group-image-url {
		display :none;
	}
</style>
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content"> 
			<div class="container">
				<div class="row">
					<form action="postContent" method="POST">
						<!-- 비회원 글작성 -->
						<div class="form-group">
							<label for="writer_id">이름</label>
	    					<input type="text" class="form-control" id="writer_id" name="writer_id" maxlength="32">
	    					<label for="pwd">비밀번호</label>
	    					<input type="password" class="form-control" id="password" name="password" maxlength="32">
				 		</div>
				 		<div class="form-group">
    						<label for="subject">제목</label>
    						<input type="text" class="form-control" id="subject" name="subject" maxlength="32">
  						</div>
  						<textarea name="content" id="summernote" class="content"></textarea>
  						<div class="row">
  							<div class="col-md-1">
								<button type="submit" class="btn btn-success">등록</button>
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
<!-- include summernote js -->
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		// 썸머노트 설정
		$('#summernote').summernote({
			fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
			fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
    		lang: 'ko-KR',
    		height: 500,
    		minHeight:null,
    		maxHeight: null,
    		focus: true,
    		placeholder: '글을 입력하세요',
    		// 콜백 함수 정의 
    		callbacks: {
    			onImageUpload: function(files, editor, welEditable) {
    				for(var i = 0; i < files.length; i++) {
    					uploadSummernoteImage(files[i],editor);
                       }
				}
			}
		})
		
		// 업로드한 이미지의 경로 반환 
		function uploadSummernoteImage(file,el) {
			var formData   = new FormData(); // 비어있는 form 객체
			formData.append("file", file); // key/value 추가
			$.ajax({
				data : formData,
				type : 'POST',
				url : 'uploadSummernoteImage',
				contentType : false,
				enctype : 'multipart/form-data',
				processData : false,
				success : function(data) {
					alert(data);
					$('#summernote').summernote('insertImage', data);
					
				},
				error : function(xhr, status, errorThrown){
					alert("에러");
				}
			});
		}
	})
</script>
</body>
</html>