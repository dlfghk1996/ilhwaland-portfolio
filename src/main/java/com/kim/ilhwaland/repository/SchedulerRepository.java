package com.kim.ilhwaland.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kim.ilhwaland.dto.Schedule;

/** paRepository는 인터페이스를 준비하기만 하면, 자동으로 클래스를 만들고 Bean을 생성 */
@Repository
public interface SchedulerRepository extends JpaRepository<Schedule, Integer>{
	
	/**
	 * 기본 메서드 : save(), findOne(), findAll(), count(), delete()
	 **/
	
	/** 전체 일정 조회  */
	List<Schedule> findAllByMemberNum(int input);

	/** 해당 일정 삭제  */
	@Modifying
	@Query(nativeQuery = true, value="DELETE FROM Schedule sc WHERE sc.schedule_num = :schedule_num")
	int deleteBySchedule(@Param("schedule_num") Integer schedule_num);
	
	/** 카테고리 삭제전, 해당 카테고리에 포함되는 일정 유무 확인  */
	@Query(nativeQuery = true, value="SELECT COUNT(*) FROM Schedule sc WHERE sc.category_num = :category_num AND sc.member_num = :member_num")
	long findByCategoryNum(@Param("category_num") Integer category_num, @Param("member_num") Integer member_num); 
	
}
