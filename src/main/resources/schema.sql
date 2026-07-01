-- 1. データベース（スキーマ）の作成と選択
CREATE SCHEMA IF NOT EXISTS `ec_db` DEFAULT CHARACTER SET utf8mb4;
USE `ec_db`;

-- 2. userテーブルの作成
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` VARCHAR(50) NOT NULL COMMENT '氏名',
  `email` VARCHAR(255) NOT NULL COMMENT 'メールアドレス',
  `password` VARCHAR(255) NOT NULL COMMENT 'パスワード（ハッシュ値）',
  `zip_code` VARCHAR(8) NOT NULL COMMENT '郵便番号',
  `address` VARCHAR(255) NOT NULL COMMENT '住所',
  `is_admin` TINYINT NOT NULL DEFAULT '0' COMMENT '管理者フラグ(0:一般, 1:管理者)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
);

-- 3. categoryテーブルの作成
CREATE TABLE IF NOT EXISTS `category` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` VARCHAR(50) NOT NULL COMMENT 'カテゴリ名',
  PRIMARY KEY (`id`)
);

-- 4. goodsテーブルの作成
CREATE TABLE IF NOT EXISTS `goods` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` VARCHAR(100) NOT NULL COMMENT '商品名',
  `price` INT NOT NULL COMMENT '価格',
  `image` VARCHAR(255) NOT NULL COMMENT '商品画像（ファイルパス）',
  `description` TEXT NOT NULL COMMENT '商品説明',
  `category_id` INT NOT NULL COMMENT 'カテゴリID',
  `create_date_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登録日時',
  `delete_date_time` DATETIME DEFAULT NULL COMMENT '削除日時',
  `is_deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '削除フラグ(0:販売中, 1:削除済み)',
  PRIMARY KEY (`id`), 
  CONSTRAINT `fk_goods_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
);

-- 5. cartテーブルの作成
CREATE TABLE IF NOT EXISTS `cart` (
  `user_id` INT NOT NULL COMMENT 'ユーザーID',
  `goods_id` INT NOT NULL COMMENT 'グッズID',
  `quantity` INT NOT NULL COMMENT '個数',
  PRIMARY KEY (`user_id`, `goods_id`), 
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_goods` FOREIGN KEY (`goods_id`) REFERENCES `goods` (`id`) ON DELETE CASCADE
);

-- 6. orderテーブルの作成
CREATE TABLE IF NOT EXISTS `order` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT NOT NULL COMMENT 'ユーザーID',
  `ordered_name` VARCHAR(50) NOT NULL COMMENT '購入時氏名',
  `ordered_zip_code` VARCHAR(8) NOT NULL COMMENT '購入時郵便番号',
  `ordered_address` VARCHAR(255) NOT NULL COMMENT '購入時住所',
  `total_price` INT NOT NULL COMMENT '合計金額',
  `ordered_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '購入日時',
  PRIMARY KEY (`id`), 
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

-- 7. order_detailテーブルの作成
CREATE TABLE IF NOT EXISTS `order_detail` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` INT NOT NULL COMMENT '注文ID',
  `ordered_image` VARCHAR(255) NOT NULL COMMENT '購入時商品画像（パス）',
  `ordered_name` VARCHAR(100) NOT NULL COMMENT '購入時商品名',
  `ordered_price` INT NOT NULL COMMENT '購入時価格',
  `quantity` INT NOT NULL COMMENT '個数',
  PRIMARY KEY (`id`), 
  CONSTRAINT `fk_detail_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE
);