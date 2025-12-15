-- PostgreSQL 数据库初始化脚本
-- 将 MySQL 表结构转换为 PostgreSQL

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    gender VARCHAR(2),
    email VARCHAR(50),
    telephone VARCHAR(20),
    introduce VARCHAR(500),
    active_code VARCHAR(50),
    state INTEGER DEFAULT 0,
    role VARCHAR(10) DEFAULT '普通用户',
    regist_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建商品表
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(40),
    pnum INTEGER NOT NULL,
    imgurl VARCHAR(255),
    description VARCHAR(1000)
);

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(100) PRIMARY KEY,
    money DECIMAL(10, 2) NOT NULL,
    receiver_address VARCHAR(255),
    receiver_name VARCHAR(20),
    receiver_phone VARCHAR(20),
    paystate INTEGER DEFAULT 0,
    ordertime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 创建订单项表
CREATE TABLE IF NOT EXISTS orderitem (
    order_id VARCHAR(100) NOT NULL,
    product_id VARCHAR(100) NOT NULL,
    buynum INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 创建公告表
CREATE TABLE IF NOT EXISTS notice (
    n_id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    details TEXT,
    n_time VARCHAR(18)
);

-- 插入初始数据
-- 用户数据（密码需要加密，这里先插入，实际应该通过应用加密）
INSERT INTO users (username, password, gender, email, telephone, introduce, active_code, state, role, regist_time)
VALUES 
    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwN8Tq5O', '男', 'huan9yun@163.com', '13041019968', '我是超级管理员，我可以登录后台管理系统', '49338fdc-d8c9-46fa-8391-0fac7bf09707', 1, '超级用户', CURRENT_TIMESTAMP),
    ('madan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwN8Tq5O', '女', 'huan9yun@163.com', '13269219270', '我是一个课程设计师', 'c1cc1229-f0ac-41b4-920c-dfef9f8a96a3', 1, '普通用户', CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- 商品数据
INSERT INTO products (id, name, price, category, pnum, imgurl, description)
VALUES 
    ('3a0196b2-71c1-4d4d-a4e8-d1f875096f00', '网管员必备宝典', 20.00, '计算机', 15, '/productImg/4/3/0270eba2-2b48-48df-956b-0341204384d9.jpg', '计算机类'),
    ('79bbe618-d2f8-4081-b35a-62ebbe938b64', 'Java基础入门', 44.50, '计算机', 7, '/productImg/9/0/697a23d6-225a-41a3-8c20-7ab624265ecc.png', '《Java基础入门》从初学者的角度详细讲解了Java开发中重点用到的多种技术。'),
    ('84c842da-16b6-4e87-953e-859a1ca62bab', '赢在影响力', 89.00, '励志', 47, '/productImg/2/8/acc9d557-f8c9-426b-9aec-50a5a7cf3960.jpg', '《赢在影响力：人际交往的学问》创造了全球出版史上空前的发行记录。')
ON CONFLICT (id) DO NOTHING;

-- 公告数据
INSERT INTO notice (title, details, n_time)
VALUES 
    ('暂停当日达业务', '尊敬的网上书城用户，为了让大家有更好的购物体验，3月25日起，当日达业务关小黑屋回炉升级！具体开放时间请留意公告，感谢大家的支持与理解，祝大家购物愉快！', '20160111113420'),
    ('年货礼包兑换时间通知', '尊敬的网上书城用户：非常抱歉，因为年后部分供应商工厂开工较晚，导致个别商品到货延迟。基于此，15年年货礼包的兑换入口会延期到3月17日，请您在有效期内完成兑换。', '20160111113411')
ON CONFLICT DO NOTHING;
