package com.kim.ilhwaland.dto;

public class Board {
	private int board_num;    	  // pk
	private String subject;   	  // 게시판 제목
	private String content;   	  // 게시판 내용
	private int writer;       	  // 회원 작성자 pk
	private String writer_id; 	  // 비회원 작성자
	private String password;      // 비회원 수정 비밀번호
	private String register_date; // 등록일
	private String edit_date;     // 수정일
	private int read_num;         // 조회수
	
	// 디비 조회 관련 변수
	private int reply_count;
	private int rnum;
	private int start;
	private int end;
	private String keyword;
	private String search_option;
	
	
	// 생성자
	public Board() {
		
	}
	
	// get & set
	public int getBoard_num() {
		return board_num;
	}
	
	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getWriter() {
		return writer;
	}
	
	public void setWriter(int writer) {
		this.writer = writer;
	}
	
	public String getWriter_id() {
		return writer_id;
	}
	
	public void setWriter_id(String writer_id) {
		this.writer_id = writer_id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRegister_date() {
		return register_date;
	}
	
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	
	public String getEdit_date() {
		return edit_date;
	}
	
	public void setEdit_date(String edit_date) {
		this.edit_date = edit_date;
	}
	
	public int getRead_num() {
		return read_num;
	}
	
	public void setRead_num(int read_num) {
		this.read_num = read_num;
	}
	
	public int getRnum() {
		return rnum;
	}

	public void setRnum(int rnum) {
		this.rnum = rnum;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSearch_option() {
		return search_option;
	}

	public void setSearch_option(String search_option) {
		this.search_option = search_option;
	}

	public int getReply_count() {
		return reply_count;
	}

	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	
	@Override
	public String toString() {
		return "Board [board_num=" + board_num + ", subject=" + subject + ", content=" + content + ", writer=" + writer
				+ ", writer_id=" + writer_id + ", password=" + password + ", register_date=" + register_date
				+ ", edit_date=" + edit_date + ", read_num=" + read_num + ", rnum=" + rnum + ", start=" + start
				+ ", end=" + end + ", keyword=" + keyword + ", search_option=" + search_option + "]";
	}

}
