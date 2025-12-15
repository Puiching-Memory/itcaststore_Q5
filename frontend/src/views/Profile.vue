<template>
  <div class="profile-page">
    <Header />
    <el-container>
      <el-main>
        <h2>个人中心</h2>
        <el-card v-if="userStore.user">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户名">{{ userStore.user.username }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.user.email }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ userStore.user.gender }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ userStore.user.telephone }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ userStore.user.role }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
}
</style>
