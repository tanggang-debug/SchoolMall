<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>商品审核</h2>
        <el-button link @click="load">刷新</el-button>
      </div>
      <el-table :data="products" v-loading="loading">
        <el-table-column prop="title" label="商品" />
        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            ¥{{ formatCurrency(row.price) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="240" show-overflow-tooltip />
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button link type="success" @click="audit(row, true)">通过</el-button>
            <el-button link type="danger" @click="audit(row, false)">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatCurrency } from '../../utils/view'

const products = ref([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const res = await request.get('/products', { params: { page: 1, size: 50, status: 0 } })
    products.value = res.data.records || []
  } finally {
    loading.value = false
  }
}

async function audit(row, approved) {
  let rejectReason = ''
  if (!approved) {
    const { value } = await ElMessageBox.prompt('请输入驳回原因', '驳回商品', {
      inputValue: '不符合规范',
      confirmButtonText: '提交',
      cancelButtonText: '取消'
    })
    rejectReason = value
  }
  await request.put(`/products/${row.id}/audit`, { approved, rejectReason })
  ElMessage.success('审核完成')
  await load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
h2 { margin: 0; }
</style>
