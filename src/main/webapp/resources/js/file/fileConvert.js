
$(document).ready(function(){
	
	// 1. drag event 발생시 css 효과 설정
	$('.dropZone').on('dragenter', function(e){
		e.preventDefault(); // 기본 효과 제한
	 
    // 드래그 요소가 특정 영역에 있을 경우 호출
	}).on('dragover', function(e){
		e.preventDefault();
		e.stopPropagation();
		$(this).css('background-color','#FFD8D8');
		
	// 드래그 요소가 특정 영역에서 떠났을 경우 호출
	}).on('dragleave', function(e){                
		e.preventDefault();
		e.stopPropagation();
		$(this).css('background-color','#ccc');
	})


	// 2. 드래그 요소가 드롭되었을 경우 호출
	$('.dropZone').on('drop',function(e) {
		e.preventDefault(); // 기본 효과 제한
		
		var files = e.originalEvent.dataTransfer.files;  // 드래그한 파일
		
		// 활성화 되어있는 tab menu 확인
		var convert_type ='';

		$('.nav-item').each(function (index,item){
			if($(item).hasClass('active')) {
				convert_type = $(this).data('value');
			}
		});
		
		// 변환가능한 파일 확장자 인지 확인
		var check = fileTypeCheck(convert_type, files[0]);
		if(check){
			$(e.target).find('input[type="file"]')
				   .prop('files', files)  
				   .closest('form')
				   .submit();  
	   
	    	// 파일 변환 요청시 실행되는 로직
	    	$(e.target).closest('form').hide();
		    $(e.target).closest('.tab-pane').children('.loading').show();
		    
		    // 응답 헤더에 서버에서 생성한 'fileDownloadToken' 라는 쿠키가 있는지  0.5 초마다 체크한다.
		    FILEDOWNLOAD_INTERVAL = setInterval(function() {
		    	
		    	// 파일 변환 완료시 실행되는 로직
		    	if ($.cookie('fileDownloadToken') == 'TRUE') {
			        // 1. 쿠키 삭제
			        $.removeCookie('fileDownloadToken', { path: '/'});
			        // 2. Interval 종료
			        clearInterval(FILEDOWNLOAD_INTERVAL);
			        // 3. UI 변경 및 alert 알림
			        $(e.target).closest('.tab-pane').children('.loading').hide();
			        $(e.target).closest('form').show();
			        alert('파일 변환이 완료 되었습니다.');	
		      	}
		   }, 500);
		}
	});


	// 4. 파일 확장자 확인
	function fileTypeCheck(convertType, file){
		
	    // 변환타입을 확인 하여, 사용자가 요청한 파일이 변환가능한 파일인지 확인
		var reg = convertType=='img'?'\\.(bmp|gif|jpg|jpeg|png)$':'\\.(pdf)$';
		var IMG_FORMAT = "\\.(bmp|gif|jpg|jpeg|png)$";

    	if(!((new RegExp(reg, "i")).test(file.name))){;
			alert('파일 확장자를 다시 확인해주세요');
			return false; 
		}
		return true;
	} 
})	