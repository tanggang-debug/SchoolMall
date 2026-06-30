<template>
  <div class="page">
    <div class="card">
      <h2>发表评价</h2>
      <el-rate v-model="form.rating" />
      <el-input v-model="form.content" type="textarea" rows="4" placeholder="评价内容" style="margin:16px 0" />
      <el-button type="primary" @click="submit">提交评价</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const form = ref({ rating: 5, content: '' })

async function submit() {
  await request.post('/reviews', {
    orderId: Number(route.params.orderId),
    productId: Number(route.params.productId),
    rating: form.value.rating,
    content: form.value.content
  })
  ElMessage.success('评价成功')
  router.push('/orders')
}
</script>
