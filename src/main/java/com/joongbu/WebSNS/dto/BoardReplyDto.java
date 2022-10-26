package com.joongbu.WebSNS.dto;

import java.util.Date;

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
	private Date postTime;
	private int boardNo;
	private int userNo;
}
