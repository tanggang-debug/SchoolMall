<template>
  <div class="page discover-page">
    <div class="hero-banner">
      <h1>探索校园新世界</h1>
      <p>发现精彩内容，结识志同道合的伙伴</p>
    </div>

    <div class="content-grid">
      <div class="section-card">
        <div class="section-header">
          <h2>热门推荐</h2>
        </div>
        <div class="recommend-list">
          <div class="recommend-item">
            <div class="recommend-image">
              <img src="https://picsum.photos/id/201/300/200" alt="科技数码" loading="lazy" />
            </div>
            <div class="recommend-content">
              <h3>科技数码专区</h3>
              <p>最新电子产品、数码配件一应俱全</p>
              <el-button size="large">立即探索</el-button>
            </div>
          </div>
          <div class="recommend-item">
            <div class="recommend-image">
              <img src="https://picsum.photos/id/580/300/200" alt="美食分享" loading="lazy" />
            </div>
            <div class="recommend-content">
              <h3>美食分享区</h3>
              <p>校园周边美食推荐与评价</p>
              <el-button size="large">查看详情</el-button>
            </div>
          </div>
          <div class="recommend-item">
            <div class="recommend-image">
              <img src="https://picsum.photos/id/1036/300/200" alt="校园风光" loading="lazy" />
            </div>
            <div class="recommend-content">
              <h3>校园风光</h3>
              <p>记录校园美景，分享青春记忆</p>
              <el-button size="large">浏览相册</el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="section-card">
        <div class="section-header">
          <h2>热门话题</h2>
        </div>
        <div class="topics-grid">
          <div class="topic-card">
            <span class="topic-badge">热门</span>
            <h3>#期末考试攻略</h3>
            <p>328人参与讨论</p>
          </div>
          <div class="topic-card">
            <span class="topic-badge">精选</span>
            <h3>#实习经验分享</h3>
            <p>156人参与讨论</p>
          </div>
          <div class="topic-card">
            <span class="topic-badge">最新</span>
            <h3>#社团活动预告</h3>
            <p>89人参与讨论</p>
          </div>
          <div class="topic-card">
            <span class="topic-badge">推荐</span>
            <h3>#考研经验交流</h3>
            <p>245人参与讨论</p>
          </div>
        </div>
      </div>

      <div class="section-card">
        <div class="section-header">
          <h2>活跃用户</h2>
        </div>
        <div class="users-list">
          <div class="user-card">
            <div class="user-avatar">张</div>
            <div class="user-info">
              <h3>张同学</h3>
              <p>计算机学院</p>
              <span class="user-badge">活跃达人</span>
            </div>
          </div>
          <div class="user-card">
            <div class="user-avatar">李</div>
            <div class="user-info">
              <h3>李同学</h3>
              <p>商学院</p>
              <span class="user-badge">优秀商户</span>
            </div>
          </div>
          <div class="user-card">
            <div class="user-avatar">王</div>
            <div class="user-info">
              <h3>王同学</h3>
              <p>艺术学院</p>
              <span class="user-badge">创意之星</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 极简黑白风格 - 性能优化 */
.discover-page {
  padding-top: 40px;
}

.hero-banner {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 50px 40px;
  text-align: center;
  margin-bottom: 40px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.hero-banner h1 {
  font-size: 36px;
  font-weight: 700;
  color: #18181b;
  margin-bottom: 16px;
  letter-spacing: -0.02em;
}

.hero-banner p {
  font-size: 16px;
  color: #71717a;
  font-weight: 400;
}

.content-grid {
  display: grid;
  gap: 32px;
}

.section-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.section-header {
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #18181b;
  letter-spacing: -0.01em;
}

.recommend-list {
  display: grid;
  gap: 24px;
}

.recommend-item {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
  background: #fafafa;
  border: 1px solid #e4e4e7;
  border-radius: 12px;
  padding: 20px;
  transition: box-shadow 0.2s ease;
}

.recommend-item:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.recommend-image {
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e4e4e7;
}

.recommend-image img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  display: block;
}

.recommend-content h3 {
  font-size: 20px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 12px;
  letter-spacing: -0.01em;
}

.recommend-content p {
  font-size: 15px;
  color: #71717a;
  margin-bottom: 20px;
  font-weight: 400;
}

.recommend-content .el-button {
  font-size: 15px;
  padding: 12px 24px;
  background: #18181b;
  border: 1px solid #18181b;
  color: #ffffff;
  font-weight: 600;
}

.topics-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.topic-card {
  background: #fafafa;
  border: 1px solid #e4e4e7;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  transition: box-shadow 0.2s ease;
}

.topic-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.topic-badge {
  display: inline-block;
  background: #18181b;
  color: #ffffff;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 12px;
}

.topic-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 8px;
  letter-spacing: -0.01em;
}

.topic-card p {
  font-size: 15px;
  color: #71717a;
  font-weight: 400;
}

.users-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.user-card {
  background: #fafafa;
  border: 1px solid #e4e4e7;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: box-shadow 0.2s ease;
}

.user-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.user-avatar {
  width: 48px;
  height: 48px;
  background: #18181b;
  color: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
}

.user-info h3 {
  font-size: 18px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 6px;
  letter-spacing: -0.01em;
}

.user-info p {
  font-size: 14px;
  color: #71717a;
  margin-bottom: 8px;
  font-weight: 400;
}

.user-badge {
  display: inline-block;
  background: #f4f4f5;
  border: 1px solid #e4e4e7;
  color: #71717a;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
}

/* 响应式 */
@media (max-width: 1024px) {
  .recommend-item {
    grid-template-columns: 1fr;
  }
  
  .recommend-image img {
    height: 200px;
  }
  
  .topics-grid,
  .users-list {
    grid-template-columns: 1fr;
  }
}
</style>