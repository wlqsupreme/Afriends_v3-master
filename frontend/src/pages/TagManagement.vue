<template>
  <div class="tag-management">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="goBack" :icon="ArrowLeft" circle />
        <h1>标签管理</h1>
      </div>
    </div>

    <div class="content">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="tag-card">
            <template #header>
              <div class="card-header">
                <h3>软标签管理</h3>
                <el-button type="primary" size="small" @click="addSoftTag">
                  <el-icon><Plus /></el-icon>
                  添加标签
                </el-button>
              </div>
            </template>
            <div class="tag-list">
              <el-tag
                v-for="tag in softTags"
                :key="tag.id"
                class="tag-item"
                closable
                @close="removeSoftTag(tag.id)"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card class="tag-card">
            <template #header>
              <div class="card-header">
                <h3>硬标签管理</h3>
                <el-button type="primary" size="small" @click="addHardTag">
                  <el-icon><Plus /></el-icon>
                  添加标签
                </el-button>
              </div>
            </template>
            <div class="tag-list">
              <el-tag
                v-for="tag in hardTags"
                :key="tag.id"
                class="tag-item"
                closable
                type="warning"
                @close="removeHardTag(tag.id)"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="stats-card">
        <template #header>
          <h3>标签统计</h3>
        </template>
        <div class="stats-content">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ softTags.length }}</div>
                <div class="stat-label">软标签数量</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ hardTags.length }}</div>
                <div class="stat-label">硬标签数量</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ totalTagUsage }}</div>
                <div class="stat-label">总使用次数</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ mostUsedTag }}</div>
                <div class="stat-label">最常用标签</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Plus } from '@element-plus/icons-vue'

const router = useRouter()

const softTags = ref([
  { id: 1, name: '技术', usage: 1250 },
  { id: 2, name: '娱乐', usage: 980 },
  { id: 3, name: '生活', usage: 756 },
  { id: 4, name: '学习', usage: 642 }
])

const hardTags = ref([
  { id: 1, name: '原创', usage: 890 },
  { id: 2, name: '转载', usage: 567 },
  { id: 3, name: '热门', usage: 445 },
  { id: 4, name: '推荐', usage: 334 }
])

const totalTagUsage = computed(() => {
  return [...softTags.value, ...hardTags.value].reduce((sum, tag) => sum + tag.usage, 0)
})

const mostUsedTag = computed(() => {
  const allTags = [...softTags.value, ...hardTags.value]
  const maxUsage = Math.max(...allTags.map(tag => tag.usage))
  const mostUsed = allTags.find(tag => tag.usage === maxUsage)
  return mostUsed ? mostUsed.name : '-'
})

const goBack = () => {
  router.back()
}

const addSoftTag = () => {
  ElMessage.info('添加软标签功能待实现')
}

const addHardTag = () => {
  ElMessage.info('添加硬标签功能待实现')
}

const removeSoftTag = (id: number) => {
  const index = softTags.value.findIndex(tag => tag.id === id)
  if (index > -1) {
    softTags.value.splice(index, 1)
    ElMessage.success('软标签已删除')
  }
}

const removeHardTag = (id: number) => {
  const index = hardTags.value.findIndex(tag => tag.id === id)
  if (index > -1) {
    hardTags.value.splice(index, 1)
    ElMessage.success('硬标签已删除')
  }
}
</script>

<style scoped>
.tag-management {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
}

.tag-card, .stats-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 100px;
  padding: 10px 0;
}

.tag-item {
  margin: 4px;
}

.stats-content {
  padding: 20px 0;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

@media (max-width: 768px) {
  .tag-management {
    padding: 15px;
  }
  
  .content .el-col {
    margin-bottom: 15px;
  }
}
</style>
