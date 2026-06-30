<template>
  <div class="page">
    <div class="header-banner">
      <h1>💬 交流社区</h1>
      <p>畅所欲言，分享你的校园生活、避坑指南和好物推荐</p>
    </div>
    <div class="community-layout">
      <div class="post-list">
        <div class="post-item" v-for="i in 6" :key="i">
          <div class="post-avatar">{{ ['李','张','王','赵','陈','刘'][i-1] }}</div>
          <div class="post-content">
            <h3>求推荐好用的机械键盘，预算300左右 {{ i }}</h3>
            <p>如题，主要用来打游戏和写代码，手感要好，声音不能太大影响室友。目前看了几款国产轴，不知道寿命怎么样，求用过的同学给点真实评价！感谢！</p>
            <div class="post-meta">
              <el-tag size="large" effect="dark" color="#118AB2" style="border:none">数码交流</el-tag>
              <span class="time">刚刚发布</span>
              <span class="reply-count">回复: {{ i * 5 + 12 }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="sidebar">
        <div class="hot-topics">
          <h2>🔥 热门话题</h2>
          <ul>
            <li># 毕业季跳蚤市场</li>
            <li># 食堂红黑榜</li>
            <li># 考研资料漂流</li>
            <li># 校园周边美食探索</li>
          </ul>
          <el-button type="primary" size="large" class="publish-btn">发布新帖</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.header-banner { background: #073B4C; color: white; padding: 60px 40px; border-radius: 24px; margin-bottom: 40px; box-shadow: 6px 6px 0px #FFD166; }
.header-banner h1 { font-size: 48px; font-weight: 900; margin-bottom: 16px; }
.header-banner p { font-size: 22px; opacity: 0.9; }

.community-layout { display: grid; grid-template-columns: 1fr 350px; gap: 40px; }
.post-list { display: flex; flex-direction: column; gap: 30px; }
.post-item { 
  display: flex; gap: 24px; background: #fff; padding: 35px; border-radius: 20px; 
  box-shadow: 0 4px 12px rgba(0,0,0,0.05); border: 3px solid #eee; transition: border-color 0.3s;
}
.post-item:hover { border-color: #118AB2; }
.post-avatar { 
  width: 70px; height: 70px; background: #EF476F; color: #fff; border-radius: 50%; 
  display: flex; align-items: center; justify-content: center; font-size: 32px; font-weight: 900; flex-shrink: 0; 
}
.post-content h3 { font-size: 24px; font-weight: 900; margin: 0 0 16px; color: #111; line-height: 1.4; }
.post-content p { font-size: 18px; color: #444; line-height: 1.8; margin: 0 0 24px; }
.post-meta { font-size: 16px; color: #888; display: flex; align-items: center; gap: 20px; font-weight: 600; }

.sidebar { position: sticky; top: 120px; }
.hot-topics { background: #fff; padding: 35px; border-radius: 20px; border: 3px solid #eee; }
.hot-topics h2 { font-size: 28px; font-weight: 900; color: #111; margin-bottom: 24px; }
.hot-topics ul { list-style: none; padding: 0; margin: 0 0 30px; display: flex; flex-direction: column; gap: 16px; }
.hot-topics li { font-size: 20px; font-weight: 700; color: #118AB2; cursor: pointer; padding: 12px; background: #f4f8fa; border-radius: 12px; }
.hot-topics li:hover { background: #118AB2; color: #fff; }
.publish-btn { width: 100%; height: 60px; font-size: 22px; font-weight: 900; border-radius: 16px; background: #06D6A0; border-color: #06D6A0; }
.publish-btn:hover { background: #05b888; border-color: #05b888; }
</style>