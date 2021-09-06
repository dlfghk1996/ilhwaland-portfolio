package com.kim.ilhwaland.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kim.ilhwaland.dao.BoardDao;
import com.kim.ilhwaland.dto.Board;
import com.kim.ilhwaland.helper.BadRequestException;

import javassist.NotFoundException;

@Repository
public class BoardDaoImpl implements BoardDao{
	
	@Autowired
	SqlSession sqlSession;
	
	/** 1) 게시판 글 작성 */
	@Override
	public int setBoardContent(Board input) throws Exception {
		int result = 0;
		result = sqlSession.insert("BoardMapper.insertBoard",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}
	
	/** 2) 게시글 상세보기 */
	@Override
	public Board getBoardContent(int input) throws Exception {
		
		Board result = null;
		result = sqlSession.selectOne("BoardMapper.selectBoard",input);
		
		if(result == null) {
			throw new NotFoundException("존재하지 않는 게시글 조회로 에러 발생");
		}
		return result;
	}
	
	/** 3) 게시글 전체 출력 */
	@Override
	public List<Board> getBoardList(Board board) throws Exception {
		List<Board> result = null;
		result = sqlSession.selectList("BoardMapper.selectBoardList",board);
		return result;
	}
	
	/** 4) 게시글 권한 확인 : 수정 & 삭제 */
	@Override
	public Board checkBoardPassword(Board input) throws Exception {
		Board result = null;
		result = sqlSession.selectOne("BoardMapper.selectBoardPassword", input);
		if(result == null) {
			throw new BadRequestException();
		}
		return result;
	}
	
	/** 5) 게시글 수정 */
	@Override
	public void updateBoardContent(Board input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardMapper.updateBoard", input);
		if(result == 0) {
			throw new NullPointerException("게시글 수정 쿼리중 삭제 에러 발생");
		}
	}

	/** 6) 게시글 삭제 */
	@Override
	public void deleteBoardContent(Board input) throws Exception {
		int result = 0;
		result = sqlSession.delete("BoardMapper.deleteBoard", input);
		if(result == 0) {
			throw new NullPointerException("게시글 삭제 쿼리중 삭제 에러 발생");
		}
	}
	
	/** 7) 조회수 업데이트 */
	@Override
	public void updateReadnum(int input) throws Exception {
		int reandnumResult = 0;
		reandnumResult = sqlSession.update("BoardMapper.updateReadNum",input); 
		if(reandnumResult == 0) {
			throw new NullPointerException("게시글 조회수 업데이트 쿼리 실행중 에러 발생");
		}
	}

	/** 8) 페이지네이션 전체 게시글 수*/
	@Override
	public int getotalContent() throws Exception {
		int result = 0;
		result = sqlSession.selectOne("BoardMapper.selectBoardCount");
		return result;
	}
}
