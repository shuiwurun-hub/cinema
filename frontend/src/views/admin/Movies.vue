<template>
  <div class="movies-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>电影管理</span>
          <el-button type="primary" @click="handleAdd">新增电影</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="电影名称">
          <el-input v-model="queryForm.name" placeholder="请输入电影名称" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="name" label="电影名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="price" label="票价">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" />
        <el-table-column prop="director" label="导演" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              :type="row.status === 1 ? 'warning' : 'success'"
              size="small"
              @click="handleStatus(row)"
            >
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="电影名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="票价" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="duration">
          <el-input-number v-model="form.duration" :min="1" />
        </el-form-item>
        <el-form-item label="导演" prop="director">
          <el-input v-model="form.director" />
        </el-form-item>
        <el-form-item label="主演" prop="actors">
          <el-input v-model="form.actors" type="textarea" />
        </el-form-item>
        <el-form-item label="上映日期" prop="releaseDate">
          <el-date-picker 
            v-model="form.releaseDate" 
            type="date" 
            value-format="YYYY-MM-DD"
            placeholder="选择上映日期"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="海报" prop="image">
          <el-upload
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            :headers="uploadHeaders"
            name="file"
          >
            <el-button type="primary">上传海报</el-button>
          </el-upload>
          <div v-if="form.image" style="margin-top: 10px;">
            <img :src="form.image" style="max-width: 200px; max-height: 200px;" />
          </div>
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

const uploadUrl = import.meta.env.VITE_API_BASE_URL || '/api/admin/common/upload'
const uploadHeaders = {
  token: localStorage.getItem('token') || ''
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const queryForm = ref({
  name: '',
  categoryId: null,
  status: null,
  page: 1,
  pageSize: 10
})

const tableData = ref([])
const total = ref(0)
const categories = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增电影')
const formRef = ref()
const form = ref({
  id: null,
  name: '',
  categoryId: null,
  price: 0,
  duration: 0,
  director: '',
  actors: '',
  releaseDate: '',
  description: '',
  image: ''
})

const rules = {
  name: [{ required: true, message: '请输入电影名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入票价', trigger: 'blur' }],
  duration: [{ required: true, message: '请输入时长', trigger: 'blur' }]
}

const loadCategories = async () => {
  try {
    const res = await request.get('/admin/category/list')
    categories.value = res.data
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadData = async () => {
  try {
    const res = await request.get('/admin/movie/page', { params: queryForm.value })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const handleQuery = () => {
  queryForm.value.page = 1
  loadData()
}

const handleReset = () => {
  queryForm.value = {
    name: '',
    categoryId: null,
    status: null,
    page: 1,
    pageSize: 10
  }
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增电影'
  form.value = {
    id: null,
    name: '',
    categoryId: null,
    price: 0,
    duration: 0,
    director: '',
    actors: '',
    releaseDate: '',
    description: '',
    image: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑电影'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleStatus = async (row) => {
  try {
    await request.post(`/admin/movie/status/${row.status === 1 ? 0 : 1}`, null, {
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
    await ElMessageBox.confirm('确定要删除该电影吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/movie/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleUploadSuccess = (response) => {
  form.value.image = response.data
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (form.value.id) {
      await request.put('/admin/movie', form.value)
    } else {
      await request.post('/admin/movie', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

const handleSizeChange = (val) => {
  queryForm.value.pageSize = val
  loadData()
}

const handleCurrentChange = (val) => {
  queryForm.value.page = val
  loadData()
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style scoped>
.movies-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>