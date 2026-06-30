<template>
  <div class="layout">
    <header class="header">
      <div class="header-inner">
        <router-link to="/" class="logo">
          <div class="logo-icon-wrapper">
            <el-icon class="logo-icon"><Goods /></el-icon>
          </div>
          <div class="logo-text">
            <span class="logo-main">校园商城</span>
            <span class="logo-sub">Campus Mall</span>
          </div>
        </router-link>
        <nav>
          <router-link to="/" class="nav-item">
            <el-icon class="nav-icon"><HomeFilled /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link to="/discover" class="nav-item">
            <el-icon class="nav-icon"><Compass /></el-icon>
            <span>发现</span>
          </router-link>
          <router-link to="/points-mall" class="nav-item">
            <el-icon class="nav-icon"><Present /></el-icon>
            <span>积分商城</span>
          </router-link>
          <router-link to="/community" class="nav-item">
            <el-icon class="nav-icon"><ChatDotRound /></el-icon>
            <span>社区</span>
          </router-link>
          <router-link v-if="user.isLogin" to="/cart" class="nav-item">
            <el-icon class="nav-icon"><ShoppingCart /></el-icon>
            <span>购物车</span>
          </router-link>
          <router-link v-if="user.isLogin" to="/orders" class="nav-item">
            <el-icon class="nav-icon"><Document /></el-icon>
            <span>订单</span>
          </router-link>
          <router-link v-if="user.isMerchant" to="/merchant/products" class="nav-item">
            <el-icon class="nav-icon"><Shop /></el-icon>
            <span>商户中心</span>
          </router-link>
          <router-link v-if="user.isAdmin" to="/admin/users" class="nav-item">
            <el-icon class="nav-icon"><Setting /></el-icon>
            <span>管理后台</span>
          </router-link>
          <router-link to="/feedback" class="nav-item">
            <el-icon class="nav-icon"><EditPen /></el-icon>
            <span>反馈</span>
          </router-link>
          <router-link to="/help" class="nav-item">
            <el-icon class="nav-icon"><QuestionFilled /></el-icon>
            <span>帮助</span>
          </router-link>
          <router-link to="/about" class="nav-item">
            <el-icon class="nav-icon"><InfoFilled /></el-icon>
            <span>关于</span>
          </router-link>
        </nav>
        <div class="user-area">
          <template v-if="user.isLogin">
            <el-dropdown trigger="click">
              <span class="user-dropdown-link">
                <el-avatar :size="38" class="user-avatar">{{ user.username.charAt(0).toUpperCase() }}</el-avatar>
                <span class="username">{{ user.username }}</span>
                <el-tag size="small" :type="getRoleType(user.role)" effect="plain" class="role-tag">{{ roleLabel }}</el-tag>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')">
                    <el-icon><User /></el-icon>个人资料
                  </el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/orders')">
                    <el-icon><Document /></el-icon>我的订单
                  </el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/notifications')">
                    <el-icon><Bell /></el-icon>消息通知
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <router-link v-else to="/login">
            <el-button type="primary" round class="login-btn">
              <el-icon><UserFilled /></el-icon>
              <span>登录 / 注册</span>
            </el-button>
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
import {
  Goods,
  HomeFilled,
  Compass,
  Present,
  ChatDotRound,
  ShoppingCart,
  Document,
  Shop,
  Setting,
  EditPen,
  QuestionFilled,
  InfoFilled,
  User,
  Bell,
  SwitchButton,
  UserFilled
} from '@element-plus/icons-vue'

const user = useUserStore()
const router = useRouter()
const roleLabel = computed(() => getRoleLabel(user.role))

const getRoleType = (role) => {
  const types = { 1: 'success', 2: 'warning', 3: 'danger' }
  return types[role] || 'info'
}

const logout = () => { user.logout(); router.push('/login') }
</script>

<style scoped>
.header { 
  background: #ffffff;
  border-bottom: 1px solid rgba(228, 228, 231, 0.8);
  position: sticky; 
  top: 0; 
  z-index: 100;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.03), 
               0 1px 2px -1px rgba(0, 0, 0, 0.03);
}
.header-inner { 
  max-width: 1400px; 
  margin: 0 auto; 
  padding: 0 40px; 
  height: 80px; 
  display: flex; 
  align-items: center; 
  gap: 40px;
}

.logo { 
  font-size: 28px; 
  font-weight: 700;
  color: #18181b;
  display: flex;
  align-items: center;
  gap: 16px;
  letter-spacing: -0.02em;
  text-decoration: none;
  transition: all 0.3s ease-in-out;
}
.logo:hover {
  color: #3f3f46;
}

.logo-icon-wrapper {
  width: 48px;
  height: 48px;
  background: #fafafa;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(228, 228, 231, 0.8);
  transition: all 0.3s ease-in-out;
}
.logo:hover .logo-icon-wrapper {
  background: #f4f4f5;
  border-color: rgba(228, 228, 231, 0.9);
}
.logo-icon {
  font-size: 26px;
  color: #18181b;
}

.logo-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.logo-main {
  font-size: 20px;
  font-weight: 700;
  letter-spacing: -0.01em;
  color: #18181b;
}
.logo-sub {
  font-size: 12px;
  opacity: 0.6;
  letter-spacing: 0.05em;
  font-weight: 400;
  color: #71717a;
}

nav { 
  flex: 1; 
  display: flex; 
  gap: 8px;
  overflow-x: auto;
  scrollbar-width: none;
  padding: 8px 0;
}
nav::-webkit-scrollbar {
  display: none;
}

.nav-item {
  font-size: 15px;
  color: #71717a;
  font-weight: 500;
  padding: 12px 20px;
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  border-radius: 12px;
  transition: all 0.3s ease-in-out;
  white-space: nowrap;
  text-decoration: none;
  background: transparent;
  border: 1px solid transparent;
}
.nav-icon {
  font-size: 18px;
  opacity: 0.7;
  transition: all 0.3s ease-in-out;
}
.nav-item:hover {
  color: #18181b;
  background: #fafafa;
  border-color: rgba(228, 228, 231, 0.6);
  transform: translateY(-0.5px);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}
.nav-item:hover .nav-icon {
  opacity: 1;
  color: #18181b;
}
.nav-item.router-link-active { 
  color: #18181b;
  background: #ffffff;
  border-color: rgba(228, 228, 231, 0.8);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04);
}
.nav-item.router-link-active .nav-icon {
  color: #18181b;
  opacity: 1;
}

.user-area { 
  display: flex; 
  align-items: center; 
  gap: 16px;
}

.user-dropdown-link {
  display: flex;
  align-items: center;
  gap: 14px;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 12px;
  transition: all 0.3s ease-in-out;
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.02);
}
.user-dropdown-link:hover {
  background: #fafafa;
  border-color: rgba(228, 228, 231, 0.9);
  transform: translateY(-0.5px);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04);
}

.user-avatar {
  background: #18181b;
  font-weight: 700;
  font-size: 16px;
  color: #ffffff;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease-in-out;
}
.user-dropdown-link:hover .user-avatar {
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.08);
}

.username {
  font-weight: 600;
  color: #18181b;
  outline: none;
  font-size: 15px;
  letter-spacing: -0.01em;
}

.role-tag {
  background: #fafafa;
  border: 1px solid rgba(228, 228, 231, 0.8);
  color: #71717a;
  font-weight: 600;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 8px;
  transition: all 0.3s ease-in-out;
}
.user-dropdown-link:hover .role-tag {
  background: #f4f4f5;
  border-color: rgba(228, 228, 231, 0.9);
}

.login-btn {
  padding: 12px 28px;
  font-size: 15px;
  font-weight: 600;
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  display: flex;
  gap: 8px;
  align-items: center;
  transition: all 0.3s ease-in-out;
  letter-spacing: 0.01em;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}
.login-btn:hover {
  background: #27272a;
  border-color: #27272a;
  transform: translateY(-0.5px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.el-dropdown-menu {
  margin-top: 12px;
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 
               0 2px 4px -2px rgba(0, 0, 0, 0.05);
}
.el-dropdown-menu__item {
  font-size: 15px;
  padding: 14px 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s ease-in-out;
  color: #71717a;
  border-radius: 8px;
  margin: 4px 8px;
}
.el-dropdown-menu__item:hover {
  background: #fafafa;
  color: #18181b;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}
.el-dropdown-menu__item .el-icon {
  font-size: 18px;
  transition: all 0.3s ease-in-out;
}
.el-dropdown-menu__item:hover .el-icon {
  color: #18181b;
}
</style>