$(document).ready(function(){
	// swal alert 플러그인 사용법  
 	// swal parameter => swal(title, text, icon(success, info, warning, error)) 
 		 
 		 
	// 일정 상세 보기 
	$('#show_detail_modal').on('show.bs.modal', function (e) {
		$.ajax({
			type: 'GET',
			url:  'scheduler/'+schedule_num,
			dataType:'json',
			success: function(result, textStatus, jqXHR) {
				//핸들바 템플릿을 가져온다.
    			var source = $("#event-template").html();
    			//핸들바 템플릿을 컴파일한다.
    			var template = Handlebars.compile(source);
				//핸들바 템플릿에 데이터 바인딩 후 HTML 생성
				var data = template(result);
				
				$('#template_box .event_wrap:first').remove();
				$('#template_box').append(data);
				
	        },
	        error: function(result, textStatus, jqXHR) {
	        	swal(result.status + ' Error!', result.responseText +'!', 'error');
				$('#show_detail_modal').modal('hide');
	        }
		});
	});
	
	
	// 일정 추가 모달 open시 default 값 할당
	$('#add_modal').on('show.bs.modal', function (e) {
		
		var today = moment().format('YYYY-MM-DD');
		$('#startDate').val(today);
		$('#endDate').val(today);
		
		$('#collapse1 .category_btn').each(function (index,item){
			if($(item).val() == 0){
				$(item).addClass('select');
				$('#category').val($(item).val());
			}
		});
	});


	// 일정 추가 
	$('#add_form').validate({
		rules:{
			title:{required: true},
			event:{required: true,rangelength : [1,25]}
		},
		message:{
			title:{required: '일정명은 필수 입력입니다.'},
			event:{
				required: '일정 내용은 필수 입력입니다.',
				rangelength : '글자수는 25 까지 가능합니다.'
			}
		},
		submitHandler:function(){
			alert('hi');
			$.ajax({
				type: 'POST',
				url:  'scheduler',
				data: $('#add_form').serialize(), 
				success: function(result,textStatus,jqXHR) {
					swal('Success!','일정이 등록되었습니다 !', 'success');
		        
		        	$('#add_modal').modal('hide');
					// 이벤트 요소 추가
					var start_date = moment(result.startDate).format('YYYY-MM-DD');
		    		var end_date = moment(result.endDate).format('YYYY-MM-DD');
		    	
		    		// 이틀 이상 AllDay 일정인 경우 달력에 표기시 하루를 더해야 정상출력
	            	if(start_date != end_date){
	            		end_date = moment(end_date,'YYYY-MM-DD').add(1,'days').toDate(); 
	            		end_date = moment(end_date).format('YYYY-MM-DD');
	            	}
	                   
		    		var new_event= [{ 
		    				id:  result.id,
			    			title: result.title, 
			    			start: start_date, 
			    			end: end_date, 
			    			event_detail: result.event,
			    			className: 'event_'+result.id,
			    			category: result.scheduleCategory.categoryName,
			    			category_num: result.scheduleCategory.id,
			    			color : result.scheduleCategory.color
		    			}];
					calendar.addEventSource(new_event);
	        	},
	        	error: function(result, textStatus, jqXHR) {
	        		swal(result.status + ' Error!', '등록을 실패하였습니다.!', 'error');
	        	}
			});
		}
	})
	
	// 일정 추가시 카테고리 선택 (일정 추가 & 일정 수정시 발생)
	$(document).on('click','.category_btn',function(){
		var input = $(this).closest('form').find(":input[name='category']");
		$('.category_btn').removeClass('select');
		
		if($(this).hasClass('select')){
	 		$(this).removeClass('select');
	 		$(input).val($(this).val(''));
	 	}else{
	 		$(this).addClass('select');
	 		$(input).val($(this).val());
	 	}
	})
	
	
	// 일정 수정 form setting
	$(document).on('click','.modify_btn',function(){
		
		$('#modify_form .id').val($(event.target).data('num'));
		$('#modify_form .title').val($('#show_detail_modal .title').text());
		$('#modify_form .event').val($('#show_detail_modal .event').text());
		
		$('#modify_form .startDate').val($('#show_detail_modal .startDate').val());
		$('#modify_form .endDate').val($('#show_detail_modal .endDate').val());
		
		// category 값 비교하여 버튼 이벤트 를 걸어주고, 값도 넣어준다.
		var category_num = $('#show_detail_modal .category').val();
		$('#modify_form .category').val(category_num);
		
		$('#collapse2 .category_btn').each(function (index,item){
			if($(item).val() == category_num){
				$(item).addClass('select');
			}
		});
		
		$('#modify_modal').modal('show');
		$('#show_detail_modal').modal('hide');
	})
	
	
	// 일정 수정
	$('#modify_form').validate({
		rules:{
			title:{required: true},
			event:{required: true,rangelength : [1,25]}
		},
		message:{
			title:{required: '일정명은 필수 입력입니다.'},
			event:{
				required: '일정 내용은 필수 입력입니다.',
				rangelength : '글자수는 25 까지 가능합니다.'
			}
		},
		submitHandler:function(){
			$.ajax({
				type: 'PUT',
				url:  'scheduler',
				contentType : 'application/json; charset=utf-8', 
				data: JSON.stringify($('#modify_form').serializeObject()),
				success: function(result, textStatus, jqXHR) {
					location.reload();
		        },
		       	error: function(result, textStatus) {
		       		swal(result.status + ' Error!', result.responseText +'!', 'error');
		       	}
			});
		}
	})
	
	
	
	// 일정 삭제 
	$(document).on('click','.delete_btn',function(){ 
		var num = $(this).data('num');
		$.ajax({
			type: 'DELETE',
			url:  'scheduler/'+num,
			success: function(result,textStatus,jqXHR) {
				$('#show_detail_modal').modal('hide');
				var event = calendar.getEventById(num);
				event.remove();
				swal('Success !', result, 'success');
	        },
	       	error: function(result, textStatus, jqXHR) {
	       		if(result.status == 500){
	       			window.location = '500';
	       		} else if(result.status == 404) {
	       			window.location = 'notFound_error';
	       		}
	       		swal(result.status + ' Error!', result.responseText +'!', 'error');
	        }
		});
	})
}) // $ end tag


		

