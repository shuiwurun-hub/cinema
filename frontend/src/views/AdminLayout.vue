<template>
  <el-container>
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <h1>冬晓影院</h1>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="el-menu-vertical"
        background-color="#1d2129"
        text-color="#fff"
        active-text-color="#ff4d4f"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/admin/movies">
          <el-icon><Picture /></el-icon>
          <span>电影管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/cinemas">
          <el-icon><OfficeBuilding /></el-icon>
          <span>影院管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/halls">
          <el-icon><Monitor /></el-icon>
          <span>影厅管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/schedules">
          <el-icon><Calendar /></el-icon>
          <span>排片管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/tickets">
          <el-icon><Ticket /></el-icon>
          <span>票务管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="header-left">
            <h2 class="site-title">管理端</h2>
          </div>
          <div class="header-right">
            <el-button type="primary" @click="logout" round>
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-button>
          </div>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { HomeFilled, Picture, OfficeBuilding, Monitor, Calendar, Ticket, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => {
  return route.path
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  router.push('/login')
}
</script>

<style scoped>
.el-container {
  height: 100vh;
  background-color: #f5f5f5;
}

.sidebar {
  background-color: #1d2129;
  color: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
}

.logo {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #303641;
}

.logo h1 {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
  color: #ff4d4f;
}

.el-menu {
  border-right: none;
  height: calc(100vh - 80px);
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  padding: 0 30px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 64px;
}

.site-title {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
  color: #ff4d4f;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.main-content {
  background-color: #f5f5f5;
  padding: 24px;
  overflow-y: auto;
}
</style>