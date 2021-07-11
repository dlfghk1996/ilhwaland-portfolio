package com.kim.ilhwaland.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kim.ilhwaland.dao.BoardDao;
import com.kim.ilhwaland.dto.Board;

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
		int reandnumResult = sqlSession.update("BoardMapper.updateReadNum",input); //조회수 업데이트
		result = sqlSession.selectOne("BoardMapper.selectBoard",input);
		if(result == null) {
			throw new NullPointerException("result == null");
		}
		if(reandnumResult == 0) {
			throw new NullPointerException("reandnumResult == 0");
		}
		return result;
	}
	
	/** 3) 게시글 전체 출력 */
	@Override
	public List<Board> getBoardList(Board board) throws Exception {
		List<Board> result = null;
		result = sqlSession.selectList("BoardMapper.selectBoardList",board);
		if(result == null) {
			throw new NullPointerException("result == null");
		}
		return result;
	}
	
	/** 4) 게시글 권한 확인 : 수정 & 삭제 */
	@Override
	public Board checkBoardPassword(Board input) throws Exception {
		Board result = null;
		result = sqlSession.selectOne("BoardMapper.selectBoardPassword",input);
		return result;
	}
	
	/** 5) 게시글 수정 */
	@Override
	public void updateBoardContent(Board input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardMapper.updateBoard",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
	}

	/** 6) 게시글 삭제 */
	@Override
	public void deleteBoardContent(int input) throws Exception {
		int result = 0;
		result = sqlSession.delete("BoardMapper.deleteBoard",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
	}

	/** 페이지네이션 전체 게시글 수*/
	@Override
	public int getotalContent() throws Exception {
		int result = 0;
		result = sqlSession.selectOne("BoardMapper.selectBoardCount");
		if(result == 0) {
			throw new NullPointerException("result == null");
		}
		return result;
	}
}
