<template>
  <div class="weight-config">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="goBack" :icon="ArrowLeft" circle />
        <h1>权重配置管理</h1>
      </div>
    </div>

    <div class="content">
      <el-card class="config-card">
        <template #header>
          <h3>推荐算法权重配置</h3>
        </template>
        <div class="config-content">
          <el-form :model="weightConfig" label-width="150px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="软标签权重">
                  <el-slider 
                    v-model="weightConfig.softTagWeight" 
                    :min="0" 
                    :max="100" 
                    show-input 
                    @change="handleWeightChange"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="硬标签权重">
                  <el-slider 
                    v-model="weightConfig.hardTagWeight" 
                    :min="0" 
                    :max="100" 
                    show-input 
                    @change="handleWeightChange"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="热度权重">
                  <el-slider 
                    v-model="weightConfig.popularityWeight" 
                    :min="0" 
                    :max="100" 
                    show-input 
                    @change="handleWeightChange"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="时间衰减权重">
                  <el-slider 
                    v-model="weightConfig.timeDecayWeight" 
                    :min="0" 
                    :max="100" 
                    show-input 
                    @change="handleWeightChange"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="多标签匹配奖励">
                  <el-slider 
                    v-model="weightConfig.multiTagBonus" 
                    :min="0" 
                    :max="50" 
                    show-input 
                    @change="handleWeightChange"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="历史惩罚权重">
                  <el-slider 
                    v-model="weightConfig.historyPenalty" 
                    :min="0" 
                    :max="30" 
                    show-input 
                    @change="handleWeightChange"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-card>

      <el-card class="action-card">
        <template #header>
          <h3>操作</h3>
        </template>
        <div class="action-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-button type="primary" @click="saveConfig" :loading="saving">
                <el-icon><Check /></el-icon>
                保存配置
              </el-button>
            </el-col>
            <el-col :span="8">
              <el-button @click="resetConfig">
                <el-icon><Refresh /></el-icon>
                重置配置
              </el-button>
            </el-col>
            <el-col :span="8">
              <el-button type="success" @click="applyToAllAlgorithms">
                <el-icon><Setting /></el-icon>
                应用到所有算法
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Check, Refresh, Setting } from '@element-plus/icons-vue'

const router = useRouter()
const saving = ref(false)

const weightConfig = reactive({
  softTagWeight: 70,
  hardTagWeight: 25,
  popularityWeight: 10,
  timeDecayWeight: 10,
  multiTagBonus: 20,
  historyPenalty: 15
})

const goBack = () => {
  router.back()
}

const handleWeightChange = () => {
  // 权重变化处理
}

const saveConfig = async () => {
  saving.value = true
  try {
    // 模拟保存配置
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('配置保存成功')
  } catch (error) {
    ElMessage.error('配置保存失败')
  } finally {
    saving.value = false
  }
}

const resetConfig = () => {
  Object.assign(weightConfig, {
    softTagWeight: 70,
    hardTagWeight: 25,
    popularityWeight: 10,
    timeDecayWeight: 10,
    multiTagBonus: 20,
    historyPenalty: 15
  })
  ElMessage.info('配置已重置')
}

const applyToAllAlgorithms = () => {
  ElMessage.success('配置已应用到所有算法')
}
</script>

<style scoped>
.weight-config {
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

.config-card, .action-card {
  margin-bottom: 20px;
}

.config-content, .action-content {
  padding: 20px 0;
}

.action-content {
  text-align: center;
}

@media (max-width: 768px) {
  .weight-config {
    padding: 15px;
  }
  
  .action-content .el-col {
    margin-bottom: 15px;
  }
}
</style>
