package com.kim.ilhwaland.dto;

// 댓글
public class BoardReply {
	private int reply_num;                 // pk
	private int board_num;                 // 게시글 번호 (FK) : 같은 주제를 갖는 게시물의 고유번호. 부모글과 부모글로부터 파생된 모든 자식글은 같은 번호를 갖는다.
	private int parent;                    // 대댓글 부모 (null 값을 받기위해 wrapper class로 지정)
	private int seq;                       // 같은 그룹내 게시물의 순서
	private int depth;                     // 대댓글 계층 :  같은 그룹내 계층
	private String reply;                  // 댓글 내용
	private int reply_writer;              // 회원 아이디 (FK) (null 값을 받기위해 wrapper class로 지정)
	private String reply_writer_nickname;  // 비회원 닉네임
	private String reply_password;         // 비회원 비밀번호 
	private String register_date;          // 등록일
	private String DEL;

	public int getReply_num() {
		return reply_num;
	}
	public void setReply_num(int reply_num) {
		this.reply_num = reply_num;
	}
	public int getBoard_num() {
		return board_num;
	}
	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public int getReply_writer() {
		return reply_writer;
	}
	public void setReply_writer(int reply_writer) {
		this.reply_writer = reply_writer;
	}
	public String getReply_writer_nickname() {
		return reply_writer_nickname;
	}
	public void setReply_writer_nickname(String reply_writer_nickname) {
		this.reply_writer_nickname = reply_writer_nickname;
	}
	public String getReply_password() {
		return reply_password;
	}
	public void setReply_password(String reply_password) {
		this.reply_password = reply_password;
	}
	public String getRegister_date() {
		return register_date;
	}
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	public String getDEL() {
		return DEL;
	}
	public void setDEL(String dEL) {
		DEL = dEL;
	}

}
