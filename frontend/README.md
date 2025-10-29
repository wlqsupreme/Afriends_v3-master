# Afriends 前端项目

基于 Vue 3 + TypeScript + Vite + Element Plus 的现代化前端管理系统。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - JavaScript 的超集
- **Vite** - 下一代前端构建工具
- **Element Plus** - Vue 3 组件库
- **Pinia** - Vue 状态管理库
- **Vue Router** - Vue 官方路由管理器
- **Axios** - HTTP 客户端

## 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── assets/            # 静态资源
│   ├── components/        # 公共组件
│   ├── pages/            # 页面组件
│   ├── router/           # 路由配置
│   ├── store/            # Pinia 状态管理
│   ├── utils/            # 工具方法
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件
├── index.html            # HTML 模板
├── package.json          # 项目配置
├── tsconfig.json         # TypeScript 配置
├── vite.config.ts        # Vite 配置
└── README.md            # 项目说明
```

## 功能特性

- 🎨 **现代化 UI** - 基于 Element Plus 的美观界面
- 📱 **响应式设计** - 支持各种屏幕尺寸
- 🔧 **TypeScript** - 完整的类型支持
- 🚀 **快速开发** - Vite 提供极速的开发体验
- 📦 **模块化** - 清晰的代码组织结构
- 🔄 **状态管理** - Pinia 提供简洁的状态管理
- 🌐 **路由管理** - Vue Router 提供单页应用路由
- 🔌 **API 集成** - 预配置的 Axios 请求拦截器

## 开发指南

### 环境要求

- Node.js >= 16.0.0
- npm >= 7.0.0

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 页面说明

### 首页 (Home)
- 系统概览
- 功能特性展示
- 统计数据展示

### 表选择器 (TableSelector)
- 数据库表列表展示
- 多选表功能
- 表数据预览
- 数据导出功能

## API 配置

项目已预配置与后端 Spring Boot 的交互：

- **基础 URL**: `http://localhost:8081/api/`
- **代理配置**: 开发环境自动代理 API 请求
- **CORS 支持**: 已配置跨域支持

### 主要 API 接口

- `GET /api/tables` - 获取表列表
- `GET /api/tables/{name}/data` - 获取表数据
- `GET /api/friends` - 获取好友列表
- `POST /api/friends` - 添加好友
- `PUT /api/friends/{id}` - 更新好友
- `DELETE /api/friends/{id}` - 删除好友

## 开发规范

### 代码风格
- 使用 TypeScript 进行类型检查
- 遵循 Vue 3 Composition API 规范
- 使用 ESLint 进行代码检查

### 组件规范
- 组件名使用 PascalCase
- 文件名使用 kebab-case
- 组件内部使用 Composition API

### 状态管理
- 使用 Pinia 进行状态管理
- Store 按功能模块划分
- 使用 TypeScript 定义 Store 类型

## 部署说明

1. 构建生产版本：`npm run build`
2. 将 `dist` 目录部署到 Web 服务器
3. 配置 Nginx 代理 API 请求到后端服务

## 许可证

MIT License
