<template>
  <div class="layout">
    <header class="header">
      <div class="header-inner">
        <router-link to="/" class="logo">
          <el-icon class="logo-icon"><Goods /></el-icon>
          校园商城
        </router-link>
        <nav>
          <router-link to="/">首页</router-link>
          <router-link v-if="user.isLogin" to="/cart">购物车</router-link>
          <router-link v-if="user.isLogin" to="/orders">我的订单</router-link>
          <router-link v-if="user.isMerchant" to="/merchant/products">商户中心</router-link>
          <router-link v-if="user.isAdmin" to="/admin/users">管理后台</router-link>
          <router-link to="/help">帮助</router-link>
          <router-link to="/about">关于</router-link>
        </nav>
        <div class="user-area">
          <template v-if="user.isLogin">
            <el-dropdown trigger="click">
              <span class="user-dropdown-link">
                <el-avatar :size="32" style="background: #409eff">{{ user.username.charAt(0).toUpperCase() }}</el-avatar>
                <span class="username">{{ user.username }}</span>
                <el-tag size="small" type="info" effect="plain" class="role-tag">{{ roleLabel }}</el-tag>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')">个人资料</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/orders')">我的订单</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/notifications')">消息通知</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <router-link v-else to="/login">
            <el-button type="primary" round>登录 / 注册</el-button>
          </router-link>
        </div>
      </div>
    </header>
    <main>
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { getRoleLabel } from '../utils/view'
import { Goods } from '@element-plus/icons-vue'

const user = useUserStore()
const router = useRouter()
const roleLabel = computed(() => getRoleLabel(user.role))
const logout = () => { user.logout(); router.push('/login') }
</script>

<style scoped>
.header { 
  background: rgba(255, 255, 255, 0.85); 
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(0,0,0,0.05); 
  position: sticky; 
  top: 0; 
  z-index: 100; 
}
.header-inner { 
  max-width: 1200px; 
  margin: 0 auto; 
  padding: 0 20px; 
  height: 64px; 
  display: flex; 
  align-items: center; 
  gap: 32px; 
}
.logo { 
  font-size: 22px; 
  font-weight: 800; 
  color: #409eff; 
  display: flex;
  align-items: center;
  gap: 8px;
  letter-spacing: 0.5px;
}
.logo-icon {
  font-size: 26px;
}
nav { 
  flex: 1; 
  display: flex; 
  gap: 28px; 
}
nav a {
  font-size: 15px;
  color: #606266;
  font-weight: 500;
  padding: 6px 0;
  position: relative;
}
nav a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: #409eff;
  transition: all 0.3s ease;
  transform: translateX(-50%);
  border-radius: 2px;
}
nav a:hover {
  color: #409eff;
}
nav a.router-link-active { 
  color: #409eff; 
}
nav a.router-link-active::after {
  width: 100%;
}

.user-area { 
  display: flex; 
  align-items: center; 
}
.user-dropdown-link {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 20px;
  transition: background 0.2s;
}
.user-dropdown-link:hover {
  background: #f5f7fa;
}
.username {
  font-weight: 600;
  color: #303133;
  outline: none;
}
.role-tag {
  border-radius: 12px;
}
main { 
  min-height: calc(100vh - 64px); 
}
</style>