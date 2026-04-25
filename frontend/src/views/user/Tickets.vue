<template>
  <div class="user-tickets-page">
    <el-card>
      <template #header>
        <span>我的订单</span>
      </template>
      
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
            <el-button
              type="info"
              size="small"
              @click="handleView(row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
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

const tableData = ref([])
const detailVisible = ref(false)
const currentTicket = ref({})

const loadData = async () => {
  try {
    const res = await request.get('/user/ticket/list')
    tableData.value = res.data
  } catch (error) {
    console.error('加载订单列表失败:', error)
  }
}

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

const handleView = (row) => {
  currentTicket.value = row
  detailVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.user-tickets-page {
  padding: 20px;
}
</style>