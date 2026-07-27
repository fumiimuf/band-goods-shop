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
				// サーバーから返ってきたJSONメッセージを取得
			    const res = xhr.responseJSON;

			    let toastMsg = "";
			    let fieldMsg = "";

			    if (res) {
			        // 【パターンA】入力エラー等の場合（両方表示）
			        toastMsg = res.message;      // トースト用メッセージ
			        fieldMsg = res.fieldError;   // ドロップダウン下用メッセージ
			    } else {
			        // 【パターンB】通信エラーの場合（トーストのみ表示）
			        toastMsg = "通信に失敗しました。ネットワーク接続を確認してください。";
			    }

			    // 1. バリデーションエラーメッセージ（fieldMsg）がある場合のみ、ドロップダウン下に表示
			    if (fieldMsg) {
			        $select.addClass('is-invalid');
			        $form.find('[data-quantity-error-message]').text(fieldMsg).show();
			    }

			    // 2. トーストを表示（通信エラー・入力エラーどちらでも表示）
			    const $errorToast = $('[data-quantity-error-toast]');
			    if ($errorToast.length) {
			        $errorToast.find('.toast-body').text(toastMsg);
			        new bootstrap.Toast($errorToast[0]).show();
			    }
			});
	});
});