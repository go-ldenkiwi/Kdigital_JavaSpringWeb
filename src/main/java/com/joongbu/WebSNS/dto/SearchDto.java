package com.joongbu.WebSNS.dto;

import lombok.Data;

@Data
public class SearchDto {
	private int page=1; 
	private String orderBy;
	private int ROWS=10; //한 페이지에 출력될 rows
	private int navSize=5; //페이지 네비의 수
	private String field; //(검색할)
	private String value; //  ""
	public SearchDto() {}
	
	public SearchDto(int page,String orderBy) {
		this.page = page;
		this.orderBy = orderBy;
	}
	public SearchDto(int page,String orderBy,String field, String value) {
		this.page = page;
		this.orderBy = orderBy;
		this.field = field;
		this.value = value;
	}
	public SearchDto(int page,String orderBy,int ROWS) {
		this.page = page;
		this.orderBy = orderBy;
		this.ROWS = ROWS;
	}
	public SearchDto(int page,String orderBy,int ROWS, int navSize) {
		this.page = page;
		this.orderBy = orderBy;
		this.ROWS = ROWS;
		this.navSize = navSize;
	}
}
