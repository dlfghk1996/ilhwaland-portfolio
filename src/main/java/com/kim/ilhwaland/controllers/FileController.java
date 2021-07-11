package com.kim.ilhwaland.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
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

@Controller
public class FileController {
	
	@Autowired
	private FileDao fileDao;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private FileApi fileApi;
	
	/** [VIEW] 업로드 파일 게시판  페이지 */
	@RequestMapping(value="fileBoard")
	public String fileBoard(Model model, HttpServletResponse response){

		List<FileDetail> fileList = new ArrayList<FileDetail>();
		try {
			fileList = fileDao.getUploadFileList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("fileList", fileList);
		return "file/fileBoard";
	}
	
	/** [VIEW] 파일 변환 페이지 */
	@RequestMapping(value="fileConvertPage",method = RequestMethod.GET)
	public String fileConvertPage(Model model, HttpServletResponse response, HttpServletRequest request){

		return "file/fileConvert";
	}
	
	/** [VIEW] DB데이터 자료실 페이지 */
	@RequestMapping(value="dataArchive",method = RequestMethod.GET)
	public String dataArchive(Model model, HttpServletResponse response){

		List<FileDetail> fileList = new ArrayList<FileDetail>();
		try {
			fileList = fileDao.getUploadFileList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("fileList", fileList);
		return "file/dataArchive";
	}
	
	
	/** 파일 업로드 ( 다중 파일 업로드 가능 ) */
	@RequestMapping(value="fileUpload", method = RequestMethod.POST)
	public String fileUpload(MultipartHttpServletRequest multipartRequest,Model model, HttpServletRequest request){
		
		// 넘어온 파일을 리스트로 저장
        List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
        try {
			List<FileDetail> fileDetailList = fileUtil.uploadFile(multipartFiles,"upload");
			
			// DB 에 업로드 하는 파일 경로 저장
			int result = fileDao.setUploadFile(fileDetailList);
			if(result  == 0) {
				System.out.println("예외처리");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/fileBoard";
	}
	
	/** 파일 다운로드 */
	@RequestMapping(value="fileDownload", method = RequestMethod.GET)
	public void fileDownload(@RequestParam("file_num") String file_num,HttpServletRequest request,HttpServletResponse response){
		try {
			// 파일 정보를 가져온다.
			FileDetail fileDetail = fileDao.getUploadFile(Integer.parseInt(file_num));
			if(fileDetail == null) {
				System.out.println(" 존재하지 않는 파일 ");
			}
			// 다운로드
			Map<String, Object> map = new HashMap<>();
			map.put("fileDownload", fileDetail);
			fileUtil.renderMergedOutputModel(map, request,response);
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/** 파일 내용 미리보기 */
	@RequestMapping(value="fileViewer", method = RequestMethod.GET)
	public String fileViewer(@RequestParam("file_num") String file_num,Model model){
		FileViewHelper fileViewHelper = new FileViewHelper(); // 읽은 파일을 담을 객체 생성
		try {
			FileDetail fileDetail = fileDao.getUploadFile(Integer.parseInt(file_num)); // 파일 정보를 가져온다.
			if(fileDetail == null) {
				System.out.println("예외처리");
			}
			String filetype = fileDetail.getFiletype();
			fileViewHelper.setFileType(filetype);  // view에서 분기처리 하기 위한 구분자 값을 넣는다.
			String filePath =  fileDetail.getFilePath() + "\\" +fileDetail.getFile_name(); // 파일을 읽기위한 파일 경로
			
			File file = new File(filePath);
			
			// 파일 타입에 맞는 방식으로, 파일을 읽고 fileView 객체에 담는다.
			switch (filetype) {
			case "xlsx": case "xls":
				fileApi.readExcel(file);
				
				fileViewHelper.setExcelMap(FileApi.sheetMap);
				FileApi.sheetMap = new HashMap<String,List<List<String>>>();
				
				
				// 초기화
				break;
			case "csv": 
				fileViewHelper.setCsvList(fileApi.readCsv(file));
				break;
			case "pdf" :
				filePath =  "fileBoard/" + fileDetail.getRegister_date() + "/" +fileDetail.getFile_name();
				String embedHtml = "<embed src='"+filePath+"' type='application/pdf' width='100%' />";
				fileViewHelper.setSourcePath(embedHtml);
				break;
			case "jpg": case "gif" : case "png" : case "bmp": 
				filePath =  "fileBoard/" + fileDetail.getRegister_date() + "/" +fileDetail.getFile_name();
				String imgHtml = "<img src='"+filePath+"'width='100%' />";
				fileViewHelper.setSourcePath(imgHtml);
				break;
			case "txt" : 
				fileViewHelper.setTxtList(fileApi.readTxt(file));
				break;
			default:
				break;
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("fileView", fileViewHelper);
		return "file/viewTemplate";
	}
	

	
	/** DB 데이터 xlxs 형식으로 다운로드 */
	@RequestMapping(value="excelDownload",method = RequestMethod.GET)
	public String excelDownload(Model model, HttpServletResponse response){
		try {
			List<FileDetail> fileList = fileDao.getUploadFileList();
			model.addAttribute("fileList", fileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "excelDownload";
	}
	
	/** DB 데이터 csv 형식으로 다운로드 */
	@RequestMapping(value="csvDownload")
	public void downloadToCsv(Model model, HttpServletResponse response, HttpServletRequest request){
		try {
			List<FileDetail> fileList = fileDao.getUploadFileList();
			// 다운로드
			Map<String, Object> map = new HashMap<>();
			map.put("dataToCsv", fileList);
			fileUtil.renderMergedOutputModel(map, request,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 파일 변환 */
	@RequestMapping(value="fileConvert",method = RequestMethod.POST)
	public void fileConvert(MultipartHttpServletRequest multipartRequest, Model model, HttpServletResponse response, HttpServletRequest request){
		try {
			
			List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
			  
			// 사용자가 요청한 파일을 변환하기 위해 사용자가 올린 파일을 로컬에 저장한다.
			List<FileDetail> fileDetailList = fileUtil.uploadFile(multipartFiles,"convert");
			String fileType = fileDetailList.get(0).getFiletype();
			// 다운로드
			System.out.println(fileType);
			Map<String, Object> map = new HashMap<>();
			String key = fileType.equals("pdf")?"pdfToImg":"imgToPdf";
			map.put(key, fileDetailList.get(0));

			fileUtil.renderMergedOutputModel(map,request,response);
			// 임시 파일들 삭제
			fileUtil.deleteFile();
		}catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
}
