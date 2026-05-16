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
	// ① 【jQuery】 $(document).on(...) はjQueryのイベント監視機能です
	$(document).on('change', '.update-quantity-select', function() {
	    
	    // ② 【jQuery】 $.post や $(this), .closest(), .serialize() はすべてjQueryの便利な機能です
	    $.post('/api/cart/update-quantity', $(this).closest('form').serialize())

	        // ③ 【jQuery】 .done はjQuery独自の「成功した時の約束」という書き方です
	        .done((res) => {
	            
	            // ④ 【JavaScript】 Intl.NumberFormat は、最新の「純粋なJavaScript」の機能です（jQueryではありません）
	            // ⑤ 【JavaScript】 const は最新のJavaScriptでの「変数の宣言」です
	            const totalText = '¥' + new Intl.NumberFormat('ja-JP').format(res.newTotalAmount);
	            
	            // ⑥ 【jQuery】 $('[data-total-amount]').text(...) はjQueryのHTML書き換え機能です
	            $('[data-total-amount]').text(totalText);
	        })

	        // ⑦ 【jQuery】 .fail はjQuery独自の「失敗した時の約束」という書き方です
	        .fail((xhr) => {
	            
	            // ⑧ 【JavaScript】 ?. や ||（論理和）は最新のJavaScriptの書き方です
	            const msg = xhr.responseJSON?.message || "エラーが発生しました";
	            
	            // ⑨ 【jQuery】 $('#errorToast .toast-body').text(...) はjQueryの機能です
	            $('#errorToast .toast-body').text(msg);
	            
	            // ⑩ 【JavaScript/Bootstrap】 new bootstrap.Toast(...) は「BootstrapのJavaScript」です
	            // ※ [0] を使ってjQueryの箱から「純粋なHTML要素」を取り出しています
	            new bootstrap.Toast($('#errorToast')[0]).show();
	        });
	});
});