package com.kim.ilhwaland.dao;

import java.util.List;

import com.kim.ilhwaland.dto.Board;


public interface BoardDao {
	
	// 게시판 글 작성
	public int setBoardContent(Board input)throws Exception;
	
	// 게시글 상세보기
	public Board getBoardContent(int input)throws Exception;

	// 전체 게시글 리스트 출력
	public List<Board> getBoardList(Board board)throws Exception;
	
	// 게시글 수정
	public void updateBoardContent(Board input)throws Exception;
	
	// 게시글 비밀번호 확인  
	public Board checkBoardPassword(Board input)throws Exception;
	
	// 게시글 삭제
	public void deleteBoardContent(Board input)throws Exception;

	// 조회수 업데이트 
	void updateReadnum(int input) throws Exception;
	
	// 페이지네이션 : 전체 게시글 수 확인
	public int getotalContent()throws Exception;
	
}
