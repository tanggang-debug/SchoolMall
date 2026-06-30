<template>
  <div class="page" v-loading="loading">
    <div v-if="product" class="detail-wrap">
      <div class="card detail-main">
        <div class="gallery">
          <div class="cover-wrapper">
            <img :src="currentImage" class="cover" />
          </div>
          <div v-if="images.length > 1" class="thumbs">
            <div 
              v-for="img in images" 
              :key="img" 
              class="thumb-item" 
              :class="{ active: currentImage === img }"
              @click="currentImage = img"
            >
              <img :src="img" />
            </div>
          </div>
        </div>
        
        <div class="info">
          <div class="info-header">
            <el-tag :type="productStatus.type" effect="dark" round class="status-tag">{{ productStatus.label }}</el-tag>
            <h1 class="title">{{ product.title }}</h1>
          </div>
          
          <div class="price-panel">
            <div class="price-main">
              <span class="currency">¥</span>
              <span class="price-value">{{ formatCurrency(product.price) }}</span>
            </div>
            <div class="price-meta">
              <span class="meta-item">销量 <b>{{ product.salesCount || 0 }}</b></span>
              <span class="divider">|</span>
              <span class="meta-item">浏览 <b>{{ product.viewCount || 0 }}</b></span>
            </div>
          </div>
          
          <div class="desc-box">
            <p class="desc">{{ product.description || '暂无商品描述' }}</p>
          </div>
          
          <div class="action-panel">
            <div class="stock-info">
              <span class="label">库存：</span>
              <span class="value">{{ product.stock }} 件</span>
            </div>
            <div class="action-row">
              <el-input-number v-model="qty" :min="1" :max="Math.max(product.stock || 1, 1)" size="large" />
              <el-button type="primary" size="large" :disabled="!product.stock" class="add-cart-btn" @click="addCart">
                <el-icon><ShoppingCart /></el-icon>
                加入购物车
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="card review-card">
        <div class="section-head">
          <h2>商品评价</h2>
          <span class="review-count">共 <b>{{ totalReviews }}</b> 条评价</span>
        </div>
        
        <el-empty v-if="!reviews.length" description="暂时还没有评价" :image-size="120" />
        
        <div v-else class="review-list">
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="review-avatar">
              <el-avatar :size="40" style="background: #a0cfff">{{ (review.username || '用').charAt(0).toUpperCase() }}</el-avatar>
            </div>
            <div class="review-main">
              <div class="review-top">
                <strong class="reviewer-name">{{ review.username || `用户${review.userId}` }}</strong>
                <span class="time">{{ review.createTime }}</span>
              </div>
              <el-rate :model-value="review.rating" disabled class="review-rate" />
              <p class="review-content">{{ review.content || '用户未填写文字评价' }}</p>
              
              <div v-if="review.reply" class="reply-box">
                <div class="reply-header">
                  <el-icon><Service /></el-icon>
                  <span class="reply-title">商家回复</span>
                </div>
                <p class="reply-content">{{ review.reply.content }}</p>
              </div>
              
              <div v-else-if="user.isMerchant" class="reply-action">
                <el-button plain type="primary" size="small" @click="openReply(review)">回复评价</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div v-else-if="!loading" class="card empty-product">
      <el-empty description="商品不存在或已下架" />
      <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
    </div>

    <el-dialog v-model="replyDialogVisible" title="回复评价" width="480px" destroy-on-close>
      <el-input
        v-model="replyForm.content"
        type="textarea"
        :rows="5"
        maxlength="200"
        show-word-limit
        placeholder="请输入回复内容，回复后买家将可见"
      />
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="replySubmitting" @click="submitReply">提交回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { useUserStore } from '../stores/user'
import { formatCurrency, getFirstImage, getProductStatusMeta, parseImageList } from '../utils/view'
import { ShoppingCart, Service } from '@element-plus/icons-vue'

const route = useRoute()
const user = useUserStore()
const product = ref(null)
const reviews = ref([])
const totalReviews = ref(0)
const qty = ref(1)
const loading = ref(false)
const currentImage = ref('')
const replyDialogVisible = ref(false)
const replySubmitting = ref(false)
const activeReviewId = ref(null)
const replyForm = ref({ content: '' })

const images = computed(() => parseImageList(product.value?.images))
const productStatus = computed(() => getProductStatusMeta(product.value?.status, product.value?.statusLabel))

async function loadProduct() {
  const res = await request.get(`/products/${route.params.id}`)
  product.value = res.data
  currentImage.value = getFirstImage(res.data?.images, 'https://picsum.photos/500/500')
  qty.value = 1
}

async function loadReviews() {
  const res = await request.get(`/reviews/products/${route.params.id}/reviews`, { params: { page: 1, size: 20 } })
  reviews.value = res.data.records || []
  totalReviews.value = res.data.total || reviews.value.length
}

async function load() {
  loading.value = true
  try {
    await Promise.all([loadProduct(), loadReviews()])
  } finally {
    loading.value = false
  }
}

async function addCart() {
  await request.post('/cart/items', { productId: Number(route.params.id), quantity: qty.value, selected: true })
  ElMessage.success('已加入购物车')
}

function openReply(review) {
  activeReviewId.value = review.id
  replyForm.value.content = ''
  replyDialogVisible.value = true
}

async function submitReply() {
  if (!replyForm.value.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  replySubmitting.value = true
  try {
    await request.post(`/reviews/${activeReviewId.value}/reply`, { content: replyForm.value.content.trim() })
    ElMessage.success('回复成功')
    replyDialogVisible.value = false
    await loadReviews()
  } finally {
    replySubmitting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.detail-wrap { display: flex; flex-direction: column; gap: 24px; }
.detail-main { display: flex; gap: 40px; padding: 32px; }

.gallery { width: 440px; flex-shrink: 0; }
.cover-wrapper {
  width: 100%;
  padding-top: 100%;
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  background: #f5f7fa;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}
.cover { position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; }

.thumbs { display: flex; gap: 12px; margin-top: 16px; flex-wrap: wrap; }
.thumb-item {
  width: 78px;
  height: 78px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s;
}
.thumb-item img { width: 100%; height: 100%; object-fit: cover; }
.thumb-item.active { border-color: #409eff; opacity: 1; }
.thumb-item:not(.active) { opacity: 0.7; }
.thumb-item:hover { opacity: 1; }

.info { flex: 1; display: flex; flex-direction: column; }
.info-header { margin-bottom: 20px; }
.status-tag { margin-bottom: 12px; }
.title { font-size: 26px; font-weight: 700; color: #1d1d1f; line-height: 1.4; margin: 0; }

.price-panel {
  background: #fff5f5;
  border-radius: 12px;
  padding: 20px 24px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.price-main { color: #f56c6c; display: flex; align-items: baseline; }
.currency { font-size: 18px; font-weight: 600; margin-right: 4px; }
.price-value { font-size: 36px; font-weight: 800; line-height: 1; }
.price-meta { color: #909399; font-size: 14px; }
.price-meta b { color: #606266; font-weight: 600; }
.divider { margin: 0 12px; color: #dcdfe6; }

.desc-box {
  flex: 1;
  margin-bottom: 24px;
}
.desc { 
  color: #606266; 
  line-height: 1.8; 
  font-size: 15px;
  white-space: pre-wrap;
}

.action-panel {
  border-top: 1px dashed #ebeef5;
  padding-top: 24px;
}
.stock-info {
  margin-bottom: 16px;
  font-size: 14px;
}
.stock-info .label { color: #909399; }
.stock-info .value { color: #303133; font-weight: 500; }
.action-row { display: flex; align-items: center; gap: 20px; }
.add-cart-btn {
  width: 180px;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  gap: 6px;
}

.review-card { padding: 32px; }
.section-head { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}
.section-head h2 { font-size: 20px; margin: 0; }
.review-count { color: #909399; font-size: 15px; }
.review-count b { color: #409eff; }

.review-list { display: flex; flex-direction: column; }
.review-item { 
  display: flex; 
  gap: 16px;
  padding: 24px 0; 
  border-bottom: 1px solid #f0f2f5; 
}
.review-item:last-child { border-bottom: none; padding-bottom: 0; }
.review-main { flex: 1; }
.review-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.reviewer-name { font-size: 15px; color: #303133; }
.time { color: #909399; font-size: 13px; }
.review-rate { margin-bottom: 12px; }
.review-content { 
  font-size: 15px;
  color: #303133;
  line-height: 1.6;
  margin: 0 0 16px 0;
}

.reply-box { 
  background: #f8f9fa; 
  border-radius: 8px; 
  padding: 16px;
  position: relative;
}
.reply-header {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #409eff;
  margin-bottom: 8px;
}
.reply-title { font-weight: 600; font-size: 14px; }
.reply-content { 
  margin: 0; 
  color: #606266; 
  font-size: 14px;
  line-height: 1.6;
}
.reply-action { margin-top: 12px; }

.empty-product {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
  gap: 20px;
}
</style>
