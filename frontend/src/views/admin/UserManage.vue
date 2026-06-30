<template>
  <div class="page">
    <div class="card">
      <div class="toolbar">
        <h2>用户管理</h2>
        <div class="toolbar-actions">
          <el-input v-model="keyword" placeholder="搜索用户名/手机号" clearable style="width: 220px" @keyup.enter="load" />
          <el-button @click="load">查询</el-button>
        </div>
      </div>
      <el-table :data="users" v-loading="loading">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="phone" label="手机" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">{{ getRoleLabel(row.role) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getUserStatusMeta(row.status).type">{{ getUserStatusMeta(row.status).label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{row}">
            <el-button link @click="toggle(row)">{{ row.status === 0 ? '冻结' : '解封' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
import { getRoleLabel, getUserStatusMeta } from '../../utils/view'

const users = ref([])
const loading = ref(false)
const keyword = ref('')

async function load() {
  loading.value = true
  try {
    const res = await request.get('/users/admin/list', { params: { page: 1, size: 50, keyword: keyword.value } })
    users.value = res.data.records || []
  } finally {
    loading.value = false
  }
}

async function toggle(row) {
  await request.put(`/users/${row.id}/status`, { status: row.status === 0 ? 1 : 0 })
  ElMessage.success('操作成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.toolbar-actions { display: flex; gap: 12px; }
h2 { margin: 0; }
</style>
