<template>
  <div style="padding: 20px;">
    <div style="margin-bottom: 20px; display: flex; justify-content: space-between;">
      <h2>🏢 公司管理</h2>

      <el-button
          v-if="userRole === 'ADMIN' || (userRole === 'COMPANY' && tableData.length === 0)"
          type="primary"
          @click="openDialog()"
      >
        + 完善/录入公司信息
      </el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />

      <el-table-column prop="companyName" label="公司名称" min-width="150">
        <template #default="scope">
          <span style="font-weight: bold;">{{ scope.row.companyName }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="industry" label="行业" width="120">
        <template #default="scope">
          <el-tag>{{ scope.row.industry || '未分类' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="address" label="地点" width="150">
        <template #default="scope">
          {{ scope.row.address || '-' }}
        </template>
      </el-table-column>

      <el-table-column prop="contactPhone" label="联系电话" width="120">
        <template #default="scope">
          {{ scope.row.contactPhone || '-' }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="openDialog(scope.row)">
            {{ userRole === 'COMPANY' ? '编辑信息' : '编辑' }}
          </el-button>

          <el-button v-if="userRole === 'ADMIN'" link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公司' : '录入新公司'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="公司名称">
          <el-input v-model="form.companyName" placeholder="例如：腾讯科技" />
        </el-form-item>
        <el-form-item label="公司行业">
          <el-input v-model="form.industry" placeholder="例如：互联网" />
        </el-form-item>
        <el-form-item label="公司简介">
          <el-input v-model="form.description" placeholder="简单介绍..." />
        </el-form-item>
        <el-form-item label="公司地点">
          <el-input v-model="form.address" placeholder="例如：深圳南山" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.contactPhone" placeholder="联系电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import request from '../utils/request'
import {ElMessage, ElMessageBox} from 'element-plus'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)

const userRole = localStorage.getItem('userRole')
const userId = localStorage.getItem('userId')

const form = reactive({
  id: null,
  userId: userId, // 🔥 关键：绑定当前用户
  companyName: '',
  industry: '',
  description: '',
  address: '',
  contactPhone: ''
})

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/companies', {
      params: {
        userId: userId,
        role: userRole
      }
    })
    if (res.code == 200) {
      // 如果是企业，我们只显示和他相关的那一条
      if (userRole === 'COMPANY') {
        // 后端可能返回所有，前端做个过滤保险
        const myComp = res.data.find(c => c.userId == userId)
        tableData.value = myComp ? [myComp] : []
      } else {
        tableData.value = res.data
      }
    }
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const openDialog = (row = null) => {
  if (row) {
    Object.assign(form, row)
  } else {
    form.id = null
    form.userId = userId // 确保新建时带上userId
    form.companyName = ''
    form.industry = ''
    form.description = ''
    form.address = ''
    form.contactPhone = ''
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!form.companyName) return ElMessage.warning('名称必填')
  try {
    const method = form.id ? 'put' : 'post'
    const res = await request[method]('/companies', form)
    if (res.code == 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      getList() // 刷新列表，这样界面上就会显示刚才录入的公司了
    } else {
      ElMessage.error('失败')
    }
  } catch (error) {
    ElMessage.error('系统异常')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除？', '警告', {type: 'warning'}).then(async () => {
    const res = await request.delete(`/companies/${row.id}`)
    if (res.code == 200) {
      ElMessage.success('删除成功')
      getList()
    }
  })
}

onMounted(() => {
  getList()
})
</script>