import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    children: [
      { path: '', name: 'home', component: () => import('../views/Home.vue') },
      { path: 'product/:id', component: () => import('../views/ProductDetail.vue') },
      { path: 'cart', component: () => import('../views/Cart.vue'), meta: { auth: true } },
      { path: 'order/confirm', component: () => import('../views/OrderConfirm.vue'), meta: { auth: true } },
      { path: 'payment/:orderNo', component: () => import('../views/Payment.vue'), meta: { auth: true } },
      { path: 'orders', component: () => import('../views/OrderList.vue'), meta: { auth: true } },
      { path: 'orders/:id', component: () => import('../views/OrderDetail.vue'), meta: { auth: true } },
      { path: 'review/:orderId/:productId', component: () => import('../views/Review.vue'), meta: { auth: true } },
      { path: 'notifications', component: () => import('../views/Notifications.vue'), meta: { auth: true } },
      { path: 'profile', component: () => import('../views/Profile.vue'), meta: { auth: true } },
      { path: 'help', component: () => import('../views/Help.vue') },
      { path: 'about', component: () => import('../views/About.vue') },
      { path: 'merchant/products', component: () => import('../views/merchant/ProductManage.vue'), meta: { auth: true, role: 2 } },
      { path: 'merchant/orders', component: () => import('../views/merchant/OrderManage.vue'), meta: { auth: true, role: 2 } },
      { path: 'merchant/analytics', component: () => import('../views/merchant/Analytics.vue'), meta: { auth: true, role: 2 } },
      { path: 'admin/users', component: () => import('../views/admin/UserManage.vue'), meta: { auth: true, role: 3 } },
      { path: 'admin/audit', component: () => import('../views/admin/ProductAudit.vue'), meta: { auth: true, role: 3 } },
      { path: 'admin/orders', component: () => import('../views/admin/OrderMonitor.vue'), meta: { auth: true, role: 3 } },
      { path: 'admin/analytics', component: () => import('../views/admin/Analytics.vue'), meta: { auth: true, role: 3 } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const user = useUserStore()
  if (to.meta.auth && !user.isLogin) return next('/login')
  if (to.meta.role !== undefined && user.role !== to.meta.role && user.role !== 3) return next('/')
  next()
})

export default router
