  package com.kim.ilhwaland.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.kim.ilhwaland.helper.BadRequestException;
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
			@RequestParam(value = "nowPage", defaultValue = "1", required = false) int pnum) throws Exception {
		
		/** 페이지 네이션 변수 설정 */
		int nowPage = pnum;  // 현재 페이지
		int totalCount = 0;
		int listCount = 10;  // 한 페이지당 표시할 게시글 수
		int pageCount = 4;   // 한 그룹당 표시할 페이지 번호 수
	
		totalCount = boardDao.getotalContent(); // 전체 게시글 수
		String pagenation = webHelper.pagenation("board", nowPage, totalCount, listCount, pageCount);
	
		/** 요청한 페이지의 게시글 범위에 해당하는 게시판 디비 조회 실행 */
		board.setStart(webHelper.getStart());
		board.setEnd(webHelper.getEnd());
		List<Board> boardList = boardDao.getBoardList(board);
		
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagenation", pagenation);
		
		return "board/board";
	}
	
	/** [VIEW] 게시판 글쓰기 */
	@RequestMapping(value="boardwrite", method = RequestMethod.GET)
	public String boardwrite(Board board){

		return "board/boardWrite";
	}
	
	/** [VIEW] 게시글 수정 페이지 */
	@RequestMapping(value="boardUpdate",method = RequestMethod.POST)
	public String boardUpdate(int board_num, Model model) throws Exception {
		
		Board board = boardDao.getBoardContent(board_num);
		model.addAttribute("board", board);
		
		return "board/boardUpdate";
	}
	
	/** [VIEW] 댓글 수정 페이지 */
	@RequestMapping(value = "replyUpdatePage", method = RequestMethod.POST)
	public String replyUpdatePage(@RequestParam(value="reply_num") int reply_num, Model model) {
		BoardReply boardReply = boardReplyDao.getBoardReply(reply_num);
		model.addAttribute("boardReply", boardReply);
		return "board/replyUpdate";
	}
	
	
	/** [Method 01] 게시판 글작성 */
	@RequestMapping(value = "postContent", method = RequestMethod.POST)
	public String postContent(Board board) throws Exception {
		
		boardDao.setBoardContent(board);
		return "redirect:board";
	}
	
	
	/** [Method 02] 게시글 상세 보기 */
	@RequestMapping(value = "boardView", method = RequestMethod.GET)
	public String boardView(@RequestParam(value="board_num", required=false,defaultValue="1") int board_num,Model model) throws Exception {
		
		Board board = boardDao.getBoardContent(board_num);
		boardDao.updateReadnum(board_num);
		
		// 해당 게시글의 전체 댓글 가져오기
		List<BoardReply> boardReplyList = boardReplyDao.getReplyList(board_num); 
				
		model.addAttribute("board", board);
		model.addAttribute("boardReplyList", boardReplyList);
			
		return "board/boardView";
	}
	
	
	/** [Method 03] 썸머노트 이미지 업로드  */
	@RequestMapping(value = "uploadSummernoteImage", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> uploadSummernoteImage(MultipartHttpServletRequest multipartRequest) throws IOException {
		
		String path = "";
		try {
			// 1. 업로드된 파일의 내용과 설명을 반환하거나 존재하지 않는 경우 null을 반환한다.
	        List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
	        // 2. 이미지 프로젝트 내부 경로에 저장
	     	List<FileDetail> fileDetails  = fileUtil.uploadFile(multipartFiles, "summernote");
	     	
	     	/* 3. 경로 반환
	     	      path : 프로젝트 경로 + resources\\img\\upload\\board\\summernote\\currentTime */
	     	FileDetail fileDetail = fileDetails.get(0);
			// path = fileDetail.getFilePath()+"\\"+fileDetail.getFile_name(); 
	     	path = fileDetail.getFilePath()+"/"+fileDetail.getFile_name(); 
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("파일업로드중 발생");
			//return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(path, HttpStatus.OK);
	}

	
	/** [Method 04] 게시글 수정 */
	@RequestMapping(value = "updateContent", method = RequestMethod.POST)
	public String UpdateContent(Board board, HttpServletRequest request) throws Exception {
		
		// 게시글 수정전에 기존 게시글 본문 내용에서 이미지 태그를 찾아서, 본문 이미지 파일을 지운다.
		Board original_board = boardDao.getBoardContent(board.getBoard_num());
		fileUtil.deleteSummernoteImg(original_board.getContent());
		boardDao.updateBoardContent(board);
		
		return "redirect:board";
	}
	
	
	/** [Method 05] 게시글 수정,삭제 시 비밀번호 확인  */
	@RequestMapping(value="passwordCheck", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> passwordCheck(@RequestBody Map<String, String> map) {
		try {
			// 1. 게시글 수정인지 삭제인지에 따른 분기처리를 위해, 사용자가 요청한 기능을 확인한다.
			String checkType = map.get("checkType");
		
			// 게시글 pk
			int board_num = Integer.parseInt(map.get("board_num"));
			
			// 2. board DTO 필드에 값 할당
			Board board = new Board();
			board.setBoard_num(board_num);
			board.setPassword(map.get("password"));
		
			// 3. 사용자가 입력한 비밀번호와 게시글 비밀번호가 일치하는지 확인
			board = boardDao.checkBoardPassword(board);
			if(board != null) {
				// 사용자가 게시글 삭제 요청 시
				if(checkType.equals("delete")) { 
					// 3-1. BoardReply DTO 필드에 값 할당
					BoardReply boardReply = new BoardReply();
					boardReply.setBoard_num(board_num);
					
					// 3-2. 게시글 본문 내용에서 이미지 태그를 찾아서, 본문 이미지 파일을 지운다.
					fileUtil.deleteSummernoteImg(board.getContent()); 
					// 3-3. 해당 게시글의 댓글이 있는지 확인후 삭제
					if(boardReplyDao.getReplyList(board_num).size() > 0) {
						boardReplyDao.deleteReply(boardReply);  
					};
					// 3-4. 게시글 삭제
					boardDao.deleteBoardContent(board); 
				}
			}
			// 사용자가 게시글 수정 요청시  : 수정페이지로 이동한다.
			
		} catch (BadRequestException e1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 맞지 않습니다.");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);  // 데이터와 상태코드 리턴( HTTP 정보를 반환)
	}
	
	
	/** [Method 06] 댓글 등록 */
	@RequestMapping(value = "replyWrite", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> replyWrite(BoardReply board_reply) {
		try {
			boardReplyDao.setReply(board_reply);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	/** [Method 07] 댓글 목록 출력 */
	// json 데이터를 리턴
	@RequestMapping(value = "replyList", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?>  replyList(@RequestParam int boardnum) {
		List<BoardReply> replyList;
		try {
			replyList = boardReplyDao.getReplyList(boardnum);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<BoardReply>>(replyList, HttpStatus.OK);
	}
	
	
	/** [Method 08] 댓글  비밀번호 확인*/
	@RequestMapping(value = "replyOption", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> replyOption(@RequestBody Map<String, String> map) {
		
		int reply_num = Integer.parseInt(map.get("reply_num"));
		String replyOption = map.get("reply_option");
		
		BoardReply boardReply = new BoardReply();
		boardReply.setReply_num(reply_num);
		boardReply.setReply_password(map.get("reply_password"));
		
		// 댓글 비밀번호 검사
		try {
		 	boardReplyDao.replyPwCheck(boardReply);
		 	if(replyOption.equals("delete")) { // 삭제
				boardReplyDao.deleteReply(boardReply);
			}	
		} catch (BadRequestException e1) {
			System.out.println(e1.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	/** [Method 10] 댓글 수정 */
	@RequestMapping(value = "replyUpdate", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> replyUpdate(BoardReply boardReply) {
		try {
			boardReplyDao.updateReply(boardReply);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	
}
