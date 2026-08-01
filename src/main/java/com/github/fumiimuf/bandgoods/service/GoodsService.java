package com.github.fumiimuf.bandgoods.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.fumiimuf.bandgoods.entity.Goods;
import com.github.fumiimuf.bandgoods.model.GoodsItem;

public interface GoodsService {

	List<GoodsItem> getGoodsByPage(boolean isDeleted, String keyword, int page, int size);
	
	long getGoodsCount(boolean isDeleted, String keyword);
	
	GoodsItem getOneGoodsItemById(int id);
	
	void registerGoods(Goods goods, MultipartFile imageFile);
	
	void updateGoods(Goods goods, MultipartFile imageFile);
	
	boolean isAvailableGoods(Integer goodsId);
	
}
