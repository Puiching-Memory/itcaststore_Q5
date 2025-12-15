<template>
  <div class="home">
    <el-container>
      <el-header>
        <Header />
      </el-header>
      <el-main>
        <el-carousel height="400px" :interval="5000">
          <el-carousel-item v-for="item in banners" :key="item.id">
            <img :src="item.image" :alt="item.title" class="banner-image" />
          </el-carousel-item>
        </el-carousel>

        <div class="hot-products">
          <h2>本周热卖</h2>
          <el-row :gutter="20">
            <el-col :span="6" v-for="product in hotProducts" :key="product.id">
              <el-card :body-style="{ padding: '0px' }" @click="goToProduct(product.id)">
                <img :src="product.imgurl" class="product-image" />
                <div style="padding: 14px">
                  <h3>{{ product.name }}</h3>
                  <p class="price">¥{{ product.price }}</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <div class="notices">
          <h2>系统公告</h2>
          <el-timeline>
            <el-timeline-item
              v-for="notice in notices"
              :key="notice.id"
              :timestamp="notice.time"
              placement="top"
            >
              <el-card>
                <h4>{{ notice.title }}</h4>
                <p v-html="notice.details"></p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import api from '@/utils/api'

interface Product {
  id: number
  name: string
  price: number
  imgurl: string
}

interface Notice {
  id: number
  title: string
  details: string
  time: string
}

interface Banner {
  id: number
  title: string
  image: string
}

const router = useRouter()
const hotProducts = ref<Product[]>([])
const notices = ref<Notice[]>([])
const banners = ref<Banner[]>([
  { id: 1, title: 'Banner 1', image: '/banner1.jpg' },
  { id: 2, title: 'Banner 2', image: '/banner2.jpg' }
])

const goToProduct = (id: number) => {
  router.push(`/product/${id}`)
}

onMounted(async () => {
  try {
    const [productsRes, noticesRes] = await Promise.all([
      api.get('/products/hot'),
      api.get('/notices')
    ])
    hotProducts.value = productsRes.data.data
    notices.value = noticesRes.data.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
})
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.banner-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.hot-products,
.notices {
  margin-top: 40px;
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
}
</style>
