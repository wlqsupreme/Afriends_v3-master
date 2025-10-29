<template>
  <div class="loading-spinner" v-if="visible">
    <el-icon class="is-loading" size="24">
      <Loading />
    </el-icon>
    <span class="loading-text">{{ text }}</span>
  </div>
</template>

<script setup lang="ts">
import { Loading } from '@element-plus/icons-vue'

interface Props {
  visible: boolean
  text?: string
}

withDefaults(defineProps<Props>(), {
  text: '加载中...'
})
</script>

<style scoped>
.loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.loading-text {
  margin-top: 10px;
  color: #909399;
  font-size: 14px;
}
</style>
