<template>
  <div class="header">
    <div class="header-content">
      <div class="logo" @click="$router.push('/')">
        <h1>ItCast Store</h1>
      </div>
      <el-menu
        mode="horizontal"
        :default-active="activeIndex"
        @select="handleSelect"
        class="menu"
      >
        <el-menu-item index="home">首页</el-menu-item>
        <el-menu-item index="products">图书</el-menu-item>
        <el-menu-item index="cart" v-if="userStore.isAuthenticated">购物车</el-menu-item>
        <el-menu-item index="orders" v-if="userStore.isAuthenticated">我的订单</el-menu-item>
      </el-menu>
      <div class="user-actions">
        <template v-if="userStore.isAuthenticated">
          <el-dropdown>
            <span class="user-name">{{ userStore.user?.username }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeIndex = computed(() => {
  const name = route.name as string
  return name || 'home'
})

const handleSelect = (key: string) => {
  router.push({ name: key })
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  cursor: pointer;
}

.logo h1 {
  margin: 0;
  color: #409eff;
}

.menu {
  flex: 1;
  border-bottom: none;
}

.user-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.user-name {
  cursor: pointer;
  color: #409eff;
}
</style>
