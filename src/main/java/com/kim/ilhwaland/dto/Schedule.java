package com.kim.ilhwaland.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;


@DynamicUpdate      // 변경 필드만 반영
@Entity
@Table(name="schedule")
@SequenceGenerator(name = "SCHEDULE_SEQ_GENERATOR", 
				   sequenceName = "schedule_SEQ",
				   initialValue=1, 		//시작값
		           allocationSize=1) 	//메모리를 통해 할당할 범위 사이즈)
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCHEDULE_SEQ_GENERATOR")
	@Column(name = "schedule_num")
	private Integer id; 
	
	@Column(name = "member_num")
	private int memberNum; 

	@Column
	private String title; 
	@Column
	private String event; 
	
	@Column(name = "start_date")
	private String startDate; 
	
	@Column(name = "end_date")
	private String endDate;   
	
	// 엔티티 클래스 내부에서만 동작하게 하는 어노테이션
	@Transient 
	private String category;  // view 에서 호출하기 위한 변수

	// @ManyToOne =>( 1: N ) 참조하는 쪽 , Innerjoin
	@ManyToOne(optional = false, fetch = FetchType.EAGER) 
	@JoinColumn(name = "category_num")
	private ScheduleCategory scheduleCategory;

	
	public Schedule() {
		
	}

	// setter & getter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEnddate(String endDate) {
		this.endDate = endDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public ScheduleCategory getScheduleCategory() {
		return scheduleCategory;
	}

	public void setScheduleCategory(ScheduleCategory scheduleCategory) {
		this.scheduleCategory = scheduleCategory;
	}

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", memberNum=" + memberNum + ", title=" + title + ", event=" + event
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", category="
				+ category + ", scheduleCategory=" + scheduleCategory + "]";
	}

}
