<template>
  <div class="page">
    <div class="points-header">
      <div class="points-info">
        <h1>🎁 积分商城</h1>
        <p>当前可用积分：<span class="points-num">8,500</span></p>
      </div>
      <el-button size="large" color="#fff" class="earn-btn">赚取更多积分</el-button>
    </div>
    
    <div class="points-grid">
      <div class="points-item" v-for="i in 8" :key="i">
        <div class="item-img-placeholder">精美礼品 {{ i }}</div>
        <h3 class="item-title">定制校园文创帆布袋 / 纪念品</h3>
        <div class="item-bottom">
          <span class="cost">{{ i * 500 + 1000 }} 积分</span>
          <el-button type="primary" size="large" class="exchange-btn">立即兑换</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.points-header { 
  background: #FFD166; color: #111; padding: 60px 50px; border-radius: 24px; 
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 50px;
  box-shadow: 6px 6px 0px #EF476F;
}
.points-info h1 { font-size: 48px; font-weight: 900; margin: 0 0 20px; }
.points-info p { font-size: 26px; font-weight: 700; margin: 0; }
.points-num { color: #EF476F; font-weight: 900; font-size: 36px; margin-left: 10px; }
.earn-btn { font-size: 20px; font-weight: 900; color: #EF476F; height: 60px; padding: 0 30px; border-radius: 16px; border: 3px solid #EF476F; }
.earn-btn:hover { background: #EF476F; color: #fff; }

.points-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 40px; }
.points-item { 
  background: #fff; padding: 30px; border-radius: 24px; 
  box-shadow: 0 10px 30px rgba(0,0,0,0.08); border: 3px solid #eee; transition: transform 0.2s;
}
.points-item:hover { transform: translateY(-8px); border-color: #FFD166; }
.item-img-placeholder { 
  height: 240px; background: #F8F9FA; border-radius: 16px; 
  display: flex; align-items: center; justify-content: center; font-size: 28px; font-weight: 800; color: #ccc; margin-bottom: 24px; 
}
.item-title { font-size: 22px; font-weight: 900; margin-bottom: 24px; color: #222; line-height: 1.5; }
.item-bottom { display: flex; justify-content: space-between; align-items: center; }
.cost { font-size: 26px; font-weight: 900; color: #EF476F; }
.exchange-btn { font-size: 18px; font-weight: 800; border-radius: 12px; background: #118AB2; border: none; }
.exchange-btn:hover { background: #0c6a8a; }
</style>