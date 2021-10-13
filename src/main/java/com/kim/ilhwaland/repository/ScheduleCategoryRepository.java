package com.kim.ilhwaland.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kim.ilhwaland.dto.ScheduleCategory;

@Repository
public interface ScheduleCategoryRepository extends JpaRepository<ScheduleCategory, Integer>{

	Optional<ScheduleCategory> findByCategoryName(String input);

	// 전체 카테고리 리스트 
	List<ScheduleCategory> findAllByMemberNumOrId(Integer input,Integer input2);
	
	// 중복 카테고리 유무 확인
	@Query(nativeQuery = true, value="select count(sc.category_num) FROM schedulecategory sc WHERE sc.member_num = :member_num AND sc.category_name = :category_name ORDER BY sc.category_num")
	long findByMemberNumAndCategoryName(@Param("member_num") Integer memberNum, @Param("category_name") String categoryName); 
	
}
