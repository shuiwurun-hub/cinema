<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>电影总数</span>
              <el-icon><Film /></el-icon>
            </div>
          </template>
          <div class="card-content">{{ stats.movieCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>影院总数</span>
              <el-icon><OfficeBuilding /></el-icon>
            </div>
          </template>
          <div class="card-content">{{ stats.cinemaCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>今日订单</span>
              <el-icon><Tickets /></el-icon>
            </div>
          </template>
          <div class="card-content">{{ stats.todayOrders }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>今日收入</span>
              <el-icon><Money /></el-icon>
            </div>
          </template>
          <div class="card-content">¥{{ stats.todayRevenue }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Film, OfficeBuilding, Tickets, Money } from '@element-plus/icons-vue'
import request from '@/utils/request'

const stats = ref({
  movieCount: 0,
  cinemaCount: 0,
  todayOrders: 0,
  todayRevenue: 0
})

onMounted(async () => {
  try {
    const res = await request.get('/admin/dashboard/stats')
    stats.value = res.data
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
  padding: 20px 0;
}
</style>