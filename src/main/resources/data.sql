------------------------------------------- 
-- カテゴリ情報の初期データ
------------------------------------------- 
INSERT INTO `category` (`name`) VALUES ('T-SHIRTS');
INSERT INTO `category` (`name`) VALUES ('CD/VINYL');
INSERT INTO `category` (`name`) VALUES ('ACCESSORIES');

------------------------------------------- 
-- 管理者ユーザー（管理者フラグが1）
------------------------------------------- 
-- パスワードは仮に 'admin123' としていますが、本来はハッシュ化した値を入れます
INSERT INTO `user` (`name`, `email`, `password`, `zip_code`, `address`, `is_admin`) 
VALUES ('管理者太郎', 'admin@example.com', 'admin123', '123-4567', '大阪府大阪市...', 1);

------------------------------------------- 
-- 商品情報の初期データ（動作確認用）
------------------------------------------- 
INSERT INTO `goods` (`name`, `price`, `image`, `description`, `category_id`) 
VALUES ('KING BROTHERS T-Shirt', 3500, '/images/tshirt.jpg', '公式ロゴTシャツです。', 1);