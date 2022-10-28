package com.joongbu.WebSNS.dto;

import java.util.Date;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/*
 +----------+--------------+------+-----+-------------------+-------------------+
| Field    | Type         | Null | Key | Default           | Extra             |
+----------+--------------+------+-----+-------------------+-------------------+
| replyNo  | int          | NO   | PRI | NULL              | auto_increment    |
| contents | varchar(255) | YES  |     |                   |                   |
| postTime | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| boardNo  | int          | NO   | MUL | NULL              |                   |
| userNo   | int          | NO   | MUL | NULL              |                   |
+----------+--------------+------+-----+-------------------+-------------------+
 */
@Data
public class BoardReplyDto {

	private int replyNo;
	private String contents;
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date postTime;
	private int boardNo;
	private int userNo;
}
