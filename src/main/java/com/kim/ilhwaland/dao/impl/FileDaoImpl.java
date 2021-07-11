package com.kim.ilhwaland.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kim.ilhwaland.dao.FileDao;
import com.kim.ilhwaland.dto.FileDetail;

@Repository
public class FileDaoImpl implements FileDao{
	
	@Autowired
	SqlSession sqlSession;
	
	/** 1) 업로드 하는 파일정보 저장 */
	@Override
	public int setUploadFile(List<FileDetail> input) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", input);
		int result = 0;
		result = sqlSession.insert("FileMapper.insertUploadFileList",map);
		if(result == 0) {
			throw new NullPointerException("result == 0");
		}
		return result;
	}
	
	/** 2) 업로드된 파일 모두 리스트로 출력 */
	@Override
	public List<FileDetail> getUploadFileList() throws Exception {
		List<FileDetail> result =  new ArrayList<FileDetail>();
		result = sqlSession.selectList("FileMapper.selectUploadFileList");
		return result;
	}
	
	/** 3) 업로드된 파일 중  미리보기가 가능한 파일 정보 가져오기(사용자 선택) */
	@Override
	public FileDetail getUploadFile(int input) throws Exception {
		FileDetail result =  null;
		result = sqlSession.selectOne("FileMapper.selectUploadFile",input);
		if(result == null) {
			throw new NullPointerException("result == null");
		}
		return result;
	}

	
}
