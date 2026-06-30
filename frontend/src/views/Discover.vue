<template>
  <div class="page">
    <h1 class="page-title">发现校园新鲜事 🌟</h1>
    <div class="discover-grid">
      <div class="discover-card color-1">
        <h2>🔥 校园二手集市火热开市</h2>
        <p>本周末在南区操场，快来淘淘宝贝吧！全场低至1折，还有神秘小礼物相送，不要错过哦！</p>
        <div class="card-footer">
          <span>阅读量 1.2w</span>
          <el-button color="#fff" style="color: #FF6B6B" round>查看详情</el-button>
        </div>
      </div>
      <div class="discover-card color-2">
        <h2>💻 最新数码产品评测</h2>
        <p>学长带你揭秘最新款平板电脑，到底值不值得学生党入手？性能、续航、价格全方位解析。</p>
        <div class="card-footer">
          <span>阅读量 8k</span>
          <el-button color="#fff" style="color: #4ECDC4" round>查看详情</el-button>
        </div>
      </div>
      <div class="discover-card color-3">
        <h2>📚 考研复习资料免费送</h2>
        <p>学姐整理了三年的考研笔记，现在免费赠送给有需要的同学，涵盖数学、英语及多门专业课，先到先得！</p>
        <div class="card-footer">
          <span>阅读量 2.5w</span>
          <el-button color="#fff" style="color: #45B7D1" round>查看详情</el-button>
        </div>
      </div>
      <div class="discover-card color-4">
        <h2>🍔 食堂新档口试吃报告</h2>
        <p>三食堂二楼新开的麻辣香锅味道如何？我们派出了“试吃小分队”，为你带来最真实的味蕾体验。</p>
        <div class="card-footer">
          <span>阅读量 5k</span>
          <el-button color="#fff" style="color: #F9A03F" round>查看详情</el-button>
        </div>
      </div>
      <div class="discover-card color-5">
        <h2>🎸 校园十佳歌手总决赛</h2>
        <p>本周五晚大礼堂，全校最强好声音巅峰对决！扫描下方二维码即可参与抢票，一起来现场为你喜欢的选手打call！</p>
        <div class="card-footer">
          <span>阅读量 1.8w</span>
          <el-button color="#fff" style="color: #9D4EDD" round>查看详情</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.page-title { font-size: 42px; font-weight: 900; margin-bottom: 40px; color: #111; }
.discover-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(400px, 1fr)); gap: 30px; }
.discover-card { 
  padding: 40px; 
  border-radius: 24px; 
  color: #fff; 
  box-shadow: 8px 8px 0px rgba(0,0,0,0.1); 
  transition: transform 0.3s, box-shadow 0.3s; 
  display: flex;
  flex-direction: column;
}
.discover-card:hover { transform: translateY(-8px); box-shadow: 12px 12px 0px rgba(0,0,0,0.15); }
.discover-card h2 { font-size: 28px; font-weight: 900; margin-bottom: 20px; line-height: 1.4; }
.discover-card p { font-size: 18px; line-height: 1.8; opacity: 0.95; flex: 1; margin-bottom: 30px; }
.card-footer { display: flex; justify-content: space-between; align-items: center; font-size: 16px; font-weight: bold; }
.card-footer .el-button { font-size: 16px; font-weight: 800; padding: 12px 24px; height: auto; }
.color-1 { background: #FF6B6B; }
.color-2 { background: #4ECDC4; }
.color-3 { background: #45B7D1; }
.color-4 { background: #F9A03F; }
.color-5 { background: #9D4EDD; }
</style>