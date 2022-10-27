package com.joongbu.WebSNS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joongbu.WebSNS.dto.BoardDto;
import com.joongbu.WebSNS.dto.BoardImgDto;
import com.joongbu.WebSNS.mapper.BoardImgMapper;
import com.joongbu.WebSNS.mapper.BoardMapper;

@Service
public class BoardService {

	@Autowired
	BoardMapper boardMapper;
	@Autowired
	BoardImgMapper boardImgMapper;
	
	public int registBoardAndBoardImgs(BoardDto board, List<String> imgPaths) {
		int insert=0;
		insert=boardMapper.insert(board);
		System.out.println(board.getBoardNo());
		int imgInsert=0;
		for(String imgPath: imgPaths) {
			BoardImgDto boardImg=new BoardImgDto();
			boardImg.setBoardNo(board.getBoardNo());
			boardImg.setImgPath(imgPath);
			imgInsert+=boardImgMapper.insert(boardImg);
		}
		return insert;
	}
	
	public int updateBoardAndBoardImgs(BoardDto board, List<String> imgPaths) {
		int update=0;
		update=boardMapper.update(board);
		int imgInsert=0;
		for(String imgPath: imgPaths) {
			BoardImgDto boardImg=new BoardImgDto();
			boardImg.setBoardNo(board.getBoardNo());
			boardImg.setImgPath(imgPath);
			imgInsert+=boardImgMapper.insert(boardImg);
		}
		return update;
	}
	
	public int boardImgDelete(int boardImgNo) {
		int delete=0;
		delete=boardImgMapper.delete(boardImgNo);
		return delete;
	}
	
	public BoardImgDto boardImgDetail(int boardImgNo) {
		BoardImgDto boardImg=null;
		boardImg=boardImgMapper.detail(boardImgNo);
		return boardImg;
	}
}
