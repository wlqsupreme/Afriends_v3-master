<template>
  <div class="novel-recommendation">
    <!-- 顶部标题栏 -->
    <div class="page-header">
      <div class="header-left">
        <el-button @click="goBack" :icon="ArrowLeft" circle />
        <h1>小说推荐算法管理</h1>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Refresh" @click="refreshData">
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 用户输入区域 -->
    <el-card class="input-card">
      <template #header>
        <div class="card-header">
          <el-icon><User /></el-icon>
          <span>用户信息查询</span>
          <el-switch
            v-model="batchMode"
            style="margin-left: 20px"
            active-text="批量模式"
            inactive-text="单个用户"
          />
        </div>
      </template>
      
      <div class="input-form">
        <el-form :model="searchForm" label-width="100px">
          <!-- 单个用户模式 -->
          <template v-if="!batchMode">
            <el-form-item label="用户ID">
              <el-input 
                v-model="searchForm.userId" 
                placeholder="请输入用户ID"
                style="width: 300px"
                clearable
              >
                <template #prepend>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </template>
          
          <!-- 批量模式 -->
          <template v-else>
            <el-form-item label="用户ID列表">
              <el-input
                v-model="batchUserIds"
                type="textarea"
                :rows="4"
                placeholder="请输入用户ID，支持多种分隔符：逗号、换行、空格&#10;例如：1,2,3 或 1&#10;2&#10;3"
                style="width: 400px"
              />
              <div class="input-tip">
                支持格式：1,2,3 或每行一个ID，最多支持50个用户
              </div>
            </el-form-item>
          </template>
          
          <el-form-item label="推荐数量">
            <el-input-number 
              v-model="searchForm.limit" 
              :min="1" 
              :max="50" 
              placeholder="推荐数量"
              style="width: 150px"
            />
          </el-form-item>
          
          <el-form-item>
            <!-- 单个用户模式按钮 -->
            <template v-if="!batchMode">
              <el-button 
                type="primary" 
                @click="searchUserInfo"
                :loading="loading"
                :disabled="!searchForm.userId"
              >
                <el-icon><Search /></el-icon>
                查询用户信息
              </el-button>
              
              <el-button 
                type="success" 
                @click="generateRecommendations"
                :loading="recommending"
                :disabled="!userInfo || !userInfo.softTags.length"
              >
                <el-icon><TrendCharts /></el-icon>
                生成推荐
              </el-button>
            </template>
            
            <!-- 批量模式按钮 -->
            <template v-else>
              <el-button 
                type="success" 
                @click="batchGenerateRecommendations"
                :loading="recommending"
                :disabled="!batchUserIds.trim()"
              >
                <el-icon><TrendCharts /></el-icon>
                批量生成推荐
              </el-button>
            </template>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 权重参数配置 -->
    <el-card class="weight-config-card">
      <template #header>
        <div class="card-header">
          <el-icon><Setting /></el-icon>
          <span>推荐算法权重配置</span>
          <el-button type="text" @click="showFormula = !showFormula">
            {{ showFormula ? '隐藏公式' : '显示公式' }}
          </el-button>
        </div>
      </template>
      
      <div class="weight-config">
        <el-form :model="weightConfig" label-width="120px">
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
                <div class="weight-desc">影响个性化匹配度</div>
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
                <div class="weight-desc">影响内容分类匹配</div>
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
                <div class="weight-desc">影响内容流行度</div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="时间衰减">
                <el-slider 
                  v-model="weightConfig.timeDecayWeight" 
                  :min="0" 
                  :max="100" 
                  show-input 
                  @change="handleWeightChange"
                />
                <div class="weight-desc">影响内容时效性</div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        
        <!-- 权重总和显示 -->
        <div class="weight-summary">
          <el-alert 
            :title="`当前权重总和: ${getTotalWeight()}%`"
            :type="getTotalWeight() === 100 ? 'success' : 'warning'"
            :closable="false"
            show-icon
          />
        </div>
      </div>
    </el-card>

    <!-- 推荐算法公式显示 -->
    <el-card v-if="showFormula" class="formula-card">
      <template #header>
        <div class="card-header">
          <el-icon><Document /></el-icon>
          <span>推荐算法公式</span>
        </div>
      </template>
      
      <div class="formula-content">
        <div class="formula-main">
          <h4>最终得分计算公式：</h4>
          <div class="formula">
            <span class="formula-text">
              最终得分 = 软标签得分 × {{ (weightConfig.softTagWeight / 100).toFixed(2) }} + 
              硬标签得分 × {{ (weightConfig.hardTagWeight / 100).toFixed(2) }} + 
              热度得分 × {{ (weightConfig.popularityWeight / 100).toFixed(2) }} + 
              时间衰减得分 × {{ (weightConfig.timeDecayWeight / 100).toFixed(2) }}
            </span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 用户标签信息 -->
    <el-card v-if="userInfo" class="user-info-card">
      <template #header>
        <div class="card-header">
          <el-icon><PriceTag /></el-icon>
          <span>用户标签信息</span>
        </div>
      </template>
      
      <div class="user-info">
        <div class="info-row">
          <span class="label">用户ID:</span>
          <el-tag type="primary">{{ userInfo.userId }}</el-tag>
        </div>
        
        <div class="info-row">
          <span class="label">软标签数量:</span>
          <el-tag type="success">{{ userInfo.softTags.length }}</el-tag>
        </div>
        
        <div class="info-row">
          <span class="label">硬标签数量:</span>
          <el-tag type="warning">{{ userInfo.hardTags.length }}</el-tag>
        </div>
      </div>
      
      <!-- 软标签展示 -->
      <div v-if="userInfo.softTags.length > 0" class="tags-section">
        <h4>软标签:</h4>
        <div class="tags-container">
          <el-tag 
            v-for="tag in userInfo.softTags" 
            :key="tag.id"
            type="success"
            class="tag-item"
          >
            {{ tag.content }}
          </el-tag>
        </div>
      </div>
      
      <!-- 硬标签展示 -->
      <div v-if="userInfo.hardTags.length > 0" class="tags-section">
        <h4>硬标签:</h4>
        <div class="tags-container">
          <el-tag 
            v-for="tag in userInfo.hardTags" 
            :key="tag.id"
            type="warning"
            class="tag-item"
          >
            {{ tag.content }}
          </el-tag>
        </div>
      </div>
      
      <!-- 无标签提示 -->
      <div v-if="userInfo.softTags.length === 0 && userInfo.hardTags.length === 0" class="no-tags">
        <el-empty description="该用户暂无标签信息">
          <el-button type="primary" @click="generateRecommendations">
            生成默认推荐
          </el-button>
        </el-empty>
      </div>
    </el-card>

    <!-- 批量推荐结果 -->
    <el-card v-if="batchMode && batchRecommendations.length > 0" class="batch-results-card">
      <template #header>
        <div class="card-header">
          <el-icon><TrendCharts /></el-icon>
          <span>批量推荐结果</span>
          <el-tag type="success" style="margin-left: 10px">
            成功: {{ batchStats.successCount || 0 }}
          </el-tag>
          <el-tag type="danger" style="margin-left: 5px">
            失败: {{ batchStats.failCount || 0 }}
          </el-tag>
        </div>
      </template>
      
      <div class="batch-results">
        <el-table :data="batchRecommendations" style="width: 100%">
          <el-table-column prop="userId" label="用户ID" width="100" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.success ? 'success' : 'danger'">
                {{ scope.row.success ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="推荐数量" width="120">
            <template #default="scope">
              {{ scope.row.count || 0 }}
            </template>
          </el-table-column>
          <el-table-column label="消息" min-width="200">
            <template #default="scope">
              {{ scope.row.message || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 推荐结果 -->
    <el-card v-if="recommendations.length > 0" class="recommendations-card">
      <template #header>
        <div class="card-header">
          <el-icon><TrendCharts /></el-icon>
          <span>推荐结果 ({{ recommendations.length }} 条)</span>
          <el-button type="text" @click="showScoreDetails = !showScoreDetails">
            {{ showScoreDetails ? '隐藏得分详情' : '显示得分详情' }}
          </el-button>
        </div>
      </template>
      
      <div class="recommendations-list">
        <div 
          v-for="(item, index) in recommendations" 
          :key="item.novelPostId" 
          class="recommendation-item"
        >
          <div class="item-header">
            <div class="rank-badge" :class="getRankClass(item.rankOrder)">
              #{{ item.rankOrder }}
            </div>
            <div class="item-id">
              <span class="label">小说ID:</span>
              <el-tag type="primary">{{ item.novelPostId }}</el-tag>
            </div>
            <div class="score-info">
              <span class="score-label">推荐得分:</span>
              <el-tag type="success" size="large">{{ item.score.toFixed(3) }}</el-tag>
            </div>
          </div>

          <!-- 得分详情 -->
          <div v-if="showScoreDetails" class="score-breakdown">
            <el-divider />
            <h5>得分详细计算：</h5>
            <div class="score-components">
              <div class="score-component">
                <span class="component-label">软标签得分:</span>
                <span class="component-value">{{ item.softTagScore?.toFixed(3) || '0.000' }}</span>
                <span class="component-weight">× {{ (weightConfig.softTagWeight / 100).toFixed(2) }}</span>
                <span class="component-result">= {{ ((item.softTagScore || 0) * weightConfig.softTagWeight / 100).toFixed(3) }}</span>
              </div>
              <div class="score-component">
                <span class="component-label">硬标签得分:</span>
                <span class="component-value">{{ item.hardTagScore?.toFixed(3) || '0.000' }}</span>
                <span class="component-weight">× {{ (weightConfig.hardTagWeight / 100).toFixed(2) }}</span>
                <span class="component-result">= {{ ((item.hardTagScore || 0) * weightConfig.hardTagWeight / 100).toFixed(3) }}</span>
              </div>
              <div class="score-component">
                <span class="component-label">热度得分:</span>
                <span class="component-value">{{ item.popularityScore?.toFixed(3) || '0.000' }}</span>
                <span class="component-weight">× {{ (weightConfig.popularityWeight / 100).toFixed(2) }}</span>
                <span class="component-result">= {{ ((item.popularityScore || 0) * weightConfig.popularityWeight / 100).toFixed(3) }}</span>
              </div>
              <div class="score-component">
                <span class="component-label">时间衰减得分:</span>
                <span class="component-value">{{ item.timeDecayScore?.toFixed(3) || '0.000' }}</span>
                <span class="component-weight">× {{ (weightConfig.timeDecayWeight / 100).toFixed(2) }}</span>
                <span class="component-result">= {{ ((item.timeDecayScore || 0) * weightConfig.timeDecayWeight / 100).toFixed(3) }}</span>
              </div>
              <div class="score-total">
                <span class="total-label">最终得分:</span>
                <span class="total-value">{{ item.score.toFixed(3) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 加载状态 -->
    <el-card v-if="loading || recommending" class="loading-card">
      <div class="loading-content">
        <el-icon class="loading-icon"><Loading /></el-icon>
        <p>{{ loading ? '正在查询用户信息...' : '正在生成推荐...' }}</p>
      </div>
    </el-card>

    <!-- 错误提示 -->
    <el-alert 
      v-if="errorMessage"
      :title="errorMessage"
      type="error"
      show-icon
      closable
      @close="errorMessage = ''"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ArrowLeft, Refresh, Search, PriceTag, TrendCharts, Document, Loading 
} from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const recommending = ref(false)
const batchRecommending = ref(false)
const errorMessage = ref('')
const batchMode = ref(false)

// 搜索表单
const searchForm = reactive({
  userId: '',
  limit: 10
})

// 批量表单
const batchForm = reactive({
  userIdsText: '',
  limit: 10
})

// 批量推荐结果
const batchResults = ref([])
const batchSummary = ref({
  totalUsers: 0,
  successCount: 0,
  failCount: 0,
  totalRecommendations: 0
})

// 用户信息
const userInfo = ref<{
  userId: string
  softTags: Array<{ id: number, content: string }>
  hardTags: Array<{ id: number, content: string }>
} | null>(null)

// 推荐结果
const recommendations = ref<Array<{
  novelPostId: number
  score: number
  rankOrder: number
  softTagScore?: number
  hardTagScore?: number
  popularityScore?: number
  timeDecayScore?: number
}>>([])

const showFormula = ref(false)
const showScoreDetails = ref(false)

// 权重配置
const weightConfig = reactive({
  softTagWeight: 40,
  hardTagWeight: 20,
  popularityWeight: 30,
  timeDecayWeight: 10
})

// API基础URL
const API_BASE_URL = 'http://localhost:8888/api/novel-recommendation'

// 返回上一页
const goBack = () => {
  router.back()
}

// 刷新数据
const refreshData = () => {
  if (userInfo.value) {
    generateRecommendations()
  }
}

// 查询用户信息
const searchUserInfo = async () => {
  if (!searchForm.userId) {
    ElMessage.warning('请输入用户ID')
    return
  }

  loading.value = true
  errorMessage.value = ''
  
  try {
    const response = await axios.get(`${API_BASE_URL}/user-tags/${searchForm.userId}`)
    const result = response.data
    
    if (result.success) {
      userInfo.value = {
        userId: result.userId.toString(),
        softTags: result.softTags || [],
        hardTags: result.hardTags || []
      }
      ElMessage.success('用户信息查询成功')
    } else {
      errorMessage.value = result.message || '查询用户信息失败'
      ElMessage.error('查询用户信息失败')
    }
  } catch (error: any) {
    const errorMsg = error.response?.data?.message || error.message || '查询用户信息失败'
    errorMessage.value = errorMsg
    ElMessage.error('查询用户信息失败')
    console.error('查询用户信息失败:', error)
  } finally {
    loading.value = false
  }
}

// 生成推荐
const generateRecommendations = async () => {
  if (!userInfo.value) {
    ElMessage.warning('请先查询用户信息')
    return
  }

  recommending.value = true
  errorMessage.value = ''
  
  try {
    const requestData = {
      userId: parseInt(userInfo.value.userId),
      limit: searchForm.limit,
      softTagWeight: weightConfig.softTagWeight,
      hardTagWeight: weightConfig.hardTagWeight,
      popularityWeight: weightConfig.popularityWeight
    }
    
    const response = await axios.post(`${API_BASE_URL}/generate`, requestData)
    const result = response.data
    
    if (result.success) {
      recommendations.value = result.recommendations || []
      ElMessage.success(`成功生成 ${recommendations.value.length} 条推荐`)
    } else {
      errorMessage.value = result.message || '生成推荐失败'
      ElMessage.error('生成推荐失败')
    }
  } catch (error: any) {
    const errorMsg = error.response?.data?.message || error.message || '生成推荐失败'
    errorMessage.value = errorMsg
    ElMessage.error('生成推荐失败')
    console.error('生成推荐失败:', error)
  } finally {
    recommending.value = false
  }
}

// 获取权重总和
const getTotalWeight = () => {
  return weightConfig.softTagWeight + 
         weightConfig.hardTagWeight + 
         weightConfig.popularityWeight + 
         weightConfig.timeDecayWeight
}

// 权重变化处理
const handleWeightChange = () => {
  console.log('权重已更新:', weightConfig)
}

// 获取排名样式
const getRankClass = (rank: number) => {
  if (rank <= 3) return 'top3'
  if (rank <= 10) return 'top10'
  return ''
}

// 切换批量模式
const toggleBatchMode = () => {
  batchMode.value = !batchMode.value
  if (batchMode.value) {
    // 清空单用户模式的数据
    userInfo.value = null
    recommendations.value = []
  } else {
    // 清空批量模式的数据
    batchResults.value = []
    batchForm.userIdsText = ''
  }
}

// 清空批量表单
const clearBatchForm = () => {
  batchForm.userIdsText = ''
  batchForm.limit = 10
  batchResults.value = []
}

// 批量生成推荐
const batchGenerateRecommendations = async () => {
  if (!batchForm.userIdsText.trim()) {
    ElMessage.warning('请输入用户ID列表')
    return
  }

  // 解析用户ID
  const userIds = batchForm.userIdsText
    .split('\n')
    .map(line => line.trim())
    .filter(line => line && !isNaN(Number(line)))
    .map(line => parseInt(line))

  if (userIds.length === 0) {
    ElMessage.warning('请输入有效的用户ID')
    return
  }

  if (userIds.length > 50) {
    ElMessage.warning('批量处理用户数量不能超过50个')
    return
  }

  batchRecommending.value = true
  errorMessage.value = ''
  
  try {
    const requestData = {
      userIds: userIds,
      limit: batchForm.limit,
      softTagWeight: weightConfig.softTagWeight,
      hardTagWeight: weightConfig.hardTagWeight,
      popularityWeight: weightConfig.popularityWeight
    }
    
    const response = await axios.post(`${API_BASE_URL}/batch-generate`, requestData)
    const result = response.data
    
    if (result.success) {
      batchResults.value = result.batchResults || []
      batchSummary.value = {
        totalUsers: result.totalUsers || 0,
        successCount: result.successCount || 0,
        failCount: result.failCount || 0,
        totalRecommendations: result.totalRecommendations || 0
      }
      
      ElMessage.success(result.message || '批量推荐生成成功')
    } else {
      errorMessage.value = result.message || '批量生成推荐失败'
      ElMessage.error('批量生成推荐失败')
    }
  } catch (error: any) {
    const errorMsg = error.response?.data?.message || error.message || '批量生成推荐失败'
    errorMessage.value = errorMsg
    ElMessage.error('批量生成推荐失败')
    console.error('批量生成推荐失败:', error)
  } finally {
    batchRecommending.value = false
  }
}
</script>

<style scoped>
.novel-recommendation {
  padding: 20px;
  min-height: 100vh;
  background: #f5f7fa;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-left h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.input-card,
.weight-config-card,
.formula-card,
.user-info-card,
.recommendations-card,
.batch-results-card,
.loading-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.input-form {
  padding: 10px 0;
}

.input-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.batch-results-card {
  margin-bottom: 20px;
}

.batch-results {
  max-height: 400px;
  overflow-y: auto;
}

.weight-config {
  padding: 20px 0;
}

.weight-desc {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.weight-summary {
  margin-top: 20px;
}

.formula-content {
  padding: 20px 0;
}

.formula {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid #409eff;
  margin: 10px 0;
}

.formula-text {
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #303133;
}

.user-info {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.label {
  font-weight: 600;
  color: #606266;
}

.tags-section {
  margin-top: 20px;
}

.tags-section h4 {
  margin-bottom: 10px;
  color: #606266;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.recommendation-item {
  padding: 20px;
  background: white;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  margin-bottom: 15px;
  transition: all 0.3s;
}

.recommendation-item:hover {
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.item-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.rank-badge {
  font-size: 20px;
  font-weight: bold;
  padding: 8px 12px;
  border-radius: 8px;
  background: #f0f2f5;
  color: #606266;
}

.rank-badge.top3 {
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  color: #fff;
  box-shadow: 0 2px 8px rgba(255, 215, 0, 0.3);
}

.rank-badge.top10 {
  background: #e8f5e9;
  color: #67c23a;
}

.score-info {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}

.score-breakdown {
  margin-top: 15px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

.score-breakdown h5 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 14px;
}

.score-component {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  padding: 8px 12px;
  background: white;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.component-label {
  flex: 0 0 100px;
  font-size: 13px;
  color: #606266;
}

.component-value {
  flex: 0 0 60px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  color: #409eff;
  font-weight: bold;
}

.component-weight {
  flex: 0 0 80px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  color: #909399;
}

.component-result {
  flex: 0 0 60px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  color: #67c23a;
  font-weight: bold;
}

.score-total {
  display: flex;
  align-items: center;
  margin-top: 10px;
  padding: 12px;
  background: #e8f5e8;
  border-radius: 4px;
  border: 2px solid #67c23a;
}

.total-label {
  flex: 0 0 100px;
  font-size: 14px;
  color: #67c23a;
  font-weight: bold;
}

.total-value {
  font-family: 'Courier New', monospace;
  font-size: 16px;
  color: #67c23a;
  font-weight: bold;
}

.loading-content {
  text-align: center;
  padding: 40px 0;
}

.loading-icon {
  font-size: 32px;
  color: #409eff;
  animation: rotate 2s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 批量推荐样式 */
.batch-form {
  padding: 20px 0;
}

.form-help {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.batch-results-card {
  margin-bottom: 20px;
}

.batch-summary {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.stat-card.success {
  background: #f0f9ff;
  border-color: #67c23a;
}

.stat-card.danger {
  background: #fef0f0;
  border-color: #f56c6c;
}

.stat-card.primary {
  background: #ecf5ff;
  border-color: #409eff;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.batch-details {
  margin-top: 20px;
}
</style>
