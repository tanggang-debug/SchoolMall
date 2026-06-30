<template>
  <div class="page">
    <div class="card pay-card">
      <h2>订单支付</h2>
      <p>订单号：{{ orderNo }}</p>
      <p class="amount" v-if="payment">应付：¥{{ payment.amount }}</p>
      <div class="qr" v-if="payment">模拟支付二维码：{{ payment.qrCode }}</div>
      <el-button type="success" size="large" :loading="loading" @click="mockPay">模拟支付成功</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const orderNo = route.params.orderNo
const payment = ref(null)
const loading = ref(false)

async function createPayment() {
  const res = await request.post('/payments', { orderNo, channel: 1 })
  payment.value = res.data
}

async function mockPay() {
  loading.value = true
  try {
    await request.post(`/payments/${payment.value.id}/mock-pay`)
    ElMessage.success('支付成功')
    router.push('/orders')
  } finally { loading.value = false }
}

onMounted(createPayment)
</script>

<style scoped>
.pay-card { text-align: center; max-width: 480px; margin: 40px auto; }
.amount { font-size: 24px; color: #f56c6c; margin: 16px 0; }
.qr { background: #f5f7fa; padding: 16px; border-radius: 8px; margin: 16px 0; word-break: break-all; }
</style>
