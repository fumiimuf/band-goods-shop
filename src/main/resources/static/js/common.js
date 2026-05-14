jQuery(function($) { 
    // 1. ポップオーバーの初期化 (既存の処理)
    $('[data-bs-toggle="popover"]').popover();
    
    // 2. カート追加ボタンのクリックイベント (Ajax)
    $('.add-to-cart-form').on('submit', function(e) {
        // 通常の画面遷移（リロード）をキャンセルします
        e.preventDefault();
        
        // 送信先URLとデータを取得
        const $form = $(this);
        const url = $form.attr('action');
        const formData = $form.serialize();
        
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            dataType: 'json'
        })
        .done(function(response) {
            // 【成功時】ヘッダーのカート個数を更新
            $('#header-cart-count').text(response.newCartCount);
            
            // 成功メッセージを表示して3秒後に消す
            $('#success-message').fadeIn();
            setTimeout(function() {
                $('#success-message').fadeOut();
            }, 3000);
            
            $('#error-message').hide();
        })
        .fail(function() {
            // 【失敗時】エラーメッセージを表示
            $('#error-message').fadeIn();
            $('#success-message').hide();
        });
    });
});