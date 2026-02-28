# 🚀 云聘 · 直通车 (YunPin) — 校园级全栈招聘管理平台

[![GitHub stars](https://img.shields.io/github/stars/YfengJ/Yunpin?style=flat-square)](https://github.com/YfengJ/Yunpin/stargazers)
[![GitHub license](https://img.shields.io/github/license/YfengJ/Yunpin?style=flat-square)](https://github.com/YfengJ/Yunpin/blob/master/LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.1-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue.js-3.x-35495E?style=flat-square&logo=vuedotjs&logoColor=4FC08D)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-5.x-646CFF?style=flat-square&logo=vite&logoColor=white)](https://vitejs.dev/)

**云聘 · 直通车** 是一款专为校园生态打造的现代化全栈招聘系统。通过对求职、招聘、审核三大环节的数字化重构，为**学生**提供沉浸式求职体验，为**企业**提供高效的人才筛选工具，为**管理员**提供多维度的系统治理手段。

---

## 🌟 核心特性与亮点

- **📊 智慧数据看板**：集成 **ECharts 5**，实时渲染行业职位分布饼图与近 7 日平台活跃趋势线图，辅助决策。
- **📝 专业富文本交互**：基于 **VueQuill** 深度定制职位描述编辑器，支持图文混排，让职位发布更具吸引力。
- **📄 A4 级简历即时预览**：独创“左侧编辑、右侧实时渲染”模式，严格遵循 A4 纸张标准，支持简历预览与下载导出。
- **🔐 严谨的鉴权机制**：基于 **JWT + Axios 拦截器** 的无感认证流程，确保用户数据的访问安全与系统稳健。
- **🚀 现代化交互设计**：采用 **Vue 3 (Composition API)** + **Element Plus**，内置流光背景登录页、毛玻璃特效卡片及流畅的页面平移动画。

---

## 🛠️ 技术图谱

### 前端技术栈 (Frontend)
- **核心框架**：Vue 3.x (Composition API)
- **构建工具**：Vite 5.x (极速冷启动与热更新)
- **UI 组件库**：Element Plus (2.8+ 现代化定制)
- **路由管理**：Vue Router (动态路由与守卫拦截)
- **数据交互**：Axios (Promise 封装 + 全局拦截)
- **图表驱动**：Apache ECharts (大数据量实时渲染)

### 后端技术栈 (Backend)
- **核心框架**：Spring Boot 3.x
- **ORM 框架**：MyBatis (XML 映射 + 动态 SQL)
- **安全机制**：基于 Token 的认证与角色访问控制
- **数据库**：MySQL 8.0+ (高性能关系型数据库)
- **构建工具**：Maven 3.9+

---

## 📋 功能矩阵 (Feature List)

### 👨‍🎓 学生端 (Student Portal)
- **职位探索**：多维搜索（职位/公司名）、实时卡片预览、收藏/取消心仪职位。
- **在线简历**：全字段简历编辑、头像上传、简历实时 A4 预览。
- **投递管理**：一键投递、申请进度实时跟踪、收藏夹管理。

### 🏢 企业端 (Company Portal)
- **职位运营**：职位全生命周期管理（发布、编辑、下线）、富文本描述。
- **简历筛选**：申请记录实时接收、求职者详情查阅、投递状态批量流转。
- **企业名片**：公司 Logo、介绍、行业属性等企业背书信息的自主维护。

### 👮‍♂️ 管理端 (Admin Portal)
- **系统总览**：多维数据统计卡片、全站趋势分析图表。
- **资源治理**：全站用户权限分配、入驻企业审核、所有职位宏观调控。
- **申请监控**：全局投递记录追溯、数据异常波动监控。

---

## 📸 界面预览

### 🏠 智能仪表盘 (Dashboard)
<img width="1264" height="665" alt="home" src="https://github.com/user-attachments/assets/816f0afb-e2d2-47f2-8f72-1e3e91ca868c" />
*核心指标（学生/企业/职位/投递）全量监控。*

### 💼 职位大厅 (Job Hall)
<img width="1279" height="658" alt="jobhall" src="https://github.com/user-attachments/assets/b171b6b8-f9c7-45b1-b981-46ed33ea3b47" />
*标签化设计，状态实时反馈（如：已投递、已收藏）。*

### 👤 简历中心 (Resume Center)
<img width="1276" height="665" alt="profile" src="https://github.com/user-attachments/assets/1957a091-a461-4302-97cd-5612305ec2dd" />
*左侧信息输入，右侧 A4 样式实时预览。*

### 🔐 极简登录 (Auth)
<img width="1277" height="672" alt="login" src="https://github.com/user-attachments/assets/9f0104f1-52b3-4161-bd0e-386282388054" />
*沉浸式视觉背景，支持动态角色验证。*

---

## 📂 目录结构说明

```text
MyJobSystem/
├── employment-system-backend/   # Java Spring Boot 后端源码
│   ├── src/main/java/com/example/employment/
│   │   ├── controller/          # 接口控制器 (Application, Company, Job, etc.)
│   │   ├── entity/              # 数据库实体类
│   │   ├── mapper/              # MyBatis 接口层
│   │   └── service/             # 业务逻辑实现层
│   └── src/main/resources/
│       ├── mapper/              # MyBatis XML 映射文件
│       └── application.properties # 核心全局配置文件 (敏感信息已脱敏)
├── frontend/                    # Vue 3 前端源码
│   ├── src/
│   │   ├── api/                 # Axios 接口模块化封装
│   │   ├── components/          # 公共 UI 组件
│   │   ├── router/              # Vue Router 路由配置
│   │   ├── utils/               # 工具类 (request.js 拦截器)
│   │   └── views/               # 核心页面 (Login, Home, JobHall, etc.)
│   ├── vite.config.js           # Vite 编译与代理配置
│   └── package.json             # 依赖管理清单
├── LICENSE                      # MIT 开源协议
└── README.md                    # 项目核心指南
```

---

## 🚀 部署指引

### 1. 环境准备
- **Java**: JDK 17+
- **Database**: MySQL 8.0+
- **Node.js**: v16+ & npm 8+
- **IDE**: IntelliJ IDEA (推荐) & VS Code

### 2. 数据库初始化
1. 创建名为 `yunpin` 的数据库。
2. 运行项目提供的 SQL 脚本（如需协助请联系作者）。

### 3. 后端启动
1. 复制 `application.properties.example` 为 `application.properties`。
2. 修改 `spring.datasource.url`、`username` 和 `password` 为您的本地环境。
3. 执行：`mvn clean install` 并运行 `EmploymentSystemBackendApplication`。

### 4. 前端启动
```bash
cd frontend
npm install           # 安装项目依赖
npm run dev           # 启动开发服务器
```
访问：`http://localhost:5173`。

---

## ⚠️ 注意事项
- **环境隔离**：本项目通过 `vite.config.js` 配置代理转发，前端访问 `/api` 路径将自动路由至后端 `8080` 端口。
- **敏感信息**：提交至 Git 的配置文件已忽略敏感数据库密码，请务必根据部署指引手动创建。

## 🤝 参与贡献
如果您有任何好的想法，欢迎提交 Pull Request 或通过 Issues 反馈！

## 📄 开源协议
本项目采用 **MIT License**，您可以自由使用、修改并分发。

© 2026 YfengJ. 开源于 GitHub.
