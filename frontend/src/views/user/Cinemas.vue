<template>
  <div class="user-cinemas-page">
    <el-row :gutter="20">
      <el-col
        v-for="item in cinemaList"
        :key="item.id"
        :span="8"
      >
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <img :src="item.image" class="cinema-image" />
          <div class="cinema-info">
            <h3>{{ item.name }}</h3>
            <p class="cinema-desc">{{ item.description }}</p>
            <div class="cinema-meta">
              <span>地址：{{ item.address }}</span>
              <span>电话：{{ item.phone }}</span>
            </div>
            <div class="cinema-action">
              <el-button type="primary" @click="handleView(item)">查看排片</el-button>
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
const cinemaList = ref([])

const loadData = async () => {
  try {
    const res = await request.get('/user/cinema/list')
    cinemaList.value = res.data
  } catch (error) {
    console.error('加载影院列表失败:', error)
  }
}

const handleView = (cinema) => {
  router.push({
    path: '/user-schedules',
    query: { cinemaId: cinema.id }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-cinemas-page {
  padding: 20px;
}

.cinema-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.cinema-info {
  padding: 15px;
}

.cinema-info h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

.cinema-desc {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  height: 40px;
  overflow: hidden;
}

.cinema-meta {
  display: flex;
  flex-direction: column;
  gap: 5px;
  margin-bottom: 15px;
  font-size: 13px;
  color: #999;
}

.cinema-action {
  text-align: center;
}
</style>