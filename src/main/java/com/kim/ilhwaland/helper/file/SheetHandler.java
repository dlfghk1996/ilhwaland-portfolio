package com.kim.ilhwaland.helper.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;


// SheetHandler : DefaultHandler를 확장 한 클래스 
// excelUpload : 엑셀에 입력되어 있는 nXn 정보를 2차원 ArrayList에 그대로 변환하는 작업 
/**  XSSFSheetXMLHandler */
public class SheetHandler implements SheetContentsHandler{
   
	private List<String> row;
	//private List<List<String>> rows;
	
	private Map<Integer,List<List<String>>> allSheets = new HashMap<Integer,List<List<String>>>();
	
	public Map<Integer,List<List<String>>> getAllSheets() {
		return allSheets;
	}
	
	//public List<List<String>> getRows() {
	//	return rows;
	//}
	
	//빈 값을 체크하기 위해 사용할 셀번호
	private int currentCol = -1;
	private int currentRow = 0; //?


	
//@ExcelColumn, @Merge, @ExcelEmbedded Annotation이 포함되어있으면, 
//Reader는 Parsing 작업시에 비순차 방법으로 진행합니다. 
//이때 내부적으로 Excel 파일 헤더 영역을 Parsing하는 과정에서 실제 Header명이 위치한 데이터 Column 위치와 
//@ExcelColumn에 기입한 Header 명을 비교하여 일치하는 Property에 값이 주입됩니다.
     

	
	// 시작
	@Override
	public void startRow(int rowNum) {
		this.currentRow = rowNum;
		this.currentCol = -1;          // 열
		row = new ArrayList<String>();
	}
	
	// 끝
	@Override
	public void endRow(int rowNum) {
		FileApi.rows.add(row);
		
	}
	
	/** 각 Row 의 각 셀을 읽을 때 */
	@Override
	public void cell(String columnName, String value, XSSFComment comment) {

		// cellReference : cell name
		// formattedValue : cell value
		// icol = cellName to Integer
		
		int iCol = (new CellReference(columnName)).getCol();
		int emptyCol = iCol - currentCol - 1;
	    
		//읽은 Cell의 번호를 이용하여 빈Cell 자리에 빈값을 강제로 저장시켜줌
		for(int i = 0 ; i < emptyCol ; i++) {
			row.add("");
		}
		currentCol = iCol;
		row.add(value);
	}

	/** 해당 엑셀의 머리말 꼬리말에 적혀 있는 값 */
	@Override
	public void headerFooter(String text, boolean isHeader, String tagName) {
	}

}
