<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>消息通知</h2>
        <el-button link @click="load">刷新</el-button>
      </div>
      <div v-if="list.length" class="notice-list" v-loading="loading">
        <div
          v-for="item in list"
          :key="item.id"
          class="notice-item"
          :class="{ unread: item.status === 0 }"
        >
          <div class="notice-head">
            <div class="title-wrap">
              <strong>{{ item.title }}</strong>
              <el-tag :type="item.status === 0 ? 'warning' : 'info'" size="small">
                {{ item.status === 0 ? '未读' : '已读' }}
              </el-tag>
            </div>
            <span class="time">{{ item.createTime }}</span>
          </div>
          <p>{{ item.content }}</p>
          <el-button v-if="item.status === 0" link type="primary" @click="markRead(item)">标记已读</el-button>
        </div>
      </div>
      <el-empty v-else description="暂无通知消息" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const list = ref([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const res = await request.get('/notifications', { params: { page: 1, size: 20 } })
    list.value = res.data.records || []
  } finally {
    loading.value = false
  }
}

async function markRead(item) {
  await request.put(`/notifications/${item.id}/read`)
  item.status = 1
  ElMessage.success('已标记为已读')
}

onMounted(load)
</script>

<style scoped>
.toolbar, .notice-head { display: flex; align-items: center; justify-content: space-between; }
.toolbar { margin-bottom: 16px; }
.notice-list { display: flex; flex-direction: column; gap: 12px; }
.notice-item { padding: 16px; border: 1px solid #ebeef5; border-radius: 8px; }
.notice-item.unread { border-color: #409eff; background: #f0f7ff; }
.title-wrap { display: flex; align-items: center; gap: 12px; }
.notice-item p { margin: 12px 0; color: #606266; line-height: 1.7; }
.time { color: #909399; font-size: 13px; }
h2 { margin: 0; }
</style>
