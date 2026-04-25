# Smart Cinema 智能影院购票系统 - 部署文档

## 项目简介

Smart Cinema 是一个基于 Spring Boot + Vue 3 的智能影院购票系统，集成了 AI 智能助手功能，支持电影浏览、影院查询、在线选座购票等功能。

## 技术栈

### 后端
- Java 8+
- Spring Boot 2.7.3
- MyBatis
- MySQL 8.0
- Redis
- 阿里云 OSS
- LangChain4j (AI 智能助手)

### 前端
- Vue 3
- Vite 5
- Element Plus
- Pinia
- Vue Router

## 环境要求

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 8+ | 推荐 JDK 8 或 JDK 11 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存服务 |
| Node.js | 16+ | 前端构建 |
| Maven | 3.6+ | 项目构建 |

## 快速开始

### 1. 克隆项目

### 2. 数据库配置

#### 2.1 创建数据库

```sql
CREATE DATABASE cinema_ticketing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2.2 导入数据库脚本

项目中的实体类会自动映射数据库表，请根据实体类创建相应的数据库表。主要表结构包括：

- `employee` - 员工表
- `user` - 用户表
- `movie` - 电影表
- `cinema` - 影院表
- `hall` - 影厅表
- `schedule` - 排片表
- `seat` - 座位表
- `ticket` - 订单表
- `ticket_detail` - 订单详情表
- `category` - 分类表
- `rating` - 评分表

### 3. 后端配置

#### 3.1 配置环境变量

**Windows (PowerShell):**
```powershell
# 数据库配置
$env:MYSQL_HOST = "localhost"
$env:MYSQL_PORT = "3306"
$env:MYSQL_DATABASE = "cinema_ticketing"
$env:MYSQL_USERNAME = "root"
$env:MYSQL_PASSWORD = "your_mysql_password"

# Redis配置
$env:REDIS_HOST = "localhost"
$env:REDIS_PORT = "6379"
$env:REDIS_PASSWORD = "your_redis_password"
$env:REDIS_DATABASE = "1"

# 阿里云OSS配置
$env:ALIYUN_OSS_ENDPOINT = "oss-cn-your-region.aliyuncs.com"
$env:ALIYUN_ACCESS_KEY_ID = "your_access_key_id"
$env:ALIYUN_ACCESS_KEY_SECRET = "your_access_key_secret"
$env:ALIYUN_BUCKET_NAME = "your_bucket_name"

# JWT配置
$env:JWT_ADMIN_SECRET_KEY = "your-admin-secret-key"
$env:JWT_USER_SECRET_KEY = "your-user-secret-key"

# AI配置（通义千问）
$env:API_KEY = "your_dashscope_api_key"
```

**Linux/macOS:**
```bash
# 数据库配置
export MYSQL_HOST="localhost"
export MYSQL_PORT="3306"
export MYSQL_DATABASE="cinema_ticketing"
export MYSQL_USERNAME="root"
export MYSQL_PASSWORD="your_mysql_password"

# Redis配置
export REDIS_HOST="localhost"
export REDIS_PORT="6379"
export REDIS_PASSWORD="your_redis_password"
export REDIS_DATABASE="1"

# 阿里云OSS配置
export ALIYUN_OSS_ENDPOINT="oss-cn-your-region.aliyuncs.com"
export ALIYUN_ACCESS_KEY_ID="your_access_key_id"
export ALIYUN_ACCESS_KEY_SECRET="your_access_key_secret"
export ALIYUN_BUCKET_NAME="your_bucket_name"

# JWT配置
export JWT_ADMIN_SECRET_KEY="your-admin-secret-key"
export JWT_USER_SECRET_KEY="your-user-secret-key"

# AI配置（通义千问）
export API_KEY="your_dashscope_api_key"
```

#### 3.2 使用配置文件（可选）

如果不使用环境变量，可以复制配置示例文件并修改：

```bash
cd Cinema-server/src/main/resources
cp application-dev-example.yml application-dev.yml
```

然后编辑 `application-dev.yml` 文件，填入你的实际配置信息。

#### 3.3 构建并运行后端

```bash
# 进入项目根目录
cd smart-cinema

# Maven构建
mvn clean install -DskipTests

# 进入server模块
cd Cinema-server

# 运行项目
mvn spring-boot:run
```

后端服务将在 `http://localhost:8084` 启动。

### 4. 前端配置与运行

#### 4.1 安装依赖

```bash
cd frontend
npm install
```

#### 4.2 开发模式运行

```bash
npm run dev
```

前端开发服务器将在 `http://localhost:3031` 启动。

#### 4.3 生产环境构建

```bash
npm run build
```

构建产物将输出到 `nginx-1.20.2/html/cinema` 目录（可根据 `vite.config.js` 修改输出路径）。

## 配置说明

### 阿里云 OSS 配置

1. 登录 [阿里云控制台](https://oss.console.aliyun.com/)
2. 创建 OSS Bucket
3. 获取 AccessKey ID 和 AccessKey Secret
4. 配置跨域规则（CORS）允许前端访问

### 通义千问 AI 配置

1. 访问 [阿里云百炼平台](https://dashscope.console.aliyun.com/)
2. 开通 DashScope 服务
3. 创建 API Key
4. 设置环境变量 `API_KEY`

## 项目结构

```
smart-cinema/
├── Cinema-common/          # 公共模块
│   └── src/main/java/com/cinema/
│       ├── constant/       # 常量定义
│       ├── context/        # 上下文
│       ├── enumeration/    # 枚举类
│       ├── exception/      # 异常类
│       ├── properties/     # 配置属性类
│       ├── result/         # 统一返回结果
│       └── utils/          # 工具类
├── Cinema-pojo/            # 实体模块
│   └── src/main/java/com/cinema/
│       ├── dto/            # 数据传输对象
│       ├── entity/         # 实体类
│       └── vo/             # 视图对象
├── Cinema-server/          # 服务模块
│   └── src/main/
│       ├── java/com/cinema/
│       │   ├── config/     # 配置类
│       │   ├── controller/ # 控制器
│       │   ├── mapper/     # MyBatis Mapper
│       │   ├── service/    # 服务层
│       │   └── handler/    # 全局异常处理
│       └── resources/
│           ├── mapper/     # MyBatis XML
│           ├── application.yml
│           └── application-dev.yml
├── frontend/               # 前端项目
│   ├── src/
│   │   ├── router/         # 路由配置
│   │   ├── utils/          # 工具函数
│   │   └── views/          # 页面组件
│   ├── package.json
│   └── vite.config.js
└── pom.xml                 # 父POM
```

## API 文档

启动后端服务后，访问 Swagger 文档：
- Knife4j 文档：`http://localhost:8084/doc.html`

## 常见问题

### 1. 数据库连接失败

- 检查 MySQL 服务是否启动
- 确认数据库用户名密码正确
- 检查数据库是否存在
- 确认 MySQL 连接驱动版本

### 2. Redis 连接失败

- 检查 Redis 服务是否启动
- 确认 Redis 密码配置正确
- 检查端口是否被占用

### 3. OSS 上传失败

- 检查 AccessKey 是否正确
- 确认 Bucket 存在且有写入权限
- 检查 OSS 跨域配置

### 4. AI 功能无响应

- 确认已设置 `API_KEY` 环境变量
- 检查通义千问服务是否已开通
- 确认 API Key 是否有效且有配额

### 5. 前端代理不生效

- 检查后端服务是否在 8084 端口运行
- 确认 `vite.config.js` 中的代理配置正确

## 生产环境部署建议

### 1. 后端部署

```bash
# 打包
mvn clean package -DskipTests

# 运行
java -jar Cinema-server/target/Cinema-server-1.0-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8084
```

### 2. 使用 Systemd 管理服务（Linux）

创建服务文件 `/etc/systemd/system/cinema.service`:

```ini
[Unit]
Description=Smart Cinema Service
After=network.target

[Service]
Type=simple
User=your-user
WorkingDirectory=/path/to/smart-cinema
Environment="MYSQL_PASSWORD=your_password"
Environment="REDIS_PASSWORD=your_password"
Environment="ALIYUN_ACCESS_KEY_ID=your_key"
Environment="ALIYUN_ACCESS_KEY_SECRET=your_secret"
Environment="API_KEY=your_api_key"
ExecStart=/usr/bin/java -jar Cinema-server/target/Cinema-server-1.0-SNAPSHOT.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

启动服务：
```bash
sudo systemctl daemon-reload
sudo systemctl start cinema
sudo systemctl enable cinema
```

### 3. Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态资源
    location / {
        root /path/to/nginx/html/cinema;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api {
        proxy_pass http://localhost:8084;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

