# ItCast Store - 网上书城系统

## 项目说明文档

**课程名称**: 网络编程技术  
**项目名称**: ItCast Store 网上书城系统  
**开发时间**: 2025年  
**技术架构**: 前后端分离架构

---

## 一、项目概述

### 1.1 项目背景

本项目是一个基于现代化技术栈开发的在线图书商城系统，采用前后端分离架构，实现了用户注册登录、商品浏览、购物车管理、订单处理等完整的电商功能。

### 1.2 项目目标

- 掌握前后端分离架构的设计与实现
- 学习 Spring Boot 3.x 企业级应用开发
- 熟悉 Vue 3 + TypeScript 前端开发
- 实践 Docker 容器化部署技术
- 理解 RESTful API 设计规范
- 掌握 JWT 认证机制和 Spring Security 安全框架

### 1.3 技术选型说明

本项目采用 2025 年主流的 Java Web 开发技术栈：

**后端技术栈**：
- **Java 21** - 使用最新的 LTS 版本，支持虚拟线程等新特性
- **Spring Boot 3.2** - 企业级应用框架，简化开发流程
- **Spring Security** - 提供完整的安全认证和授权机制
- **Spring Data JPA** - 简化数据库操作，提高开发效率
- **PostgreSQL 16** - 功能强大的开源关系型数据库
- **JWT** - 实现无状态认证，提升系统可扩展性
- **Maven** - 项目构建和依赖管理工具

**前端技术栈**：
- **Vue 3** - 采用 Composition API，提供更好的代码组织方式
- **TypeScript** - 类型安全，减少运行时错误
- **Element Plus** - 成熟的 Vue 3 UI 组件库
- **Vite** - 快速的构建工具，提升开发体验
- **Pinia** - Vue 3 官方推荐的状态管理库
- **Axios** - HTTP 客户端，处理 API 请求

**部署技术**：
- **Docker** - 容器化技术，实现环境一致性
- **Docker Compose** - 多容器服务编排
- **Nginx** - 反向代理和静态资源服务

---

## 二、项目功能

### 2.1 用户端功能

| 功能模块 | 功能描述 | 实现状态 |
|---------|---------|---------|
| 用户注册 | 支持用户注册，包含表单验证 | ✅ 已完成 |
| 用户登录 | JWT 认证，支持 Token 自动刷新 | ✅ 已完成 |
| 商品浏览 | 支持按分类浏览、搜索、分页展示 | ✅ 已完成 |
| 商品详情 | 展示商品详细信息、库存、价格等 | ✅ 已完成 |
| 购物车 | 添加商品、修改数量、删除商品 | ⚠️ 基础结构已完成 |
| 订单管理 | 创建订单、查看订单历史、订单详情 | ✅ 已完成 |
| 个人中心 | 查看和编辑个人信息 | ⚠️ 查看功能已完成 |
| 公告展示 | 查看系统公告列表和详情 | ✅ 已完成 |
| 热卖推荐 | 首页展示本周热卖商品 | ✅ 已完成 |

### 2.2 管理端功能

| 功能模块 | 功能描述 | 实现状态 |
|---------|---------|---------|
| 商品管理 | 商品的增删改查、图片上传 | ⚠️ 查询功能已完成 |
| 订单管理 | 订单查询、状态管理、订单详情 | ⚠️ 查询功能已完成 |
| 公告管理 | 公告的增删改查 | ⚠️ 查询功能已完成 |
| 用户管理 | 用户信息查看和管理 | ⚠️ 待实现 |
| 权限控制 | 基于角色的访问控制（RBAC） | ✅ 已完成 |

---

## 三、项目结构

```
itcaststore/
├── backend/                    # 后端项目（Spring Boot）
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── cn/itcast/itcaststore/
│   │       │       ├── controller/    # 控制器层（RESTful API）
│   │       │       ├── domain/         # 实体类（JPA 实体）
│   │       │       ├── repository/     # 数据访问层（Spring Data JPA）
│   │       │       ├── security/       # 安全配置（Spring Security + JWT）
│   │       │       ├── dto/            # 数据传输对象
│   │       │       └── util/           # 工具类
│   │       └── resources/
│   │           └── application.yml     # 应用配置文件
│   ├── pom.xml                        # Maven 依赖配置
│   └── Dockerfile                     # Docker 镜像构建文件
│
├── frontend/                   # 前端项目（Vue 3 + TypeScript）
│   ├── src/
│   │   ├── components/        # 可复用组件
│   │   ├── views/             # 页面组件
│   │   ├── router/            # 路由配置
│   │   ├── stores/            # 状态管理（Pinia）
│   │   └── utils/             # 工具函数
│   ├── package.json           # 依赖配置
│   ├── vite.config.ts         # Vite 构建配置
│   ├── tsconfig.json          # TypeScript 配置
│   └── Dockerfile             # Docker 镜像构建文件
│
├── docker/                     # Docker 部署配置
│   ├── docker-compose.yml     # 服务编排配置
│   └── postgres/
│       └── init.sql           # 数据库初始化脚本
│
├── plan.md                     # 功能实现计划
└── README.md                   # 项目说明文档（本文件）
```

---

## 四、技术实现要点

### 4.1 后端架构设计

#### 4.1.1 分层架构
采用经典的三层架构：
- **Controller 层**：处理 HTTP 请求，参数验证，返回响应
- **Service 层**：业务逻辑处理（当前版本简化，业务逻辑在 Controller）
- **Repository 层**：数据访问，使用 Spring Data JPA

#### 4.1.2 安全认证
- 使用 **JWT（JSON Web Token）** 实现无状态认证
- 集成 **Spring Security** 提供安全框架支持
- 密码使用 **BCrypt** 加密存储
- 支持基于角色的访问控制（RBAC）

#### 4.1.3 数据库设计
- 使用 **PostgreSQL** 作为关系型数据库
- 通过 **Spring Data JPA** 实现 ORM 映射
- 实体类使用 JPA 注解进行映射
- 支持数据库自动初始化

### 4.2 前端架构设计

#### 4.2.1 组件化开发
- 使用 Vue 3 **Composition API** 进行组件开发
- 采用 **TypeScript** 提供类型安全
- 使用 **Element Plus** 组件库快速构建 UI

#### 4.2.2 状态管理
- 使用 **Pinia** 进行全局状态管理
- 实现用户登录状态、购物车状态等管理

#### 4.2.3 路由管理
- 使用 **Vue Router** 实现前端路由
- 支持路由守卫，保护需要认证的页面

### 4.3 前后端交互

- 采用 **RESTful API** 设计规范
- 使用 **Axios** 进行 HTTP 请求
- 统一响应格式：`{ code, message, data }`
- 支持跨域请求（CORS 配置）

---

## 五、部署说明

### 5.1 Docker 部署（推荐）

本项目使用 Docker Compose 进行容器化部署，包含以下服务：

1. **PostgreSQL 数据库**（端口 5432）
2. **Spring Boot 后端**（端口 8080）
3. **Vue 前端 + Nginx**（端口 80）

#### 启动步骤

```bash
# 1. 进入 Docker 配置目录
cd docker

# 2. 构建并启动所有服务
docker-compose up -d --build

# 3. 查看服务状态
docker-compose ps

# 4. 查看日志（可选）
docker-compose logs -f
```

#### 访问地址

- **前端访问**: http://localhost
- **后端 API**: http://localhost:8080/api
- **数据库**: localhost:5432

#### 默认账号

- **管理员**: `admin` / `123456`
- **普通用户**: `madan` / `123456`

### 5.2 本地开发部署

#### 环境要求

- JDK 21
- Node.js 20+
- PostgreSQL 16
- Maven 3.9+

#### 启动步骤

**1. 启动数据库**

```bash
docker run -d \
  --name postgres \
  -e POSTGRES_DB=itcaststore \
  -e POSTGRES_USER=itcaststore \
  -e POSTGRES_PASSWORD=itcaststore \
  -p 5432:5432 \
  postgres:16-alpine
```

**2. 启动后端**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务地址：http://localhost:8080

**3. 启动前端**

```bash
cd frontend
npm install
npm run dev
```

前端服务地址：http://localhost:5173

---

## 六、API 接口文档

### 6.1 认证接口

#### 用户注册
- **接口**: `POST /api/auth/register`
- **请求体**:
  ```json
  {
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "gender": "男",
    "telephone": "13800138000"
  }
  ```
- **响应**: 返回 JWT Token 和用户信息

#### 用户登录
- **接口**: `POST /api/auth/login`
- **请求体**:
  ```json
  {
    "username": "testuser",
    "password": "123456"
  }
  ```
- **响应**: 返回 JWT Token 和用户信息

### 6.2 商品接口

#### 获取商品列表
- **接口**: `GET /api/products`
- **参数**: 
  - `page`: 页码（默认 0）
  - `size`: 每页数量（默认 12）
  - `category`: 分类（可选）
  - `keyword`: 搜索关键词（可选）
- **响应**: 分页商品列表

#### 获取商品详情
- **接口**: `GET /api/products/{id}`
- **响应**: 商品详细信息

#### 获取热卖商品
- **接口**: `GET /api/products/hot`
- **响应**: 热卖商品列表（最多 10 条）

### 6.3 订单接口（需要认证）

#### 获取订单列表
- **接口**: `GET /api/orders`
- **参数**: 
  - `page`: 页码（默认 0）
  - `size`: 每页数量（默认 10）
- **请求头**: `Authorization: Bearer {token}`
- **响应**: 分页订单列表

#### 创建订单
- **接口**: `POST /api/orders`
- **请求体**:
  ```json
  {
    "money": 100.00,
    "receiverName": "张三",
    "receiverAddress": "北京市朝阳区",
    "receiverPhone": "13800138000",
    "orderItems": [...]
  }
  ```
- **请求头**: `Authorization: Bearer {token}`

### 6.4 公告接口

#### 获取公告列表
- **接口**: `GET /api/notices`
- **响应**: 公告列表（按时间倒序）

#### 获取公告详情
- **接口**: `GET /api/notices/{id}`
- **响应**: 公告详细信息

---

## 七、数据库设计

### 7.1 数据表结构

#### 用户表（users）
- `id`: 主键，自增
- `username`: 用户名（唯一）
- `password`: 密码（BCrypt 加密）
- `email`: 邮箱
- `gender`: 性别
- `telephone`: 电话
- `role`: 角色（普通用户/超级用户）
- `state`: 状态（0:未激活 1:已激活）

#### 商品表（products）
- `id`: 主键（UUID）
- `name`: 商品名称
- `price`: 价格
- `category`: 分类
- `pnum`: 库存数量
- `imgurl`: 图片地址
- `description`: 商品描述

#### 订单表（orders）
- `id`: 主键（UUID）
- `money`: 订单金额
- `receiverName`: 收货人姓名
- `receiverAddress`: 收货地址
- `receiverPhone`: 收货电话
- `paystate`: 支付状态（0:未支付 1:已支付）
- `ordertime`: 下单时间
- `user_id`: 用户ID（外键）

#### 订单项表（orderitem）
- `order_id`: 订单ID（复合主键）
- `product_id`: 商品ID（复合主键）
- `buynum`: 购买数量

#### 公告表（notice）
- `n_id`: 主键，自增
- `title`: 公告标题
- `details`: 公告内容
- `n_time`: 发布时间

---

## 八、核心功能实现

### 8.1 用户认证流程

1. 用户提交登录信息（用户名、密码）
2. 后端验证用户信息
3. 验证通过后生成 JWT Token
4. 前端存储 Token 到 localStorage
5. 后续请求在请求头中携带 Token
6. 后端通过 JWT Filter 验证 Token 有效性

### 8.2 商品浏览流程

1. 前端请求商品列表 API
2. 支持分页、分类筛选、关键词搜索
3. 后端通过 Spring Data JPA 查询数据库
4. 返回分页结果给前端
5. 前端渲染商品列表

### 8.3 订单创建流程

1. 用户选择商品加入购物车
2. 填写收货信息
3. 提交订单请求
4. 后端验证用户身份和商品信息
5. 创建订单和订单项记录
6. 返回订单信息给前端

---

## 九、项目亮点

### 9.1 技术亮点

1. **现代化技术栈**
   - 使用 Java 21 最新 LTS 版本
   - Spring Boot 3.2 最新特性
   - Vue 3 Composition API
   - TypeScript 类型安全

2. **安全机制**
   - JWT 无状态认证
   - BCrypt 密码加密
   - Spring Security 安全框架
   - 基于角色的访问控制

3. **开发体验**
   - 前后端分离，职责清晰
   - RESTful API 设计规范
   - Docker 容器化部署
   - 热重载开发支持

### 9.2 架构优势

1. **前后端分离**
   - 前后端独立开发、部署
   - 支持多端接入（Web、移动端）
   - 提升开发效率和可维护性

2. **容器化部署**
   - 环境一致性
   - 快速部署和扩展
   - 便于运维管理

3. **代码规范**
   - 分层清晰，职责明确
   - 统一的响应格式
   - 完善的异常处理

---

## 十、开发环境

### 10.1 开发工具

- **IDE**: IntelliJ IDEA / VS Code
- **Java**: JDK 21
- **Node.js**: 20+
- **数据库**: PostgreSQL 16
- **构建工具**: Maven 3.9+, npm

### 10.2 开发规范

- **代码风格**: 遵循 Java 和 TypeScript 编码规范
- **Git 提交**: 使用有意义的提交信息
- **API 设计**: 遵循 RESTful 规范
- **注释**: 关键业务逻辑添加注释

---

## 十一、项目总结

### 11.1 完成情况

本项目已完成核心功能的开发，包括：
- ✅ 用户注册登录系统
- ✅ 商品浏览和搜索功能
- ✅ 订单管理功能
- ✅ 公告展示功能
- ✅ Docker 容器化部署
- ✅ JWT 认证机制
- ✅ 前后端分离架构

### 11.2 技术收获

通过本项目的开发，掌握了：
1. Spring Boot 3.x 企业级应用开发
2. Vue 3 + TypeScript 前端开发
3. JWT 认证和 Spring Security 安全框架
4. Docker 容器化部署技术
5. PostgreSQL 数据库使用
6. RESTful API 设计规范
7. 前后端分离架构设计

### 11.3 后续优化方向

1. 完善购物车功能
2. 实现管理后台完整功能
3. 添加文件上传功能
4. 优化数据库查询性能
5. 添加单元测试和集成测试
6. 完善错误处理和日志记录

---

## 十二、参考资料

- Spring Boot 官方文档: https://spring.io/projects/spring-boot
- Vue 3 官方文档: https://vuejs.org/
- PostgreSQL 官方文档: https://www.postgresql.org/docs/
- Docker 官方文档: https://docs.docker.com/

---

## 附录：快速启动命令

### Docker 部署

```bash
cd docker
docker-compose up -d --build
```

### 本地开发

**后端**:
```bash
cd backend
mvn spring-boot:run
```

**前端**:
```bash
cd frontend
npm install
npm run dev
```

---

**项目作者**: [学生姓名]  
**学号**: [学号]  
**提交日期**: 2025年

---

*本文档为课程项目说明文档，详细记录了项目的技术选型、功能实现、部署方式等内容，供老师审阅。*
