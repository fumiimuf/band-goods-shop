jQuery(function($) { 
	
	console.log("common.jsが読み込まれました。");
	
    // 1. ポップオーバーの初期化 (既存の処理)
    $('[data-bs-toggle="popover"]').popover();

    // 2. カート追加（Ajax送信）
    $('[data-cart-add-form]').on('submit', function(e) {
        // ブラウザ本来のページ遷移をキャンセル
        e.preventDefault();
		
		const $form = $(this);
		
		const url = $form.attr('action');
		
        // $.post(宛先, 送信データ) でシンプルに記述
        $.post(url, $form.serialize())
            .done((res) => {
                // 成功：ヘッダーの個数を更新し、成功トーストを表示
                $('[data-header-cart-count]').text(res.newCartCount);
                new bootstrap.Toast($('[data-cart-success-toast]')[0]).show();
            })
            .fail(() => {
                // 失敗：エラー用トーストを表示
                new bootstrap.Toast($('[data-cart-error-toast]')[0]).show();
            });
	});
	
	// カート数量変更（Ajax送信）
	$('[data-cart-quantity-select]').on('change', function() {
	    
		// 操作されたセレクトボックスとその親フォームをキープ
		const $select = $(this);
		const $form = $select.closest('[data-cart-quantity-form]');
		
		//「小計」のHTML要素を探しておく
		const $subtotalCell = $form.closest('tr').find('[data-item-subtotal]');
		
		// HTMLの隠しフィールド <input type="hidden" name="goodsId"> からグッズIDを取得して数値に変換
		const targetGoodsId = Number($form.find('input[name="goodsId"]').val());
		
		// 新しく通信を始める前に、過去のエラー表示をリセットします
		$('[data-cart-quantity-select]').removeClass('is-invalid');
		$('[data-quantity-error-message]').hide();
		
		const url = $form.attr('action');
		
	    $.post(url, $form.serialize())

	        .done((res) => {
				// 小計
				// サーバーから返ってきた cartList から、変更したグッズIDと同じ行のデータを探す
				const targetItem = res.cartList.find(item => item.goods && item.goods.id === targetGoodsId);
				
				if (targetItem) {
					// Java側の getSubtotal() の計算結果を取得（万が一に備え、フロント側での計算も安全策として用意）
					const subtotal = targetItem.subtotal !== undefined 
									? targetItem.subtotal 
									: targetItem.goods.price * targetItem.quantity;
					
					// 3桁カンマに変換
					const formattedSubtotal = Number(subtotal).toLocaleString();
					
					$subtotalCell.text("¥ " + formattedSubtotal);
				}
				
				// 合計金額
				const formattedTotalAmount = Number(res.totalAmount).toLocaleString();
			    $('[data-total-amount]').text("¥ " + formattedTotalAmount);
				
				// ヘッダーのカートアイコンの合計数
				$('[data-header-cart-count]').text(res.totalQuantity);
	        })
	        .fail(() => {
				$select.addClass('is-invalid');
				$form.find('[data-quantity-error-message]').show();
	            new bootstrap.Toast($('[data-quantity-error-toast]')[0]).show();
	        });
	});
});