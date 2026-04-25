<template>
  <div class="login-container">
    <!-- 左上角Logo -->
    <div class="logo-area">
      <Icon icon="mdi:movie-open" class="logo-icon" />
      <span class="logo-text">冬晓影院</span>
    </div>

    <!-- 主内容区 -->
    <div class="login-content">
      <!-- 左侧图片区域 -->
      <div class="left-section">
        <img src="https://picsum.photos/seed/cinema-poster/500/600" alt="电影海报" class="poster-img" />
      </div>

      <!-- 右侧登录表单 -->
      <div class="right-section">
        <div class="login-box">
          <h2 class="login-title">登录</h2>
          
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
            <el-form-item prop="username">
              <el-input 
                v-model="loginForm.username" 
                placeholder="账号"
                size="large"
              >
                <template #prefix>
                  <Icon icon="mdi:account" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="密码"
                size="large"
                show-password
              >
                <template #prefix>
                  <Icon icon="mdi:lock" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            
            <el-button 
              type="primary" 
              @click="handleLogin" 
              class="login-btn"
              size="large"
            >
              登录
            </el-button>
          </el-form>
          
          <div class="register-link">
            <el-button type="text" @click="goToRegister">
              用户注册
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const router = useRouter()

const loginFormRef = ref()
const loginForm = ref({
  username: '',
  password: ''
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await loginFormRef.value.validate()
  try {
    try {
      const adminRes = await request.post('/admin/employee/login', loginForm.value)
      localStorage.setItem('token', adminRes.data.token)
      localStorage.setItem('role', 'admin')
      ElMessage.success('登录成功')
      router.push('/admin/dashboard')
      return
    } catch (adminError) {
      try {
        const userRes = await request.post('/user/user/username/login', {
          username: loginForm.value.username,
          password: loginForm.value.password
        })
        localStorage.setItem('token', userRes.data.token)
        localStorage.setItem('role', 'user')
        localStorage.setItem('username', userRes.data.username || loginForm.value.username)
        ElMessage.success('登录成功')
        router.push('/user/movies')
        return
      } catch (userError) {
        ElMessage.error('登录失败，请检查用户名和密码')
      }
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请稍后重试')
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  background: linear-gradient(135deg, #fff5f5 0%, #ffe0e0 50%, #ffd1d1 100%);
  padding: 40px;
}

/* Logo区域 */
.logo-area {
  position: absolute;
  top: 30px;
  left: 40px;
  display: flex;
  align-items: center;
  gap: 10px;
  z-index: 10;
}

.logo-icon {
  font-size: 36px;
  color: #d32f2f;
}

.logo-text {
  font-size: 24px;
  font-weight: bold;
  color: #000;
  letter-spacing: 1px;
}

/* 主内容区 */
.login-content {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 80px;
  max-width: 1200px;
  margin: 60px auto 0;
  height: calc(100vh - 140px);
}

/* 左侧图片区域 */
.left-section {
  flex: 1;
  max-width: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.poster-img {
  width: 100%;
  max-width: 450px;
  height: auto;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
  object-fit: cover;
}

/* 右侧登录区域 */
.right-section {
  flex-shrink: 0;
}

.login-box {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 50px 45px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  width: 380px;
}

.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #000;
  text-align: center;
  margin: 0 0 35px 0;
  letter-spacing: 2px;
}

.input-icon {
  font-size: 20px;
  color: #999;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: bold;
  letter-spacing: 2px;
  margin-top: 10px;
  background: linear-gradient(135deg, #ff6b35 0%, #ff4d4f 100%);
  border: none;
  border-radius: 8px;
  color: #fff;
  transition: all 0.3s ease;
}

.login-btn:hover {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff6b35 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 77, 79, 0.4);
}

.register-link {
  text-align: center;
  margin-top: 25px;
}

.register-link :deep(.el-button) {
  color: #666;
  font-size: 14px;
}

.register-link :deep(.el-button:hover) {
  color: #ff4d4f;
}

:deep(.el-form-item) {
  margin-bottom: 28px;
}

:deep(.el-input__wrapper) {
  height: 46px;
  border-radius: 8px;
  background-color: #fafafa;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  background-color: #fff;
  box-shadow: 0 0 0 2px rgba(255, 107, 53, 0.1);
}

:deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  box-shadow: 0 0 0 2px rgba(255, 107, 53, 0.3);
}

:deep(.el-input__inner) {
  font-size: 15px;
  color: #000;
}

:deep(.el-input__inner::placeholder) {
  color: #bbb;
}

@media (max-width: 968px) {
  .login-content {
    flex-direction: column;
    gap: 40px;
    margin-top: 60px;
    height: calc(100vh - 120px);
  }
  
  .left-section {
    display: none;
  }
  
  .login-box {
    width: 100%;
    max-width: 420px;
  }
}
</style>
