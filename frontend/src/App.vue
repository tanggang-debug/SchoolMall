<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
body { 
  font-family: 'Inter', 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif; 
  background: #f4f6f8; 
  color: #1d1d1f; 
  -webkit-font-smoothing: antialiased;
}
a { text-decoration: none; color: inherit; transition: color 0.2s ease; }
a:hover { color: #409eff; }
.page { max-width: 1200px; margin: 0 auto; padding: 24px 20px; min-height: calc(100vh - 60px); }
.card { 
  background: #fff; 
  border-radius: 12px; 
  padding: 24px; 
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04); 
  transition: box-shadow 0.3s ease;
}
.card:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
