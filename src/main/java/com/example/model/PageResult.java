package com.example.model;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageResult<T> {

	private final List<T> content;
	
	private final int currentPage;
	
	private final int totalPages;
	
	private final int startPage;
	
	private final int endPage;
}
