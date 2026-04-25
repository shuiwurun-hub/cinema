<template>
  <div class="schedules-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>排片管理</span>
          <div class="header-actions">
            <el-select
              v-model="selectedCinemaId"
              placeholder="请选择影院"
              style="width: 250px; margin-right: 10px"
              @change="handleCinemaChange"
            >
              <el-option
                v-for="item in cinemaList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
            <el-select
              v-model="selectedHallId"
              placeholder="请选择影厅"
              style="width: 200px; margin-right: 10px"
              @change="handleHallChange"
              :disabled="!selectedCinemaId"
            >
              <el-option
                v-for="item in hallList"
                :key="item.id"
                :label="`${item.name} (${item.type})`"
                :value="item.id"
              />
            </el-select>
            <el-button type="primary" @click="handleAdd" :disabled="!selectedHallId">
              新增排片
            </el-button>
          </div>
        </div>
      </template>

      <div v-if="!selectedCinemaId || !selectedHallId" class="empty-state">
        <el-empty description="请先选择影院和影厅">
          <template #image>
            <Icon icon="mdi:calendar-clock" style="font-size: 80px; color: #ccc" />
          </template>
        </el-empty>
      </div>

      <div v-else class="schedule-groups">
        <div
          v-for="group in groupedSchedules"
          :key="group.movieId"
          class="movie-group"
        >
          <div class="movie-header">
            <div class="movie-info">
              <Icon icon="mdi:movie" style="font-size: 24px; color: #ff6b6b; margin-right: 10px" />
              <span class="movie-name">{{ group.movieName }}</span>
              <el-tag size="small" type="info" style="margin-left: 10px">
                {{ group.schedules.length }} 场次
              </el-tag>
            </div>
            <el-button
              type="primary"
              size="small"
              @click="handleAddForMovie(group.movieId)"
            >
              <Icon icon="mdi:plus" style="margin-right: 5px" />
              添加场次
            </el-button>
          </div>

          <el-table :data="group.schedules" style="width: 100%">
            <el-table-column prop="showDate" label="放映日期" width="120">
              <template #default="{ row }">
                {{ formatDate(row.showDate) }}
              </template>
            </el-table-column>
            <el-table-column prop="showTime" label="放映时间" width="100">
              <template #default="{ row }">
                {{ formatTime(row.showTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="100">
              <template #default="{ row }">
                {{ formatTime(row.endTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="price" label="票价" width="120">
              <template #default="{ row }">
                <span style="color: #ff4d4f; font-weight: bold; font-size: 16px">
                  ¥{{ row.price }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '正常' : '已取消' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                <el-button
                  :type="row.status === 1 ? 'warning' : 'success'"
                  size="small"
                  @click="handleStatus(row)"
                >
                  {{ row.status === 1 ? '取消' : '启用' }}
                </el-button>
                <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="groupedSchedules.length === 0" class="empty-state">
          <el-empty description="暂无排片数据">
            <template #image>
              <Icon icon="mdi:movie-outline" style="font-size: 80px; color: #ccc" />
            </template>
          </el-empty>
        </div>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="电影" prop="movieId">
          <el-select v-model="form.movieId" placeholder="请选择电影" style="width: 100%">
            <el-option
              v-for="item in movieList"
              :key="item.id"
              :label="`${item.name} (${item.duration}分钟)`"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="影院">
          <el-input :value="selectedCinema?.name" disabled />
        </el-form-item>
        <el-form-item label="影厅">
          <el-input :value="selectedHall?.name" disabled />
        </el-form-item>
        <el-form-item label="放映日期" prop="showDate">
          <el-date-picker
            v-model="form.showDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="放映时间" prop="showTime">
          <el-time-picker
            v-model="form.showTime"
            placeholder="选择开始时间"
            style="width: 100%"
            format="HH:mm"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="form.endTime"
            placeholder="选择结束时间"
            style="width: 100%"
            format="HH:mm"
          />
        </el-form-item>
        <el-form-item label="票价" prop="price">
          <el-input-number
            v-model="form.price"
            :min="0"
            :precision="2"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #999; font-size: 12px">元</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Icon } from '@iconify/vue'
import request from '@/utils/request'

const tableData = ref([])
const movieList = ref([])
const cinemaList = ref([])
const hallList = ref([])
const selectedCinemaId = ref(null)
const selectedHallId = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新增排片')
const formRef = ref()
const form = ref({
  id: null,
  movieId: null,
  cinemaId: null,
  hallId: null,
  showDate: '',
  showTime: '',
  endTime: '',
  price: 50
})

const rules = {
  movieId: [{ required: true, message: '请选择电影', trigger: 'change' }],
  showDate: [{ required: true, message: '请选择放映日期', trigger: 'change' }],
  showTime: [{ required: true, message: '请选择放映时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  price: [{ required: true, message: '请输入票价', trigger: 'blur' }]
}

const selectedCinema = computed(() => {
  return cinemaList.value.find(c => c.id === selectedCinemaId.value)
})

const selectedHall = computed(() => {
  return hallList.value.find(h => h.id === selectedHallId.value)
})

const groupedSchedules = computed(() => {
  const groups = {}
  tableData.value.forEach(schedule => {
    const movieId = schedule.movieId
    if (!groups[movieId]) {
      groups[movieId] = {
        movieId: movieId,
        movieName: schedule.movieName,
        schedules: []
      }
    }
    groups[movieId].schedules.push(schedule)
  })
  
  return Object.values(groups).sort((a, b) => {
    return a.movieName.localeCompare(b.movieName, 'zh-CN')
  })
})

const loadMovies = async () => {
  try {
    const res = await request.get('/admin/movie/list', { params: { status: 1 } })
    movieList.value = res.data
  } catch (error) {
    console.error('加载电影列表失败:', error)
  }
}

const loadCinemas = async () => {
  try {
    const res = await request.get('/admin/cinema/list')
    cinemaList.value = res.data
    if (cinemaList.value.length > 0 && !selectedCinemaId.value) {
      selectedCinemaId.value = cinemaList.value[0].id
      await loadHalls()
    }
  } catch (error) {
    console.error('加载影院列表失败:', error)
  }
}

const loadHalls = async () => {
  if (!selectedCinemaId.value) {
    hallList.value = []
    selectedHallId.value = null
    return
  }
  
  try {
    const res = await request.get('/admin/hall/list', {
      params: { cinemaId: selectedCinemaId.value }
    })
    hallList.value = res.data
    if (hallList.value.length > 0 && !selectedHallId.value) {
      selectedHallId.value = hallList.value[0].id
      await loadData()
    }
  } catch (error) {
    console.error('加载影厅列表失败:', error)
  }
}

const loadData = async () => {
  if (!selectedHallId.value) {
    tableData.value = []
    return
  }
  
  try {
    const res = await request.get('/admin/schedule/list', {
      params: { hallId: selectedHallId.value }
    })
    tableData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const handleCinemaChange = () => {
  selectedHallId.value = null
  tableData.value = []
  loadHalls()
}

const handleHallChange = () => {
  loadData()
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatTime = (time) => {
  if (!time) return ''
  return time.substring(0, 5)
}

const handleAdd = () => {
  if (!selectedHallId.value) {
    ElMessage.warning('请先选择影院和影厅')
    return
  }
  
  dialogTitle.value = '新增排片'
  form.value = {
    id: null,
    movieId: null,
    cinemaId: selectedCinemaId.value,
    hallId: selectedHallId.value,
    showDate: '',
    showTime: '',
    endTime: '',
    price: 50
  }
  dialogVisible.value = true
}

const handleAddForMovie = (movieId) => {
  if (!selectedHallId.value) {
    ElMessage.warning('请先选择影院和影厅')
    return
  }
  
  dialogTitle.value = '新增排片'
  form.value = {
    id: null,
    movieId: movieId,
    cinemaId: selectedCinemaId.value,
    hallId: selectedHallId.value,
    showDate: '',
    showTime: '',
    endTime: '',
    price: 50
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑排片'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleStatus = async (row) => {
  try {
    await request.post(`/admin/schedule/status/${row.status === 1 ? 0 : 1}`, null, {
      params: { id: row.id }
    })
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    console.error('操作失败:', error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该排片吗？删除后无法恢复！', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/schedule/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    form.value.cinemaId = selectedCinemaId.value
    form.value.hallId = selectedHallId.value
    
    if (form.value.id) {
      await request.put('/admin/schedule', form.value)
    } else {
      await request.post('/admin/schedule', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

onMounted(() => {
  loadMovies()
  loadCinemas()
})
</script>

<style scoped>
.schedules-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.schedule-groups {
  margin-top: 10px;
}

.movie-group {
  margin-bottom: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.movie-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
  color: white;
}

.movie-info {
  display: flex;
  align-items: center;
}

.movie-name {
  font-size: 18px;
  font-weight: bold;
}

.movie-group .el-table {
  border-top: none;
}
</style>
