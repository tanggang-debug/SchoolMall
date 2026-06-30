import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const role = ref(Number(localStorage.getItem('role') || 0))

  const isLogin = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 3)
  const isMerchant = computed(() => role.value === 2)

  function setUser(data) {
    token.value = data.token
    userId.value = data.userId
    username.value = data.username
    role.value = data.role
    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', data.userId)
    localStorage.setItem('username', data.username)
    localStorage.setItem('role', data.role)
  }

  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    role.value = 0
    localStorage.clear()
  }

  return { token, userId, username, role, isLogin, isAdmin, isMerchant, setUser, logout }
})
