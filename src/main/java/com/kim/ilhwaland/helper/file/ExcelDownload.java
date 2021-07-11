package com.kim.ilhwaland.helper.file;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.kim.ilhwaland.dto.FileDetail;
import com.kim.ilhwaland.helper.Annotation;


/** excel file 생성후 다운로드 */
public class ExcelDownload extends AbstractXlsxView{
	
	private Workbook workbook;  // Excel형식에 맞는 인스턴스 객체
	private Sheet sheet;        // Excel Sheet
	
	@Override
	protected void buildExcelDocument(Map<String, Object> modelMap, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.workbook = workbook;
		
		// 엑셀화 시킬 객체 리스트
		List<FileDetail> fileList =  (List<FileDetail>) modelMap.get("fileList");
		
		/** 1. 엑셀 파일 내부 sheet 생성 */
		sheet = workbook.createSheet("fileList"); 
		
		/** 2. 엑셀 헤더에 데이터 삽입  */
		renderCellHeader();
		
		/** 3. 엑셀 바디에 데이터 삽입  */
		renderCellBody(fileList);
		
		
	}
	
	/** 1. 엑셀 헤더에 데이터 삽입  */ 
	private void renderCellHeader() {
		Row headerRow = sheet.createRow(0);
		int columnIndex = 0;
		
		// FileDetail DTO 멤버변수에 접근
		for (Field field : new FileDetail().getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Annotation.class)) {
				
				// 각 필드의 적용되어 있는 어노테이션을 가져온다. 
				Annotation columnAnnotation = field.getAnnotation(Annotation.class);
				
				// @ExcelColumn(headerName = "아이디") 
		        String headerName = columnAnnotation.headerName();
		        Cell headerCell = headerRow.createCell(columnIndex++);
		        headerCell.setCellValue(headerName);
		        headerCell.setCellStyle(takeCellStyle("header"));
		        columnWidth(headerRow);
			}
		}
	}
	
	/** 2. 엑셀 바디에 데이터 삽입  */ 
	private void renderCellBody(List<FileDetail> FileList) throws IllegalArgumentException, IllegalAccessException{  
		
		for (int i = 0; i < FileList.size(); i++) {
			Row bodyRow = sheet.createRow(sheet.getLastRowNum() + 1); // 행 생성
			int columnIndex = 0;
			// Class 객체를 이용하여 해당 클래스의 생성자, 필드, 메소드 등 내부 정보를 조회 한다. ( REFLECTION )
			for(Field field : FileList.get(i).getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Annotation.class)) {
					
					field.setAccessible(true); // private 필드 ,메서드에 접근 하기 위한 설정 
					
					// cell 생성
					Cell bodyCell = bodyRow.createCell(columnIndex++);
					
					// filed의 값을 가져온다. :  get() 메소드에 해당 클래스의 인스턴스(new UserDetail())를 인자로 던져주면 값을 반환한다.
					Object cellValue = field.get(FileList.get(i)); 

					// filed 의 type 에 맞게 형변환을 해준다.
					if (cellValue instanceof Number) { 
						 Number numberValue = (Number) cellValue;
						 bodyCell.setCellValue(numberValue.doubleValue()); 
					} else if(cellValue instanceof String) {
						 bodyCell.setCellValue(cellValue.toString());
					}
					bodyCell.setCellStyle(takeCellStyle(""));
					columnWidth(bodyRow);
				}
			}
		}
	}
	
	/** 3. Cell Style 설정 */
	private CellStyle takeCellStyle(String cellType) {
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

		// 중앙 정렬
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 세로
		cellStyle.setAlignment(HorizontalAlignment.CENTER);       
		
		//본문 스타일 : 테두리
		cellStyle.setBorderBottom(BorderStyle.THICK);
		cellStyle.setBorderLeft(BorderStyle.THICK);
		cellStyle.setBorderRight(BorderStyle.THICK);
		cellStyle.setBorderTop(BorderStyle.THICK);
		
		if(cellType.equals("header")) {
			// Font 설정
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 13);
			font.setBold(true);
			
			// 배경색 지정 (R,G,B 컬러 사용)
			cellStyle.setFillForegroundColor((short)41); 
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 채우기 적용
			
			// row.setHeight((short)512);
		}
		return cellStyle;
	}
	
	/** 4. Column width 설정 */
	private void columnWidth(Row row) {
		// 컬럼 너비 자동 설정
		for(int colNum = 0; colNum<row.getLastCellNum(); colNum++) {
			// getLastCellNum : row의 유효한 컬럼 수 
			workbook.getSheetAt(0).autoSizeColumn(colNum); // 컬럼 넓이 자동 조절
			workbook.getSheetAt(0).setColumnWidth(colNum, (workbook.getSheetAt(0).getColumnWidth(colNum))+(short)1024); //  자동조정한 사이즈에 (short)1024 추가
		}
	}
}
