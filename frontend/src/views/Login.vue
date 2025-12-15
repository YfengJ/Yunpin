<template>
  <div class="login-container">
    <div class="bg-layer"></div>

    <div class="login-box">
      <div class="login-header">
        <div class="logo-wrapper">
          <span class="logo-icon">🚀</span>
        </div>
        <h2 class="app-title">云聘 · 直通车</h2>
        <p class="app-subtitle">连接校园精英与未来机遇</p>
      </div>

      <el-form :model="form" class="login-form">
        <el-form-item>
          <el-input
              v-model="form.username"
              placeholder="请输入账号"
              :prefix-icon="UserIcon"
              size="large"
              class="glass-input"
          />
        </el-form-item>

        <el-form-item>
          <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              class="glass-input"
              @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading" size="large">
          立即登录
        </el-button>

        <div class="form-footer">
          <span>还没有账号？</span>
          <a @click="$router.push('/register')">立即注册</a>
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
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const handleLogin = async () => {
  if (!form.username || !form.password) return ElMessage.warning('请输入账号密码')

  loading.value = true
  try {
    const res = await request.post('/users/login', form)
    if (res.code == 200 || res.code === '200') {
      const user = res.data
      const safeRole = user.role ? user.role.toUpperCase() : 'STUDENT'

      ElMessage.success(`欢迎回来，${user.username}`)
      localStorage.setItem('token', user.token || 'mock-token')
      localStorage.setItem('userRole', safeRole)
      localStorage.setItem('username', user.username)
      localStorage.setItem('userId', user.id)

      router.push('/dashboard/home')
    } else {
      ElMessage.error(res.msg || '账号或密码错误')
    }
  } catch (error) {
    ElMessage.error('连接服务器失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 🔥 全屏容器 */
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  position: relative;
}

/* 🔥 动态流光背景 - 强制生效 */
.bg-layer {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: linear-gradient(125deg, #2c3e50, #2980b9, #8e44ad, #2c3e50);
  background-size: 400% 400%;
  animation: gradientMove 15s ease infinite;
  z-index: -1;
}

@keyframes gradientMove {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* 🔥 磨砂玻璃卡片 */
.login-box {
  width: 420px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9); /* 略微不透明，保证文字清晰 */
  border-radius: 16px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px); /* 磨砂效果 */
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
  background: #ecf5ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 15px;
  box-shadow: 0 4px 10px rgba(64, 158, 255, 0.2);
}

.logo-icon { font-size: 32px; }

.app-title {
  margin: 0;
  font-size: 26px;
  font-weight: 700;
  color: #303133;
  letter-spacing: 1px;
}

.app-subtitle {
  margin: 8px 0 0;
  font-size: 14px;
  color: #909399;
}

/* 输入框样式重写 */
.glass-input :deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none;
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
}

.glass-input :deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  border-color: #409EFF;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
}

.login-btn {
  width: 100%;
  margin-top: 10px;
  font-weight: bold;
  letter-spacing: 2px;
  background: linear-gradient(to right, #409EFF, #3a8ee6);
  border: none;
  transition: transform 0.2s;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.3);
}

.form-footer {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.form-footer a {
  color: #409EFF;
  cursor: pointer;
  margin-left: 5px;
  font-weight: 600;
}
</style>