<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>商品管理</h2>
        <div class="toolbar-actions">
          <el-input v-model="keyword" placeholder="搜索商品标题" clearable style="width: 220px" @keyup.enter="load" />
          <el-button @click="load">查询</el-button>
          <el-button type="primary" @click="openCreate">发布商品</el-button>
        </div>
      </div>
      <el-table :data="products" v-loading="loading">
        <el-table-column prop="title" label="标题" />
        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            ¥{{ formatCurrency(row.price) }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getProductStatusMeta(row.status, row.statusLabel).type">
              {{ getProductStatusMeta(row.status, row.statusLabel).label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesCount" label="销量" width="100" />
        <el-table-column label="操作" min-width="260">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 1" link @click="toggleShelf(row, false)">下架</el-button>
            <el-button v-if="row.status === 2" link type="success" @click="toggleShelf(row, true)">上架</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="showAdd" :title="editingId ? '编辑商品' : '发布商品'" width="560px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类"><el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" /></el-select></el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="价格"><el-input-number v-model="form.price" :min="0" /></el-form-item>
        <el-form-item label="原价"><el-input-number v-model="form.originalPrice" :min="0" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="图片"><el-input v-model="imageText" placeholder="支持单个链接或多个链接，使用英文逗号分隔" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAdd = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submit">{{ editingId ? '保存' : '提交' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { formatCurrency, getProductStatusMeta, parseImageList } from '../../utils/view'

const products = ref([])
const categories = ref([])
const showAdd = ref(false)
const loading = ref(false)
const submitting = ref(false)
const keyword = ref('')
const editingId = ref(null)
const imageText = ref('https://picsum.photos/400/400')
const form = ref({ categoryId: 1, title: '', description: '', price: 0, originalPrice: 0, stock: 0, images: '' })

function resetForm() {
  editingId.value = null
  imageText.value = 'https://picsum.photos/400/400'
  form.value = { categoryId: categories.value[0]?.id || 1, title: '', description: '', price: 0, originalPrice: 0, stock: 0, images: '' }
}

async function load() {
  loading.value = true
  try {
    const res = await request.get('/products', { params: { page: 1, size: 50, keyword: keyword.value } })
    products.value = res.data.records || []
  } finally {
    loading.value = false
  }
}

async function loadCategories() {
  const res = await request.get('/categories')
  categories.value = res.data
  if (!form.value.categoryId && categories.value.length) {
    form.value.categoryId = categories.value[0].id
  }
}

function openCreate() {
  resetForm()
  showAdd.value = true
}

function openEdit(row) {
  editingId.value = row.id
  form.value = {
    categoryId: row.categoryId,
    title: row.title,
    description: row.description,
    price: Number(row.price),
    originalPrice: Number(row.originalPrice || row.price),
    stock: row.stock,
    images: row.images
  }
  imageText.value = parseImageList(row.images).join(', ')
  showAdd.value = true
}

async function submit() {
  if (!form.value.title || !form.value.categoryId) {
    ElMessage.warning('请填写完整的商品信息')
    return
  }
  submitting.value = true
  try {
    const payload = {
      ...form.value,
      images: JSON.stringify(parseImageList(imageText.value))
    }
    if (editingId.value) {
      await request.put(`/products/${editingId.value}`, payload)
      ElMessage.success('商品已更新')
    } else {
      await request.post('/products', payload)
      ElMessage.success('已提交审核')
    }
    showAdd.value = false
    await load()
  } finally {
    submitting.value = false
  }
}

async function toggleShelf(row, online) {
  await request.put(`/products/${row.id}/${online ? 'on-shelf' : 'off-shelf'}`)
  ElMessage.success(online ? '商品已上架' : '商品已下架')
  await load()
}

async function remove(row) {
  await ElMessageBox.confirm(`确认删除商品“${row.title}”吗？`, '提示', { type: 'warning' })
  await request.delete(`/products/${row.id}`)
  ElMessage.success('商品已删除')
  await load()
}

onMounted(async () => {
  await loadCategories()
  resetForm()
  showAdd.value = false
  await load()
})
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.toolbar-actions { display: flex; gap: 12px; align-items: center; }
</style>
