<template>
  <div class="my-favorites">
    <div class="header-card">
      <h2>🌟 我的收藏夹</h2>
      <span class="subtitle">这里保存了你感兴趣的所有职位</span>
    </div>

    <div class="card-container" v-loading="loading">
      <el-empty v-if="tableData.length === 0" description="暂无收藏，快去职位大厅逛逛吧" />

      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in tableData" :key="item.id" style="margin-bottom: 20px;">
          <el-card class="job-card" shadow="hover">
            <div class="card-header">
              <h3 class="job-name" :title="item.jobName">{{ item.jobName }}</h3>
              <span class="salary-tag">{{ item.salary }}</span>
            </div>

            <div class="company-info">
              <img v-if="item.companyLogo" :src="item.companyLogo" class="card-logo" alt="logo"/>
              <el-icon v-else><OfficeBuilding /></el-icon>
              <span class="company-name">{{ item.companyName || '未知公司' }}</span>
            </div>

            <div class="desc-preview">
              {{ stripHtml(item.description) }}...
            </div>

            <div class="card-actions">
              <el-button type="primary" link @click="handleDetail(item)">查看详情</el-button>

              <div>
                <el-button
                    :type="myAppliedJobIds.includes(item.id) ? 'info' : 'primary'"
                    :disabled="myAppliedJobIds.includes(item.id)"
                    size="small" round
                    @click="handleApply(item)"
                >
                  {{ myAppliedJobIds.includes(item.id) ? '已投递' : '投递' }}
                </el-button>

                <el-button type="danger" link size="small" @click="handleRemove(item)">取消收藏</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-dialog v-model="detailVisible" title="职位详情" width="600px">
      <div class="detail-content">
        <h2>{{ currentJob.jobName }} <span style="color: #f56c6c; margin-left:10px;">{{ currentJob.salary }}</span></h2>
        <div style="display: flex; align-items: center; margin-bottom: 10px;">
          <img v-if="currentJob.companyLogo" :src="currentJob.companyLogo" style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px;" />
          <el-tag effect="dark" type="info">{{ currentJob.companyName }}</el-tag>
        </div>
        <el-divider />
        <div class="ql-editor" v-html="currentJob.description" style="padding: 0;"></div>
      </div>
      <template #footer>
        <el-button
            :type="myAppliedJobIds.includes(currentJob.id) ? 'info' : 'primary'"
            :disabled="myAppliedJobIds.includes(currentJob.id)"
            @click="handleApply(currentJob)"
            style="margin-right: 10px;"
        >
          {{ myAppliedJobIds.includes(currentJob.id) ? '已投递' : '立即投递' }}
        </el-button>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { OfficeBuilding } from '@element-plus/icons-vue'

const tableData = ref([])
const loading = ref(false)
const detailVisible = ref(false)
const currentJob = ref({})
const userId = Number(localStorage.getItem('userId'))

// 🔥 新增：存储我已经投递过的职位ID
const myAppliedJobIds = ref([])

// 去除HTML标签
const stripHtml = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').substring(0, 50)
}

// 获取收藏列表
const getFavorites = async () => {
  loading.value = true
  const res = await request.get('/favorites/list', { params: { userId } })
  if (res.code == 200) {
    tableData.value = res.data || []
  }
  loading.value = false
}

// 🔥 新增：查询我投递了哪些职位 (跟 JobHall 一样的逻辑)
const fetchMyApplications = async () => {
  try {
    const res = await request.get('/applications', {
      params: { userId: userId, role: 'STUDENT' }
    })
    if (res.code == 200) {
      myAppliedJobIds.value = res.data.map(app => app.jobId)
    }
  } catch (e) {
    console.error('获取投递记录失败')
  }
}

const handleDetail = (item) => {
  currentJob.value = item
  detailVisible.value = true
}

// 取消收藏
const handleRemove = (item) => {
  ElMessageBox.confirm('确定将该职位移出收藏夹？', '提示', { type: 'warning' }).then(async () => {
    const res = await request.post('/favorites/toggle', { userId, jobId: item.id })
    if (res.code == 200) {
      ElMessage.success('已移除')
      getFavorites() // 刷新列表
    }
  })
}

const handleApply = (item) => {
  // 🔥 前端双重拦截
  if (myAppliedJobIds.value.includes(item.id)) return

  detailVisible.value = false // 关弹窗

  ElMessageBox.confirm(`确认投递给【${item.companyName}】？`, '提示', { type: 'info' })
      .then(async () => {
        const res = await request.post('/applications', {
          jobId: item.id,
          studentId: userId,
          status: 'PENDING'
        })
        if (res.code == 200) {
          ElMessage.success('投递成功！')
          // 🔥 投递成功后，立马把ID加进去，让按钮变灰
          myAppliedJobIds.value.push(item.id)
        } else {
          ElMessage.error(res.msg || '投递失败')
        }
      })
}

onMounted(() => {
  getFavorites()
  fetchMyApplications() // 🔥 页面加载时同时获取投递记录
})
</script>

<style scoped>
.my-favorites { padding: 20px; background-color: #f5f7fa; min-height: 100vh; }
.header-card { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
.subtitle { color: #909399; font-size: 14px; margin-top: 5px; display: block; }
.job-card { transition: all 0.3s; height: 220px; display: flex; flex-direction: column; }
.job-card:hover { transform: translateY(-5px); box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.card-header { display: flex; justify-content: space-between; margin-bottom: 10px; }
.job-name { margin: 0; font-size: 18px; max-width: 150px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }
.salary-tag { color: #f56c6c; font-weight: bold; }
.company-info { display: flex; align-items: center; color: #666; margin-bottom: 10px; font-size: 14px; }
.card-logo { width: 24px; height: 24px; border-radius: 4px; margin-right: 5px; object-fit: cover; }
.desc-preview { color: #999; font-size: 13px; height: 40px; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.card-actions { margin-top: auto; padding-top: 15px; border-top: 1px solid #eee; display: flex; justify-content: space-between; align-items: center;}

/* 引入 Quill 样式 */
.ql-editor { padding: 0; line-height: 1.6; }
</style>