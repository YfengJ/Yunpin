# 🚀 云聘 · 直通车 (YunPin)

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vue 3](https://img.shields.io/badge/Vue.js-3.x-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element_Plus-2.x-409EFF?style=for-the-badge&logo=element-plus&logoColor=white)](https://element-plus.org/)
[![Vite](https://img.shields.io/badge/Vite-5.x-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)

**云聘 · 直通车** 是一款专为校园设计的现代化招聘管理系统。它连接了企业、学生与管理员，提供从职位发布、简历投递到入职跟踪的一站式闭环体验。

[查看在线演示 (示例)](https://github.com/YfengJ/Yunpin) · [报告 Bug](https://github.com/YfengJ/Yunpin/issues) · [提出新特性](https://github.com/YfengJ/Yunpin/issues)

---

## ✨ 核心特性

- **🚀 现代化交互**：基于 Vue 3 + Element Plus 构建，支持响应式布局与极速页面切换。
- **📊 数据可视化**：集成 ECharts 5，实时展示行业职位占比与平台运营趋势。
- **📝 富文本编辑**：内置 VueQuill，支持职位描述的图文混合编辑。
- **📄 简历即所得**：学生端支持在线编辑并实时预览 A4 级标准的简历。
- **🔒 安全可靠**：前后端分离架构，基于 Token 的安全验证机制。

## 📸 界面预览

> **提示**：建议在运行项目后，将新界面的截图替换至 `screenshots/` 目录。

### 🏠 智能仪表盘
<img width="1264" height="665" alt="home" src="https://github.com/user-attachments/assets/816f0afb-e2d2-47f2-8f72-1e3e91ca868c" />
*多维度展示系统统计数据，核心指标一目了然。*

### 💼 职位大厅
<img width="1279" height="658" alt="jobhall" src="https://github.com/user-attachments/assets/b171b6b8-f9c7-45b1-b981-46ed33ea3b47" />
*精美的卡片式设计，支持收藏、一键投递与智能搜索。*

### 👤 简历中心
<img width="1276" height="665" alt="profile" src="https://github.com/user-attachments/assets/1957a091-a461-4302-97cd-5612305ec2dd" />
*左侧编辑，右侧实时渲染 A4 标准简历，助力高效求职。*

### 🔐 极简登录
<img width="1277" height="672" alt="login" src="https://github.com/user-attachments/assets/9f0104f1-52b3-4161-bd0e-386282388054" />
*毛玻璃特效配合流光背景，提供沉浸式访问体验。*

---

## 🛠️ 架构说明

### 前端 (Frontend)
- **核心框架**：Vue 3 (Composition API)
- **组件库**：Element Plus
- **图表库**：ECharts
- **编辑器**：VueQuill
- **状态/路由**：Vue Router + Axios

### 后端 (Backend)
- **核心框架**：Spring Boot 3
- **持久层**：MyBatis
- **数据库**：MySQL 8.0
- **依赖管理**：Maven

---

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/YfengJ/Yunpin.git
cd Yunpin
```

### 2. 后端配置 (Spring Boot)
1. 进入 `employment-system-backend` 目录。
2. 复制 `src/main/resources/application.properties.example` 为 `application.properties`。
3. 修改数据库连接信息：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/yunpin?serverTimezone=GMT%2B8
   spring.datasource.username=你的用户名
   spring.datasource.password=你的密码
   ```
4. 运行 `EmploymentSystemBackendApplication`。

### 3. 前端启动 (Vue 3)
```bash
cd frontend
npm install
npm run dev
```
访问：`http://localhost:5173`

---

## ⚠️ 注意事项
数据库 SQL 脚本尚未包含在本项目中（如有需要请联系作者）。

## 🤝 参与贡献

我们非常欢迎 Pull Requests！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 Pull Request

---

## 📄 开源协议

本项目基于 MIT 协议开源 - 详情请参阅 [LICENSE](LICENSE) 文件。

© 2026 YfengJ. Built with ❤️ for students.
