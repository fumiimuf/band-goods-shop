$(function() {

	console.log("goods-index.jsが読み込まれました。");

	// 1. ポップオーバーの初期化（標準のclickトリガーを使用）
	const $popovers = $('[data-bs-toggle="popover"]').popover();

	// ボタン以外をクリックしたときに閉じるための補正コード
	$(document).on('click', function(e) {
		// クリックされた要素が「商品説明ボタン」でもなく、「ポップオーバーの吹き出し内」でもない場合
		if (!$(e.target).closest('[data-bs-toggle="popover"]').length && !$(e.target).closest('.popover').length) {
			$popovers.popover('hide'); // すべてのポップオーバーを閉じる
		}
	});

	console.log("[初期化] Bootstrapポップオーバー(clickトリガー)を有効化しました。");

	// 2. カート追加（Ajax送信）
	$('[data-cart-add-form]').on('submit', function(e) {
		// ブラウザ本来のページ遷移をキャンセル
		e.preventDefault();

		const $form = $(this);

		const url = $form.attr('action');

		console.log("[カート追加] 送信を開始します。URL: " + url);

		// $.post(宛先, 送信データ) でシンプルに記述
		$.post(url, $form.serialize())
			.done((res) => {

				console.log("[カート追加] 成功しました。サーバーからのレスポンス:", res);

				// 成功：ヘッダーの個数を更新し、成功トーストを表示
				$('[data-header-cart-count]').text(res.newCartCount);
				new bootstrap.Toast($('[data-cart-success-toast]')[0]).show();

				console.log("[カート追加] UIの更新が完了しました。新しいカート内個数: " + res.newCartCount);
			})
			.fail(() => {

				console.error("[カート追加] 失敗しました。エラー内容:");

				// 失敗：エラー用トーストを表示
				new bootstrap.Toast($('[data-cart-error-toast]')[0]).show();
			});
	});
});