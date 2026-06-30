<template>
  <div class="page help-page">
    <div class="page-header">
      <h1>帮助中心</h1>
      <p>了解校园商城的使用指南和常见问题</p>
    </div>

    <div class="help-grid">
      <div class="help-card">
        <div class="help-icon">📋</div>
        <h3>新手指南</h3>
        <p>了解如何注册、购物和成为商家</p>
      </div>

      <div class="help-card">
        <div class="help-icon">🔍</div>
        <h3>购物流程</h3>
        <p>从搜索到下单支付的完整流程</p>
      </div>

      <div class="help-card">
        <div class="help-icon">💼</div>
        <h3>商家入驻</h3>
        <p>如何申请成为商家并管理商品</p>
      </div>

      <div class="help-card">
        <div class="help-icon">🔒</div>
        <h3>安全须知</h3>
        <p>交易安全和账号保护指南</p>
      </div>

      <div class="help-card">
        <div class="help-icon">❓</div>
        <h3>常见问题</h3>
        <p>用户常见的疑问和解答</p>
      </div>

      <div class="help-card">
        <div class="help-icon">📞</div>
        <h3>联系我们</h3>
        <p>客服联系方式和反馈渠道</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 极简黑白风格 - 帮助页 */
.help-page {
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

.help-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.help-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 32px;
  border-radius: 12px;
  text-align: center;
  transition: box-shadow 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.help-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.help-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.help-card h3 {
  font-size: 20px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 12px;
  letter-spacing: -0.01em;
}

.help-card p {
  font-size: 15px;
  color: #71717a;
  font-weight: 400;
}

@media (max-width: 1024px) {
  .help-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .help-grid {
    grid-template-columns: 1fr;
  }
}
</style>