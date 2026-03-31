<template>
  <div class="user-movies-page">
    <el-row :gutter="20">
      <el-col
        v-for="item in movieList"
        :key="item.id"
        :span="6"
      >
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <img :src="item.image" class="movie-image" />
          <div class="movie-info">
            <h3>{{ item.name }}</h3>
            <p class="movie-desc">{{ item.description }}</p>
            <div class="movie-meta">
              <span>时长：{{ item.duration }}分钟</span>
              <span>导演：{{ item.director }}</span>
            </div>
            <div class="movie-price">
              <span class="price">¥{{ item.price }}</span>
              <el-button type="primary" size="small" @click="handleView(item)">查看排片</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const movieList = ref([])

const loadData = async () => {
  try {
    const res = await request.get('/user/movie/list', { params: { status: 1 } })
    movieList.value = res.data
  } catch (error) {
    console.error('加载电影列表失败:', error)
  }
}

const handleView = (movie) => {
  router.push({
    path: '/user-schedules',
    query: { movieId: movie.id }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-movies-page {
  padding: 20px;
}

.movie-image {
  width: 100%;
  height: 300px;
  object-fit: cover;
}

.movie-info {
  padding: 15px;
}

.movie-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

.movie-desc {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  height: 40px;
  overflow: hidden;
}

.movie-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 13px;
  color: #999;
}

.movie-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
}
</style>