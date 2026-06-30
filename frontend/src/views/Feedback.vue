<template>
  <div class="page feedback-page">
    <div class="page-header">
      <h1>意见反馈</h1>
      <p>帮助我们改进校园商城，让体验更美好</p>
    </div>

    <div class="feedback-card">
      <form class="feedback-form">
        <div class="form-group">
          <label>反馈类型</label>
          <div class="type-grid">
            <div class="type-item active">功能建议</div>
            <div class="type-item">问题反馈</div>
            <div class="type-item">体验优化</div>
            <div class="type-item">其他</div>
          </div>
        </div>

        <div class="form-group">
          <label>反馈标题</label>
          <input type="text" placeholder="请简要描述您的反馈内容" />
        </div>

        <div class="form-group">
          <label>详细描述</label>
          <textarea placeholder="请详细描述您的建议或遇到的问题..." rows="6"></textarea>
        </div>

        <div class="form-group">
          <label>联系方式</label>
          <input type="text" placeholder="邮箱或手机号（可选）" />
        </div>

        <button type="submit" class="submit-btn">提交反馈</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* 极简黑白风格 - 反馈页 */
.feedback-page {
  padding-top: 40px;
}

.page-header {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 40px;
  text-align: center;
  margin-bottom: 40px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.page-header h1 {
  font-size: 36px;
  font-weight: 700;
  color: #18181b;
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.page-header p {
  font-size: 16px;
  color: #71717a;
  font-weight: 400;
}

.feedback-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.feedback-form {
  max-width: 800px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 32px;
}

.form-group label {
  font-size: 17px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 12px;
  display: block;
}

.type-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.type-item {
  background: #fafafa;
  border: 1px solid #e4e4e7;
  padding: 14px 20px;
  border-radius: 8px;
  text-align: center;
  font-size: 15px;
  font-weight: 600;
  color: #71717a;
  cursor: pointer;
  transition: box-shadow 0.2s ease;
}

.type-item:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.type-item.active {
  background: #18181b;
  border-color: #18181b;
  color: #ffffff;
}

.form-group input,
.form-group textarea {
  width: 100%;
  background: #fafafa;
  border: 1px solid #e4e4e7;
  padding: 14px 16px;
  border-radius: 8px;
  font-size: 15px;
  color: #18181b;
  font-weight: 400;
  outline: none;
  transition: box-shadow 0.2s ease;
}

.form-group input:hover,
.form-group textarea:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #18181b;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: #a1a1aa;
}

.submit-btn {
  width: 100%;
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  padding: 16px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: box-shadow 0.2s ease;
}

.submit-btn:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .type-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>