<template>
  <div class="dashboard-container fade-in">
    <div class="welcome-section">
      <div class="welcome-content">
        <h2 class="welcome-title">👋 {{ greeting }}，{{ username }}</h2>
        <p class="welcome-subtitle">欢迎回到云聘 · 直通车。系统为您汇总了最新的招聘与求职动态。</p>
        <div class="welcome-actions">
          <el-button type="primary" @click="$router.push('/dashboard/job-hall')">浏览职位大厅</el-button>
          <el-button v-if="userRole === 'STUDENT'" plain @click="$router.push('/dashboard/profile')">完善个人简历</el-button>
        </div>
      </div>
      <div class="welcome-illustration">
        <img src="https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png" alt="welcome" />
      </div>
    </div>

    <el-row :gutter="24" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="(item, index) in cards" :key="index">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '0px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrapper" :style="{ background: item.color }">
              <el-icon :size="28" color="#fff"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-data">
              <div class="stat-value">{{ item.value.toLocaleString() }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="trend up">
              <el-icon><CaretTop /></el-icon> 活跃度高
            </span>
            <span class="trend-text">数据实时同步中</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="chart-row">
      <el-col :xs="24" :lg="10">
        <el-card shadow="hover" class="chart-card main-card">
          <template #header>
            <div class="card-header">
              <span class="header-title">📊 热门行业职位占比</span>
              <el-tag size="small" type="success" effect="plain">TOP 5</el-tag>
            </div>
          </template>
          <div ref="pieChartRef" class="chart-box"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="14">
        <el-card shadow="hover" class="chart-card main-card">
          <template #header>
            <div class="card-header">
              <span class="header-title">📈 平台数据增长趋势</span>
              <div class="header-ops">
                <el-tag size="small" effect="dark">近7日</el-tag>
              </div>
            </div>
          </template>
          <div ref="lineChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed, nextTick } from 'vue'
import request from '../utils/request'
import * as echarts from 'echarts'
import {
  User, OfficeBuilding, Suitcase, Document,
  CaretTop
} from '@element-plus/icons-vue'

const currentDate = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
const userRole = localStorage.getItem('userRole') || 'STUDENT'
const username = localStorage.getItem('username') || 'User'

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 9) return '早安'
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const cards = ref([
  { label: '注册学生', value: 0, icon: 'User', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { label: '入驻企业', value: 0, icon: 'OfficeBuilding', color: 'linear-gradient(135deg, #2af598 0%, #009efd 100%)' },
  { label: '在招职位', value: 0, icon: 'Suitcase', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { label: '累计投递', value: 0, icon: 'Document', color: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)' }
])

const pieChartRef = ref(null)
const lineChartRef = ref(null)
let pieChartInstance = null
let lineChartInstance = null

const loadDashboardData = async () => {
  try {
    const res = await request.get('/dashboard/stats')
    if (res.code === 200 || res.code === '200') {
      const data = res.data

      cards.value[0].value = data.studentCount || 0
      cards.value[1].value = data.companyCount || 0
      cards.value[2].value = data.jobCount || 0
      cards.value[3].value = data.applicationCount || 0

      await nextTick()
      initPieChart(data.industryPie || [])
      initLineChart(data.trendLine || {})
    }
  } catch (error) {
    console.error('加载仪表盘数据失败', error)
  }
}

const initPieChart = (data) => {
  if (pieChartInstance) pieChartInstance.dispose()
  if (!pieChartRef.value) return
  pieChartInstance = echarts.init(pieChartRef.value)
  pieChartInstance.setOption({
    tooltip: { trigger: 'item', padding: 10, backgroundColor: 'rgba(255,255,255,0.9)' },
    legend: { bottom: '0%', left: 'center', itemWidth: 10, itemHeight: 10 },
    series: [
      {
        name: '职位数量',
        type: 'pie',
        radius: ['45%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 4 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        data: data
      }
    ]
  })
}

const initLineChart = (data) => {
  if (lineChartInstance) lineChartInstance.dispose()
  if (!lineChartRef.value) return
  lineChartInstance = echarts.init(lineChartRef.value)
  lineChartInstance.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
    legend: { data: ['简历投递量', '新增职位'], top: 10 },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data.dates,
      axisLine: { lineStyle: { color: '#eee' } }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { type: 'dashed' } } },
    series: [
      {
        name: '简历投递量',
        type: 'line',
        smooth: true,
        data: data.values,
        itemStyle: { color: '#409EFF' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64,158,255,0.3)' },
            { offset: 1, color: 'rgba(64,158,255,0)' }
          ])
        }
      },
      {
        name: '新增职位',
        type: 'line',
        smooth: true,
        data: data.views,
        itemStyle: { color: '#67C23A' }
      }
    ]
  })
}

const handleResize = () => {
  pieChartInstance?.resize()
  lineChartInstance?.resize()
}

onMounted(() => {
  loadDashboardData()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  pieChartInstance?.dispose()
  lineChartInstance?.dispose()
})
</script>

<style scoped>
.dashboard-container { padding: 0; }

.welcome-section {
  background: linear-gradient(135deg, #fff 0%, #f0f7ff 100%);
  padding: 32px;
  border-radius: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.welcome-title { margin: 0 0 12px 0; font-size: 26px; color: #1f2f3d; }
.welcome-subtitle { color: #5e6d82; margin: 0 0 24px 0; font-size: 15px; }
.welcome-illustration img { height: 160px; }

.stat-row { margin-bottom: 24px; }
.stat-card { border: none; border-radius: 12px; transition: all 0.3s; }
.stat-card:hover { transform: translateY(-5px); box-shadow: 0 8px 24px rgba(0,0,0,0.1); }

.stat-card-inner { padding: 24px; display: flex; align-items: center; gap: 20px; }
.stat-icon-wrapper {
  width: 64px; height: 64px; border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.stat-data .stat-value { font-size: 28px; font-weight: 800; color: #303133; line-height: 1.2; }
.stat-data .stat-label { font-size: 14px; color: #909399; margin-top: 4px; }

.stat-footer {
  padding: 10px 24px;
  background-color: #fafafa;
  border-top: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.trend.up { color: #67c23a; display: flex; align-items: center; font-weight: bold; }
.trend-text { color: #999; }

.chart-card { border-radius: 12px; border: none; }
.chart-box { height: 350px; }

.card-header { display: flex; justify-content: space-between; align-items: center; }
.header-title { font-weight: 700; font-size: 16px; position: relative; padding-left: 12px; }
.header-title::before {
  content: ''; position: absolute; left: 0; top: 50%; transform: translateY(-50%);
  width: 4px; height: 16px; background: #409EFF; border-radius: 2px;
}

.fade-in { animation: fadeIn 0.8s ease-out; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed, nextTick } from 'vue'
import request from '../utils/request'
import * as echarts from 'echarts'
import { User, OfficeBuilding, Suitcase, Document } from '@element-plus/icons-vue'

const currentDate = new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' })
const userRole = localStorage.getItem('userRole') || 'STUDENT'
const username = localStorage.getItem('username') || 'User'

const roleName = computed(() => {
  if (userRole === 'STUDENT') return '学生端'
  if (userRole === 'COMPANY') return '企业端'
  return '管理员'
})

const cards = ref([
  { label: '注册学生', value: 0, icon: 'User', color: 'linear-gradient(135deg, #69FF97 0%, #00E4FF 100%)' },
  { label: '入驻企业', value: 0, icon: 'OfficeBuilding', color: 'linear-gradient(135deg, #FFD26F 0%, #3677FF 100%)' },
  { label: '在招职位', value: 0, icon: 'Suitcase', color: 'linear-gradient(135deg, #FF977C 0%, #FF6B66 100%)' },
  { label: '累计投递', value: 0, icon: 'Document', color: 'linear-gradient(135deg, #B6CEE8 0%, #F578DC 100%)' }
])

const pieChartRef = ref(null)
const lineChartRef = ref(null)
let pieChartInstance = null
let lineChartInstance = null

const loadDashboardData = async () => {
  try {
    const res = await request.get('/dashboard/stats')
    if (res.code === 200 || res.code === '200') {
      const data = res.data

      cards.value[0].value = data.studentCount || 0
      cards.value[1].value = data.companyCount || 0
      cards.value[2].value = data.jobCount || 0
      cards.value[3].value = data.applicationCount || 0

      await nextTick()
      initPieChart(data.industryPie || [])
      initLineChart(data.trendLine || {})
    }
  } catch (error) {
    console.error('加载仪表盘数据失败', error)
  }
}

const initPieChart = (data) => {
  if (pieChartInstance) pieChartInstance.dispose()
  if (!pieChartRef.value) return

  pieChartInstance = echarts.init(pieChartRef.value)

  pieChartInstance.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: '0%', left: 'center' },
    series: [
      {
        name: '职位数量',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false, position: 'center' },
        emphasis: { label: { show: true, fontSize: 18, fontWeight: 'bold' } },
        data: data
      }
    ]
  })
}

const initLineChart = (data) => {
  if (lineChartInstance) lineChartInstance.dispose()
  if (!lineChartRef.value) return

  lineChartInstance = echarts.init(lineChartRef.value)

  lineChartInstance.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['简历投递量', '新增职位'] }, // 🔥 图例名称已更新
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data.dates
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '简历投递量',
        type: 'line',
        stack: 'Total',
        smooth: true,
        data: data.values,
        itemStyle: { color: '#409EFF' },
        areaStyle: { opacity: 0.3, color: '#409EFF' }
      },
      {
        name: '新增职位', // 🔥 对应后端传来的 views 数据
        type: 'line',
        smooth: true,
        data: data.views,
        itemStyle: { color: '#67C23A' }
      }
    ]
  })
}

const handleResize = () => {
  pieChartInstance?.resize()
  lineChartInstance?.resize()
}

onMounted(() => {
  loadDashboardData()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  pieChartInstance?.dispose()
  lineChartInstance?.dispose()
})
</script>

<style scoped>
.dashboard-container { padding: 20px; background-color: #f5f7fa; min-height: 100vh; }
.welcome-card {
  background: white; padding: 24px; border-radius: 8px;
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 20px; box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
.welcome-card h2 { margin: 0 0 10px 0; font-size: 20px; color: #333; }
.sub-text { color: #888; margin: 0; font-size: 14px; }
.welcome-img { height: 120px; object-fit: contain; }

.stat-row { margin-bottom: 20px; }
.stat-card { border: none; border-radius: 8px; }
.stat-content { display: flex; align-items: center; }
.stat-icon {
  width: 56px; height: 56px; border-radius: 12px;
  display: flex; justify-content: center; align-items: center;
  margin-right: 15px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}
.stat-card:hover .stat-icon { transform: scale(1.1); }
.stat-info { display: flex; flex-direction: column; }
.stat-label { font-size: 14px; color: #909399; margin-bottom: 5px; }
.stat-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }

.chart-row { margin-bottom: 20px; }
.chart-card { border-radius: 8px; border: none; }
.card-header { font-weight: bold; font-size: 16px; display: flex; align-items: center; }
.card-header span { border-left: 4px solid #409EFF; padding-left: 10px; }
</style>