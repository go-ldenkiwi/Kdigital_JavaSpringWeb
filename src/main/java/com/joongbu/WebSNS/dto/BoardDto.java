package com.joongbu.WebSNS.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/*
+-------------+--------------+------+-----+-------------------+-------------------+
| Field       | Type         | Null | Key | Default           | Extra             |
+-------------+--------------+------+-----+-------------------+-------------------+
| boardNo     | int          | NO   | PRI | NULL              | auto_increment    |
| contents    | varchar(255) | YES  |     |                   |                   |
| postTime    | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| userNo      | int          | NO   | MUL | NULL              |                   |
| category    | varchar(255) | NO   |     | NULL              |                   |
| mapConfirm  | int          | YES  |     | NULL              |                   |
| boardReport | int          | NO   |     | 0                 |                   |
+-------------+--------------+------+-----+-------------------+-------------------+
*/
@Data
public class BoardDto {
	// 게시판
	private int boardNo; //pk
	private String contents;
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date postTime;
	private int userNo;
	private String category;
	private int mapConfirm;
	private int boardReport;
	private int likes;
	// 이미지 정보
	private List<BoardImgDto> boardImgList;
	// 댓글
	private List<BoardReplyDto> boardReplyList;
	// 유저 정보
	private UserDto user;
}
