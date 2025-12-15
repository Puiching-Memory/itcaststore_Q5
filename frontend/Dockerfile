FROM node:20-alpine AS build
WORKDIR /app
# 复制依赖文件
COPY package.json package-lock.json* ./
# 安装依赖
RUN npm ci --only=production=false || npm install
# 复制源代码
COPY . .
# 构建应用
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
