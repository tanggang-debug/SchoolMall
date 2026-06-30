<template>
  <div class="page">
    <div class="about-hero card">
      <el-icon class="brand-icon"><Goods /></el-icon>
      <h1>校园商城</h1>
      <p class="slogan">连接校园生活，发现二手好物</p>
    </div>

    <div class="content-grid">
      <div class="card info-card">
        <h3>项目简介</h3>
        <p>校园商城 (Campus Mall) 是一个专为在校师生打造的综合性电商平台。我们致力于解决校园内二手物品流转效率低、信息不对称的问题，同时为有创业意愿的同学提供一个安全、便捷的在线展示窗口。</p>
        <p>在这里，你可以买到物美价廉的教材、电子产品和生活用品；你也可以申请成为商家，将闲置物品变现。所有用户均需通过校园身份认证，保障交易安全。</p>
      </div>

      <div class="card info-card">
        <h3>核心功能</h3>
        <ul class="feature-list">
          <li>
            <el-icon><Check /></el-icon>
            <div>
              <strong>角色隔离</strong>
              <span>学生、商户、管理员三级权限，互不干扰</span>
            </div>
          </li>
          <li>
            <el-icon><Check /></el-icon>
            <div>
              <strong>商品管理</strong>
              <span>支持分类检索、图文展示与后台合规审核</span>
            </div>
          </li>
          <li>
            <el-icon><Check /></el-icon>
            <div>
              <strong>完整交易闭环</strong>
              <span>从加购、结算、支付到发货、收货、评价全流程</span>
            </div>
          </li>
          <li>
            <el-icon><Check /></el-icon>
            <div>
              <strong>数据分析</strong>
              <span>为商户和管理员提供直观的销量与用户活跃度报表</span>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div class="card tech-card">
      <h3>技术栈架构</h3>
      <div class="tech-tags">
        <div class="tech-group">
          <h4>前端 (Frontend)</h4>
          <el-tag effect="dark">Vue 3</el-tag>
          <el-tag effect="dark">Vite</el-tag>
          <el-tag effect="dark">Element Plus</el-tag>
          <el-tag effect="dark">Pinia</el-tag>
          <el-tag effect="dark">Vue Router 4</el-tag>
        </div>
        <div class="tech-group">
          <h4>后端 (Backend)</h4>
          <el-tag type="success" effect="dark">Spring Boot 2.7</el-tag>
          <el-tag type="success" effect="dark">MyBatis-Plus</el-tag>
          <el-tag type="success" effect="dark">Spring Cloud Gateway</el-tag>
          <el-tag type="success" effect="dark">JWT Auth</el-tag>
        </div>
        <div class="tech-group">
          <h4>数据存储 (Storage)</h4>
          <el-tag type="warning" effect="dark">MySQL 8.0</el-tag>
          <el-tag type="danger" effect="dark">Redis 7.0</el-tag>
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
import { Goods, Check } from '@element-plus/icons-vue'
</script>

<style scoped>
.about-hero {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: #fff;
  margin-bottom: 24px;
}
.brand-icon {
  font-size: 64px;
  color: #409eff;
  margin-bottom: 16px;
}
.about-hero h1 {
  font-size: 36px;
  font-weight: 800;
  color: #303133;
  margin: 0 0 12px;
  letter-spacing: 2px;
}
.slogan {
  font-size: 18px;
  color: #909399;
  margin: 0;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}
.info-card {
  padding: 32px;
}
.info-card h3, .tech-card h3 {
  font-size: 20px;
  margin: 0 0 20px;
  color: #303133;
  position: relative;
  padding-bottom: 12px;
}
.info-card h3::after, .tech-card h3::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 3px;
  background: #409eff;
  border-radius: 2px;
}

.info-card p {
  color: #606266;
  line-height: 1.8;
  margin-bottom: 16px;
  font-size: 15px;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.feature-list li {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}
.feature-list .el-icon {
  color: #67c23a;
  font-size: 20px;
  margin-top: 2px;
}
.feature-list strong {
  display: block;
  font-size: 15px;
  color: #303133;
  margin-bottom: 4px;
}
.feature-list span {
  font-size: 14px;
  color: #909399;
}

.tech-card {
  padding: 32px;
}
.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
}
.tech-group h4 {
  font-size: 15px;
  color: #606266;
  margin: 0 0 12px;
}
.tech-group .el-tag {
  margin-right: 8px;
  margin-bottom: 8px;
  border: none;
}

.footer-copy {
  text-align: center;
  padding: 40px 0 20px;
  color: #c0c4cc;
  font-size: 13px;
}
.footer-copy p {
  margin: 4px 0;
}

@media (max-width: 768px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
}
</style>