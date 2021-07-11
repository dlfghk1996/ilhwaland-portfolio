package com.kim.ilhwaland.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kim.ilhwaland.dao.BoardReplyDao;
import com.kim.ilhwaland.dto.BoardReply;

@Repository
public class BoardReplyDaoImpl implements BoardReplyDao{
	
	@Autowired
	SqlSession sqlSession;
	
	/** 댓글 등록 */
	@Override
	public int setReply(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.insert("BoardReplyMapper.insertBoardReply",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}

	/** 해당 게시글 댓글 출력 */
	@Override
	public List<BoardReply> getReplyList(int input) throws Exception {
		List<BoardReply> result = null;
		result = sqlSession.selectList("BoardReplyMapper.selectBoardReply",input);
		return result;
	}
	
	/** 댓글 수정 */
	@Override
	public void updateReply(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.update("BoardReplyMapper.updateBoardReply",input);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
	}
	
	/** 댓글 삭제 */
	@Override
	public int deleteReply(int input) throws Exception {
		int result = 0;
		result = sqlSession.delete("BoardReplyMapper.deleteBoardReply",input);
		return result;
	}

	/** 댓글 비밀번호 확인  */
	@Override
	public int replyPwCheck(BoardReply input) throws Exception {
		int result = 0;
		result = sqlSession.selectOne("BoardReplyMapper.selectReplyPassword",input);
		return result;
	}
}
