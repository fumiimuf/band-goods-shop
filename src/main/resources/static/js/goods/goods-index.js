$(function() {

    // 1. ポップオーバーの初期化（標準のclickトリガーを使用）
    const $popovers = $('[data-bs-toggle="popover"]').popover();

    // ボタン以外をクリックしたときに閉じるための補正コード
    $(document).on('click', function(e) {
        // クリックされた要素が「商品説明ボタン」でもなく、「ポップオーバーの吹き出し内」でもない場合
        if (!$(e.target).closest('[data-bs-toggle="popover"]').length && !$(e.target).closest('.popover').length) {
            $popovers.popover('hide'); // すべてのポップオーバーを閉じる
        }
    });

    // 2. カート追加（Ajax送信）
    $('[data-cart-add-form]').on('submit', function(e) {
        // ブラウザ本来のページ遷移をキャンセル
        e.preventDefault();

        const $form = $(this);

        const url = $form.attr('action');

        // $.post(宛先, 送信データ) でシンプルに記述
        $.post(url, $form.serialize(), null, 'json')
            .done((res) => {
                // ① 成功（true）：ヘッダーの個数を更新し、成功トーストを表示
                $('[data-header-cart-count]').text(res.newCartCount);

                // Javaから届いた成功メッセージがあればトーストの本文にセット（HTML側の要素がある場合）
                if (res.message) {
                    $('[data-cart-success-toast] .toast-body').text(res.message);
                }

                new bootstrap.Toast($('[data-cart-success-toast]')[0]).show();
            })
            .fail((xhr) => {
				// サーバー（Java）から届いたメッセージを取得（届いていなければ空文字）
                const errorMsg = xhr.responseJSON?.message || "";

                // 3. エラートーストの本文にメッセージをセットして表示
                $('[data-cart-error-message]').text(errorMsg);
                new bootstrap.Toast($('[data-cart-error-toast]')[0]).show();
            });
    });
});