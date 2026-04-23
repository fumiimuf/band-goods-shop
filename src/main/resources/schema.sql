------------------------------------------- 
-- テーブル作成
------------------------------------------- 
CREATE SCHEMA `ec_db` DEFAULT CHARACTER SET utf8mb4;


------------------------------------------- 
-- userテーブル関連
------------------------------------------- 
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '氏名',
  `email` varchar(255) NOT NULL COMMENT 'メールアドレス',
  `password` varchar(255) NOT NULL COMMENT 'パスワード（ハッシュ値）',
  `zip_code` varchar(8) NOT NULL COMMENT '郵便番号',
  `address` varchar(255) NOT NULL COMMENT '住所',
  `is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '管理者フラグ(0:一般, 1:管理者)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
)

------------------------------------------- 
-- goodsテーブル関連
------------------------------------------- 
CREATE TABLE `goods` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '商品名',
  `price` int NOT NULL COMMENT '価格',
  `image` varchar(255) DEFAULT NULL COMMENT '商品画像（ファイルパス）',
  `description` text NOT NULL COMMENT '商品説明',
  `category_id` int NOT NULL COMMENT 'カテゴリID',
  `create_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
  `delete_date_time` datetime DEFAULT NULL COMMENT '削除日時',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '削除フラグ(0:販売中, 1:削除済み)',
  PRIMARY KEY (`id`)
)

------------------------------------------- 
-- categoryテーブル関連
------------------------------------------- 
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT 'カテゴリ名',
  PRIMARY KEY (`id`)
)

------------------------------------------- 
-- cartテーブル関連
------------------------------------------- 
CREATE TABLE `cart` (
  `user_id` int NOT NULL COMMENT 'ユーザーID',
  `goods_id` int NOT NULL COMMENT 'グッズID',
  `quantity` int NOT NULL COMMENT '個数',
  PRIMARY KEY (`user_id`,`goods_id`)
)

------------------------------------------- 
-- orderテーブル商品関連
------------------------------------------- 
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int NOT NULL COMMENT 'ユーザーID',
  `ordered_name` varchar(50) NOT NULL COMMENT '購入時氏名',
  `ordered_zip_code` varchar(8) NOT NULL COMMENT '購入時郵便番号',
  `ordered_address` varchar(255) NOT NULL COMMENT '購入時住所',
  `total_price` int NOT NULL COMMENT '合計金額',
  `ordered_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '購入日時',
  PRIMARY KEY (`id`)
)

------------------------------------------- 
-- order_detailテーブル関連
------------------------------------------- 
CREATE TABLE `order_detail` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` int NOT NULL COMMENT '注文ID',
  `ordered_image` varchar(255) NOT NULL COMMENT '購入時商品画像（パス）',
  `ordered_name` varchar(100) NOT NULL COMMENT '購入時商品名',
  `ordered_price` int NOT NULL COMMENT '購入時価格',
  `quantity` int NOT NULL COMMENT '個数',
  PRIMARY KEY (`id`)
)

