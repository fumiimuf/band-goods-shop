-- 1. カテゴリ情報の初期データ
INSERT INTO `category` (`name`) 
VALUES 
('アパレル'), 
('雑貨'), 
('応援グッズ');

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
-- 3. 商品情報の初期データ（カテゴリIDをそれぞれの属性に適切に分配）
INSERT INTO goods (name, price, image, description, category_id) VALUES 
-- グループA
('公式ツアーTシャツ（A）', 3500, 'tshirt_a.png', 'メンバーカラーをあしらった、普段使いもしやすいビッグシルエットの公式ツアーTシャツです。', 1),
('ラバーキーホルダー（A）', 800, 'keychain_a.png', 'バッグや鍵のアクセントにぴったりな、耐久性の高いぷっくりとした立体ラバーキーホルダーです。', 2),
('アクリルスタンド（A）', 1200, 'acrylic_stand_a.png', 'デスクや棚に飾って楽しめる、完全撮り下ろし（イラスト）デザインの限定アクリルスタンドです。', 2),
('オリジナルペンライト（A）', 2500, 'penlight_a.png', 'ライブ会場を鮮やかに染め上げる、メンバーカラーに高輝度発光するオリジナルペンライトです。', 3),
('ロゴステッカーセット（A）', 500, 'sticker_a.png', 'スマホケースやノートPCに貼るのにちょうどいい、耐水加工を施したお洒落なロゴステッカーです。', 2),
('応援ジャンボうちわ（A）', 1000, 'uchiwa_a.png', 'ライブでのアピールに欠かせない、遠くからでもメンバーの目を引く大きめサイズの応援うちわです。', 3),
('ロゴトートバッグ（A）', 2000, 'totebag_a.png', 'マチ付きでA4サイズもすっぽり入る、ライブで買ったグッズの持ち帰りにも便利な大容量トートバッグです。', 1), 

-- グループB
('公式ツアーTシャツ（B）', 3500, 'tshirt_b.png', 'メンバーカラーBをあしらった、普段使いもしやすいビッグシルエットの公式ツアーTシャツです。', 1),
('ラバーキーホルダー（B）', 800, 'keychain_b.png', 'バッグや鍵のアクセントにぴったりな、耐久性の高いぷっくりとした立体ラバーキーホルダーです。', 2),
('アクリルスタンド（B）', 1200, 'acrylic_stand_b.png', 'デスクや棚に飾って楽しめる、完全撮り下ろし（イラスト）デザインの限定アクリルスタンドです。', 2),
('オリジナルペンライト（B）', 2500, 'penlight_b.png', 'ライブ会場を鮮やかに染め上げる、メンバーカラーBに高輝度発光するオリジナルペンライトです。', 3),
('ロゴステッカーセット（B）', 500, 'sticker_b.png', 'スマホケースやノートPCに貼るのにちょうどいい、耐水加工を施したお洒落なロゴステッカーです。', 2),
('応援ジャンボうちわ（B）', 1000, 'uchiwa_b.png', 'ライブでのアピールに欠かせない、遠くからでもメンバーの目を引く大きめサイズの応援うちわです。', 3),
('ロゴトートバッグ（B）', 2000, 'totebag_b.png', 'マチ付きでA4サイズもすっぽり入る、ライブで買ったグッズの持ち帰りにも便利な大容量トートバッグです。', 1), 

-- グループC
('公式ツアーTシャツ（C）', 3500, 'tshirt_c.png', 'メンバーカラーCをあしらった、普段使いもしやすいビッグシルエットの公式ツアーTシャツです。', 1),
('ラバーキーホルダー（C）', 800, 'keychain_c.png', 'バッグや鍵のアクセントにぴったりな、耐久性の高いぷっくりとした立体ラバーキーホルダーです。', 2),
('アクリルスタンド（C）', 1200, 'acrylic_stand_c.png', 'デスクや棚に飾って楽しめる、完全撮り下ろし（イラスト）デザインの限定アクリルスタンドです。', 2),
('オリジナルペンライト（C）', 2500, 'penlight_c.png', 'ライブ会場を鮮やかに染め上げる、メンバーカラーCに高輝度発光するオリジナルペンライトです。', 3),
('ロゴステッカーセット（C）', 500, 'sticker_c.png', 'スマホケースやノートPCに貼るのにちょうどいい、耐水加工を施したお洒落なロゴステッカーです。', 2),
('応援ジャンボうちわ（C）', 1000, 'uchiwa_c.png', 'ライブでのアピールに欠かせない、遠くからでもメンバーの目を引く大きめサイズの応援うちわです。', 3),
('ロゴトートバッグ（C）', 2000, 'totebag_c.png', 'マチ付きでA4サイズもすっぽり入る、ライブで買ったグッズの持ち帰りにも便利な大容量トートバッグです。', 1);