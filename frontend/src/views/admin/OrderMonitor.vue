<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>订单监控</h2>
        <el-select v-model="status" placeholder="全部状态" clearable style="width: 180px" @change="load">
          <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </div>
      <el-table :data="orders" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="userId" label="用户ID" width="100" />
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
        <el-table-column prop="createTime" label="下单时间" width="180" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { formatCurrency, getOrderStatusMeta, ORDER_STATUS_MAP } from '../../utils/view'
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
    const res = await request.get('/orders', { params: { page: 1, size: 50, status: status.value } })
    orders.value = res.data.records || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
h2 { margin: 0; }
</style>
