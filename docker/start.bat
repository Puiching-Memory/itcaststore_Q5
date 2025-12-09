@echo off
REM Docker 快速启动脚本（Windows）

echo ==========================================
echo   ItCast Store Docker 部署脚本
echo ==========================================

REM 检查 Docker 是否安装
docker --version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到 Docker，请先安装 Docker Desktop
    pause
    exit /b 1
)

REM 检查 Docker Compose 是否安装
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到 Docker Compose，请先安装 Docker Compose
    pause
    exit /b 1
)

echo.
echo 1. 检查 Docker 服务状态...
docker info >nul 2>&1
if errorlevel 1 (
    echo 错误: Docker 服务未运行，请启动 Docker Desktop
    pause
    exit /b 1
)
echo    ✓ Docker 服务运行正常

echo.
echo 2. 构建并启动所有服务...
docker-compose up -d --build

echo.
echo 3. 等待服务启动...
timeout /t 10 /nobreak >nul

echo.
echo 4. 检查服务状态...
docker-compose ps

echo.
echo ==========================================
echo   部署完成！
echo ==========================================
echo.
echo 访问地址：
echo   - 前端（通过 Nginx）: http://localhost
echo   - 后端（直接访问）: http://localhost:8080
echo   - MySQL: localhost:3306
echo.
echo 常用命令：
echo   - 查看日志: docker-compose logs -f
echo   - 停止服务: docker-compose down
echo   - 重启服务: docker-compose restart
echo.
pause
