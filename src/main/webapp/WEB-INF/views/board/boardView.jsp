<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>boardView</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">

<style type="text/css">
	.reply_table{
		width :100%;
	}

</style>
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content"> 
			<div class="container text-center">
				<div class="row">
					<h1>자유게시판</h1>
					<div class="panel panel-default">
						<div class="panel-heading">
				 			<h3>${board.subject}</h3>
				 			<div class="contentInfo1">
				 				<span>${board.writer}</span>
				 				<span>작성자이름   &nbsp; | ${board.register_date}</span>
				 			</div>
				 			<div class="contentInfo2">
				 				<ul>
				 					<li><span>조회  &nbsp;&nbsp;</span>${board.read_num}</li>
				 				</ul>
				 			</div>
				 		</div>			 
				 		<!-- 글 내용 -->
      			 		<div class="panel-body">
      			 			<p>${board.content}</p>	
      			 		</div>
      			 		<div class="panel-footer">
      			 			<button type="button" class="btn btn-primary boardPasswordCheckModal_open_btn" data-toggle="modal" data-target="#boardPasswordCheck_modal" value="update">수정</button>
      			 			<button type="button" class="btn-secondary boardPasswordCheckModal_open_btn" data-toggle="modal" data-target="#boardPasswordCheck_modal" value="delete">삭제</button>
      			 		</div>
					</div>
				  
				  	<!-- 댓글 -->
					<form id="reply_form" action="replyWrite" method="GET">
						<input type="hidden" value="${board.board_num}" name="board_num">
						<div class="form-group">
							<div class="form-inline col-xs-6">
								<label for="reply_writer_nickname" class="col-xs-2">닉네임 :</label>
				      			<div class="col-xs-9"><input type="text" class="form-control" id="reply_writer_nickname" placeholder="닉네임" name="reply_writer_nickname" width="100%" ></div>
							</div>
							<div class="form-inline col-xs-6">
								<label for="reply_password"  class="col-xs-2">Password:</label>
				      			<div class="col-xs-9"><input type="password" class="form-control" id="reply_password" placeholder="password" name="reply_password"></div>
					   		</div>
					   	</div>
						<div class="form-group col-xs-12 comment">
				  			<label for="reply" class="col-xs-2">Comment:</label>
				  			<div class="col-xs-8">
				  				<textarea class="form-control" rows="3" cols="30" id="reply" name="reply" placeholder="댓글을 입력하세요"></textarea>
				  			</div>
				  			<button type="button" class="btn btn-default" id="reply_btn">댓글 등록</button>
						</div>
					</form>
					
					<!-- 댓글 목록 -->
					<div id="reply_container">
						<table class="reply_table">
							<c:forEach var="boardReply" items="${boardReplyList}">
								<tr>
				 					<td class="nickname_${boardReply.reply_num}">${boardReply.reply_writer_nickname}</td>
				 					<td class="reply_${boardReply.reply_num}">${boardReply.reply}</td>
				 					<td>${boardReply.register_date}</td>
				 					<td><button class="btn btn-default password_popover" data-toggle="popover" value="${boardReply.reply_num}" data-reply_option="update">수정</button></td>
				 					<td><button class="btn btn-default password_popover" data-toggle="popover" value="${boardReply.reply_num}" data-reply_option="delete">삭제</button></td>
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
									<input type="password" class="board_num" name="board_num">
									<button type="button" class="btn btn-primary boardPasswordCheck_btn">확인</button>
								</div>
							</div>
						</div>
					</div>

					<!-- 댓글 수정/삭제 비밀번호 -->
					<div id ="replyPasswordCheck_popover" class="hide">
						<input type="hidden" name="reply_num" id="reply_num">
						<input type="hidden" name="reply_option" id="reply_option">
						<input type="password" id="reply_password_chk" name="reply_password">
						<button type="button" class="btn btn-warning replyPasswordCheck_btn" >확인</button>
						<button type="button" class="btn-info btn popover_close_btn">취소</button>
					</div>
					
				</div> <!-- .row endTag  -->
			</div> <!-- .container endTag -->
		</div>  <!-- .content endTag -->
		</div> <!-- .wrapper endTag -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
	var boardnum = "<c:out value='${board.board_num}'/>";
	$(document).ready(function(){ 
		
		// 1. 게시글 삭제 or 수정 modal open
		$(document).on('click', '.boardPasswordCheckModal_open_btn', function () {
			var checkType = $(this).attr('value');
			$("#boardPasswordCheck_modal .boardPasswordCheck_btn").attr('value',checkType);
		});
		
		// 2. 게시글 삭제 or 수정
		$('.boardPasswordCheck_btn').click(function(){
			var checkType = $(this).attr('value');
			var data = {
					password :  $('.board_num').val(),            
					checkType : checkType,
					board_num : boardnum
					}
			$.ajax({
				type : 'POST',
			    contentType: 'application/json;charset=UTF-8',
			    data: JSON.stringify(data),
				url : 'passwordCheck',
				success : function(data) {
					if(data == '0'){
						alert('비밀번호가 틀렸습니다.');
					}else{
						// 게시글 삭제
						if(checkType == 'delete'){
							location.href='board';
						// 게시글 수정
						}else{
							var form = document.createElement('form');
							form.setAttribute('method', 'POST');
							form.setAttribute('action', 'boardUpdate');
							var input = document.createElement('input');
							input.setAttribute('type','hidden');
							input.setAttribute('name','board_num');
							input.setAttribute('value',boardnum);
							form.appendChild(input);
							document.body.appendChild(form);
							form.submit();
						}
					}
				},
				error: function(data) {
					alert('error');
				}
			});
		});
		
		// 3. 댓글 등록
		$('#reply_btn').click(function(){
			
			$.ajax({
				data : $('#reply_form').serialize(),
				type : 'POST',
				url : 'replyWrite',
				success : function(data) {
					$('#reply_writer_nickname').val(''); // 댓글 내용 초기화
					$('#reply_password').val(''); // 댓글 작성자 초기화
					$('#reply').val(''); // 댓글 작성자 초기화
					//댓글 목록 리스트 출력
					getReplyList();
				},
				error: function(data) {
					alert('error');
				}
			});
		});
		
		// 4. 댓글 수정/삭제 비밀번호  popover
		$(document).on('click','.password_popover', function(e) {
			$('.password_popover').not($(this)).popover('hide');  // 다른 popover 창 닫기
			// popover설정
			$(this).popover({
				 placement : 'bottom',
				 title: '<h5>password</h5>',
				 container: 'body',
				 html: true, 
				 sanitize: false,
				 content: function(){
               			return $('#replyPasswordCheck_popover').html(); 
               			}
			}).popover('show');
			
			// popover 호출 즉시 발생
			// 해당 댓글의 값을 input 에 넣기 
			$(document).on('shown.bs.popover', function(event){	
				$('#reply_num').val(event.target.getAttribute('value')); // 댓글 pk
				$('#reply_option').val(event.target.getAttribute('data-reply_option')); // update or delete 
			});
			
			// popover close
			$(document).on('click', '.popover_close_btn', function(e) {
				$(this).parents('.popover').popover('hide');
			});
		})
	
		// 5. 댓글 비밀 번호 확인
		$(document).on('click','.replyPasswordCheck_btn', function(e) {
			$(this).parents('.popover').popover('hide');
			var data = {
					reply_num :  $('#reply_num').val(),            
			  	    reply_password :$('.popover #reply_password_chk').val(),
			  	    reply_option :$('#reply_option').val()
					}
			$.ajax({
				type : 'POST',
				url : 'replyOption',
			    contentType: 'application/json;charset=UTF-8',
			    data: JSON.stringify(data),
				success : function(data) {
					if(data == 0){
						alert('비밀번호가 일치 하지 않습니다.');
					}else{
						if($('#reply_option').val() == 'delete'){
							getReplyList();
						}else{
							window.open('replyUpdatePage', 'replyModify', 'width=450, height=450,resizable = yes'); 
						} 
					}
				},
				error: function(data) {
					alert('ajax 통신 error');
				}
			});
		});

		// 6. 댓글 리스트 출력
		function getReplyList(){
			
			$.getJSON('replyList?boardnum='+boardnum, function (data) {
				var html = '';	
				$.each(data, function(i,item){
						html += '<tr>';
						html += '<td class="nikname_"'+item.reply_num+'">'+item.reply_writer_nickname+'</td>';
						html += '<td class="reply_"'+item.reply_num+'">'+item.reply+'</td>';
						html += '<td>'+item.register_date+'</td>';
						html += '<td><button class="btn btn-default password_popover" value="'+item.reply_num+'" data-reply_option="update">수정</button></td>';
						html += '<td><button class="btn btn-default password_popover" value="'+item.reply_num+'" data-reply_option="delete">삭제</button></td></tr>';
					})
					$('.reply_table').empty();
					$('.reply_table').append(html);
					
				})
		}
		window.getReplyList = getReplyList;
	}) // $(document).ready end tag
</script>
</body>
</html>