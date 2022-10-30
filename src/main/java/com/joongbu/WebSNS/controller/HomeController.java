package com.joongbu.WebSNS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joongbu.WebSNS.dto.BoardDto;
import com.joongbu.WebSNS.dto.BoardImgDto;
import com.joongbu.WebSNS.mapper.BoardImgMapper;
import com.joongbu.WebSNS.mapper.BoardMapper;

@Controller
public class HomeController {
	@Autowired
	BoardMapper boardMapper;	
	@Autowired
	BoardImgMapper imgMapper;

	@GetMapping ("/")
	public String index(Model model) {
		List<BoardDto> boardList=boardMapper.list();
		for(BoardDto board: boardList) {
			List<BoardImgDto> imgList=imgMapper.list(board.getBoardNo());
			board.setBoardImgList(imgList);
			
			// 유저 정보 가져오는 것도 추가 해야함
		}
		model.addAttribute("boardList", boardList);
		return "index";

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
}
