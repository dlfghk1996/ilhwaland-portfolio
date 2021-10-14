 package com.kim.ilhwaland.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kim.ilhwaland.dao.BoardReplyDao;
import com.kim.ilhwaland.dto.BoardReply;
import com.kim.ilhwaland.helper.BadRequestException;

@Repository
public class BoardReplyDaoImpl implements BoardReplyDao{
	
	@Autowired
	SqlSession sqlSession;
	
	/** 1) 댓글 등록 */
	@Override
	public int setReply(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.insert("BoardReplyMapper.insertBoardReply",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}

	/** 2) 해당 게시글 댓글 출력 */
	@Override
	public List<BoardReply> getReplyList(int input) throws Exception {
		List<BoardReply> result = null;
		result = sqlSession.selectList("BoardReplyMapper.selectBoardReply",input);
		return result;
	}
	
	/** 3) 댓글 수정 */
	@Override
	public void updateReply(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardReplyMapper.updateBoardReply",input);
		if(result == 0) {
			throw new NullPointerException("댓글 수정 쿼리 시도 중 에러 발생");
		}
	}
	
	/** 4) 댓글 삭제 */
	@Override
	public void deleteReply(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.delete("BoardReplyMapper.deleteBoardReply",input);
		if(result == 0) {
			throw new NullPointerException("댓글 삭제 쿼리 시도 중 에러 발생");
		}
	}

	/** 5) 댓글 비밀번호 확인  */
	@Override
	public void replyPwCheck(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.selectOne("BoardReplyMapper.selectReplyPassword",input);
		if(result == 0) {
			throw new BadRequestException();
		}
	}
	
	/** 4) 수정 할 댓글 조회 */
	@Override
	public BoardReply getBoardReply(int input) {
		BoardReply result = null;
		result = sqlSession.selectOne("BoardReplyMapper.selectReply",input);
		if(result == null) {
			throw new NullPointerException("댓글 조회 중 에러 발생");
		}
		return result;
	}
}
