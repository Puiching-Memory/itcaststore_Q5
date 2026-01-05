<template>
  <div class="payment-page">
    <Header />
    <div class="payment-container">
      <el-card class="payment-card">
        <div class="payment-header">
          <Icon icon="mdi:shield-check" class="shield-icon" />
          <h2>安全支付</h2>
          <p class="order-info">订单号: {{ orderId }}</p>
        </div>

        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="5" animated />
        </div>

        <div v-else-if="order" class="payment-content">
          <div class="amount-section">
            <span class="label">支付金额</span>
            <span class="amount">¥{{ order.money?.toFixed(2) }}</span>
          </div>

          <div class="method-section">
            <h3>选择支付方式</h3>
            <div class="payment-methods">
              <div 
                class="method-item" 
                :class="{ active: selectedMethod === 'wechat' }"
                @click="selectedMethod = 'wechat'"
              >
                <Icon icon="logos:wechat-pay" class="method-icon" />
                <span>微信支付</span>
                <Icon v-if="selectedMethod === 'wechat'" icon="mdi:check-circle" class="check-icon" />
              </div>
              <div 
                class="method-item" 
                :class="{ active: selectedMethod === 'alipay' }"
                @click="selectedMethod = 'alipay'"
              >
                <Icon icon="logos:alipay" class="method-icon" />
                <span>支付宝</span>
                <Icon v-if="selectedMethod === 'alipay'" icon="mdi:check-circle" class="check-icon" />
              </div>
              <div 
                class="method-item" 
                :class="{ active: selectedMethod === 'bank' }"
                @click="selectedMethod = 'bank'"
              >
                <Icon icon="mdi:bank" class="method-icon bank" />
                <span>网银支付</span>
                <Icon v-if="selectedMethod === 'bank'" icon="mdi:check-circle" class="check-icon" />
              </div>
            </div>
          </div>

          <div class="action-section">
            <el-button 
              type="primary" 
              size="large" 
              class="pay-button" 
              :loading="paying"
              @click="handlePay"
            >
              立即支付 ¥{{ order.money?.toFixed(2) }}
            </el-button>
            <el-button size="large" @click="$router.back()">返回</el-button>
          </div>
        </div>

        <div v-else class="error-state">
          <el-result icon="error" title="订单不存在" sub-title="请检查订单号是否正确">
            <template #extra>
              <el-button type="primary" @click="$router.push('/orders')">返回订单列表</el-button>
            </template>
          </el-result>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'
import Header from '@/components/Header.vue'
import api from '@/utils/api'

const route = useRoute()
const router = useRouter()
const orderId = ref(route.query.orderId as string)
const order = ref<any>(null)
const loading = ref(true)
const paying = ref(false)
const selectedMethod = ref('wechat')

const loadOrderDetails = async () => {
  if (!orderId.value) {
    loading.value = false
    return
  }

  try {
    // 这里我们直接从订单列表接口过滤，或者如果后端有详情接口就用详情接口
    // 暂时使用列表接口过滤，因为后端目前没有公开的单个订单查询接口（除了管理员）
    const response = await api.get('/orders/list')
    const orders = response.data.data
    order.value = orders.find((o: any) => o.id === orderId.value)
    
    if (order.value && order.value.paystate === 1) {
      ElMessage.info('该订单已支付')
      router.push('/orders')
    }
  } catch (error) {
    console.error('加载订单详情失败:', error)
    ElMessage.error('加载订单详情失败')
  } finally {
    loading.value = false
  }
}

const handlePay = async () => {
  paying.value = true
  try {
    const response = await api.post(`/orders/${orderId.value}/pay`)
    if (response.data.code === 200) {
      ElMessage.success('支付成功！')
      setTimeout(() => {
        router.push('/orders')
      }, 1500)
    } else {
      ElMessage.error(response.data.message || '支付失败')
    }
  } catch (error: any) {
    console.error('支付失败:', error)
    ElMessage.error(error.response?.data?.message || '支付失败，请稍后重试')
  } finally {
    paying.value = false
  }
}

onMounted(() => {
  loadOrderDetails()
})
</script>

<style scoped>
.payment-page {
  min-height: 100vh;
  background: #f2f2f7;
}

.payment-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 0 20px;
}

.payment-card {
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.05);
  border: none;
}

.payment-header {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.shield-icon {
  font-size: 48px;
  color: #67c23a;
  margin-bottom: 12px;
}

.payment-header h2 {
  margin: 0;
  font-size: 24px;
  color: #1c1c1e;
}

.order-info {
  color: #8e8e93;
  font-size: 14px;
  margin-top: 8px;
}

.amount-section {
  text-align: center;
  padding: 30px 0;
}

.amount-section .label {
  display: block;
  color: #8e8e93;
  font-size: 14px;
  margin-bottom: 8px;
}

.amount-section .amount {
  font-size: 40px;
  font-weight: 700;
  color: #1c1c1e;
}

.method-section h3 {
  font-size: 16px;
  color: #1c1c1e;
  margin-bottom: 16px;
}

.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.method-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 2px solid #f2f2f7;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.method-item:hover {
  background: #f9f9fb;
}

.method-item.active {
  border-color: #007aff;
  background: #f0f7ff;
}

.method-icon {
  font-size: 24px;
  margin-right: 12px;
}

.method-icon.bank {
  color: #ff9500;
}

.check-icon {
  position: absolute;
  right: 16px;
  color: #007aff;
  font-size: 20px;
}

.action-section {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.pay-button {
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
}

.loading-state, .error-state {
  padding: 40px 0;
}
</style>
