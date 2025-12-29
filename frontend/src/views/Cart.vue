<template>
  <div class="cart-page">
    <Header />
    <el-container>
      <el-main>
        <div class="cart-container">
          <div class="cart-header">
            <h2>
              <Icon icon="mdi:cart" class="page-icon" />
              购物车
              <span v-if="cartStore.totalQuantity > 0" class="cart-count">
                ({{ cartStore.totalQuantity }})
              </span>
            </h2>
            <el-button
              v-if="cartStore.items.length > 0"
              type="danger"
              :icon="Delete"
              @click="handleClearCart"
            >
              清空购物车
            </el-button>
          </div>

          <el-empty
            v-if="cartStore.items.length === 0"
            description="购物车是空的"
            :image-size="120"
          >
            <template #image>
              <Icon icon="mdi:cart-off" :width="120" :height="120" style="color: #c0c4cc;" />
            </template>
            <el-button type="primary" @click="$router.push('/products')">
              去购物
            </el-button>
          </el-empty>

          <div v-else class="cart-content">
            <el-card class="cart-card">
              <el-table :data="cartStore.items" style="width: 100%">
                <el-table-column label="商品" width="300">
                  <template #default="{ row }">
                    <div class="product-cell">
                      <img
                        :src="row.imgurl || '/products/product-1.png'"
                        :alt="row.name"
                        class="product-image"
                        @error="handleImageError($event)"
                      />
                      <div class="product-info">
                        <div class="product-name" @click="goToProduct(row.id)">
                          {{ row.name }}
                        </div>
                        <div v-if="row.category" class="product-category">
                          {{ row.category }}
                        </div>
                      </div>
                    </div>
                  </template>
                </el-table-column>

                <el-table-column label="单价" width="120" align="center">
                  <template #default="{ row }">
                    <span class="price-text">¥{{ row.price }}</span>
                  </template>
                </el-table-column>

                <el-table-column label="数量" width="180" align="center">
                  <template #default="{ row }">
                    <div class="quantity-control">
                      <el-button
                        :icon="Minus"
                        circle
                        size="small"
                        @click="decreaseQuantity(row.id)"
                        :disabled="row.quantity <= 1"
                      />
                      <span class="quantity-value">{{ row.quantity }}</span>
                      <el-button
                        :icon="Plus"
                        circle
                        size="small"
                        @click="increaseQuantity(row.id)"
                      />
                    </div>
                  </template>
                </el-table-column>

                <el-table-column label="小计" width="120" align="center">
                  <template #default="{ row }">
                    <span class="subtotal-text">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
                  </template>
                </el-table-column>

                <el-table-column label="操作" width="100" align="center">
                  <template #default="{ row }">
                    <el-button
                      type="danger"
                      :icon="Delete"
                      size="small"
                      circle
                      @click="handleRemoveItem(row.id)"
                    />
                  </template>
                </el-table-column>
              </el-table>

              <div class="cart-footer">
                <div class="cart-summary">
                  <div class="summary-item">
                    <span class="summary-label">商品总数：</span>
                    <span class="summary-value">{{ cartStore.totalQuantity }} 件</span>
                  </div>
                  <div class="summary-item">
                    <span class="summary-label">合计：</span>
                    <span class="summary-total">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
                  </div>
                </div>
                <div class="cart-actions">
                  <el-button
                    size="large"
                    @click="$router.push('/products')"
                  >
                    继续购物
                  </el-button>
                  <el-button
                    type="primary"
                    size="large"
                    @click="handleCheckout"
                  >
                    去结算
                  </el-button>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </el-main>
    </el-container>

    <!-- 结算对话框 -->
    <el-dialog
      v-model="checkoutDialogVisible"
      title="确认订单信息"
      width="600px"
      :close-on-click-modal="false"
      class="checkout-dialog"
    >
      <el-form
        ref="checkoutFormRef"
        :model="checkoutForm"
        :rules="checkoutRules"
        label-width="100px"
        label-position="top"
      >
        <div class="order-summary">
          <h3 class="summary-title">订单详情</h3>
          <div class="order-items">
            <div
              v-for="item in cartStore.items"
              :key="item.id"
              class="order-item"
            >
              <img
                :src="item.imgurl || '/products/product-1.png'"
                :alt="item.name"
                class="item-image"
                @error="handleImageError($event)"
              />
              <div class="item-info">
                <div class="item-name">{{ item.name }}</div>
                <div class="item-details">
                  <span>¥{{ item.price }}</span>
                  <span>× {{ item.quantity }}</span>
                </div>
              </div>
              <div class="item-subtotal">
                ¥{{ (item.price * item.quantity).toFixed(2) }}
              </div>
            </div>
          </div>
          <div class="order-total">
            <span class="total-label">订单总额：</span>
            <span class="total-amount">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
          </div>
        </div>

        <el-divider />

        <h3 class="form-title">收货信息</h3>
        <el-form-item label="收货人姓名" prop="receiverName">
          <el-input
            v-model="checkoutForm.receiverName"
            placeholder="请输入收货人姓名"
            clearable
          />
        </el-form-item>

        <el-form-item label="联系电话" prop="receiverPhone">
          <el-input
            v-model="checkoutForm.receiverPhone"
            placeholder="请输入联系电话"
            clearable
          />
        </el-form-item>

        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input
            v-model="checkoutForm.receiverAddress"
            type="textarea"
            :rows="3"
            placeholder="请输入详细收货地址"
            clearable
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="checkoutDialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="submitting"
            @click="handleSubmitOrder"
          >
            确认下单
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Delete, Plus, Minus } from '@element-plus/icons-vue'
import Header from '@/components/Header.vue'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import api from '@/utils/api'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const checkoutDialogVisible = ref(false)
const checkoutFormRef = ref<FormInstance>()
const submitting = ref(false)

const checkoutForm = reactive({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: ''
})

const checkoutRules: FormRules = {
  receiverName: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  receiverPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  receiverAddress: [
    { required: true, message: '请输入收货地址', trigger: 'blur' },
    { max: 255, message: '地址长度不能超过 255 个字符', trigger: 'blur' }
  ]
}

const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = '/products/product-1.png'
}

const goToProduct = (productId: string) => {
  router.push(`/product/${productId}`)
}

const increaseQuantity = (productId: string) => {
  const item = cartStore.items.find(item => item.id === productId)
  if (item) {
    cartStore.updateQuantity(productId, item.quantity + 1)
  }
}

const decreaseQuantity = (productId: string) => {
  const item = cartStore.items.find(item => item.id === productId)
  if (item && item.quantity > 1) {
    cartStore.updateQuantity(productId, item.quantity - 1)
  }
}

const handleRemoveItem = (productId: string) => {
  const item = cartStore.items.find(item => item.id === productId)
  if (item) {
    ElMessageBox.confirm(
      `确定要从购物车删除《${item.name}》吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      cartStore.removeItem(productId)
    }).catch(() => {
      // 用户取消
    })
  }
}

const handleClearCart = () => {
  ElMessageBox.confirm(
    '确定要清空购物车吗？',
    '确认清空',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    cartStore.clearCart()
  }).catch(() => {
    // 用户取消
  })
}

const handleCheckout = () => {
  if (cartStore.items.length === 0) {
    ElMessage.warning('购物车是空的')
    return
  }

  // 检查用户是否已登录
  if (!userStore.isAuthenticated) {
    ElMessage.warning('请先登录后再结算')
    router.push('/login')
    return
  }

  // 如果有用户信息，预填充收货信息
  if (userStore.user) {
    checkoutForm.receiverName = userStore.user.username || ''
    checkoutForm.receiverPhone = userStore.user.telephone || ''
    checkoutForm.receiverAddress = ''
  }

  checkoutDialogVisible.value = true
}

const handleSubmitOrder = async () => {
  if (!checkoutFormRef.value) return

  try {
    await checkoutFormRef.value.validate()
    submitting.value = true

    // 构建订单项（简化格式，后端会自动处理）
    const orderItems = cartStore.items.map(item => ({
      productId: item.id,
      buynum: item.quantity
    }))

    // 构建订单数据
    const orderData = {
      money: cartStore.totalPrice,
      receiverName: checkoutForm.receiverName,
      receiverPhone: checkoutForm.receiverPhone,
      receiverAddress: checkoutForm.receiverAddress,
      orderItems: orderItems
    }

    // 调用后端API创建订单
    const response = await api.post('/orders', orderData)

    if (response.data.code === 200) {
      ElMessage.success('订单创建成功！')
      
      // 清空购物车
      cartStore.clearCart()
      
      // 关闭对话框
      checkoutDialogVisible.value = false
      
      // 跳转到订单页面
      router.push('/orders')
    } else {
      ElMessage.error(response.data.message || '订单创建失败')
    }
  } catch (error: any) {
    console.error('订单创建失败:', error)
    if (error.response?.data?.message) {
      ElMessage.error(error.response.data.message)
    } else if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error('订单创建失败，请稍后重试')
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background: var(--ios-gray-100, #F2F2F7);
}

.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.cart-header h2 {
  margin: 0;
  font-size: 32px;
  font-weight: 800;
  color: var(--graphite, #1C1C1E);
  letter-spacing: -0.02em;
  display: flex;
  align-items: center;
}

.page-icon {
  font-size: 32px;
  margin-right: 12px;
  color: var(--graphite, #1C1C1E);
}

.cart-count {
  font-size: 20px;
  font-weight: 600;
  color: var(--ios-gray-600, #8E8E93);
  margin-left: 8px;
}

.cart-content {
  margin-top: 24px;
}

.cart-card {
  background: var(--frosted-bg-medium, rgba(255, 255, 255, 0.7));
  backdrop-filter: blur(var(--blur-lg, 50px));
  -webkit-backdrop-filter: blur(var(--blur-lg, 50px));
  border: 1px solid var(--frosted-border, rgba(255, 255, 255, 0.6));
  border-radius: var(--radius-2xl, 32px);
  box-shadow: var(--shadow-xl, 0 24px 48px -12px rgba(0, 0, 0, 0.08));
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 16px;
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: var(--radius-md, 20px);
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--graphite, #1C1C1E);
  cursor: pointer;
  transition: color 0.2s;
}

.product-name:hover {
  color: var(--ios-gray-700, #636366);
}

.product-category {
  font-size: 13px;
  color: var(--ios-gray-600, #8E8E93);
}

.price-text {
  font-size: 16px;
  font-weight: 600;
  color: var(--graphite, #1C1C1E);
}

.quantity-control {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.quantity-value {
  min-width: 40px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: var(--graphite, #1C1C1E);
}

.subtotal-text {
  font-size: 18px;
  font-weight: 700;
  color: var(--graphite, #1C1C1E);
}

.cart-footer {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 24px;
}

.cart-summary {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.summary-label {
  font-size: 16px;
  color: var(--ios-gray-600, #8E8E93);
}

.summary-value {
  font-size: 16px;
  font-weight: 600;
  color: var(--graphite, #1C1C1E);
}

.summary-total {
  font-size: 24px;
  font-weight: 800;
  color: var(--graphite, #1C1C1E);
}

.cart-actions {
  display: flex;
  gap: 12px;
}

:deep(.el-main) {
  padding: 32px 0;
  background: transparent;
}

:deep(.el-table) {
  background: transparent;
}

:deep(.el-table th) {
  background: rgba(255, 255, 255, 0.5);
  color: var(--graphite, #1C1C1E);
  font-weight: 600;
}

:deep(.el-table td) {
  background: transparent;
}

:deep(.el-table tr:hover > td) {
  background: rgba(255, 255, 255, 0.3);
}

@media (max-width: 768px) {
  .cart-container {
    padding: 16px;
  }

  .cart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .cart-header h2 {
    font-size: 24px;
  }

  .product-cell {
    flex-direction: column;
    align-items: flex-start;
  }

  .product-image {
    width: 60px;
    height: 60px;
  }

  .cart-footer {
    flex-direction: column;
    align-items: stretch;
  }

  .cart-actions {
    width: 100%;
    flex-direction: column;
  }

  .cart-actions .el-button {
    width: 100%;
  }
}

/* 结算对话框样式 */
.checkout-dialog :deep(.el-dialog) {
  border-radius: 28px;
  overflow: hidden;
}

.checkout-dialog :deep(.el-dialog__header) {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  padding: 24px 32px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.checkout-dialog :deep(.el-dialog__title) {
  font-size: 20px;
  font-weight: 700;
  color: #1C1C1E;
}

.checkout-dialog :deep(.el-dialog__body) {
  padding: 32px;
  background: var(--ios-gray-100, #F2F2F7);
}

.checkout-dialog :deep(.el-dialog__footer) {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  padding: 20px 32px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.order-summary {
  margin-bottom: 24px;
}

.summary-title,
.form-title {
  font-size: 18px;
  font-weight: 700;
  color: #1C1C1E;
  margin-bottom: 16px;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
  max-height: 300px;
  overflow-y: auto;
  padding-right: 8px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-size: 15px;
  font-weight: 600;
  color: #1C1C1E;
}

.item-details {
  display: flex;
  gap: 12px;
  font-size: 13px;
  color: #8E8E93;
}

.item-subtotal {
  font-size: 16px;
  font-weight: 700;
  color: #1C1C1E;
  min-width: 80px;
  text-align: right;
}

.order-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.total-label {
  font-size: 16px;
  font-weight: 600;
  color: #1C1C1E;
}

.total-amount {
  font-size: 24px;
  font-weight: 800;
  color: #1C1C1E;
}

.checkout-dialog :deep(.el-form-item__label) {
  font-size: 13px;
  font-weight: 700;
  color: #636366;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  margin-bottom: 8px;
  padding: 0;
}

.checkout-dialog :deep(.el-input__inner),
.checkout-dialog :deep(.el-textarea__inner) {
  border-radius: 20px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.checkout-dialog :deep(.el-input__inner):focus,
.checkout-dialog :deep(.el-textarea__inner):focus {
  border-color: #1C1C1E;
  box-shadow: 0 0 0 3px rgba(28, 28, 30, 0.1);
  background: rgba(255, 255, 255, 0.95);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dialog-footer .el-button {
  border-radius: 20px;
  padding: 12px 24px;
  font-weight: 600;
}

/* 滚动条样式 */
.order-items::-webkit-scrollbar {
  width: 6px;
}

.order-items::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.04);
  border-radius: 3px;
}

.order-items::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}

.order-items::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.3);
}
</style>
