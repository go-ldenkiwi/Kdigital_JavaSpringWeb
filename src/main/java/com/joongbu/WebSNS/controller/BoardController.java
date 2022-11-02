package com.joongbu.WebSNS.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.joongbu.WebSNS.dto.BoardDto;
import com.joongbu.WebSNS.dto.BoardImgDto;
import com.joongbu.WebSNS.dto.BoardPreferDto;
import com.joongbu.WebSNS.dto.UserDto;
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
			@SessionAttribute(required = false) UserDto loginUser,
			Model model
			) {
		BoardDto board=null;
		BoardPreferDto boardPrefer=null;
		int prefer=0;
		try {
			board=boardMapper.detail(boardNo);
			if(loginUser!=null) {
				boardPrefer=preferMapper.detail(loginUser.getUserNo(), boardNo);
				if(boardPrefer!=null) {
					prefer=1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null) {	
			model.addAttribute("board", board);
			model.addAttribute("prefer", prefer);
			return "/board/detail";
		}else {
			return "redirect:/board/list.do";
		}
	}
	
	@GetMapping("/insert.do")
	public String insert(
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session
			) {
		if(loginUser!=null) {
			return "/board/insertForm";
		}
		session.setAttribute("msg", "로그인 해주세요");
		return "redirect:/user/login.do";
	}
	
	@PostMapping("/insert.do")
	public String insert(
			BoardDto board,
			@RequestParam(name = "img") MultipartFile [] imgList
			) {
		int insert=0;
		ArrayList<String> imgPaths=new ArrayList<String>();
		
		Pattern tags = Pattern.compile("#(\\S+)");
        Matcher mat = tags.matcher(board.getContents());
        List<String> tagList = new ArrayList<>();
        
        while(mat.find()) {
            tagList.add((mat.group(1)));
        }
        
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
			boardService.saveTag(tagList, board.getBoardNo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert>0) {
			return "redirect:/";
		}else {			
			return "redirect:/board/insert.do";
		}
	}
	
	@GetMapping("/update.do")
	public String update(
			@RequestParam(required = true)int boardNo,
			Model model,
			@SessionAttribute(required=false) UserDto loginUser,
			HttpSession session
			) {
		BoardDto board=null;
		String msg="";
		String url="";
		try {
			if(loginUser!=null) {				
				board=boardMapper.detail(boardNo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(board!=null&&(board.getUserNo()==loginUser.getUserNo())) {		
			model.addAttribute("board", board);
			return "/board/updateForm";
		} else {
			if(loginUser==null) {
				msg="로그인 해주세요";
				url="/user/login.do";
			}else {
				msg="글쓴이만 수정 가능 합니다";
				url="/board/list.do";
			}
			session.setAttribute("msg", msg);
			return "redirect:"+url;
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

		Pattern tags = Pattern.compile("#(\\S+)");
        Matcher mat = tags.matcher(board.getContents());
        List<String> tagList = new ArrayList<>();
        
        while(mat.find()) {
            tagList.add((mat.group(1)));
        }
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
			boardService.saveTag(tagList, board.getBoardNo());
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
			@RequestParam(required = true)int boardNo,
			@SessionAttribute(required = false) UserDto loginUser,
			HttpSession session
			) {
		int delete=0;
		String msg="";
		String url="";
		List<BoardImgDto> imgList=null;
		try {
			if(loginUser!=null) {
				BoardDto board=boardMapper.detail(boardNo);
				if(board.getUserNo()==loginUser.getUserNo()) {					
					imgList=boardMapper.detail(boardNo).getBoardImgList();
					if(imgList.size()>0) {
						for(BoardImgDto img: imgList) {
							File file=new File(imgPath+"/board/"+img.getImgPath());
							file.delete();
						}
					}
					delete=boardMapper.delete(boardNo);
				} else {
					msg="글쓴이만 삭제 가능합니다";
					url="/board/list.do";
				}
			} else {
				msg="로그인 해주세요";
				url="/user/login.do";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete>0) {
			return "redirect:/";
		} else {
			session.setAttribute("msg", msg);
			return "redirect:"+url;
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
			@SessionAttribute(required = false)UserDto loginUser,
			@RequestParam(required = true)boolean prefer			
			) {
		CheckStatus checkStatus=new CheckStatus();
		try {
			BoardPreferDto boardPrefer=preferMapper.detail(loginUser.getUserNo(), boardNo);
			if(boardPrefer==null) {
				boardPrefer=new BoardPreferDto();
				boardPrefer.setBoardNo(boardNo);
				boardPrefer.setUserNo(loginUser.getUserNo());
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
			@SessionAttribute(required = false) UserDto loginUser,
			Model model
			) {
		BoardDto board=null;
		BoardPreferDto boardPrefer = null;
		int prefer=0;
		try {			
			board=boardMapper.detail(boardNo);
			boardPrefer=preferMapper.detail(loginUser.getUserNo(), boardNo);
			if(boardPrefer!=null) {
				prefer=1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("board", board);
		model.addAttribute("boardPrefer", boardPrefer);
		model.addAttribute("prefer", prefer);
		return "/board/prefer";
	}
	
	@GetMapping("/selectCatergory.do")
	public String selectCategory(
			@RequestParam String category,
			Model model
			) {
		List<BoardDto> findCategoryList=null;
		if(category.equals("all")) {
			findCategoryList=boardMapper.list();
			
		}else {
			findCategoryList=boardMapper.findByCategory(category);
		}
		model.addAttribute("boardList",findCategoryList);
		return "/board/findCategoryList";
	}
	
	@GetMapping("/findByHasgTag.do")
	public String findByHasgTag(
			@RequestParam String tagContent,
			Model model
			) {
		List<BoardDto> findByHasgTag=null;
		try {
			findByHasgTag=boardMapper.findByHasgTag(tagContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("boardList", findByHasgTag);
		return "/board/findByHasgTag";
	}
}
