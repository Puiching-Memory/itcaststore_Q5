<template>
  <div class="cart-page">
    <Header />
    <el-container>
      <el-main>
        <h2>
          <Icon icon="mdi:cart" class="page-icon" />
          购物车
        </h2>
        <el-empty v-if="cartItems.length === 0" description="购物车是空的">
          <template #image>
            <Icon icon="mdi:cart-off" :width="120" :height="120" style="color: #c0c4cc;" />
          </template>
        </el-empty>
        <el-table :data="cartItems" v-else>
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="price" label="价格" />
          <el-table-column prop="quantity" label="数量" />
          <el-table-column label="操作">
            <template #default>
              <el-button type="danger" size="small">
                <Icon icon="mdi:delete" class="btn-icon" />
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Icon } from '@iconify/vue'
import Header from '@/components/Header.vue'

const cartItems = ref([])
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
}

.page-icon {
  font-size: 24px;
  margin-right: 8px;
  vertical-align: middle;
}

.btn-icon {
  font-size: 14px;
  margin-right: 4px;
  vertical-align: middle;
}
</style>
