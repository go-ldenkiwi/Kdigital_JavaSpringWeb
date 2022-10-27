package com.joongbu.WebSNS.dto;

import lombok.Data;

/*
+---------------+------------+------+-----+---------+----------------+
| Field         | Type       | Null | Key | Default | Extra          |
+---------------+------------+------+-----+---------+----------------+
| boardPreferNo | int        | NO   | PRI | NULL    | auto_increment |
| boardNo       | int        | NO   | MUL | NULL    |                |
| prefer        | tinyint(1) | YES  |     | NULL    |                |
| userNo        | int        | NO   | MUL | NULL    |                |
+---------------+------------+------+-----+---------+----------------+
*/
@Data
public class BoardPreferDto {

	private int boardPreferNo;
	private int boardNo;
	private boolean prefer;
	private int userNo;
}
