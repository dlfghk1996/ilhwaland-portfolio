<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>replyModify</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/board/modify_reply_popup.css">
</head>
<body>
	<h3>댓글 수정</h3>
	<div class="form_box">
		<form action="replyUpdate" method="POST" id="replyUpdate_form">
			<fieldset>
				<input type="hidden" name="reply_num" id="reply_num" value="${boardReply.reply_num}">
				<div class="wrap_writer">
					<label>작성자</label>
					<input type="text" name="reply_writer_nickname" id="reply_writer_nickname" class="form-control" value="${boardReply.reply_writer_nickname}"required>
				</div>
				<div class="wrap_content">
					<label>내용</label>
					<textarea style="resize: both;" name="reply" id="reply" class="reply form-control" required>${boardReply.reply}</textarea>
				</div>	
				<div class="wrap_btn">
					<input type="submit" class="btn btn_submit replyUpdate_btn" value="확인">
					<button type="reset"  class="btn btn_reset">취소</button>
				</div>
			</fieldset>
		</form>
	</div>
	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.min.js"></script> 
<script type="text/javascript">
	
	/* 부모창에서 해당 댓글의 값을 가져온다.
	// popover 창에서 가져온다.
	var reply_num = $('#reply_num', opener.document).val();  
	// 댓글목록에서 해당 댓글의 값을 가져온다.
	var reply_writer_nickname = $('.nickname_'+reply_num, opener.document).text(); 
	var reply = $('.reply_'+reply_num, opener.document).text();
	
	// 부모창에서 가져온 값을 form에 이식한다. 
	$('#reply_num').val(reply_num);
	$('#reply_writer_nickname').val(reply_writer_nickname);
	$('#reply').val(reply);
	 **/
	$('#replyUpdate_form').validate({
		rules:{
			reply_writer_nickname:{required: true},
			reply:{required: true}
		},
		message:{
			reply_writer_nickname:{required: '닉네임은 필수 입력입니다.'},
			reply:{required: '댓글은 필수 입력입니다.'}
		},
		submitHandler:function(){
			$.ajax({
				data : $('#replyUpdate_form').serialize(),
				type : 'POST',
				url : 'replyUpdate',
				success : function(data) {
					window.parent.opener.getReplyList();
					swal('수정되었습니다.');
					window.self.close();
				},
				error: function(data) {
					location.href = 'error';
				}
			});
		}
	})
</script>
</body>
</html>