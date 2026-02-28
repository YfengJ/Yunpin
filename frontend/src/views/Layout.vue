<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '240px'" class="aside">
      <div class="logo-container">
        <div class="logo-icon">🚀</div>
        <h3 v-show="!isCollapse" class="system-title">云聘 · 直通车</h3>
      </div>

      <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          :collapse="isCollapse"
          background-color="#001529"
          text-color="#a6adb4"
          active-text-color="#ffffff"
          router
      >
        <el-menu-item index="/dashboard/home">
          <el-icon><DataBoard /></el-icon>
          <template #title>仪表盘</template>
        </el-menu-item>

        <el-menu-item index="/dashboard/job-hall">
          <el-icon><Briefcase /></el-icon>
          <template #title>职位大厅</template>
        </el-menu-item>

        <el-menu-item v-if="userRole === 'STUDENT'" index="/dashboard/my-favorites">
          <el-icon><Star /></el-icon>
          <template #title>我的收藏</template>
        </el-menu-item>

        <el-menu-item v-if="userRole === 'STUDENT'" index="/dashboard/profile">
          <el-icon><User /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>

        <el-menu-item v-if="userRole === 'ADMIN' || userRole === 'COMPANY'" index="/dashboard/company">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>{{ userRole === 'ADMIN' ? '公司管理' : '企业信息' }}</template>
        </el-menu-item>

        <el-menu-item v-if="userRole === 'ADMIN'" index="/dashboard/users">
          <el-icon><Avatar /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>

        <el-menu-item v-if="userRole !== 'ADMIN'" index="/dashboard/application">
          <el-icon><Document /></el-icon>
          <template #title>{{ userRole === 'STUDENT' ? '申请记录' : '简历审核' }}</template>
        </el-menu-item>

        <el-menu-item v-if="userRole === 'ADMIN'" index="/dashboard/application">
          <el-icon><Management /></el-icon>
          <template #title>申请管理</template>
        </el-menu-item>
      </el-menu>

      <div class="aside-footer" @click="isCollapse = !isCollapse">
        <el-icon>
          <component :is="isCollapse ? 'Expand' : 'Fold'" />
        </el-icon>
      </div>
    </el-aside>

    <el-container class="main-content">
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ route.meta.name || '系统页面' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <div class="user-profile">
            <el-avatar :size="32" class="user-avatar">{{ username.charAt(0).toUpperCase() }}</el-avatar>
            <span class="username">欢迎回来，{{ username }}</span>
          </div>
          <el-divider direction="vertical" />
          <el-button type="danger" link @click="logout" class="logout-btn">
            <el-icon><SwitchButton /></el-icon>
            <span>退出</span>
          </el-button>
        </div>
      </el-header>

      <el-main class="page-view">
        <div class="view-wrapper">
          <router-view v-slot="{ Component }">
            <transition name="fade-transform" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  DataBoard, Briefcase, Star, User, OfficeBuilding,
  Avatar, Document, Management, Expand, Fold,
  SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const userRole = localStorage.getItem('userRole')
const username = localStorage.getItem('username') || '用户'
const isCollapse = ref(false)

const activeMenu = computed(() => route.path)

const logout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100vw;
  background-color: #f0f2f5;
}

.aside {
  background-color: #001529;
  transition: width 0.3s;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 21, 41, 0.15);
  z-index: 100;
}

.logo-container {
  height: 64px;
  display: flex;
  align-items: center;
  padding-left: 20px;
  background-color: #002140;
  overflow: hidden;
  white-space: nowrap;
}

.logo-icon {
  font-size: 24px;
  margin-right: 12px;
}

.system-title {
  color: white;
  font-size: 18px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 1px;
}

.el-menu-vertical {
  border-right: none;
  flex: 1;
}

.el-menu-vertical :deep(.el-menu-item.is-active) {
  background-color: #1890ff !important;
}

.aside-footer {
  height: 48px;
  line-height: 48px;
  text-align: center;
  color: #fff;
  background-color: #002140;
  cursor: pointer;
  transition: all 0.3s;
}

.aside-footer:hover {
  background-color: #1890ff;
}

.header {
  height: 64px;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 99;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-profile {
  display: flex;
  align-items: center;
}

.user-avatar {
  background-color: #1890ff;
  margin-right: 12px;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.logout-btn {
  margin-left: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.page-view {
  padding: 24px;
  background-color: #f0f2f5;
  overflow-y: auto;
}

.view-wrapper {
  max-width: 1400px;
  margin: 0 auto;
}

/* 页面切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>