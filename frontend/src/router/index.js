import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'
import Login from '@/views/Login.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'movies',
        name: 'Movies',
        component: () => import('@/views/admin/Movies.vue'),
        meta: { title: '电影管理' }
      },
      {
        path: 'cinemas',
        name: 'Cinemas',
        component: () => import('@/views/admin/Cinemas.vue'),
        meta: { title: '影院管理' }
      },
      {
        path: 'halls',
        name: 'Halls',
        component: () => import('@/views/admin/Halls.vue'),
        meta: { title: '影厅管理' }
      },
      {
        path: 'schedules',
        name: 'Schedules',
        component: () => import('@/views/admin/Schedules.vue'),
        meta: { title: '排片管理' }
      },
      {
        path: 'tickets',
        name: 'Tickets',
        component: () => import('@/views/admin/Tickets.vue'),
        meta: { title: '票务管理' }
      },
      {
        path: 'user-movies',
        name: 'UserMovies',
        component: () => import('@/views/user/Movies.vue'),
        meta: { title: '电影列表' }
      },
      {
        path: 'user-cinemas',
        name: 'UserCinemas',
        component: () => import('@/views/user/Cinemas.vue'),
        meta: { title: '影院列表' }
      },
      {
        path: 'user-schedules',
        name: 'UserSchedules',
        component: () => import('@/views/user/Schedules.vue'),
        meta: { title: '排片列表' }
      },
      {
        path: 'user-tickets',
        name: 'UserTickets',
        component: () => import('@/views/user/Tickets.vue'),
        meta: { title: '我的订单' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router