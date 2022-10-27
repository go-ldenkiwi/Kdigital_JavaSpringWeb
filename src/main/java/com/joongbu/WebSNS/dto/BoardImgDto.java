package com.joongbu.WebSNS.dto;

import lombok.Data;

/*
 +------------+--------------+------+-----+---------+----------------+
| Field      | Type         | Null | Key | Default | Extra          |
+------------+--------------+------+-----+---------+----------------+
| boardImgNo | int          | NO   | PRI | NULL    | auto_increment |
| boardNo    | int          | NO   | MUL | NULL    |                |
| imgPath    | varchar(255) | NO   |     | NULL    |                |
+------------+--------------+------+-----+---------+----------------+
 */
@Data
public class BoardImgDto {

	private int boardImgNo;
	private int boardNo;
	private String imgPath;
}
