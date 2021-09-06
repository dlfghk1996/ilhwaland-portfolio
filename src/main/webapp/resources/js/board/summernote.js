$(document).ready(function() {
	
	// 썸머노트 설정
	$('#summernote').summernote({
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
		fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
		lang: 'ko-KR',
		height: 500,
		minHeight:null,
		maxHeight: null,
		focus: true,
		placeholder: '글을 입력하세요',
		// 콜백 함수 정의 
		callbacks: {
			onImageUpload: function(files, editor, welEditable) {
				for(var i = 0; i < files.length; i++) {
					uploadSummernoteImage(files[i],editor);
                   }
			}
		}
	})
		
	// 업로드한 이미지의 경로 반환 
	function uploadSummernoteImage(file,el) {
		// 비어있는 form 객체 생성
		var formData = new FormData();   
		// key & value 추가
		formData.append('file', file);   
		$.ajax({ 
			data : formData,
			type : 'POST',
			url : 'uploadSummernoteImage',
			contentType : false,
			enctype : 'multipart/form-data',
			processData : false,
			success : function(data) {
				$('#summernote').summernote('insertImage', data);
			},
			error : function(xhr, status, errorThrown){
				window.location = 'error';
			}
		});
	}
})