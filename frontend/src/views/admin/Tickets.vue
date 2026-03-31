<template>
  <div class="tickets-page">
    <el-card>
      <template #header>
        <span>票务管理</span>
      </template>
      
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="订单号">
          <el-input v-model="queryForm.number" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="电影名称">
          <el-input v-model="queryForm.movieName" placeholder="请输入电影名称" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="待支付" :value="1" />
            <el-option label="已支付" :value="2" />
            <el-option label="已取消" :value="3" />
            <el-option label="已退款" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="number" label="订单号" />
        <el-table-column prop="movieName" label="电影名称" />
        <el-table-column prop="cinemaName" label="影院名称" />
        <el-table-column prop="hallName" label="影厅名称" />
        <el-table-column prop="showDate" label="放映日期" />
        <el-table-column prop="showTime" label="放映时间" />
        <el-table-column prop="amount" label="订单金额">
          <template #default="{ row }">
            ¥{{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">查看详情</el-button>
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

    <el-dialog v-model="detailVisible" title="订单详情" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentTicket.number }}</el-descriptions-item>
        <el-descriptions-item label="电影名称">{{ currentTicket.movieName }}</el-descriptions-item>
        <el-descriptions-item label="影院名称">{{ currentTicket.cinemaName }}</el-descriptions-item>
        <el-descriptions-item label="影厅名称">{{ currentTicket.hallName }}</el-descriptions-item>
        <el-descriptions-item label="放映日期">{{ currentTicket.showDate }}</el-descriptions-item>
        <el-descriptions-item label="放映时间">{{ currentTicket.showTime }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentTicket.amount }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentTicket.status)">
            {{ getStatusText(currentTicket.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ getPayMethodText(currentTicket.payMethod) }}
        </el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ currentTicket.payTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="取消原因" :span="2">
          {{ currentTicket.cancelReason || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      <el-divider>座位信息</el-divider>
      <el-table :data="currentTicket.ticketDetails" style="width: 100%">
        <el-table-column prop="rowNum" label="行号" />
        <el-table-column prop="colNum" label="列号" />
        <el-table-column prop="seatType" label="座位类型" />
        <el-table-column prop="price" label="票价">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const queryForm = ref({
  number: '',
  movieName: '',
  status: null,
  page: 1,
  pageSize: 10
})

const tableData = ref([])
const total = ref(0)
const detailVisible = ref(false)
const currentTicket = ref({})

const getStatusText = (status) => {
  const statusMap = {
    1: '待支付',
    2: '已支付',
    3: '已取消',
    4: '已退款'
  }
  return statusMap[status] || '未知'
}

const getStatusType = (status) => {
  const typeMap = {
    1: 'warning',
    2: 'success',
    3: 'info',
    4: 'danger'
  }
  return typeMap[status] || 'info'
}

const getPayMethodText = (method) => {
  const methodMap = {
    1: '微信支付',
    2: '支付宝',
    3: '银行卡'
  }
  return methodMap[method] || '未知'
}

const loadData = async () => {
  try {
    const res = await request.get('/admin/ticket/page', { params: queryForm.value })
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
    number: '',
    movieName: '',
    status: null,
    page: 1,
    pageSize: 10
  }
  loadData()
}

const handleView = (row) => {
  currentTicket.value = row
  detailVisible.value = true
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
  loadData()
})
</script>

<style scoped>
.tickets-page {
  padding: 20px;
}
</style>