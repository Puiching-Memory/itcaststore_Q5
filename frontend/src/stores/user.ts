import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/utils/api'

export interface User {
  id: number
  username: string
  email: string
  gender: string
  telephone: string
  role: string
}

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))

  const isAuthenticated = ref(!!token.value)

  const login = async (username: string, password: string) => {
    const response = await api.post('/auth/login', { username, password })
    token.value = response.data.data.token
    user.value = response.data.data.user
    if (token.value) {
      localStorage.setItem('token', token.value)
    }
    isAuthenticated.value = true
    return response.data
  }

  const register = async (userData: any) => {
    const response = await api.post('/auth/register', userData)
    token.value = response.data.data.token
    user.value = response.data.data.user
    if (token.value) {
    localStorage.setItem('token', token.value)
    }
    isAuthenticated.value = true
    return response.data
  }

  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    isAuthenticated.value = false
  }

  return {
    user,
    token,
    isAuthenticated,
    login,
    register,
    logout
  }
})
