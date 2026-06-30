<template>
  <div class="page home-page">
    <div class="hero-banner">
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
      <div class="hero-stats">
        <div class="stat-item">
          <el-icon class="stat-icon"><Goods /></el-icon>
          <div class="stat-content">
            <span class="stat-value">1,200+</span>
            <span class="stat-label">优质商品</span>
          </div>
        </div>
        <div class="stat-item">
          <el-icon class="stat-icon"><User /></el-icon>
          <div class="stat-content">
            <span class="stat-value">3,500+</span>
            <span class="stat-label">活跃用户</span>
          </div>
        </div>
        <div class="stat-item">
          <el-icon class="stat-icon"><ShoppingCart /></el-icon>
          <div class="stat-content">
            <span class="stat-value">8,900+</span>
            <span class="stat-label">成交订单</span>
          </div>
        </div>
      </div>
    </div>

    <div class="category-filter">
      <h3>商品分类</h3>
      <el-radio-group v-model="categoryId" size="large" @change="load" class="category-radio-group">
        <el-radio-button :value="null">全部商品</el-radio-button>
        <el-radio-button v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</el-radio-button>
      </el-radio-group>
    </div>

    <div class="product-grid" v-loading="loading">
      <div v-for="p in products" :key="p.id" class="product-card" @click="$router.push(`/product/${p.id}`)">
        <div class="image-wrapper">
          <img :src="firstImage(p.images)" alt="" />
          <div class="hover-overlay">
            <el-button type="primary" circle :icon="View" size="large" />
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
      <el-empty description="没有找到相关的商品" :image-size="200" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { formatCurrency } from '../utils/view'
import { Search, Goods, User, ShoppingCart, View } from '@element-plus/icons-vue'

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
.home-page { 
  padding-top: 40px; 
}

.hero-banner {
  display: flex;
  flex-direction: column;
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  padding: 60px 50px;
  color: #18181b;
  border-radius: 20px;
  margin-bottom: 50px;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04), 
               0 1px 2px -1px rgba(0, 0, 0, 0.04);
}

.hero-content {
  text-align: center;
  margin-bottom: 40px;
}
.hero-content h2 {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 20px;
  letter-spacing: -0.025em;
  background: linear-gradient(135deg, #18181b 0%, #52525b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.hero-content p {
  font-size: 18px;
  color: #71717a;
  margin-bottom: 32px;
  font-weight: 400;
}

.search-bar {
  max-width: 700px;
  margin: 0 auto;
}
.search-input {
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
  border-radius: 12px;
  overflow: hidden;
  font-size: 16px;
  border: 1px solid rgba(228, 228, 231, 0.8);
  transition: all 0.3s ease-in-out;
}
.search-input:hover,
.search-input:focus-within {
  border-color: rgba(228, 228, 231, 0.9);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.05);
}
.search-input :deep(.el-input__wrapper) {
  background: #ffffff;
  padding: 14px 18px;
  font-size: 16px;
  border: none;
  box-shadow: none;
}
.search-input :deep(.el-input__inner) {
  color: #18181b;
  font-weight: 500;
}
.search-input :deep(.el-input__inner::placeholder) {
  color: #a1a1aa;
}
.search-input :deep(.el-input-group__append) {
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  padding: 0 20px;
}
.search-btn {
  font-weight: 600;
  font-size: 16px;
  letter-spacing: 0.01em;
  padding: 14px 30px;
  background: transparent;
  border: none;
  color: #ffffff;
  transition: all 0.3s ease-in-out;
}
.search-btn:hover {
  transform: translateY(-0.5px);
}

.hero-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-top: 40px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 20px;
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  padding: 24px 32px;
  border-radius: 16px;
  transition: all 0.3s ease-in-out;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.02);
}
.stat-item:hover {
  transform: translateY(-0.5px);
  background: #fafafa;
  border-color: rgba(228, 228, 231, 0.9);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04);
}

.stat-icon {
  font-size: 40px;
  color: #18181b;
  transition: all 0.3s ease-in-out;
}
.stat-item:hover .stat-icon {
  color: #3f3f46;
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #18181b;
  letter-spacing: -0.02em;
}

.stat-label {
  font-size: 14px;
  color: #71717a;
  font-weight: 500;
  letter-spacing: 0.01em;
}

.category-filter {
  margin-bottom: 40px;
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  padding: 32px 40px;
  border-radius: 16px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.02);
}

.category-filter h3 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 20px;
  letter-spacing: -0.01em;
}

.category-radio-group {
  display: flex;
  gap: 12px;
}

.product-grid { 
  display: grid; 
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); 
  gap: 24px; 
  margin-bottom: 40px;
}

.product-card { 
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer; 
  transition: all 0.3s ease-in-out;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.02);
}
.product-card:hover { 
  transform: translateY(-1px);
  border-color: rgba(228, 228, 231, 0.9);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 
               0 2px 4px -2px rgba(0, 0, 0, 0.05);
}

.image-wrapper {
  position: relative;
  width: 100%;
  padding-top: 100%;
  overflow: hidden;
  background: #fafafa;
}
.image-wrapper img { 
  position: absolute;
  top: 0;
  left: 0;
  width: 100%; 
  height: 100%; 
  object-fit: cover; 
  transition: transform 0.4s ease-in-out;
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
  background: rgba(24, 24, 27, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
}
.product-card:hover .hover-overlay {
  opacity: 1;
}

.product-info {
  padding: 20px;
}
.product-title { 
  margin: 0 0 12px; 
  font-size: 16px; 
  color: #18181b;
  font-weight: 600;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  letter-spacing: -0.01em;
}
.product-meta {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}
.price { 
  color: #dc2626; 
  font-size: 20px; 
  font-weight: 700; 
  line-height: 1;
  letter-spacing: -0.02em;
}
.currency {
  font-size: 14px;
  margin-right: 2px;
  opacity: 0.8;
}
.sales { 
  color: #71717a; 
  font-size: 13px;
  font-weight: 500;
  letter-spacing: 0.01em;
}

.empty-wrapper {
  padding: 60px 0;
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  border-radius: 16px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.02);
}
</style>