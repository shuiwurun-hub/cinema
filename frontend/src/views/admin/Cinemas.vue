<template>
  <div class="cinemas-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>影院管理</span>
          <el-button type="primary" @click="handleAdd">新增影院</el-button>
        </div>
      </template>
      
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="name" label="影院名称" />
        <el-table-column prop="address" label="影院地址" />
        <el-table-column prop="phone" label="联系电话" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '营业中' : '已关闭' }}
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
              {{ row.status === 1 ? '关闭' : '开启' }}
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="影院名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="影院地址" prop="address">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="影院描述" prop="description">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="影院图片" prop="image">
          <el-upload
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            :headers="uploadHeaders"
            name="file"
          >
            <el-button type="primary">上传图片</el-button>
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

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增影院')
const formRef = ref()
const form = ref({
  id: null,
  name: '',
  address: '',
  phone: '',
  description: '',
  image: ''
})

const rules = {
  name: [{ required: true, message: '请输入影院名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入影院地址', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await request.get('/admin/cinema/list')
    tableData.value = res.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增影院'
  form.value = {
    id: null,
    name: '',
    address: '',
    phone: '',
    description: '',
    image: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑影院'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleStatus = async (row) => {
  try {
    await request.post(`/admin/cinema/status/${row.status === 1 ? 0 : 1}`, null, {
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
    await ElMessageBox.confirm('确定要删除该影院吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/admin/cinema/${row.id}`)
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
      await request.put('/admin/cinema', form.value)
    } else {
      await request.post('/admin/cinema', form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.cinemas-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>