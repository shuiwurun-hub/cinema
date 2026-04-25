<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>影院票务系统</h2>
          <p class="login-subtitle">用户登录</p>
        </div>
      </template>
      
      <!-- 用户登录 -->
      <el-form :model="userLoginForm" :rules="userLoginRules" ref="userLoginFormRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userLoginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userLoginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUserLogin" style="width: 100%">登录</el-button>
        </el-form-item>
        <el-form-item class="register-link">
          <el-button type="text" @click="goToRegister">用户注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()

// 用户登录
const userLoginFormRef = ref()
const userLoginForm = ref({
  username: '',
  password: ''
})

const userLoginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleUserLogin = async () => {
  await userLoginFormRef.value.validate()
  try {
    const res = await request.post('/user/user/username/login', userLoginForm.value)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('role', 'user')
    ElMessage.success('登录成功')
    router.push('/user/movies')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请检查用户名和密码')
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0 0 10px 0;
  color: #409eff;
  font-size: 24px;
  font-weight: bold;
}

.login-subtitle {
  margin: 0 0 20px 0;
  color: #666;
  font-size: 16px;
}

.register-link {
  text-align: center;
  margin-top: 15px;
}
</style>