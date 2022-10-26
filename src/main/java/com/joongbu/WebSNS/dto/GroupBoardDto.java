package com.joongbu.WebSNS.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
/*
 +-------------------+--------------+------+-----+-------------------+-------------------+
| Field             | Type         | Null | Key | Default           | Extra             |
+-------------------+--------------+------+-----+-------------------+-------------------+
| groupBoardNo      | int          | NO   | PRI | NULL              | auto_increment    |
| groupNo           | int          | NO   | MUL | NULL              |                   |
| userNo            | int          | NO   | MUL | NULL              |                   |
| groupBoardTitle   | varchar(255) | NO   |     | NULL              |                   |
| groupBoardContent | varchar(255) | YES  |     | NULL              |                   |
| groupBoardTime    | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
| groupBoardViews   | int          | NO   |     | 0                 |                   |
+-------------------+--------------+------+-----+-------------------+-------------------+ 
 */
@Data
public class GroupBoardDto {
	private int groupBoardNo;
	private int groupNo;
	private int userNo;
	private String groupBoardTitle;
	private String groupBoardContent;
	private Date groupBoardTime;
	private int groupBoardViews;
	private List<GroupReplyDto> replyList;
}
