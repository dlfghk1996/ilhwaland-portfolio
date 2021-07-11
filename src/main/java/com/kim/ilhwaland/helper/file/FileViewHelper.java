package com.kim.ilhwaland.helper.file;

import java.util.List;
import java.util.Map;

/** 읽은 파일의 내용을 담기위한 파일 타입별 객체 설정 */
public class FileViewHelper {
	private Map<String,List<List<String>>> excelMap; //.xls, xlxs 데이터
	private List<List<String>> excelList;   //.xls, xlxs 데이터
	private List<String[]> csvList;         // .csv 파일
	private List<String> txtList;           // .txt 파일
	private String sourcePath;              // img, pdf
	private String fileType;
	
	
	// get,set
	public List<List<String>> getExcelList() {
		return excelList;
	}
	public void setExcelList(List<List<String>> excelList) {
		this.excelList = excelList;
	}
	public List<String[]> getCsvList() {
		return csvList;
	}
	public void setCsvList(List<String[]> csvList) {
		this.csvList = csvList;
	}
	public List<String> getTxtList() {
		return txtList;
	}
	public void setTxtList(List<String> txtList) {
		this.txtList = txtList;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}   
	public Map<String, List<List<String>>> getExcelMap() {
		return excelMap;
	}
	public void setExcelMap(Map<String, List<List<String>>> excelMap) {
		this.excelMap = excelMap;
	}
}
