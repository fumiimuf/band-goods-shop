-- SET FOREIGN_KEY_CHECKS = 0; -- 外部キー制約を一時的に無視
-- TRUNCATE TABLE `user`;
-- TRUNCATE TABLE `category`;
-- TRUNCATE TABLE `goods`;
-- SET FOREIGN_KEY_CHECKS = 1;




------------------------------------------- 
-- カテゴリ情報の初期データ
------------------------------------------- 
INSERT INTO `category` (`name`) 
VALUES 
('T-SHIRTS'), 
('CD/VINYL'), 
('ACCESSORIES');

------------------------------------------- 
-- 管理者ユーザー（管理者フラグが1）
------------------------------------------- 
-- パスワードは仮に 'password' としていますが、本来はハッシュ化した値を入れます
INSERT INTO `user` (`name`, `email`, `password`, `zip_code`, `address`, `is_admin`) 
VALUES 
('管理者太郎', 'admin@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '123-4567', '大阪府大阪市...', 1), 
('一般太郎', 'user@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '123-4567', '大阪府大阪市...', 0),
('一般次郎', 'user2@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '234-5678', '大阪府堺市...', 0);

------------------------------------------- 
-- 商品情報の初期データ（動作確認用）
------------------------------------------- 
INSERT INTO `goods` (`name`, `price`, `image`, `description`, `category_id`) 
VALUES 
('T-Shirt', 3500, '/images/T-shirt_A.png', '公式ロゴTシャツです。', 1), 
('公式トートバッグ', 2000, '/images/bag.jpg', '使い勝手の良いA4サイズのバッグです。', 1),
('ラバーバンド', 500, '/images/rubber-band.jpg', 'ライブの定番アイテム。', 1),
('マフラータオル', 1500, '/images/towel.jpg', '吸水性の良いコットン100%タオル。', 1),
('ロゴステッカーセット', 800, '/images/sticker.jpg', '大小5種類のステッカーセット。', 1),
('スマホリング', 1200, '/images/ring.jpg', '落下防止に便利なロゴ入りリング。', 1),
('ピンバッジ', 600, '/images/badge.jpg', 'キャップやバッグのアクセントに。', 1),
('クリアファイル', 400, '/images/file.jpg', 'アーティスト写真を使用したデザイン。', 1),
('パーカー', 6500, '/images/hoodie.jpg', '裏起毛で暖かい、ゆったりシルエット。', 1),
('ポスター', 1000, '/images/poster.jpg', '最新アルバムのメインビジュアル。', 1);