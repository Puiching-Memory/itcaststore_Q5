# 后端应用 Dockerfile
# 基于 Tomcat 8.5 镜像构建
FROM tomcat:8.5-jre8

# 维护者信息
MAINTAINER itcaststore

# 安装 netcat 用于检查 MySQL 服务可用性
RUN apt-get update && apt-get install -y netcat-openbsd curl && rm -rf /var/lib/apt/lists/*

# 设置工作目录
WORKDIR /usr/local/tomcat

# 删除 Tomcat 默认的 webapps 内容
RUN rm -rf webapps/*

# 复制应用文件到 Tomcat webapps 目录
COPY WebContent/ webapps/ROOT/
COPY build/classes/ webapps/ROOT/WEB-INF/classes/

# 启动脚本将在 docker-compose 中通过卷挂载或单独复制
# 如果需要，可以在这里复制启动脚本
# COPY docker/entrypoint.sh /usr/local/bin/entrypoint.sh
# RUN chmod +x /usr/local/bin/entrypoint.sh

# 创建数据卷挂载点（用于上传的文件）
VOLUME ["/usr/local/tomcat/webapps/ROOT/productImg"]

# 暴露 Tomcat 端口
EXPOSE 8080

# 启动 Tomcat
# entrypoint.sh 将通过 docker-compose 卷挂载和配置
CMD ["catalina.sh", "run"]
