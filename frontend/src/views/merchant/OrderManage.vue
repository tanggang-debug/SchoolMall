<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>订单管理</h2>
        <div class="toolbar-actions">
          <el-select v-model="status" placeholder="订单状态" clearable style="width: 180px" @change="load">
            <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-button @click="load">刷新</el-button>
        </div>
      </div>
      <el-table :data="orders" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" min-width="220" />
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
        <el-table-column prop="logisticsNo" label="物流单号" min-width="180" />
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button v-if="row.status === 2" link type="primary" @click="ship(row)">发货</el-button>
            <el-button v-if="row.status === 3" link type="success" disabled>已发货</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { formatCurrency, getOrderStatusMeta, ORDER_STATUS_MAP } from '../../utils/view'

const orders = ref([])
const loading = ref(false)
const status = ref(2)
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

async function ship(row) {
  const { value } = await ElMessageBox.prompt('请输入物流单号', '发货', {
    inputPattern: /\S+/,
    inputErrorMessage: '物流单号不能为空'
  })
  await request.put(`/orders/${row.id}/ship`, { logisticsNo: value })
  ElMessage.success('发货成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.toolbar-actions { display: flex; gap: 12px; align-items: center; }
h2 { margin: 0; }
</style>
