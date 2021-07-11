<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>replyModify</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<style type="text/css">
	body{
		background-color: #dcdcdca3;
	}
	fieldset{
		background-color: white;
		border: 0;
    	text-align: center;
    	padding: 30px 28px;
    	font-size: 14px;
    	max-width: 294px;
    	margin: 0 auto;
	}
	.wrap_btn {
		float: right;
	}
	input{
		width : 100%;	
	}
</style>
</head>
<body>
	<h1>댓글 수정</h1>
	<form action="replyUpdate" method="POST" id="replyUpdate_form">
		<fieldset>
			<input type="hidden" name="reply_num" id="reply_num">
			<div class="wrap_writer">
				<span>작성자</span>
				<input type="text" name="reply_writer_nickname" id="reply_writer_nickname">
			</div>
			<div class="wrap_content">
				<span>내용</span>
				<textarea style="resize: both;" name="reply"  id="reply" class="reply"></textarea>
			</div>	
			<div class="wrap_btn">
				<button type="button" class="btn btn_submit replyUpdate_btn">확인</button>
				<button type="reset"  class="btn btn_reset">취소</button>
			</div>
		</fieldset>
	</form>
	
	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
	var reply_num  =$('#reply_num', opener.document).val();
	var reply_writer_nickname = $('.nickname_'+reply_num, opener.document).text();
	var reply = $('.reply_'+reply_num, opener.document).text();
	 
	// 부모창 값 가져오기
	$('#reply_num').val(reply_num);
	$('#reply_writer_nickname').val(reply_writer_nickname);
	$('#reply').val(reply);

	$(document).on('click','.replyUpdate_btn', function(e) {
		$.ajax({
			data : $('#replyUpdate_form').serialize(),
			type : 'POST',
			url : 'replyUpdate',
			success : function(data) {
				window.parent.opener.getReplyList();
				alert('수정되었습니다.');
				window.self.close();
			},
			error: function(data) {
				alert('ajax 통신 error');
			}
		});
	})

</script>
</body>
</html>