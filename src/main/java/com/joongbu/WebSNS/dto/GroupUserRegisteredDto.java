package com.joongbu.WebSNS.dto;

import lombok.Data;

/*
+---------+------+------+-----+---------+----------------+
| Field   | Type | Null | Key | Default | Extra          |
+---------+------+------+-----+---------+----------------+
| urgNo   | int  | NO   | PRI | NULL    | auto_increment |
| groupNo | int  | NO   | MUL | NULL    |                |
| userNo  | int  | NO   | MUL | NULL    |                |
+---------+------+------+-----+---------+----------------+
*/
@Data
public class GroupUserRegisteredDto {
	private int urgNo;
	private int groupNo;
	private int userNo;
}
