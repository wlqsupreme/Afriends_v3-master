<template>
  <div class="recommendation-admin">
    <!-- 页面头部 -->
    <div class="admin-header">
      <h1>推荐算法管理后台</h1>
      <p class="admin-desc">管理和配置推荐算法参数，监控推荐效果</p>
    </div>

    <!-- 侧边栏按钮 -->
    <div class="sidebar-toggle" @click="toggleSidebar">
      <el-icon><Menu /></el-icon>
      <span>菜单</span>
    </div>

    <!-- 侧边栏组件 -->
    <Sidebar :visible="sidebarVisible" @close="closeSidebar" />

    <!-- 主要内容区域 -->
    <div class="admin-content">
      <!-- 欢迎卡片 -->
      <el-card class="welcome-card">
        <div class="welcome-content">
          <div class="welcome-icon">
            <el-icon size="48"><TrendCharts /></el-icon>
          </div>
          <div class="welcome-text">
            <h2>欢迎使用推荐算法管理后台</h2>
            <p>您可以在这里管理文字、图片、小说推荐算法，调整权重参数，查看推荐效果统计。</p>
          </div>
        </div>
      </el-card>

      <!-- 功能卡片网格 -->
      <div class="function-grid">
        <!-- 文字推荐 -->
        <el-card class="function-card" @click="goToTextRecommendation">
          <div class="card-content">
            <div class="card-icon text-icon">
              <span class="icon">📝</span>
            </div>
            <div class="card-info">
              <h3>文字推荐算法</h3>
              <p>管理文字内容的推荐策略和参数配置</p>
              <div class="card-stats">
                <span class="stat-item">
                  <el-icon><User /></el-icon>
                  用户标签匹配
                </span>
                <span class="stat-item">
                  <el-icon><Star /></el-icon>
                  内容热度计算
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 图片推荐 -->
        <el-card class="function-card" @click="goToImageRecommendation">
          <div class="card-content">
            <div class="card-icon image-icon">
              <span class="icon">🖼️</span>
            </div>
            <div class="card-info">
              <h3>图片推荐算法</h3>
              <p>管理图片内容的推荐策略和参数配置</p>
              <div class="card-stats">
                <span class="stat-item">
                  <el-icon><Picture /></el-icon>
                  图像特征分析
                </span>
                <span class="stat-item">
                  <el-icon><View /></el-icon>
                  视觉相似度
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 小说推荐 -->
        <el-card class="function-card" @click="goToNovelRecommendation">
          <div class="card-content">
            <div class="card-icon novel-icon">
              <span class="icon">📚</span>
            </div>
            <div class="card-info">
              <h3>小说推荐算法</h3>
              <p>管理小说内容的推荐策略和参数配置</p>
              <div class="card-stats">
                <span class="stat-item">
                  <el-icon><Reading /></el-icon>
                  文本语义分析
                </span>
                <span class="stat-item">
                  <el-icon><Collection /></el-icon>
                  阅读偏好学习
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 权重配置 -->
        <el-card class="function-card" @click="goToWeightConfig">
          <div class="card-content">
            <div class="card-icon config-icon">
              <span class="icon">⚙️</span>
            </div>
            <div class="card-info">
              <h3>权重配置</h3>
              <p>调整推荐算法各维度权重参数</p>
              <div class="card-stats">
                <span class="stat-item">
                  <el-icon><Setting /></el-icon>
                  软标签权重
                </span>
                <span class="stat-item">
                  <el-icon><Timer /></el-icon>
                  时间衰减因子
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 标签管理 -->
        <el-card class="function-card" @click="goToTagManagement">
          <div class="card-content">
            <div class="card-icon tag-icon">
              <span class="icon">🏷️</span>
            </div>
            <div class="card-info">
              <h3>标签管理</h3>
              <p>管理软硬标签体系和标签分类</p>
              <div class="card-stats">
                <span class="stat-item">
                  <el-icon><PriceTag /></el-icon>
                  软标签管理
                </span>
                <span class="stat-item">
                  <el-icon><Grid /></el-icon>
                  硬标签分类
                </span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 推荐统计 -->
        <el-card class="function-card" @click="goToRecommendationStats">
          <div class="card-content">
            <div class="card-icon stats-icon">
              <span class="icon">📊</span>
            </div>
            <div class="card-info">
              <h3>推荐统计</h3>
              <p>查看推荐效果数据和用户反馈统计</p>
              <div class="card-stats">
                <span class="stat-item">
                  <el-icon><DataAnalysis /></el-icon>
                  点击率分析
                </span>
                <span class="stat-item">
                  <el-icon><TrendCharts /></el-icon>
                  转化率统计
                </span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Menu, TrendCharts, User, Star, Picture, View, Reading, Collection, Setting, Timer, PriceTag, Grid, DataAnalysis } from '@element-plus/icons-vue'
import Sidebar from './Sidebar.vue'

const router = useRouter()
const sidebarVisible = ref(false)

// 切换侧边栏
const toggleSidebar = () => {
  sidebarVisible.value = true
}

// 关闭侧边栏
const closeSidebar = () => {
  sidebarVisible.value = false
}

// 导航方法
const goToTextRecommendation = () => {
  router.push('/text-recommendation')
}

const goToImageRecommendation = () => {
  router.push('/image-recommendation')
}

const goToNovelRecommendation = () => {
  router.push('/novel-recommendation')
}

const goToWeightConfig = () => {
  router.push('/weight-config')
}

const goToTagManagement = () => {
  router.push('/tag-management')
}

const goToRecommendationStats = () => {
  router.push('/recommendation-stats')
}
</script>

<style scoped>
.recommendation-admin {
  min-height: 100vh;
  background: #f5f7fa;
  position: relative;
}

/* 全局优化：启用硬件加速 */
* {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.function-card {
  backface-visibility: hidden;
  -webkit-backface-visibility: hidden;
}

.admin-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px 20px;
  text-align: center;
}

.admin-header h1 {
  margin: 0 0 10px 0;
  font-size: 32px;
  font-weight: 600;
}

.admin-desc {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.sidebar-toggle {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1000;
  background: white;
  border-radius: 8px;
  padding: 12px 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1),
              box-shadow 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform, box-shadow;
  transform: translateZ(0);
}

.sidebar-toggle:hover {
  transform: translateY(-1px) translateZ(0);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.sidebar-toggle:active {
  transform: translateY(0) translateZ(0);
}

.admin-content {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 30px;
}

.welcome-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.welcome-icon {
  color: #409eff;
}

.welcome-text h2 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
}

.welcome-text p {
  margin: 0;
  color: #606266;
  font-size: 16px;
  line-height: 1.5;
}

.function-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 20px;
  contain: layout style paint;
}

.function-card {
  cursor: pointer;
  transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1),
              box-shadow 0.2s cubic-bezier(0.4, 0, 0.2, 1),
              border-color 0.2s ease;
  border: 1px solid #e4e7ed;
  will-change: transform, box-shadow;
  transform: translateZ(0); /* 启用GPU加速 */
}

.function-card:hover {
  transform: translateY(-2px) translateZ(0);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
  border-color: #409eff;
}

.card-content {
  display: flex;
  gap: 16px;
  transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform;
}

.function-card:hover .card-content {
  transform: translateX(2px);
}

.function-card:active {
  transform: translateY(0) translateZ(0);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform;
}

.function-card:hover .card-icon {
  transform: scale(1.05);
}

.text-icon {
  background: linear-gradient(135deg, #4ECDC4, #44A08D);
}

.image-icon {
  background: linear-gradient(135deg, #FF69B4, #FF1493);
}

.novel-icon {
  background: linear-gradient(135deg, #9370DB, #8A2BE2);
}

.config-icon {
  background: linear-gradient(135deg, #FFD700, #FFA500);
}

.tag-icon {
  background: linear-gradient(135deg, #45B7D1, #96CEB4);
}

.stats-icon {
  background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
}

.icon {
  font-size: 28px;
}

.card-info h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
  transition: color 0.2s ease;
}

.function-card:hover .card-info h3 {
  color: #409eff;
}

.card-info p {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 14px;
  line-height: 1.4;
}

.card-stats {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .admin-header {
    padding: 30px 15px;
  }
  
  .admin-header h1 {
    font-size: 24px;
  }
  
  .admin-content {
    padding: 15px;
  }
  
  .function-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .welcome-content {
    flex-direction: column;
    text-align: center;
  }
  
  .sidebar-toggle {
    top: 15px;
    left: 15px;
    padding: 10px 12px;
  }
}
</style>
