<template>
  <div class="page">
    <div class="feedback-container">
      <h1>📝 意见反馈</h1>
      <p class="subtitle">我们非常重视您的宝贵建议！请在下方畅所欲言，帮助我们变得更好。</p>
      
      <div class="feedback-form">
        <div class="form-group">
          <label>反馈类型</label>
          <el-radio-group v-model="type" size="large" class="big-radio-group">
            <el-radio-button label="功能建议" />
            <el-radio-button label="体验优化" />
            <el-radio-button label="Bug反馈" />
            <el-radio-button label="其他" />
          </el-radio-group>
        </div>
        
        <div class="form-group">
          <label>详细描述</label>
          <el-input 
            type="textarea" 
            :rows="8" 
            placeholder="请详细描述您遇到的问题或建议，字数尽量不少于20字..." 
            v-model="content" 
            class="big-textarea" 
          />
        </div>
        
        <div class="form-group">
          <label>联系方式 (选填)</label>
          <el-input 
            size="large" 
            placeholder="留下您的邮箱或手机号，方便我们联系您" 
            v-model="contact" 
            class="big-input"
          />
        </div>
        
        <el-button type="primary" size="large" class="submit-btn" @click="submit">🚀 立即提交反馈</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const type = ref('功能建议')
const content = ref('')
const contact = ref('')

const submit = () => {
  if (!content.value) {
    return ElMessage.warning({ message: '请填写反馈内容哦！', grouping: true, customClass: 'big-message' })
  }
  ElMessage.success({ message: '反馈提交成功，感谢您的支持！', customClass: 'big-message' })
  content.value = ''
  contact.value = ''
}
</script>

<style scoped>
.feedback-container { 
  max-width: 900px; margin: 0 auto; background: #fff; padding: 70px; 
  border-radius: 30px; box-shadow: 0 20px 50px rgba(0,0,0,0.1); 
  border-top: 15px solid #06D6A0; border-bottom: 3px solid #eee; border-left: 3px solid #eee; border-right: 3px solid #eee;
}
.feedback-container h1 { font-size: 46px; font-weight: 900; color: #111; margin-bottom: 20px; }
.subtitle { font-size: 22px; color: #555; margin-bottom: 50px; line-height: 1.6; }

.form-group { margin-bottom: 40px; }
.form-group label { display: block; font-size: 22px; font-weight: 900; color: #222; margin-bottom: 20px; }

.big-radio-group :deep(.el-radio-button__inner) { font-size: 18px; font-weight: bold; padding: 16px 30px; }
.big-radio-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) { background-color: #06D6A0; border-color: #06D6A0; box-shadow: -1px 0 0 0 #06D6A0; }

.big-textarea :deep(.el-textarea__inner) { font-size: 20px; padding: 20px; line-height: 1.8; border-radius: 16px; border: 2px solid #ddd; }
.big-textarea :deep(.el-textarea__inner:focus) { border-color: #06D6A0; }

.big-input :deep(.el-input__wrapper) { padding: 12px 20px; border-radius: 12px; border: 2px solid #ddd; box-shadow: none; }
.big-input :deep(.el-input__inner) { font-size: 20px; height: 30px; }
.big-input :deep(.el-input__wrapper.is-focus) { border-color: #06D6A0; }

.submit-btn { width: 100%; height: 80px; font-size: 28px; font-weight: 900; border-radius: 20px; background: #06D6A0; border-color: #06D6A0; margin-top: 20px; }
.submit-btn:hover { background: #05b888; border-color: #05b888; transform: translateY(-2px); box-shadow: 0 10px 20px rgba(6, 214, 160, 0.3); }

/* Global overwrite for ElMessage if needed, though scoped won't affect it easily. We just use large text inside container */
</style>