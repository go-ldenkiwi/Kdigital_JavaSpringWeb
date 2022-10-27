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
import org.springframework.web.multipart.MultipartFile;

import com.joongbu.WebSNS.dto.BoardDto;
import com.joongbu.WebSNS.dto.BoardImgDto;
import com.joongbu.WebSNS.mapper.BoardImgMapper;
import com.joongbu.WebSNS.mapper.BoardMapper;
import com.joongbu.WebSNS.service.BoardService;

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
	
	@GetMapping("/list.do")
	public void boardList(Model model) {
		List<BoardDto> boardList=boardMapper.list();
		for(BoardDto board: boardList) {
			List<BoardImgDto> imgList=imgMapper.list(board.getBoardNo());
			board.setBoardImgList(imgList);
			
			// 유저 정보 가져오는 것도 추가 해야함
		}
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
		int imgUpdate=0;
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
}