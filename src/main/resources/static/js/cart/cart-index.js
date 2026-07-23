$(function() {

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

		$.post(url, $form.serialize(), null, 'json')
			.done((res) => {
				// 小計
				// サーバーから返ってきた cartList から、変更したグッズIDと同じ行のデータを探す
				const targetItem = res.cartList.find(item => item.goods && Number(item.goods.id) === targetGoodsId);

				if (targetItem) {
					$subtotalCell.text("¥ " + Number(targetItem.subtotal).toLocaleString());
				}

				// 合計金額
				$('[data-total-amount]').text("¥ " + Number(res.totalAmount).toLocaleString());

				// ヘッダーのカートアイコンの合計数
				$('[data-header-cart-count]').text(res.totalQuantity);
			})
			.fail((xhr) => {
				// サーバー（Java）から届いたメッセージを取得（届いていなければ空文字）
				const errorMsg = xhr.responseJSON?.message || "";

				$select.addClass('is-invalid');
				$form.find('[data-quantity-error-message]').text(errorMsg).show();
				// トーストの本文にエラーメッセージをセットして表示
				const $errorToast = $('[data-quantity-error-toast]');
				if (errorMsg) {
					$errorToast.find('.toast-body').text(errorMsg);
				}
				
				new bootstrap.Toast($('[data-quantity-error-toast]')[0]).show();
			});
	});
});