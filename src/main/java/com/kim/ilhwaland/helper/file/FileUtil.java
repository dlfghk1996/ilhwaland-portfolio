package com.kim.ilhwaland.helper.file;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.kim.ilhwaland.dto.FileDetail;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/** 파일 업로드/다운로드 관련 기능 */
@Component
public class FileUtil extends AbstractView{
	
	private final String uploadPath = "C:\\ILHAW\\fileBoard\\";           			 // 파일 게시판 업로드 경로
	private final String convertPath = "C:\\ILHAW\\fileBoard\\convert\\"; 			 // 파일 변환을 위한 임시 업로드 경로
	private final String summernoteUploadPath = "C:\\ILHAW\\board\\summernonte\\";   // 게시판 썸머노트에디터 이미지 업로드 경로

	private String path;
	
	/** 1. 파일  업로드 */
	public List<FileDetail> uploadFile(List<MultipartFile> multipartFiles,String uploadType)throws Exception{
		
		List<FileDetail> fileDetailList = new ArrayList<FileDetail>();
		String filename = "";
		// 업로드 파일 저장 경로 설정
		setFilePath(uploadType);
		for(MultipartFile multipartFile : multipartFiles) {
			 String originalFilename = multipartFile.getOriginalFilename();
			 // 파일이름 중복방지 
			 filename = UUID.randomUUID().toString();
			 // 파일 객체 생성
			 File file = new File(path, filename);
			
			 // file 디렉토리가 없을경우 디렉토리 생성
			 if(!file.exists()) {
				 file.mkdirs();
			 };
			 multipartFile.transferTo(file);
			 // 업로드 한 파일 정보 db에 저장하기 위해 객체 생성 후 리스트에 추가
		     fileDetailList.add(setFileDetail(originalFilename,filename,uploadType));
			 
		 }
		 return fileDetailList;
	 }
	 
	 /** 2. 파일 경로 생성*/
	 protected void setFilePath(String uploadType) {
		 // 현재 날짜로 폴더 생성
		 String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		 
		 // 업로드 타입 확인
		 if(uploadType.equals("upload")) {        // 파일 게시판 업로드
			 this.path = uploadPath+currentTime;
		 }else if(uploadType.equals("convert")) { // 파일 변환
			 this.path = convertPath;
		 }else {                                  // 게시판 썸머노트에디터 이미지 업로드
			 this.path = summernoteUploadPath+currentTime;
		 }
	 }
	 
	 /** 3. FileDetail 객체생성 */
	 protected FileDetail setFileDetail(String originalFilename,String filename,String uploadType) {
		 // summernote 업로드 일 경우 파일을 불러와야 되므로, sertvlet.xml 에서 설정한 경로로 수정한다.
		 if(uploadType.equals("summernoteUpload")) {
			 path = path.substring(path.indexOf("board"));
		 }
		 
		 // 파일 확장자 추출
		 String filetype = StringUtils.getFilenameExtension(originalFilename);
		 // 읽을 수 있는 파일 인지 확인
		 String[] filetypeList = {"pdf", "csv", "txt", "xls", "xlsx","jpg", "gif", "png", "jpeg"};
		 String readAble = Arrays.asList(filetypeList).contains(filetype)?"y":"n";
		 
		 FileDetail fileDetail = new FileDetail(0, originalFilename, filename, filetype, path, readAble);
		 
		 return fileDetail;
	 }

	 /** 4. 파일 다운로드  */
	 @Override
	 public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
		
		 // 업로드 파일 다운로드
		 if(model.containsKey("fileDownload")) {
			 FileDetail fileDetail = (FileDetail) model.get("fileDownload");
			 
			 // 다운로드 할 파일 경로
			 String filePath =  fileDetail.getFilePath() + "\\" +fileDetail.getFile_name(); // 파일을 읽기위한 파일 경로
			 // 파일 다운로드 설정
			 prepareResponse(request,response,fileDetail.getOriginal_name(),"application/octet-stream");
			 try {		
				 // 5. 바이트 단위로 파일을 읽어들인다.
				 FileInputStream fileInputStream = new FileInputStream(new File(filePath));
				 
				 BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				 
				 // 6. 지정한 InputStream의 내용을, 지정한 OutputStream에 복사하고, 스트림을 닫는다. 리턴값 : copy한 byte 수
				 OutputStream outputStream = response.getOutputStream();
				 
				 FileCopyUtils.copy(bufferedInputStream, outputStream); // 스프링 프레임워크에서 제공하는 파일 다운로드 기능 (while,flush,close)
			
				 if(fileInputStream != null) {
					 fileInputStream.close();
				 }
				 bufferedInputStream.close();
				 outputStream.close();
				 
			 } catch (FileNotFoundException e) {
					e.printStackTrace();
			 } catch (UnsupportedEncodingException e) {
					e.printStackTrace();
			 } catch (IOException e) {
					e.printStackTrace();
			 }
	
		// DB 에 있는 data csv 로 다운로드
		}else if(model.containsKey("dataToCsv")) {
			System.out.println("fileUtil ToCsv");
			List<FileDetail> fileList = (List<FileDetail>) model.get("dataToCsv");
			// 파일 다운로드 설정
			prepareResponse(request,response,"다운로드내역.csv","text/csv; charset=UTF-8");			
			downloadCsv(response,fileList);
		
		// img -> pdf 로 변환	
		}else if(model.containsKey("imgToPdf")) {
			FileDetail fileDetail = (FileDetail) model.get("imgToPdf");
			// 파일 다운로드 설정
			prepareResponse(request,response,fileDetail.getOriginal_name().replace(fileDetail.getFiletype(), "pdf"),"application/pdf");
			
			downloadPdf(response,fileDetail);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		
		// pdf -> img 변환
		}else if(model.containsKey("pdfToImg")) {
			 FileDetail fileDetail = (FileDetail) model.get("pdfToImg");
			 String content = downloadImg(response,fileDetail);
	
			 String original_name = fileDetail.getOriginal_name();
			 // 파일 압축 처리 
			 if(!(content.equals("application/zip"))) { // 단일 이미지 파일
				 prepareResponse(request,response,original_name.replace(fileDetail.getFiletype(), "png"),"application/octet-stream");
				 downloadStream(response,content);
			 }else { // 복수 이미지 파일
				 prepareResponse(request,response,original_name.replace(fileDetail.getFiletype(), "zip"),"application/zip");
				 response.getOutputStream().flush();
				 response.getOutputStream().close();
			 }
		}
	 }
	 
     /** 5. 응답헤더 설정*/
	 protected void prepareResponse(HttpServletRequest request,HttpServletResponse response,String original_name,String contentType) {
		 try {
			 // 1. 브라우저, 운영체제정보에 따른 파일 이름 인코딩 설정
			 String downloadFileName = "";
			 String userBrowser = request.getHeader("User-Agent");
			 if(userBrowser.contains("Trident") || userBrowser.contains("MSIE")) {
				 downloadFileName = URLEncoder.encode(original_name, "UTF-8").replaceAll("\\+", " "); // IE
			 }else {
				downloadFileName = new String(original_name.getBytes("UTF-8"), "ISO-8859-1"); // Chorme 등
			 }
			 // 2. 읽어온 파일 정보를 화면에서 다운로드 할 수 있게 변환 설정
			 response.setContentType(contentType);  // MIME Type
			 
			 // 3. 다운로드 옵션 설정 : 데이터 형식, 첨부파일, 다운로드 되는 파일 이름 
			 response.setHeader("Content-Disposition", "attachment; fileName=\"" + downloadFileName +"\";");
			 
			 // 4. 전송되는 데이터의 안의 내용물들의 인코딩 방식
			 response.setHeader("Content-Transfer-Encoding", "binary");
		 } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		 }
	 }
	 
	 /** 6. 파일 경로를 읽어들여 다운로드 방식*/
	 protected void downloadStream(HttpServletResponse response,String filePath) {
		 try {		
			 // 5. 바이트 단위로 파일을 읽어들인다.
			 FileInputStream fileInputStream = new FileInputStream(new File(filePath));
			 
			 BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			 
			 // 6. 지정한 InputStream의 내용을, 지정한 OutputStream에 복사하고, 스트림을 닫는다. 리턴값 : copy한 byte 수
			 OutputStream outputStream = response.getOutputStream();
			 
			 FileCopyUtils.copy(bufferedInputStream, outputStream); // 스프링 프레임워크에서 제공하는 파일 다운로드 기능 (while,flush,close)
		
			 if(fileInputStream != null) {
				 fileInputStream.close();
			 }
			 bufferedInputStream.close();
			 outputStream.close();
			 
		 } catch (FileNotFoundException e) {
				e.printStackTrace();
		 } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
		 } catch (IOException e) {
				e.printStackTrace();
		 }
	 }

	/** 7. CSV 생성 */	 
	public void downloadCsv(HttpServletResponse response,List<FileDetail> fileList) {
		/** - StatefulBeanToCsv 객체 : bean 객체를 바로 CSV를 변환, 각각 컬럼 명을 빈의 필드명으로 사용한다. 순서는 무작위
        	- StatefulBeanToCsvBuilder 빌더 : CSV 파일 작성에 필요한 모든 매개 변수를 설정한다. (Param : 빈의 csv 버전을 출력하는 데 사용되는 작성기.)
        	- Mapping Stategy 객체 : 헤더 이름 매핑전략을 사용, 헤더 순서를 일정.
		 **/
		try {			
	        CSVWriter csvWriter;
			csvWriter = new CSVWriter(response.getWriter(),
				        CSVWriter.DEFAULT_SEPARATOR,
				        CSVWriter.NO_QUOTE_CHARACTER,
				        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
				        CSVWriter.DEFAULT_LINE_END);
	        
			// 고유 한 매핑 설정 인스턴스화
	    	CustomMappingStrategy<FileDetail> strategy = new CustomMappingStrategy<>();
	    	strategy.setType(FileDetail.class);
	    
	    	// 매핑 설정을 StatefulBeanToCsvBuilder에 전달
	        StatefulBeanToCsv<FileDetail> beanToCsv = new StatefulBeanToCsvBuilder<FileDetail>(csvWriter).withMappingStrategy(strategy).build();
			
	        // CSV 형식으로 빈을 작성
	        beanToCsv.write(fileList);
	   
		}catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException  e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 8. pdf 생성 */	 
	public void downloadPdf(HttpServletResponse response,FileDetail fileDetail) {
		// pdf 문서 생성
		Document document = new Document(PageSize.A4); 
		
		// 지정된 자료를 출력할 수 있는 곳을 지정함.
		PdfWriter pdfWriter;
		try {
			pdfWriter = PdfWriter.getInstance(document,response.getOutputStream());
			document.open(); // 문서 오픈
		
			// 이미지 객체 생성
			Image img = Image.getInstance(fileDetail.getFilePath()+"\\"+fileDetail.getFile_name());
			document.add(img);
		
			document.close();
			pdfWriter.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 임시파일 삭제
	}
	
	/** 9. pdf-> img 생성 */
	public String downloadImg(HttpServletResponse response,FileDetail fileDetail) {
		String content =""; // 변환 파일 갯수에 따른 다운로드 처리 방식 구분
		PDDocument pdDocument; 
		try {
			pdDocument = PDDocument.load(new File(fileDetail.getFilePath()+"\\"+fileDetail.getFile_name()));
		
			PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);   // PDF 문서를 렌더링
						
			List<String> savedImgList = new ArrayList<>(); //저장된 이미지 경로를 저장하는 List 객체
			String imgFileName = "";
			// pdf 전체 페이지를 읽으면서 이미지 파일로 변환
			for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
				String filename = fileDetail.getOriginal_name().substring(0, fileDetail.getOriginal_name().indexOf("."));
				imgFileName = convertPath + "\\"+ filename +"(" +i +")" +".png"; 
				
				// 이미지 객체 생성 : BufferedImage(int width, int height, int imageType)
				BufferedImage bufferedImage;
				bufferedImage = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
				// 이미지로 변환
	            ImageIOUtil.writeImage(bufferedImage, imgFileName , 300);
	            //저장 완료된 이미지를 list에 추가한다.
	            savedImgList.add(imgFileName);
			}

			// pdf 페이지가 여러개 일 경우 zip 압축
			if( pdDocument.getNumberOfPages() > 1) {
				content = "application/zip";
				createZip(savedImgList,response);
			// pdf 페이지가 한개 일 경우 png로 반환
			}else {
				content = imgFileName;
			}
			pdDocument.close();
		}catch (InvalidPasswordException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 임시파일 삭제
		return content;
	}
	
	/** 10. zip 압축 */
	public void createZip(List<String> savedImgList,HttpServletResponse response) throws IOException {
		byte[] buffer = new byte[1024];
		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream()); 
		// 본래 파일명 유지, 압축하는 파일 추가
		//해당경로의 파일들을 루프
		for (int i = 0; i < savedImgList.size(); i++) {
			//스트림으로 파일을 읽음
			FileInputStream inputStream = new FileInputStream(savedImgList.get(i)); // Stream to read file
			// 파일의 이름
			String filename =  savedImgList.get(i).substring(savedImgList.get(i).lastIndexOf("\\")+1);
			
		    //zip파일을 만들기 위하여 out객체에 write하여 zip파일 생성
			ZipEntry zipEntry = new ZipEntry(filename); // Make a ZipEntry
			zipOutputStream.putNextEntry(zipEntry); // zip 파일에 저장할 파일들 구별	
			
			int length;
			while ((length = inputStream.read(buffer)) > 0 ) {
				zipOutputStream.write(buffer, 0, length);
			  }
			inputStream.close();
        }
		zipOutputStream.closeEntry();
		zipOutputStream.close();
        	
		
	}
	
	/** 11. 파일 삭제 */
	public void deleteFile() throws IOException {
		try {
		    File rootDir = new File(convertPath);
		    FileUtils.deleteDirectory(rootDir);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	/** 12. summernote 이미지 삭제*/
	public boolean findImgPath(String imgstr) {
		boolean result = true;
		// 이미지 태그를 추출하기 위한 정규식.
		Pattern imgPattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
		List<String> imgPathList = new ArrayList<String>();
		// 패턴을 target(imgstr)과 매치 
		Matcher matcher = imgPattern.matcher(imgstr);
		while (matcher.find()) {
			imgPathList.add(matcher.group(1)); 
			if(!matcher.find()) {
				return false;
			}
		}
		for(String imgPath: imgPathList) {
			 File file = new File("C:/ILHAW/"+ imgPath);
			 if(file.exists()) {
				 result = file.delete();
				 if(result == false) {
					System.out.println("예외처리");
				 }
			 }
		}
		return result;
	 }
}	 

	 
	 
	 
	 

