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
import com.example.model.PageResult;
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

	@Override
	public PageResult<GoodsItem> getGoodsPage(String keyword, int page) {

		// 1ページあたりの表示件数は「8件」と決めます
		int size = 8;

		// Serviceに「ページ番号」と「件数」を渡してリストを取得
		List<GoodsItem> goodsList = findByPage(false, keyword, page, size);

		// 全体の件数を数えます（全何ページあるか計算するため）
		long totalCount = count(false, keyword);

		// 全体のページ数を計算します（端数は切り上げ。例：9件なら2ページ）
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		if (totalPages == 0) {
			totalPages = 1;
		}

		// 表示するページボタンの範囲を最大3に設定
		int displayButtonCount = 3;

		// 開始ページ
		int startPage = Math.max(0, page - (displayButtonCount / 2));

		// 終了ページ
		int endPage = Math.min(totalPages - 1, startPage + displayButtonCount - 1);

		// ページの終わりで方でボタンが3つ未満になってしまう場合の調整
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}

		// 大きな変数(お盆)に組み立てる
		PageResult<GoodsItem> result = new PageResult<GoodsItem>(goodsList, page, totalPages, startPage, endPage);

		return result;
	}
	
	@Override
	public PageResult<GoodsItem> getAdminGoodsPage(String status, String keyword, int page) {
		
		// 1ページの表示件数は「5件」
		int size = 5;

		// 1. 文字列の status（active / suspended）を boolean（false / true）に翻訳する
		// status が "suspended"（停止中）なら true（削除済み）、それ以外なら false（販売中）
		boolean isDeleted = status.equals("suspended");

		// 条件に合うグッズを5件分だけ取得する
		List<GoodsItem> goodsList = findByPage(isDeleted, keyword, page, size);

		// 状態（販売中 or 停止中）に合わせた総件数を取得する
		long totalCount = count(isDeleted, keyword);
		
		// 全体のページ数を計算（端数切り上げ。例：6件なら2ページ）
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		if (totalPages == 0) {
			totalPages = 1;
		}
		
		// 表示するページボタンの範囲を最大3つに設定
		int displayButtonCount = 3;
		
		// 開始ページ
		int startPage = Math.max(0, page - (displayButtonCount / 2));
		
		// 終了ページ
		int endPage = Math.min(totalPages - 1, startPage + displayButtonCount - 1);
		
		// ページの終わりでボタンが3つ未満になってしまう場合の調整
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}
		
		PageResult<GoodsItem> result = new PageResult<GoodsItem>(goodsList, page, totalPages, startPage, endPage);
		
		return result;
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
