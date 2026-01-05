import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/utils/api'
import { useUserStore } from './user'

export interface CartItem {
  id: string
  name: string
  price: number
  imgurl: string
  quantity: number
  category?: string
  description?: string
}

interface CartItemResponse {
  productId: string
  quantity: number
  name: string
  price: number
  imgurl?: string
  category?: string
  description?: string
}

const mapResponseToItem = (payload: CartItemResponse): CartItem => ({
  id: payload.productId,
  name: payload.name,
  price: Number(payload.price),
  imgurl: payload.imgurl || '/products/product-1.png',
  quantity: payload.quantity,
  category: payload.category,
  description: payload.description
})

const handleError = (message: string, error: any) => {
  console.error(message, error)
  if (error?.response?.data?.message) {
    ElMessage.error(error.response.data.message)
  } else {
    ElMessage.error(message)
  }
}

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])
  const loading = ref(false)
  const userStore = useUserStore()

  const totalQuantity = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  })

  const fetchCart = async () => {
    if (!userStore.isAuthenticated) {
      items.value = []
      return
    }

    loading.value = true
    try {
      const response = await api.get('/cart')
      const payload: CartItemResponse[] = response.data.data || []
      items.value = payload.map(mapResponseToItem)
    } catch (error) {
      handleError('加载购物车失败，请稍后再试', error)
    } finally {
      loading.value = false
    }
  }

  const upsertItem = (payload: CartItemResponse) => {
    const parsed = mapResponseToItem(payload)
    const existing = items.value.find(item => item.id === parsed.id)
    if (existing) {
      existing.quantity = parsed.quantity
      existing.name = parsed.name
      existing.price = parsed.price
      existing.imgurl = parsed.imgurl
      existing.category = parsed.category
      existing.description = parsed.description
    } else {
      items.value.push(parsed)
    }
  }

  const addItem = async (
    product: {
      id: string
      name: string
      price: number
      imgurl?: string
      category?: string
      description?: string
    },
    quantity: number = 1
  ) => {
    if (!userStore.isAuthenticated) {
      ElMessage.warning({
        message: '请先登录后再添加商品到购物车',
        duration: 2000
      })
      return false
    }

    try {
      const response = await api.post('/cart', {
        productId: product.id,
        quantity
      })
      upsertItem(response.data.data)
      ElMessage.success({
        message: `已将《${product.name}》添加到购物车`,
        duration: 1200
      })
      return true
    } catch (error) {
      handleError('添加商品到购物车失败', error)
      return false
    }
  }

  const updateQuantity = async (productId: string, quantity: number) => {
    if (quantity <= 0) {
      await removeItem(productId)
      return
    }

    try {
      const response = await api.put(`/cart/${productId}`, {
        productId,
        quantity
      })
      upsertItem(response.data.data)
    } catch (error) {
      handleError('更新购物车数量失败', error)
      await fetchCart()
    }
  }

  const removeItem = async (productId: string) => {
    try {
      await api.delete(`/cart/${productId}`)
      const index = items.value.findIndex(item => item.id === productId)
      if (index !== -1) {
        const item = items.value[index]
        items.value.splice(index, 1)
        ElMessage.success({
          message: `已从购物车删除《${item.name}》`,
          duration: 1200
        })
      }
    } catch (error) {
      handleError('删除购物车商品失败', error)
    }
  }

  const clearCart = async () => {
    if (!userStore.isAuthenticated) {
      items.value = []
      return
    }

    try {
      await api.delete('/cart')
      items.value = []
      ElMessage.success({
        message: '购物车已清空',
        duration: 1200
      })
    } catch (error) {
      handleError('清空购物车失败', error)
    }
  }

  const isInCart = (productId: string) => {
    return items.value.some(item => item.id === productId)
  }

  const getItemQuantity = (productId: string) => {
    const item = items.value.find(item => item.id === productId)
    return item ? item.quantity : 0
  }

  watch(
    () => userStore.isAuthenticated,
    (authenticated) => {
      if (authenticated) {
        fetchCart()
      } else {
        items.value = []
      }
    },
    { immediate: true }
  )

  return {
    items,
    loading,
    totalQuantity,
    totalPrice,
    addItem,
    updateQuantity,
    removeItem,
    clearCart,
    isInCart,
    getItemQuantity,
    fetchCart
  }
})

