<template>
  <div class="page" v-loading="loading">
    <div v-if="order" class="detail-layout">
      <div class="card">
        <div class="head">
          <div>
            <h2>订单详情</h2>
            <p class="sub">订单号：{{ order.orderNo }}</p>
          </div>
          <el-tag :type="statusMeta.type" size="large">{{ statusMeta.label }}</el-tag>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="下单时间">{{ order.createTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="实付金额">¥{{ formatCurrency(order.payAmount) }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ order.addressSnapshot || '-' }}</el-descriptions-item>
          <el-descriptions-item label="订单备注" :span="2">{{ order.remark || '无' }}</el-descriptions-item>
          <el-descriptions-item label="物流单号" :span="2">{{ order.logisticsNo || '暂无' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="card">
        <h3>商品信息</h3>
        <el-table :data="order.items || []" style="margin-top: 16px">
          <el-table-column prop="productTitle" label="商品" min-width="220" />
          <el-table-column label="单价" width="120">
            <template #default="{ row }">
              ¥{{ formatCurrency(row.price) }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button v-if="order.status === 4" link @click="goReview(row)">去评价</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="card action-bar">
        <el-button v-if="order.status === 0" type="primary" @click="$router.push(`/payment/${order.orderNo}`)">去支付</el-button>
        <el-button v-if="order.status === 0" @click="cancel">取消订单</el-button>
        <el-button v-if="order.status === 3" type="success" @click="confirm">确认收货</el-button>
        <el-button v-if="[1, 2, 3].includes(order.status)" type="danger" plain @click="refund">申请退款</el-button>
      </div>
    </div>
    <el-empty v-else description="订单不存在" />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { formatCurrency, getOrderStatusMeta } from '../utils/view'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

const statusMeta = computed(() => getOrderStatusMeta(order.value?.status, order.value?.statusLabel))

async function load() {
  loading.value = true
  try {
    const res = await request.get(`/orders/${route.params.id}`)
    order.value = res.data
  } finally {
    loading.value = false
  }
}

async function cancel() {
  await request.put(`/orders/${route.params.id}/cancel`)
  ElMessage.success('订单已取消')
  await load()
}

async function confirm() {
  await request.put(`/orders/${route.params.id}/confirm`)
  ElMessage.success('已确认收货')
  await load()
}

async function refund() {
  const { value } = await ElMessageBox.prompt('请输入退款原因（可选）', '申请退款', {
    inputValue: '',
    confirmButtonText: '提交',
    cancelButtonText: '取消'
  })
  await request.post(`/orders/${route.params.id}/refund`, { reason: value })
  ElMessage.success('退款申请已提交')
  await load()
}

function goReview(item) {
  router.push(`/review/${order.value.id}/${item.productId}`)
}

onMounted(load)
</script>

<style scoped>
.detail-layout { display: grid; gap: 20px; }
.head, .action-bar { display: flex; align-items: center; justify-content: space-between; }
.head { margin-bottom: 16px; }
.sub { margin-top: 8px; color: #909399; }
.action-bar { justify-content: flex-end; }
h2, h3 { margin: 0; }
</style>
