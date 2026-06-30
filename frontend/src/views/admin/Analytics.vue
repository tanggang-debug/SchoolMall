<template>
  <div class="page">
    <div class="card">
      <h2>统计报表</h2>
      <el-descriptions :column="3" border style="margin-bottom:20px">
        <el-descriptions-item label="活跃用户">{{ activity.activeUsers || 0 }}</el-descriptions-item>
        <el-descriptions-item label="新用户">{{ activity.newUsers || 0 }}</el-descriptions-item>
        <el-descriptions-item label="今日订单">{{ activity.orderCount || 0 }}</el-descriptions-item>
      </el-descriptions>
      <h3>热门商品</h3>
      <el-table :data="hotProducts">
        <el-table-column prop="title" label="商品" />
        <el-table-column prop="salesCount" label="销量" width="100" />
      </el-table>
    </div>
    <div class="card" style="margin-top: 20px">
      <div class="toolbar">
        <h3>销售趋势</h3>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="YYYY-MM-DD"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="loadSales"
        />
      </div>
      <el-table :data="sales">
        <el-table-column prop="date" label="日期" />
        <el-table-column label="销售额" width="160">
          <template #default="{ row }">
            ¥{{ formatCurrency(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="订单数" width="120" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { formatCurrency } from '../../utils/view'

const activity = ref({})
const hotProducts = ref([])
const sales = ref([])
const dateRange = ref([])

async function loadSales() {
  const [start, end] = dateRange.value || []
  const res = await request.get('/analytics/sales', { params: { start, end } })
  sales.value = res.data || []
}

onMounted(async () => {
  const [a, h] = await Promise.all([
    request.get('/analytics/user-activity'),
    request.get('/analytics/hot-products', { params: { limit: 10 } })
  ])
  activity.value = a.data
  hotProducts.value = h.data || []
  await loadSales()
})
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
h2, h3 { margin: 0 0 16px; }
</style>
