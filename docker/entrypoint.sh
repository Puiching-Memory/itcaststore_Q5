#!/bin/bash
set -e

echo "=== 启动 ItCast Store 后端应用 ==="

# 等待 MySQL 服务可用
echo "等待 MySQL 服务启动..."
max_attempts=60
attempt=0
while ! nc -z mysql 3306; do
  attempt=$((attempt + 1))
  if [ $attempt -ge $max_attempts ]; then
    echo "错误: MySQL 服务在 60 秒内未启动"
    exit 1
  fi
  echo "尝试连接 MySQL... ($attempt/$max_attempts)"
  sleep 1
done

echo "MySQL 服务已就绪"

# 复制 Docker 专用的数据库配置文件
if [ -f /docker/c3p0-config.xml ]; then
    mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes
    cp /docker/c3p0-config.xml /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/c3p0-config.xml
    echo "已更新数据库配置文件"
else
    echo "警告: 未找到数据库配置文件 /docker/c3p0-config.xml"
fi

# 设置启动脚本权限
chmod +x /usr/local/bin/entrypoint.sh 2>/dev/null || true

# 执行原始的 CMD
echo "启动 Tomcat..."
exec "$@"
