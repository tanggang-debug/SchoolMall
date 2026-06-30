<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
body { 
  font-family: 'Inter', 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  background: #fafafa;
  color: #18181b;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-size: 16px;
  line-height: 1.7;
  overflow-x: hidden;
}
a { text-decoration: none; color: inherit; transition: all 0.3s ease-in-out; }
a:hover { color: #18181b; }

.page { 
  max-width: 1400px; 
  margin: 0 auto; 
  padding: 40px 30px; 
  min-height: calc(100vh - 80px); 
}

.card { 
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  border-radius: 16px;
  padding: 32px;
  transition: all 0.3s ease-in-out;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04), 
               0 1px 2px -1px rgba(0, 0, 0, 0.04);
}
.card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 
               0 2px 4px -2px rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease-in-out, transform 0.3s ease-in-out;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

/* 全局字体 - 大字重标题，强调行高和呼吸感 */
h1 { 
  font-size: 48px; 
  font-weight: 700;
  letter-spacing: -0.025em;
  line-height: 1.3;
  color: #18181b;
}
h2 { 
  font-size: 36px; 
  font-weight: 600;
  letter-spacing: -0.02em;
  line-height: 1.4;
  color: #18181b;
}
h3 { 
  font-size: 28px; 
  font-weight: 600;
  letter-spacing: -0.01em;
  line-height: 1.5;
  color: #18181b;
}
h4 { 
  font-size: 22px; 
  font-weight: 600;
  line-height: 1.5;
  color: #18181b;
}
p { 
  font-size: 16px; 
  font-weight: 400;
  line-height: 1.7;
  color: #3f3f46;
}

/* 全局渐变效果 - 细腻的灰度渐变 */
.gradient-text {
  background: linear-gradient(135deg, #18181b 0%, #71717a 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 滚动条样式 - 极简风格 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-track {
  background: #fafafa;
  border-radius: 3px;
}
::-webkit-scrollbar-thumb {
  background: #d4d4d8;
  border-radius: 3px;
  transition: background 0.3s ease-in-out;
}
::-webkit-scrollbar-thumb:hover {
  background: #a1a1aa;
}

/* 按钮基础样式 - 丝滑过渡 */
.el-button {
  transition: all 0.3s ease-in-out;
  font-weight: 600;
  letter-spacing: 0.01em;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}
.el-button:hover {
  transform: translateY(-0.5px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}
.el-button:active {
  transform: translateY(0);
}

/* Element Plus 浅色主题调整 */
.el-radio-button__inner {
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  color: #18181b;
  transition: all 0.3s ease-in-out;
  font-weight: 500;
}
.el-radio-button__original-radio:checked + .el-radio-button__inner {
  background: #18181b;
  border-color: #18181b;
  color: #ffffff;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
}
.el-radio-button__inner:hover {
  background: #fafafa;
  border-color: rgba(228, 228, 231, 0.9);
}

/* 输入框样式 */
.el-input__wrapper {
  background: #ffffff;
  border: 1px solid rgba(228, 228, 231, 0.8);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.02);
  transition: all 0.3s ease-in-out;
}
.el-input__wrapper:hover {
  border-color: rgba(228, 228, 231, 0.9);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04);
}
.el-input__wrapper:focus-within {
  border-color: #18181b;
  box-shadow: 0 0 0 1px rgba(24, 24, 27, 0.1);
}
.el-input__inner {
  color: #18181b;
  font-weight: 400;
}
.el-input__inner::placeholder {
  color: #a1a1aa;
}

/* Empty 组件样式 */
.el-empty {
  color: #71717a;
}
</style>
