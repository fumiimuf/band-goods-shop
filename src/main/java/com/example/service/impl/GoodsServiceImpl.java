package com.example.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Goods;
import com.example.model.GoodsItem;
import com.example.model.Pagination;
import com.example.repository.GoodsMapper;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsServiceImpl implements GoodsService {

	private final GoodsMapper goodsMapper;

	@Value("${upload-directory}")
	private String uploadDirectory;

	@Override
	public List<GoodsItem> findByPage(boolean isDeleted, String keyword, int page, int size) {
		int offset = page * size;
		return goodsMapper.selectByPage(isDeleted, keyword, size, offset);
	}

	@Override
	public long count(boolean isDeleted, String keyword) {
		return goodsMapper.selectCount(isDeleted, keyword);
	}

	@Override
	public GoodsItem findById(int id) {
		return goodsMapper.selectById(id);
	}

	@Override
	public void registerGoods(Goods goods, MultipartFile imageFile) {

		String savedFileName = "";

		if (imageFile != null && !imageFile.isEmpty()) {
			savedFileName = saveImage(imageFile);
		}

		goods.setImage(savedFileName);
		goods.setIsDeleted(false);

		goodsMapper.insert(goods);
	}

	@Override
	public void updateGoods(Goods goods, MultipartFile imageFile) {

		GoodsItem existingGoods = goodsMapper.selectById(goods.getId());

		if (goods.getIsDeleted()) {
			goods.setDeleteDateTime(LocalDateTime.now());
		} else {
			goods.setDeleteDateTime(null);
		}

		if (imageFile != null && !imageFile.isEmpty()) {
			goods.setImage(saveImage(imageFile));
		} else {
			if (existingGoods != null) {
				goods.setImage(existingGoods.getGoods().getImage());
			}
		}
		goodsMapper.update(goods);
	}

	@Override
	public Pagination<GoodsItem> getGoodsPage(String keyword, int page) {

		int size = 8;

		List<GoodsItem> goodsList = findByPage(false, keyword, page, size);

		long totalCount = count(false, keyword);

		Pagination<GoodsItem> pagination = new Pagination<GoodsItem>(goodsList, page, totalCount, size);

		return pagination;
	}
	
	@Override
	public Pagination<GoodsItem> getAdminGoodsPage(String status, String keyword, int page) {
		
		int size = 5;

		boolean isDeleted = status.equals("suspended");

		List<GoodsItem> goodsList = findByPage(isDeleted, keyword, page, size);
		
		long totalCount = count(isDeleted, keyword);

		Pagination<GoodsItem> result = new Pagination<GoodsItem>(goodsList, page, totalCount, size);
		
		return result;
	}

	// 画像をパソコンのフォルダに物理保存するための共通プライベートメソッド
	private String saveImage(MultipartFile imageFile) {
		try {
			String originalFileName = imageFile.getOriginalFilename();

			String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

			String filePath = uploadDirectory + savedFileName;

			byte[] bytes = imageFile.getBytes();
			Files.write(Paths.get(filePath), bytes);

			return savedFileName;

		} catch (IOException e) {
			throw new RuntimeException("画像の保存に失敗しました", e);
		}
	}
}
