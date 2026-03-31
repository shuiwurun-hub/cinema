<template>
  <div class="halls-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>影厅管理</span>
          <el-button type="primary" @click="handleAdd">新增影厅</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="name" label="影厅名称" />
        <el-table-column prop="type" label="影厅类型" />
        <el-table-column prop="rowCount" label="行数" />
        <el-table-column prop="colCount" label="列数" />
        <el-table-column prop="totalSeats" label="总座位数" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '维护中' }}
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
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="影院" prop="cinemaId">
          <el-select v-model="form.cinemaId" placeholder="请选择影院">
            <el-option
              v-for="item in cinemaList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="影厅名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="影厅类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="IMAX" value="IMAX" />
            <el-option label="3D" value="3D" />
            <el-option label="2D" value="2D" />
            <el-option label="4DX" value="4DX" />
          </el-select>
        </el-form-item>
        <el-form-item label="行数" prop="rowCount">
          <el-input-number v-model="form.rowCount" :min="1" />
        </el-form-item>
        <el-form-item label="列数" prop="colCount">
          <el-input-number v-model="form.colCount" :min="1" />
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
const cinemaList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增影厅')
const formRef = ref()
const form = ref({
  id: null,
  cinemaId: null,
  name: '',
  type: '',
  rowCount: 0,
  colCount: 0
})

const rules = {
  cinemaId: [{ required: true, message: '请选择影院', trigger: 'change' }],
  name: [{ required: true, message: '请输入影厅名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择影厅类型', trigger: 'change' }],
  rowCount: [{ required: true, message: '请输入行数', trigger: 'blur' }],
  colCount: [{ required: true, message: '请输入列数', trigger: 'blur' }]
}

const loadCinemas = async () => {
  try {
    const res = await request.get('/admin/cinema/list')
    cinemaList.value = res.data
  } catch (error) {
    console.error('加载影院列表失败:', error)
  }
}

const loadData = async () => {
  try {
    const res = await request.get('/admin/hall/list')
    tableData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增影厅'
  form.value = {
    id: null,
    cinemaId: null,
    name: '',
    type: '',
    rowCount: 0,
    colCount: 0
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑影厅'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleStatus = async (row) => {
  try {
    await request.post(`/admin/hall/status/${row.status === 1 ? 0 : 1}`, null, {
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
    await ElMessageBox.confirm('确定要删除该影厅吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/hall/${row.id}`)
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
      await request.put('/admin/hall', form.value)
    } else {
      await request.post('/admin/hall', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

onMounted(() => {
  loadCinemas()
  loadData()
})
</script>

<style scoped>
.halls-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>