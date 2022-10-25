package com.joongbu.WebSNS.dto;

import lombok.Data;

/*+-----------+--------------+------+-----+---------+----------------+
| Field     | Type         | Null | Key | Default | Extra          |
+-----------+--------------+------+-----+---------+----------------+
| groupNo   | int          | NO   | PRI | NULL    | auto_increment |
| groupName | varchar(255) | NO   | UNI | NULL    |                |
| groupDesc | varchar(255) | YES  |     | NULL    |                |
| userId    | varchar(255) | NO   | MUL | NULL    |                |
+-----------+--------------+------+-----+---------+----------------+*/
@Data
public class GroupDto {
	private int groupNo;
	private String groupName;
	private String groupDesc;
	private int userId;
}
