SET FOREIGN_KEY_CHECKS = 0; -- 外部キー制約を一時的に無視
TRUNCATE TABLE `user`;
TRUNCATE TABLE `category`;
TRUNCATE TABLE `goods`;
SET FOREIGN_KEY_CHECKS = 1;




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
VALUES ('管理者太郎', 'admin@example.com', '$2a$10$LBnjYnoTF42WzSG7BbQZFOApS/yRdpE2U0jhGwvC0LyLfeA6VSqi.', '123-4567', '大阪府大阪市...', 1);

------------------------------------------- 
-- 商品情報の初期データ（動作確認用）
------------------------------------------- 
INSERT INTO `goods` (`name`, `price`, `image`, `description`, `category_id`) 
VALUES ('KING BROTHERS T-Shirt', 3500, '/images/tshirt.jpg', '公式ロゴTシャツです。', 1);