-- 校园商城数据库初始化脚本
CREATE DATABASE IF NOT EXISTS campus_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_mall;

DROP TABLE IF EXISTS review_reply;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS payment_record;
DROP TABLE IF EXISTS order_event;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS login_log;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS sales_report;
DROP TABLE IF EXISTS hot_product;

CREATE TABLE `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20),
    email VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    role TINYINT NOT NULL DEFAULT 0 COMMENT '0学生/1教师/2商户/3管理员',
    avatar VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0正常/1冻结',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip VARCHAR(50),
    result TINYINT DEFAULT 1
);

CREATE TABLE address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    province VARCHAR(50),
    city VARCHAR(50),
    district VARCHAR(50),
    detail VARCHAR(255) NOT NULL,
    is_default TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    sort_order INT DEFAULT 0
);

CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    merchant_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    stock INT NOT NULL DEFAULT 0,
    images VARCHAR(1000),
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0待审核/1已上架/2已下架/3审核拒绝',
    reject_reason VARCHAR(255),
    view_count INT DEFAULT 0,
    sales_count INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_status (category_id, status),
    INDEX idx_merchant_status (merchant_id, status)
);

CREATE TABLE `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    pay_amount DECIMAL(10,2) NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    address_snapshot JSON,
    remark VARCHAR(255),
    pay_time DATETIME,
    ship_time DATETIME,
    confirm_time DATETIME,
    cancel_time DATETIME,
    logistics_no VARCHAR(64),
    version INT NOT NULL DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_time (user_id, create_time),
    INDEX idx_status_time (status, create_time)
);

CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_title VARCHAR(200),
    product_image VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    INDEX idx_order_id (order_id)
);

CREATE TABLE order_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    from_status TINYINT,
    to_status TINYINT NOT NULL,
    operator_id BIGINT,
    operator_role TINYINT,
    remark VARCHAR(255),
    event_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_order_time (order_id, event_time)
);

CREATE TABLE payment_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL,
    payment_no VARCHAR(64) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    channel TINYINT DEFAULT 1,
    status TINYINT NOT NULL DEFAULT 0,
    trade_no VARCHAR(64),
    callback_no VARCHAR(64),
    callback_data TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_order_no (order_no)
);

CREATE TABLE review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating TINYINT NOT NULL,
    content TEXT,
    images VARCHAR(1000),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE review_reply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    review_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT,
    template_code VARCHAR(50),
    business_id VARCHAR(64),
    status TINYINT DEFAULT 0 COMMENT '0未读/1已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
);

CREATE TABLE sales_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_date DATE NOT NULL,
    total_amount DECIMAL(12,2) DEFAULT 0,
    order_count INT DEFAULT 0,
    UNIQUE KEY uk_date (report_date)
);

CREATE TABLE hot_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    sales_count INT DEFAULT 0,
    report_date DATE NOT NULL,
    INDEX idx_date_sales (report_date, sales_count DESC)
);

-- 初始数据 (密码均为 123456)
INSERT INTO `user` (username, phone, password, role, status) VALUES
('admin', '13800000000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 3, 0),
('merchant1', '13800000001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 2, 0),
('student1', '13800000002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 0, 0);

INSERT INTO category (name, sort_order) VALUES
('数码电子', 1), ('图书文具', 2), ('生活用品', 3), ('运动户外', 4), ('食品饮料', 5);


INSERT INTO address (user_id, receiver_name, phone, province, city, district, detail, is_default) VALUES
(3, '张同学', '13800000002', '广东省', '广州市', '天河区', '某某大学1号宿舍楼301', 1);

-- 清空已有商品（可选，为了防止数据混淆）
-- TRUNCATE TABLE product;

-- 重新插入高度匹配的高清商品数据
INSERT INTO product (merchant_id, category_id, title, description, price, original_price, stock, images, status, sales_count) VALUES
-- 1. 数码电子 (category_id = 1)
(2, 1, '机械键盘 87键', '青轴幻彩背光，打字清脆，考研敲代码神器', 129.00, 169.00, 45, '["https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=400&h=400&fit=crop"]', 1, 15),
(2, 1, '智能手环 运动版', '全天候心率监测，睡眠分析，超长续航2周', 189.00, 249.00, 60, '["https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400&h=400&fit=crop"]', 1, 42),


-- 2. 图书文具 (category_id = 2)
(2, 2, '大学英语四六级真题详解', '包含最近5年真题及高清听力音频，备考必备', 35.80, 45.00, 500, '["https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400&h=400&fit=crop"]', 1, 310),
(2, 2, '精美彩色手账笔记本', '加厚纸质不洇墨，自带错题整理分区，高效复习', 18.90, 25.00, 300, '["https://images.unsplash.com/photo-1517842645767-c639042777db?w=400&h=400&fit=crop"]', 1, 140),
(2, 2, '办公书写中性笔 12支装', '0.5mm 经典黑，书写流畅不漏墨，刷题刷不停', 15.00, 20.00, 1000, '["https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=400&h=400&fit=crop"]', 1, 850),

-- 3. 生活用品 (category_id = 3)
(2, 3, '不锈钢保温杯 500ml', '316医用级不锈钢，长效保温24小时，自带茶隔', 59.00, 89.00, 85, '["https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400&h=400&fit=crop"]', 1, 95),
(2, 3, '简约陶瓷马克杯', '带盖带勺，男女生宿舍泡燕麦、喝水高颜值水杯', 25.00, 35.00, 200, '["https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=400&h=400&fit=crop"]', 1, 110),
(2, 3, '多功能双层便当盒', '可微波炉加热，密封防漏，带去食堂打包神器', 29.90, 39.90, 150, '["https://images.unsplash.com/photo-1606787366850-de6330128bfc?w=400&h=400&fit=crop"]', 1, 88),

-- 4. 运动户外 (category_id = 4)
(2, 4, '耐磨吸汗篮球（7号标准球）', '室内外通用软皮篮球，送气针+网袋+打气筒', 88.00, 128.00, 50, '["https://images.unsplash.com/photo-1546519638-68e109498ffc?w=400&h=400&fit=crop"]', 1, 34),
(2, 4, '时尚百搭运动跑鞋', '轻量化透气网面，减震耐磨，体育课及夜跑必备', 139.00, 199.00, 70, '["https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop"]', 1, 56),
(2, 4, '户外运动双肩背包', '大容量多隔层，可放电脑与运动装备，短途旅行通用', 99.00, 149.00, 90, '["https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop"]', 1, 45),

-- 5. 食品饮料 (category_id = 5)
(2, 5, '全脂纯牛奶 245ml*12盒', '优质乳蛋白，营养早餐搭配，浓郁丝滑', 39.90, 49.90, 120, '["https://images.unsplash.com/photo-1563636619-e9143da7973b?w=400&h=400&fit=crop"]', 1, 98),
(2, 5, '美式提神黑咖啡 30条', '零脂零糖冷热双泡，上课考研刷题提神醒脑必备', 29.90, 39.90, 400, '["https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=400&h=400&fit=crop"]', 1, 320);
