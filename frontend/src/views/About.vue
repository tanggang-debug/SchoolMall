<template>
  <div class="page">
    <div class="about-hero">
      <div class="brand-icon">
        <el-icon><Goods /></el-icon>
      </div>
      <h1>校园商城</h1>
      <p class="slogan">连接校园生活，发现二手好物</p>
      <p class="brand-desc">让校园交易更简单、更安全、更有温度</p>
    </div>

    <div class="content-grid">
      <div class="info-card">
        <h3>项目简介</h3>
        <p>校园商城 (Campus Mall) 是一个专为在校师生打造的综合性电商平台。我们致力于解决校园内二手物品流转效率低、信息不对称的问题，同时为有创业意愿的同学提供一个安全、便捷的在线展示窗口。</p>
        <p>在这里，你可以买到物美价廉的教材、电子产品和生活用品；你也可以申请成为商家，将闲置物品变现。所有用户均需通过校园身份认证，保障交易安全。</p>
      </div>

      <div class="info-card">
        <h3>核心功能</h3>
        <ul class="feature-list">
          <li>
            <div class="feature-icon">✓</div>
            <div>
              <strong>角色隔离</strong>
              <span>学生、商户、管理员三级权限，互不干扰</span>
            </div>
          </li>
          <li>
            <div class="feature-icon">✓</div>
            <div>
              <strong>商品管理</strong>
              <span>支持分类检索、图文展示与后台合规审核</span>
            </div>
          </li>
          <li>
            <div class="feature-icon">✓</div>
            <div>
              <strong>完整交易闭环</strong>
              <span>从加购、结算、支付到发货、收货、评价全流程</span>
            </div>
          </li>
          <li>
            <div class="feature-icon">✓</div>
            <div>
              <strong>数据分析</strong>
              <span>为商户和管理员提供直观的销量与用户活跃度报表</span>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div class="tech-card">
      <h3>技术栈架构</h3>
      <div class="tech-tags">
        <div class="tech-group">
          <h4>前端 (Frontend)</h4>
          <span class="tech-tag">Vue 3</span>
          <span class="tech-tag">Vite</span>
          <span class="tech-tag">Element Plus</span>
          <span class="tech-tag">Pinia</span>
          <span class="tech-tag">Vue Router 4</span>
        </div>
        <div class="tech-group">
          <h4>后端 (Backend)</h4>
          <span class="tech-tag">Spring Boot 2.7</span>
          <span class="tech-tag">MyBatis-Plus</span>
          <span class="tech-tag">Spring Cloud Gateway</span>
          <span class="tech-tag">JWT Auth</span>
        </div>
        <div class="tech-group">
          <h4>数据存储 (Storage)</h4>
          <span class="tech-tag">MySQL 8.0</span>
          <span class="tech-tag">Redis 7.0</span>
        </div>
      </div>
    </div>
    
    <div class="footer-copy">
      <p>&copy; 2024-2026 Campus Mall Project. All rights reserved.</p>
      <p>Version 1.0.0</p>
    </div>
  </div>
</template>

<script setup>
import { Goods } from '@element-plus/icons-vue'
</script>

<style scoped>
/* 极简黑白风格 - 关于页 */
.about-hero {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 80px 40px;
  text-align: center;
  margin-bottom: 40px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.brand-icon {
  width: 64px;
  height: 64px;
  background: #18181b;
  color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  font-size: 32px;
}

.about-hero h1 {
  font-size: 36px;
  font-weight: 700;
  color: #18181b;
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.slogan {
  font-size: 18px;
  color: #71717a;
  margin-bottom: 12px;
  font-weight: 400;
}

.brand-desc {
  font-size: 16px;
  color: #a1a1aa;
  font-weight: 400;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
  margin-bottom: 40px;
}

.info-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.info-card h3 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 24px;
  letter-spacing: -0.01em;
  border-bottom: 1px solid #e4e4e7;
  padding-bottom: 16px;
}

.info-card p {
  font-size: 15px;
  color: #71717a;
  line-height: 1.8;
  margin-bottom: 16px;
  font-weight: 400;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-list li {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 20px;
}

.feature-icon {
  width: 24px;
  height: 24px;
  background: #18181b;
  color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  margin-top: 2px;
}

.feature-list strong {
  font-size: 17px;
  color: #18181b;
  margin-bottom: 6px;
  font-weight: 600;
  display: block;
}

.feature-list span {
  font-size: 15px;
  color: #71717a;
  font-weight: 400;
}

.tech-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 32px;
  border-radius: 16px;
  margin-bottom: 40px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.tech-card h3 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 24px;
  text-align: center;
  letter-spacing: -0.01em;
}

.tech-tags {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.tech-group {
  text-align: center;
}

.tech-group h4 {
  font-size: 17px;
  color: #71717a;
  margin-bottom: 12px;
  font-weight: 600;
}

.tech-tag {
  display: inline-block;
  background: #fafafa;
  border: 1px solid #e4e4e7;
  color: #18181b;
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  margin: 4px;
  transition: box-shadow 0.2s ease;
}

.tech-tag:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.footer-copy {
  text-align: center;
  padding: 40px 0;
  color: #71717a;
  font-size: 15px;
}

.footer-copy p {
  margin: 8px 0;
  font-weight: 400;
}

@media (max-width: 1024px) {
  .content-grid,
  .tech-tags {
    grid-template-columns: 1fr;
  }
}
</style>