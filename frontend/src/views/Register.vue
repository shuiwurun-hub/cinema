<template>
  <div class="register-container">
    <!-- 左上角Logo -->
    <div class="logo-area">
      <Icon icon="mdi:movie-open" class="logo-icon" />
      <span class="logo-text">冬晓影院</span>
    </div>

    <!-- 主内容区 -->
    <div class="register-content">
      <!-- 左侧图片区域 -->
      <div class="left-section">
        <img src="https://picsum.photos/seed/cinema-register/500/600" alt="电影海报" class="poster-img" />
      </div>

      <!-- 右侧注册表单 -->
      <div class="right-section">
        <div class="register-box">
          <h2 class="register-title">注册</h2>
          
          <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef">
            <el-form-item prop="username">
              <el-input 
                v-model="registerForm.username" 
                placeholder="用户名"
                size="large"
              >
                <template #prefix>
                  <Icon icon="mdi:account" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input 
                v-model="registerForm.password" 
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
            
            <el-form-item prop="confirmPassword">
              <el-input 
                v-model="registerForm.confirmPassword" 
                type="password" 
                placeholder="确认密码"
                size="large"
                show-password
              >
                <template #prefix>
                  <Icon icon="mdi:lock-check" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="email">
              <el-input 
                v-model="registerForm.email" 
                placeholder="邮箱"
                size="large"
              >
                <template #prefix>
                  <Icon icon="mdi:email" class="input-icon" />
                </template>
              </el-input>
            </el-form-item>
            
            <el-button 
              type="primary" 
              @click="handleRegister" 
              class="register-btn"
              size="large"
            >
              注册
            </el-button>
          </el-form>
          
          <div class="login-link">
            <el-button type="text" @click="goToLogin">
              返回登录
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

const registerFormRef = ref()
const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: ''
})

const registerRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  await registerFormRef.value.validate()
  try {
    const registerData = {
      username: registerForm.value.username,
      password: registerForm.value.password,
      email: registerForm.value.email
    }
    const res = await request.post('/user/user/register', registerData)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error('注册失败，请稍后重试')
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
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
.register-content {
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

/* 右侧注册区域 */
.right-section {
  flex-shrink: 0;
}

.register-box {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 40px 45px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  width: 380px;
}

.register-title {
  font-size: 28px;
  font-weight: bold;
  color: #000;
  text-align: center;
  margin: 0 0 25px 0;
  letter-spacing: 2px;
}

.input-icon {
  font-size: 20px;
  color: #999;
}

.register-btn {
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

.register-btn:hover {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff6b35 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 77, 79, 0.4);
}

.login-link {
  text-align: center;
  margin-top: 20px;
}

.login-link :deep(.el-button) {
  color: #666;
  font-size: 14px;
}

.login-link :deep(.el-button:hover) {
  color: #ff4d4f;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
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
  .register-content {
    flex-direction: column;
    gap: 40px;
    margin-top: 60px;
    height: calc(100vh - 120px);
  }
  
  .left-section {
    display: none;
  }
  
  .register-box {
    width: 100%;
    max-width: 420px;
  }
}
</style>
