<template>
  <div class="movies-page">
    <div class="page-banner">
      <div class="banner-content">
        <div class="banner-text">
          <h2>电影列表</h2>
          <p>发现精彩电影，享受观影时光</p>
        </div>
        <div class="banner-illustration">
          <img src="https://picsum.photos/seed/cinema-banner/600/400" alt="Cinema" class="illustration-img" />
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="filter-section">
        <div class="filter-item">
          <Icon icon="mdi:movie-filter" class="filter-icon" />
          <span class="filter-label">状态：</span>
          <el-radio-group v-model="currentStatus" size="small">
            <el-radio-button label="hot">正在热映</el-radio-button>
            <el-radio-button label="upcoming">即将上映</el-radio-button>
          </el-radio-group>
        </div>
        
        <div class="filter-item">
          <Icon icon="mdi:tag-multiple" class="filter-icon" />
          <span class="filter-label">类型：</span>
          <el-radio-group v-model="currentCategory" size="small">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="动作">动作</el-radio-button>
            <el-radio-button label="喜剧">喜剧</el-radio-button>
            <el-radio-button label="爱情">爱情</el-radio-button>
            <el-radio-button label="科幻">科幻</el-radio-button>
            <el-radio-button label="悬疑">悬疑</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <div class="movie-list">
        <div
          v-for="item in filteredMovies"
          :key="item.id"
          class="movie-item"
          @click="handleView(item)"
        >
          <div class="movie-poster">
            <img :src="item.image" :alt="item.name" />
            <div v-if="currentStatus === 'hot'" class="movie-rating-badge">
              <Icon icon="mdi:star" class="star-icon-small" />
              <span class="rating-num">{{ item.rating }}</span>
              <span class="rating-unit">分</span>
            </div>
            <div v-else class="movie-status-badge">
              <Icon icon="mdi:calendar" class="status-icon" />
              {{ item.releaseDate || '即将上映' }}
            </div>
            <div class="movie-hover-mask">
              <el-button v-if="currentStatus === 'hot'" type="danger" size="large" round>
                <Icon icon="mdi:ticket-outline" class="btn-icon" />
                立即购票
              </el-button>
              <el-button v-else type="primary" size="large" round>
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

      <div v-if="filteredMovies.length === 0" class="empty-state">
        <img src="https://undraw.co/api/illustrations/not-found" alt="No Movies" class="empty-illustration" />
        <el-empty description="暂无电影数据" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const movieList = ref([])
const currentStatus = ref('hot')
const currentCategory = ref('')

const filteredMovies = computed(() => {
  let movies = movieList.value
  
  console.log('当前状态:', currentStatus.value)
  console.log('电影列表:', movies)
  
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  console.log('今天日期:', today)
  
  if (currentStatus.value === 'hot') {
    movies = movies.filter(movie => {
      if (!movie.releaseDate) return true
      const releaseDate = new Date(movie.releaseDate)
      console.log(`电影 ${movie.name} 上映日期:`, releaseDate, '是否<=今天:', releaseDate <= today)
      return releaseDate <= today
    })
  } else if (currentStatus.value === 'upcoming') {
    movies = movies.filter(movie => {
      if (!movie.releaseDate) return false
      const releaseDate = new Date(movie.releaseDate)
      console.log(`电影 ${movie.name} 上映日期:`, releaseDate, '是否>今天:', releaseDate > today)
      return releaseDate > today
    })
  }
  
  if (currentCategory.value) {
    movies = movies.filter(movie => movie.category === currentCategory.value)
  }
  
  console.log('过滤后的电影:', movies)
  
  return movies
})

const loadMovies = async () => {
  try {
    const res = await request.get('/user/movie/list', { params: { status: 1 } })
    const movies = res.data || []
    
    console.log('接收到电影数据:', movies)
    console.log('电影数量:', movies.length)
    
    movieList.value = movies.map((movie, index) => ({
      ...movie,
      image: movie.image || `https://picsum.photos/seed/${currentStatus.value}-${movie.id || index}/400/600`,
      rating: (Math.random() * 2 + 8).toFixed(1),
      category: movie.categoryName || ['动作', '喜剧', '爱情', '科幻', '悬疑'][Math.floor(Math.random() * 5)]
    }))
    
    console.log('处理后的电影列表:', movieList.value)
  } catch (error) {
    console.error('加载电影列表失败:', error)
  }
}

const handleView = (movie) => {
  router.push(`/user/movie/${movie.id}`)
}

watch(currentStatus, () => {
  currentCategory.value = ''
  loadMovies()
})

watch(() => route.query.type, (newType) => {
  if (newType === 'hot') {
    currentStatus.value = 'hot'
  } else if (newType === 'upcoming') {
    currentStatus.value = 'upcoming'
  }
  loadMovies()
}, { immediate: true })

onMounted(() => {
  // watch 已经通过 immediate: true 处理了初始加载
})
</script>

<style scoped>
.movies-page {
  background: #f5f5f5;
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
  padding: 60px 0;
  margin-bottom: 0;
}

.banner-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.banner-text {
  flex: 1;
  color: #fff;
}

.banner-text h2 {
  font-size: 48px;
  font-weight: bold;
  margin: 0 0 20px 0;
}

.banner-text p {
  font-size: 20px;
  margin: 0;
  opacity: 0.9;
}

.banner-illustration {
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

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

.filter-section {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.filter-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.filter-item:last-child {
  margin-bottom: 0;
}

.filter-icon {
  font-size: 20px;
  color: #ff4d4f;
  margin-right: 10px;
}

.filter-label {
  font-size: 14px;
  color: #666;
  margin-right: 15px;
  min-width: 50px;
}

.movie-list {
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

.btn-icon {
  font-size: 18px;
  margin-right: 8px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
  background: #fff;
  border-radius: 4px;
  grid-column: 1 / -1;
}

.empty-illustration {
  max-width: 300px;
  margin-bottom: 20px;
}

:deep(.el-radio-button__inner) {
  background: #fff;
  border-color: #dcdfe6;
  color: #606266;
}

:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: #ff4d4f;
  border-color: #ff4d4f;
  color: #fff;
}

@media (max-width: 1200px) {
  .movie-list {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 992px) {
  .movie-list {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .movie-list {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .banner-illustration {
    display: none;
  }
}
</style>
