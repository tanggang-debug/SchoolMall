<template>
  <div class="page home-page">
    <div class="hero-banner card">
      <div class="hero-content">
        <h2>发现校园好物</h2>
        <p>为你精选优质二手商品与校园周边</p>
        <div class="search-bar">
          <el-input 
            v-model="keyword" 
            placeholder="搜索你想要的商品..." 
            clearable 
            size="large"
            :prefix-icon="Search"
            @keyup.enter="load" 
            class="search-input"
          >
            <template #append>
              <el-button type="primary" @click="load" class="search-btn">搜索</el-button>
            </template>
          </el-input>
        </div>
      </div>
      <div class="hero-image">
        <el-icon class="hero-icon"><ShoppingBag /></el-icon>
      </div>
    </div>

    <div class="category-filter">
      <el-radio-group v-model="categoryId" size="large" @change="load">
        <el-radio-button :value="null">全部商品</el-radio-button>
        <el-radio-button v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</el-radio-button>
      </el-radio-group>
    </div>

    <div class="product-grid" v-loading="loading">
      <div v-for="p in products" :key="p.id" class="product-card card" @click="$router.push(`/product/${p.id}`)">
        <div class="image-wrapper">
          <img :src="firstImage(p.images)" alt="" />
          <div class="hover-overlay">
            <el-button type="primary" circle :icon="View" />
          </div>
        </div>
        <div class="product-info">
          <h3 class="product-title" :title="p.title">{{ p.title }}</h3>
          <div class="product-meta">
            <span class="price"><span class="currency">¥</span>{{ formatCurrency(p.price) }}</span>
            <span class="sales">已售 {{ p.salesCount || 0 }}</span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="empty-wrapper" v-if="!loading && !products.length">
      <el-empty description="没有找到相关的商品" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { formatCurrency } from '../utils/view'
import { Search, ShoppingBag, View } from '@element-plus/icons-vue'

const keyword = ref('')
const categoryId = ref(null)
const categories = ref([])
const products = ref([])
const loading = ref(false)

function firstImage(images) {
  if (!images) return 'https://picsum.photos/300/300'
  try { const arr = JSON.parse(images); return arr[0] || 'https://picsum.photos/300/300' } catch { return images }
}

async function loadCategories() {
  const res = await request.get('/categories')
  categories.value = res.data
}

async function load() {
  loading.value = true
  try {
    const res = await request.get('/search', { params: { keyword: keyword.value, categoryId: categoryId.value, page: 1, size: 20 } })
    products.value = res.data.records || res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => { await loadCategories(); await load() })
</script>

<style scoped>
.home-page { padding-top: 10px; }

.hero-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 40px 60px;
  background: linear-gradient(120deg, #e0c3fc 0%, #8ec5fc 100%);
  color: #fff;
  border-radius: 16px;
  margin-bottom: 32px;
  border: none;
  box-shadow: 0 10px 30px rgba(142, 197, 252, 0.2);
}
.hero-content {
  flex: 1;
  max-width: 600px;
}
.hero-content h2 {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 12px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.hero-content p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 28px;
}
.search-bar {
  display: flex;
  gap: 12px;
}
.search-input {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border-radius: 8px;
  overflow: hidden;
}
.search-input :deep(.el-input-group__append) {
  background-color: #409eff;
  border: none;
  color: white;
}
.search-btn {
  font-weight: 600;
  letter-spacing: 2px;
}
.hero-image {
  font-size: 160px;
  opacity: 0.8;
  transform: rotate(15deg);
  text-shadow: 0 10px 20px rgba(0,0,0,0.1);
}

.category-filter {
  margin-bottom: 24px;
  display: flex;
  justify-content: center;
}

.product-grid { 
  display: grid; 
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); 
  gap: 24px; 
}
.product-card { 
  padding: 0;
  overflow: hidden;
  cursor: pointer; 
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1); 
  border: 1px solid transparent;
}
.product-card:hover { 
  transform: translateY(-8px); 
  box-shadow: 0 12px 24px rgba(0,0,0,0.1);
  border-color: rgba(64, 158, 255, 0.1);
}

.image-wrapper {
  position: relative;
  width: 100%;
  padding-top: 100%; /* 1:1 Aspect Ratio */
  overflow: hidden;
  background: #f5f7fa;
}
.image-wrapper img { 
  position: absolute;
  top: 0;
  left: 0;
  width: 100%; 
  height: 100%; 
  object-fit: cover; 
  transition: transform 0.5s ease;
}
.product-card:hover .image-wrapper img {
  transform: scale(1.05);
}

.hover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}
.product-card:hover .hover-overlay {
  opacity: 1;
}

.product-info {
  padding: 16px;
}
.product-title { 
  margin: 0 0 12px; 
  font-size: 16px; 
  color: #303133;
  font-weight: 600;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.product-meta {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}
.price { 
  color: #f56c6c; 
  font-size: 22px; 
  font-weight: 800; 
  line-height: 1;
}
.currency {
  font-size: 14px;
  margin-right: 2px;
}
.sales { 
  color: #909399; 
  font-size: 13px; 
}
.empty-wrapper {
  padding: 60px 0;
  background: #fff;
  border-radius: 12px;
}
</style>