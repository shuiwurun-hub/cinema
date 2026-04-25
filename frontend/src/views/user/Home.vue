<template>
  <div class="home-page">
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>欢迎来到冬晓影院</h1>
          <p>发现最新最热的电影，享受极致观影体验</p>
          <div class="welcome-actions">
            <el-button type="danger" size="large" @click="goToMovies">
              <Icon icon="mdi:movie-open" class="btn-icon" />
              浏览电影
            </el-button>
            <el-button size="large" @click="goToCinemas">
              <Icon icon="mdi:theater" class="btn-icon" />
              查找影院
            </el-button>
          </div>
        </div>
        <div class="welcome-illustration">
          <img src="https://picsum.photos/seed/movie-night/600/400" alt="Movie Night" class="illustration-img" />
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="section">
        <div class="section-header">
          <div class="section-title">
            <Icon icon="mdi:fire" class="title-icon" />
            <h3>正在热映</h3>
            <span class="movie-count">{{ hotMovies.length }}部</span>
          </div>
          <el-button type="text" @click="goToMovies('hot')">
            更多
            <Icon icon="mdi:arrow-right" class="arrow-icon" />
          </el-button>
        </div>

        <div class="movie-grid">
          <div
            v-for="item in hotMovies"
            :key="item.id"
            class="movie-item"
            @click="handleView(item)"
          >
            <div class="movie-poster">
              <img :src="item.image" :alt="item.name" />
              <div class="movie-rating-badge">
                <Icon icon="mdi:star" class="star-icon-small" />
                <span class="rating-num">{{ item.rating }}</span>
                <span class="rating-unit">分</span>
              </div>
              <div class="movie-hover-mask">
                <el-button type="danger" size="large" round>
                  <Icon icon="mdi:ticket-outline" class="btn-icon" />
                  立即购票
                </el-button>
              </div>
            </div>
            <div class="movie-info">
              <h4 class="movie-name">{{ item.name }}</h4>
              <div class="movie-meta">
                <span class="movie-category">
                  <Icon icon="mdi:tag" class="meta-icon" />
                  {{ item.category }}
                </span>
                <span class="movie-duration">
                  <Icon icon="mdi:clock-outline" class="meta-icon" />
                  {{ item.duration }}分钟
                </span>
              </div>
            </div>
          </div>
          <div v-if="hotMovies.length === 0" class="empty-state">
            <img src="https://picsum.photos/seed/empty-movies/300/200" alt="No Movies" class="empty-illustration" />
            <el-empty description="暂无热映电影数据" />
          </div>
        </div>
      </div>

      <div class="section">
        <div class="section-header">
          <div class="section-title">
            <Icon icon="mdi:clock-outline" class="title-icon" />
            <h3>即将上映</h3>
            <span class="movie-count">{{ upcomingMovies.length }}部</span>
          </div>
          <el-button type="text" @click="goToMovies('upcoming')">
            更多
            <Icon icon="mdi:arrow-right" class="arrow-icon" />
          </el-button>
        </div>

        <div class="movie-grid">
          <div
            v-for="item in upcomingMovies"
            :key="item.id"
            class="movie-item"
            @click="handleView(item)"
          >
            <div class="movie-poster">
              <img :src="item.image" :alt="item.name" />
              <div class="movie-status-badge">
                <Icon icon="mdi:calendar" class="status-icon" />
                {{ item.releaseDate || '即将上映' }}
              </div>
              <div class="movie-hover-mask">
                <el-button type="primary" size="large" round>
                  <Icon icon="mdi:bell-outline" class="btn-icon" />
                  想看
                </el-button>
              </div>
            </div>
            <div class="movie-info">
              <h4 class="movie-name">{{ item.name }}</h4>
              <div class="movie-meta">
                <span class="movie-category">
                  <Icon icon="mdi:tag" class="meta-icon" />
                  {{ item.category }}
                </span>
                <span class="movie-duration">
                  <Icon icon="mdi:clock-outline" class="meta-icon" />
                  {{ item.duration }}分钟
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const router = useRouter()
const hotMovies = ref([])
const upcomingMovies = ref([])

const loadHotMovies = async () => {
  try {
    const res = await request.get('/user/movie/list', { params: { status: 1 } })
    const movies = res.data || []
    
    console.log('首页 - 接收到电影数据:', movies)
    
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    
    const hotMoviesList = movies.filter(movie => {
      if (!movie.releaseDate) return true
      const releaseDate = new Date(movie.releaseDate)
      console.log(`首页 - 电影 ${movie.name} 上映日期:`, releaseDate, '是否<=今天:', releaseDate <= today)
      return releaseDate <= today
    })
    
    console.log('首页 - 正在热映电影数量:', hotMoviesList.length)
    
    hotMovies.value = hotMoviesList.slice(0, 10).map((movie, index) => ({
      ...movie,
      image: movie.image || `https://picsum.photos/seed/hot-${movie.id || index}/400/600`,
      rating: (Math.random() * 2 + 8).toFixed(1),
      category: movie.categoryName || ['动作', '喜剧', '爱情', '科幻', '悬疑'][Math.floor(Math.random() * 5)]
    }))
  } catch (error) {
    console.error('加载热映电影失败:', error)
  }
}

const loadUpcomingMovies = async () => {
  try {
    const res = await request.get('/user/movie/list', { params: { status: 1 } })
    const movies = res.data || []
    
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    
    const upcomingMoviesList = movies.filter(movie => {
      if (!movie.releaseDate) return false
      const releaseDate = new Date(movie.releaseDate)
      console.log(`首页 - 电影 ${movie.name} 上映日期:`, releaseDate, '是否>今天:', releaseDate > today)
      return releaseDate > today
    })
    
    console.log('首页 - 即将上映电影数量:', upcomingMoviesList.length)
    
    upcomingMovies.value = upcomingMoviesList.slice(0, 10).map((movie, index) => ({
      ...movie,
      image: movie.image || `https://picsum.photos/seed/upcoming-${movie.id || index}/400/600`,
      category: movie.categoryName || ['动作', '喜剧', '爱情', '科幻', '悬疑'][Math.floor(Math.random() * 5)]
    }))
  } catch (error) {
    console.error('加载即将上映电影失败:', error)
  }
}

const goToMovies = (type) => {
  if (type) {
    router.push({ path: '/user/movies', query: { type } })
  } else {
    router.push('/user/movies')
  }
}

const goToCinemas = () => {
  router.push('/user/cinemas')
}

const handleView = (movie) => {
  router.push(`/user/movie/${movie.id}`)
}

onMounted(() => {
  loadHotMovies()
  loadUpcomingMovies()
})
</script>

<style scoped>
.home-page {
  background: #f5f5f5;
  min-height: 100vh;
}

.welcome-section {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
  padding: 60px 0;
  margin-bottom: 0;
}

.welcome-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.welcome-text {
  flex: 1;
  color: #fff;
}

.welcome-text h1 {
  font-size: 48px;
  font-weight: bold;
  margin: 0 0 20px 0;
}

.welcome-text p {
  font-size: 20px;
  margin: 0 0 30px 0;
  opacity: 0.9;
}

.welcome-actions {
  display: flex;
  gap: 15px;
}

.welcome-actions .el-button {
  padding: 12px 30px;
}

.welcome-illustration {
  flex: 1;
  text-align: right;
}

.illustration-img {
  max-width: 100%;
  height: auto;
  max-height: 300px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.empty-illustration {
  max-width: 300px;
  margin-bottom: 20px;
  opacity: 0.6;
}

.btn-icon {
  margin-right: 8px;
  font-size: 18px;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

.section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 0;
  border-bottom: 2px solid #e8e8e8;
}

.section-title {
  display: flex;
  align-items: center;
}

.title-icon {
  font-size: 28px;
  color: #ff4d4f;
  margin-right: 10px;
}

.section-title h3 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.section-title .movie-count {
  margin-left: 10px;
  font-size: 14px;
  color: #999;
}

.arrow-icon {
  margin-left: 5px;
  font-size: 16px;
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
}

.movie-item {
  cursor: pointer;
  transition: all 0.3s ease;
}

.movie-item:hover {
  transform: translateY(-5px);
}

.movie-poster {
  position: relative;
  width: 100%;
  height: 280px;
  border-radius: 4px;
  overflow: hidden;
  background: #f0f0f0;
}

.movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-rating-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: #ffb400;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
}

.movie-status-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(24, 144, 255, 0.9);
  color: #fff;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
}

.status-icon {
  font-size: 14px;
  margin-right: 4px;
}

.star-icon-small {
  font-size: 14px;
  margin-right: 4px;
}

.rating-num {
  font-size: 16px;
  font-weight: bold;
  margin-right: 2px;
}

.rating-unit {
  font-size: 10px;
}

.movie-hover-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.movie-item:hover .movie-hover-mask {
  opacity: 1;
}

.movie-info {
  padding: 12px 0;
}

.movie-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.movie-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.movie-category,
.movie-duration {
  display: flex;
  align-items: center;
}

.meta-icon {
  font-size: 14px;
  margin-right: 4px;
}

.movie-category {
  color: #ff4d4f;
}

:deep(.el-button--text) {
  color: #ff4d4f;
  font-size: 14px;
}

:deep(.el-button--text:hover) {
  color: #ff7875;
}

@media (max-width: 1200px) {
  .movie-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 992px) {
  .movie-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .movie-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .welcome-illustration {
    display: none;
  }
}
</style>
