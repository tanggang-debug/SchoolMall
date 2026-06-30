<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>我的订单</h2>
        <el-select v-model="status" placeholder="状态" clearable @change="load" style="width: 180px">
          <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </div>
      <el-table v-if="orders.length" :data="orders" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="220" />
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="金额" width="120">
          <template #default="{ row }">
            ¥{{ formatCurrency(row.payAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getOrderStatusMeta(row.status, row.statusLabel).type">
              {{ getOrderStatusMeta(row.status, row.statusLabel).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="260">
          <template #default="{ row }">
            <el-button link @click="$router.push(`/orders/${row.id}`)">详情</el-button>
            <el-button v-if="row.status === 0" link type="primary" @click="$router.push(`/payment/${row.orderNo}`)">去支付</el-button>
            <el-button v-if="row.status === 0" link @click="cancelOrder(row)">取消</el-button>
            <el-button v-if="row.status === 3" link type="success" @click="confirmOrder(row)">确认收货</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无订单记录" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { formatCurrency, getOrderStatusMeta, ORDER_STATUS_MAP } from '../utils/view'

const orders = ref([])
const loading = ref(false)
const status = ref(null)
const statusList = Object.entries(ORDER_STATUS_MAP).map(([value, meta]) => ({
  value: Number(value),
  label: meta.label
}))

async function load() {
  loading.value = true
  try {
    const res = await request.get('/orders', { params: { page: 1, size: 20, status: status.value } })
    orders.value = res.data.records || []
  } finally {
    loading.value = false
  }
}

async function cancelOrder(row) {
  await ElMessageBox.confirm(`确认取消订单 ${row.orderNo} 吗？`, '提示', { type: 'warning' })
  await request.put(`/orders/${row.id}/cancel`)
  ElMessage.success('订单已取消')
  await load()
}

async function confirmOrder(row) {
  await request.put(`/orders/${row.id}/confirm`)
  ElMessage.success('已确认收货')
  await load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
h2 { margin: 0; }
</style>
