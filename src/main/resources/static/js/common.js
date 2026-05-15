jQuery(function($) { 
	
	console.log("common.jsが読み込まれました。");
	
    // 1. ポップオーバーの初期化 (既存の処理)
    $('[data-bs-toggle="popover"]').popover();
    
    // 2. カート追加ボタンのクリックイベント (Ajax)
    $('.add-to-cart-form').on('submit', function(e) {
        // 通常の画面遷移（リロード）をキャンセルします
        e.preventDefault();
        
        // 送信先URLとデータを取得
        const $form = $(this); // どのフォームか特定する
        const formData = $form.serialize(); // 特定したフォームの中のデータをJavaに送れる形式（goodsId=10 のような文字列）に変換する
        
        $.ajax({
            url: '/api/cart/add',
            type: 'POST',
            data: formData,
            dataType: 'json'
        })
        .done(function(response) {
            // 【成功時】ヘッダーのカート個数を更新
            $('#header-cart-count').text(response.newCartCount);
			// Bootstrap標準の命令でトーストを表示
            const toast = new bootstrap.Toast($('#successToast')[0]);
            toast.show();
		})
        .fail(function() {
			// エラー時も同様
            const toast = new bootstrap.Toast($('#errorToast')[0]);
            toast.show();
        });
    });

    // カート追加（Ajax送信）
    jQuery(function($) {
    // 1. ポップオーバーの初期化
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
});
});