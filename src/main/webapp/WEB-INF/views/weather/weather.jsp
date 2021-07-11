<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<button onclick="testRestAPI()">현재 위치</button>
	<h1>오늘 날씨</h1>
	
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>	
	<script type="text/javascript">
		var requestJSON = JSON.stringify('파라미터');
		function testRestAPI() {
			$.ajax({
		        url : 'weather',
		        method : 'GET',
		        headers : {
		        	'Content-Type': 'application/json',	
		        },
		        success : function(data){
		        	alert(data);
		        },
		        error : function(data){
		        	
		        }
		    })
		}
	    
/* /* 	    $.patch = function(api_with_id, patch_data, success, fail) {
			$.ajax({
        		method: 'PATCH',
        		url: api_with_id,
        		data: patch_data,
      		})
      		.then(
        		$.type(success) === 'function' ? success : function(){},
        		$.type(fail) === 'function' ? fail : function(){}
      		);
    		};
  		}
	
	
	
		function handleGeoSucc(position) {
		    const latitude = position.coords.latitude;  // 경도  
		    const longitude = position.coords.longitude;  // 위도
		    alert('위도:' + latitude +'/ 경도: ' + longitude);
		}
	
		function handleGeoErr(err) {
		    console.log("geo err! " + err);
		}
	
		function findLocation() {
			// geolocation API : 사용자의 현재 위치 정보를 가져옴
		    if (navigator.geolocation) { 
		        navigator.geolocation.getCurrentPosition(handleGeoSucc, handleGeoErr); 
		    } else { 
		        loc.innerHTML = "이 문장은 사용자의 웹 브라우저가 Geolocation API를 지원하지 않을 때 나타납니다!"; 

		    }
		}
		findLocation(); */
	</script>
</body>
</html>