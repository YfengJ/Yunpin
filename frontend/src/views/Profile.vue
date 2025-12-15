<template>
  <div style="padding: 20px;">
    <el-row :gutter="20">

      <el-col :span="12">
        <el-card>
          <template #header><h3>✏️ 编辑简历</h3></template>

          <el-form :model="form" label-width="90px">

            <el-form-item label="头像照片">
              <el-upload
                  class="avatar-uploader"
                  action="/api/files/upload"
                  name="file"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :on-error="handleAvatarError"
              >
                <img v-if="form.avatar" :src="form.avatar" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">+</el-icon>
              </el-upload>
            </el-form-item>

            <el-row>
              <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.fullname" /></el-form-item></el-col>
              <el-col :span="12">
                <el-form-item label="性别">
                  <el-radio-group v-model="form.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12"><el-form-item label="年龄"><el-input-number v-model="form.age" :min="16" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="期望薪资"><el-input v-model="form.expectedSalary" placeholder="如: 10k-15k"/></el-form-item></el-col>
            </el-row>

            <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
            <el-form-item label="电子邮箱"><el-input v-model="form.email" /></el-form-item>
            <el-form-item label="求职意向"><el-input v-model="form.jobIntention" placeholder="如: Java工程师"/></el-form-item>

            <el-form-item label="毕业院校"><el-input v-model="form.university" /></el-form-item>
            <el-form-item label="所学专业"><el-input v-model="form.major" /></el-form-item>

            <el-form-item label="自我介绍">
              <el-input v-model="form.resumeContent" type="textarea" :rows="5" />
            </el-form-item>

            <el-button type="primary" style="width: 100%; margin-top: 10px;" @click="saveProfile">💾 保存简历</el-button>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="preview-card" :body-style="{ padding: '0px' }">
          <div class="resume-paper">

            <div class="resume-header-bg">
              <div class="header-left">
                <h1 class="name">{{ form.fullname || '您的姓名' }}</h1>
                <p class="job-target">求职意向：{{ form.jobIntention || '暂无' }}</p>
                <div class="header-tags">
                  <span>{{ form.gender }}</span> · <span>{{ form.age }}岁</span> · <span>{{ form.university || '未填写学校' }}</span>
                </div>
              </div>
              <div class="header-right">
                <img :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="preview-avatar">
              </div>
            </div>

            <div class="contact-bar">
              <span>📞 {{ form.phone || '电话未填' }}</span>
              <span style="margin: 0 10px">|</span>
              <span>📧 {{ form.email || '邮箱未填' }}</span>
              <span style="margin: 0 10px">|</span>
              <span>💰 期望: {{ form.expectedSalary || '-' }}</span>
            </div>

            <div class="resume-body">
              <div class="section">
                <div class="section-title">🎓 教育背景</div>
                <div class="section-item">
                  <span class="school">{{ form.university || '学校名称' }}</span>
                  <span class="major">{{ form.major || '专业名称' }}</span>
                </div>
              </div>

              <div class="section">
                <div class="section-title">✨ 个人能力与经历</div>
                <div class="resume-text">
                  {{ form.resumeContent || '请在左侧填写详细介绍...' }}
                </div>
              </div>
            </div>

          </div>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const userId = localStorage.getItem('userId')

const form = reactive({
  id: null,
  userId: userId,
  fullname: '',
  gender: '男',
  age: 22,
  university: '',
  major: '',
  resumeContent: '',
  avatar: '',
  phone: '',
  email: '',
  expectedSalary: '',
  jobIntention: ''
})

const handleAvatarSuccess = (res) => {
  if (res.code == 200 || res.code === '200') {
    form.avatar = res.data
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(res.msg || '上传业务失败')
  }
}

const handleAvatarError = (err) => {
  console.error('上传报错:', err)
  ElMessage.error('上传失败，请检查文件大小或网络')
}

const loadProfile = async () => {
  if (!userId) return
  try {
    // 🔥 修复：去掉 /api
    const res = await request.get('/students/info', { params: { userId } })
    if (res.code == 200 && res.data && res.data.id) {
      Object.assign(form, res.data)
    }
  } catch (e) { console.error(e) }
}

const saveProfile = async () => {
  try {
    // 🔥 修复：去掉 /api
    const res = await request.put('/students/info', form)
    if (res.code == 200) ElMessage.success('保存成功')
    else ElMessage.error('保存失败')
  } catch (e) { ElMessage.error('异常') }
}

onMounted(() => loadProfile())
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
}
.avatar-uploader:hover { border-color: #409EFF; }
.avatar-uploader-icon {
  font-size: 28px; color: #8c939d; width: 100px; height: 100px; text-align: center; line-height: 100px;
}
.avatar { width: 100px; height: 100px; display: block; object-fit: cover; }
.resume-paper { background: white; min-height: 500px; }
.resume-header-bg { background: #3a4b66; color: white; padding: 25px; display: flex; justify-content: space-between; align-items: center; }
.name { margin: 0; font-size: 26px; }
.job-target { margin: 5px 0; opacity: 0.9; font-size: 14px; }
.header-tags { font-size: 12px; opacity: 0.8; }
.preview-avatar { width: 70px; height: 70px; border-radius: 50%; border: 2px solid white; object-fit: cover; }
.contact-bar { background: #f2f4f7; padding: 10px 20px; font-size: 13px; color: #555; display: flex; align-items: center; }
.resume-body { padding: 20px; }
.section { margin-bottom: 25px; }
.section-title { font-size: 16px; font-weight: bold; color: #3a4b66; border-bottom: 2px solid #3a4b66; padding-bottom: 5px; margin-bottom: 10px; }
.section-item { display: flex; justify-content: space-between; font-weight: bold; color: #333; }
.resume-text { white-space: pre-wrap; font-size: 14px; color: #666; line-height: 1.6; }
</style>