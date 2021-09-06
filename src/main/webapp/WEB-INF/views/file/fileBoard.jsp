<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>fileBoard</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/file/fileBoard.css">
</head>
<body>
<div id="my-container"></div>
<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
	<div id="content">
		<div id="content_detail">
			<h3>파일 업로드 <i class="fas fa-plus"></i> 파일 다운로드 <i class="fas fa-plus"></i> 파일 데이터 미리보기</h3>
			<div>
				<p><i class="far fa-check-square"></i><b>엑셀파일</b></p>
				<ul>
					<li><i class="fas fa-square"></i>아파치 POI 라이브러리를 사용하여 엑셀파일을 읽습니다.</li>
					<li><i class="fas fa-square"></i>SAX parser 방식의 XMLReader 를 사용해서  대용량 Excel 파일도 처리 할수 있습니다.</li>
					<li><i class="fas fa-square"></i>파일안의 데이터를 각 시트별로 그룹화하여 가져오고, tab 메뉴를 통해 원하는 시트에 접근할수 있습니다.</li>
				</ul>
			</div>
			<div>
				<p><i class="far fa-check-square"></i><b>csv 파일</b></p>
				<ul>
					<li><i class="fas fa-square"></i>아파치 CSV 라이브러리를 사용하여 csv파일을 읽습니다.</li>
					<li><i class="fas fa-square"></i>csv format 형식을 지정하고 지정된 형식에 따라 csv파일을 구문분석하고 분석한 데이터는 테이블 형식으로 표현합니다.</li>
					<li><i class="fas fa-square"></i>jchardet 라이브러리를 사용하여 파일의 인코딩을 확인하고, 해당 charset 으로 파일을 읽는다.</li>
				</ul>
			</div>
			<div><i class="far fa-check-square"></i><b>이미지,텍스트 파일의 내용을 다운로드 없이 web에서 볼 수 있습니다.</b></div> 
			<div class="preview_explain">
				* 미리보기 가능 조건  *<br>
				- 확장자 : csv, xls, xlsx, txt, jpg, gif, png, jpeg <br>
				- 용량 : 1mb 이하 <br>
				- 제약조건 자세히 보기 <span data-toggle="modal" data-target="#preview_explain_modal"><i class="far fa-question-circle" style="color: #e66868;"></i></span>  
			</div>
		</div>
		<div class="fileMenu">
			<ul class="nav nav-tabs">
				<li><button class="btn btn-primary" data-toggle="modal" data-target="#fileUpload-modal">업로드</button></li>
			</ul>
		</div>
		<div id="uploadList">
			<h3>UploadList</h3>
			<table class="table table-bordered table-hover table-striped">
				<thead class="thead-dark">
					<tr>
						<th>번호</th>
						<th>파일명</th>
						<th>타입</th>
						<th>등록일</th>
						<th>미리보기 지원</th>
						<th>다운로드</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="fileDetail" items="${fileList}">
						<tr>
							<td>${fileDetail.rnum}</td>
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
    	
    	
    	<!-- File Upload Form Modal -->
    	<div class="modal fade" id="fileUpload-modal" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		      		</div>
		      		<div class="modal-body">
		      			<form method="POST" action="fileUpload" enctype="multipart/form-data" id="uploadForm">
							<label>File:</label> 
							<input type="file" multiple="multiple" id="file" name="file" style="display: inline-block;"/>
							<input class="btn btn-primary btn-sm fileUpload_btn" type="button" value="업로드"/>
						</form>
		      		</div>
		    	</div>
		  	</div>
		</div>
		
		<!-- upload된 file을 웹페이지에서 미리  보여주는 Modal -->
		<div class="modal fade" id="fileView_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog modal-lg" style="width:auto; display:table" role="document">
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

		<!-- 미리보기 지원 가능한 포맷 설명 -->
		<!-- 모달 영역 -->
		<div class="modal fade" id="preview_explain_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
						<h4 class="modal-title" id="myModalLabel">모달 타이틀</h4>
					</div>
					<div class="modal-body">
						<ul class="nav nav-tabs">
							<li class="nav-item active">
    							<a data-toggle="tab" href="#xlsx">xlsx,xlx</a>
  							</li>
  							<li class="nav-item">
							    <a data-toggle="tab" href="#csv">csv</a>
							</li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane fade in active" id="xlsx">
								<div class ="xlxs_formt_img">
									<img src="${pageContext.request.contextPath}/resources/img/file/xlsx_format.PNG" style="width: 100%">		
								</div>
								<ul>
									<li><i class="fas fa-exclamation-circle"></i> Sheet 3장 까지 미리보기가 가능합니다.</li>
									<li><i class="fas fa-exclamation-circle"></i> 15 열 까지만 미리보기가 가능합니다. </li>
									<li><i class="fas fa-exclamation-circle"></i> column 사이에 공백컬럼이 많을 경우 기존 데이터 table 모양을 유지할 수 없습니다.</li>
								</ul>
						  	</div>
						  	<div class="tab-pane fade" id="csv">
						  		<div class ="csv_formt_img">
									<img src="${pageContext.request.contextPath}/resources/img/file/csv_format.PNG" style="width: 100%">		
								</div>
						    	<ul>
						    		<li><i class="fas fa-exclamation-circle"></i> 필드를 쉼표(,)로 구분하여 읽습니다. </li>
						    		<li><i class="fas fa-exclamation-circle"></i> 인코딩 타입 : UTF-8, EUC-kr</li>
						    		<li><i class="fas fa-exclamation-circle"></i> 15열 까지만 미리보기가 가능합니다.</li>
									<li class="wrong_csv_format">
										<div><i class="fas fa-exclamation-circle"></i> 잘못된 형식</div>
										<div>- A,"B,C", D</div>
										<div>- A,"B,C, D</div>
									</li>
									<li>* 잘못된 데이터 형식 일경우 tab 으로 구분하여 데이터를 읽습니다. *</li>
								</ul>
						  	</div>
						</div>
						
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary">확인</button>
					</div>
				</div>
			</div>
		</div>
			
 	</div> <!-- .content endTag  -->
 </div> <!-- .wrapper endTag -->



<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/58a77f3783.js"></script> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
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
					var html = jQuery('<div>').html(data); // viewTemplate.jsp를 읽어들인다.
					var contents = html.find('#uploadFileContent').html();
					$('#fileView_modal').find('.table_container').html(contents);  // template 삽입
					$('#fileView_modal').modal('show');
				},
			 	error: function(result, textStatus, jqXHR) {
		       		if(result.status == 500){
		       			window.location = "error";
		       		}
		       		swal(result.status + ' Error!', '파일을 읽을 수 없습니다.!', 'error');
			 	}
			});
		})
		
		// 파일 업로드 기능
		$('.fileUpload_btn').on('click',function(e) {
			var vidFileLength = $('#file')[0].files.length;
			if(vidFileLength === 0){
			    alert("선택된 파일이 없습니다.");
			    return;
			}else {
				$('#uploadForm').submit();
			}
		})
	})

</script>
</body>
</html>
