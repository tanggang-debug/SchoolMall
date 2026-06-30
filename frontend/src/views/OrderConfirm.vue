<template>
  <div class="page" v-loading="pageLoading">
    <div class="confirm-layout">
      <div class="card block-card">
        <div class="section-head">
          <h2>选择收货地址</h2>
          <el-button type="primary" plain @click="showAddressDialog = true">新增地址</el-button>
        </div>
        
        <div v-if="addresses.length" class="address-list">
          <label
            v-for="address in addresses"
            :key="address.id"
            class="address-card"
            :class="{ active: addressId === address.id }"
          >
            <input v-model="addressId" type="radio" :value="address.id" class="hidden-radio" />
            <div class="address-top">
              <strong class="name">{{ address.receiverName }}</strong>
              <span class="phone">{{ address.phone }}</span>
              <el-tag v-if="address.isDefault" size="small" type="success" effect="dark" class="default-tag">默认</el-tag>
            </div>
            <p class="address-detail">{{ formatAddress(address) }}</p>
            <div class="address-actions">
              <el-button v-if="!address.isDefault" link type="primary" size="small" @click.stop="setDefault(address)">设为默认</el-button>
            </div>
            <div class="active-icon" v-if="addressId === address.id">
              <el-icon><Select /></el-icon>
            </div>
          </label>
        </div>
        <el-empty v-else description="暂无收货地址，请先新增地址" />
        
        <div class="remark-section">
          <h3>订单备注</h3>
          <el-input
            v-model="remark"
            type="textarea"
            :rows="3"
            maxlength="100"
            show-word-limit
            placeholder="给商家的备注留言（选填）"
            class="remark-input"
          />
        </div>
      </div>

      <div class="card block-card">
        <h3 class="section-title">商品清单</h3>
        <el-table 
          v-if="items.length" 
          :data="items"
          :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
        >
          <el-table-column label="商品" min-width="260">
            <template #default="{ row }">
              <div class="product-cell">
                <img :src="row.image || 'https://picsum.photos/120/120'" />
                <span class="product-title">{{ row.title }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" width="140" align="center">
            <template #default="{ row }">
              <span class="price">¥{{ formatCurrency(row.price) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" align="center" />
          <el-table-column label="小计" width="140" align="center">
            <template #default="{ row }">
              <span class="subtotal">¥{{ formatCurrency(row.price * row.quantity) }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else description="请先在购物车勾选要结算的商品" />
        
        <div class="submit-bar">
          <div class="amount-wrap">
            <span class="amount-label">实付款：</span>
            <span class="amount">¥{{ formatCurrency(totalAmount) }}</span>
          </div>
          <el-button type="primary" size="large" class="submit-btn" :loading="loading" :disabled="!items.length" @click="submit">提交订单</el-button>
        </div>
      </div>
    </div>

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
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import request from '../utils/request'
import { formatAddress, formatCurrency } from '../utils/view'
import { Select } from '@element-plus/icons-vue'

const router = useRouter()
const user = useUserStore()
const addresses = ref([])
const items = ref([])
const addressId = ref(null)
const remark = ref('')
const loading = ref(false)
const pageLoading = ref(false)
const showAddressDialog = ref(false)
const addressSubmitting = ref(false)
const addressForm = ref({
  receiverName: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detail: '',
  isDefault: false
})

const totalAmount = computed(() => items.value.reduce((sum, item) => sum + Number(item.price) * item.quantity, 0))

async function loadAddresses() {
  const res = await request.get(`/users/${user.userId}/addresses`)
  addresses.value = res.data || []
  const defaultAddress = addresses.value.find(item => item.isDefault)
  addressId.value = defaultAddress?.id || addresses.value[0]?.id || null
}

async function loadCart() {
  const res = await request.get('/cart')
  items.value = (res.data || []).filter(item => item.selected)
}

async function load() {
  pageLoading.value = true
  try {
    await Promise.all([loadAddresses(), loadCart()])
  } finally {
    pageLoading.value = false
  }
}

async function setDefault(address) {
  await request.put(`/users/${user.userId}/addresses/${address.id}/default`)
  ElMessage.success('默认地址已更新')
  await loadAddresses()
}

async function createAddress() {
  if (!addressForm.value.receiverName || !addressForm.value.phone || !addressForm.value.detail) {
    ElMessage.warning('请填写完整的收货信息')
    return
  }
  addressSubmitting.value = true
  try {
    await request.post(`/users/${user.userId}/addresses`, addressForm.value)
    ElMessage.success('地址已保存')
    showAddressDialog.value = false
    addressForm.value = {
      receiverName: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detail: '',
      isDefault: false
    }
    await loadAddresses()
  } finally {
    addressSubmitting.value = false
  }
}

async function submit() {
  if (!addressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }
  if (!items.value.length) {
    ElMessage.warning('请先选择要结算的商品')
    return
  }
  loading.value = true
  try {
    const res = await request.post('/orders', { addressId: addressId.value, remark: remark.value })
    const order = (res.data || [])[0]
    if (order) {
      router.push(`/payment/${order.orderNo}`)
      return
    }
    ElMessage.success('订单创建成功')
    router.push('/orders')
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.confirm-layout { display: grid; gap: 24px; }
.block-card { padding: 32px; }

.section-head { 
  display: flex; 
  align-items: center; 
  justify-content: space-between; 
  margin-bottom: 24px;
}
.section-head h2 { margin: 0; font-size: 20px; }
.section-title { margin: 0 0 20px; font-size: 18px; }

.address-list { 
  display: grid; 
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); 
  gap: 20px; 
}
.address-card { 
  position: relative; 
  display: flex;
  flex-direction: column;
  padding: 20px; 
  border: 2px solid #ebeef5; 
  border-radius: 12px; 
  cursor: pointer; 
  transition: all .2s; 
  background: #fafafa;
}
.address-card:hover { border-color: #c6e2ff; }
.address-card.active { 
  border-color: #409eff; 
  background: #fff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.address-top { 
  display: flex; 
  align-items: center; 
  gap: 12px; 
  margin-bottom: 12px; 
}
.name { font-size: 16px; color: #303133; }
.phone { color: #606266; font-size: 15px; }
.default-tag { transform: scale(0.9); transform-origin: left; }

.address-detail { 
  color: #606266; 
  line-height: 1.6; 
  font-size: 14px;
  margin: 0;
  flex: 1;
}

.address-actions {
  margin-top: 12px;
  height: 24px;
  display: flex;
  align-items: center;
}

.active-icon {
  position: absolute;
  right: -2px;
  bottom: -2px;
  width: 32px;
  height: 32px;
  background: #409eff;
  border-radius: 16px 0 10px 0;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.hidden-radio { display: none; }

.remark-section { margin-top: 32px; border-top: 1px dashed #ebeef5; padding-top: 24px; }
.remark-section h3 { margin: 0 0 16px; font-size: 16px; }
.remark-input { max-width: 600px; }

.product-cell { display: flex; align-items: center; gap: 16px; }
.product-cell img { width: 64px; height: 64px; border-radius: 6px; object-fit: cover; border: 1px solid #ebeef5; }
.product-title { color: #303133; font-weight: 500; }
.price { color: #303133; }
.subtotal { color: #f56c6c; font-weight: 600; font-size: 16px; }

.submit-bar { 
  margin-top: 32px; 
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 32px;
}
.amount-wrap { display: flex; align-items: baseline; }
.amount-label { color: #606266; font-size: 15px; }
.amount { font-size: 28px; color: #f56c6c; font-weight: 800; line-height: 1; }
.submit-btn { width: 160px; font-size: 16px; border-radius: 20px; }
</style>
