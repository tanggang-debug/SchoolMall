<template>
  <div class="page community-page">
    <div class="page-header">
      <h1>社区交流</h1>
      <p>分享校园生活，交流学习心得</p>
    </div>

    <div class="posts-grid">
      <div class="post-card">
        <div class="post-header">
          <div class="post-avatar">张</div>
          <div class="post-meta">
            <span class="post-author">张同学</span>
            <span class="post-time">2小时前</span>
          </div>
        </div>
        <div class="post-content">
          <h3>期末考试复习心得分享</h3>
          <p>分享一下我的期末复习方法，希望对大家有帮助...</p>
        </div>
        <div class="post-footer">
          <span class="post-stats">128 回复 · 256 赞</span>
        </div>
      </div>

      <div class="post-card">
        <div class="post-header">
          <div class="post-avatar">李</div>
          <div class="post-meta">
            <span class="post-author">李同学</span>
            <span class="post-time">5小时前</span>
          </div>
        </div>
        <div class="post-content">
          <h3>校园美食推荐合集</h3>
          <p>整理了一份校园周边美食清单，亲测好评...</p>
        </div>
        <div class="post-footer">
          <span class="post-stats">89 回复 · 312 赞</span>
        </div>
      </div>

      <div class="post-card">
        <div class="post-header">
          <div class="post-avatar">王</div>
          <div class="post-meta">
            <span class="post-author">王同学</span>
            <span class="post-time">昨天</span>
          </div>
        </div>
        <div class="post-content">
          <h3>实习经验分享</h3>
          <p>最近完成了一轮实习面试，总结了一些经验...</p>
        </div>
        <div class="post-footer">
          <span class="post-stats">67 回复 · 189 赞</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 极简黑白风格 - 社区页 */
.community-page {
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

.posts-grid {
  display: grid;
  gap: 24px;
}

.post-card {
  background: #ffffff;
  border: 1px solid #e4e4e7;
  padding: 24px;
  border-radius: 12px;
  transition: box-shadow 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.02);
}

.post-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.post-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.post-avatar {
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

.post-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.post-author {
  font-size: 17px;
  font-weight: 600;
  color: #18181b;
}

.post-time {
  font-size: 14px;
  color: #71717a;
  font-weight: 400;
}

.post-content h3 {
  font-size: 20px;
  font-weight: 600;
  color: #18181b;
  margin-bottom: 12px;
  letter-spacing: -0.01em;
}

.post-content p {
  font-size: 15px;
  color: #71717a;
  line-height: 1.6;
  font-weight: 400;
}

.post-footer {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #e4e4e7;
}

.post-stats {
  font-size: 14px;
  color: #71717a;
  font-weight: 500;
}
</style>