<template>
  <div id="app">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>Afriends 管理系统</h1>
          <el-menu
            :default-active="$route.path"
            mode="horizontal"
            router
            class="nav-menu"
          >
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/table-selector">表选择器</el-menu-item>
          </el-menu>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()

onMounted(() => {
  // 初始化应用
  appStore.initApp()
})
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-content h1 {
  margin: 0;
  color: #409eff;
}

.nav-menu {
  border-bottom: none;
}

.el-header {
  background-color: #f5f5f5;
  border-bottom: 1px solid #e4e7ed;
}

.el-main {
  padding: 20px;
  min-height: calc(100vh - 60px);
}
</style>
