<template>
  <div class="page">
    <div class="card summary-card">
      <h2>店铺统计</h2>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="总销售额">¥{{ formatCurrency(data.totalSales) }}</el-descriptions-item>
        <el-descriptions-item label="订单数">{{ data.orderCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="商品数">{{ data.productCount || 0 }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div class="card" style="margin-top: 20px">
      <div class="toolbar">
        <h3>销售报表</h3>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="YYYY-MM-DD"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="loadSales"
        />
      </div>
      <el-table :data="sales" v-loading="loadingSales">
        <el-table-column prop="date" label="日期" />
        <el-table-column label="销售额" width="140">
          <template #default="{ row }">
            ¥{{ formatCurrency(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="订单数" width="120" />
      </el-table>
    </div>
    <div class="card" style="margin-top: 20px">
      <div class="toolbar">
        <h3>店铺热销商品</h3>
        <el-button link @click="loadHotProducts">刷新</el-button>
      </div>
      <el-table :data="hotProducts" v-loading="loadingHot">
        <el-table-column prop="title" label="商品" />
        <el-table-column prop="salesCount" label="销量" width="120" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import request from '../../utils/request'
import { formatCurrency } from '../../utils/view'

const user = useUserStore()
const data = ref({})
const sales = ref([])
const hotProducts = ref([])
const dateRange = ref([])
const loadingSales = ref(false)
const loadingHot = ref(false)

async function loadSummary() {
  const res = await request.get(`/analytics/merchant/${user.userId}`)
  data.value = res.data
}

async function loadSales() {
  loadingSales.value = true
  try {
    const [start, end] = dateRange.value || []
    const res = await request.get('/analytics/sales', { params: { start, end } })
    sales.value = res.data || []
  } finally {
    loadingSales.value = false
  }
}

async function loadHotProducts() {
  loadingHot.value = true
  try {
    const res = await request.get('/analytics/hot-products', { params: { limit: 10 } })
    hotProducts.value = res.data || []
  } finally {
    loadingHot.value = false
  }
}

onMounted(async () => {
  await Promise.all([loadSummary(), loadSales(), loadHotProducts()])
})
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.summary-card h2, .toolbar h3 { margin: 0; }
</style>
