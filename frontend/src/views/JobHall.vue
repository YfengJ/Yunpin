<template>
  <div class="job-hall">
    <div class="header-card fade-in">
      <div class="title-section">
        <h2 class="main-title">💼 职位大厅</h2>
        <p class="subtitle">汇聚优质机遇，助你开启职业生涯新篇章</p>
      </div>

      <div class="search-section">
        <el-input
            v-model="searchName"
            placeholder="搜索职位名称、公司名称..."
            class="search-input"
            clearable
            @clear="getJobList"
            @keyup.enter="getJobList"
        >
          <template #prefix>
            <el-icon class="el-input__icon"><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" :icon="Search" @click="getJobList">搜索</el-button>
        <el-button @click="resetSearch" :icon="Refresh" plain>重置</el-button>
        <el-button
            v-if="userRole !== 'STUDENT'"
            type="success"
            class="publish-btn"
            @click="openCreateDialog"
            :icon="Plus"
        >发布职位</el-button>
      </div>
    </div>

    <div class="card-container" v-loading="loading">
      <el-empty v-if="tableData.length === 0" description="暂无职位数据，换个关键词试试吧" />

      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="(item, index) in tableData" :key="item.id" class="card-col">
          <el-card class="job-card" shadow="hover" :style="{ animationDelay: (index * 0.05) + 's' }">
            <div class="card-top">
              <div class="job-header">
                <h3 class="job-name" @click="handleDetail(item)">{{ item.jobName }}</h3>
                <el-icon
                    v-if="userRole === 'STUDENT'"
                    class="star-icon"
                    :class="{ 'is-active': myFavoriteIds.includes(item.id) }"
                    @click.stop="handleToggleFavorite(item)"
                >
                  <component :is="myFavoriteIds.includes(item.id) ? 'StarFilled' : 'Star'" />
                </el-icon>
              </div>
              <div class="salary-box">{{ item.salary }}</div>
              <div class="job-tags">
                <el-tag size="small" effect="plain">全职</el-tag>
                <el-tag size="small" type="success" effect="plain">校园招聘</el-tag>
                <el-tag size="small" type="warning" effect="plain" v-if="item.id % 2 === 0">急招</el-tag>
              </div>
            </div>

            <div class="company-row">
              <div class="company-brand">
                <el-avatar :size="32" :src="item.companyLogo" class="company-logo">
                  <el-icon><OfficeBuilding /></el-icon>
                </el-avatar>
                <span class="company-name">{{ item.companyName || '未知企业' }}</span>
              </div>
            </div>

            <div class="desc-content">
              {{ stripHtml(item.description) }}
            </div>

            <div class="card-footer">
              <div class="footer-left">
                <span class="post-time">{{ formatDate(item.createTime) }}发布</span>
              </div>
              <div class="footer-right">
                <el-button
                    v-if="userRole === 'STUDENT'"
                    :type="myAppliedJobIds.includes(item.id) ? 'info' : 'primary'"
                    :disabled="myAppliedJobIds.includes(item.id)"
                    size="small"
                    round
                    @click="handleApply(item)"
                >
                  {{ myAppliedJobIds.includes(item.id) ? '已投递' : '立即投递' }}
                </el-button>

                <div v-else-if="canEdit(item)" class="action-btns">
                  <el-button link type="primary" size="small" @click="handleEdit(item)">编辑</el-button>
                  <el-button link type="danger" size="small" @click="handleDelete(item)">删除</el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-dialog v-model="detailVisible" title="职位详情" width="700px" custom-class="job-detail-dialog">
      <div class="detail-wrapper">
        <div class="detail-header">
          <div class="detail-title-row">
            <h1>{{ currentJob.jobName }}</h1>
            <span class="detail-salary">{{ currentJob.salary }}</span>
          </div>
          <div class="detail-company-row">
            <el-avatar :size="48" :src="currentJob.companyLogo">
              <el-icon><OfficeBuilding /></el-icon>
            </el-avatar>
            <div class="company-text">
              <div class="comp-name">{{ currentJob.companyName }}</div>
              <div class="comp-meta">企业认证 · 诚信经营</div>
            </div>
          </div>
        </div>

        <el-divider>职位描述</el-divider>
        <div class="detail-body">
          <div class="ql-editor" v-html="currentJob.description"></div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
          <el-button
              v-if="userRole === 'STUDENT'"
              :type="myAppliedJobIds.includes(currentJob.id) ? 'info' : 'primary'"
              :disabled="myAppliedJobIds.includes(currentJob.id)"
              @click="handleApply(currentJob)"
          >
            {{ myAppliedJobIds.includes(currentJob.id) ? '已投递' : '立即投递' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        v-model="dialogVisible"
        :title="currentId ? '编辑职位' : '发布新职位'"
        width="800px"
        destroy-on-close
    >
      <el-form :model="form" label-width="80px" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属公司">
              <el-select v-model="form.companyId" :disabled="userRole === 'COMPANY'" style="width: 100%">
                <el-option v-for="item in companyOptions" :key="item.id" :label="item.companyName" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位名称">
              <el-input v-model="form.jobName" placeholder="例如：高级 Java 开发工程师" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="薪资范围">
          <el-input v-model="form.salary" placeholder="例如：15k-25k" />
        </el-form-item>

        <el-form-item label="职位详情">
          <div class="editor-container">
            <QuillEditor
                v-model:content="form.description"
                contentType="html"
                theme="snow"
                placeholder="请详细描述职位要求、工作内容等..."
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, OfficeBuilding, Star, StarFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { QuillEditor } from '@vueup/vue-quill'

const router = useRouter()
const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const currentId = ref(null)
const companyOptions = ref([])
const currentJob = ref({})
const searchName = ref('')
const userRole = localStorage.getItem('userRole')
const userId = Number(localStorage.getItem('userId'))
const myCompanyId = ref(null)

const myFavoriteIds = ref([])
const myAppliedJobIds = ref([])

const form = reactive({ companyId: '', jobName: '', salary: '', description: '' })

const stripHtml = (html) => {
  if (!html) return ''
  const text = html.replace(/<[^>]+>/g, '')
  return text.length > 60 ? text.substring(0, 60) + '...' : text
}

const formatDate = (dateStr) => {
  if (!dateStr) return '刚刚'
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

const canEdit = (item) => {
  if (userRole === 'ADMIN') return true
  return userRole === 'COMPANY' && item.companyId === myCompanyId.value
}

const initData = async () => {
  loading.value = true
  await getJobList()

  if (userRole === 'STUDENT') {
    fetchMyFavorites()
    fetchMyApplications()
  }

  const res = await request.get('/companies')
  if (res.code == 200) {
    companyOptions.value = res.data
    if (userRole === 'COMPANY') {
      const myComp = res.data.find(c => c.userId === userId)
      if (myComp) myCompanyId.value = myComp.id
    }
  }
  loading.value = false
}

const fetchMyFavorites = async () => {
  const res = await request.get('/favorites/my-ids', { params: { userId: userId } })
  if (res.code == 200) myFavoriteIds.value = res.data
}

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

const handleToggleFavorite = async (item) => {
  try {
    const res = await request.post('/favorites/toggle', { userId: userId, jobId: item.id })
    if (res.code == 200) {
      if (myFavoriteIds.value.includes(item.id)) {
        myFavoriteIds.value = myFavoriteIds.value.filter(id => id !== item.id)
        ElMessage.success('已取消收藏')
      } else {
        myFavoriteIds.value.push(item.id)
        ElMessage.success('收藏成功')
      }
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const getJobList = async () => {
  const res = await request.get('/jobs', { params: { name: searchName.value } })
  if (res.code == 200) {
    tableData.value = res.data || []
  }
}

const resetSearch = () => { searchName.value = ''; getJobList() }

const handleDetail = (item) => {
  currentJob.value = item
  detailVisible.value = true
}

const handleApply = (item) => {
  if (myAppliedJobIds.value.includes(item.id)) return
  detailVisible.value = false

  ElMessageBox.confirm(`确认投递给【${item.companyName}】？`, '提示', {
    confirmButtonText: '立即投递',
    cancelButtonText: '再想想',
    type: 'info'
  }).then(async () => {
    const res = await request.post('/applications', {
      jobId: item.id,
      studentId: userId,
      status: 'PENDING'
    })
    if (res.code == 200) {
      ElMessage.success('投递成功！')
      myAppliedJobIds.value.push(item.id)
    } else {
      ElMessage.error(res.msg || '投递失败')
    }
  })
}

const openCreateDialog = () => {
  if (userRole === 'COMPANY' && !myCompanyId.value) {
    ElMessageBox.alert('请先完善企业信息', '提示').then(() => router.push('/dashboard/company'))
    return
  }
  currentId.value = null
  Object.keys(form).forEach(key => form[key] = '')
  form.companyId = userRole === 'COMPANY' && myCompanyId.value ? myCompanyId.value : ''
  dialogVisible.value = true
}

const handleEdit = (item) => {
  currentId.value = item.id
  Object.assign(form, item)
  dialogVisible.value = true
}

const handleDelete = (item) => {
  ElMessageBox.confirm('确定删除该职位？此操作不可逆', '警告', { type: 'warning' }).then(async () => {
    await request.delete(`/jobs/${item.id}`)
    getJobList()
    ElMessage.success('已删除')
  })
}

const submitForm = async () => {
  if (!form.companyId) return ElMessage.error('所属公司不能为空')
  if (!form.jobName) return ElMessage.error('职位名称不能为空')
  const method = currentId.value ? 'put' : 'post'
  const data = currentId.value ? { ...form, id: currentId.value } : form
  await request[method]('/jobs', data)
  dialogVisible.value = false
  getJobList()
  ElMessage.success('操作成功')
}

onMounted(() => { initData() })
</script>

<style scoped>
.job-hall { padding: 0; background-color: transparent; }

.header-card {
  background: white;
  padding: 24px 32px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.main-title { margin: 0; font-size: 24px; color: #303133; }
.subtitle { margin: 4px 0 0; color: #909399; font-size: 14px; }

.search-section { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.search-input { width: 320px; }

.card-container { min-height: 400px; }

.job-card {
  height: 260px;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  transition: all 0.3s cubic-bezier(.25,.8,.25,1);
  margin-bottom: 24px;
  animation: slideIn 0.5s ease-out backwards;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.job-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.1);
  border-color: #409EFF;
}

.card-top { padding: 20px 20px 0; }

.job-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px; }
.job-name {
  margin: 0;
  font-size: 18px;
  color: #303133;
  font-weight: 600;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}
.job-name:hover { color: #409EFF; }

.star-icon {
  font-size: 20px;
  color: #c0c4cc;
  cursor: pointer;
  transition: all 0.3s;
  margin-left: 8px;
}
.star-icon.is-active { color: #f7ba2a; }

.salary-box { font-size: 16px; color: #f56c6c; font-weight: 700; margin-bottom: 12px; }

.job-tags { display: flex; gap: 8px; margin-bottom: 16px; }

.company-row { padding: 0 20px; margin-bottom: 12px; }
.company-brand { display: flex; align-items: center; gap: 10px; }
.company-logo { border: 1px solid #f2f6fc; }
.company-name { font-size: 14px; color: #606266; }

.desc-content {
  padding: 0 20px;
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
  height: 42px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-footer {
  margin-top: auto;
  padding: 12px 20px;
  border-top: 1px solid #f2f6fc;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-time { font-size: 12px; color: #c0c4cc; }

.detail-header { padding-bottom: 20px; }
.detail-title-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.detail-title-row h1 { margin: 0; font-size: 28px; }
.detail-salary { font-size: 24px; color: #f56c6c; font-weight: bold; }

.detail-company-row { display: flex; align-items: center; gap: 16px; background: #f8f9fb; padding: 16px; border-radius: 8px; }
.company-text .comp-name { font-size: 18px; font-weight: 600; margin-bottom: 4px; }
.company-text .comp-meta { font-size: 13px; color: #909399; }

.editor-container { border: 1px solid #dcdfe6; border-radius: 4px; }
:deep(.ql-container) { min-height: 250px; }
</style>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, OfficeBuilding, Star, StarFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { QuillEditor } from '@vueup/vue-quill'

const router = useRouter()
const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const currentId = ref(null)
const companyOptions = ref([])
const currentJob = ref({})
const searchName = ref('')
const userRole = localStorage.getItem('userRole')
const userId = Number(localStorage.getItem('userId'))
const myCompanyId = ref(null)

const myFavoriteIds = ref([])
// 🔥 新增：存储我已经投递过的职位ID
const myAppliedJobIds = ref([])

const form = reactive({ companyId: '', jobName: '', salary: '', description: '' })

const stripHtml = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]+>/g, '').substring(0, 50)
}

const canEdit = (item) => {
  if (userRole === 'ADMIN') return true
  return userRole === 'COMPANY' && item.companyId === myCompanyId.value
}

const initData = async () => {
  loading.value = true
  await getJobList()

  if (userRole === 'STUDENT') {
    fetchMyFavorites()
    fetchMyApplications() // 🔥 查投递记录
  }

  const res = await request.get('/companies')
  if (res.code == 200) {
    companyOptions.value = res.data
    if (userRole === 'COMPANY') {
      const myComp = res.data.find(c => c.userId === userId)
      if (myComp) myCompanyId.value = myComp.id
    }
  }
  loading.value = false
}

const fetchMyFavorites = async () => {
  const res = await request.get('/favorites/my-ids', { params: { userId: userId } })
  if (res.code == 200) myFavoriteIds.value = res.data
}

// 🔥 新增：查询我投递了哪些职位
const fetchMyApplications = async () => {
  try {
    const res = await request.get('/applications', {
      params: { userId: userId, role: 'STUDENT' }
    })
    if (res.code == 200) {
      // 提取所有 jobId 存入数组
      myAppliedJobIds.value = res.data.map(app => app.jobId)
    }
  } catch (e) {
    console.error('获取投递记录失败')
  }
}

const handleToggleFavorite = async (item) => {
  try {
    const res = await request.post('/favorites/toggle', { userId: userId, jobId: item.id })
    if (res.code == 200) {
      if (myFavoriteIds.value.includes(item.id)) {
        myFavoriteIds.value = myFavoriteIds.value.filter(id => id !== item.id)
        ElMessage.success('已取消收藏')
      } else {
        myFavoriteIds.value.push(item.id)
        ElMessage.success('收藏成功')
      }
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const getJobList = async () => {
  const res = await request.get('/jobs', { params: { name: searchName.value } })
  if (res.code == 200) {
    tableData.value = res.data || []
  }
}

const resetSearch = () => { searchName.value = ''; getJobList() }

const handleDetail = (item) => {
  currentJob.value = item
  detailVisible.value = true
}

const handleApply = (item) => {
  // 🔥 前端双重拦截：如果已经投递过，直接返回
  if (myAppliedJobIds.value.includes(item.id)) return

  // 详情弹窗可以关了，体验更好
  detailVisible.value = false

  ElMessageBox.confirm(`确认投递给【${item.companyName}】？`, '提示', { type: 'info' })
      .then(async () => {
        const res = await request.post('/applications', {
          jobId: item.id,
          studentId: userId,
          status: 'PENDING'
        })
        if (res.code == 200) {
          ElMessage.success('投递成功！')
          // 🔥 投递成功后，立马把这个ID加到数组里，按钮立马变灰，不需要刷新页面
          myAppliedJobIds.value.push(item.id)
        } else {
          ElMessage.error(res.msg || '投递失败')
        }
      })
}

const openCreateDialog = () => {
  if (userRole === 'COMPANY' && !myCompanyId.value) {
    ElMessageBox.alert('请先完善企业信息', '提示').then(() => router.push('/dashboard/company'))
    return
  }
  currentId.value = null
  form.jobName = ''
  form.salary = ''
  form.description = ''
  form.companyId = userRole === 'COMPANY' && myCompanyId.value ? myCompanyId.value : ''
  dialogVisible.value = true
}

const handleEdit = (item) => {
  currentId.value = item.id
  Object.assign(form, item)
  dialogVisible.value = true
}

const handleDelete = (item) => {
  ElMessageBox.confirm('确定删除？', '警告', { type: 'warning' }).then(async () => {
    await request.delete(`/jobs/${item.id}`)
    getJobList()
    ElMessage.success('已删除')
  })
}

const submitForm = async () => {
  if (!form.companyId) return ElMessage.error('所属公司不能为空')
  const method = currentId.value ? 'put' : 'post'
  const data = currentId.value ? { ...form, id: currentId.value } : form
  await request[method]('/jobs', data)
  dialogVisible.value = false
  getJobList()
  ElMessage.success('操作成功')
}

onMounted(() => { initData() })
</script>

<style scoped>
.job-hall { padding: 20px; background-color: #f5f7fa; min-height: 100vh; }
.header-card { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; }
.search-section { display: flex; align-items: center; }
.search-input { width: 300px; }
.ml-2 { margin-left: 10px; }
.job-card { transition: all 0.3s; height: 220px; display: flex; flex-direction: column; }
.job-card:hover { transform: translateY(-5px); box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.card-header { display: flex; justify-content: space-between; margin-bottom: 10px; }
.header-left { display: flex; align-items: center; overflow: hidden; }

.job-name { margin: 0; font-size: 18px; max-width: 150px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; }

.star-icon {
  margin-left: 8px;
  cursor: pointer;
  font-size: 18px;
  color: #909399;
  transition: all 0.3s;
}
.star-icon:hover { transform: scale(1.2); }
.star-icon.is-active { color: #e6a23c; }

.salary-tag { color: #f56c6c; font-weight: bold; flex-shrink: 0; }
.company-info { display: flex; align-items: center; color: #666; margin-bottom: 10px; font-size: 14px; }
.card-logo { width: 24px; height: 24px; border-radius: 4px; margin-right: 5px; object-fit: cover; }
.desc-preview { color: #999; font-size: 13px; height: 40px; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
.card-actions { margin-top: auto; padding-top: 15px; border-top: 1px solid #eee; display: flex; justify-content: space-between; }

.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  width: 100%;
  background-color: white;
}

:deep(.ql-toolbar) {
  border: none;
  border-bottom: 1px solid #eee;
  background-color: #fafafa;
}

:deep(.ql-container) {
  border: none;
  font-size: 14px;
}

:deep(.ql-editor) {
  min-height: 200px;
  padding: 12px 15px;
  line-height: 1.6;
}
</style>