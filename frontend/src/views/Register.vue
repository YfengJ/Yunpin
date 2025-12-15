<template>
  <div class="login-container">
    <div class="bg-layer"></div>

    <div class="login-box">
      <div class="login-header">
        <div class="logo-wrapper">
          <span class="logo-icon">👋</span>
        </div>
        <h2 class="app-title">加入云聘</h2>
        <p class="app-subtitle">开启您的职业生涯之旅</p>
      </div>

      <el-form :model="form" ref="formRef" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="设置账号" :prefix-icon="UserIcon" size="large" class="glass-input" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="设置密码" :prefix-icon="Lock" size="large" show-password class="glass-input" />
        </el-form-item>

        <el-form-item prop="role">
          <el-select v-model="form.role" placeholder="请选择您的身份" size="large" style="width: 100%" class="glass-select">
            <el-option label="👨‍🎓 我是学生 (求职)" value="STUDENT" />
            <el-option label="🏢 我是企业 (招聘)" value="COMPANY" />
          </el-select>
        </el-form-item>

        <el-button type="success" class="register-btn" @click="handleRegister" :loading="loading" size="large">
          立即注册
        </el-button>

        <div class="form-footer">
          <span>已有账号？</span>
          <a @click="$router.push('/login')" style="color: #67C23A;">立即登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { User as UserIcon, Lock } from '@element-plus/icons-vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '', role: 'STUDENT' })

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  role: [{ required: true, message: '请选择身份', trigger: 'change' }]
}

const handleRegister = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await request.post('/users/register', form)
      if (res.code == 200 || res.code === '200') {
        ElMessage.success('注册成功！请登录')
        router.push('/login')
      } else {
        ElMessage.error(res.msg || '注册失败')
      }
    } catch (error) {
      ElMessage.error('连接失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
/* 🔥 复用 Login.vue 的样式，保持统一视觉 */
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  position: relative;
}

.bg-layer {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  /* 注册页稍微变一点色调 */
  background: linear-gradient(125deg, #134e5e, #71b280, #2c3e50);
  background-size: 400% 400%;
  animation: gradientMove 15s ease infinite;
  z-index: -1;
}

@keyframes gradientMove {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.login-box {
  width: 450px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  animation: floatUp 0.8s ease-out;
}

@keyframes floatUp {
  from { transform: translateY(50px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-wrapper {
  width: 60px; height: 60px;
  background: #f0f9eb; /* 绿色调 */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
  box-shadow: 0 4px 10px rgba(103, 194, 58, 0.2);
}

.logo-icon { font-size: 32px; }

.app-title { margin: 0; font-size: 26px; font-weight: 700; color: #303133; }
.app-subtitle { margin: 8px 0 0; font-size: 14px; color: #909399; }

.glass-input :deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none;
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
}

.glass-input :deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  border-color: #67C23A; /* 注册页绿色 */
  box-shadow: 0 0 0 3px rgba(103, 194, 58, 0.2);
}

.register-btn {
  width: 100%;
  margin-top: 10px;
  font-weight: bold;
  letter-spacing: 2px;
  background: linear-gradient(to right, #67C23A, #95d475);
  border: none;
  transition: transform 0.2s;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(103, 194, 58, 0.3);
}

.form-footer {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.form-footer a {
  color: #67C23A;
  cursor: pointer;
  margin-left: 5px;
  font-weight: 600;
}
</style>