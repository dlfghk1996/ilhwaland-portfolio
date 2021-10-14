$.datepicker.setDefaults({
  closeText: '닫기',
  dateFormat: 'yy-mm-dd',
  prevText: '이전 달',
  nextText: '다음 달',
  monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
  monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
  dayNames: ['일', '월', '화', '수', '목', '금', '토'],
  dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
  showMonthAfterYear: true,
  yearSuffix: '년'
});

$(function () {modify_endDate
	$('#startDate').datepicker();
	$('#startDate').datepicker("option", "maxDate", $("#endDate").val());
    $('#startDate').datepicker("option", "onClose", function ( selectedDate ) {
        $("#endDate").datepicker( "option", "minDate", selectedDate );
    });

    $('#endDate').datepicker();
    $('#endDate').datepicker("option", "minDate", $("#startDate").val());
    $('#endDate').datepicker("option", "onClose", function ( selectedDate ) {
        $("#startDate").datepicker( "option", "maxDate", selectedDate );
    });

	$('#modify_startDate').datepicker();
	$('#modify_startDate').datepicker("option", "maxDate", $("#modify_endDate").val());
    $('#modify_startDate').datepicker("option", "onClose", function ( selectedDate ) {
        $("#modify_endDate").datepicker( "option", "minDate", selectedDate );
    });

    $('#modify_endDate').datepicker();
    $('#modify_endDate').datepicker("option", "minDate", $("#modify_startDate").val());
    $('#modify_endDate').datepicker("option", "onClose", function ( selectedDate ) {
        $("#modify_startDate").datepicker( "option", "maxDate", selectedDate );
    });
	//onClose 옵션을 주어야, 종료일이 시작일보다 뒤로 갈수 없고, 시작일이 종료일보다 앞으로 갈 수 없음.

});