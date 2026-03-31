<template>
  <div class="schedules-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>排片管理</span>
          <el-button type="primary" @click="handleAdd">新增排片</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="movieName" label="电影名称" />
        <el-table-column prop="cinemaName" label="影院名称" />
        <el-table-column prop="hallName" label="影厅名称" />
        <el-table-column prop="showDate" label="放映日期" />
        <el-table-column prop="showTime" label="放映时间" />
        <el-table-column prop="endTime" label="结束时间" />
        <el-table-column prop="price" label="票价">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '已取消' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
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
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="电影" prop="movieId">
          <el-select v-model="form.movieId" placeholder="请选择电影">
            <el-option
              v-for="item in movieList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="影院" prop="cinemaId">
          <el-select v-model="form.cinemaId" placeholder="请选择影院" @change="handleCinemaChange">
            <el-option
              v-for="item in cinemaList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="影厅" prop="hallId">
          <el-select v-model="form.hallId" placeholder="请选择影厅">
            <el-option
              v-for="item in hallList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="放映日期" prop="showDate">
          <el-date-picker v-model="form.showDate" type="date" />
        </el-form-item>
        <el-form-item label="放映时间" prop="showTime">
          <el-time-picker v-model="form.showTime" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker v-model="form.endTime" />
        </el-form-item>
        <el-form-item label="票价" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const tableData = ref([])
const movieList = ref([])
const cinemaList = ref([])
const hallList = ref([])
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
  price: 0
})

const rules = {
  movieId: [{ required: true, message: '请选择电影', trigger: 'change' }],
  cinemaId: [{ required: true, message: '请选择影院', trigger: 'change' }],
  hallId: [{ required: true, message: '请选择影厅', trigger: 'change' }],
  showDate: [{ required: true, message: '请选择放映日期', trigger: 'change' }],
  showTime: [{ required: true, message: '请选择放映时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  price: [{ required: true, message: '请输入票价', trigger: 'blur' }]
}

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
  } catch (error) {
    console.error('加载影院列表失败:', error)
  }
}

const handleCinemaChange = async (cinemaId) => {
  try {
    const res = await request.get('/admin/hall/list', { params: { cinemaId } })
    hallList.value = res.data
  } catch (error) {
    console.error('加载影厅列表失败:', error)
  }
}

const loadData = async () => {
  try {
    const res = await request.get('/admin/schedule/list')
    tableData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增排片'
  form.value = {
    id: null,
    movieId: null,
    cinemaId: null,
    hallId: null,
    showDate: '',
    showTime: '',
    endTime: '',
    price: 0
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
    await ElMessageBox.confirm('确定要删除该排片吗？', '提示', {
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
  loadData()
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
</style>