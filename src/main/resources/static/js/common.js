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
                $('#header-cart-count').text(res.newCartCount);
                new bootstrap.Toast($('#successToast')[0]).show();
            })
            .fail(() => {
                // 失敗：エラー用トーストを表示
                new bootstrap.Toast($('#errorToast')[0]).show();
            });
	});
	
	// カート数量変更（Ajax送信）
	$(document).on('change', '[data-cart-quantity-select]', function() {
	    
		// 操作されたセレクトボックスとその親フォームをキープ
		const $select = $(this);
		const $form = $select.closest('form');
		
		//「小計」のHTML要素を探しておく
		const $subtotalCell = $form.closest('tr').find('[data-item-subtotal]');
		
		// 新しく通信を始める前に、過去のエラー表示をリセットします
		$('[data-cart-quantity-select]').removeClass('is-invalid');
		$('.custom-error-message').hide();
		
		const url = $form.attr('action');
		
	    $.post(url, $form.serialize())

	        .done((res) => {
				// カンマ区切りにするためのフォーマッター
				const formatter = new Intl.NumberFormat('ja-JP');
				
				// 小計
				$subtotalCell.text('\ ' + formatter.format(res.subtotal));
				
				// 合計金額
				$('[data-total-amount]').text('¥ ' + formatter.format(res.totalAmount));
				
				// ヘッダーのカートアイコンの合計数
				$('#header-cart-count').text(res.totalQuantity);
	        })
	        .fail(() => {
	            //const msg = xhr.responseJSON?.message || "エラーが発生しました。";
				
				// 操作されたセレクトボックスだけを赤枠（is-invalid）にする
			  	//$select.addClass('is-invalid');
				
				//$form.find('.custom-error-message').text(msg).show();
	            
	            new bootstrap.Toast($('#errorToast')[0]).show();
	        });
	});
});