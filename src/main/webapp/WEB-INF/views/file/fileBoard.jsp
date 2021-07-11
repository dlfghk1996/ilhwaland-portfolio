<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>fileBoard</title>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/fileBoard.css">
	<style type="text/css">
		
	</style>
</head>
<body>
<div class="wrapper">
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/sidebar.jsp" %>
	<div id="content">
		<div class="fileMenu">
			<ul class="nav nav-tabs">
				<li><button class="btn btn-primary" data-toggle="modal" data-target="#fileUpload-modal">업로드</button></li>
			</ul>
		</div>
		<div id="uploadList">
			<h3>UploadList</h3>
			<table class="table table-bordered table-hover">
				<thead>
					<tr>
						<th>번호</th>
						<th>파일명</th>
						<th>타입</th>
						<th>등록일</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="fileDetail" items="${fileList}">
						<tr>
							<td>${fileDetail.file_num}</td>
							<td>${fileDetail.tempFilename}</td>
							<td>${fileDetail.filetype}</td>
							<td>${fileDetail.register_date}</td>
							<td>
								<c:if test="${fileDetail.readAble eq 'y'}">
									<button type="button" class="btn btn-primary fileView_btn" value="${fileDetail.file_num}">
											미리보기
									</button>
								</c:if>
							</td>
							<td>
		        				<a href="fileDownload?file_num=${fileDetail.file_num}" class="btn btn-primary">다운로드</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
    	
    	
    	<!-- upload form Modal -->
    	<div class="modal fade" id="fileUpload-modal" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		      		</div>
		      		<div class="modal-body">
		      			<form method="POST" action="fileUpload" enctype="multipart/form-data" id="uploadForm">
							<label>File:</label> 
							<input type="file" multiple="multiple" id="file" name="file"/> <br/>
							<input class="btn btn-primary btn-sm" type="submit" value="업로드"/>
						</form>
		      		</div>
		    	</div>
		  	</div>
		</div>
		
		<!-- upload된 file을 웹페이지에서 미리  보여주는 Modal -->
		<div class="modal fade" id="fileView_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog modal-lg" style="width:auto;display:table" role="document">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        		<h4 class="modal-title">미리보기</h4>
		      		</div>
		      		<div class="modal-body">
		      			<div class="table_container">
		      			</div>
		      		</div>
		    	</div>
		  	</div>
		</div>
			
			
 	</div> <!-- .content endTag  -->
 </div> <!-- .wrapper endTag -->



<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		//업로드 파일 미리보기 기능 
		$('.fileView_btn').on('click',function(e) {
			var file_num = $(this).val();
				$.ajax({
					url: 'fileViewer',
					type: 'GET', 
					data :  ({'file_num': file_num}),
					contentType : 'application/json; charset=utf-8',
					success : function(data){
						//if($('#fileView_modal .table_container').length){ // not zero
						//	$('#fileView_modal .table_container').empty();
						//}
						var html = jQuery('<div>').html(data); // viewTemplate.jsp를 읽어들인다.
						var contents = html.find('#uploadFileContent').html(); 
						$('#fileView_modal').find('.table_container').html(contents);  // template 삽입
						$('#fileView_modal').modal('show');
					},
					error : function(xhr, status, errorThrown){
						alert("에러");
					}
				});
		})


	})

</script>
</body>
</html>
