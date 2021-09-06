<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>fileConvert</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/file/fileConvert.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content">
			<div id="content_detail">
				<h3>PDF 파일 변환</h3>
				<h4>ITextPDF를 이용한 PDF 변환 & PDFbox를 이용한 IMG 변환 </h4>
				<ul>
					<li><i class="far fa-check-square"></i> 드래그 앤 드롭 기능으로 이미지 파일은 PDF로 변환, PDF는 이미지 파일로 변환합니다.</li>
					<li>* 해당 PDF파일의 페이지가 1장이상일 경우 ZIP 파일형식으로 압축되어 제공합니다.*</li>
				</ul> 
			</div>
			<ul class="nav nav-tabs">
  				<li class="nav-item active" data-value="img">
    				<a class="nav-link convert_tab_btn" data-toggle="tab" href="#IMG-to-PDF">IMG To PDF</a>
  				</li>
  				<li class="nav-item" data-value="pdf">
    				<a class="nav-link convert_tab_btn" data-toggle="tab" href="#PDF-to-IMG">PDF To IMG</a>
  				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="IMG-to-PDF">
					<form action="fileConvert" method="POST" enctype="multipart/form-data" name="fileCovert-form">
						<!-- 첨부파일 등록  영역 -->
						<div class="convertBox">
							<div class="dropZone">
								<img src="resources/img/file/JPG-to-PDF.png" class="convertImg">
								<h2>여기에 파일을 드롭 하세요.</h2>
								<span>PDF로 변환할 이미지 파일(JPG, PNG, BMP 등)을 드래그하여 놓습니다.</span>
								<input type="file" name="file" style="display:none" class="img"/>
							</div>
						</div>
					</form>
				</div>
				<div class="tab-pane fade" id="PDF-to-IMG">
					<form action="fileConvert" method="POST" enctype="multipart/form-data" name="fileCovert-form">
						<!-- 첨부파일 등록  영역 -->
						<div class="convertBox">
							<div class="dropZone">
								<img src="resources/img/file/PDF-to-JPG.png" class="convertImg">
								<h2>여기에 파일을 드롭 하세요.</h2>
								<span>이미지로 변환할 PDF 파일을 드래그하여 놓습니다.</span>
								<input type="file" name="file" style="display:none" class="img"/>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://kit.fontawesome.com/58a77f3783.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>		
<script src="${pageContext.request.contextPath}/resources/js/file/fileConvert.js"></script>

</body>
</html>