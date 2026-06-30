# 校园商城平台 (Campus Mall)

基于 **Java 11 + Spring Boot 2.7 + Vue 3** 的校园商城系统，实现设计文档中的最小可运行闭环：

> 注册登录 → 浏览商品 → 加入购物车 → 创建订单 → 模拟支付 → 商户发货 → 确认收货 → 评价

## 项目结构

```
campus-mall/
├── common/                 # 公共模块（统一响应、异常、JWT）
├── mall-service/           # 核心业务服务（8081）
├── gateway/                # API 网关（8080）
├── frontend/               # Vue3 前端（5173）
├── sql/init.sql            # 数据库初始化脚本
└── docker/docker-compose.yml
```

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7、MyBatis-Plus、JWT、Redis |
| 网关 | Spring Cloud Gateway |
| 前端 | Vue 3、Vite、Element Plus、Pinia、Axios |
| 数据库 | MySQL 8、Redis 7 |

## 快速启动

### 1. 启动基础设施

```bash
cd docker
docker-compose up -d
```

### 2. 启动后端

```bash
# 编译
mvn clean package -DskipTests

# 启动业务服务
java -jar mall-service/target/mall-service-1.0.0.jar

# 启动网关（新终端）
java -jar gateway/target/gateway-1.0.0.jar
```

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问：http://localhost:5173

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| student1 | 123456 | 学生 |
| merchant1 | 123456 | 商户 |
| admin | 123456 | 管理员 |

## 核心 API

| 模块 | 路径 |
|------|------|
| 认证 | `/api/auth/login` |
| 商品 | `/api/products` |
| 搜索 | `/api/search` |
| 购物车 | `/api/cart` |
| 订单 | `/api/orders` |
| 支付 | `/api/payments` |
| 评价 | `/api/reviews` |
| 统计 | `/api/analytics` |

## 业务流程

1. **学生**：浏览商品 → 加购 → 下单 → 模拟支付 → 确认收货 → 评价
2. **商户**：发布商品 → 处理待发货订单 → 查看销售统计
3. **管理员**：审核商品 → 用户管理 → 订单监控 → 查看报表

## 配置说明

- MySQL：`localhost:3306/campus_mall`，用户名/密码 `root/root`
- Redis：`localhost:6379`
- 网关端口：`8080`，业务服务端口：`8081`

如需修改，编辑 `mall-service/src/main/resources/application.yml`。
