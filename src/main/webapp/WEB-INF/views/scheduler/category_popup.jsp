<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-colorpicker/2.5.3/css/bootstrap-colorpicker.css" rel="stylesheet"> <!-- colorpicker 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/scheduler/category_popup.css">

</head>
<body class="text-center">
	<main class="form">
	    <form id="modify_form">
	    	<input type="hidden" id ="id" name="id" value="${scheduleCategory.id}">
	    	<h1 class="h3 mb-3 fw-normal">카테고리 수정</h1> 
	    	<div class="form-floating">
	    		<label for="id" class="visually-hidden">Category name</label> 
	    		<input type="text" id="categoryName floatingInput" class="form-control" name="categoryName" value="${scheduleCategory.categoryName}">
	    	</div>
	    	<div class="form-floating">
		    	<div id="cp-component" class="input-group">
		    		<label for="color" class="visually-hidden">Color</label> 
					<input type="text" value="${scheduleCategory.color}" class="form-control color" name="color"/>
	    			<span class="input-group-addon"><i></i></span>
	  			</div>
		    </div>
		    <button type="button" class="category_modify_btn w-100 btn btn-lg btn-primary">수정</button>
		</form>
	</main>
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-colorpicker/2.5.3/js/bootstrap-colorpicker.js"></script> <!--colorpicker 플러그인  -->
	<script src="${pageContext.request.contextPath}/resources/js/calendar/jQuery_serializeObject.js"></script>
	<script type="text/javascript">
		$(function () {
			
			// colorpicker 플러그인 사용 설정
			$('#cp-component').colorpicker({ 
		        inline: false,
		        container: true,
	            format: 'hex' 
		   })
		
			// 카테고리 수정
		    $('.category_modify_btn').click(function(){
		    	if($('#categoryName').val() != ''){
		    		$.ajax({
						type: 'PUT',
						url:  'scheduleCategory',
						contentType: 'application/json; charset=utf-8', 
						data: JSON.stringify($('#modify_form').serializeObject()),
						success: function(result, textStatus, jqXHR) {
							swal('', '수정 되었습니다', 'success');
							opener.parent.location.reload();
							window.close();
				        },
				       	error: function(jqXHR, status, error) {
				       		swal(jqXHR.status + ' Error!', jqXHR.responseText +'!', 'error');
				        }
					});
		    	}else {
		    		alert('카테고리명을 입력해주세요.');
		    	}
		    })
		});		
	</script>
</body>
</html>