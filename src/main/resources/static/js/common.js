jQuery(function($) { 
	
	// --- 💡 【重要】ここからセキュリティ設定を追加 ---
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    
    // Ajax送信のたびに、自動的に合言葉（トークン）をヘッダーに詰める「おまじない」
    $(document).ajaxSend(function(e, xhr, options) {
        if (token && header) {
            xhr.setRequestHeader(header, token);
        }
    });
	
	console.log("common.jsが読み込まれました。");
	
    // 1. ポップオーバーの初期化 (既存の処理)
    $('[data-bs-toggle="popover"]').popover();

    // 2. カート追加（Ajax送信）
    $('.add-to-cart-form').on('submit', function(e) {
        // ブラウザ本来のページ遷移をキャンセル
        e.preventDefault();

        // $.post(宛先, 送信データ) でシンプルに記述
        $.post('/api/cart/add', $(this).serialize())
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
	$(document).on('change', '.update-quantity-select', function() {
	    
		// 操作されたセレクトボックスの位置を最初にキープします
		const $select = $(this);
		
		// 新しく通信を始める前に、過去のエラー表示をリセットします
		$('.update-quantity-select').removeClass('is-invalid');
		$('.custom-error-message').hide();
		
	    $.post('/api/cart/update-quantity', $select.closest('form').serialize())

	        .done((res) => {
	            
	            const totalText = '¥' + new Intl.NumberFormat('ja-JP').format(res.newTotalAmount);
	            
	            $('[data-total-amount]').text(totalText);
	        })

	        .fail((xhr) => {
	            const msg = xhr.responseJSON?.message || "エラーが発生しました。";
				
				// 操作されたセレクトボックスだけを赤枠（is-invalid）にする
			  	$select.addClass('is-invalid');
				
				$select.closest('form').find('.custom-error-message').text(msg).show();
	            
	            new bootstrap.Toast($('#errorToast')[0]).show();
	        });
	});
});