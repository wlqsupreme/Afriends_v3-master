<template>
  <div class="recommendation-stats">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="goBack" :icon="ArrowLeft" circle />
        <h1>推荐统计</h1>
      </div>
      <div class="header-right">
        <el-button @click="refreshData" :loading="loading">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </div>

    <div class="content">
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon click-rate">
                <el-icon><Mouse /></el-icon>
              </div>
              <div class="stat-info">
                <h3>总体点击率</h3>
                <p class="stat-number">12.5%</p>
                <span class="stat-trend positive">↑ 2.3%</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon conversion-rate">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <h3>转化率</h3>
                <p class="stat-number">8.7%</p>
                <span class="stat-trend positive">↑ 1.1%</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon user-satisfaction">
                <el-icon><Star /></el-icon>
              </div>
              <div class="stat-info">
                <h3>用户满意度</h3>
                <p class="stat-number">4.2/5</p>
                <span class="stat-trend positive">↑ 0.3</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon algorithm-performance">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="stat-info">
                <h3>算法性能</h3>
                <p class="stat-number">95.8%</p>
                <span class="stat-trend negative">↓ 0.5%</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细统计 -->
      <el-card class="detail-stats-card">
        <template #header>
          <h3>详细统计信息</h3>
        </template>
        <div class="detail-stats">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="algorithm-stats">
                <h4>各算法表现</h4>
                <div class="algorithm-item">
                  <span class="algorithm-name">文字推荐</span>
                  <div class="algorithm-bar">
                    <div class="bar-fill" style="width: 85%"></div>
                  </div>
                  <span class="algorithm-score">85%</span>
                </div>
                <div class="algorithm-item">
                  <span class="algorithm-name">图片推荐</span>
                  <div class="algorithm-bar">
                    <div class="bar-fill" style="width: 78%"></div>
                  </div>
                  <span class="algorithm-score">78%</span>
                </div>
                <div class="algorithm-item">
                  <span class="algorithm-name">小说推荐</span>
                  <div class="algorithm-bar">
                    <div class="bar-fill" style="width: 92%"></div>
                  </div>
                  <span class="algorithm-score">92%</span>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="time-stats">
                <h4>时间趋势</h4>
                <div class="time-item">
                  <span class="time-label">今日推荐</span>
                  <span class="time-value">2,456</span>
                </div>
                <div class="time-item">
                  <span class="time-label">昨日推荐</span>
                  <span class="time-value">2,189</span>
                </div>
                <div class="time-item">
                  <span class="time-label">本周平均</span>
                  <span class="time-value">2,312</span>
                </div>
                <div class="time-item">
                  <span class="time-label">本月总计</span>
                  <span class="time-value">69,456</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Refresh, Mouse, TrendCharts, Star, DataAnalysis } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)

const goBack = () => {
  router.back()
}

const refreshData = async () => {
  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.recommendation-stats {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  height: 100%;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.click-rate {
  background: linear-gradient(135deg, #409eff, #67c23a);
  color: white;
}

.conversion-rate {
  background: linear-gradient(135deg, #e6a23c, #f56c6c);
  color: white;
}

.user-satisfaction {
  background: linear-gradient(135deg, #f56c6c, #e6a23c);
  color: white;
}

.algorithm-performance {
  background: linear-gradient(135deg, #909399, #606266);
  color: white;
}

.stat-info h3 {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #909399;
}

.stat-number {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.stat-trend {
  font-size: 12px;
  font-weight: 500;
}

.stat-trend.positive {
  color: #67c23a;
}

.stat-trend.negative {
  color: #f56c6c;
}

.detail-stats-card {
  margin-bottom: 20px;
}

.detail-stats {
  padding: 20px 0;
}

.algorithm-stats h4, .time-stats h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
}

.algorithm-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.algorithm-name {
  width: 80px;
  font-size: 14px;
  color: #606266;
}

.algorithm-bar {
  flex: 1;
  height: 8px;
  background: #f0f2f5;
  border-radius: 4px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.algorithm-score {
  width: 40px;
  text-align: right;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.time-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f2f5;
}

.time-item:last-child {
  border-bottom: none;
}

.time-label {
  font-size: 14px;
  color: #606266;
}

.time-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

@media (max-width: 768px) {
  .recommendation-stats {
    padding: 15px;
  }
  
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .stats-cards .el-col {
    margin-bottom: 15px;
  }
  
  .detail-stats .el-col {
    margin-bottom: 20px;
  }
}
</style>
