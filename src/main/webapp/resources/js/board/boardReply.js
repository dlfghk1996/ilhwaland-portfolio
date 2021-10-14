
$('#reply_form').validate({
	rules:{
		reply_writer_nickname:{required: true},
		reply_password:{required: true,rangelength : [4,6]},
		reply:{required: true}
	},
	message:{
		reply_writer_nickname:{required: '닉네임은 필수 입력입니다.'},
		reply_password:{
			required: '비밀번호는 필수 입력입니다.',
			rangelength : '비밀번호는 4-6까지 입력입니다.'
		},
		reply:{required: '댓글은 필수 입력입니다.'}
	},
	submitHandler:function(){
		$.ajax({
			data : $('#reply_form').serialize(),
			type : 'POST',
			url : 'replyWrite',
			success : function(data) {
				// 댓글 작성 form 초기화
				$('#reply_form').find('.form-group input, .form-group textarea').val(''); 
			         
				//댓글 목록 리스트 출력
				getReplyList();
			},
			error: function(result, textStatus, jqXHR) {
				window.location = 'error';
			}
		});	
	}
})


// 2. 댓글 수정 & 삭제 비밀번호  popover
$(document).on('click','.password_popover', function(e) {
	$('.password_popover').not($(this)).popover('hide');  // 열려있는 popover 창 닫기
	
	// popover설정
	$(this).popover({
		 
		placement : 'bottom',
		title: '<h5>Password <span class="popover_close_btn"><i class="fas fa-times-circle"></i></span></h5>',
		container: 'body',
		html: true, 
		sanitize: false,
		content: function(){ return $('#replyPasswordCheck_popover').html();}
		
	}).popover('show');
	
	// popover 호출 즉시 발생 => 해당 댓글의 값을 input 에 넣기 
	$(document).on('shown.bs.popover', function(event) {	
		$('#reply_num').val(event.target.getAttribute('value')); // 댓글 pk
		$('#reply_option').val(event.target.getAttribute('data-reply_option')); // update OR delete 
	});
	
	// popover Close
	$(document).on('click', '.popover_close_btn', function(e) {
		$(this).parents('.popover').popover('hide');
	});
})


// 3. 댓글 비밀 번호 확인
$(document).on('click','.replyPasswordCheck_btn', function(e) {
	if($('.popover #reply_password_chk').val()!=''){
		$(this).parents('.popover').popover('hide');
		var data = {
						reply_num :  $('#reply_num').val(),            
		  	    		reply_password: $('.popover #reply_password_chk').val(),
		  	    		reply_option : $('#reply_option').val()
					}
		$.ajax({
			type : 'POST',
			url : 'replyOption',
		    contentType: 'application/json;charset=UTF-8',
		    data: JSON.stringify(data),
			success : function(data) {
				// 댓글 삭제
				if($('#reply_option').val() == 'delete'){
					getReplyList();
				// 댓글 수정
				}else{
					window.open('replyUpdatePage', 'replyModify', 'width=450, height=450, resizable=yes'); 
				} 
			},
			error: function(result, textStatus, jqXHR) {
				if(result.status == 500){
	       			window.location = 'error';
	       		} else {
	       			swal(result.status + ' Error!', result.responseText +'!', 'error');
	       		}
			}
		});
	}else{
		alert('비밀번호를 입력하세요.');
	}
});


// * 댓글 리스트 출력
function getReplyList(){
	// 해당 게시글의 댓글 목록을 불러온다.
	$.getJSON('replyList?boardnum='+boardnum, function (data) {
		var html = '';
		$.each(data, function(i,item){
			html += '<tr>';
			html += '<td class="reply_icon"><i class="far fa-comment-dots"></i></td>';
			html += '<td class="reply_nickname nikname_"'+ item.reply_num +'">'+ item.reply_writer_nickname +'</td>';
			html += '<td class="reply_text reply_"'+ item.reply_num +'">'+ item.reply +'</td>';
			html += '<td>'+ item.register_date +'</td>';
			html += '<td><button class="btn btn-secondary password_popover" value="'+ item.reply_num +'" data-reply_option="update" data-toggle="popover">수정</button></td>';
			html += '<td><button class="btn btn-secondary password_popover" value="'+ item.reply_num +'" data-reply_option="delete" data-toggle="popover">삭제</button></td></tr>';
		})
		$('.reply_table').empty();
		$('.reply_table').append(html);
	})
}

