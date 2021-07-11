package com.kim.ilhwaland.helper.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.opencsv.CSVReader;

import com.opencsv.exceptions.CsvValidationException;

/** 업로드된 파일을 읽기 위한, 파일 타입별 api */
@Component
public class FileApi {
   
   public static String sheetnames = null;
   public static Map<String,List<List<String>>> sheetMap = new HashMap<String,List<List<String>>>();  // xlxs 데이터 반환 객체
   public static List<List<String>> rows = new ArrayList<List<String>>();
   
   /** 1. xls, xlsx 읽기*/
   // 단일 시트 또는 파일의 모든 시트를 가져 오기
   public SheetHandler readExcel(File file) throws Exception {
		SheetHandler sheetHandler= new SheetHandler();
		
		// 1. OPCPackage : 파일을 읽거나 쓸수 있는 상태의 컨테이너 생성 
		OPCPackage opcPackage = OPCPackage.open(file);
		
		// 2. XSSFReader : OPC 컨테이너를 XSSF 형식으로 읽어옴
		XSSFReader xssfReader = new XSSFReader(opcPackage); // 문자열 테이블과 스타일에 대한 인터페이스를 선택적으로 제공

		// 3. 파일의 데이터를 Table형식으로 읽을 수 있도록 지원
		ReadOnlySharedStringsTable sharedStringsTable = new ReadOnlySharedStringsTable(opcPackage);
		
		// 4. 읽어온 Table에 적용되어 있는 Style
		StylesTable stylesTable = xssfReader.getStylesTable();
		
		// 5. SAX parser 방식의  XMLReader : XML 데이터를 이동하고 노드의 내용을 읽는다
		XMLReader xmlReader = SAXHelper.newXMLReader();

		// 6. XSSFSheetXMLHandler : 엑셀 Sheet 또는 XML의 처리를 위해 행 및 셀 이벤트를 생성
		ContentHandler handler = new XSSFSheetXMLHandler(stylesTable, sharedStringsTable, sheetHandler, false);
		
		// 7. 현재 ContentHandler를 설정. ContentHandler가 설정되지 않으면, 내용 이벤트가 버려진다. 
		xmlReader.setContentHandler(handler);
		
       //InputStream sheet = xssfReader.getSheetsData().next();
	
		// 복수 Sheets일 경우 : sheet별 collection으로 분할해서 가져옴
		SheetIterator itr = (SheetIterator)xssfReader.getSheetsData();
	
		while(itr.hasNext()) {

			InputStream sheet = itr.next();
			sheetnames = itr.getSheetName();
			// 8. 엔티티를 읽기 위해 XMLReader에 필요한 정보를 캡슐화 한다. -> 읽을 InputSource 반환
	        InputSource sheetSource = new InputSource(sheet);

	        // 9. source로 입력된 XML문서를 파싱하면서 SAX이벤트를 발생
	        xmlReader.parse(sheetSource);
	        
	        sheetMap.put(sheetnames,rows);
	        rows = new ArrayList<List<String>>();
	        
	        sheet.close();
		}
      
       // 10. xml 문서 안의 정보들이 파싱되면서 순서대로 이벤트가 ContentHandler을 통해서 호출된다
       return sheetHandler;
   }
//   public void processSheet(StylesTable stylesTable, ReadOnlySharedStringsTable sharedStringsTable, SheetHandler2 sheetHandler,InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
//         
//           try {
//              // 1. SAX parser 방식의  XMLReader : XML 데이터를 이동하고 노드의 내용을 읽는다
//               XMLReader xmlReader = SAXHelper.newXMLReader();
//               // 2. XSSFSheetXMLHandler : 엑셀 Sheet 또는 XML의 처리를 위해 행 및 셀 이벤트를 생성
//               //ContentHandler handler = new XSSFSheetXMLHandler(stylesTable, sharedStringsTable, (SheetContentsHandler) sheetHandler, false);
//               ContentHandler handler = new SheetHandler2(sharedStringsTable);
//               // 3. 현재 ContentHandler를 설정. ContentHandler가 설정되지 않으면, 내용 이벤트가 버려진다. 
//               xmlReader.setContentHandler(handler);
//               // 4. 엔티티를 읽기 위해 XMLReader에 필요한 정보를 캡슐화 한다. -> 읽을 InputSource 반환
//               InputSource sheetSource = new InputSource(sheetInputStream);
//               // 5. source로 입력된 XML문서를 파싱하면서 SAX이벤트를 발생
//               xmlReader.parse(sheetSource);
//               sheetInputStream.close();
//           } catch (ParserConfigurationException e) {
//               throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
//           }
//    }
   
   /** 2. CSV 읽기*/
   public List<String[]> readCsv(File file) throws Exception {
       // "," or 개행문자 or "" 처리 해야됨
      CSVReader csvReader = new CSVReader(new FileReader(file)); // FileReader : 문자 단위로 처리 (<-> InputStream 바이트 단위)
      List<String[]> csvList = new ArrayList<String[]>();
   
      try {
         String[] inputLine;
         // 라인단위로 데이터를 읽는다.
         while ((inputLine = csvReader.readNext()) != null) {
            int i=0;
            String [] tempLine = new String[inputLine.length];
            // 개행문자 제거
            for(String field : inputLine) { 
               tempLine[i++] = field.replace("\r\n", "a");
            }
            csvList.add(tempLine);
         }
         csvReader.close();
      } catch (CsvValidationException e) {
         e.printStackTrace();
      }catch (IOException e) {
         e.printStackTrace();
      }
      
      return csvList;
   }
   
   /** 3. TXT 읽기*/
   public List<String> readTxt(File file) throws Exception {
      List<String> txtList = new ArrayList<String>();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file)); 
      String line = null; 
      while((line = bufferedReader.readLine()) != null ) {
            txtList.add(line);
         }
      bufferedReader.close();
      return txtList;
   }
}