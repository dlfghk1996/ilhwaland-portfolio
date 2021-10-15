<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Scheduler</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> <!-- Bootstrap -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.css">
	<link rel="stylesheet"href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> <!-- alert 플러그인 -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/jquery-contextmenu@2.9.2/dist/jquery.contextMenu.min.css"> <!-- contextMenu 플러그인 -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-colorpicker/2.5.3/css/bootstrap-colorpicker.css" rel="stylesheet"> <!-- colorpicker 플러그인 -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css"/> <!-- datepicker 플러그인 -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/commons.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sidebar.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/scheduler/scheduler.css">
</head>
<body>
	<div class="wrapper">
	<%@ include file="../include/header.jsp" %>
	<%@ include file="../include/sidebar.jsp" %>
		
		<div id="content"> 
			<div class="row">
				<div class="content_detail_box">
		        	<div id="content_detail">
						<h3><b>Fullcalendar 라이브러리를 사용한 일정 관리</b></h3>
						<div>	
							<ul>
								<li><i class="far fa-check-square"></i><b>Front 기능 구현</b></li>
								<li><i class="fas fa-square"></i>풀캘린더의 이벤트 소스기능를  이용하여, 이벤트 개체로 이루어진 배열을 생성하고 달력에 랜더링합니다.</li>
								<li><i class="fas fa-square"></i>이벤트의 개체의 옵션 속성을 지정하여, 이벤트개체에 대한 데이터를 제공하고,동적으로 이벤트를 조작(삭제, 추가 등)합니다</li>
							</ul>
							<ul>
								<li><i class="far fa-check-square"></i><b>Server 기능구현</b></li>
								<li><i class="fas fa-square"></i>REST URI 설계방식으로 리소스에 접근합니다</li>
								<li><i class="fas fa-square"></i>서버는 요청받은 데이터를 쿼리메서드와 JPQL 를 사용하여 CRUD를 처리합니다.</li>
								<li><i class="fas fa-square"></i>응답받은 JSON 데이터를 Handlebar.js 템플릿을 이용해 view를 표현합니다.</li>
							</ul>
						</div>  
					</div>
	        	</div>
	        	
	        	<!-- calendar -->
	        	<div style="overflow: hidden;">
	        	
					<!-- category -->	
					<div class="category_container">
						<!-- category 추가 -->
						<div class="add_category_box">
							<p class="category_title">일정 목록 추가</p>
							<input type="text" name="categoryName" class="form-control" required>
							<div id="cp-component" class="input-group colorpicker-component">
								<input type="text" value="#269faf" class="form-control color" name="color"/>
		    					<span class="input-group-addon"><i></i></span>
	  						</div>
	  						<div>
	  							<button class="category_add_btn btn btn-primary btn-xs">추가</button>
	  						</div>
						</div>
						
						<!-- category 목록관리  및 필터링 -->
						<div class="category_box">
							<p class="category_title"><b>나만의 일정 목록</b></p>
							
							<c:if test = "${not empty category_list}">
								<c:forEach var="category_list" items="${category_list}">
									<div class="form-check" style="background-color: ${category_list.color}">
										<span class="form-check-label category-menu" data-key="${category_list.id}">
											${category_list.categoryName}
										</span>
										<span>
											<input class="form-check-input category_checkbox" type="checkbox" value="${category_list.id}" name="category" >
											<c:if test="${category_list.id ne 0}">
												<span>
				  									<button class="category_delete btn btn-dark btn-xs" data-key="${category_list.id}">삭제</button>
				  								</span>
				  								<span>
				  									<button class="category_modify btn btn-dark btn-xs" data-key="${category_list.id}">수정</button>
				  								</span>
											</c:if>
										</span>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>
		
					<div class="calendar_box"><div id="calendar"></div></div>		
				</div>
				
				<!-- 일정 추가 Modal-->
				<div class="modal fade" id="add_modal" tabindex="-1" role="dialog">
					<div class="modal-dialog" role="document">
				    	<div class="modal-content">
				    		<div class="modal-header">
					        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					      	</div>
					      	<div class="modal-body">
					      		<form name ="add_form" id="add_form">
					      			<span class= "form_title">일정 추가</span>
					      			<input type="hidden" name="memberNum" value="${member_num}"/>
					      			<input type="hidden" name="category" id="category"/>
					      			<div class="form-group col-md-12">
					      				<div class="wrap-input bg1">
						      				<span class="label-input">Title</span>
											<input type="text" class="save_input" name="title" id="title" placeholder="Enter title" required>
						        		</div>
						        	</div>
							        <div class="form-group col-md-12">
										<div class="wrap-input bg1 col-md-5">
								            <span class="label-input">Starts at</span>
								            <!--<input type="date" class="save_input" name="startDate" id="startDate" required>  -->
								            <input type="text" class="save_input" name="startDate" id="startDate" required>
							          	</div>
										<div class="wrap-input bg1 col-md-5 col-md-offset-2">
											<span class="label-input">Ends at</span>
											<!--<input type="date" class="save_input" name="endDate" id="endDate" required>-->
											<input type="text" class="save_input" name="endDate" id="endDate" required>
										</div>
							        </div>
							        <!-- category option -->
							        <div class="panel-group col-md-12">
								       	<div class="panel panel-default">
		                    				<div id="collapse1">
		                    					<div class="panel-heading">카테고리</div>
		                    					<div class="panel-body">
		                    						<c:forEach var="category_list" items="${category_list}">
		                    							<button type="button" class="btn btn-primary btn-rounded category_btn" value="${category_list.id}">
		                    								${category_list.categoryName}
		                    							</button>
													</c:forEach>
	       		 								</div>
		                    				</div>
		                    			</div>
		                    		</div>
							        <div class="form-group col-md-6">
							        	<div class="wrap-input">
											<span class="label-input">Message</span>
											<textarea class="input100" name="event" id="event" placeholder="상세 내용을 적어주세요." maxlength="25"></textarea>
										</div>
							        </div>
							        <div>
							        	<button type="submit" class="btn btn-primary save_btn" id="save-event">등록</button>
					      			</div>
					      		</form>  
					      	</div>
				    	</div>  <!-- modal content endTag -->
  					</div>
				</div> <!-- add_modal endTag -->
				
				<!-- 일정 상세보기 Modal-->
				<div class="modal fade" id="show_detail_modal" tabindex="-1" role="dialog">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-body">
       							<div class="row">
       								<div class="col-md-5">
       									<div class="carousel-inner" role="listbox">
       										<img src="${pageContext.request.contextPath}/resources/img/calendar/detail_schedule_bg.png" style="height: 100%;">
							            </div>
           							</div>
           							
           							<div id="template_box">
	           							<!-- handlebars-template -->
	           							<script id="event-template" type="text/x-handlebars-template">
											<div class="event_wrap">
           										<div class="col-md-7 event_content">
													<div>
           												<h2 class="title">{{title}}</h2>
           											</div>
           											<div class="event_box">
								           				<div class="event_date">
								            				<div class="form-group row">
																<label class="col-sm-3 col-form-label">시작일</label>
																<div class="col-sm-9">
								            						<input type="date" class="startDate form-control" value="{{formatDate startDate}}" readonly>
								            					</div>
															</div>
								            				<div class="form-group row">
																<label class="col-sm-3 col-form-label">종료일</label>
								            					<div class="col-sm-9">	
																	<input type="date" class="endDate form-control" value="{{formatDate endDate}}" readonly>
								            					</div>
															</div>
							                			</div>
							               				<select class="selectpicker form-control" data-style="btn-inverse">
							               					<option class="category" value="{{scheduleCategory.id}}">{{scheduleCategory.categoryName}}</option>
							               				</select>
           												<div>
						                  					<div class="event">{{event}}</div>
						                  				</div>
														<div class="row text-center">
       														<button type="button" class="btn btn-info modify_btn" data-num="{{id}}">수정</button>
         													<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        												</div>
           											</div> 
           										</div>
											</div>
											</script> <!-- handlebars-template endTag -->
									</div>
				    			</div>  <!-- row endTag -->
  							</div> <!-- modal body endTag -->
						</div> <!-- modal content endTag -->
					</div>
				</div> <!-- detail_modal endTag -->
				
				<!-- 일정 수정  Modal-->
				<div class="modal fade" id="modify_modal" tabindex="-1" role="dialog">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
				    		<div class="modal-header">
					        	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					      	</div>
					      	<div class="modal-body">
					      		<form id="modify_form" role="form" action="scheduler" method="post">
					      			<span class="form_title">일정 수정</span>
					      			<input type="hidden" name="memberNum" value="${member_num}"/>
					      			<input type="hidden" class="id" name="id">
					      			<input type="hidden" class="category" name="category">
					      			<div class="form-group col-md-12">
					      				<div class="wrap-input bg1">
											<input type="text" class="modify_input title" name="title">
						        		</div>
						        	</div>
							        <div class="form-group col-md-12">
										<div class="wrap-input bg1 col-md-5">
								            <!--<input type="date" class="modify_input startDate" name="startDate"/>-->
								            <input type="text" class="modify_input startDate" id="modify_startDate" name="startDate"/>
							          	</div>
										<div class="wrap-input bg1 col-md-5 col-md-offset-2">
											<!--<input type="date" class="modify_input endDate" name="endDate">-->
											<input type="text" class="modify_input endDate" id="modify_endDate" name="endDate">
										</div>
							        </div>
							        <!-- category option -->
							        <div class="panel-group col-md-12" id="accordion" role="tablist" aria-multiselectable="true">
								       	<div class="panel panel-default">
						                    <div class="panel-heading" role="tab">
						                    	<h4 class="panel-title">
						                        	<a data-toggle="collapse" data-parent="#accordion" href="#collapse2" class="accordion">
						                        		<span class="glyphicon glyphicon-th-list"></span>
						                        		Category
						                        		<i class="fas fa-arrow-circle-down" style="position: absolute; right: 0;"></i>
						                        	</a>
						                        </h4>
						                    </div>
		                    				<div id="collapse2" class="panel-collapse collapse" role="tabpanel">
		                    					<div class="panel-body">
		                    						<c:forEach var="category_list" items="${category_list}">
		                    							<button type="button" class="btn btn-primary btn-rounded category_btn" value="${category_list.id}">${category_list.categoryName}</button>
													</c:forEach>
	       		 								</div>
		                    				</div>
		                    			</div>
		                    		</div>
							        <div class="form-group col-md-12">
							        	<div class="wrap-input">
											<textarea class="event" name="event" placeholder="상세 내용을 적어주세요."></textarea>
										</div>
							        </div>
							        <div>
						    			<input type="submit" class="btn btn-default modify_submit_btn" value="OK">
						    		</div>
					      		</form>  
					      	</div>
				    	</div>  <!-- modal-content endTag -->
  					</div> <!-- modal-dialog endTag -->
				</div> <!-- modify_modal endTag -->
					
			</div> <!-- .row endTag -->
		</div> <!-- .content endTag -->
	</div> <!-- .wrapper endTag -->
	
	
	
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script src="https://kit.fontawesome.com/58a77f3783.js"></script> 
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.js"></script> <!-- fullcalendar 플러그인 -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script> <!-- alert 플러그인 -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-colorpicker/2.5.3/js/bootstrap-colorpicker.js"></script> <!--colorpicker 플러그인  -->
	<script src="https://unpkg.com/@popperjs/core@2/dist/umd/popper.min.js"></script>  <!-- Tooltip 플러그인  -->
	<script src="https://unpkg.com/tippy.js@6/dist/tippy-bundle.umd.js"></script>      <!-- Tooltip 플러그인  -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>   <!-- datepicker 플러그인  -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.min.js"></script>   <!-- validate 플러그인  -->
	<script src="${pageContext.request.contextPath}/resources/js/calendar/jQuery_serializeObject.js"></script>
	<script type="text/javascript">
	
	var calendar;
	var schedule_num;
	var member_num = ${member_num};

    // 문서가 로딩된 시점에 캘린더를 초기화
	$(document).ready(function(){
		
	    // 1. 캘린더 로드	
		var calendarEl = document.getElementById('calendar');
	
		calendar = new FullCalendar.Calendar(calendarEl, {
	  		defaultDate: moment('2021-08'), //실제 사용시 현재 날짜로 수정
	  		locale : 'ko', 
	  		initialView: 'dayGridMonth',
	    	headerToolbar: {
	    		left: 'prev,next',
	    	    center: 'title',
	    	    right: 'addEventButton'
	    	},
	    	dayMaxEventRows: true, 
	    	views: {
	    		dayGrid: {
	    			dayMaxEventRows: 2 
	    		}
	    	},
	        editable: false,
	    	customButtons: {
		      	addEventButton: {
		        	text: '일정추가',
		        	click: function() {
		        		$('#add_modal').modal('show');
		            }	
		        }
	    	},
	    	/** 일정 클릭시 호출, 해당일정 상세보기 */
	    	eventClick: function(info) {  
	    		// 클릭한 일정정보에서 해당 스케줄 pk 추출
	    		schedule_num = info.event.classNames[0].split('_')[1];
	    		$('#show_detail_modal').modal('show');
	    	},
	    	/** 이벤트 1개가 달력에 render 될 때 마다 호출된다. 이벤트 데이터가 변경되면 다시 호출되지 않는다. == eventrender */
	    	eventDidMount: function(eventObj) { 
	    		
	    		// rendering 된 이벤트에서 상세 내용 추출
	    		var event_content = eventObj.event._def.extendedProps.event_detail;
	    		// rendering 된 이벤트에서 'category_num' 추출
	    		var event_category_num = eventObj.event._def.extendedProps.category_num;
	    		// rendering 된 이벤트에서 'event_num' 추출
	    		var event_num = eventObj.event.id;
	    		
	    		// 1. 이벤트에 hover시 tootip 을 보여준다.
    	        tippy(eventObj.el, {
                    content:  event_content
                });
	    		
	    		// 2. 이벤트 삭제 버튼
    	      	var el = eventObj.el;
    	      	$(el).before("<span class='delete_btn' data-num='"+event_num+"'><i class='fas fa-times-circle' style='color:#8d5e5e'></i></span>");
    	    
	    		// 3. checked 된 카테고리의 일정만, 보여준다.
	    		$("input[type=checkbox][name=category]:checked").each(function() {
 	 	    		 //  checked Checkbox의 value
    				 var checkCategory = this.value;
					 
    				 if(checkCategory != event_category_num){
 	 	    			 eventObj.event.setProp('display','none');
 	 	    		 } else {
 	 	    			 eventObj.event.setProp('display','block');
 	 	    			 return false;
 	 	    		 }
	    		});
	    	},
	    	events:function(info, successCallback, failureCallback){
	    		// 서버에서 받아온 데이터를 캘린더에 셋팅
				<c:if test = "${not empty schedule_list}">
		    		var events = new Array();
		    		<c:forEach items='${schedule_list}' var='schedule'>
		    		
			    		var start_date = moment('${schedule.startDate}').format('YYYY-MM-DD');
			    		var end_date = moment('${schedule.endDate}').format('YYYY-MM-DD');
	                    
	                    // 이틀 이상 AllDay 일정인 경우 달력에 표기시 하루를 더해야 정상출력
	                    if(start_date != end_date){
	                    	end_date = moment(end_date,'YYYY-MM-DD').add(1,'days').toDate(); 
	                    	end_date = moment(end_date).format('YYYY-MM-DD');
	                    }
	                    
	                    events.push({
		    					id: '${schedule.id}',
								title: '${schedule.title}',
								start: start_date,
								end: end_date,
								event_detail: '${schedule.event}',
								className: 'event_${schedule.id}',
								category: "${schedule.scheduleCategory.categoryName}",
								category_num: "${schedule.scheduleCategory.id}",
								color : '${schedule.scheduleCategory.color}'
							}); 
		    			
		    		</c:forEach>
	    			successCallback(events);
	    		</c:if>
	    	} //** event end tag **/
	  	}); //** calendar end tag **//
	  
	  	calendar.render();
	  	
	  	// 2. date format 을 변경하는  handlebars.js 플러그인 안에 사용자 정의 함수
	  	Handlebars.registerHelper('formatDate',function(date){
	  		return moment(date).format('YYYY-MM-DD');
		})
		
		// 3. modal close시 form value reset
		$('.modal').on('hidden.bs.modal', function (event) {
  			var modal = event.target;  		
  		
  			// value reset 
  			$(modal).find('form textarea, form input[name!="memberNum"]').val(''); 
  			$(modal).find('.category_btn').removeClass('select');
		})
		
		// 4. 카테고리 추가시 colorpicker 플러그인 사용 설정
		$('#cp-component').colorpicker({ 
	        inline: false,
	        container: true,
            format: 'hex' 
	   })
		
	}); //** document end tag **//
 	
	</script>
	<script src="${pageContext.request.contextPath}/resources/js/calendar/scheduler.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/calendar/category.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/calendar/datepicker.js"></script>
</body>
</html>