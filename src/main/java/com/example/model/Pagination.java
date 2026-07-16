package com.example.model;

import java.util.List;

public class Pagination<T> {

	public List<T> content;
	
	public int currentPage;
	
	public int totalPages;
	
	public final int startPage;
	
	public final int endPage;
	
	public Pagination(int page, long totalCount, int size) {
		
		int pages = (int) Math.ceil((double) totalCount / size);
		this.totalPages = (pages == 0) ? 1 : pages;
		
		int validatiedPage = page;
		
		if (validatiedPage < 0) {
			validatiedPage = 0;
			
		} else if (validatiedPage >= this.totalPages) {
			validatiedPage = this.totalPages -1;
		}
		this.currentPage = validatiedPage;
		
        int displayButtonCount = 3;
        int start = Math.max(0, this.currentPage - (displayButtonCount / 2));
        int end = Math.min(this.totalPages - 1, start + displayButtonCount - 1);

        if (end - start + 1 < displayButtonCount) {
            start = Math.max(0, end - displayButtonCount + 1);
        }

        this.startPage = start;
        this.endPage = end;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
