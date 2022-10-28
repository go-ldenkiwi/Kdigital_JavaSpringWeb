package com.joongbu.WebSNS.dto;

import lombok.Data;
/*
+------------+------------+------+-----+---------+----------------+
| Field      | Type       | Null | Key | Default | Extra          |
+------------+------------+------+-----+---------+----------------+
| inviteNo   | int        | NO   | PRI | NULL    | auto_increment |
| groupNo    | int        | NO   | MUL | NULL    |                |
| userNo     | int        | NO   | MUL | NULL    |                |
| isAccepted | tinyint(1) | YES  |     | NULL    |                |
+------------+------------+------+-----+---------+----------------+
*/
@Data
public class GroupInviteDto {
	private int inviteNo;
	private int groupNo;
	private int userNo;
	private boolean isAccepted;
}
