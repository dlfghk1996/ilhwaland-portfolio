$(function(){
	
	// modal close시 form value reset
	$('.modal').on('hidden.bs.modal', function (event) {
	  	var modal = event.target;  		
	  	$(modal).find('input').val(''); 
	})
	
	
	// 1. 게시글 삭제 or 수정 modal open
	$(document).on('click', '.boardPasswordCheckModal_open_btn', function () {
		// Type : update OR delete
		var checkType = $(this).attr('value');    
		$("#boardPasswordCheck_modal .boardPasswordCheck_btn").attr('value',checkType);
	});
	
	
	// 2. 게시글 삭제 or 수정
	$('.boardPasswordCheck_btn').click(function() {
		var checkType = $(this).attr('value');     // Type : update OR delete
		var data = {
				password : $('.password').val(),            
				checkType : checkType,
				board_num : boardnum
			}
		$.ajax({
			type : 'POST',
	    	contentType: 'application/json;charset=UTF-8',
	    	data: JSON.stringify(data),
			url : 'passwordCheck',
			success : function(data) {
				// 게시글 삭제
				if(checkType == 'delete'){
					location.href='board';
				// 게시글 수정
				}else{
					modifyBoard();
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
	});
	
	
	// 게시글 수정 : 동적 form을 생성하여 서버로 보낸다.
	function modifyBoard(){
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
});