package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;
import com.example.model.PageResult;
import com.example.repository.OrderDetailMapper;
import com.example.repository.OrderMapper;
import com.example.service.CartService;
import com.example.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;
	private final CartService cartService;
	
	@Override
	@Transactional
	public void createOrder(User user, List<CartItem> cartList, int totalAmount) {
		
		// 販売終了の商品がないか確認
		for (CartItem item : cartList) {
			if (item.getGoods() != null && item.getGoods().getIsDeleted() != null && item.getGoods().getIsDeleted()) {
				// 販売終了の商品が含まれていたら、独自の例外を発生させて処理を中断します
				throw new IllegalStateException(item.getGoods().getName() + " は販売終了しているため、注文できません。");
			}
		}
		
		// 親テーブル(order)への保存
		Order order = new Order();
		order.setUserId(user.getId());
		order.setOrderedName(user.getName());
		order.setOrderedZipCode(user.getZipCode());
		order.setOrderedAddress(user.getAddress());
		order.setTotalPrice(totalAmount);
		
		// 親テーブルにインサート（この時点で order.getId() に自動採番されたIDがセットされます）
        orderMapper.insert(order);
        
        // 子テーブル(order_detail)へカート内の全商品を1件ずつ保存
        for (CartItem item : cartList) {
        	OrderDetail detail = new OrderDetail();
        	// 自動採番されたばかりの注文ID（order.getId()）をセットします
        	detail.setOrderId(order.getId());
        	// カート内の商品情報を、購入時の状態としてセットします
        	detail.setOrderedImage(item.getGoods().getImage());
			detail.setOrderedName(item.getGoods().getName());
			detail.setOrderedPrice(item.getGoods().getPrice());
			detail.setQuantity(item.getQuantity());
			
			// 子テーブル（order_detail）に1件分をインサート
			orderDetailMapper.insert(detail);
        }
	     // ログインユーザーのカート内をすべて削除
		 cartService.deleteAllByUserId(user.getId());
	}
	
	// ログインユーザーの注文履歴と明細をすべて取得する
	@Override
	public List<OrderViewItem> getOrderHistory(Integer userId) {
		
		List<OrderViewItem> historyList = new ArrayList<>();
		
		// ログインユーザーの注文履歴(親)を取得
		List<Order> orders = orderMapper.selectByUserId(userId);
		
		// 注文の件数文ループを回して、それぞれの明細(子)を回収する
		for (Order order : orders) {
			OrderViewItem viewItem = new OrderViewItem();
			// 親をセット
			viewItem.setOrder(order);
			
			// この注文IDに紐づく明細リストを取得
			List<OrderDetail> details = orderDetailMapper.selectByOrderId(order.getId());
			// 子をセット
			viewItem.setDetails(details);
			
			// 親子セットになったものを大元に格納
			historyList.add(viewItem);
		}
		return historyList;
	}

	// ログインユーザーの注文履歴と明細をページ指定して取得する
	@Override
	public List<OrderViewItem> getOrderHistoryByPage(Integer userId, int page, int size) {
		
		List<OrderViewItem> historyList = new ArrayList<>();
		
		// ページ番号と1ページあたりの件数からoffsetを計算
		int offset = page * size;
		
		// ページに必要な件数だけの注文履歴(親)を取得
		List<Order> orders = orderMapper.selectByPage(userId, size, offset);
		
		// 取得した件数文(最大2件分)だけループで回して、それぞれの明細(子)を回収する
		for (Order order : orders) {
			OrderViewItem viewItem = new OrderViewItem();
			
			// 注文履歴(親)をセット
			viewItem.setOrder(order);
			
			// この注文履歴に紐づくすべての明細リストを取得
			List<OrderDetail> details = orderDetailMapper.selectByOrderId(order.getId());
			// 明細(子)をセット
			viewItem.setDetails(details);
			
			// 親子セットが完成した一組を、表示用のリストに格納
			historyList.add(viewItem);
		}
		
		return historyList;
	}

	// ログインユーザーの注文履歴が全部で何件あるか数える
	@Override
	public long countByUserId(Integer userId) {
		
		return orderMapper.selectCountByUserId(userId);
	}

	// 一般ユーザーの販売履歴を全件取得する
	@Override
	public List<OrderViewItem> getAllOrderHistoryByPage(String keyword, int page, int size) {
		
		List<OrderViewItem> historyList = new ArrayList<>();
		
		int offset = page * size;
		
		// Mapperから全件取得
		List<Order> orders = orderMapper.selectAllOrdersByPage(keyword, size, offset);
		
		// 取得した注文履歴（親）をループし、それぞれに紐づく明細（子）をセットする
		for (Order order : orders) {
			OrderViewItem viewItem = new OrderViewItem();
			
			// 親をセット
			viewItem.setOrder(order);
			
			// 既存の明細取得メソッドを利用して、この注文IDの明細を全て取得
			List<OrderDetail> details = orderDetailMapper.selectByOrderId(order.getId());
			viewItem.setDetails(details);
			
			historyList.add(viewItem);
		}
		return historyList;
	}

	@Override
	public long countAllOrders(String keyword) {
		return orderMapper.selectCountAllOrders(keyword);
	}

	@Override
	public PageResult<OrderViewItem> getOrderPage(Integer userId, int page) {
		
		// 1ページあたりの表示件数は「2件」と決める
		int size = 2;
		
		// Serviceにページ番号で必要な2件分を取得する
		List<OrderViewItem> historyList = getOrderHistoryByPage(userId, page, size);
		
		// ログインユーザーの全注文件数を取得する
		long totalCount = countByUserId(userId);
		
		// 全体のページ数を計算する。
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
		
		// ページの終わりでボタンが3つ未満になってしまう場合の調整
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}
		
		PageResult<OrderViewItem> result = new PageResult<OrderViewItem>(historyList, page, totalPages, startPage, endPage);
		
		return result;
	}

	@Override
	public PageResult<OrderViewItem> getAdminOrderPage(String keyword, int page) {
		
		int size = 5;
		
		List<OrderViewItem> orderList = getAllOrderHistoryByPage(keyword, page, size);
		
		long totalCount = countAllOrders(keyword);
		
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		if (totalPages == 0) {
			totalPages = 1;
		}
		
		int displayButtonCount = 3;
		
		int startPage = Math.max(0, page - (displayButtonCount / 2));
		
		int endPage = Math.min(totalPages - 1, startPage + displayButtonCount - 1);
		
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}
		
		PageResult<OrderViewItem> result = new PageResult<OrderViewItem>(orderList, page, totalPages, startPage, endPage);
		
		return result;
	}
	
	
}
