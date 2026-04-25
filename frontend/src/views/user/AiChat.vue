<template>
  <div class="ai-chat-page">
    <el-card class="chat-container">
      <template #header>
        <div class="chat-header">
          <div class="header-left">
            <el-icon :size="24" color="#409EFF">
              <ChatDotRound />
            </el-icon>
            <span class="title">AI智能助手</span>
          </div>
          <el-button text @click="clearHistory">
            <el-icon><Delete /></el-icon>
            清空对话
          </el-button>
        </div>
      </template>

      <div class="chat-messages" ref="messagesContainer">
        <div v-if="messages.length === 0" class="empty-state">
          <el-icon :size="60" color="#C0C4CC">
            <ChatDotRound />
          </el-icon>
          <p>你好！我是冬晓影院AI助手</p>
          <p class="tip">我可以帮你推荐电影、查询影院信息、生成影评等</p>
        </div>

        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message', message.role]"
        >
          <div class="message-avatar">
            <el-avatar
              v-if="message.role === 'user'"
              :size="36"
              style="background: #409EFF"
            >
              我
            </el-avatar>
            <el-avatar
              v-else
              :size="36"
              style="background: #67C23A"
            >
              AI
            </el-avatar>
          </div>
          <div class="message-content">
            <div class="message-text">{{ message.content }}</div>
          </div>
        </div>

        <div v-if="loading" class="message assistant">
          <div class="message-avatar">
            <el-avatar :size="36" style="background: #67C23A">AI</el-avatar>
          </div>
          <div class="message-content">
            <div class="message-text typing">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
            </div>
          </div>
        </div>
      </div>

      <div class="chat-input">
        <el-input
          v-model="inputMessage"
          placeholder="输入你的问题，例如：推荐一部好看的电影"
          @keyup.enter="sendMessage"
          :disabled="loading"
        >
          <template #append>
            <el-button
              type="primary"
              @click="sendMessage"
              :loading="loading"
              :disabled="!inputMessage.trim()"
            >
              发送
            </el-button>
          </template>
        </el-input>
      </div>

      <div class="quick-questions">
        <el-tag
          v-for="question in quickQuestions"
          :key="question"
          @click="askQuickQuestion(question)"
          class="quick-tag"
          effect="plain"
        >
          {{ question }}
        </el-tag>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ChatDotRound, Delete } from '@element-plus/icons-vue'
import request from '@/utils/request'

const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messagesContainer = ref(null)

const memoryId = 'chat-' + Date.now()

const quickQuestions = [
  '推荐一部评分高的电影',
  '最近有什么好看的电影',
  '附近有什么影院',
  '帮我写个影评'
]

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  const message = inputMessage.value.trim()
  if (!message || loading.value) return

  messages.value.push({
    role: 'user',
    content: message
  })

  inputMessage.value = ''
  loading.value = true
  scrollToBottom()

  try {
    const response = await fetch('/api/user/chat/message', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: `memoryId=${encodeURIComponent(memoryId)}&message=${encodeURIComponent(message)}`
    })

    if (!response.ok) {
      throw new Error('请求失败')
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let aiMessage = ''

    messages.value.push({
      role: 'assistant',
      content: ''
    })

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      const chunk = decoder.decode(value, { stream: true })
      aiMessage += chunk
      messages.value[messages.value.length - 1].content = aiMessage
      scrollToBottom()
    }

    loading.value = false
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败，请重试')
    loading.value = false
    messages.value.pop()
  }
}

const askQuickQuestion = (question) => {
  inputMessage.value = question
  sendMessage()
}

const clearHistory = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有对话记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await request.delete(`/user/chat/history/${memoryId}`)
    messages.value = []
    ElMessage.success('对话已清空')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空对话失败:', error)
    }
  }
}
</script>

<style scoped>
.ai-chat-page {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.chat-container {
  height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 20px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #909399;
}

.empty-state p {
  margin: 10px 0;
  font-size: 16px;
}

.empty-state .tip {
  font-size: 14px;
  color: #C0C4CC;
}

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  flex: 1;
  max-width: 70%;
}

.message.user .message-content {
  display: flex;
  justify-content: flex-end;
}

.message-text {
  padding: 12px 16px;
  border-radius: 8px;
  line-height: 1.6;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.message.user .message-text {
  background: #409EFF;
  color: white;
}

.message.assistant .message-text {
  background: white;
  color: #303133;
  border: 1px solid #E4E7ED;
}

.typing {
  display: flex;
  gap: 4px;
  padding: 16px;
}

.dot {
  width: 8px;
  height: 8px;
  background: #67C23A;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out;
}

.dot:nth-child(1) {
  animation-delay: 0s;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

.chat-input {
  margin-bottom: 15px;
}

.quick-questions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.quick-tag {
  cursor: pointer;
  transition: all 0.3s;
}

.quick-tag:hover {
  background: #409EFF;
  color: white;
  border-color: #409EFF;
}
</style>
