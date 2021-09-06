package com.kim.ilhwaland.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="scheduleCategory")
@SequenceGenerator(name = "SCHEDULECATEGORY_SEQ_GENERATOR", 
				   sequenceName = "scheduleCategory_SEQ",
				   initialValue=1,   //시작값
		           allocationSize=1) //메모리를 통해 할당할 범위 사이즈
public class ScheduleCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCHEDULECATEGORY_SEQ_GENERATOR")
	@Column(name = "category_num")
	private Integer id;
	
	@Column(name = "member_num")
	private Integer memberNum;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column
	private String color;
	
	
	public ScheduleCategory() {
	}
	
	
	public ScheduleCategory( String categoryName) {
		super();
		this.categoryName = categoryName;
	}
	
	
	public ScheduleCategory(Integer idx, String categoryName) {
		super();
		this.id=idx;
		this.categoryName = categoryName;
	}
	
	
	// getter & setter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "ScheduleCategory [id=" + id + ", memberNum=" + memberNum + ", categoryName=" + categoryName + ", color="
				+ color + "]";
	}
}
