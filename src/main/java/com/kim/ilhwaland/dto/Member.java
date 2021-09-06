package com.kim.ilhwaland.dto;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


public class Member {
	
	private int member_num;      // pk
	private String member_id;    // 아이디
	private String member_email; // 이메일(이메일 인증 )
	private String password;     // 비밀번호
	private String name;         // 이름
	private String regdate;      // 회원가입일
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)  // 패치 타입 LAZY 설정(proxy)
	@JoinColumn(name = "member_num") 
	private Schedule schedule;
	
	public Member() {
	}
	
	// get & set
	public int getMember_num() {
		return member_num;
	}
	public void setMember_num(int member_num) {
		this.member_num = member_num;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_email() {
		return member_email;
	}
	public void setMember_email(String member_email) {
		this.member_email = member_email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}


}
