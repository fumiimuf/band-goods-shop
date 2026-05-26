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
			if (item.getIsDeleted() != null && item.getIsDeleted()) {
				// 販売終了の商品が含まれていたら、独自の例外を発生させて処理を中断します
				throw new IllegalStateException(item.getName() + " は販売終了しているため、注文できません。");
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
        	detail.setOrderedImage(item.getImage());
			detail.setOrderedName(item.getName());
			detail.setOrderedPrice(item.getPrice());
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
		List<Order> orders = orderMapper.findByUserId(userId);
		
		// 注文の件数文ループを回して、それぞれの明細(子)を回収する
		for (Order order : orders) {
			OrderViewItem viewItem = new OrderViewItem();
			// 親をセット
			viewItem.setOrder(order);
			
			// この注文IDに紐づく明細リストを取得
			List<OrderDetail> details = orderDetailMapper.findByOrderId(order.getId());
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
		
		
		return null;
	}

	// ログインユーザーの注文履歴が全部で何件あるか数える
	@Override
	public long countByUserId(Integer userId) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
	
	
}
