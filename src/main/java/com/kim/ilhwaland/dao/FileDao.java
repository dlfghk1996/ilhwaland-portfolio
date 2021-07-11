package com.kim.ilhwaland.dao;

import java.util.List;

import com.kim.ilhwaland.dto.FileDetail;

public interface FileDao {
	
	// 업로드 하는 파일 정보 DB에 저장 ( 단일 or 다중 )
	public int setUploadFile(List<FileDetail> input) throws Exception;
		
	// 업로드된 파일 모두 리스트로 출력
	public List<FileDetail> getUploadFileList() throws Exception;
	
	// 업로드 된 파일 정보 가져오기 (단일)
	public FileDetail getUploadFile(int input) throws Exception;
	
}
