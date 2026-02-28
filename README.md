# 🚀 云聘 · 直通车 (YunPin) — 校园级全栈招聘管理平台

<div align="center">
  <img src="https://img.shields.io/badge/Release-v1.0.0-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Framework-Vue_3.x-4fc08d?style=for-the-badge&logo=vue.js" />
  <img src="https://img.shields.io/badge/Backend-SpringBoot_3.x-6db33f?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" />
</div>

---

## 📖 项目深度背景 | Project Overview

在当今数字化校园求职环境下，传统的招聘方式效率低下。**云聘 · 直通车** 致力于打造一个**高颜值、高性能、全闭环**的校招对接平台。
本项目通过现代化的 UI 设计语言和严谨的后端架构，实现了从**职位精准推送**到**简历在线渲染**，再到**企业审核入职**的全流程数字化管理。

---

## ✨ 核心技术亮点 | Technical Highlights

### 1. 🎨 沉浸式 UI/UX 设计
- **动态背景引擎**：登录与注册页面采用自研 CSS 线性流光动画，配合 **Backdrop Filter** 磨砂玻璃特效，提供极佳的第一眼视觉冲击力。
- **响应式交互布局**：基于 Element Plus 的栅格系统，完美适配从笔记本到 4K 显示器的不同分辨率。
- **平滑过场动画**：路由切换采用 `fade-transform` 动效，告别生硬的页面跳转，提升系统操控手感。

### 2. 📊 高级数据可视化 (ECharts 5)
- **行业分析模型**：后端聚合职位行业属性，前端通过环形图实时呈现市场热度分布。
- **动态趋势分析**：通过折线面积图展示平台近七日的投递量与职位增长量，数据驱动运营。

### 3. 📝 简历实时渲染引擎 (A4 Standard)
- **所见即所得**：采用双栏布局，左侧录入个人核心信息，右侧同步渲染符合 A4 纸张标准的专业简历，支持预览与格式控制。

---

## 📸 视觉画廊与功能详解 | Feature Gallery

### 🔐 01. 智能门户与鉴权 (Portal & Auth)
<div align="center">
  <img src="screenshots/login.png" width="800" alt="登录页面" />
  <p><i>（建议截图重点：展示带有流光背景和磨砂玻璃卡片的登录框）</i></p>
</div>

- **多角色验证**：支持学生、企业、管理员三重角色一键切换登录。
- **安全拦截**：集成 JWT Token 验证机制，对所有受保护路由进行全量守卫。

### 🏠 02. 管理员驾驶舱 (Admin Dashboard)
<div align="center">
  <img src="screenshots/home.png" width="800" alt="数据仪表盘" />
  <p><i>（建议截图重点：展示顶部的渐变色统计卡片和底部的 ECharts 图表区）</i></p>
</div>

- **全量指标统计**：实时统计注册人数、入驻企业、在招职位及累计投递量。
- **运营趋势监控**：直观展示平台数据增长曲线。

### 💼 03. 职位大厅与智能搜索 (Job Hall)
<div align="center">
  <img src="screenshots/jobhall.png" width="800" alt="职位列表" />
  <p><i>（建议截图重点：展示带有“急招”、“校园招聘”标签的悬浮特效职位卡片）</i></p>
</div>

- **卡片式瀑布流**：精致的职位卡片设计，支持悬浮阴影特效。
- **智能标签系统**：自动识别职位状态（已投递变灰、已收藏高亮）。
- **实时搜索**：支持公司名、职位名关键字秒级响应搜索。

### 📄 04. 简历编辑与 A4 预览 (Resume Center)
<div align="center">
  <img src="screenshots/profile.png" width="800" alt="简历编辑" />
  <p><i>（建议截图重点：展示左侧编辑表单与右侧专业的蓝色调简历预览区）</i></p>
</div>

- **简历全字段维护**：涵盖基础信息、教育背景、专业技能、自我评价。
- **动态头像上传**：集成文件上传服务，支持个人形象照即时预览。

### 🏢 05. 企业招聘管理 (Corporate Management)
<div align="center">
  <img src="screenshots/company.png" width="800" alt="企业中心" />
  <p><i>（建议截图重点：展示企业发布职位时的富文本编辑器页面）</i></p>
</div>

- **富文本发布**：内置 Quill 编辑器，支持加粗、列表、图片等复杂排版。
- **申请流程审批**：企业可实时查阅投递列表，并进行录用或拒绝操作。

---

## 🛠️ 后端架构与功能目录

### 📦 核心实体模型
- **User (用户)**: 统一身份标识，支持权限分级。
- **Job (职位)**: 存储薪资、要求、发布时间等元数据。
- **Student (学生)**: 存储详细简历与求职意向。
- **Company (公司)**: 存储企业背景、行业认证与 Logo。
- **Application (申请)**: 记录投递全生命周期状态（PENDING/APPROVED/REJECTED）。

### 📂 核心目录架构
```text
MyJobSystem/
├── employment-system-backend/   # SpringBoot 3 后端核心
│   ├── controller/              # RESTful API 路由入口
│   ├── entity/                  # 数据库对象映射模型
│   ├── mapper/                  # MyBatis 动态 SQL 映射
│   └── service/                 # 核心业务逻辑处理层
├── frontend/                    # Vue 3 前端工程
│   ├── src/views/               # 核心 UI 视图组件
│   ├── src/router/              # 动态路由与导航守卫
│   ├── src/utils/               # Axios 拦截器与工具函数
│   └── src/style/               # 全局样式与公共 CSS
└── LICENSE                      # MIT 许可证
```

---

## 🚀 极简部署方案 | Deployment

### 1. 数据库环境
```sql
CREATE DATABASE yunpin CHARACTER SET utf8mb4;
-- 导入 project_schema.sql (若无脚本，请根据实体类生成)
```

### 2. 后端服务 (Port: 8080)
1. 确认 `application.properties` 中的 DB 连接正确。
2. 运行 `mvn clean spring-boot:run`。

### 3. 前端服务 (Port: 5173)
```bash
cd frontend
npm install
npm run dev
```

---

## 🤝 贡献与反馈
- **提交 Bug**: 请前往 [Issues](https://github.com/YfengJ/Yunpin/issues) 页面。
- **开源协议**: 采用 **MIT License**，欢迎自由 Fork 与交流。

---
<div align="center">
  <p>© 2026 <b>YfengJ</b>. 为每一位求职者点亮指路明灯。</p>
</div>
