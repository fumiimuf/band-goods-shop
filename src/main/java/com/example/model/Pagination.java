package com.example.model;

import java.util.List;

import lombok.Data;

@Data
public class Pagination<T> {

	private final List<T> content;
	
	private final int currentPage;
	
	private final int totalPages;
	
	private final int startPage;
	
	private final int endPage;
	
	public Pagination(List<T> list, int page, long totalCount, int size) {
		this.content = list;
		this.currentPage = page;
		
		int pages = (int) Math.ceil((double) totalCount / size);
		this.totalPages = (pages == 0) ? 1 : pages;
		
        int displayButtonCount = 3;
        int start = Math.max(0, page - (displayButtonCount / 2));
        int end = Math.min(this.totalPages - 1, start + displayButtonCount - 1);

        if (end - start + 1 < displayButtonCount) {
            start = Math.max(0, end - displayButtonCount + 1);
        }

        this.startPage = start;
        this.endPage = end;
	}
}
