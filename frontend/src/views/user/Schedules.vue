<template>
  <div class="cinemas-page">
    <div class="page-header">
      <el-button class="back-btn" @click="goBack">
        <Icon icon="mdi:arrow-left" />
        返回
      </el-button>
    </div>

    <div v-if="movieInfo" class="movie-header">
      <div class="movie-poster">
        <img :src="movieInfo.image" :alt="movieInfo.name" />
      </div>
      <div class="movie-info">
        <h1>{{ movieInfo.name }}</h1>
        <div class="movie-meta">
          <span class="rating">
            <Icon icon="mdi:star" class="star-icon" />
            {{ movieInfo.rating || '暂无评分' }}
          </span>
          <span class="meta-item">{{ movieInfo.categoryName }}</span>
          <span class="meta-item">{{ movieInfo.duration }}分钟</span>
          <span class="meta-item">{{ movieInfo.director }}</span>
        </div>
      </div>
    </div>

    <div class="date-selector">
      <div class="date-tabs">
        <div
          v-for="(date, index) in dateOptions"
          :key="index"
          :class="['date-tab', { active: selectedDate === date.value }]"
          @click="selectDate(date.value)"
        >
          <div class="date-label">{{ date.label }}</div>
          <div class="date-value">{{ date.display }}</div>
        </div>
      </div>
    </div>

    <div class="cinema-selector">
      <h3 class="selector-title">
        <Icon icon="mdi:theater" />
        选择影院
      </h3>
      <div class="cinema-grid">
        <div
          v-for="cinema in availableCinemas"
          :key="cinema.cinemaId"
          :class="['cinema-card', { active: selectedCinema === cinema.cinemaId }]"
          @click="selectCinema(cinema.cinemaId)"
        >
          <div class="cinema-name">{{ cinema.cinemaName }}</div>
          <div class="cinema-address">
            <Icon icon="mdi:map-marker" class="address-icon" />
            {{ cinema.cinemaAddress }}
          </div>
          <div class="cinema-schedule-count">
            <Icon icon="mdi:calendar-clock" />
            {{ cinema.scheduleCount }}场
          </div>
          <div class="cinema-price">
            <span class="price-label">¥</span>
            <span class="price-value">{{ cinema.minPrice }}</span>
            <span class="price-unit">起</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="selectedCinema && cinemaSchedules.length > 0" class="schedule-section">
      <div class="section-header">
        <h3>
          <Icon icon="mdi:movie-open" />
          {{ selectedCinemaName }} - {{ movieInfo?.name }}
        </h3>
        <div class="schedule-count">
          共 {{ cinemaSchedules.length }} 个场次
        </div>
      </div>

      <div class="schedule-grid">
        <div
          v-for="schedule in cinemaSchedules"
          :key="schedule.id"
          class="schedule-card"
          @click="handleSelectSeats(schedule)"
        >
          <div class="schedule-time">
            <div class="start-time">{{ schedule.showTime }}</div>
            <div class="end-time">{{ getEndTime(schedule.showTime, movieInfo?.duration) }}散场</div>
          </div>
          <div class="schedule-hall">
            <div class="hall-name">{{ schedule.hallName }}</div>
            <div class="seat-info">
              <span class="available">{{ schedule.availableSeats }}</span>/<span>{{ schedule.totalSeats || 100 }}</span>座
            </div>
          </div>
          <div class="schedule-price">
            <div class="price">
              <span class="currency">¥</span>
              <span class="amount">{{ schedule.price }}</span>
            </div>
            <el-button type="danger" size="small" class="buy-btn">选座购票</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-else-if="selectedCinema && cinemaSchedules.length === 0" description="该影院暂无排片" />
    <el-empty v-else-if="!selectedCinema" description="请选择影院查看排片" />

    <el-dialog v-model="seatDialogVisible" title="选座购票" width="900px" class="seat-dialog">
      <div class="dialog-movie-info">
        <img :src="movieInfo?.image" :alt="movieInfo?.name" class="dialog-poster" />
        <div class="dialog-details">
          <h3>{{ currentSchedule.movieName }}</h3>
          <p><Icon icon="mdi:theater" /> {{ currentSchedule.cinemaName }}</p>
          <p><Icon icon="mdi:seat" /> {{ currentSchedule.hallName }}</p>
          <p><Icon icon="mdi:calendar" /> {{ currentSchedule.showDate }} {{ currentSchedule.showTime }}</p>
          <p class="dialog-price">
            <Icon icon="mdi:ticket" /> 
            <span class="price-label">¥</span>
            <span class="price-value">{{ currentSchedule.price }}</span>
          </p>
        </div>
      </div>

      <div class="screen-area">
        <div class="screen">银幕</div>
        <div class="screen-shadow"></div>
      </div>

      <div class="seat-area">
        <div class="seat-container">
          <div
            v-for="seat in seats"
            :key="seat.id"
            :class="['seat-item', getSeatClass(seat)]"
            @click="handleSeatClick(seat)"
          >
            <div class="seat-number">{{ seat.rowNum }}-{{ seat.colNum }}</div>
          </div>
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

      <div class="order-summary">
        <div class="summary-left">
          <div class="selected-info">
            <span class="label">已选座位：</span>
            <span class="count">{{ selectedSeats.length }}</span>个
          </div>
          <div v-if="selectedSeats.length > 0" class="selected-detail">
            {{ selectedSeats.map(s => `${s.rowNum}排${s.colNum}座`).join('、') }}
          </div>
        </div>
        <div class="summary-right">
          <div class="total-price">
            <span class="label">总价：</span>
            <span class="currency">¥</span>
            <span class="amount">{{ totalPrice }}</span>
          </div>
          <el-button type="danger" size="large" @click="showConfirmDialog" :disabled="selectedSeats.length === 0">
            确认选座
          </el-button>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="confirmDialogVisible" title="确认订单" width="600px" class="confirm-dialog">
      <div class="confirm-content">
        <div class="confirm-section">
          <h3><Icon icon="mdi:movie" /> 电影信息</h3>
          <div class="info-item">
            <span class="label">电影：</span>
            <span class="value">{{ movieInfo?.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">影院：</span>
            <span class="value">{{ currentSchedule.cinemaName }}</span>
          </div>
          <div class="info-item">
            <span class="label">影厅：</span>
            <span class="value">{{ currentSchedule.hallName }}</span>
          </div>
          <div class="info-item">
            <span class="label">场次：</span>
            <span class="value">{{ currentSchedule.showDate }} {{ currentSchedule.showTime }}</span>
          </div>
        </div>

        <div class="confirm-section">
          <h3><Icon icon="mdi:seat" /> 座位信息</h3>
          <div class="seat-list">
            <div v-for="seat in selectedSeats" :key="seat.id" class="seat-item-confirm">
              <Icon icon="mdi:seat" />
              <span>{{ seat.rowNum }}排{{ seat.colNum }}座</span>
              <span class="seat-type">{{ seat.seatType }}</span>
            </div>
          </div>
        </div>

        <div class="confirm-section">
          <h3><Icon icon="mdi:cash" /> 支付信息</h3>
          <div class="info-item">
            <span class="label">票价：</span>
            <span class="value">¥{{ currentSchedule.price }}/张</span>
          </div>
          <div class="info-item">
            <span class="label">数量：</span>
            <span class="value">{{ selectedSeats.length }}张</span>
          </div>
          <div class="info-item total">
            <span class="label">总计：</span>
            <span class="value price">¥{{ totalPrice }}</span>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="confirmDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="submitOrder" :loading="submitting">
            确认购票
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const movieInfo = ref(null)
const allSchedules = ref([])
const selectedDate = ref(null)
const selectedCinema = ref(null)
const loading = ref(false)
const seatDialogVisible = ref(false)
const confirmDialogVisible = ref(false)
const currentSchedule = ref({})
const seats = ref([])
const selectedSeats = ref([])
const submitting = ref(false)

const dateOptions = computed(() => {
  const options = []
  const today = new Date()
  
  for (let i = 0; i < 8; i++) {
    const date = new Date(today)
    date.setDate(today.getDate() + i)
    
    const month = date.getMonth() + 1
    const day = date.getDate()
    const weekDay = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][date.getDay()]
    
    let label = ''
    if (i === 0) label = '今天'
    else if (i === 1) label = '明天'
    else if (i === 2) label = '后天'
    else label = weekDay
    
    options.push({
      label,
      value: `${date.getFullYear()}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`,
      display: `${month}月${day}日`
    })
  }
  
  return options
})

const totalPrice = computed(() => {
  return selectedSeats.value.length * currentSchedule.value.price
})

const availableCinemas = computed(() => {
  const cinemaMap = new Map()
  
  allSchedules.value.forEach(schedule => {
    if (!cinemaMap.has(schedule.cinemaId)) {
      cinemaMap.set(schedule.cinemaId, {
        cinemaId: schedule.cinemaId,
        cinemaName: schedule.cinemaName,
        cinemaAddress: schedule.cinemaAddress || '暂无地址',
        scheduleCount: 0,
        minPrice: schedule.price,
        schedules: []
      })
    }
    
    const cinema = cinemaMap.get(schedule.cinemaId)
    cinema.scheduleCount++
    cinema.schedules.push(schedule)
    if (schedule.price < cinema.minPrice) {
      cinema.minPrice = schedule.price
    }
  })
  
  return Array.from(cinemaMap.values())
})

const selectedCinemaName = computed(() => {
  const cinema = availableCinemas.value.find(c => c.cinemaId === selectedCinema.value)
  return cinema?.cinemaName || ''
})

const cinemaSchedules = computed(() => {
  if (!selectedCinema.value) return []
  return allSchedules.value
    .filter(s => s.cinemaId === selectedCinema.value)
    .sort((a, b) => a.showTime.localeCompare(b.showTime))
})

const selectDate = (date) => {
  selectedDate.value = date
  selectedCinema.value = null
  loadData()
}

const selectCinema = (cinemaId) => {
  selectedCinema.value = cinemaId
}

const getEndTime = (showTime, duration) => {
  if (!showTime || !duration) return ''
  const [hours, minutes] = showTime.split(':').map(Number)
  const totalMinutes = hours * 60 + minutes + duration
  const endHours = Math.floor(totalMinutes / 60) % 24
  const endMinutes = totalMinutes % 60
  return `${String(endHours).padStart(2, '0')}:${String(endMinutes).padStart(2, '0')}`
}

const loadMovieInfo = async () => {
  const movieId = route.query.movieId
  if (!movieId) return
  
  try {
    const res = await request.get(`/user/movie/${movieId}`)
    movieInfo.value = res.data
  } catch (error) {
    console.error('加载电影信息失败:', error)
  }
}

const loadData = async () => {
  if (!selectedDate.value) return
  
  loading.value = true
  try {
    const params = {
      movieId: route.query.movieId,
      showDate: selectedDate.value
    }
    
    const res = await request.get('/user/schedule/list', { params })
    allSchedules.value = res.data || []
  } catch (error) {
    console.error('加载排片列表失败:', error)
    allSchedules.value = []
  } finally {
    loading.value = false
  }
}

const loadSeats = async (hallId, scheduleId) => {
  try {
    console.log('加载座位，hallId:', hallId, 'scheduleId:', scheduleId)
    
    const res = await request.get(`/user/seat/list/${hallId}`)
    seats.value = res.data || []
    console.log('座位列表:', seats.value.length, '个座位')
    
    const soldRes = await request.get(`/user/seat/sold/${scheduleId}`)
    const soldSeatIds = soldRes.data || []
    console.log('已售座位ID列表:', soldSeatIds)
    
    seats.value.forEach(seat => {
      if (soldSeatIds.includes(seat.id)) {
        seat.isSold = true
        console.log('标记已售座位:', seat.id, seat.rowNum, seat.colNum)
      }
    })
    
    console.log('已售座位数量:', seats.value.filter(s => s.isSold).length)
  } catch (error) {
    console.error('加载座位失败:', error)
    seats.value = []
  }
}

const handleSelectSeats = (row) => {
  currentSchedule.value = row
  selectedSeats.value = []
  loadSeats(row.hallId, row.id)
  seatDialogVisible.value = true
}

const getSeatClass = (seat) => {
  if (seat.isSold) {
    return 'sold'
  }
  const isSelected = selectedSeats.value.some(s => s.id === seat.id)
  if (isSelected) {
    return 'selected'
  }
  return 'available'
}

const handleSeatClick = (seat) => {
  if (seat.isSold) {
    ElMessage.warning('该座位已被购买，请选择其他座位')
    return
  }
  
  const index = selectedSeats.value.findIndex(s => s.id === seat.id)
  if (index > -1) {
    selectedSeats.value.splice(index, 1)
  } else {
    selectedSeats.value.push(seat)
  }
}

const showConfirmDialog = () => {
  if (selectedSeats.value.length === 0) {
    ElMessage.warning('请先选择座位')
    return
  }
  seatDialogVisible.value = false
  confirmDialogVisible.value = true
}

const submitOrder = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录后再购票')
    router.push('/login')
    return
  }

  submitting.value = true
  try {
    const seatIds = selectedSeats.value.map(s => s.id)
    const res = await request.post('/user/ticket/submit', {
      scheduleId: currentSchedule.value.id,
      seatIds: seatIds,
      payMethod: 1
    })
    
    ElMessage.success('购票成功！')
    confirmDialogVisible.value = false
    router.push('/user/tickets')
  } catch (error) {
    console.error('提交订单失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error(error.response?.data?.msg || '订单提交失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  if (dateOptions.value.length > 0) {
    selectedDate.value = dateOptions.value[0].value
  }
  loadMovieInfo()
  loadData()
})

watch(() => route.query.movieId, () => {
  loadMovieInfo()
  loadData()
})
</script>

<style scoped>
.cinemas-page {
  background: #f5f5f5;
  min-height: 100vh;
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

.movie-header {
  background: linear-gradient(to right, #ff4d4f 0%, #ff7875 100%);
  padding: 30px 20px;
  color: white;
  display: flex;
  gap: 20px;
}

.movie-poster {
  width: 100px;
  height: 140px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  flex-shrink: 0;
}

.movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-info h1 {
  margin: 0 0 15px 0;
  font-size: 28px;
  font-weight: bold;
}

.movie-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 24px;
  font-weight: bold;
  color: #ffc107;
}

.star-icon {
  font-size: 20px;
}

.meta-item {
  font-size: 14px;
  opacity: 0.9;
}

.date-selector {
  background: white;
  padding: 15px 20px;
  border-bottom: 1px solid #e0e0e0;
}

.date-tabs {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 5px;
}

.date-tab {
  flex-shrink: 0;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;
  border: 1px solid #e0e0e0;
  min-width: 80px;
}

.date-tab:hover {
  border-color: #ff4d4f;
}

.date-tab.active {
  background: #ff4d4f;
  color: white;
  border-color: #ff4d4f;
}

.date-label {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 5px;
}

.date-value {
  font-size: 12px;
  opacity: 0.8;
}

.cinema-selector {
  background: white;
  padding: 20px;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.selector-title {
  margin: 0 0 15px 0;
  font-size: 18px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cinema-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 15px;
}

.cinema-card {
  padding: 15px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background: white;
}

.cinema-card:hover {
  border-color: #ff4d4f;
  box-shadow: 0 2px 12px rgba(255, 77, 79, 0.15);
}

.cinema-card.active {
  border-color: #ff4d4f;
  background: #fff1f0;
}

.cinema-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.cinema-address {
  font-size: 13px;
  color: #999;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.address-icon {
  color: #ff4d4f;
  flex-shrink: 0;
}

.cinema-schedule-count {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.cinema-price {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.price-label {
  font-size: 14px;
  color: #999;
}

.price-value {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
}

.price-unit {
  font-size: 14px;
  color: #999;
}

.schedule-section {
  background: white;
  padding: 20px;
  margin: 0 20px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.schedule-count {
  font-size: 14px;
  color: #999;
}

.schedule-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
}

.schedule-card {
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.schedule-card:hover {
  border-color: #ff4d4f;
  box-shadow: 0 2px 12px rgba(255, 77, 79, 0.2);
  transform: translateY(-2px);
}

.schedule-time {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.start-time {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.end-time {
  font-size: 12px;
  color: #999;
}

.schedule-hall {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #666;
}

.hall-name {
  color: #666;
}

.seat-info .available {
  color: #52c41a;
  font-weight: bold;
}

.schedule-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.schedule-price .price {
  color: #ff4d4f;
  font-weight: bold;
}

.schedule-price .currency {
  font-size: 14px;
}

.schedule-price .amount {
  font-size: 20px;
}

.buy-btn {
  padding: 5px 15px;
}

.seat-dialog .dialog-movie-info {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 20px;
}

.dialog-poster {
  width: 80px;
  height: 110px;
  border-radius: 4px;
  object-fit: cover;
}

.dialog-details h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #333;
}

.dialog-details p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 8px;
}

.dialog-price {
  margin-top: 10px;
}

.dialog-price .price-label {
  font-size: 16px;
  color: #ff4d4f;
}

.dialog-price .price-value {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
}

.screen-area {
  text-align: center;
  margin-bottom: 30px;
}

.screen {
  display: inline-block;
  padding: 10px 60px;
  background: linear-gradient(to bottom, #e0e0e0, #f5f5f5);
  border-radius: 4px 4px 50% 50%;
  color: #666;
  font-weight: bold;
  font-size: 14px;
}

.screen-shadow {
  width: 60%;
  height: 20px;
  margin: 0 auto;
  background: radial-gradient(ellipse at center, rgba(0, 0, 0, 0.1) 0%, transparent 70%);
}

.seat-area {
  padding: 0 20px;
  margin-bottom: 20px;
}

.seat-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(45px, 1fr));
  gap: 8px;
  max-height: 350px;
  overflow-y: auto;
  padding: 10px;
}

.seat-item {
  width: 45px;
  height: 45px;
  border-radius: 8px 8px 4px 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  border: 2px solid #dcdfe6;
  background: white;
}

.seat-number {
  font-size: 10px;
  color: #999;
}

.seat-item.available:hover {
  border-color: #ff4d4f;
  background: #fff1f0;
}

.seat-item.selected {
  background: #ff4d4f;
  border-color: #ff4d4f;
}

.seat-item.selected .seat-number {
  color: white;
}

.seat-item.sold {
  background: #f4f4f5;
  border-color: #f4f4f5;
  cursor: not-allowed;
}

.seat-item.sold .seat-number {
  color: #c0c4cc;
}

.seat-legend {
  display: flex;
  justify-content: center;
  gap: 40px;
  padding: 15px;
  margin-bottom: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
}

.seat-sample {
  width: 25px;
  height: 25px;
  border-radius: 6px 6px 3px 3px;
}

.seat-sample.available {
  background: white;
  border: 2px solid #dcdfe6;
}

.seat-sample.selected {
  background: #ff4d4f;
  border: 2px solid #ff4d4f;
}

.seat-sample.sold {
  background: #f4f4f5;
  border: 2px solid #f4f4f5;
}

.order-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.summary-left {
  flex: 1;
}

.selected-info {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.selected-info .count {
  font-size: 20px;
  font-weight: bold;
  color: #ff4d4f;
  margin: 0 5px;
}

.selected-detail {
  font-size: 13px;
  color: #999;
}

.summary-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.total-price {
  text-align: right;
}

.total-price .label {
  font-size: 14px;
  color: #666;
}

.total-price .currency {
  font-size: 16px;
  color: #ff4d4f;
  margin-left: 5px;
}

.total-price .amount {
  font-size: 28px;
  font-weight: bold;
  color: #ff4d4f;
}

@media (max-width: 768px) {
  .movie-header {
    flex-direction: column;
    padding: 20px;
  }

  .movie-poster {
    width: 120px;
    height: 168px;
    margin: 0 auto;
  }

  .movie-info {
    text-align: center;
  }

  .movie-meta {
    justify-content: center;
  }

  .date-tabs {
    padding-bottom: 10px;
  }

  .date-tab {
    min-width: 70px;
    padding: 8px 15px;
  }

  .cinema-grid {
    grid-template-columns: 1fr;
  }

  .schedule-grid {
    grid-template-columns: 1fr;
  }

  .order-summary {
    flex-direction: column;
    gap: 15px;
  }

  .summary-right {
    width: 100%;
    justify-content: space-between;
  }
}

.confirm-dialog .confirm-content {
  padding: 10px 0;
}

.confirm-section {
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.confirm-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.confirm-section h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
}

.info-item .label {
  color: #999;
}

.info-item .value {
  color: #333;
  font-weight: 500;
}

.info-item.total {
  margin-top: 10px;
  padding-top: 15px;
  border-top: 2px solid #f0f0f0;
}

.info-item.total .value.price {
  font-size: 24px;
  font-weight: bold;
  color: #ff4d4f;
}

.seat-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.seat-item-confirm {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 15px;
  background: #f5f7fa;
  border-radius: 6px;
  font-size: 14px;
  color: #333;
}

.seat-item-confirm .seat-type {
  font-size: 12px;
  color: #999;
  padding: 2px 8px;
  background: white;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
