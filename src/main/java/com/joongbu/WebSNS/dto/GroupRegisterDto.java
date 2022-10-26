package com.joongbu.WebSNS.dto;

import lombok.Data;

/*
+------------+------------+------+-----+---------+----------------+
| Field      | Type       | Null | Key | Default | Extra          |
+------------+------------+------+-----+---------+----------------+
| registerNo | int        | NO   | PRI | NULL    | auto_increment |
| groupNo    | int        | NO   | MUL | NULL    |                |
| userNo     | int        | NO   | MUL | NULL    |                |
| isAccepted | tinyint(1) | YES  |     | NULL    |                |
+------------+------------+------+-----+---------+----------------+
*/
@Data
public class GroupRegisterDto {
	private int registerNo;
	private int groupNo;
	private int userNo;
	private boolean isAccepted;
}
