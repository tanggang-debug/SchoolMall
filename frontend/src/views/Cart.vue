<template>
  <div class="page">
    <div class="card cart-card">
      <div class="toolbar">
        <h2>我的购物车</h2>
        <el-button plain type="danger" :disabled="!items.length" @click="clearCart">
          <el-icon class="el-icon--left"><Delete /></el-icon>
          清空购物车
        </el-button>
      </div>
      
      <el-table 
        v-if="items.length" 
        :data="items" 
        v-loading="loading" 
        class="cart-table"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
      >
        <el-table-column label="选择" width="80" align="center">
          <template #default="{ row }">
            <el-checkbox :model-value="row.selected" size="large" @change="value => updateItem(row, { selected: value })" />
          </template>
        </el-table-column>
        <el-table-column label="商品信息" min-width="320">
          <template #default="{ row }">
            <div class="product-cell" @click="$router.push(`/product/${row.productId}`)">
              <img :src="row.image || defaultImage" />
              <div class="product-info">
                <div class="title">{{ row.title }}</div>
                <div class="stock" :class="{ 'low-stock': row.stock < 10 }">
                  库存 {{ row.stock }} 件
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="140" align="center">
          <template #default="{ row }">
            <span class="price">¥{{ formatCurrency(row.price) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="180" align="center">
          <template #default="{ row }">
            <el-input-number
              v-model="row.quantity"
              :min="1"
              :max="Math.max(row.stock || 1, 1)"
              size="small"
              @change="value => updateItem(row, { quantity: value })"
            />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="140" align="center">
          <template #default="{ row }">
            <span class="subtotal">¥{{ formatCurrency(row.price * row.quantity) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div v-else class="empty-cart">
        <el-empty description="购物车还是空的，先去逛逛吧" :image-size="160">
          <el-button type="primary" size="large" @click="$router.push('/')">去购物</el-button>
        </el-empty>
      </div>
      
      <div class="footer-bar" v-if="items.length">
        <div class="summary">
          <span class="selected-count">已选 <b>{{ selectedCount }}</b> 件商品</span>
          <div class="total">
            合计：<span class="total-price">¥{{ formatCurrency(totalAmount) }}</span>
          </div>
        </div>
        <el-button 
          type="primary" 
          size="large" 
          class="checkout-btn"
          :disabled="!selectedCount" 
          @click="$router.push('/order/confirm')"
        >
          去结算
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { formatCurrency } from '../utils/view'
import { Delete } from '@element-plus/icons-vue'

const defaultImage = 'https://picsum.photos/120/120'
const items = ref([])
const loading = ref(false)

const selectedItems = computed(() => items.value.filter(item => item.selected))
const selectedCount = computed(() => selectedItems.value.reduce((sum, item) => sum + item.quantity, 0))
const totalAmount = computed(() => selectedItems.value.reduce((sum, item) => sum + Number(item.price) * item.quantity, 0))

async function load() {
  loading.value = true
  try {
    const res = await request.get('/cart')
    items.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function updateItem(row, payload) {
  await request.put(`/cart/items/${row.productId}`, {
    quantity: payload.quantity ?? row.quantity,
    selected: payload.selected ?? row.selected
  })
  if (payload.selected !== undefined) {
    row.selected = payload.selected
  }
}

async function remove(row) {
  await request.delete(`/cart/items/${row.productId}`)
  ElMessage.success('已删除')
  await load()
}

async function clearCart() {
  await ElMessageBox.confirm('确定清空购物车中的所有商品吗？', '提示', { type: 'warning' })
  await request.delete('/cart/clear')
  ElMessage.success('购物车已清空')
  await load()
}

onMounted(load)
</script>

<style scoped>
.cart-card {
  padding: 0;
  overflow: hidden;
}
.toolbar { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  padding: 24px;
  border-bottom: 1px solid #ebeef5;
}
.toolbar h2 { margin: 0; font-size: 20px; }

.cart-table {
  width: 100%;
}
.cart-table :deep(.el-table__inner-wrapper::before) {
  display: none;
}

.product-cell { 
  display: flex; 
  align-items: center; 
  gap: 16px; 
  cursor: pointer;
}
.product-cell img { 
  width: 80px; 
  height: 80px; 
  border-radius: 8px; 
  object-fit: cover; 
  border: 1px solid #ebeef5;
}
.product-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.title { 
  font-weight: 500; 
  color: #303133;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.title:hover { color: #409eff; }
.stock { color: #909399; font-size: 13px; }
.low-stock { color: #e6a23c; }

.price { color: #303133; font-weight: 500; }
.subtotal { color: #f56c6c; font-weight: 600; font-size: 16px; }

.empty-cart {
  padding: 60px 0;
}

.footer-bar { 
  display: flex; 
  justify-content: flex-end; 
  align-items: center; 
  padding: 20px 24px;
  background: #f8f9fa;
  border-top: 1px solid #ebeef5;
  gap: 32px;
}
.summary { 
  display: flex; 
  align-items: center; 
  gap: 24px; 
}
.selected-count { color: #606266; font-size: 15px; }
.selected-count b { color: #409eff; margin: 0 4px; }
.total { color: #303133; font-size: 15px; }
.total-price { 
  font-size: 24px; 
  color: #f56c6c; 
  font-weight: 700; 
}
.checkout-btn {
  width: 140px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 20px;
}
</style>
