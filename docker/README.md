# Docker 部署指南

## 项目结构

```
itcaststore/
├── docker/
│   ├── docker-compose.yml      # Docker Compose 服务编排配置
│   ├── entrypoint.sh           # 后端应用启动脚本
│   ├── c3p0-config.xml         # Docker 环境数据库配置
│   ├── mysql/
│   │   └── init.sql            # MySQL 数据库初始化脚本
│   └── nginx/
│       ├── Dockerfile          # Nginx 镜像构建文件
│       └── nginx.conf          # Nginx 配置文件
├── itcaststore/
│   └── Dockerfile              # 后端应用镜像构建文件
└── .dockerignore               # Docker 构建忽略文件
```

## 服务说明

### 1. MySQL 数据库服务
- **镜像**: mysql:5.7
- **端口**: 3306
- **数据卷**: mysql_data（持久化数据库数据）
- **初始化**: 自动执行 `mysql/init.sql` 创建数据库和表结构

### 2. 后端应用服务（Tomcat）
- **基础镜像**: tomcat:8.5-jre8
- **端口**: 8080
- **数据卷**: product_img_data（持久化上传的商品图片）
- **特性**: 
  - 自动等待 MySQL 服务就绪
  - 自动配置数据库连接

### 3. Nginx 反向代理服务
- **镜像**: nginx:alpine
- **端口**: 80
- **功能**: 
  - 反向代理到后端 Tomcat 服务
  - 静态资源缓存
  - 负载均衡（可扩展）

## 快速开始

### 前置要求
- Docker Engine 20.10+
- Docker Compose 2.0+

### 部署步骤

1. **进入 docker 目录**
```bash
cd docker
```

2. **构建并启动所有服务**
```bash
docker-compose up -d
```

3. **查看服务状态**
```bash
docker-compose ps
```

4. **查看日志**
```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f backend
docker-compose logs -f mysql
docker-compose logs -f nginx
```

5. **访问应用**
- 前端访问: http://localhost (通过 Nginx 反向代理)
- 后端直接访问: http://localhost:8080 (直接访问 Tomcat)
- MySQL 连接: localhost:3306

### 常用命令

```bash
# 停止所有服务
docker-compose down

# 停止服务并删除数据卷（注意：会删除数据库数据）
docker-compose down -v

# 重新构建并启动
docker-compose up -d --build

# 重启特定服务
docker-compose restart backend

# 进入容器
docker-compose exec backend bash
docker-compose exec mysql bash

# 查看 MySQL 数据
docker-compose exec mysql mysql -uroot -pitcast itcaststore
```

## 数据库配置

### 默认配置
- **数据库名**: itcaststore
- **用户名**: root
- **密码**: itcast
- **端口**: 3306

### 修改配置
如需修改数据库配置，请编辑以下文件：
1. `docker/docker-compose.yml` - 修改 MySQL 环境变量
2. `docker/c3p0-config.xml` - 修改数据库连接配置
3. 重新构建并启动服务

## 数据持久化

项目使用 Docker 数据卷持久化以下数据：
- **mysql_data**: MySQL 数据库数据
- **product_img_data**: 上传的商品图片

数据卷在 `docker-compose down` 时不会自动删除，确保数据安全。

## 故障排查

### 1. 后端无法连接数据库
- 检查 MySQL 服务是否正常启动: `docker-compose ps`
- 查看 MySQL 日志: `docker-compose logs mysql`
- 确认网络连接: `docker-compose exec backend ping mysql`

### 2. 端口冲突
如果 80、8080 或 3306 端口被占用，请在 `docker-compose.yml` 中修改端口映射：
```yaml
ports:
  - "新端口:容器端口"
```

### 3. 应用启动失败
- 检查构建日志: `docker-compose build --no-cache backend`
- 查看容器日志: `docker-compose logs backend`
- 检查文件权限和路径

## 开发环境配置

### 热部署
开发时可以使用卷挂载实现代码热更新：
```yaml
volumes:
  - ../itcaststore/WebContent:/usr/local/tomcat/webapps/ROOT:ro
  - ../itcaststore/build/classes:/usr/local/tomcat/webapps/ROOT/WEB-INF/classes:ro
```

### 调试
如需远程调试，在 `docker-compose.yml` 中添加：
```yaml
environment:
  - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
ports:
  - "5005:5005"
```

## 生产环境建议

1. **安全性**
   - 修改默认密码
   - 配置 SSL/TLS
   - 使用 secrets 管理敏感信息

2. **性能优化**
   - 配置 Nginx 缓存策略
   - 优化 MySQL 配置
   - 使用 Redis 缓存（如需要）

3. **监控**
   - 添加日志收集（如 ELK）
   - 配置健康检查
   - 使用监控工具（如 Prometheus）

4. **备份**
   - 定期备份数据库数据卷
   - 备份上传文件数据卷

## 技术支持

如有问题，请查看：
- 项目文档: `plan.md`
- Docker 日志: `docker-compose logs`
- GitHub Issues
