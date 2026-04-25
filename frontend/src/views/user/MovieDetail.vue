<template>
  <div class="movie-detail">
    <div class="page-header">
      <el-button class="back-btn" @click="goBack">
        <Icon icon="mdi:arrow-left" />
        返回
      </el-button>
    </div>

    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading" :size="50">
        <Loading />
      </el-icon>
    </div>

    <div v-else-if="movie" class="detail-container">
      <div class="movie-header">
        <div class="movie-poster">
          <img :src="movie.image" :alt="movie.name" />
        </div>
        <div class="movie-info">
          <h1 class="movie-title">{{ movie.name }}</h1>
          <div class="movie-meta">
            <span class="rating">
              <Icon icon="mdi:star" class="star-icon" />
              {{ movie.rating || '暂无评分' }}
            </span>
            <span v-if="userRating" class="user-rating">
              <Icon icon="mdi:star" class="user-star-icon" />
              我的评分: {{ userRating.rating }}星
            </span>
            <span class="category">{{ movie.categoryName }}</span>
            <span class="duration">{{ movie.duration }}分钟</span>
          </div>
          <div class="movie-details">
            <p><strong>导演：</strong>{{ movie.director }}</p>
            <p><strong>主演：</strong>{{ movie.actors }}</p>
            <p><strong>上映日期：</strong>{{ formatDate(movie.releaseDate) }}</p>
            <p><strong>票价：</strong><span class="price">¥{{ movie.price }}</span></p>
          </div>
          <div class="movie-actions">
            <el-button type="primary" size="large" @click="openRatingDialog">
              <Icon icon="mdi:star" class="btn-icon" />
              评分
            </el-button>
            <el-button type="danger" size="large" @click="buyTicket">
              <Icon icon="mdi:ticket" class="btn-icon" />
              立即购票
            </el-button>
          </div>
        </div>
      </div>

      <div class="movie-content">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="剧情简介" name="synopsis">
            <div class="synopsis-content">
              <p>{{ movie.description || '暂无剧情简介' }}</p>
            </div>
          </el-tab-pane>

          <el-tab-pane label="演职人员" name="cast">
            <div class="cast-section">
              <div class="cast-item">
                <div class="cast-avatar">
                  <Icon icon="mdi:account" :size="60" />
                </div>
                <div class="cast-info">
                  <h4>{{ movie.director }}</h4>
                  <p>导演</p>
                </div>
              </div>
              <div v-for="(actor, index) in actorsList" :key="index" class="cast-item">
                <div class="cast-avatar">
                  <Icon icon="mdi:account" :size="60" />
                </div>
                <div class="cast-info">
                  <h4>{{ actor.name }}</h4>
                  <p>饰：{{ actor.role }}</p>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="影片资料" name="info">
            <div class="info-section">
              <div class="info-item">
                <span class="info-label">出品发行</span>
                <span class="info-value">冬晓影业有限公司</span>
              </div>
              <div class="info-item">
                <span class="info-label">技术参数</span>
                <span class="info-value">{{ movie.duration }}分钟</span>
              </div>
              <div class="info-item">
                <span class="info-label">上映日期</span>
                <span class="info-value">{{ formatDate(movie.releaseDate) }}</span>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div v-else class="error-container">
      <el-empty description="电影不存在" />
    </div>

    <!-- 评分弹窗 -->
    <el-dialog
      v-model="ratingDialogVisible"
      title="电影评分"
      width="500px"
      destroy-on-close
    >
      <div class="rating-dialog-content">
        <div class="rating-section">
          <h3>请为《{{ movie?.name }}》评分</h3>
          <div class="rating-stars">
            <el-rate
              v-model="ratingValue"
              :max="5"
              :colors="['#999', '#999', '#ff9900', '#ff9900', '#ff9900']"
              :texts="['很差', '较差', '一般', '良好', '优秀']"
              show-text
            />
          </div>
        </div>
        <div class="comment-section">
          <h4>写评语</h4>
          <el-input
            v-model="comment"
            type="textarea"
            :rows="4"
            placeholder="请输入您的评语..."
          />
          <div class="ai-button-container">
            <el-button
              type="info"
              @click="generateAiComment"
              :loading="aiLoading"
            >
              AI写
            </el-button>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="ratingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRating">提交评分</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Loading } from '@element-plus/icons-vue'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const movie = ref(null)
const loading = ref(true)
const activeTab = ref('synopsis')

// 评分相关变量
const ratingDialogVisible = ref(false)
const ratingValue = ref(0)
const comment = ref('')
const aiLoading = ref(false)
const userRating = ref(null)

const actorsList = computed(() => {
  if (!movie.value || !movie.value.actors) return []
  return movie.value.actors.split(/[,，、]/).map(actor => {
    const parts = actor.trim().split(/饰[：:]/)
    return {
      name: parts[0].trim(),
      role: parts[1] ? parts[1].trim() : ''
    }
  }).filter(actor => actor.name)
})

const loadMovieDetail = async () => {
  const movieId = route.params.id
  if (!movieId) {
    loading.value = false
    return
  }

  try {
    const res = await request.get(`/user/movie/${movieId}`)
    movie.value = res.data
    
    // 加载用户评分
    await loadUserRating()
  } catch (error) {
    console.error('加载电影详情失败:', error)
  } finally {
    loading.value = false
  }
}

const loadUserRating = async () => {
  if (!movie.value) return
  
  try {
    const res = await request.get(`/user/rating/${movie.value.id}`)
    userRating.value = res.data
  } catch (error) {
    // 用户未评分或加载失败，不显示错误
    console.log('用户未评分或加载失败')
    userRating.value = null
  }
}

const formatDate = (date) => {
  if (!date) return '暂无'
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const buyTicket = () => {
  router.push(`/user/schedules?movieId=${movie.value.id}`)
}

// 评分相关方法
const openRatingDialog = async () => {
  // 如果已经有用户评分，则显示之前的评分
  if (userRating.value) {
    ratingValue.value = userRating.value.rating
    comment.value = userRating.value.comment || ''
  } else {
    ratingValue.value = 0
    comment.value = ''
  }
  ratingDialogVisible.value = true
}

const generateAiComment = async () => {
  if (!movie.value) return
  
  aiLoading.value = true
  try {
    const memoryId = `rating_${movie.value.id}_${Date.now()}`
    const message = `请为电影《${movie.value.name}》写一段评语，基于以下信息：\n导演：${movie.value.director}\n主演：${movie.value.actors}\n类型：${movie.value.categoryName || '未知'}\n时长：${movie.value.duration}分钟\n上映日期：${formatDate(movie.value.releaseDate)}`
    
    console.log('调用AI接口:', {
      memoryId,
      message
    })
    
    // 直接使用fetch调用，绕过axios的响应拦截器，以便查看完整的错误信息
    const response = await fetch(`/api/user/ai/review?memoryId=${memoryId}&message=${encodeURIComponent(message)}`)
    
    console.log('响应状态:', response.status)
    console.log('响应状态文本:', response.statusText)
    
    if (!response.ok) {
      const errorText = await response.text()
      console.error('API错误响应:', errorText)
      throw new Error(`API请求失败: ${response.status} ${response.statusText}\n${errorText}`)
    }
    
    const aiComment = await response.text()
    console.log('AI生成的评语:', aiComment)
    
    if (!aiComment) {
      throw new Error('AI生成的评语为空')
    }
    
    comment.value = aiComment
    console.log('评语已设置到表单:', comment.value)
  } catch (error) {
    console.error('生成AI评语失败:', error)
    console.error('错误详情:', error.stack)
    ElMessage.error(`生成评语失败: ${error.message || '未知错误'}`)
  } finally {
    aiLoading.value = false
    console.log('AI生成评语操作完成')
  }
}

const submitRating = async () => {
  if (ratingValue.value === 0) {
    ElMessage.warning('请选择评分')
    return
  }
  
  try {
    await request.post('/user/rating', {
      movieId: movie.value.id,
      rating: ratingValue.value,
      comment: comment.value
    })
    
    ElMessage.success('评分提交成功')
    ratingDialogVisible.value = false
    
    // 重新加载用户评分
    await loadUserRating()
  } catch (error) {
    console.error('提交评分失败:', error)
    ElMessage.error('提交评分失败，请重试')
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  loadMovieDetail()
})
</script>

<style scoped>
.movie-detail {
  min-height: calc(100vh - 60px);
  background: #f5f5f5;
}

.page-header {
  background: white;
  padding: 15px 20px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  color: #666;
  border: 1px solid #dcdfe6;
  background: white;
}

.back-btn:hover {
  color: #ff4d4f;
  border-color: #ff4d4f;
  background: #fff1f0;
}

.loading-container,
.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.movie-header {
  display: flex;
  gap: 30px;
  background: white;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.movie-poster {
  flex-shrink: 0;
  width: 300px;
  height: 420px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.movie-title {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.movie-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 16px;
  color: #666;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #ff9900;
  font-weight: bold;
  font-size: 18px;
}

.star-icon {
  color: #ff9900;
}

.user-rating {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #1890ff;
  font-weight: bold;
  font-size: 16px;
  padding: 4px 12px;
  background: #e6f7ff;
  border-radius: 4px;
}

.user-star-icon {
  color: #1890ff;
}

.category,
.duration {
  padding: 4px 12px;
  background: #f0f0f0;
  border-radius: 4px;
}

.movie-details {
  margin-bottom: 30px;
}

.movie-details p {
  margin: 10px 0;
  font-size: 15px;
  color: #666;
  line-height: 1.8;
}

.price {
  color: #ff4d4f;
  font-size: 24px;
  font-weight: bold;
}

.movie-actions {
  display: flex;
  gap: 15px;
  margin-top: auto;
}

.btn-icon {
  margin-right: 5px;
}

.movie-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.synopsis-content {
  line-height: 1.8;
  font-size: 15px;
  color: #666;
  text-align: justify;
}

.cast-section {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.cast-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 8px;
}

.cast-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.cast-info h4 {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #333;
}

.cast-info p {
  margin: 0;
  font-size: 14px;
  color: #999;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: flex;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 8px;
}

.info-label {
  width: 120px;
  font-weight: bold;
  color: #333;
}

.info-value {
  flex: 1;
  color: #666;
}

@media (max-width: 768px) {
  .movie-header {
    flex-direction: column;
    padding: 20px;
  }

  .movie-poster {
    width: 100%;
    max-width: 300px;
    height: auto;
    aspect-ratio: 2/3;
    margin: 0 auto;
  }

  .movie-title {
    font-size: 24px;
  }

  .movie-meta {
    flex-wrap: wrap;
    gap: 10px;
  }

  .movie-actions {
    flex-direction: column;
  }

  .cast-section {
    grid-template-columns: 1fr;
  }
}

/* 评分弹窗样式 */
.rating-dialog-content {
  padding: 10px 0;
}

.rating-section {
  margin-bottom: 20px;
}

.rating-section h3 {
  margin-bottom: 15px;
  font-size: 18px;
  color: #333;
}

.rating-stars {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.comment-section {
  margin-top: 20px;
}

.comment-section h4 {
  margin-bottom: 10px;
  font-size: 16px;
  color: #333;
}

.ai-button-container {
  display: flex;
  justify-content: flex-start;
  margin-top: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
