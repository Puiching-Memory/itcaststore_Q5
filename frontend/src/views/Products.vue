<template>
  <div class="products-page">
    <Header />
    <el-container>
      <el-main>
        <div class="search-bar">
          <el-input
            v-model="keyword"
            placeholder="搜索图书..."
            @keyup.enter="searchProducts"
            style="width: 300px"
          >
            <template #append>
              <el-button @click="searchProducts">搜索</el-button>
            </template>
          </el-input>
        </div>

        <el-row :gutter="20">
          <el-col :span="6" v-for="product in products" :key="product.id">
            <el-card :body-style="{ padding: '0px' }" @click="goToProduct(product.id)">
              <img :src="product.imgurl" class="product-image" />
              <div style="padding: 14px">
                <h3>{{ product.name }}</h3>
                <p class="price">¥{{ product.price }}</p>
                <el-button type="primary" @click.stop="addToCart(product)">加入购物车</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadProducts"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import api from '@/utils/api'
import { ElMessage } from 'element-plus'

interface Product {
  id: number
  name: string
  price: number
  imgurl: string
}

const router = useRouter()
const products = ref<Product[]>([])
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadProducts = async () => {
  try {
    const params: any = {
      page: currentPage.value - 1,
      size: pageSize.value
    }
    if (keyword.value) {
      params.keyword = keyword.value
    }
    const response = await api.get('/products', { params })
    products.value = response.data.data.content
    total.value = response.data.data.totalElements
  } catch (error) {
    console.error('加载商品失败:', error)
  }
}

const searchProducts = () => {
  currentPage.value = 1
  loadProducts()
}

const goToProduct = (id: number) => {
  router.push(`/product/${id}`)
}

const addToCart = (_product: Product) => {
  ElMessage.success('已添加到购物车')
  // TODO: 实现购物车功能
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
.products-page {
  min-height: 100vh;
}

.search-bar {
  margin-bottom: 20px;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 10px 0;
}
</style>
