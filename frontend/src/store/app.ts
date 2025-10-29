import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 状态
  const loading = ref(false)
  const theme = ref('light')
  const collapsed = ref(false)

  // 动作
  const setLoading = (value: boolean) => {
    loading.value = value
  }

  const setTheme = (value: 'light' | 'dark') => {
    theme.value = value
  }

  const setCollapsed = (value: boolean) => {
    collapsed.value = value
  }

  const initApp = () => {
    console.log('应用初始化完成')
  }

  return {
    loading,
    theme,
    collapsed,
    setLoading,
    setTheme,
    setCollapsed,
    initApp
  }
})
