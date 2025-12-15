<template>
  <div class="dashboard-container">
    <div class="welcome-section">
      <h2>👋 欢迎回来，{{ username }} ({{ roleName }})</h2>
      <p class="subtitle">今天是 {{ currentDate }}，祝你今天事事顺心！</p>
    </div>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover" class="data-card card-blue">
          <div class="card-header">
            <span>💼 职位总数</span>
            <el-icon><Briefcase /></el-icon>
          </div>
          <div class="card-value">{{ stats.jobsCount }}</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="data-card card-green">
          <div class="card-header">
            <span v-if="userRole === 'STUDENT'">📤 我已投递</span>
            <span v-else>🏢 入驻公司</span>
            <el-icon><School /></el-icon>
          </div>
          <div class="card-value">
            {{ userRole === 'STUDENT' ? stats.myApplyCount : stats.companiesCount }}
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="data-card card-orange">
          <div class="card-header">
            <span v-if="userRole === 'STUDENT'">📄 申请记录</span>
            <span v-else>📄 收到简历</span>
            <el-icon><Document /></el-icon>
          </div>
          <div class="card-value">{{ stats.applicationsCount }}</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="data-card card-red" @click="$router.push('/dashboard/application')" style="cursor: pointer">
          <div class="card-header">
            <span>{{ userRole === 'STUDENT' ? '⏳ 待面试' : '⏳ 待审核' }}</span>
            <el-icon><Bell /></el-icon>
          </div>
          <div class="card-value">{{ stats.pendingCount }}</div>
          <div class="card-footer">点击查看 >></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px;" header="🚀 快捷操作">
      <div class="quick-actions">

        <el-button type="primary" size="large" @click="$router.push('/dashboard/job-hall')" plain>
          {{ userRole === 'STUDENT' ? '去投简历' : '发布职位' }}
        </el-button>

        <el-button v-if="userRole === 'ADMIN'" type="success" size="large" @click="$router.push('/dashboard/company')" plain>
          录入新公司
        </el-button>

        <el-button v-if="userRole === 'STUDENT'" type="warning" size="large" @click="$router.push('/dashboard/profile')" plain>
          完善简历
        </el-button>

        <el-button v-if="userRole !== 'STUDENT'" type="danger" size="large" @click="$router.push('/dashboard/application')" plain>
          审核简历
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import request from '../utils/request'
import { Briefcase, School, Document, Bell } from '@element-plus/icons-vue'

const currentDate = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })

// 获取本地存储的用户信息
const userRole = localStorage.getItem('userRole') || 'STUDENT'
const username = localStorage.getItem('username') || 'User'
const userId = localStorage.getItem('userId')

const roleName = computed(() => {
  if (userRole === 'STUDENT') return '学生'
  if (userRole === 'COMPANY') return '企业'
  return '管理员'
})

const stats = reactive({
  jobsCount: 0,
  companiesCount: 0,
  applicationsCount: 0,
  pendingCount: 0,
  myApplyCount: 0
})

const loadDashboardData = async () => {
  try {
    // 🔥 修复：去掉 /api 前缀
    const jobRes = await request.get('/jobs')
    if (jobRes.code == 200) stats.jobsCount = jobRes.data.length

    if (userRole !== 'STUDENT') {
      // 🔥 修复：去掉 /api 前缀
      const compRes = await request.get('/companies')
      if (compRes.code == 200) stats.companiesCount = compRes.data.length
    }

    // 🔥 修复：去掉 /api 前缀
    const appRes = await request.get('/applications', {
      params: { userId: userId, role: userRole }
    })

    if (appRes.code == 200) {
      const list = appRes.data
      stats.applicationsCount = list.length

      if (userRole === 'STUDENT') {
        stats.myApplyCount = list.length
        stats.pendingCount = list.filter(item => item.status === 'ACCEPTED').length
      } else {
        stats.pendingCount = list.filter(item => item.status === 'PENDING').length
      }
    }

  } catch (error) {
    console.error('加载失败', error)
  }
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.dashboard-container { padding: 20px; }
.welcome-section h2 { margin-bottom: 5px; color: #303133; }
.subtitle { color: #909399; font-size: 14px; }
.data-card { color: white; border: none; transition: transform 0.3s; cursor: default; }
.data-card:hover { transform: translateY(-5px); }
.card-header { display: flex; justify-content: space-between; align-items: center; font-size: 16px; opacity: 0.9; }
.card-value { font-size: 36px; font-weight: bold; margin-top: 10px; }
.card-footer { margin-top: 10px; font-size: 12px; opacity: 0.8; text-align: right; }
.card-blue { background: linear-gradient(135deg, #409EFF, #79bbff); }
.card-green { background: linear-gradient(135deg, #67C23A, #95d475); }
.card-orange { background: linear-gradient(135deg, #E6A23C, #f3d19e); }
.card-red { background: linear-gradient(135deg, #F56C6C, #fab6b6); }
.quick-actions { display: flex; gap: 20px; }
</style>