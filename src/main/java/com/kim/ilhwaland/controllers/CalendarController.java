package com.kim.ilhwaland.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kim.ilhwaland.dto.Schedule;
import com.kim.ilhwaland.dto.ScheduleCategory;
import com.kim.ilhwaland.service.ScheduleCategoryService;
import com.kim.ilhwaland.service.SchedulerService;

import javassist.NotFoundException;

/**
 * @RestController 
 * @ResponseBody : 자바객체를  XML/JSON 으로 변환해서  BODY 에 실어 전송 .
 * @RequestBody  : 클라이언트가 요청한  XML/JSON을 자바 객체로 변환해서 전달 받을수 있다.
 * ResponseEntity
 * RequestEntity
 * **/

@Controller
public class CalendarController {
		
	@Autowired
    private SchedulerService schedulerService;
	
	@Autowired
    private ScheduleCategoryService scheduleCategoryService;
	
	/** [VIEW] default page */
	@RequestMapping(value = "schedulerPage")
	public String scheduler(HttpServletRequest request, Model model) throws Exception {
		
		HttpSession session = request.getSession();
		int member_num  = (Integer) session.getAttribute("member_num");
		List<Schedule> schedule_list = schedulerService.getScheduleList(member_num);
		List<ScheduleCategory> category_list = scheduleCategoryService.getCategoryList(member_num, 0);
	
		model.addAttribute("schedule_list", schedule_list);
		model.addAttribute("category_list", category_list);

		return "scheduler/scheduler";
	}
	
	/** [VIEW] category 수정 pop_up */
	@RequestMapping(value = "category_popup")
	public String category(@RequestParam("id") int category_num, Model model) throws Exception {
		
		ScheduleCategory scheduleCategory = scheduleCategoryService.getCategory(category_num);
		model.addAttribute("scheduleCategory", scheduleCategory);
	
		return "scheduler/category_popup";
	}
	
	/** [Method 01] 등록 **/
	@ResponseBody
	@PostMapping("scheduler")
	public ResponseEntity<Schedule> addSchedule(Schedule schedule) {
		try {
			int num = schedulerService.addSchedule(schedule); // 저장
			schedule = schedulerService.getSchedule(num);     // 저장 한 값 불러오기
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);  
	}
	
	/** [Method 02] 단일 조회 **/
	@ResponseBody
	@GetMapping("scheduler/{idx}")
	public ResponseEntity<?> getSchedule(@PathVariable("idx") int schedule_num) {
		Schedule schedule;
		try {
			schedule = schedulerService.getSchedule(schedule_num);
		} catch (NotFoundException e1) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일정이 존재하지 않습니다.");
		} catch (Exception e2) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);  // 데이터와 상태코드 리턴( HTTP 정보를 반환)
	}
	
	
	/** [Method 03] 일정 삭제  **/
	@ResponseBody
	@DeleteMapping("scheduler/{idx}")
	public ResponseEntity<?> removeSchedule(@PathVariable("idx") String schedule_num) {
		
		try {
			schedulerService.removeSchedule(Integer.parseInt(schedule_num));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
		} catch (NotFoundException e1) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.status(HttpStatus.OK).body("일정이 삭제되었습니다.");  // 데이터와 상태코드 리턴( HTTP 정보를 반환)
	}
	
	/** [Method 04] 일정 수정 **/
	@ResponseBody
	@PutMapping("scheduler")
	public ResponseEntity<?> modifySchedule(@RequestBody Schedule schedule) {
		try {
			schedulerService.modifySchedule(schedule);
			
		} catch (NotFoundException e1) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 게시물 입니다.");
		} catch (Exception e2) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);  // 데이터와 상태코드 리턴( HTTP 정보를 반환)
	}
	
	/** [Method 05] 카테고리 등록 **/
	@ResponseBody
	@PostMapping("scheduleCategory")
	public ResponseEntity<?> addScheduleCategory(@RequestBody ScheduleCategory scheduleCategory) {

		try {
			boolean result = scheduleCategoryService.addScheduleCategory(scheduleCategory);
			if(!result) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카테고리가 이미 존재합니다.");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);  // 데이터와 상태코드 리턴( HTTP 정보를 반환)
	}
	
	/** [Method 06] 카테고리 수정 **/
	@ResponseBody
	@PutMapping("scheduleCategory")
	public ResponseEntity<?> modifyScheduleCategory(@RequestBody ScheduleCategory scheduleCategory) {

		try {
			scheduleCategoryService.modifyCategory(scheduleCategory);
		} catch (NotFoundException e1) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 카테고리 입니다.");
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.status(HttpStatus.OK).body("카테고리가 수정되었습니다.");  
	}
	
	/** [Method 07] 카테고리 삭제 **/
	@ResponseBody
	@DeleteMapping("scheduleCategory")
	public ResponseEntity<?> romveScheduleCategory(@RequestBody ScheduleCategory scheduleCategory) {

		int category_num = scheduleCategory.getId();
		int member_num = scheduleCategory.getId();
		try {
			long result = schedulerService.findScheduleinCategory(category_num, member_num);
			if(result == 0) {
				scheduleCategoryService.removeScheduleCategory(category_num);
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 카테고리는 삭제할수 없습니다.");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("카테고리가 삭제되었습니다.");  
	}
	
}
