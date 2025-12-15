<template>
  <div style="padding: 20px;">
    <h2>👥 用户账户管理</h2>

    <div style="margin-bottom: 15px; text-align: right;">
      <el-button @click="getUsers" :icon="Refresh">刷新列表</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" align="center" />

      <el-table-column prop="username" label="用户名" min-width="150">
        <template #default="scope">
          <span style="font-weight: bold">{{ scope.row.username }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="role" label="角色身份" width="120" align="center">
        <template #default="scope">
          <el-tag :type="getRoleType(scope.row.role)">
            {{ formatRole(scope.row.role) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="注册时间" width="180" align="center">
        <template #default="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="250" align="center" fixed="right">
        <template #default="scope">
          <div v-if="scope.row.role && scope.row.role.toUpperCase() !== 'ADMIN'">

            <el-popconfirm title="确定重置密码为 123456 吗？" @confirm="handleReset(scope.row)">
              <template #reference>
                <el-button type="warning" size="small" :icon="Key">重置密码</el-button>
              </template>
            </el-popconfirm>

            <el-popconfirm title="⚠️ 确定删除该账号？(关联的简历/公司也会被删除)" @confirm="handleDelete(scope.row)">
              <template #reference>
                <el-button type="danger" size="small" :icon="Delete" style="margin-left: 10px;">删除</el-button>
              </template>
            </el-popconfirm>

          </div>
          <span v-else style="color: #999; font-size: 12px;">系统管理员</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import { Refresh, Key, Delete } from '@element-plus/icons-vue'

const tableData = ref([])
const loading = ref(false)

const getUsers = async () => {
  loading.value = true
  try {
    const res = await request.get('/users')
    if (res.code == 200 || res.code === '200') {
      tableData.value = res.data
    }
  } catch (error) {
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

const handleReset = async (row) => {
  const res = await request.put(`/users/${row.id}/reset-password`)
  if (res.code == 200 || res.code === '200') {
    ElMessage.success(`用户 ${row.username} 密码已重置为 123456`)
  } else {
    ElMessage.error(res.msg || '重置失败')
  }
}

const handleDelete = async (row) => {
  const res = await request.delete(`/users/${row.id}`)
  if (res.code == 200 || res.code === '200') {
    ElMessage.success('删除成功')
    getUsers()
  } else {
    ElMessage.error(res.msg || '删除失败')
  }
}

// 🔥🔥🔥 核心修复：忽略大小写判断 🔥🔥🔥
const getRoleType = (role) => {
  if (!role) return 'info'
  const r = role.toUpperCase() // 强转大写
  if (r === 'ADMIN') return 'danger'
  if (r === 'COMPANY') return 'success'
  return 'primary'
}

const formatRole = (role) => {
  if (!role) return '未知'
  const r = role.toUpperCase() // 强转大写
  if (r === 'ADMIN') return '管理员'
  if (r === 'COMPANY') return '企业'
  return '学生'
}

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : '-'
}

onMounted(() => {
  getUsers()
})
</script>