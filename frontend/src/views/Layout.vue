<template>
  <div class="layout-container">
    <div class="aside">
      <h3 class="system-title">云聘 · 校园招聘平台</h3>

      <ul class="menu">
        <li :class="{ active: route.path === '/dashboard/home' }" @click="$router.push('/dashboard/home')">
          <span>🏠 仪表盘</span>
        </li>

        <li :class="{ active: route.path === '/dashboard/job-hall' }" @click="$router.push('/dashboard/job-hall')">
          <span>💼 职位大厅</span>
        </li>

        <li v-if="userRole === 'STUDENT'" :class="{ active: route.path === '/dashboard/profile' }" @click="$router.push('/dashboard/profile')">
          <span>👤 个人中心</span>
        </li>

        <li v-if="userRole === 'ADMIN' || userRole === 'COMPANY'" :class="{ active: route.path === '/dashboard/company' }" @click="$router.push('/dashboard/company')">
          <span>🏢 {{ userRole === 'ADMIN' ? '公司管理' : '企业信息' }}</span>
        </li>

        <li v-if="userRole === 'ADMIN'" :class="{ active: route.path === '/dashboard/users' }" @click="$router.push('/dashboard/users')">
          <span>👥 用户管理</span>
        </li>

        <li v-if="userRole !== 'ADMIN'" :class="{ active: route.path === '/dashboard/application' }" @click="$router.push('/dashboard/application')">
          <span>📄 {{ userRole === 'STUDENT' ? '申请记录' : '简历审核' }}</span>
        </li>

        <li v-if="userRole === 'ADMIN'" :class="{ active: route.path === '/dashboard/application' }" @click="$router.push('/dashboard/application')">
          <span>👮‍♂️ 申请管理</span>
        </li>

      </ul>
    </div>

    <div class="main-content">
      <div class="header">
        <div class="breadcrumb">当前位置：{{ route.meta.name || '系统页面' }}</div>
        <div class="user-info">
          <span>欢迎回来，{{ username }}</span>
          <el-button type="danger" link size="small" @click="logout" style="margin-left: 15px;">退出登录</el-button>
        </div>
      </div>

      <div class="page-view">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const userRole = localStorage.getItem('userRole')
const username = localStorage.getItem('username') || '用户'

const logout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.layout-container { display: flex; height: 100vh; width: 100%; }
.aside { width: 220px; background-color: #001529; color: white; display: flex; flex-direction: column; box-shadow: 2px 0 6px rgba(0,21,41,.35); }
.system-title { height: 60px; line-height: 60px; text-align: center; font-size: 18px; font-weight: bold; background-color: #002140; margin: 0; letter-spacing: 1px; }
.menu { list-style: none; padding: 10px 0; margin: 0; }
.menu li { padding: 15px 25px; cursor: pointer; transition: all 0.3s; color: #a6adb4; display: flex; align-items: center; }
.menu li:hover { color: white; background-color: #1890ff; }
.menu li.active { background-color: #1890ff; color: white; border-right: 4px solid #40a9ff; }
.menu li span { margin-left: 8px; font-size: 15px; }

.main-content { flex: 1; display: flex; flex-direction: column; background-color: #f0f2f5; }
.header { height: 60px; background-color: #fff; border-bottom: 1px solid #ddd; display: flex; justify-content: space-between; align-items: center; padding: 0 20px; box-shadow: 0 1px 4px rgba(0,21,41,.08); }
.breadcrumb { font-size: 14px; color: #666; font-weight: 500; }
.page-view { padding: 20px; flex: 1; overflow-y: auto; }
</style>