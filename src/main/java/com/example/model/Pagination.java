package com.example.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Pagination<T> {

	@Setter
	private List<T> content;
	
	private int currentPage;
	
	private int totalPages;
	
	private int startPage;
	
	private int endPage;
	
	public static final int DISPLAY_BUTTON_COUNT = 3;
	
	public Pagination(int page, long totalCount, int size) {
		
		int pages = (int) Math.ceil((double) totalCount / size);
		this.totalPages = (pages == 0) ? 1 : pages;
		
		int validatiedPage = page;
		
		if (page < 0) {
			validatiedPage = 0;
			
		} else if (page >= this.totalPages) {
			validatiedPage = this.totalPages -1;
		}
		this.currentPage = validatiedPage;
		
        int start = Math.max(0, this.currentPage - (DISPLAY_BUTTON_COUNT / 2));
        int end = Math.min(this.totalPages - 1, start + DISPLAY_BUTTON_COUNT - 1);

        if (end - start + 1 < DISPLAY_BUTTON_COUNT) {
            start = Math.max(0, end - DISPLAY_BUTTON_COUNT + 1);
        }

        this.startPage = start;
        this.endPage = end;
	}

//	public void setContent(List<T> content) {
//		this.content = content;
//	}

	
	
}
