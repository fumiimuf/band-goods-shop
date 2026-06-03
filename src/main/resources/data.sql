-- 1. カテゴリ情報の初期データ
INSERT INTO `category` (`name`) 
VALUES 
('T-SHIRTS'), 
('CD/VINYL'), 
('ACCESSORIES');

-- 2. ユーザー情報の初期データ（管理者1名、一般ユーザー17名）
INSERT INTO `user` (`name`, `email`, `password`, `zip_code`, `address`, `is_admin`) 
VALUES 
('管理者太郎', 'admin@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '123-4567', '大阪府大阪市...', 1), 
('一般太郎', 'user@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '123-4567', '大阪府大阪市...', 0),
('一般次郎', 'user2@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '234-5678', '大阪府堺市...', 0), 
('一般三郎', 'user3@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '345-6789', '大阪府東大阪市旭町1-1', 0),
('一般四郎', 'user4@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '456-7890', '大阪府豊中市本町2-2', 0),
('一般五郎', 'user5@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '567-8901', '大阪府吹田市金田町3-3', 0),
('佐藤花子', 'sato@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '678-9012', '大阪府高槻市城北町4-4', 0),
('鈴木一郎', 'suzuki@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '789-0123', '大阪府枚方市大垣内町5-5', 0),
('高橋健太', 'takahashi@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '890-1234', '大阪府八尾市本町6-6', 0),
('田中美紀', 'tanaka@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '901-2345', '大阪府寝屋川市本町7-7', 0),
('伊藤純', 'ito@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '111-2222', '大阪府岸和田市岸城町8-8', 0),
('渡辺大介', 'watanabe@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '222-3333', '大阪府和泉市府中町9-9', 0),
('山本由美', 'yamamoto@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '333-4444', '大阪府箕面市西小路1-2', 0),
('中村たくみ', 'nakamura@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '444-5555', '大阪府茨木市駅前2-3', 0),
('小林直樹', 'kobayashi@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '555-6666', '大阪府守口市京阪本通3-4', 0),
('加藤さくら', 'kato@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '666-7777', '大阪府門真市中町4-5', 0),
('吉田修平', 'yoshida@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '777-8888', '大阪府大東市谷川5-6', 0),
('佐々木翔', 'sasaki@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '888-9999', '大阪府松原市阿保6-7', 0);

-- 3. 商品情報の初期データ（すべてカテゴリID=1に関連付け）
INSERT INTO `goods` (`name`, `price`, `image`, `description`, `category_id`) 
VALUES 
('T-Shirt', 3500, '/T-shirt_A.png', '公式ロゴTシャツです。', 1), 
('公式トートバッグ', 2000, '/bag.jpg', '使い勝手の良いA4サイズのバッグです。', 1),
('ラバーバンド', 500, '/rubber-band.jpg', 'ライブの定番アイテム。', 1),
('マフラータオル', 1500, '/towel.jpg', '吸水性の良いコットン100%タオル。', 1),
('ロゴステッカーセット', 800, '/sticker.jpg', '大小5種類のステッカーセット。', 1),
('スマホリング', 1200, '/ring.jpg', '落下防止に便利なロゴ入りリング。', 1),
('ピンバッジ', 600, '/badge.jpg', 'キャップやバッグのアクセントに。', 1),
('クリアファイル', 400, '/file.jpg', 'アーティスト写真を使用したデザイン。', 1),
('パーカー', 6500, '/hoodie.jpg', '裏起毛で暖かい、ゆったりシルエット。', 1),
('ポスター', 1000, '/poster.jpg', '最新アルバムのメインビジュアル。', 1);