<template>
  <div class="product-detail">
    <Header />
    <el-container>
      <el-main>
        <el-row :gutter="40" v-if="product">
          <el-col :span="12">
            <img :src="product.imgurl" class="detail-image" />
          </el-col>
          <el-col :span="12">
            <h1>{{ product.name }}</h1>
            <p class="price">¥{{ product.price }}</p>
            <p>分类：{{ product.category }}</p>
            <p>库存：{{ product.pnum }}</p>
            <p>{{ product.description }}</p>
            <el-button type="primary" size="large" @click="addToCart">加入购物车</el-button>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '@/components/Header.vue'
import api from '@/utils/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const product = ref<any>(null)

const loadProduct = async () => {
  try {
    const response = await api.get(`/products/${route.params.id}`)
    product.value = response.data.data
  } catch (error) {
    console.error('加载商品详情失败:', error)
  }
}

const addToCart = () => {
  ElMessage.success('已添加到购物车')
  // TODO: 实现购物车功能
}

onMounted(() => {
  loadProduct()
})
</script>

<style scoped>
.product-detail {
  min-height: 100vh;
}

.detail-image {
  width: 100%;
  max-height: 600px;
  object-fit: contain;
}

.price {
  color: #f56c6c;
  font-size: 28px;
  font-weight: bold;
  margin: 20px 0;
}
</style>
