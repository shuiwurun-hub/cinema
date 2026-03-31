<template>
  <div class="user-schedules-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>排片列表</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="movieName" label="电影名称" />
        <el-table-column prop="cinemaName" label="影院名称" />
        <el-table-column prop="hallName" label="影厅名称" />
        <el-table-column prop="showDate" label="放映日期" />
        <el-table-column prop="showTime" label="放映时间" />
        <el-table-column prop="price" label="票价">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="availableSeats" label="剩余座位" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleSelectSeats(row)">选座购票</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="seatDialogVisible" title="选座" width="800px">
      <div class="schedule-info">
        <h3>{{ currentSchedule.movieName }}</h3>
        <p>影院：{{ currentSchedule.cinemaName }}</p>
        <p>影厅：{{ currentSchedule.hallName }}</p>
        <p>时间：{{ currentSchedule.showDate }} {{ currentSchedule.showTime }}</p>
        <p>票价：¥{{ currentSchedule.price }}</p>
      </div>
      
      <div class="seat-container">
        <div
          v-for="seat in seats"
          :key="seat.id"
          :class="['seat-item', getSeatClass(seat)]"
          @click="handleSeatClick(seat)"
        >
          {{ seat.rowNum }}行{{ seat.colNum }}列
        </div>
      </div>
      
      <div class="seat-legend">
        <div class="legend-item">
          <div class="seat-sample available"></div>
          <span>可选</span>
        </div>
        <div class="legend-item">
          <div class="seat-sample selected"></div>
          <span>已选</span>
        </div>
        <div class="legend-item">
          <div class="seat-sample sold"></div>
          <span>已售</span>
        </div>
      </div>
      
      <div class="selected-info">
        <span>已选座位：{{ selectedSeats.length }}个</span>
        <span>总价：¥{{ totalPrice }}</span>
        <el-button type="primary" @click="handleSubmit" :disabled="selectedSeats.length === 0">提交订单</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const tableData = ref([])
const seatDialogVisible = ref(false)
const currentSchedule = ref({})
const seats = ref([])
const selectedSeats = ref([])

const totalPrice = computed(() => {
  return selectedSeats.value.length * currentSchedule.value.price
})

const loadData = async () => {
  try {
    const params = {}
    if (route.query.movieId) {
      params.movieId = route.query.movieId
    }
    if (route.query.cinemaId) {
      params.cinemaId = route.query.cinemaId
    }
    const res = await request.get('/user/schedule/list', { params })
    tableData.value = res.data
  } catch (error) {
    console.error('加载排片列表失败:', error)
  }
}

const loadSeats = async (scheduleId) => {
  try {
    const res = await request.get(`/admin/hall/list`, { params: { cinemaId: currentSchedule.value.cinemaId } })
    const hall = res.data.find(h => h.id === currentSchedule.value.hallId)
    if (hall) {
      const seatRes = await request.get(`/admin/seat/list/${hall.id}`)
      seats.value = seatRes.data
    }
  } catch (error) {
    console.error('加载座位失败:', error)
  }
}

const handleBack = () => {
  router.back()
}

const handleSelectSeats = (row) => {
  currentSchedule.value = row
  selectedSeats.value = []
  loadSeats(row.id)
  seatDialogVisible.value = true
}

const getSeatClass = (seat) => {
  const isSelected = selectedSeats.value.some(s => s.id === seat.id)
  if (isSelected) {
    return 'selected'
  }
  return 'available'
}

const handleSeatClick = (seat) => {
  const index = selectedSeats.value.findIndex(s => s.id === seat.id)
  if (index > -1) {
    selectedSeats.value.splice(index, 1)
  } else {
    selectedSeats.value.push(seat)
  }
}

const handleSubmit = async () => {
  try {
    const seatIds = selectedSeats.value.map(s => s.id)
    const res = await request.post('/user/ticket/submit', {
      scheduleId: currentSchedule.value.id,
      seatIds: seatIds,
      payMethod: 1
    })
    ElMessage.success('订单提交成功，请支付')
    seatDialogVisible.value = false
    router.push('/user-tickets')
  } catch (error) {
    console.error('提交订单失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-schedules-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.schedule-info {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.schedule-info h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.schedule-info p {
  margin: 5px 0;
  color: #666;
}

.seat-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.seat-item {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.seat-item.available {
  background: #fff;
  color: #333;
}

.seat-item.available:hover {
  background: #ecf5ff;
  border-color: #409eff;
}

.seat-item.selected {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

.seat-item.sold {
  background: #f4f4f5;
  color: #c0c4cc;
  cursor: not-allowed;
}

.seat-legend {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 10px;
  background: #fff;
  border-radius: 4px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.seat-sample {
  width: 30px;
  height: 30px;
  border-radius: 4px;
}

.seat-sample.available {
  background: #fff;
  border: 2px solid #dcdfe6;
}

.seat-sample.selected {
  background: #409eff;
  border: 2px solid #409eff;
}

.seat-sample.sold {
  background: #f4f4f5;
  border: 2px solid #f4f4f5;
}

.selected-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #fff;
  border-radius: 4px;
  font-weight: bold;
}
</style>