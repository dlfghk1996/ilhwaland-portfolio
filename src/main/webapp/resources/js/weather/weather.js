$(document).ready(function(){
	
	// 시간별 예보 정보가 3개 이상일떄 다음 버튼 생성
	if($('.timelineWeather_box > div > ul').children().length > 3){
		$('.timeline_slides_next').removeClass('off');
	}
	
	// [ 시간별 예보 정보 ] 다음 버튼 클릭시
	$('.timeline_slides_next').click(function() {
		for(var i=0; i<3; i++){
			var heigth = $('.timelineWeather').eq(i).outerHeight(true);
			$('.timelineWeather').eq(i).css({'margin-top' : '-191.77px'});
		}
	
		// 이전 버튼 visible
		$('.timeline_slides_prev').removeClass('off');
		// 다음 버튼 hide
		$('.timeline_slides_next').addClass('off');
	})	

	// [ 시간별 예보 정보 ] 이전 버튼 클릭시
	$('.timeline_slides_prev').click(function() {
		for(var i=0; i<3; i++){
			var heigth = $('.timelineWeather').eq(i).outerHeight(true);
			$('.timelineWeather').eq(i).css({'margin-top' : 0});
		}
			
	// 다음 버튼 visible
	$('.timeline_slides_next').removeClass('off');
		// 이전 버튼 hide
		$('.timeline_slides_prev').addClass('off');
	})
})