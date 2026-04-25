import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import AdminLayout from '@/views/AdminLayout.vue'
import UserLayout from '@/views/UserLayout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  // 管理端路由
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'movies',
        name: 'AdminMovies',
        component: () => import('@/views/admin/Movies.vue'),
        meta: { title: '电影管理' }
      },
      {
        path: 'cinemas',
        name: 'AdminCinemas',
        component: () => import('@/views/admin/Cinemas.vue'),
        meta: { title: '影院管理' }
      },
      {
        path: 'halls',
        name: 'AdminHalls',
        component: () => import('@/views/admin/Halls.vue'),
        meta: { title: '影厅管理' }
      },
      {
        path: 'schedules',
        name: 'AdminSchedules',
        component: () => import('@/views/admin/Schedules.vue'),
        meta: { title: '排片管理' }
      },
      {
        path: 'tickets',
        name: 'AdminTickets',
        component: () => import('@/views/admin/Tickets.vue'),
        meta: { title: '票务管理' }
      }
    ]
  },
  // 用户端路由
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/home',
    children: [
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('@/views/user/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'movies',
        name: 'UserMovies',
        component: () => import('@/views/user/Movies.vue'),
        meta: { title: '电影列表' }
      },
      {
        path: 'movie/:id',
        name: 'MovieDetail',
        component: () => import('@/views/user/MovieDetail.vue'),
        meta: { title: '电影详情' }
      },
      {
        path: 'cinemas',
        name: 'UserCinemas',
        component: () => import('@/views/user/Cinemas.vue'),
        meta: { title: '影院列表' }
      },
      {
        path: 'cinema/:id',
        name: 'CinemaDetail',
        component: () => import('@/views/user/CinemaDetail.vue'),
        meta: { title: '影院详情' }
      },
      {
        path: 'schedules',
        name: 'UserSchedules',
        component: () => import('@/views/user/Schedules.vue'),
        meta: { title: '排片列表' }
      },
      {
        path: 'tickets',
        name: 'UserTickets',
        component: () => import('@/views/user/Tickets.vue'),
        meta: { title: '我的订单' }
      },
      {
        path: 'ai-chat',
        name: 'AiChat',
        component: () => import('@/views/user/AiChat.vue'),
        meta: { title: 'AI助手' }
      }
    ]
  },
  // 默认重定向到用户端首页
  {
    path: '/',
    redirect: '/user/home'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  
  if (to.path.startsWith('/admin')) {
    if (!token || role !== 'admin') {
      next('/login')
    } else {
      next()
    }
  } else if (to.path.startsWith('/user')) {
    const publicPaths = ['/user/home', '/user/movies', '/user/cinemas', '/user/cinema', '/user/schedules', '/user/movie', '/user/ai-chat']
    const isPublicPath = publicPaths.some(path => to.path.startsWith(path))
    
    if (isPublicPath) {
      next()
    } else if (!token || role !== 'user') {
      next('/login')
    } else {
      next()
    }
  } else if (to.path === '/login' || to.path === '/register') {
    if (token) {
      if (role === 'admin') {
        next('/admin/dashboard')
      } else if (role === 'user') {
        next('/user/home')
      } else {
        next()
      }
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router