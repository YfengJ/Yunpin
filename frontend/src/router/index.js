import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { name: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { name: '注册新账号' }
  },
  {
    path: '/dashboard',
    name: 'Layout',
    component: Layout,
    redirect: '/dashboard/home',
    children: [
      {
        path: 'home',
        name: 'DashboardHome',
        component: Home,
        meta: { name: '📊 仪表盘' }
      },
      {
        path: 'job-hall',
        name: 'JobHall',
        component: () => import('../views/JobHall.vue'),
        meta: { name: '💼 职位大厅' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { name: '👤 个人简历中心' }
      },
      {
        path: 'application',
        name: 'ApplicationList',
        component: () => import('../views/ApplicationList.vue'),
        meta: { name: '📄 申请与审核管理' }
      },
      {
        path: 'company',
        name: 'CompanyManagement',
        component: () => import('../views/CompanyManagement.vue'),
        meta: { name: '🏢 合作企业管理' }
      },
      // 🔥🔥🔥 新增：管理员用户管理路由 🔥🔥🔥
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('../views/UserManagement.vue'),
        meta: { name: '👥 用户账户管理' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if ((to.path === '/login' || to.path === '/register') && token) {
    next('/dashboard/home')
    return
  }

  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
    return
  }

  if (to.meta.name) {
    document.title = `${to.meta.name} - 云聘平台`
  }
  next()
})

export default router