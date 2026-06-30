<template>
  <div class="page">
    <div class="help-header card">
      <h1>帮助中心</h1>
      <p>遇到问题？在这里寻找答案，或者联系我们</p>
      
      <div class="search-box">
        <el-input
          v-model="keyword"
          placeholder="搜索常见问题..."
          size="large"
          :prefix-icon="Search"
          clearable
        />
      </div>
    </div>

    <div class="help-content">
      <div class="card faq-card">
        <h3><el-icon><QuestionFilled /></el-icon> 常见问题 (FAQ)</h3>
        
        <el-collapse v-model="activeNames">
          <el-collapse-item v-for="(item, index) in filteredFaqs" :key="index" :title="item.q" :name="index">
            <div class="answer">{{ item.a }}</div>
          </el-collapse-item>
        </el-collapse>
        
        <el-empty v-if="!filteredFaqs.length" description="没有找到相关问题" :image-size="100" />
      </div>

      <div class="card contact-card">
        <h3><el-icon><Service /></el-icon> 联系客服</h3>
        <div class="contact-methods">
          <div class="contact-item">
            <el-icon class="icon"><Message /></el-icon>
            <div class="info">
              <h4>发送邮件</h4>
              <p>support@campusmall.edu</p>
            </div>
          </div>
          <div class="contact-item">
            <el-icon class="icon"><Phone /></el-icon>
            <div class="info">
              <h4>服务热线</h4>
              <p>400-123-4567</p>
              <span>(工作日 9:00 - 18:00)</span>
            </div>
          </div>
          <div class="contact-item">
            <el-icon class="icon"><Location /></el-icon>
            <div class="info">
              <h4>办公地址</h4>
              <p>大学城科技园 A区 101室</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Search, QuestionFilled, Service, Message, Phone, Location } from '@element-plus/icons-vue'

const keyword = ref('')
const activeNames = ref([0])

const faqs = [
  { q: '如何注册成为校园商城的卖家？', a: '目前商户账号需要通过管理员后台进行审核分配。如果您有开店需求，请发送邮件至 support@campusmall.edu 提交您的学号、姓名和售卖商品类别申请。' },
  { q: '购买商品后如何退款？', a: '在"我的订单"页面中，对于未发货或已收货但状态允许的订单，您可以点击"申请退款"按钮，并填写退款原因。商家审核通过后，款项将原路退回。' },
  { q: '为什么我无法修改默认收货地址？', a: '请在"订单确认"或"个人中心"页面，找到对应的地址并点击"设为默认"。如果遇到网络异常，请刷新页面后重试。' },
  { q: '模拟支付是怎么回事？', a: '本项目为演示性质的校园商城，不接入真实的微信/支付宝支付。点击"去支付"后跳转的支付页面会显示模拟二维码，点击"模拟支付成功"即可完成订单状态流转。' },
  { q: '如何查看物流信息？', a: '商家发货后会填写物流单号，您可以在"我的订单" -> "订单详情" 中查看到物流单号。目前系统暂未接入第三方物流轨迹查询接口。' }
]

const filteredFaqs = computed(() => {
  if (!keyword.value) return faqs
  const k = keyword.value.toLowerCase()
  return faqs.filter(f => f.q.toLowerCase().includes(k) || f.a.toLowerCase().includes(k))
})
</script>

<style scoped>
.help-header {
  text-align: center;
  padding: 48px 20px;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  color: #303133;
  margin-bottom: 24px;
}
.help-header h1 {
  font-size: 32px;
  margin: 0 0 12px;
  font-weight: 800;
}
.help-header p {
  font-size: 16px;
  color: #606266;
  margin-bottom: 32px;
}
.search-box {
  max-width: 600px;
  margin: 0 auto;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  border-radius: 8px;
}

.help-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.faq-card, .contact-card {
  padding: 32px;
}

h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 24px;
  font-size: 20px;
  color: #303133;
}
h3 .el-icon {
  color: #409eff;
}

.answer {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  padding: 4px 0 8px;
}

/* 覆盖 el-collapse 样式 */
:deep(.el-collapse-item__header) {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.contact-methods {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: transform 0.2s;
}
.contact-item:hover {
  transform: translateX(4px);
  background: #ecf5ff;
}
.contact-item .icon {
  font-size: 24px;
  color: #409eff;
  margin-top: 2px;
}
.info h4 {
  margin: 0 0 4px;
  font-size: 15px;
  color: #303133;
}
.info p {
  margin: 0;
  font-size: 14px;
  color: #606266;
}
.info span {
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .help-content {
    grid-template-columns: 1fr;
  }
}
</style>