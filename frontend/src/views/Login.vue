<template>
  <div class="login-page">
    <div class="login-card card">
      <div class="brand">
        <el-icon class="brand-icon"><Goods /></el-icon>
        <h1>校园商城</h1>
      </div>
      <p class="subtitle">欢迎回来，请登录您的账号</p>
      
      <el-alert
        title="测试账号：student1 / merchant1 / admin"
        description="密码均为 123456"
        type="info"
        show-icon
        :closable="false"
        class="test-account-alert"
      />
      
      <el-tabs v-model="tab" class="custom-tabs">
        <el-tab-pane label="账号登录" name="login">
          <el-form :model="loginForm" @submit.prevent="doLogin" size="large">
            <el-form-item>
              <el-input v-model="loginForm.username" placeholder="请输入用户名" :prefix-icon="User" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password :prefix-icon="Lock" @keyup.enter="doLogin" />
            </el-form-item>
            <el-button type="primary" class="submit-btn" @click="doLogin" :loading="loading" round>立即登录</el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册新账号" name="register">
          <el-form :model="regForm" size="large">
            <el-form-item>
              <el-input v-model="regForm.username" placeholder="设置用户名" :prefix-icon="User" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="regForm.password" type="password" placeholder="设置密码" show-password :prefix-icon="Lock" />
            </el-form-item>
            <el-form-item>
              <el-input v-model="regForm.phone" placeholder="输入手机号" :prefix-icon="Phone" />
            </el-form-item>
            <el-button type="primary" class="submit-btn" @click="doRegister" :loading="loading" round>立即注册</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <ul class="bg-bubbles">
      <li></li><li></li><li></li><li></li><li></li>
      <li></li><li></li><li></li><li></li><li></li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import request from '../utils/request'
import { ElMessage } from 'element-plus'
import { Goods, User, Lock, Phone } from '@element-plus/icons-vue'

const router = useRouter()
const user = useUserStore()
const tab = ref('login')
const loading = ref(false)
const loginForm = ref({ username: 'student1', password: '123456' })
const regForm = ref({ username: '', password: '', phone: '' })

async function doLogin() {
  if (!loginForm.value.username || !loginForm.value.password) {
    return ElMessage.warning('请输入用户名和密码')
  }
  loading.value = true
  try {
    const res = await request.post('/auth/login', loginForm.value)
    user.setUser(res.data)
    ElMessage.success('登录成功，欢迎回来！')
    router.push('/')
  } finally { loading.value = false }
}

async function doRegister() {
  if (!regForm.value.username || !regForm.value.password || !regForm.value.phone) {
    return ElMessage.warning('请填写完整的注册信息')
  }
  loading.value = true
  try {
    const res = await request.post('/auth/register', regForm.value)
    user.setUser(res.data)
    ElMessage.success('注册成功，已自动登录')
    router.push('/')
  } finally { loading.value = false }
}
</script>

<style scoped>
.login-page { 
  min-height: 100vh; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  background: linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%);
  position: relative;
  overflow: hidden;
}

.login-card { 
  width: 440px; 
  padding: 40px; 
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
  transform: translateY(-20px);
}

.brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 8px;
}
.brand-icon {
  font-size: 36px;
  color: #409eff;
}
.brand h1 { 
  text-align: center; 
  font-size: 28px;
  font-weight: 800;
  color: #303133;
  margin: 0;
}
.subtitle {
  text-align: center;
  color: #909399;
  margin-bottom: 24px;
  font-size: 15px;
}

.test-account-alert {
  margin-bottom: 24px;
  border-radius: 8px;
}

.custom-tabs :deep(.el-tabs__nav-wrap::after) {
  height: 1px;
  background-color: #ebeef5;
}
.custom-tabs :deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
  height: 48px;
  line-height: 48px;
}

.submit-btn {
  width: 100%;
  margin-top: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
}

/* Background Bubbles Animation */
.bg-bubbles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  list-style: none;
}
.bg-bubbles li {
  position: absolute;
  display: block;
  width: 40px;
  height: 40px;
  background-color: rgba(255, 255, 255, 0.15);
  bottom: -160px;
  animation: square 25s infinite;
  transition-timing-function: linear;
  border-radius: 50%;
}
.bg-bubbles li:nth-child(1) { left: 10%; }
.bg-bubbles li:nth-child(2) { left: 20%; width: 80px; height: 80px; animation-delay: 2s; animation-duration: 17s; }
.bg-bubbles li:nth-child(3) { left: 25%; animation-delay: 4s; }
.bg-bubbles li:nth-child(4) { left: 40%; width: 60px; height: 60px; animation-duration: 22s; background-color: rgba(255, 255, 255, 0.25); }
.bg-bubbles li:nth-child(5) { left: 70%; }
.bg-bubbles li:nth-child(6) { left: 80%; width: 120px; height: 120px; animation-delay: 3s; background-color: rgba(255, 255, 255, 0.2); }
.bg-bubbles li:nth-child(7) { left: 32%; width: 160px; height: 160px; animation-delay: 7s; }
.bg-bubbles li:nth-child(8) { left: 55%; width: 20px; height: 20px; animation-delay: 15s; animation-duration: 40s; }
.bg-bubbles li:nth-child(9) { left: 25%; width: 10px; height: 10px; animation-delay: 2s; animation-duration: 40s; background-color: rgba(255, 255, 255, 0.3); }
.bg-bubbles li:nth-child(10) { left: 90%; width: 160px; height: 160px; animation-delay: 11s; }

@keyframes square {
  0% { transform: translateY(0); }
  100% { transform: translateY(-1200px) rotate(600deg); }
}
</style>