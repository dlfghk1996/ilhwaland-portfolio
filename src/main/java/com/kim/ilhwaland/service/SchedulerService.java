package com.kim.ilhwaland.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kim.ilhwaland.dto.Schedule;
import com.kim.ilhwaland.dto.ScheduleCategory;
import com.kim.ilhwaland.repository.SchedulerRepository;

import javassist.NotFoundException;

@Service
public class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;

	/** 일정 전체 조회 */
	@Transactional
	public List<Schedule> getScheduleList(Integer input) throws Exception {
		List<Schedule> scheduleList = schedulerRepository.findAllByMemberNum(input);
		return scheduleList;
	}
	
	/** 일정 등록 */
	@Transactional
	public int addSchedule(Schedule input) throws Exception {
		
		ScheduleCategory category = new ScheduleCategory();
		category.setId(Integer.parseInt(input.getCategory()));

		input.setScheduleCategory(category);
		
		input = schedulerRepository.saveAndFlush(input);
		int newSchedule_num = input.getId();
		return newSchedule_num;
	}
	
	/** 일정 상세 조회 */
	@Transactional(readOnly = true)
	public Schedule getSchedule(Integer input) throws Exception {
		Schedule schedule = schedulerRepository.findById(input)
				.orElseThrow(() -> new NotFoundException("schedule not found"));
	
		return schedule;
	}

	/** 일정 삭제 */
	@Transactional
	public void removeSchedule(Integer input) throws Exception {
		int result = 0;
		result = schedulerRepository.deleteBySchedule(input);
		if(result == 0) {
			System.out.println("schedule not found");
			throw new NotFoundException("schedule not found");
		}
	}

	/** 일정 수정 */
	@Transactional
	public void modifySchedule(Schedule input) throws Exception {
		Schedule schedule = schedulerRepository.findById(input.getId()).orElseThrow(() -> new NotFoundException("schedule not found"));

		schedule.setEvent(input.getEvent());
		schedule.setTitle(input.getTitle());
		schedule.setEndDate(input.getEndDate());
		schedule.setStartDate(input.getStartDate());

		ScheduleCategory scheduleCategory = new ScheduleCategory();
		
		scheduleCategory.setId(Integer.parseInt(input.getCategory()));
		schedule.setScheduleCategory(scheduleCategory);
	}
	
	/** 카테고리 삭제전, 해당 카테고리에 포함되는 일정 유무 확인  */
	@Transactional
	public long findScheduleinCategory(Integer input1,Integer input2) throws Exception {
		long result = schedulerRepository.findByCategoryNum(input1,input2);
		return result;
	}
}
