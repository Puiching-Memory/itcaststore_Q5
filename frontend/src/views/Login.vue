<template>
  <div class="login-container">
    <div class="login-card">
      <div class="card-header">
        <Icon icon="mdi:login" class="header-icon" />
        <h2>用户登录</h2>
      </div>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label class="form-label">用户名</label>
          <div class="input-wrapper">
            <Icon icon="mdi:account" class="input-icon" />
            <input
              v-model="form.username"
              type="text"
              placeholder="请输入用户名"
              class="form-input"
              required
            />
          </div>
        </div>
        <div class="form-group">
          <label class="form-label">密码</label>
          <div class="input-wrapper">
            <Icon icon="mdi:lock" class="input-icon" />
            <input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              class="form-input"
              required
            />
          </div>
        </div>
        <button type="submit" class="submit-btn" :disabled="loading">
          <Icon icon="mdi:login" class="btn-icon" />
          <span>{{ loading ? '登录中...' : '登录' }}</span>
        </button>
        <div class="form-footer">
          <button type="button" class="link-btn" @click="$router.push('/register')">
            <Icon icon="mdi:account-plus" class="link-icon" />
            <span>还没有账号？立即注册</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('/backgrounds/background-texture.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  padding: 32px;
  position: relative;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(2px);
  -webkit-backdrop-filter: blur(2px);
  z-index: 0;
}

.login-container > * {
  position: relative;
  z-index: 1;
}

.login-card {
  width: 100%;
  max-width: 440px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(50px);
  -webkit-backdrop-filter: blur(50px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 40px;
  padding: 48px;
  box-shadow: 0 24px 48px -12px rgba(0, 0, 0, 0.15),
              0 1px 0 rgba(255, 255, 255, 0.6) inset;
}

.card-header {
  text-align: center;
  margin-bottom: 40px;
}

.card-header h2 {
  margin: 16px 0 0 0;
  font-size: 32px;
  font-weight: 800;
  color: #1C1C1E;
  letter-spacing: -0.02em;
}

.header-icon {
  font-size: 48px;
  width: 48px;
  height: 48px;
  color: #1C1C1E;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.form-label {
  font-size: 13px;
  font-weight: 700;
  color: #636366;
  text-transform: uppercase;
  letter-spacing: 0.1em;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 20px;
  font-size: 20px;
  width: 20px;
  height: 20px;
  color: #8E8E93;
  z-index: 1;
}

.form-input {
  width: 100%;
  padding: 16px 20px 16px 56px;
  border-radius: 20px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  font-size: 17px;
  color: #1C1C1E;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  outline: none;
}

.form-input:focus {
  border-color: #1C1C1E;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.06),
              0 0 0 3px rgba(28, 28, 30, 0.1);
  background: rgba(255, 255, 255, 0.95);
}

.form-input::placeholder {
  color: #8E8E93;
}

.submit-btn {
  width: 100%;
  padding: 16px 24px;
  border-radius: 24px;
  background: #1C1C1E;
  color: white;
  border: none;
  font-weight: 700;
  font-size: 17px;
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(28, 28, 30, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 8px;
}

.submit-btn:hover:not(:disabled) {
  background: #2C2C2E;
  box-shadow: 0 6px 16px rgba(28, 28, 30, 0.35);
  transform: translateY(-1px);
}

.submit-btn:active:not(:disabled) {
  transform: scale(0.98);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-icon {
  font-size: 20px;
  width: 20px;
  height: 20px;
}

.form-footer {
  text-align: center;
  margin-top: 8px;
}

.link-btn {
  background: none;
  border: none;
  color: #1C1C1E;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border-radius: 16px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.link-btn:hover {
  background: rgba(0, 0, 0, 0.04);
}

.link-btn:active {
  transform: scale(0.96);
}

.link-icon {
  font-size: 16px;
  width: 16px;
  height: 16px;
}

@media (max-width: 768px) {
  .login-card {
    padding: 32px 24px;
    border-radius: 32px;
  }
  
  .card-header h2 {
    font-size: 28px;
  }
}
</style>
