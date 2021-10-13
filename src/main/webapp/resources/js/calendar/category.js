$(function(){ 

  	// 카테고리 필터 : checkbox에 이벤트 발생시 함수 호출 
    $('.category_checkbox').on('change', function(){
    	// 모든 소스에서 이벤트를 다시 가져와 화면에 다시 렌더링한다
		calendar.refetchEvents();
    });

	// 카테고리 추가 
    $(document).on('click','.category_add_btn',function(){
    	
		var value = $(".add_category_box input[name='categoryName']").val();
    	var color = $('.add_category_box .color').val();
    	$.ajax({
			type: 'POST',
			url:  'scheduleCategory',
			contentType: 'application/json; charset=UTF-8',
			data : JSON.stringify({'categoryName': value, 'color': color, 'memberNum': member_num}),
			success: function(result, textStatus, jqXHR) {
				location.reload();
	        },
	       	error: function(jqXHR, textStatus) {
	       		swal(jqXHR.status + ' Error!',  jqXHR.responseText +'!', 'error');
	        }
		});
	})
	
  	// 카테고리 삭제
    $(document).on('click','.category_delete',function(){
		$.ajax({
			type: 'DELETE',
			url:  'scheduleCategory',
			contentType : 'application/json; charset=utf-8', 
			data: JSON.stringify({'id': $(this).data('key'), 'memberNum': member_num}),
			success: function(result, textStatus, jqXHR) {
				location.reload();
	        },
	       	error: function(jqXHR, textStatus) {
	       		swal(jqXHR.status + ' Error!', jqXHR.responseText +'!', 'error');
	        }
		});
    })

	// 카테고리 수정
    $(document).on('click','.category_modify',function(){
    	window.open('', 'category', 'width=422px, height=401px');
		        		
		var $form = $('<form></form>'); 
			$form.attr('action', 'category_popup'); 
			$form.attr('method', 'POST'); 
			$form.attr('target', 'category'); 
			$form.appendTo('body'); 
		var num = $('<input type="hidden" value="'+$(this).data('key')+'" name="id">');  
		$form.append(num);
		$form.submit();
    })
    
}); // function end tag

    
    
   
    
  