package com.joongbu.WebSNS.dto;

import java.util.Date;

import lombok.Data;

/*
+--------------+--------------+------+-----+-------------------+-------------------+
| Field        | Type         | Null | Key | Default           | Extra             |
+--------------+--------------+------+-----+-------------------+-------------------+
| boardNo      | int          | NO   | PRI | NULL              | auto_increment    |
| contents     | varchar(255) | YES  |     |                   |                   |
| post_time    | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| userNo       | int          | NO   | MUL | NULL              |                   |
| category     | varchar(255) | NO   |     | NULL              |                   |
| mapConfirm   | int          | YES  |     | NULL              |                   |
| board_report | int          | NO   |     | 0                 |                   |
+--------------+--------------+------+-----+-------------------+-------------------+
*/
@Data
public class BoardDto {
	private int boardNo; //pk
	private String contents;
	private Date postTime;
	private int userNo;
	private String category;
	private int mapConfirm;
	private int boardReport;
}
