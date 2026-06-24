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
import com.example.repository.GoodsMapper;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsServiceImpl implements GoodsService {

	private final GoodsMapper goodsMapper;
	
	// application.properties から画像の保存先フォルダのパスを取得
	// application.properties から画像の保存先（C:/Users/...）を引っ張ってきます
	// @RequiredArgsConstructorがあっても、この@Valueフィールドは自動生成の対象外になるので安全です
	@Value("${upload-directory}")
	private String uploadDirectory;

	@Override
	public List<GoodsItem> findByPage(boolean isDeleted, String keyword, int page, int size) {
		int offset = page * size;
		return goodsMapper.findByPage(isDeleted, keyword, size, offset);
	}

	@Override
	public long count(boolean isDeleted, String keyword) {
		return goodsMapper.count(isDeleted, keyword);
	}

	@Override
	public GoodsItem findById(int id) {
		// Mapperを呼び出して1件取得
		return goodsMapper.findById(id);
	}

	@Override
	public void registerGoods(Goods goods, MultipartFile imageFile) {
		
		String savedFileName = "";
		
		if (imageFile != null && !imageFile.isEmpty()) {
			savedFileName = saveImage(imageFile);
		}
		
		// Entityに必要な初期値をセットしてMapperに渡す
		goods.setImage(savedFileName);
		goods.setIsDeleted(false);
		
		goodsMapper.insert(goods);
	}

	@Override
	public void updateGoods(Goods goods, MultipartFile imageFile) {
		
		// 変更前の「古いグッズ情報」をDBから1件取得
		// 画面から画像が送られてこなかった場合、元の画像ファイル名をDBから引き継ぐために使用します
		GoodsItem existingGoods = goodsMapper.findById(goods.getId());
		
		// 削除フラグが停止中(true)に変更された場合、現在日時をセットする
		if (goods.getIsDeleted()) {
			goods.setDeleteDateTime(LocalDateTime.now());
		} else {
			goods.setDeleteDateTime(null);
		}
		
		// 画像の物理保存とファイル名の引継ぎ判定
		if (imageFile != null && !imageFile.isEmpty()) {
			//新しい画像があれば保存してセット
			goods.setImage(saveImage(imageFile));
		} else {
			// 画像が変更されなかった場合は、古いファイル名を引き継ぐ
			if (existingGoods != null) {
				goods.setImage(existingGoods.getGoods().getImage());
			}
		}
		goodsMapper.update(goods);
	}
	
	// 画像をパソコンのフォルダに物理保存するための共通プライベートメソッド
	private String saveImage(MultipartFile imageFile) {
			try {
				// 元のファイル名を取得
				String originalFileName = imageFile.getOriginalFilename();
				
				// 同名ファイルの上書きを防ぐため、UUID(ランダムな一意の文字列)を先頭に結合
				String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
				
				// 保存先ディレクトリとファイルを結合
				String filePath = uploadDirectory + savedFileName;
				
				// バイナリデータとして物理書き出し
				byte[] bytes = imageFile.getBytes();
				Files.write(Paths.get(filePath), bytes);
				
				// 生成した唯一無二のファイル名を呼び出して返す
				return savedFileName;
				
			} catch (IOException e) {
				throw new RuntimeException("画像の保存に失敗しました", e);
			}
            
	}
	
	
	

}
