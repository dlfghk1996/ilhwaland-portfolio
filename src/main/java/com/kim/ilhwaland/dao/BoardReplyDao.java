package com.kim.ilhwaland.dao;

import java.util.List;

import com.kim.ilhwaland.dto.BoardReply;

public interface BoardReplyDao {
	
	// 댓글 등록
	public int setReply(BoardReply input)throws Exception;
	
	// 해당 게시글 댓글 출력
	public List<BoardReply> getReplyList(int input)throws Exception;
	
	// 댓글 수정
	public void updateReply(BoardReply input)throws Exception;
	
	// 댓글 삭제
	public void deleteReply(BoardReply input)throws Exception;
	
	// 비밀번호 확인
	public void replyPwCheck(BoardReply input) throws Exception;
	
}
