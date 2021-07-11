<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	.convertBox{
		width: 100%;
		border: 3px dotted #9191c594;
		padding: 10px;
		border-radius: 50px;
	}
	.dropZone{
		background: #ccc;
        height: 400px;
		border-radius: 50px;
		position: relative;
		margin-top: 33px;
	}
	.dropZone * {
		text-align: center;
		font-weight: 700;
	}
	.dropZone h2{
		top: 42%;
    	left: 33%;
		color: white;
		position: absolute;
	}
	.dropZone span{
    	position: absolute;
    	top: 65%;
    	left: 26%;
    	font-weight: 700;
    	font-size: 15px;
    }
    .dropZone img{
    	width: 35%;
    	position: absolute;
  		top: 4%;
    	left: 32%;
    }
		
</style>
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		<div id="content">
			<ul class="nav nav-tabs">
  				<li class="nav-item">
    				<a class="nav-link active" data-toggle="tab" href="#convert" onclick="convertBtn('img')">IMG To PDF</a>
  				</li>
  				<li class="nav-item">
    				<a class="nav-link" data-toggle="tab" href="#convert" onclick="convertBtn('pdf')">PDF To IMG</a>
  				</li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane show active" id="convert">
					<form action="fileConvert" method="POST" id="fileCovert-form" enctype="multipart/form-data" name="fileCovert-form">
						<!-- 첨부파일 등록  영역 -->
						<div class="convertBox">
							<div class="dropZone">
								<img src="resources/img/JPG-to-PDF.png" class="convertImg">
								<h2>여기에 파일을 드롭 하세요.</h2>
								<span>PDF로 변환할 이미지 파일(JPG, PNG, BMP 등)을 드래그하여 놓습니다.</span>
								<input type="file" name="file" id="file" style="display:none" class="img"/>
								<!--<input type="file" name="file" id="file"> -->
								
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>	
<script type="text/javascript">
	    var fileList = []; //파일 정보를 담아 둘 배열
		// 파일 타입 확인
		$(document).ready(function(){
			 $('.dropZone').on('dragenter', function(e){
				 e.preventDefault(); // 기본 효과 제한
			 }).on('dragover', function(e){                 // 드래그 요소가 특정 영역에 있을 경우 호출
			     e.preventDefault();
			     e.stopPropagation();
			     $(this).css('background-color', '#FFD8D8');
			 }).on('dragleave', function(e){                 // 드래그 요소가 특정 영역에 있을 경우 호출
			     e.preventDefault();
			     e.stopPropagation();
			     $(this).css('background-color', '#ccc');
			 })

			 // 드래그 요소가 드롭되었을 경우 호출
			 $('.dropZone').on('drop',function(e){
				 e.preventDefault(); // 기본 효과 제한
				 
				 var files = e.originalEvent.dataTransfer.files;  // 드래그한 파일들  
				 var check = fileTypeCheck($('input[type="file"]').attr("class"));
				 if(check){
					 $('input[type="file"]')
				       .prop('files', e.originalEvent.dataTransfer.files)  // put files into element
				       .closest('form')
				       .submit();  
				 }
			 });
		})
		
		// 파일 변환 탭 메뉴 클릭 시, value에 따라 보여지는 문구를 다르게 하는 기능 
		function convertBtn(param) {
				var msg = '';
		    	if(param == 'img'){
		    		 msg ='PDF로 변환할 이미지 파일(JPG, PNG, BMP 등)을 드래그하여 놓습니다.';
		    		 $('.convertImg').attr('src', 'resources/img/JPG-to-PDF.png');
		    		 $('input[type="file"]').removeClass().addClass(param);
		    	}else if(param == 'pdf'){ 
		    		msg ='이미지로 변환할 PDF 파일을 드래그하여 놓습니다.';
		    		$('.convertImg').attr('src', 'resources/img/PDF-to-JPG.png');
		    		$('input[type="file"]').removeClass().addClass(param);
		    	}
				$('.dropZone span').text(msg);
		}
	    
	    // 파일 확장자 확인
		function fileTypeCheck(convertType){
	    	//	var reg = convertType=='img'?'/(.*?)\.(jpg|jpeg|png|gif|bmp)$/':'/(.*?)\.(pdf)$/';
			//	var file = document.getElementById('file').value;
  
			//	if(!(file.match(reg))) {
			//		alert('file : ' +file + 'reg : '+reg );
			//		alert('파일 확장자를 다시 확인해주세요');
			//		return false; 
			//	}
				return true;
		} 

</script>
</body>
</html>