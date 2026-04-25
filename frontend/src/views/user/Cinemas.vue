<template>
  <div class="cinema-page">
    <div class="page-banner">
      <div class="banner-content">
        <div class="banner-text">
          <h2>影院列表</h2>
          <p>找到离你最近的影院</p>
        </div>
        <div class="banner-illustration">
          <img src="https://picsum.photos/seed/theater-banner/600/400" alt="Cinema" class="illustration-img" />
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="filter-section">
        <div class="filter-item">
          <Icon icon="mdi:map-marker" class="filter-icon" />
          <span class="filter-label">区域：</span>
          <el-radio-group v-model="selectedDistrict" size="small">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="朝阳区">朝阳区</el-radio-button>
            <el-radio-button label="海淀区">海淀区</el-radio-button>
            <el-radio-button label="东城区">东城区</el-radio-button>
            <el-radio-button label="西城区">西城区</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <div class="cinema-list">
        <div
          v-for="item in filteredCinemas"
          :key="item.id"
          class="cinema-item"
          @click="handleView(item)"
        >
          <div class="cinema-image">
            <img :src="`https://picsum.photos/seed/cinema-${item.id}/300/200`" :alt="item.name" />
          </div>
          <div class="cinema-content">
            <div class="cinema-main">
              <h3 class="cinema-name">{{ item.name }}</h3>
              <div class="cinema-tags">
                <el-tag size="small" type="danger">
                  <Icon icon="mdi:ticket-confirmation" class="tag-icon" />
                  可改签
                </el-tag>
                <el-tag size="small" type="warning">
                  <Icon icon="mdi:cash-refund" class="tag-icon" />
                  可退票
                </el-tag>
              </div>
              <div class="cinema-info">
                <div class="info-item">
                  <Icon icon="mdi:map-marker" class="info-icon" />
                  <span>{{ item.address }}</span>
                </div>
                <div class="info-item">
                  <Icon icon="mdi:phone" class="info-icon" />
                  <span>{{ item.phone }}</span>
                </div>
              </div>
            </div>
            <div class="cinema-price">
              <div class="price-info">
                <span class="price-label">起</span>
                <span class="price-num">¥{{ Math.floor(Math.random() * 50) + 30 }}</span>
              </div>
              <el-button type="danger" size="small" round>
                <Icon icon="mdi:seat" class="btn-icon" />
                选座购票
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="filteredCinemas.length === 0" class="empty-state">
        <img src="https://undraw.co/api/illustrations/not-found" alt="No Cinemas" class="empty-illustration" />
        <el-empty description="暂无影院数据" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const router = useRouter()
const cinemaList = ref([])
const selectedDistrict = ref('')

const filteredCinemas = computed(() => {
  if (!selectedDistrict.value) {
    return cinemaList.value
  }
  return cinemaList.value.filter(cinema => 
    cinema.address && cinema.address.includes(selectedDistrict.value)
  )
})

const loadData = async () => {
  try {
    const res = await request.get('/user/cinema/list')
    cinemaList.value = res.data || []
  } catch (error) {
    console.error('加载影院列表失败:', error)
  }
}

const handleView = (cinema) => {
  router.push(`/user/cinema/${cinema.id}`)
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.cinema-page {
  background: #f5f5f5;
  min-height: 100vh;
}

.page-banner {
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
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
}

.filter-icon {
  font-size: 20px;
  color: #1890ff;
  margin-right: 10px;
}

.filter-label {
  font-size: 14px;
  color: #666;
  margin-right: 15px;
  min-width: 50px;
}

.cinema-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.cinema-item {
  background: #fff;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
}

.cinema-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.cinema-image {
  width: 300px;
  height: 200px;
  flex-shrink: 0;
}

.cinema-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cinema-content {
  flex: 1;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cinema-main {
  flex: 1;
}

.cinema-name {
  margin: 0 0 10px 0;
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.cinema-tags {
  margin-bottom: 12px;
}

.cinema-tags .el-tag {
  margin-right: 8px;
}

.tag-icon {
  font-size: 12px;
  margin-right: 4px;
}

.cinema-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #999;
}

.info-icon {
  font-size: 16px;
  margin-right: 8px;
  color: #666;
}

.cinema-price {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
  min-width: 120px;
}

.price-info {
  display: flex;
  align-items: baseline;
}

.price-label {
  font-size: 12px;
  color: #999;
  margin-right: 4px;
}

.price-num {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
}

.btn-icon {
  font-size: 16px;
  margin-right: 4px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
  background: #fff;
  border-radius: 4px;
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
  background: #1890ff;
  border-color: #1890ff;
  color: #fff;
}
</style>
