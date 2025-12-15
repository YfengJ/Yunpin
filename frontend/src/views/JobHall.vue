<template>
  <div class="job-hall">
    <div class="header">
      <div class="left">
        <h2>💼 职位大厅</h2>
        <span class="subtitle">共找到 {{ tableData.length }} 个职位</span>
      </div>

      <div class="center" style="flex: 1; margin: 0 40px; max-width: 500px;">
        <el-input
            v-model="searchName"
            placeholder="🔍 搜索职位名称..."
            clearable
            @clear="getJobList"
            @keyup.enter="getJobList"
        >
          <template #append>
            <el-button :icon="Search" @click="getJobList" />
          </template>
        </el-input>
      </div>

      <div class="right">
        <el-button @click="resetSearch" :icon="Refresh" circle title="重置/刷新" />

        <el-button
            v-if="userRole !== 'STUDENT'"
            type="primary"
            @click="openCreateDialog"
            :icon="Plus"
        >发布新职位</el-button>
      </div>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading" stripe border>
      <el-table-column prop="jobName" label="职位名称" min-width="150" />
      <el-table-column label="所属公司" min-width="160">
        <template #default="scope">
          <el-tag effect="plain" type="info">{{ scope.row.companyName || '未知公司' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="salary" label="薪资" width="150" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />

      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template #default="scope">

          <el-button
              v-if="userRole === 'STUDENT'"
              type="success" size="small" :icon="Promotion"
              @click="handleApply(scope.row)"
          >立即投递</el-button>

          <div v-else>
            <div v-if="canEdit(scope.row)">
              <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
            </div>
            <span v-else style="color: #999; font-size: 12px;">无权操作</span>
          </div>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="currentId ? '编辑' : '发布'" width="500px">
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="所属公司">
          <el-select v-model="form.companyId" :disabled="userRole === 'COMPANY'" style="width: 100%">
            <el-option v-for="item in companyOptions" :key="item.id" :label="item.companyName" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="职位名称"> <el-input v-model="form.jobName" /> </el-form-item>
        <el-form-item label="薪资范围"> <el-input v-model="form.salary" /> </el-form-item>
        <el-form-item label="职位描述"> <el-input v-model="form.description" type="textarea" /> </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import request from '../utils/request'
import {ElMessage, ElMessageBox} from 'element-plus'
// 🔥 新增 Search 图标
import {Plus, Refresh, Promotion, Search} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const currentId = ref(null)
const companyOptions = ref([])

// 🔥 新增：搜索关键词
const searchName = ref('')

const userRole = localStorage.getItem('userRole')
const userId = Number(localStorage.getItem('userId'))
const myCompanyId = ref(null)

const form = reactive({companyId: '', jobName: '', salary: '', description: ''})

const canEdit = (row) => {
  if (userRole === 'ADMIN') return true
  return userRole === 'COMPANY' && row.companyId === myCompanyId.value
}

const initData = async () => {
  loading.value = true
  await getJobList()

  const res = await request.get('/companies')
  if (res.code == 200) {
    companyOptions.value = res.data
    if (userRole === 'COMPANY') {
      const myComp = res.data.find(c => c.userId === userId)
      if (myComp) {
        myCompanyId.value = myComp.id
      }
    }
  }
  loading.value = false
}

// 🔥 修改：获取列表时带上参数
const getJobList = async () => {
  const res = await request.get('/jobs', {
    params: {
      name: searchName.value // 把搜索框里的字传给后端
    }
  })
  if (res.code == 200) tableData.value = res.data || []
}

// 🔥 新增：重置搜索
const resetSearch = () => {
  searchName.value = ''
  getJobList()
}

const handleApply = (row) => {
  ElMessageBox.confirm(`投递给【${row.companyName}】？`, '提示', {type: 'info'})
      .then(async () => {
        await request.post('/applications', {
          jobId: row.id,
          studentId: userId,
          status: 'PENDING'
        })
        ElMessage.success('投递成功')
      })
}

const openCreateDialog = () => {
  if (userRole === 'COMPANY' && !myCompanyId.value) {
    ElMessageBox.confirm('您尚未完善企业信息，无法发布职位。是否现在去完善？', '提示', {
      confirmButtonText: '去完善',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      router.push('/dashboard/company')
    })
    return
  }

  currentId.value = null
  form.jobName = '';
  form.salary = '';
  form.description = ''
  if (userRole === 'COMPANY' && myCompanyId.value) {
    form.companyId = myCompanyId.value
  } else {
    form.companyId = ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除？', '警告', {type: 'warning'}).then(async () => {
    await request.delete(`/jobs/${row.id}`)
    getJobList()
    ElMessage.success('已删除')
  })
}

const submitForm = async () => {
  if (!form.companyId) return ElMessage.error('所属公司不能为空')

  const method = currentId.value ? 'put' : 'post'
  const data = currentId.value ? {...form, id: currentId.value} : form
  await request[method]('/jobs', data)
  dialogVisible.value = false
  getJobList()
}

onMounted(() => {
  initData()
})
</script>

<style scoped>
.job-hall { padding: 20px; }
.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.subtitle { color: #999; font-size: 13px; margin-left: 10px; }
</style>