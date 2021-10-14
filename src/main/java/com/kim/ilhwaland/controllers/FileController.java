package com.kim.ilhwaland.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.kim.ilhwaland.dao.FileDao;
import com.kim.ilhwaland.dto.FileDetail;
import com.kim.ilhwaland.helper.file.FileApi;
import com.kim.ilhwaland.helper.file.FileUtil;
import com.kim.ilhwaland.helper.file.FileViewHelper;
import com.kim.ilhwaland.helper.file.SheetHelper;

@Controller
public class FileController {
	
	@Autowired
	private FileDao fileDao;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private FileApi fileApi;
	
	
	/** [VIEW] 업로드 파일 게시판  페이지 : 파일 업로드 & 미리보기 & 다운로드 */
	@RequestMapping(value="fileBoard")
	public String fileBoard(Model model, HttpServletResponse response) throws Exception {

		List<FileDetail> fileList = new ArrayList<FileDetail>();
		
		fileList = fileDao.getUploadFileList();
		
		model.addAttribute("fileList", fileList);
		return "file/fileBoard";
	}
	
	/** [VIEW] 파일 변환 페이지 */
	@RequestMapping(value="fileConvertPage",method = RequestMethod.GET)
	public String fileConvertPage(Model model, HttpServletResponse response, HttpServletRequest request){

		return "file/fileConvert";
	}
	
	/** [VIEW] DB데이터 자료실 페이지 : 데이터를  xlsx & csv 확장자로 다운로드 */
	@RequestMapping(value="dataArchive", method = RequestMethod.GET)
	public String dataArchive(Model model, HttpServletResponse response) throws Exception {

		List<FileDetail> fileList = new ArrayList<FileDetail>();
		
		fileList = fileDao.getUploadFileList();
		
		model.addAttribute("fileList", fileList);
		return "file/dataArchive";
	}
	
	
	/** [Method 01] 파일 업로드 ( 다중 파일 업로드 가능 ) */
	@RequestMapping(value="fileUpload", method = RequestMethod.POST)
	public String fileUpload(MultipartHttpServletRequest multipartRequest,Model model, HttpServletRequest request) throws Exception {
		
		// 넘어온 파일을 리스트로 저장
        List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
       
        // 파일 저장후, 파일객체 생성
		List<FileDetail> fileDetailList = fileUtil.uploadFile(multipartFiles,"upload");
			
		// DB 에 업로드 하는 파일 경로 저장
		fileDao.setUploadFile(fileDetailList);

		return "redirect:/fileBoard";
	}
	
	
	/** [Method 02] 파일 다운로드 */
	@RequestMapping(value="fileDownload", method = RequestMethod.GET)
	public void fileDownload(@RequestParam("file_num") String file_num,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		// 파일 정보를 가져온다.
		FileDetail fileDetail = fileDao.getUploadFile(Integer.parseInt(file_num));
		
		// renderMergedOutputModel 메서드의 매개변수
		Map<String, Object> map = new HashMap<>();
		map.put("fileDownload", fileDetail);
		
		/* @Explan : RequestDispatcher를 통해 JSP를 포함하는 뷰의 실제 렌더링 메서드
		   @Parmeter : 파일 타입별 다운로드 방식으로 구현하기 위한 구분자   */ 
		fileUtil.renderMergedOutputModel(map, request,response);	
	}
	
	
	/** [Method 03] 파일 내용 미리보기 */
	@RequestMapping(value="fileViewer", method = RequestMethod.GET)
	public String fileViewer(@RequestParam("file_num") String file_num,Model model,HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		// retrun Object : 요청한 파일을 읽고 해당 파일의 데이터 형태를 유지하면서, 데이이터를 저장하는 객체 
		FileViewHelper fileViewHelper = new FileViewHelper(); 
		
		// 1. DB에서 요청한 파일의 경로, 업로드일자 등의 상세정보를 가져온다.
		FileDetail fileDetail = fileDao.getUploadFile(Integer.parseInt(file_num)); 

		// 2. 요청한 파일에 접근 하기 위해, DB에서 가져온 상세정보를 조합하여, 파일경로 형식에 맞게 구성한다. 
		//String filePath = fileDetail.getFilePath() + "\\" +fileDetail.getFile_name();
		String filePath = fileDetail.getFilePath() + "/" +fileDetail.getFile_name();
		// 3. 파일 타입 마다 읽는 방식이 다르기때문에, 파일 타입을 구분자로 하여 기능을 구현하기위한 구분자 변수에 파일타입명 할당.
		String filetype = fileDetail.getFiletype();
		
		// 4. 파일 객체 생성
		File file = new File(filePath);
		
		// 5. 파일 타입 별  데이터를 읽어오는 방식으로  파일을 읽고 fileViewHelper DTO 에 결과값을 담는다.
		switch (filetype) {
			// 엑셀 파일
			case "xlsx": case "xls":
				
				fileApi.readExcel(file);
		
				fileViewHelper.setSheetHelper(FileApi.sheet_list);
				// 초기화
				FileApi.sheet_list = new ArrayList<SheetHelper>();
				break;
			
			// csv 파일
			case "csv": 
				fileViewHelper.setCsvList(fileApi.readCsv(file));
				break;
				
			// pdf 파일
			case "pdf" :
				// filePath = "fileboard\\" + fileDetail.getRegister_date() + "\\" + fileDetail.getFile_name() + ".pdf";
				filePath = "fileboard/" + fileDetail.getRegister_date() + "/" + fileDetail.getFile_name() + ".pdf";
				fileViewHelper.setSourcePath(filePath);
				
				break;
				
			// img 파일 
			case "jpg": case "gif" : case "png" : case "bmp": 
				//filePath = "fileboard\\" + fileDetail.getRegister_date() + "\\" + fileDetail.getFile_name();
				filePath = "fileboard/" + fileDetail.getRegister_date() + "/" + fileDetail.getFile_name();
				String imgHtml = "<img src='"+filePath+"'width='100%'/>";
				fileViewHelper.setSourcePath(imgHtml);
				break;
			case "txt" : 
				fileViewHelper.setTxtList(fileApi.readTxt(file));
				break;
			default:
				break;
		}
		// view에서 분기처리 하기 위해, 반환 객체 필드에 파일타입을 저장한다.
		fileViewHelper.setFileType(filetype); 
		model.addAttribute("fileView", fileViewHelper);
		
		return "file/viewTemplate";
	}
	
	
	/** [Method 04] DB 데이터를 xlxs 형식으로 다운로드 */
	@RequestMapping(value="excelDownload", method = RequestMethod.GET)
	public String excelDownload(Model model, HttpServletResponse response) throws Exception {
		
		// DB에서 요청한 파일의 경로, 업로드일자 등의 상세정보를 가져온다.
		List<FileDetail> fileList = fileDao.getUploadFileList();
		
		model.addAttribute("fileList", fileList);
		
		// 이름이 "excelDownload"인 beans를  찾아 view를 매핑한다.(BeanNameViewResolver)
		return "excelDownload"; 
	}
	
	
	/** [Method 05] DB 데이터 csv 형식으로 다운로드 */
	@RequestMapping(value="csvDownload")
	public void downloadToCsv(Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		// DB에서 요청한 파일의 경로, 업로드일자 등의 상세정보를 가져온다.
		List<FileDetail> fileList = fileDao.getUploadFileList();
		
		// renderMergedOutputModel 메서드의 매개변수
		Map<String, Object> map = new HashMap<>();
		map.put("dataToCsv", fileList);
		
		/* 5. 파일 변환 후  출력 
		 * @Explan : RequestDispatcher를 통해 JSP를 포함하는 뷰의 실제 렌더링 메서드
		   @Parmeter : 파일 타입별 다운로드 방식으로 구현하기 위한 구분자   */ 
		fileUtil.renderMergedOutputModel(map, request, response);
	}

	
	/** [Method 06] PDF => IMG OR IMG => PDF 로 타입 변환 */
	@RequestMapping(value="fileConvert", method = RequestMethod.POST)
	public void fileConvert(MultipartHttpServletRequest multipartRequest, Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		// 1. MultipartHttpServletRequest 타입의 파라미터를 이용해서 업로드 파일 데이터를 전달받는다.
		List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
		
		/* 2. 사용자가 요청한 파일을 접근하여, 사용할수 있도록  사용자가 올린 파일을 내 프로젝트 내부에 임시로 저장한다.
		      @Parmeter : 업로드메서드를 호출한 지점의 기능명 */ 
		List<FileDetail> fileDetailList = fileUtil.uploadFile(multipartFiles,"convert");
		
		// 3. 사용자가 요청한 파일 타입을 변수에 할당
		String fileType = fileDetailList.get(0).getFiletype();

		// 4. renderMergedOutputModel 메서드의 매개변수
		Map<String, Object> map = new HashMap<String, Object>();
		String key = fileType.equals("pdf")?"pdfToImg":"imgToPdf";
		map.put(key, fileDetailList.get(0));
		
		/* 5. 파일 변환 후  출력 
		 * @Explan : RequestDispatcher를 통해 JSP를 포함하는 뷰의 실제 렌더링 메서드
		   @Parmeter : 파일 타입별 다운로드 방식으로 구현하기 위한 구분자   */ 
		fileUtil.renderMergedOutputModel(map,request,response);
		
		// 7. 요청한 파일의 타입 변환 및 출력 이후에는, 파일을 사용하지 않기 때문에 해당 파일을 내 프로젝트 내부 업로드 폴더에서 삭제한다.
		fileUtil.deleteFile();
	}
}
