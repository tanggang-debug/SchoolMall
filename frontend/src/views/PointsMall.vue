<template>
  <div class="page points-page">
    <div class="points-banner">
      <h1>积分商城</h1>
      <p>用积分兑换精美礼品，更多惊喜等你发现</p>
      <div class="points-info">
        <div class="points-box">
          <span class="points-label">我的积分</span>
          <span class="points-value">2,580</span>
        </div>
        <el-button size="large" class="earn-btn">赚取积分</el-button>
      </div>
    </div>

    <div class="categories-section">
      <h2>礼品分类</h2>
      <div class="categories-grid">
        <div class="category-card" @click="filterCategory('digital')">
          <span>数码产品</span>
        </div>
        <div class="category-card" @click="filterCategory('daily')">
          <span>生活用品</span>
        </div>
        <div class="category-card" @click="filterCategory('study')">
          <span>学习文具</span>
        </div>
        <div class="category-card" @click="filterCategory('food')">
          <span>美食零食</span>
        </div>
      </div>
    </div>

    <div class="gifts-section">
      <div class="section-header">
        <h2>热门礼品</h2>
        <el-button size="large">查看全部</el-button>
      </div>
      <div class="gifts-grid">
        <div class="gift-card">
          <div class="gift-image">
            <img src="https://picsum.photos/id/2/300/300" alt="蓝牙耳机" loading="lazy" />
          </div>
          <div class="gift-info">
            <h3>高品质蓝牙耳机</h3>
            <p>音质出色，佩戴舒适</p>
            <div class="gift-footer">
              <span class="gift-points">500积分</span>
              <el-button size="large">立即兑换</el-button>
            </div>
          </div>
        </div>
        <div class="gift-card">
          <div class="gift-image">
            <img src="https://picsum.photos/id/225/300/300" alt="充电宝" loading="lazy" />
          </div>
          <div class="gift-info">
            <h3>便携充电宝</h3>
            <p>20000mAh大容量</p>
            <div class="gift-footer">
              <span class="gift-points">300积分</span>
              <el-button size="large">立即兑换</el-button>
            </div>
          </div>
        </div>
        <div class="gift-card">
          <div class="gift-image">
            <img src="https://picsum.photos/id/598/300/300" alt="文具套装" loading="lazy" />
          </div>
          <div class="gift-info">
            <h3>精美文具套装</h3>
            <p>学习必备，品质优良</p>
            <div class="gift-footer">
              <span class="gift-points">150积分</span>
              <el-button size="large">立即兑换</el-button>
            </div>
          </div>
        </div>
        <div class="gift-card">
          <div class="gift-image">
            <img src="https://picsum.photos/id/312/300/300" alt="零食礼包" loading="lazy" />
          </div>
          <div class="gift-info">
            <h3>精选零食礼包</h3>
            <p>美味可口，营养健康</p>
            <div class="gift-footer">
              <span class="gift-points">80积分</span>
              <el-button size="large">立即兑换</el-button>
            </div>
          </div>
        </div>
        <div class="gift-card">
          <div class="gift-image">
            <img src="https://picsum.photos/id/1/300/300" alt="U盘" loading="lazy" />
          </div>
          <div class="gift-info">
            <h3>高速U盘 64GB</h3>
            <p>传输快速，稳定可靠</p>
            <div class="gift-footer">
              <span class="gift-points">200积分</span>
              <el-button size="large">立即兑换</el-button>
            </div>
          </div>
        </div>
        <div class="gift-card">
          <div class="gift-image">
            <img src="https://picsum.photos/id/625/300/300" alt="咖啡礼盒" loading="lazy" />
          </div>
          <div class="gift-info">
            <h3>进口咖啡礼盒</h3>
            <p>香浓醇厚，提神醒脑</p>
            <div class="gift-footer">
              <span class="gift-points">250积分</span>
              <el-button size="large">立即兑换</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="rules-section">
      <div class="rules-card">
        <h2>积分规则说明</h2>
        <div class="rules-list">
          <div class="rule-item">
            <h3>购物积分</h3>
            <p>每消费1元获得1积分</p>
          </div>
          <div class="rule-item">
            <h3>评价积分</h3>
            <p>完成商品评价获得5积分</p>
          </div>
          <div class="rule-item">
            <h3>分享积分</h3>
            <p>分享商品给好友获得3积分</p>
          </div>
          <div class="rule-item">
            <h3>签到积分</h3>
            <p>每日签到获得2积分</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const filterCategory = (category) => {
  console.log('Filter by category:', category)
}
</script>

<style scoped>
/* 极简黑白风格 - 性能优化 */
.points-page {
  padding-top: 40px;
}

.points-banner {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 50px 40px;
  text-align: center;
  margin-bottom: 40px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.points-banner h1 {
  font-size: 36px;
  font-weight: 700;
  color: #18181b;
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.points-banner p {
  font-size: 16px;
  color: #71717a;
  margin-bottom: 30px;
  font-weight: 400;
}

.points-info {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
}

.points-box {
  background: #fafafa;
  border: 1px solid #e4e4e7;
  padding: 20px 30px;
  border-radius: 12px;
  text-align: center;
}

.points-label {
  font-size: 14px;
  color: #71717a;
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.points-value {
  font-size: 32px;
  font-weight: 700;
  color: #18181b;
  display: block;
  letter-spacing: -0.02em;
}

.earn-btn {
  font-size: 15px;
  padding: 14px 28px;
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  font-weight: 600;
}

.categories-section {
  margin-bottom: 40px;
}

.categories-section h2 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 20px;
  letter-spacing: -0.01em;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.category-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: box-shadow 0.2s ease, background-color 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.category-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background: #fafafa;
}

.category-card span {
  font-size: 16px;
  font-weight: 600;
  color: #18181b;
  letter-spacing: -0.01em;
}

.gifts-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  letter-spacing: -0.01em;
}

.section-header .el-button {
  font-size: 15px;
  padding: 12px 24px;
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  font-weight: 600;
}

.gifts-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.gift-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  border-radius: 12px;
  overflow: hidden;
  transition: box-shadow 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.gift-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.gift-image {
  height: 240px;
  overflow: hidden;
  background: #fafafa;
  border-bottom: 1px solid #e4e4e7;
}

.gift-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.gift-info {
  padding: 20px;
}

.gift-info h3 {
  font-size: 18px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 12px;
  letter-spacing: -0.01em;
}

.gift-info p {
  font-size: 14px;
  color: #71717a;
  margin-bottom: 16px;
  font-weight: 400;
}

.gift-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.gift-points {
  font-size: 18px;
  color: #18181b;
  font-weight: 700;
  letter-spacing: -0.01em;
}

.gift-footer .el-button {
  font-size: 14px;
  padding: 10px 20px;
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  font-weight: 600;
}

.rules-section {
  margin-bottom: 40px;
}

.rules-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.rules-card h2 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 24px;
  text-align: center;
  letter-spacing: -0.01em;
}

.rules-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.rule-item {
  background: #fafafa;
  border: 1px solid #e4e4e7;
  padding: 20px;
  border-radius: 12px;
}

.rule-item h3 {
  font-size: 18px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 8px;
  letter-spacing: -0.01em;
}

.rule-item p {
  font-size: 15px;
  color: #71717a;
  font-weight: 400;
}

/* 响应式 */
@media (max-width: 1024px) {
  .categories-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .gifts-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .rules-list {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .gifts-grid,
  .categories-grid {
    grid-template-columns: 1fr;
  }
  
  .points-info {
    flex-direction: column;
    gap: 20px;
  }
}
</style>