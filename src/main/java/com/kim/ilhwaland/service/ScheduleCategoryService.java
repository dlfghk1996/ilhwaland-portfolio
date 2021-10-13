package com.kim.ilhwaland.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kim.ilhwaland.dto.ScheduleCategory;
import com.kim.ilhwaland.repository.ScheduleCategoryRepository;

import javassist.NotFoundException;


@Service
public class ScheduleCategoryService {
	
	@Autowired
	private ScheduleCategoryRepository scheduleCategoryRepository;
	
	/** 접속중인 회원이 가진 카테고리 전체 가져오기 + 기본 카테고리 포함  */
	@Transactional
	public List<ScheduleCategory> getCategoryList(Integer input1,Integer input2) throws Exception {
		List<ScheduleCategory> result = scheduleCategoryRepository.findAllByMemberNumOrId(input1, input2);
		for(ScheduleCategory s: result) {
			System.out.println(s.toString());
		}
		return result;
	}
	
	/** 카테고리 추가 */
	@Transactional
	public boolean addScheduleCategory(ScheduleCategory input) throws Exception{
		long category_count = scheduleCategoryRepository.findByMemberNumAndCategoryName(input.getMemberNum(),input.getCategoryName());
		if(category_count > 0) {
			return false;
		}
		input.setMemberNum(input.getMemberNum());
		scheduleCategoryRepository.save(input);
		return true;
	}
	
	/** 카테고리 수정을 위한 단일 조회 */
	@Transactional
	public ScheduleCategory getCategory(Integer input) throws Exception{
		
		ScheduleCategory scheduleCategory = scheduleCategoryRepository.findById(input).orElseThrow(() -> new NotFoundException("scheduleCategory not found"));
	
		return scheduleCategory;
	}
	
	/** 카테고리 수정 */
	@Transactional
	public void modifyCategory(ScheduleCategory input) throws Exception{
		
		ScheduleCategory scheduleCategory = scheduleCategoryRepository.findById(input.getId()).orElseThrow(() -> new NotFoundException("scheduleCategory not found"));
		scheduleCategory.setCategoryName(input.getCategoryName());
		scheduleCategory.setColor(input.getColor());
		
	}
	
	/** 카테고리 삭제  => 활성화된 일정중에 해당 카레고리에 포함되어있다면  삭제 불가*/
	@Transactional
	public void removeScheduleCategory(Integer input) throws Exception{
		ScheduleCategory scheduleCategory = scheduleCategoryRepository.findById(input).orElseThrow(() -> new NotFoundException("scheduleCategory not found"));
		scheduleCategoryRepository.delete(scheduleCategory);
	}

	
}
