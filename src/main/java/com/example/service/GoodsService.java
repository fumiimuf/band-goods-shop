package com.example.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Goods;
import com.example.model.GoodsItem;
import com.example.model.Pagination;

public interface GoodsService {

	List<GoodsItem> findByPage(boolean isDeleted, String keyword, int page, int size);
	
	long count(boolean isDeleted, String keyword);
	
	GoodsItem findById(int id);
	
	void registerGoods(Goods goods, MultipartFile imageFile);
	
	void updateGoods(Goods goods, MultipartFile imageFile);
	
	Pagination<GoodsItem> getGoodsPage(String keyword, int page);
	
	Pagination<GoodsItem> getAdminGoodsPage(String status, String keyword, int page);
	
}
