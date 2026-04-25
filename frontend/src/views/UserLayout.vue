<template>
  <el-container class="user-container">
    <el-header class="user-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="site-logo" @click="goHome">
            <Icon icon="mdi:movie-open" class="logo-icon" />
            冬晓影院
          </h1>
        </div>
        <div class="header-center">
          <el-menu :default-active="activeMenu" mode="horizontal" router :ellipsis="false">
            <el-menu-item index="/user/home">
              <Icon icon="mdi:home" class="menu-icon" />
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/user/movies">
              <Icon icon="mdi:movie" class="menu-icon" />
              <span>电影</span>
            </el-menu-item>
            <el-sub-menu index="/user/cinemas">
              <template #title>
                <Icon icon="mdi:theater" class="menu-icon" />
                <span>影院</span>
              </template>
              <el-menu-item index="/user/cinemas">
                <Icon icon="mdi:view-list" class="sub-menu-icon" />
                <span>全部影院</span>
              </el-menu-item>
              <el-menu-item v-for="cinema in nearbyCinemas" :key="cinema.id" :index="`/user/cinema/${cinema.id}`">
                <Icon icon="mdi:map-marker" class="sub-menu-icon" />
                <span>{{ cinema.name }}</span>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/user/ai-chat">
              <Icon icon="mdi:robot" class="menu-icon" />
              <span>AI助手</span>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="header-right">
          <template v-if="isLoggedIn">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" style="margin-right: 8px">
                  <Icon icon="mdi:account" class="avatar-icon" />
                </el-avatar>
                <span>{{ username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToTickets">
                    <Icon icon="mdi:ticket" class="dropdown-icon" />
                    我的订单
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">
                    <Icon icon="mdi:logout" class="dropdown-icon" />
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="danger" @click="goToLogin">
              <Icon icon="mdi:login" class="btn-icon" />
              登录
            </el-button>
          </template>
        </div>
      </div>
    </el-header>
    <el-main class="user-main">
      <router-view />
    </el-main>
    <el-footer class="user-footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="javascript:;">
            <Icon icon="mdi:information" class="link-icon" />
            关于我们
          </a>
          <span class="divider">|</span>
          <a href="javascript:;">
            <Icon icon="mdi:phone" class="link-icon" />
            联系我们
          </a>
          <span class="divider">|</span>
          <a href="javascript:;">
            <Icon icon="mdi:file-document" class="link-icon" />
            用户协议
          </a>
          <span class="divider">|</span>
          <a href="javascript:;">
            <Icon icon="mdi:shield-check" class="link-icon" />
            隐私政策
          </a>
        </div>
        <p class="copyright">©2026 冬晓影院 - 所有权利保留</p>
      </div>
    </el-footer>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => {
  return route.path
})

const isLoggedIn = computed(() => {
  return localStorage.getItem('token') && localStorage.getItem('role') === 'user'
})

const username = computed(() => {
  return localStorage.getItem('username') || '用户'
})

const nearbyCinemas = ref([])

const loadNearbyCinemas = async () => {
  try {
    const res = await request.get('/user/cinema/list')
    // 只显示前5个影院作为附近影院
    nearbyCinemas.value = (res.data || []).slice(0, 5)
  } catch (error) {
    console.error('加载附近影院失败:', error)
  }
}

onMounted(() => {
  loadNearbyCinemas()
})

const goHome = () => {
  router.push('/user/movies')
}

const goToLogin = () => {
  router.push('/login')
}

const goToTickets = () => {
  router.push('/user/tickets')
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  localStorage.removeItem('username')
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.user-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.user-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  height: 60px;
  padding: 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
}

.site-logo {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  font-size: 28px;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.header-center .el-menu {
  border-bottom: none;
}

.header-center .el-menu-item {
  padding: 0 20px;
}

:deep(.el-menu--horizontal > .el-menu-item) {
  display: inline-flex;
  align-items: center;
}

:deep(.el-menu--horizontal .el-sub-menu__title) {
  display: inline-flex;
  align-items: center;
}

.menu-icon {
  font-size: 18px;
  margin-right: 5px;
}

.sub-menu-icon {
  font-size: 16px;
  margin-right: 8px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f5f5;
}

.avatar-icon {
  font-size: 20px;
  color: #fff;
}

.dropdown-icon {
  font-size: 16px;
  margin-right: 8px;
}

.btn-icon {
  font-size: 16px;
  margin-right: 5px;
}

.user-main {
  flex: 1;
  padding: 0;
  background: #f5f5f5;
}

.user-footer {
  background: #262626;
  color: #fff;
  height: auto;
  padding: 30px 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
}

.footer-links {
  margin-bottom: 15px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  margin: 0 10px;
  transition: color 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.footer-links a:hover {
  color: #fff;
}

.link-icon {
  font-size: 16px;
}

.divider {
  color: rgba(255, 255, 255, 0.3);
}

.copyright {
  margin: 0;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

:deep(.el-menu-item) {
  font-size: 16px;
}

:deep(.el-menu-item.is-active) {
  color: #ff4d4f;
  border-bottom-color: #ff4d4f;
}

:deep(.el-menu-item:hover) {
  color: #ff4d4f;
}
</style>
