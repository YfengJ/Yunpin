<template>
  <div style="padding: 20px;">
    <h2>📄 申请管理与审核</h2>

    <div style="margin-bottom: 15px; text-align: right;">
      <el-button @click="getApplications" :icon="Refresh">刷新列表</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%" stripe>

      <el-table-column v-if="userRole === 'STUDENT'" label="招聘公司" min-width="150">
        <template #default="scope">
          <span style="font-weight: bold;">{{ scope.row.job?.companyName || '未知公司' }}</span>
        </template>
      </el-table-column>

      <el-table-column v-if="userRole !== 'STUDENT'" label="求职者" min-width="180">
        <template #default="scope">
          <div style="display: flex; align-items: center; gap: 10px">
            <el-avatar :size="40" :src="scope.row.student?.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div style="display: flex; flex-direction: column;">
              <span style="font-weight: bold">{{ scope.row.student?.fullname || '未完善' }}</span>
              <span style="font-size: 12px; color: #666">{{ scope.row.student?.university || '未知学校' }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="申请职位" min-width="150">
        <template #default="scope">
          <span style="font-weight: bold; color: #409EFF">{{ scope.row.job?.jobName || '未知职位' }}</span>
          <br/>
          <span style="font-size: 12px; color: #888">薪资: {{ scope.row.job?.salary }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="状态" align="center" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="面试/反馈" min-width="180">
        <template #default="scope">
          <div v-if="scope.row.status === 'ACCEPTED'">
            <el-button type="success" link size="small" @click="viewInvite(scope.row)">
              <el-icon><Calendar /></el-icon>&nbsp;查看面试邀请
            </el-button>
          </div>
          <div v-else-if="scope.row.status === 'REJECTED'">
            <el-tooltip :content="scope.row.reason || '暂无详细理由'" placement="top">
               <span style="color: #F56C6C; font-size: 13px; cursor: pointer;">
                 <el-icon><Warning /></el-icon> 查看拒绝原因
               </span>
            </el-tooltip>
          </div>
          <span v-else style="color: #999; font-size: 12px;">等待处理中...</span>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="申请时间" width="160" align="center">
        <template #default="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template #default="scope">

          <el-button type="primary" link size="small" @click="openResume(scope.row.student)">
            {{ userRole === 'STUDENT' ? '预览简历' : '查看简历' }}
          </el-button>

          <span v-if="userRole === 'COMPANY'" style="margin-left: 10px;">
            <div v-if="scope.row.status === 'PENDING'" style="display: inline-block">
              <el-button type="success" size="small" circle icon="Check" @click="openAuditDialog(scope.row, 'ACCEPTED')" title="通过并邀请面试"></el-button>
              <el-button type="danger" size="small" circle icon="Close" @click="openAuditDialog(scope.row, 'REJECTED')" title="拒绝"></el-button>
            </div>
            <el-button v-else-if="scope.row.status === 'ACCEPTED'" link type="warning" size="small" @click="openAuditDialog(scope.row, 'ACCEPTED')">
              修改邀请
            </el-button>
          </span>

          <span v-if="userRole === 'ADMIN'" style="margin-left: 10px;">
            <el-popconfirm title="确定删除？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="danger" size="small" icon="Delete">删除</el-button>
              </template>
            </el-popconfirm>
          </span>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="resumeVisible" title="📄 简历详情" width="600px">
      <div v-if="currentStudent" class="resume-preview">
        <div class="resume-header">
          <div class="header-info">
            <h2 class="name">{{ currentStudent.fullname || '未填写姓名' }}</h2>
            <div class="tags">
              <el-tag size="small">{{ currentStudent.gender }}</el-tag>
              <el-tag size="small" type="warning" style="margin-left: 5px">{{ currentStudent.age }}岁</el-tag>
              <el-tag size="small" type="info" style="margin-left: 5px">{{ currentStudent.university }}</el-tag>
            </div>
            <p style="margin-top: 10px; color: #666">
              <el-icon><Phone /></el-icon> {{ currentStudent.phone || '-' }} &nbsp;|&nbsp;
              <el-icon><Message /></el-icon> {{ currentStudent.email || '-' }}
            </p>
          </div>
          <img :src="currentStudent.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="resume-avatar">
        </div>
        <el-divider />
        <div class="resume-section">
          <h4>🎯 求职意向</h4>
          <p>{{ currentStudent.jobIntention || '未填写' }} (期望薪资: {{ currentStudent.expectedSalary }})</p>
        </div>
        <div class="resume-section">
          <h4>🎓 教育背景</h4>
          <p><strong>{{ currentStudent.university }}</strong> - {{ currentStudent.major }}</p>
        </div>
        <div class="resume-section">
          <h4>✨ 个人能力与经历</h4>
          <p class="resume-content">{{ currentStudent.resumeContent || '该同学很懒，没有填写详细介绍...' }}</p>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="auditDialogVisible" :title="auditForm.status === 'ACCEPTED' ? '📧 发送面试邀请' : '❌ 拒绝申请'" width="500px">
      <el-form :model="auditForm">

        <div v-if="auditForm.status === 'ACCEPTED'">
          <el-alert title="通过审核后，将自动发送面试邀请给学生" type="success" :closable="false" style="margin-bottom: 20px;" />
          <el-form-item label="面试时间">
            <el-date-picker
                v-model="auditForm.interviewTime"
                type="datetime"
                placeholder="选择面试时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%" />
          </el-form-item>
          <el-form-item label="面试地点">
            <el-input v-model="auditForm.interviewLocation" placeholder="例如：科技园A座302会议室" />
          </el-form-item>
          <el-form-item label="联系方式">
            <el-input v-model="auditForm.interviewContact" placeholder="例如：李HR 138xxxx..." />
          </el-form-item>
        </div>

        <div v-else>
          <el-alert title="请填写拒绝理由，以便学生了解不足之处" type="warning" :closable="false" style="margin-bottom: 20px;" />
          <el-form-item label="拒绝理由">
            <el-input v-model="auditForm.reason" type="textarea" :rows="3" placeholder="例如：经验暂不匹配，感谢申请。" />
          </el-form-item>
        </div>

      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确认提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="inviteDialogVisible" title="🎉 面试邀请函" width="450px" center>
      <div style="text-align: center; padding: 20px;">
        <el-icon size="60" color="#67C23A"><Checked /></el-icon>
        <h2 style="color: #333">恭喜！您的申请已通过</h2>
        <p style="color: #666; margin-bottom: 30px;">企业已向您发出面试邀请，请准时参加。</p>

        <div style="background: #f0f9eb; padding: 25px; border-radius: 8px; text-align: left; line-height: 2;">
          <p><strong>🕒 时间：</strong> {{ currentInvite.time || '待定' }}</p>
          <p><strong>📍 地点：</strong> {{ currentInvite.location || '待定' }}</p>
          <p><strong>📞 联系：</strong> {{ currentInvite.contact || '待定' }}</p>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="inviteDialogVisible = false">收到，我会准时参加</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import request from '../utils/request'
import {ElMessage, ElMessageBox} from 'element-plus'
import { Check, Close, Phone, Message, Delete, Refresh, Warning, Calendar, Checked } from '@element-plus/icons-vue'

const tableData = ref([])
const userRole = localStorage.getItem('userRole')
const userId = localStorage.getItem('userId')

const resumeVisible = ref(false)
const auditDialogVisible = ref(false)
const inviteDialogVisible = ref(false)

const currentStudent = ref({})
const currentInvite = reactive({ time: '', location: '', contact: '' })

// 审核表单
const auditForm = reactive({
  id: null,
  status: '',
  reason: '',
  interviewTime: '',
  interviewLocation: '',
  interviewContact: '' // 🔥 新增
})

const getApplications = async () => {
  const res = await request.get('/applications', {
    params: {userId: userId, role: userRole}
  })
  if (res.code == 200 || res.code === '200') {
    tableData.value = res.data
  }
}

const openResume = (student) => {
  if (!student) {
    ElMessage.warning('该用户暂未完善简历信息')
    return
  }
  currentStudent.value = student
  resumeVisible.value = true
}

// 打开审核/邀请弹窗
const openAuditDialog = (row, newStatus) => {
  auditForm.id = row.id
  auditForm.status = newStatus
  // 回显已有数据
  auditForm.reason = row.reason || ''
  auditForm.interviewTime = row.interviewTime || ''
  auditForm.interviewLocation = row.interviewLocation || ''
  auditForm.interviewContact = row.interviewContact || '' // 🔥 回显

  auditDialogVisible.value = true
}

// 提交审核
const submitAudit = async () => {
  if (auditForm.status === 'ACCEPTED') {
    if (!auditForm.interviewTime || !auditForm.interviewLocation || !auditForm.interviewContact) {
      return ElMessage.warning('请将面试信息填写完整')
    }
  } else {
    if (!auditForm.reason) {
      return ElMessage.warning('请填写拒绝理由')
    }
  }

  // 先获取完整对象
  const row = tableData.value.find(item => item.id === auditForm.id)

  await request.put(`/applications/${auditForm.id}`, {
    ...row,
    status: auditForm.status,
    reason: auditForm.reason,
    interviewTime: auditForm.interviewTime,
    interviewLocation: auditForm.interviewLocation,
    interviewContact: auditForm.interviewContact
  })

  ElMessage.success('操作成功')
  auditDialogVisible.value = false
  getApplications()
}

// 学生查看邀请函
const viewInvite = (row) => {
  currentInvite.time = row.interviewTime
  currentInvite.location = row.interviewLocation
  currentInvite.contact = row.interviewContact // 🔥 显示
  inviteDialogVisible.value = true
}

const handleDelete = async (id) => {
  const res = await request.delete(`/applications/${id}`)
  if (res.code == 200) {
    ElMessage.success('删除成功')
    getApplications()
  } else {
    ElMessage.error(res.msg || '删除失败')
  }
}

const getStatusType = (s) => s === 'PENDING' ? 'warning' : (s === 'ACCEPTED' ? 'success' : 'danger')
const formatStatus = (s) => ({'PENDING': '待审核', 'ACCEPTED': '面试邀约', 'REJECTED': '已拒绝'}[s] || s)
const formatTime = (t) => t ? t.replace('T', ' ') : '-'

onMounted(() => {
  getApplications()
})
</script>na