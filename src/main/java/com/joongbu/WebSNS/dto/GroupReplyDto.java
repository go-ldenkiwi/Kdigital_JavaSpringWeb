package com.joongbu.WebSNS.dto;

import java.util.Date;

import lombok.Data;

/*
 +--------------+--------------+------+-----+-------------------+-------------------+
| Field        | Type         | Null | Key | Default           | Extra             |
+--------------+--------------+------+-----+-------------------+-------------------+
| replyNo      | int          | NO   | PRI | NULL              | auto_increment    |
| groupBoardNo | int          | NO   | MUL | NULL              |                   |
| userNo       | int          | NO   | MUL | NULL              |                   |
| parentNo     | int          | YES  | MUL | NULL              |                   |
| replyContent | varchar(255) | YES  |     | NULL              |                   |
| replyTime    | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+--------------+--------------+------+-----+-------------------+-------------------+ 
 */

@Data
public class GroupReplyDto {
	private int replyNo;
	private int groupBoardNo;
	private int userNo;
	private int parentNo;
	private String replyContent;
	private Date replyTime;
}
