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
-- 用户数据（使用明文密码，密码都是"123456"）
INSERT INTO users (username, password, gender, email, telephone, introduce, active_code, state, role, regist_time)
VALUES 
    ('admin', '123456', '男', 'huan9yun@163.com', '13041019968', '我是超级管理员，我可以登录后台管理系统', '49338fdc-d8c9-46fa-8391-0fac7bf09707', 1, '超级用户', CURRENT_TIMESTAMP),
    ('madan', '123456', '女', 'huan9yun@163.com', '13269219270', '我是一个课程设计师', 'c1cc1229-f0ac-41b4-920c-dfef9f8a96a3', 1, '普通用户', CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- 商品数据（使用新的PNG图片路径，根据美术需求文档设计，库存值从高到低，确保热卖商品能正确显示）
-- 所有20本书都对应 product-1.png 到 product-20.png
INSERT INTO products (id, name, price, category, pnum, imgurl, description)
VALUES 
    -- product-1: JavaScript高级程序设计（编程类）
    ('product-1', 'JavaScript高级程序设计（第4版）', 129.00, '编程类', 150, '/products/product-1.png', 'JavaScript编程领域的经典之作，全面介绍JavaScript语言核心特性、DOM操作、事件处理、异步编程等核心知识。适合前端开发者和JavaScript学习者，从基础到高级应用一应俱全。'),
    
    -- product-2: UI/UX设计指南（设计类）
    ('product-2', 'UI/UX设计指南：从入门到精通', 89.00, '设计类', 140, '/products/product-2.png', '全面介绍UI/UX设计原理和实践方法，涵盖用户研究、交互设计、视觉设计、原型制作等核心内容。帮助设计师提升专业技能，打造优秀的用户体验。'),
    
    -- product-3: Python数据分析（技术类）
    ('product-3', 'Python数据分析实战', 79.00, '技术类', 130, '/products/product-3.png', '深入浅出地讲解Python数据分析的核心技术，包括NumPy、Pandas、Matplotlib等库的使用，以及数据清洗、可视化、统计分析等实战案例。适合数据分析师和Python开发者。'),
    
    -- product-4: Vue.js实战（前端开发）
    ('product-4', 'Vue.js 3.0实战开发', 69.00, '前端开发', 120, '/products/product-4.png', 'Vue.js框架的完整实战教程，从基础语法到高级特性，包括组件开发、状态管理、路由配置、服务端渲染等。通过实际项目案例，帮助开发者快速掌握Vue.js开发技能。'),
    
    -- product-5: Node.js开发指南（后端开发）
    ('product-5', 'Node.js后端开发实战', 75.00, '后端开发', 110, '/products/product-5.png', 'Node.js服务器端开发的完整指南，涵盖Express框架、数据库操作、API设计、性能优化等内容。适合后端开发者和全栈开发者学习使用。'),
    
    -- product-6: React Native入门（移动开发）
    ('product-6', 'React Native移动应用开发', 65.00, '移动开发', 100, '/products/product-6.png', '使用React Native开发跨平台移动应用的完整教程，包括组件开发、导航、状态管理、原生模块调用等。一次开发，同时支持iOS和Android平台。'),
    
    -- product-7: 数据结构与算法（算法）
    ('product-7', '数据结构与算法：从理论到实践', 85.00, '算法', 95, '/products/product-7.png', '计算机科学核心课程，系统讲解常见数据结构和算法设计思想，包括数组、链表、树、图等数据结构，以及排序、搜索、动态规划等算法。适合程序员和算法学习者。'),
    
    -- product-8: MySQL性能优化（数据库）
    ('product-8', 'MySQL性能优化与调优', 72.00, '数据库', 90, '/products/product-8.png', '数据库性能优化的实践指南，深入讲解MySQL索引优化、查询优化、存储引擎选择、主从复制等高级技术。帮助DBA和开发者提升数据库性能。'),
    
    -- product-9: Web安全攻防（网络安全）
    ('product-9', 'Web安全攻防：从入门到精通', 88.00, '网络安全', 85, '/products/product-9.png', 'Web安全防护和攻击技术详解，涵盖SQL注入、XSS攻击、CSRF、文件上传漏洞等常见安全问题，以及防护措施和最佳实践。适合安全工程师和Web开发者。'),
    
    -- product-10: AWS架构设计（云计算）
    ('product-10', 'AWS云架构设计与实践', 95.00, '云计算', 80, '/products/product-10.png', 'AWS云平台架构设计与最佳实践，包括EC2、S3、RDS、Lambda等核心服务的应用，以及高可用、可扩展、安全可靠的云架构设计模式。'),
    
    -- product-11: 深度学习入门（机器学习）
    ('product-11', '深度学习入门：理论与实践', 108.00, '机器学习', 75, '/products/product-11.png', '深度学习基础理论和实践应用，包括神经网络、卷积神经网络、循环神经网络等核心概念，以及TensorFlow、PyTorch等框架的使用。适合AI学习者和数据科学家。'),
    
    -- product-12: Docker容器技术（DevOps）
    ('product-12', 'Docker容器化部署实战', 68.00, 'DevOps', 70, '/products/product-12.png', 'Docker容器化部署和管理技术，包括镜像构建、容器编排、Docker Compose、Kubernetes等工具的使用。帮助开发者实现应用的容器化部署和运维。'),
    
    -- product-13: 自动化测试实践（测试）
    ('product-13', '自动化测试实践指南', 62.00, '测试', 65, '/products/product-13.png', '软件测试自动化的方法和工具，包括单元测试、集成测试、端到端测试等测试类型，以及Jest、Selenium、Cypress等测试框架的使用。提升测试效率和质量。'),
    
    -- product-14: 敏捷开发方法（项目管理）
    ('product-14', '敏捷开发方法：Scrum实战', 58.00, '项目管理', 60, '/products/product-14.png', '敏捷开发流程和Scrum实践，包括Sprint规划、每日站会、回顾会议等敏捷实践，以及用户故事、看板、燃尽图等工具的使用。适合项目经理和开发团队。'),
    
    -- product-15: 微服务架构设计（架构设计）
    ('product-15', '微服务架构设计与实践', 92.00, '架构设计', 55, '/products/product-15.png', '微服务架构的设计原则和实现，包括服务拆分、API网关、服务发现、配置管理、分布式事务等核心概念。帮助架构师设计可扩展的微服务系统。'),
    
    -- product-16: React进阶指南（前端框架）
    ('product-16', 'React进阶指南：深入理解React', 78.00, '前端框架', 50, '/products/product-16.png', 'React高级特性和性能优化技巧，包括Hooks、Context、性能优化、代码分割、服务端渲染等进阶内容。适合有一定React基础的开发者深入学习。'),
    
    -- product-17: 全栈Web开发（全栈开发）
    ('product-17', '全栈Web开发：前后端一体化', 82.00, '全栈开发', 45, '/products/product-17.png', '前后端全栈开发技术栈，包括前端框架、后端API、数据库设计、部署运维等全流程开发知识。帮助开发者成为全栈工程师。'),
    
    -- product-18: Web性能优化（性能优化）
    ('product-18', 'Web性能优化：提升用户体验', 66.00, '性能优化', 40, '/products/product-18.png', 'Web应用性能优化的实战技巧，包括代码优化、资源压缩、缓存策略、CDN使用、懒加载等技术。帮助开发者打造快速响应的Web应用。'),
    
    -- product-19: 设计模式精解（设计模式）
    ('product-19', '设计模式精解：23种经典模式', 74.00, '设计模式', 35, '/products/product-19.png', '23种设计模式的深入解析，包括创建型、结构型、行为型三大类设计模式，以及在实际项目中的应用场景。提升代码质量和可维护性。'),
    
    -- product-20: 大规模系统设计（系统设计）
    ('product-20', '大规模系统设计：分布式系统架构', 98.00, '系统设计', 30, '/products/product-20.png', '大规模分布式系统设计原理，包括负载均衡、数据库分片、缓存策略、消息队列、分布式锁等核心技术。适合系统架构师和高级开发者。')
ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    price = EXCLUDED.price,
    category = EXCLUDED.category,
    pnum = EXCLUDED.pnum,
    imgurl = EXCLUDED.imgurl,
    description = EXCLUDED.description;

-- 公告数据
INSERT INTO notice (title, details, n_time)
VALUES 
    ('暂停当日达业务', '尊敬的网上书城用户，为了让大家有更好的购物体验，3月25日起，当日达业务关小黑屋回炉升级！具体开放时间请留意公告，感谢大家的支持与理解，祝大家购物愉快！', '20160111113420'),
    ('年货礼包兑换时间通知', '尊敬的网上书城用户：非常抱歉，因为年后部分供应商工厂开工较晚，导致个别商品到货延迟。基于此，15年年货礼包的兑换入口会延期到3月17日，请您在有效期内完成兑换。', '20160111113411')
ON CONFLICT DO NOTHING;
