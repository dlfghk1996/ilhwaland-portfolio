package com.kim.ilhwaland.dto;

public class Authority {
	private int role_id;      // 권한 pk
	private String role_name; // 권한 이름
	
	// get/set
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
}
