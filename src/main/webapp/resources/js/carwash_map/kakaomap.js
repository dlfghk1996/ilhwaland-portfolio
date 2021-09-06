document.addEventListener('DOMContentLoaded', function(){
	
	/**1. 지도 설정 */
	var markers = [];

	var circle; // 반경을 표시할 원 

	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		mapOption = {
	        		center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
	        		level: 3 // 지도의 확대 레벨
		};
   /** 1. 지도 생성 */   
	var map = new kakao.maps.Map(mapContainer, mapOption); 

	/** 2. 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체 생성 */
	var bounds = new kakao.maps.LatLngBounds();    
	
	/** 3. 장소 검색 객체 생성 */
	var ps = new kakao.maps.services.Places();  
	
	/** 4. 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다 */
	var infowindow = new kakao.maps.InfoWindow({zIndex:1});
	
	// [3-2] 키워드로 장소를 검색
	// 장소검색 객체를 통해 키워드로 장소검색을 요청
	ps.keywordSearch( address + '세차', placesSearchCB); 
	 
	// [3-3] 장소검색이 완료됐을 때 호출되는 콜백함수 
	function placesSearchCB(data, status, pagination) {
		if (status === kakao.maps.services.Status.OK) {
		
	    	displayPlaces(data); // 1. 정상적으로 검색이 완료시 검색 목록과 마커 표출
	  
	    	makeBoundCircle();   // 2. 내 반경 원 표시
	    
		} else if (status === kakao.maps.services.Status.ZERO_RESULT) {
			alert('검색 결과가 존재하지 않습니다.');
	    	return;
		} else if (status === kakao.maps.services.Status.ERROR) {
	    	alert('검색 결과 중 오류가 발생했습니다.');
	    	return;
		}
	}

	// [3-3] 검색 결과 목록과 마커를 표출하는 함수
	function displayPlaces(places) {
	
	 	var listEl = document.getElementById('placesList'), 
		 	menuEl = document.getElementById('menu_wrap'),
	     	fragment = document.createDocumentFragment(), 
	     	bounds = new kakao.maps.LatLngBounds(), 
	     	listStr = '';		    	
	   
	    
	 	// 기존 검색 결과 목록에 추가된 항목들 제거
	 	removeAllChildNods(listEl);
	
	 	// 기존 지도에 표시되고 있는 마커를 제거
	 	removeMarker();
	    
	 	for ( var i=0; i<places.length; i++ ) {
	 	
	 		// 마커를 생성하고 지도에 표시합니다
	    	var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
	        	marker = addMarker(placePosition, i), 
	        	itemEl = getListItem(i, places[i], marker); // 검색 결과 항목 Element를 생성    
	
	    	// 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해 LatLngBounds 객체에 좌표 추가
	    	bounds.extend(placePosition);
	
	    	// 1. 마커와 검색결과 항목에 mouseover 했을때 해당 장소에 인포윈도우에 장소명을 표시
	    	// mouseout 했을 때는 인포윈도우를 닫는다.
	    	kakao.maps.event.addListener(marker, 'mouseover', displayInfowindow(marker, places[i].place_name));
   			kakao.maps.event.addListener(marker, 'mouseout', makeOutListener());
   			
   			// 마커에 클릭이벤트를 등록
   			kakao.maps.event.addListener(marker, 'click', function() {
   				
   				var level = map.getLevel(); // 현재 지도의 레벨
   				map.setLevel(level - 2); // 지도를 레벨을 내린다 => (지도가 확대됩니다)
   	 		});    
   			
   			itemEl.addEventListener('mouseover', displayInfowindow(marker, places[i].place_name));
   			itemEl.addEventListener('mouseout', makeOutListener());

	    	fragment.appendChild(itemEl);
		}
	
		// 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
  		listEl.appendChild(fragment);
		menuEl.scrollTop = 0;
	
		// 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
		map.setBounds(bounds);
	}	

	// 검색결과 항목을 Element로 반환하는 함수입니다
	function getListItem(index, places, marker) {
		var el = document.createElement('li');
		if (marker.getMap() != null) {
				itemStr = '<span class="markerbg marker_'+(index+1) +'"></span>'+'<div class="info">'+'<h5>'+places.place_name +'</h5>';
		
			if (places.road_address_name) {
				itemStr += '<span>'+places.road_address_name+'</span>'+'<span class="jibun gray">'+places.address_name+'</span>';
			} else {
				itemStr += '<span>'+places.address_name+'</span>'; 
			}	
		
			itemStr += '<span class="tel">'+places.phone+'</span>'+'</div>';           
		
			el.innerHTML = itemStr;
			el.className = 'item';
		}
		return el;
	}
	
	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
	function addMarker(position, idx, title) {
		var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
		imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
		imgOptions = {
			spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
	    	spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
	    	offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
		},
		markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
		marker = new kakao.maps.Marker({ position: position,image: markerImage });
	
		//marker.setMap(map); // 지도 위에 마커를 표출합니다
		marker = setBoundary(marker);  // 내 위치 기준으로 지도 위에 마커를 표출
		markers.push(marker);  // 배열에 생성된 마커를 추가합니다

		return marker;
	}	

	// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수 => 인포윈도우에 장소명을 표시
	function displayInfowindow(marker, title) {
		var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
		return function() {
			infowindow.setContent(content);
	    	infowindow.open(map, marker);
		};
	}
	
	// 인포윈도우를 닫는 클로저를 만드는 함수입니다 
	function makeOutListener() {
		return function() {
			infowindow.close();
		};
	}

	// 중심점 기준 반경 설정
	function setBoundary(marker){
		// 원(Circle)의 옵션으로 넣어준 반지름
		var radius = 2000;
		var c1 = map.getCenter();
		var c2 = marker.getPosition();
	    
	    
		// 받은 좌표를 배열에 넣어서 Polyline 객체로 지도 위에 선을 그린다.
		var poly = new kakao.maps.Polyline({path: [c1, c2]});
		var dist = poly.getLength(); // 두 주소의 거리를 m(미터) 단위로 리턴

		if (dist < radius) {
			marker.setMap(map);
		} else {
			marker.setMap(null);
		}
		return marker;
	}

	// 지도 위에 표시되고 있는 마커를 모두 제거합니다
	function removeMarker() {
		for ( var i = 0; i < markers.length; i++ ) {
			markers[i].setMap(null);
		}
		markers = [];
	}
	
	// 검색결과 목록의 자식 Element를 제거하는 함수입니다
	function removeAllChildNods(el) {
		while (el.hasChildNodes()) {
			el.removeChild (el.lastChild);
		}
	}
	
	// [3-3] 내 반경 지도에  표시할 원 생성
	function makeBoundCircle(){
		if (circle) {  // 기존 생성된 원 초기화
     	   circle.setMap(null);
    	}
		var latlng = map.getCenter(); 
		var lat = latlng.getLat(); // 위도
		var lng = latlng.getLng(); // 경도	
	
		circle = new kakao.maps.Circle({
			center : new kakao.maps.LatLng(lat,lng),  // 원의 중심좌표 
			radius: 2500, 			// 미터 단위의 원의 반지름
			strokeWeight: 5, 		// 선의 두께
			strokeColor: '#FF0000', // 선의 색깔
			strokeOpacity: 0.8, 	// 선의 불투명( 1~0 사이의 값, 0에 가까울수록 투명)
			strokeStyle: 'dashed', 	// 선의 스타일
			fillColor: '#CFE7FF', 	// 채우기 색깔
			fillOpacity: 0.4  		// 채우기 불투명도   
		});			
		// 지도에 원을 표시합니다 
		circle.setMap(map);
	}	
	
	/** 5. 지도 드래그 이동으로 인한 중심좌표 변경시 호출 (범위 재설정) **/
	var geocoder = new kakao.maps.services.Geocoder();
	kakao.maps.event.addListener(map,'dragend', function() {
		  
    	/** 지도의 중심좌표 가져오기 */
    	var latlng = map.getCenter();

    	var address_name = '';
    	searchAddrFromCoords(latlng, function(result, status) {
    		if (status === kakao.maps.services.Status.OK) {
    			for(var i = 0; i < result.length; i++) {
					if (result[i].region_type === 'H') { // 행정동의 region_type 값 == 'H'
						address_name = result[i].address_name;
						ps.keywordSearch(address_name + ' 세차', placesSearchCB);
						$('[name=address]').val(address_name); 
						getCarwashPost(address_name);
						
						break;
	    	    	}	
	    		}
    		}
    	})

	});		

	// 좌표로 행정동 주소 정보 요청
	function searchAddrFromCoords(coords, callback) {
		geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);         
	}	

	/** 2. 초기위치로 이동 */
	$('.setCenter_btn').click(function(){
		var moveLatLon = new kakao.maps.LatLng(latitude, longitude); // 이동할 위도 경도 위치를 생성
  		// 지도 중심을 이동
 		map.setCenter(moveLatLon);
 		ps.keywordSearch( address + ' 세차', placesSearchCB); 
 		$('[name=address]').val(address); 
 		getCarwashPost(address);
	})
	
	/** 3. 주소찾기 API */
	$('.addressSearch_btn').click(function(){
		var keyword = $('#address').val();
		// 주소로 좌표를 검색합니다
		geocoder.addressSearch(keyword, function(result, status) {
			// 정상적으로 검색이 완료됐으면 
			if (status === kakao.maps.services.Status.OK) {
				var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
				// 결과값으로 받은 위치를 마커로 표시합니다
			    var marker = new kakao.maps.Marker({
			        				map: map,
			        				position: coords
			        		 });
			        
			    map.setCenter(coords);
			    marker.setMap(map); // 지도의 중심을 결과값으로 받은 위치로 이동
			}
		})
		
		ps.keywordSearch(keyword + ' 세차', placesSearchCB);
		$('#address').val(keyword); 
		getCarwashPost(keyword);
	})
	
	/** 4. 네이버 블로그 크롤링 */
	function getCarwashPost(keyword){
		$.ajax({
			type: 'GET', 
			url:'doCrawling?address='+keyword,
			success : function(result){
				var html='';
				if(result.length > 0){
					$.each(result, function(i){
						html = '<dl>';
						html += '<dd class="icon_box">'
						html += '<img src="resources/img/carwash/map/naver_blog_icon.png" style="width:100%"></dd>';
						html +=	'<dt><a href="'+ result[i].link +'">'+ result[i].title +'</a></dt>';
					 	html += '<dd class="description">'+ result[i].description +'</dd>';
					 	html += '<dd class="bloggername">'+ result[i].bloggername +'</dd>';
					 	html += '</dl>';
  					});
				}else{
					html = '<div>존재하는 게시물이 없습니다.</div>';
				}
  				$('.carwash_post_wrap').html(html);
			},
			error: function(result, textStatus, jqXHR) {
				console.log(result);
				if(result.status == 400){
					location.href = 'httpConnection_error';
				}
	        	swal(result.status + ' Error!', '에러가 발생했습니다. 다시 시도해주세요.!', 'error');
	        }
		});		
	}	
});

 