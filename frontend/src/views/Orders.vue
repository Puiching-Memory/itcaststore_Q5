<template>
  <div class="orders-page">
    <Header />
    <el-container>
      <el-main>
        <h2>我的订单</h2>
        <el-table :data="orders" v-loading="loading">
          <el-table-column prop="id" label="订单号" />
          <el-table-column prop="money" label="金额" />
          <el-table-column prop="receiverName" label="收货人" />
          <el-table-column prop="receiverAddress" label="收货地址" />
          <el-table-column prop="paystate" label="支付状态">
            <template #default="{ row }">
              <el-tag :type="row.paystate === 1 ? 'success' : 'warning'">
                {{ row.paystate === 1 ? '已支付' : '未支付' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="ordertime" label="下单时间" />
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import api from '@/utils/api'

const orders = ref([])
const loading = ref(false)

const loadOrders = async () => {
  loading.value = true
  try {
    const response = await api.get('/orders/list')
    orders.value = response.data.data
  } catch (error) {
    console.error('加载订单失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
  min-height: 100vh;
}
</style>
