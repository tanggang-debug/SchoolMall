<template>
  <div class="page">
    <div class="profile-layout">
      <!-- 侧边栏：用户信息 -->
      <div class="card user-sidebar">
        <div class="avatar-wrap">
          <el-avatar :size="80" style="background: #409eff; font-size: 32px">
            {{ user.username.charAt(0).toUpperCase() }}
          </el-avatar>
        </div>
        <h2 class="username">{{ user.username }}</h2>
        <div class="user-meta">
          <el-tag size="small" effect="dark" round class="role-tag">{{ roleLabel }}</el-tag>
          <span class="user-id">ID: {{ user.userId }}</span>
        </div>
        
        <el-divider />
        
        <div class="sidebar-menu">
          <a class="menu-item active">
            <el-icon><User /></el-icon>
            个人资料
          </a>
          <a class="menu-item" @click="$router.push('/orders')">
            <el-icon><List /></el-icon>
            我的订单
          </a>
          <a class="menu-item" @click="$router.push('/notifications')">
            <el-icon><Bell /></el-icon>
            消息通知
          </a>
        </div>
      </div>

      <!-- 主内容：表单修改 -->
      <div class="card main-content" v-loading="loading">
        <div class="section-head">
          <h3>个人资料</h3>
        </div>
        
        <el-form :model="form" label-width="100px" class="profile-form">
          <el-form-item label="用户名">
            <el-input v-model="user.username" disabled />
            <div class="tip">用户名不可修改</div>
          </el-form-item>
          
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号码" />
          </el-form-item>
          
          <el-form-item label="电子邮箱">
            <el-input v-model="form.email" placeholder="请输入电子邮箱" />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="saveProfile" :loading="submitting">保存修改</el-button>
          </el-form-item>
        </el-form>

        <el-divider style="margin: 40px 0 30px" />
        
        <div class="section-head">
          <h3>我的收货地址</h3>
          <el-button type="primary" plain @click="showAddressDialog = true">新增地址</el-button>
        </div>
        
        <el-table :data="addresses" class="address-table" :header-cell-style="{ background: '#f5f7fa' }">
          <el-table-column prop="receiverName" label="收货人" width="120" />
          <el-table-column prop="phone" label="手机号" width="140" />
          <el-table-column label="地址" min-width="260">
            <template #default="{ row }">
              {{ formatAddress(row) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.isDefault" size="small" type="success">默认</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template #default="{ row }">
              <el-button v-if="!row.isDefault" link type="primary" @click="setDefault(row)">设为默认</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 新增地址弹窗 -->
    <el-dialog v-model="showAddressDialog" title="新增收货地址" width="520px" destroy-on-close>
      <el-form :model="addressForm" label-width="88px">
        <el-form-item label="收货人"><el-input v-model="addressForm.receiverName" placeholder="姓名" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="addressForm.phone" placeholder="11位手机号码" /></el-form-item>
        <el-form-item label="省份"><el-input v-model="addressForm.province" placeholder="如：浙江省" /></el-form-item>
        <el-form-item label="城市"><el-input v-model="addressForm.city" placeholder="如：杭州市" /></el-form-item>
        <el-form-item label="区县"><el-input v-model="addressForm.district" placeholder="如：西湖区" /></el-form-item>
        <el-form-item label="详细地址"><el-input v-model="addressForm.detail" type="textarea" :rows="3" placeholder="街道门牌、楼层房间号等信息" /></el-form-item>
        <el-form-item label="默认地址"><el-switch v-model="addressForm.isDefault" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" :loading="addressSubmitting" @click="createAddress">保存地址</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { getRoleLabel, formatAddress } from '../utils/view'
import { User, List, Bell } from '@element-plus/icons-vue'

const user = useUserStore()
const roleLabel = computed(() => getRoleLabel(user.role))

const loading = ref(false)
const submitting = ref(false)
const form = ref({ phone: '', email: '' })

const addresses = ref([])
const showAddressDialog = ref(false)
const addressSubmitting = ref(false)
const addressForm = ref({
  receiverName: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false
})

async function loadProfile() {
  loading.value = true
  try {
    const res = await request.get(`/users/${user.userId}`)
    if (res.data) {
      form.value.phone = res.data.phone || ''
      form.value.email = res.data.email || ''
    }
    await loadAddresses()
  } finally {
    loading.value = false
  }
}

async function saveProfile() {
  submitting.value = true
  try {
    await request.put(`/users/${user.userId}`, {
      phone: form.value.phone,
      email: form.value.email
    })
    ElMessage.success('资料已更新')
  } finally {
    submitting.value = false
  }
}

async function loadAddresses() {
  const res = await request.get(`/users/${user.userId}/addresses`)
  addresses.value = res.data || []
}

async function setDefault(address) {
  await request.put(`/users/${user.userId}/addresses/${address.id}/default`)
  ElMessage.success('默认地址已更新')
  await loadAddresses()
}

async function createAddress() {
  if (!addressForm.value.receiverName || !addressForm.value.phone || !addressForm.value.detail) {
    return ElMessage.warning('请填写完整的收货信息')
  }
  addressSubmitting.value = true
  try {
    await request.post(`/users/${user.userId}/addresses`, addressForm.value)
    ElMessage.success('地址已保存')
    showAddressDialog.value = false
    addressForm.value = { receiverName: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false }
    await loadAddresses()
  } finally {
    addressSubmitting.value = false
  }
}

onMounted(loadProfile)
</script>

<style scoped>
.profile-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
}

.user-sidebar {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 24px;
}
.avatar-wrap {
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
  border-radius: 50%;
}
.username {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 12px;
  color: #303133;
}
.user-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-id {
  font-size: 13px;
  color: #909399;
}

.sidebar-menu {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  border-radius: 8px;
  color: #606266;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.2s;
}
.menu-item:hover {
  background: #f5f7fa;
  color: #409eff;
}
.menu-item.active {
  background: #ecf5ff;
  color: #409eff;
  font-weight: 600;
}

.main-content {
  padding: 32px 40px;
}
.section-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.section-head h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
  position: relative;
  padding-left: 12px;
}
.section-head h3::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background: #409eff;
  border-radius: 2px;
}

.profile-form {
  max-width: 500px;
}
.tip {
  font-size: 12px;
  color: #909399;
  margin-left: 12px;
}

.address-table {
  width: 100%;
}
</style>