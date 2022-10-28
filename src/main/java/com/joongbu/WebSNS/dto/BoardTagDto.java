package com.joongbu.WebSNS.dto;

import lombok.Data;

/*
 +------------+--------------+------+-----+---------+----------------+
| Field      | Type         | Null | Key | Default | Extra          |
+------------+--------------+------+-----+---------+----------------+
| tagNo      | int          | NO   | PRI | NULL    | auto_increment |
| tagContent | varchar(255) | NO   |     | NULL    |                |
+------------+--------------+------+-----+---------+----------------+
 */
@Data
public class BoardTagDto {

	private int tagNo;
	private String tagContent;
}
