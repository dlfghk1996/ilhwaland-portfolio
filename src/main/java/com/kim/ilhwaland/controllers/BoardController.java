package com.kim.ilhwaland.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kim.ilhwaland.dao.BoardDao;
import com.kim.ilhwaland.dao.BoardReplyDao;
import com.kim.ilhwaland.dto.Board;
import com.kim.ilhwaland.dto.BoardReply;
import com.kim.ilhwaland.dto.FileDetail;
import com.kim.ilhwaland.helper.WebHelpler;
import com.kim.ilhwaland.helper.file.FileUtil;

@Controller
public class BoardController {
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private BoardReplyDao boardReplyDao;
	
	@Autowired
	private WebHelpler webHelper;
	
	/** [VIEW] 게시판 */
	@RequestMapping(value="board",method = RequestMethod.GET)
	public String fileConvertPage(Model model, Board board,
			@RequestParam(value = "nowPage", defaultValue = "1", required = false) int pnum ){
		/** 페이지 네이션 변수 설정 */
		// 현재 페이지
		int nowPage = pnum;
		int totalCount = 0;
		// 한 페이지당 표시할 게시글 수
		int listCount = 10;
		// 한 그룹당 표시할 페이지 번호 수
		int pageCount = 4;
		
		try {
			totalCount = boardDao.getotalContent(); // 전체 게시글 수
			String pagenation = webHelper.pagenation("board", nowPage, totalCount, listCount, pageCount);
		
			// 디비 조회 관련 setter
			board.setStart(webHelper.getStart());
			board.setEnd(webHelper.getEnd());
			List<Board> boardList = boardDao.getBoardList(board);
			model.addAttribute("boardList", boardList);
			model.addAttribute("pagenation", pagenation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "board/board";
	}
	
	/** [VIEW] 게시판 글쓰기 */
	@RequestMapping(value="boardwrite",method = RequestMethod.GET)
	public String boardwrite(Board board){

		return "board/boardWrite";
	}
	
	/** [VIEW] 게시글 수정 페이지 */
	@RequestMapping(value="boardUpdate",method = RequestMethod.POST)
	public String boardUpdate(int board_num,Model model){

		System.out.println("boardUpdate : " +board_num);
		try {
			Board board = boardDao.getBoardContent(board_num);
			model.addAttribute("board", board);
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		return "board/boardUpdate";
	}
	
	/** 비밀번호 확인 */
	@RequestMapping(value="passwordCheck",method = RequestMethod.POST)
	public @ResponseBody String passwordCheck(@RequestBody Map<String, String> map){
		String checkType = map.get("checkType");
		Board board = new Board();
		board.setBoard_num(Integer.parseInt(map.get("board_num")));
		board.setPassword( map.get("password"));
		try {
			board = boardDao.checkBoardPassword(board);
			
			if(board == null) {
				return "0"; 
			}else {
				if(checkType.equals("delete")) { // 삭제
					// 본문 이미지 지우기
					fileUtil.findImgPath(board.getContent()); 
					boardReplyDao.deleteReply(board.getBoard_num()); // 댓글 지우기
					boardDao.deleteBoardContent(board.getBoard_num()); // 게시글 지우기
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1";
	}

	
	/** 게시판 글작성 */
	@RequestMapping(value = "postContent", method = RequestMethod.POST)
	public String postContent(Board board) {

		try {
			boardDao.setBoardContent(board);
		} catch (Exception e) {
			System.out.println("예외처리 : " + e.getMessage());
		}
		// 글 작성 후, 작성 된 글을 보여준다.
		return "redirect:boardView?board_num=" + board.getBoard_num();
	}
	
	/** 게시글 상세 보기 */
	@RequestMapping(value = "boardView", method = RequestMethod.GET)
	public String boardView(@RequestParam("board_num")int board_num,Model model) {
		try {
			Board board= boardDao.getBoardContent(board_num);
			List<BoardReply> boardReplyList = boardReplyDao.getReplyList(board_num);
			
			model.addAttribute("board", board);
			model.addAttribute("boardReplyList", boardReplyList);
		} catch (Exception e) {
			System.out.println("예외처리 : " + e.getMessage());
		}
		return "board/boardView";
	}
	
	/** 썸머노트 이미지 업로드 */
	@RequestMapping(value = "uploadSummernoteImage", method = RequestMethod.POST)
	public @ResponseBody String uploadSummernoteImage(MultipartHttpServletRequest multipartRequest) throws Exception {
		// 넘어온 파일을 리스트로 저장
        List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
        
		// 이미지 외부 경로에 저장
		List<FileDetail> fileDetails = fileUtil.uploadFile(multipartFiles, "summernoteUpload");
		FileDetail fileDetail = fileDetails.get(0);
        // 경로 반환
		return fileDetail.getFilePath()+"\\"+fileDetail.getFile_name(); 
	}

	/** 게시글 수정 */
	@RequestMapping(value = "updateContent", method = RequestMethod.POST)
	public String UpdateContent(Board board) {
		try {
			boardDao.updateBoardContent(board);
			// 게시글 수정시 본문이미지 변경
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "redirect:boardView?board_num=" + board.getBoard_num();
	}
	
	/** 댓글 등록 */
	@RequestMapping(value = "replyWrite", method = RequestMethod.POST)
	@ResponseBody
	public String replyWrite(BoardReply board_reply) {
		try {
			boardReplyDao.setReply(board_reply);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	/** 댓글 목록 출력 */
	// json 데이터를 리턴
	@RequestMapping(value = "replyList", method = RequestMethod.GET)
	public @ResponseBody List<BoardReply> replyList(@RequestParam int boardnum) throws Exception {
		List<BoardReply> replyList = boardReplyDao.getReplyList(boardnum);
		return replyList;
	}
	
	/** 댓글  비밀번호 확인*/
	@RequestMapping(value = "replyOption", method = RequestMethod.POST)
	public @ResponseBody String replyOption(@RequestBody Map<String, String> map) {
		BoardReply boardReply = new BoardReply();
		boardReply.setReply_num(Integer.parseInt(map.get("reply_num")));
		boardReply.setReply_password(map.get("reply_password"));
		String replyOption = map.get("reply_option");
		
		int reply_num = boardReply.getReply_num();
		String url = "success";
		
	    // 댓글 비밀번호 확인
		try {
			int result = boardReplyDao.replyPwCheck(boardReply);
			if(result == 0) {
				url ="0";
			}else {
				if(replyOption.equals("delete")) { // 삭제
					System.out.println("replyOption : 댓글 삭제");
					boardReplyDao.deleteReply(reply_num);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/** 댓글 수정 페이지 */
	@RequestMapping(value = "replyUpdatePage", method = RequestMethod.GET)
	public String replyUpdatePage() {
		return "board/replyUpdate";
	}
	
	/** 댓글 수정 */
	@RequestMapping(value = "replyUpdate", method = RequestMethod.POST)
	public @ResponseBody String replyUpdate(BoardReply boardReply) {
		try {
			boardReplyDao.updateReply(boardReply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	
	
}
