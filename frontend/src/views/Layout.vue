<template>
  <el-container>
    <el-aside width="200px">
      <el-menu
        :default-active="activeMenu"
        router
        class="el-menu-vertical"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="admin">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>管理端</span>
          </template>
          <el-menu-item index="/movies">电影管理</el-menu-item>
          <el-menu-item index="/cinemas">影院管理</el-menu-item>
          <el-menu-item index="/halls">影厅管理</el-menu-item>
          <el-menu-item index="/schedules">排片管理</el-menu-item>
          <el-menu-item index="/tickets">票务管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户端</span>
          </template>
          <el-menu-item index="/user-movies">电影列表</el-menu-item>
          <el-menu-item index="/user-cinemas">影院列表</el-menu-item>
          <el-menu-item index="/user-schedules">排片列表</el-menu-item>
          <el-menu-item index="/user-tickets">我的订单</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>影院票务系统</h2>
          <el-button type="danger" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { HomeFilled, Setting, User } from '@element-plus/icons-vue'

const router = useRouter()
const activeMenu = ref('/dashboard')

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
.el-container {
  height: 100vh;
}

.el-aside {
  background-color: #545c64;
  color: #fff;
}

.el-menu {
  border-right: none;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.header-content h2 {
  margin: 0;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>