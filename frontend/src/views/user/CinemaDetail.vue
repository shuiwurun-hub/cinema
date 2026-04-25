<template>
  <div class="cinema-detail-page">
    <div class="page-banner">
      <div class="banner-content">
        <div class="banner-text">
          <h2>{{ cinema?.name }}</h2>
          <p>{{ cinema?.address }}</p>
        </div>
        <div class="banner-illustration">
          <img :src="`https://picsum.photos/seed/cinema-${cinema?.id}/600/400`" :alt="cinema?.name" class="illustration-img" />
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="cinema-info-section">
        <div class="info-card">
          <div class="info-item">
            <Icon icon="mdi:map-marker" class="info-icon" />
            <span class="info-label">地址：</span>
            <span class="info-value">{{ cinema?.address }}</span>
          </div>
          <div class="info-item">
            <Icon icon="mdi:phone" class="info-icon" />
            <span class="info-label">电话：</span>
            <span class="info-value">{{ cinema?.phone }}</span>
          </div>
          <div class="info-item">
            <Icon icon="mdi:star" class="info-icon" />
            <span class="info-label">评分：</span>
            <span class="info-value">{{ (Math.random() * 2 + 8).toFixed(1) }}</span>
          </div>
        </div>
      </div>

      <div class="movies-section">
        <h3 class="section-title">
          <Icon icon="mdi:film" class="title-icon" />
          正在热映
        </h3>
        
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="6" animated />
        </div>
        
        <div v-else-if="movies.length === 0" class="empty-state">
          <img src="https://undraw.co/api/illustrations/no-content" alt="No Movies" class="empty-illustration" />
          <el-empty description="暂无热映电影" />
        </div>
        
        <div v-else class="movies-grid">
          <div
            v-for="movie in movies"
            :key="movie.id"
            class="movie-card"
            @click="goToMovieDetail(movie.id)"
          >
            <div class="movie-image">
              <img :src="movie.image" :alt="movie.name" />
              <div class="movie-rating">
                <Icon icon="mdi:star" class="rating-icon" />
                <span class="rating-value">{{ movie.score || (Math.random() * 2 + 7).toFixed(1) }}</span>
              </div>
            </div>
            <div class="movie-info">
              <h4 class="movie-name">{{ movie.name }}</h4>
              <p class="movie-category">{{ movie.categoryName || '动作' }}</p>
              <div class="movie-schedule-info">
                <Icon icon="mdi:calendar-clock" class="schedule-icon" />
                <span class="schedule-text">{{ todaySchedules[movie.id] || 0 }}场</span>
              </div>
            </div>
            <div class="movie-action">
              <el-button type="danger" size="small" round>
                <Icon icon="mdi:ticket" class="btn-icon" />
                购票
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const cinemaId = route.params.id

const cinema = ref(null)
const movies = ref([])
const loading = ref(true)
const todaySchedules = ref({})

const goToMovieDetail = (movieId) => {
  router.push(`/user/movie/${movieId}`)
}

const loadCinemaInfo = async () => {
  try {
    const res = await request.get(`/user/cinema/${cinemaId}`)
    console.log('影院信息:', res.data)
    cinema.value = res.data
  } catch (error) {
    console.error('加载影院信息失败:', error)
    // 添加模拟数据
    cinema.value = {
      id: cinemaId,
      name: '万达影城',
      address: '北京市朝阳区建国路88号',
      phone: '010-12345678'
    }
  }
}

const loadMovies = async () => {
  try {
    // 获取该影院的热映电影（只显示有排片的电影）
    const today = new Date().toISOString().split('T')[0]
    console.log('查询日期:', today)
    
    // 先获取该影院今天的所有排片
    const scheduleRes = await request.get('/user/schedule/list', {
      params: {
        cinemaId: cinemaId,
        showDate: today
      }
    })
    console.log('排片数据:', scheduleRes.data)
    
    if (scheduleRes.data && scheduleRes.data.length > 0) {
      // 从排片中提取电影ID
      const movieIds = [...new Set(scheduleRes.data.map(s => s.movieId))]
      console.log('电影ID列表:', movieIds)
      
      // 获取这些电影的详细信息
      const movieRes = await request.get('/user/movie/list', {
        params: { status: 1 }
      })
      console.log('所有热映电影:', movieRes.data)
      
      // 过滤出在该影院有排片的电影
      movies.value = (movieRes.data || []).filter(movie => movieIds.includes(movie.id))
      console.log('该影院热映电影:', movies.value)
      
      // 计算每个电影在该影院的排片数
      for (const movie of movies.value) {
        const movieSchedules = scheduleRes.data.filter(s => s.movieId === movie.id)
        todaySchedules.value[movie.id] = movieSchedules.length
      }
    } else {
      movies.value = []
      console.log('该影院今天没有排片')
    }
  } catch (error) {
    console.error('加载电影列表失败:', error)
    // 添加模拟数据
    movies.value = [
      {
        id: 1,
        name: '速度与激情10',
        image: 'https://picsum.photos/seed/movie1/400/600',
        score: 8.5,
        categoryName: '动作'
      },
      {
        id: 2,
        name: '复仇者联盟5',
        image: 'https://picsum.photos/seed/movie2/400/600',
        score: 8.8,
        categoryName: '科幻'
      },
      {
        id: 3,
        name: '流浪地球3',
        image: 'https://picsum.photos/seed/movie3/400/600',
        score: 9.0,
        categoryName: '科幻'
      }
    ]
    // 模拟排片数
    todaySchedules.value = {
      1: 5,
      2: 4,
      3: 6
    }
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await loadCinemaInfo()
  await loadMovies()
})
</script>

<style scoped>
.cinema-detail-page {
  background: #f5f5f5;
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #ff6b35 0%, #ff4d4f 100%);
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
  margin: 0 0 10px 0;
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

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

.cinema-info-section {
  margin-bottom: 30px;
}

.info-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 16px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-icon {
  font-size: 20px;
  color: #ff4d4f;
  margin-right: 12px;
  min-width: 24px;
}

.info-label {
  font-weight: bold;
  color: #333;
  margin-right: 8px;
  min-width: 60px;
}

.info-value {
  color: #666;
}

.movies-section {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0 0 25px 0;
  display: flex;
  align-items: center;
}

.title-icon {
  font-size: 28px;
  color: #ff4d4f;
  margin-right: 10px;
}

.loading-state {
  padding: 40px 0;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.empty-illustration {
  max-width: 300px;
  margin-bottom: 20px;
  opacity: 0.6;
}

.movies-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.movie-card {
  background: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.movie-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.movie-image {
  position: relative;
  width: 100%;
  padding-top: 140%;
  overflow: hidden;
}

.movie-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.movie-card:hover .movie-image img {
  transform: scale(1.05);
}

.movie-rating {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.8);
  color: #ffd700;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.rating-icon {
  font-size: 12px;
  margin-right: 4px;
}

.movie-info {
  flex: 1;
  padding: 15px;
  display: flex;
  flex-direction: column;
}

.movie-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.movie-category {
  font-size: 12px;
  color: #999;
  margin: 0 0 10px 0;
}

.movie-schedule-info {
  font-size: 12px;
  color: #666;
  display: flex;
  align-items: center;
  margin-top: auto;
}

.schedule-icon {
  font-size: 12px;
  margin-right: 4px;
  color: #ff4d4f;
}

.movie-action {
  padding: 0 15px 15px;
  text-align: center;
}

.btn-icon {
  font-size: 14px;
  margin-right: 4px;
}

@media (max-width: 768px) {
  .banner-content {
    flex-direction: column;
    text-align: center;
  }
  
  .banner-illustration {
    margin-top: 20px;
    text-align: center;
  }
  
  .movies-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .info-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .info-label {
    margin-bottom: 4px;
  }
}
</style>