package com.joongbu.WebSNS.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.joongbu.WebSNS.dto.BoardDto;
import com.joongbu.WebSNS.dto.BoardImgDto;
import com.joongbu.WebSNS.dto.BoardPreferDto;
import com.joongbu.WebSNS.mapper.BoardImgMapper;
import com.joongbu.WebSNS.mapper.BoardMapper;
import com.joongbu.WebSNS.mapper.BoardPreferMapper;
import com.joongbu.WebSNS.service.BoardService;

import lombok.Data;

@RequestMapping("/board")
@Controller
public class BoardController {

	@Value("${spring.servlet.multipart.location}") // vlaue : 설정 가져오는 어노테이션
	String imgPath;
	
	@Autowired
	BoardMapper boardMapper;
	
	@Autowired
	BoardImgMapper imgMapper;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardPreferMapper preferMapper;
	
	@GetMapping("/list.do")
	public void boardList(Model model) {
		List<BoardDto> boardList=boardMapper.list();
		model.addAttribute("boardList", boardList);
	}
	
	@GetMapping("/detail.do")
	public String boardDetail(
			@RequestParam(required = true)int boardNo,
			Model model
			) {
		BoardDto board=null;
		try {
			board=boardMapper.detail(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null) {	
			model.addAttribute("board", board);
			return "/board/detail";
		}else {
			return "redirect:/board/list.do";
		}
	}
	
	@GetMapping("/insert.do")
	public String insert() {
		return "/board/insertForm";
	}
	
	@PostMapping("/insert.do")
	public String insert(
			BoardDto board,
			@RequestParam(name = "img") MultipartFile [] imgList
			) {
		int insert=0;
		ArrayList<String> imgPaths=new ArrayList<String>();
		String [] tags=board.getContents().split("/#[^\s#]+/g");
		
		try {
			for(MultipartFile img : imgList) {
				if(!img.isEmpty()) {
					String [] contentTypes=img.getContentType().split("/");
					if(contentTypes[0].equals("image")) {
						String fileName="board_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
						Path path=Paths.get(imgPath+"/board/"+fileName);
						img.transferTo(path);
						imgPaths.add(fileName);
					}
				}
			}
			insert=boardService.registBoardAndBoardImgs(board, imgPaths);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert>0) {
			return "redirect:/board/list.do";
		}else {			
			return "redirect:/board/insert.do";
		}
	}
	
	@GetMapping("/update.do")
	public String update(
			@RequestParam(required = true)int boardNo,
			Model model
			) {
		BoardDto board=null;
		try {
			board=boardMapper.detail(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null) {		
			model.addAttribute("board", board);
			return "/board/updateForm";
		} else {
			return "redirect:/board/detail.do?boardNo="+boardNo;
		}
	}
	
	@PostMapping("/update.do")
	public String update(
			BoardDto board,
			@RequestParam(name = "img") MultipartFile [] imgList,
			@RequestParam(name = "boardImgNo", required = false) String [] imgNoList
			) {
		int update=0;
		ArrayList<String> imgPaths=new ArrayList<String>();
		try {
			for(MultipartFile img:imgList) {
				if(!img.isEmpty()) {
					String[] contentTypes=img.getContentType().split("/");
					if(contentTypes[0].equals("image")) {
						String fileName="board_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
						Path path=Paths.get(imgPath+"/board/"+fileName);
						img.transferTo(path);
						imgPaths.add(fileName);
					}
				}
			}
			update=boardService.updateBoardAndBoardImgs(board, imgPaths);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int imgDelete=0;
		if(update>0) {
			if(imgNoList!=null && imgNoList.length!=0) {
				for(String imgNo_str:imgNoList) {
					int imgNo=Integer.parseInt(imgNo_str);
					BoardImgDto boardImg=boardService.boardImgDetail(imgNo);
					File file=new File(imgPath+"/board/"+boardImg.getImgPath());
					System.out.println("파일 삭제 성공: "+file.delete());
					imgDelete=boardService.boardImgDelete(imgNo);
					if(imgDelete>0) {
						System.out.println("이미지 db에서 삭제 성공");
					}
				}
			}
			return "redirect:/board/detail.do?boardNo="+board.getBoardNo();
		}else {
			return "redirect:/board/update.do?boardNo="+board.getBoardNo();
		}
	}
	
	@GetMapping("/delete.do")
	public String delete(
			@RequestParam(required = true)int boardNo
			) {
		int delete=0;
		List<BoardImgDto> imgList=null;
		try {
			imgList=boardMapper.detail(boardNo).getBoardImgList();
			if(imgList.size()>0) {
				for(BoardImgDto img: imgList) {
					File file=new File(imgPath+"/board/"+img.getImgPath());
					file.delete();
				}
			}
			delete=boardMapper.delete(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete>0) {
			return "redirect:/board/list.do";
		} else {
			return "redirect:/board/update.do?boardNo="+boardNo;
		}
	}
	
	@Data
	class CheckStatus{
		private int status;
		// 0: 등록 실패, 1: 등록 성공, -1: 로그인하세요, 2: 삭제 성공
	}
	
	@GetMapping("/prefer.do")
	public @ResponseBody CheckStatus prefer(
			@RequestParam(required = true)int boardNo,
			@RequestParam(required = true)int userNo,
			@RequestParam(required = true)boolean prefer			
			) {
		CheckStatus checkStatus=new CheckStatus();
		try {
			BoardPreferDto boardPrefer=preferMapper.detail(userNo, boardNo);
			if(boardPrefer==null) {
				boardPrefer=new BoardPreferDto();
				boardPrefer.setBoardNo(boardNo);
				boardPrefer.setUserNo(userNo);
				boardPrefer.setPrefer(prefer);
				int insert=preferMapper.insert(boardPrefer);
				checkStatus.setStatus(insert);
			} else if(boardPrefer.isPrefer()==prefer) {
				int delete=preferMapper.delete(boardPrefer.getBoardPreferNo());
				if(delete>0) checkStatus.setStatus(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkStatus;
	}
	
	@GetMapping("/preferDetail.do")
	public String preferDetail(
			@RequestParam(required = true) int boardNo,
			@RequestParam(required = true) int userNo,
			Model model
			) {
		BoardDto board=null;
		BoardPreferDto boardPrefer = null;
		try {			
			board=boardMapper.detail(boardNo);
			boardPrefer=preferMapper.detail(userNo, boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("board", board);
		model.addAttribute("boardPrefer", boardPrefer);
		return "/board/prefer";
	}
}
