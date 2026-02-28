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

      <el-table-column label="公司Logo" width="100" align="center">
        <template #default="scope">
          <el-image
              style="width: 50px; height: 50px; border-radius: 4px;"
              :src="scope.row.logo"
              :preview-src-list="[scope.row.logo]"
              fit="cover"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>

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

      <el-table-column prop="auditStatus" label="审核状态" width="120" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.auditStatus === 'approved'" type="success">✅ 已通过</el-tag>
          <el-tag v-else-if="scope.row.auditStatus === 'rejected'" type="danger">❌ 已驳回</el-tag>
          <el-tag v-else type="warning">⏳ 待审核</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="address" label="地点" width="150" show-overflow-tooltip />
      <el-table-column prop="contactPhone" label="联系电话" width="120" />

      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template #default="scope">
          <div v-if="userRole === 'ADMIN' && (scope.row.auditStatus === 'pending' || !scope.row.auditStatus)">
            <el-button type="success" size="small" @click="handleAudit(scope.row, 'approved')">通过</el-button>
            <el-button type="danger" size="small" @click="handleAudit(scope.row, 'rejected')">驳回</el-button>
          </div>

          <div v-else style="display: inline-block;">
            <el-button link type="primary" size="small" @click="openDialog(scope.row)">
              {{ userRole === 'COMPANY' ? '编辑信息' : '编辑' }}
            </el-button>
            <el-button v-if="userRole === 'ADMIN'" link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公司' : '录入新公司'" width="500px">
      <el-form :model="form" label-width="80px">

        <el-form-item label="公司Logo">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/api/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.logo" :src="form.logo" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div style="font-size: 12px; color: #999; line-height: 1.5; margin-top: 5px;">
            点击图标上传，建议尺寸 200x200
          </div>
        </el-form-item>

        <el-form-item label="公司名称">
          <el-input v-model="form.companyName" placeholder="例如：腾讯科技" />
        </el-form-item>
        <el-form-item label="公司行业">
          <el-input v-model="form.industry" placeholder="例如：互联网" />
        </el-form-item>
        <el-form-item label="公司简介">
          <el-input v-model="form.description" type="textarea" placeholder="简单介绍..." />
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
import { Plus, Picture } from '@element-plus/icons-vue'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)

const userRole = localStorage.getItem('userRole')
const userId = localStorage.getItem('userId')

const form = reactive({
  id: null,
  userId: userId,
  companyName: '',
  logo: '',
  industry: '',
  description: '',
  address: '',
  contactPhone: ''
})

// Logo上传成功回调
const handleAvatarSuccess = (response, uploadFile) => {
  if (response.code === 200 || response.code === '200') {
    form.logo = response.data
    ElMessage.success('Logo上传成功')
  } else {
    ElMessage.error('上传失败：' + response.msg)
  }
}

// Logo上传前校验
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('Logo必须是 JPG 或 PNG 格式!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 5) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

const getList = async () => {
  loading.value = true
  try {
    const res = await request.get('/companies', {
      params: { userId: userId, role: userRole }
    })
    if (res.code == 200) {
      if (userRole === 'COMPANY') {
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

// 🔥 新增：处理审核逻辑
const handleAudit = (row, status) => {
  const actionText = status === 'approved' ? '通过' : '驳回'
  ElMessageBox.confirm(`确定要【${actionText}】该公司入驻申请吗？`, '审核提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: status === 'approved' ? 'success' : 'warning'
  }).then(async () => {
    // 调用刚才写的后端接口
    const res = await request.post(`/companies/audit/${row.id}`, null, {
      params: { status: status }
    })
    if (res.code == 200) {
      ElMessage.success(`已${actionText}`)
      getList() // 刷新列表
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  })
}

const openDialog = (row = null) => {
  if (row) {
    Object.assign(form, row)
  } else {
    form.id = null
    form.userId = userId
    form.companyName = ''
    form.logo = ''
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
      getList()
    } else {
      ElMessage.error(res.msg || '失败')
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

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>