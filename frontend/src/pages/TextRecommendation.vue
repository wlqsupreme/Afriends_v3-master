<template>
  <div class="text-recommendation">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button @click="goBack" :icon="ArrowLeft" circle />
        <h1>文字推荐算法管理</h1>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="refreshData" :loading="loading">
          <el-icon><Refresh /></el-icon>
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
        
        <el-divider />
        
        <div class="formula-details">
          <h4>各维度得分计算：</h4>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="formula-item">
                <h5>软标签得分</h5>
                <p>基于用户软标签与内容标签的匹配度计算</p>
                <code>匹配度 = 匹配标签数 / max(用户标签数, 内容标签数)</code>
                <code>得分 = 匹配度 × 0.8 + min(匹配标签数 × 0.2, 0.4)</code>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="formula-item">
                <h5>硬标签得分</h5>
                <p>基于用户硬标签与内容硬标签的匹配度计算</p>
                <code>匹配度 = 匹配标签数 / max(用户标签数, 内容标签数)</code>
                <code>得分 = 匹配度 × 0.9 + 匹配标签数 × 0.1</code>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="formula-item">
                <h5>热度得分</h5>
                <p>基于内容互动数据计算热度</p>
                <code>互动值 = 点赞数 + 收藏数×2 + 评论数×3</code>
                <code>得分 = log10(互动值+1) / 3</code>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="formula-item">
                <h5>时间衰减得分</h5>
                <p>基于内容发布时间计算时效性</p>
                <code>1天内: 1.0 | 7天内: 0.8 | 30天内: 0.5 | 30天以上: 0.2</code>
              </div>
            </el-col>
          </el-row>
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
    <el-card v-if="!batchMode && recommendations.length > 0" class="recommendations-card">
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
          :key="item.textContentId"
          class="recommendation-item"
        >
          <div class="item-rank">
            <el-tag :type="getRankType(index + 1)" size="large">
              #{{ index + 1 }}
            </el-tag>
          </div>
          
          <div class="item-content">
            <div class="content-header">
              <h4>内容ID: {{ item.textContentId }}</h4>
              <div class="score-info">
                <el-tag type="info" size="small">
                  推荐分数: {{ item.score?.toFixed(2) || 'N/A' }}
                </el-tag>
                <el-tag type="success" size="small">
                  排名: {{ item.rankOrder }}
                </el-tag>
              </div>
            </div>
            
            <div class="content-text">
              <p>{{ item.contentText || '内容文本加载中...' }}</p>
            </div>
            
            <div class="content-stats">
              <div class="stat-item">
                <el-icon><Star /></el-icon>
                <span>{{ item.likeCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><Star /></el-icon>
                <span>{{ item.favoriteCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><ChatDotRound /></el-icon>
                <span>{{ item.commentCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><TrendCharts /></el-icon>
                <span>热度: {{ item.heatScore?.toFixed(2) || 'N/A' }}</span>
              </div>
            </div>
            
            <div class="content-tags">
              <div v-if="item.softTags" class="tags-row">
                <span class="tag-label">软标签:</span>
                <el-tag 
                  v-for="tag in item.softTags.split(',')" 
                  :key="tag"
                  :type="getMatchedTagType(tag.trim(), 'soft')"
                  size="small"
                  class="content-tag"
                >
                  {{ tag.trim() }}
                </el-tag>
              </div>
              
              <div v-if="item.hardTags" class="tags-row">
                <span class="tag-label">硬标签:</span>
                <el-tag 
                  v-for="tag in item.hardTags.split(',')" 
                  :key="tag"
                  :type="getMatchedTagType(tag.trim(), 'hard')"
                  size="small"
                  class="content-tag"
                >
                  {{ tag.trim() }}
                </el-tag>
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
              
              <!-- 标签匹配详情 -->
              <div class="tag-matching-details">
                <h6>标签匹配详情：</h6>
                <div v-if="item.softTags" class="matching-section">
                  <span class="matching-label">软标签匹配:</span>
                  <span class="matching-info">
                    用户标签: {{ userInfo?.softTags.map(t => t.content).join(', ') || '无' }}
                  </span>
                  <br>
                  <span class="matching-label">内容标签:</span>
                  <span class="matching-info">{{ item.softTags }}</span>
                  <br>
                  <span class="matching-label">匹配标签:</span>
                  <span class="matching-info">{{ getMatchedTags(item.softTags, 'soft').join(', ') || '无匹配' }}</span>
                </div>
                <div v-if="item.hardTags" class="matching-section">
                  <span class="matching-label">硬标签匹配:</span>
                  <span class="matching-info">
                    用户标签: {{ userInfo?.hardTags.map(t => t.content).join(', ') || '无' }}
                  </span>
                  <br>
                  <span class="matching-label">内容标签:</span>
                  <span class="matching-info">{{ item.hardTags }}</span>
                  <br>
                  <span class="matching-label">匹配标签:</span>
                  <span class="matching-info">{{ getMatchedTags(item.hardTags, 'hard').join(', ') || '无匹配' }}</span>
                </div>
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

    <!-- 主要内容 -->
    <div class="content">
      <!-- 算法状态卡片 -->
      <el-row :gutter="20" class="status-cards">
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon active">
                <el-icon><Check /></el-icon>
              </div>
              <div class="card-info">
                <h3>算法状态</h3>
                <p class="status-text active">运行中</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="card-info">
                <h3>活跃用户</h3>
                <p class="number">1,234</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon">
                <el-icon><Document /></el-icon>
              </div>
              <div class="card-info">
                <h3>推荐内容</h3>
                <p class="number">5,678</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="status-card">
            <div class="card-content">
              <div class="card-icon">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="card-info">
                <h3>点击率</h3>
                <p class="number">12.5%</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 功能操作区域 -->
      <el-card class="function-card">
        <template #header>
          <div class="card-header">
            <h3>算法控制</h3>
            <span class="card-desc">管理文字推荐算法的运行状态和参数</span>
          </div>
        </template>
        
        <div class="function-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="control-item">
                <h4>算法开关</h4>
                <el-switch 
                  v-model="algorithmEnabled" 
                  active-text="启用" 
                  inactive-text="禁用"
                  @change="handleAlgorithmToggle"
                />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="control-item">
                <h4>推荐数量</h4>
                <el-input-number 
                  v-model="recommendationCount" 
                  :min="1" 
                  :max="100" 
                  @change="handleCountChange"
                />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="control-item">
                <h4>更新频率</h4>
                <el-select v-model="updateFrequency" @change="handleFrequencyChange">
                  <el-option label="实时更新" value="realtime" />
                  <el-option label="每小时" value="hourly" />
                  <el-option label="每天" value="daily" />
                </el-select>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 参数配置区域 -->
      <el-card class="config-card">
        <template #header>
          <div class="card-header">
            <h3>权重参数配置</h3>
            <span class="card-desc">调整推荐算法各维度权重</span>
          </div>
        </template>
        
        <div class="config-content">
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
                <el-form-item label="时间衰减">
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
          </el-form>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ArrowLeft, Refresh, Check, User, Document, TrendCharts,
  Search, PriceTag, Star, ChatDotRound, Loading 
} from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const recommending = ref(false)
const errorMessage = ref('')

// 搜索表单
const searchForm = reactive({
  userId: '',
  limit: 10
})

// 批量推荐模式
const batchMode = ref(false)
const batchUserIds = ref('')
const batchRecommendations = ref<any[]>([])
const batchStats = ref<any>({})

// 用户信息
const userInfo = ref<{
  userId: string
  softTags: Array<{ id: number, content: string }>
  hardTags: Array<{ id: number, content: string }>
} | null>(null)

// 推荐结果
const recommendations = ref<Array<{
  textContentId: number
  contentText: string
  softTags: string
  hardTags: string
  likeCount: number
  favoriteCount: number
  commentCount: number
  heatScore: number
  score: number
  rankOrder: number
  softTagScore?: number
  hardTagScore?: number
  popularityScore?: number
  timeDecayScore?: number
}>>([])

// 状态数据
const algorithmEnabled = ref(true)
const recommendationCount = ref(20)
const updateFrequency = ref('hourly')
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
const API_BASE_URL = 'http://localhost:8888/api/text-recommendation'

// 返回上一页
const goBack = () => {
  router.back()
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
      const skippedCount = result.skippedCount || 0
      const totalCandidates = result.totalCandidates || 0
      
      if (skippedCount > 0) {
        ElMessage.success(`成功生成 ${recommendations.value.length} 条推荐，跳过已推荐的 ${skippedCount} 条内容`)
      } else {
        ElMessage.success(`成功生成 ${recommendations.value.length} 条推荐`)
      }
      
      // 显示推荐统计信息
      console.log(`推荐统计: 总候选内容 ${totalCandidates} 条，已推荐 ${skippedCount} 条，新推荐 ${recommendations.value.length} 条`)
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

// 批量生成推荐
const batchGenerateRecommendations = async () => {
  if (!batchUserIds.value.trim()) {
    ElMessage.warning('请输入用户ID列表')
    return
  }

  const userIds = batchUserIds.value.split(/[,\n\s]+/)
    .map(id => id.trim())
    .filter(id => id && !isNaN(Number(id)))
    .map(id => Number(id))

  if (userIds.length === 0) {
    ElMessage.warning('请输入有效的用户ID')
    return
  }

  recommending.value = true
  errorMessage.value = ''
  
  try {
    const requestData = {
      userIds: userIds,
      limit: searchForm.limit,
      softTagWeight: weightConfig.softTagWeight,
      hardTagWeight: weightConfig.hardTagWeight,
      popularityWeight: weightConfig.popularityWeight
    }
    
    const response = await axios.post(`${API_BASE_URL}/batch-generate`, requestData)
    const result = response.data
    
    if (result.success) {
      batchRecommendations.value = result.batchResults || []
      batchStats.value = {
        totalUsers: result.totalUsers,
        successCount: result.successCount,
        failCount: result.failCount
      }
      
      ElMessage.success(`批量推荐完成: 成功 ${result.successCount} 个用户，失败 ${result.failCount} 个用户`)
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
    recommending.value = false
  }
}

// 监听模式切换
watch(batchMode, (newValue) => {
  if (newValue) {
    // 切换到批量模式，清空单个用户的数据
    recommendations.value = []
    userInfo.value = null
  } else {
    // 切换到单个用户模式，清空批量数据
    batchRecommendations.value = []
    batchStats.value = {}
    batchUserIds.value = ''
  }
})

// 获取排名标签类型
const getRankType = (rank: number) => {
  if (rank <= 3) return 'success'
  if (rank <= 10) return 'warning'
  return 'info'
}

// 刷新数据
const refreshData = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  } finally {
    loading.value = false
  }
}

// 处理算法开关
const handleAlgorithmToggle = (value: boolean) => {
  ElMessage.success(value ? '算法已启用' : '算法已禁用')
}

// 处理推荐数量变化
const handleCountChange = (value: number) => {
  ElMessage.info(`推荐数量已更新为: ${value}`)
}

// 处理更新频率变化
const handleFrequencyChange = (value: string) => {
  ElMessage.info(`更新频率已设置为: ${value}`)
}

// 处理权重变化
const handleWeightChange = () => {
  ElMessage.info('权重参数已更新')
}

// 计算权重总和
const getTotalWeight = () => {
  return weightConfig.softTagWeight + weightConfig.hardTagWeight + 
         weightConfig.popularityWeight + weightConfig.timeDecayWeight
}

// 获取匹配标签的类型（用于高亮显示）
const getMatchedTagType = (tag: string, type: 'soft' | 'hard') => {
  if (!userInfo.value) return type === 'soft' ? 'success' : 'warning'
  
  const userTags = type === 'soft' ? userInfo.value.softTags : userInfo.value.hardTags
  const isMatched = userTags.some(userTag => userTag.content === tag.trim())
  
  if (isMatched) {
    return 'success'
  } else {
    return type === 'soft' ? 'info' : 'warning'
  }
}

// 获取匹配的标签列表
const getMatchedTags = (contentTags: string, type: 'soft' | 'hard') => {
  if (!userInfo.value || !contentTags) return []
  
  const userTags = type === 'soft' ? userInfo.value.softTags : userInfo.value.hardTags
  const contentTagList = contentTags.split(',').map(tag => tag.trim())
  
  return userTags
    .filter(userTag => contentTagList.includes(userTag.content))
    .map(userTag => userTag.content)
}
</script>

<style scoped>
.text-recommendation {
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

.status-cards {
  margin-bottom: 20px;
}

.status-card {
  height: 100%;
}

.card-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  color: #606266;
}

.card-icon.active {
  background: #e8f5e8;
  color: #67c23a;
}

.card-info h3 {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: #909399;
}

.number {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.status-text {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.status-text.active {
  color: #67c23a;
}

.input-card,
.weight-config-card,
.formula-card,
.user-info-card,
.recommendations-card,
.loading-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.input-form {
  padding: 10px 0;
}

.user-info {
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.label {
  font-weight: 600;
  margin-right: 10px;
  min-width: 100px;
}

.tags-section {
  margin-bottom: 20px;
}

.tags-section h4 {
  margin-bottom: 10px;
  color: #303133;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  margin: 2px;
}

.no-tags {
  text-align: center;
  padding: 40px 0;
}

.recommendations-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.recommendation-item {
  display: flex;
  gap: 15px;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: white;
  transition: all 0.3s ease;
}

.recommendation-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.item-rank {
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

.item-content {
  flex: 1;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.content-header h4 {
  margin: 0;
  color: #303133;
}

.score-info {
  display: flex;
  gap: 8px;
}

.content-text {
  margin-bottom: 15px;
}

.content-text p {
  margin: 0;
  line-height: 1.6;
  color: #606266;
}

.content-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 14px;
}

.content-tags {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tags-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tag-label {
  font-weight: 600;
  color: #606266;
  min-width: 60px;
}

.content-tag {
  margin: 1px;
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

/* 权重配置样式 */
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
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
}

/* 公式显示样式 */
.formula-content {
  padding: 20px 0;
}

.formula-main {
  margin-bottom: 20px;
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

.formula-details {
  margin-top: 20px;
}

.formula-item {
  background: #fafafa;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 15px;
  border: 1px solid #e4e7ed;
}

.formula-item h5 {
  margin: 0 0 8px 0;
  color: #409eff;
  font-size: 14px;
}

.formula-item p {
  margin: 0 0 8px 0;
  color: #606266;
  font-size: 13px;
}

.formula-item code {
  display: block;
  background: #f1f2f3;
  padding: 6px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #e6a23c;
  margin: 4px 0;
}

/* 得分详情样式 */
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

.score-components {
  margin-bottom: 20px;
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

/* 标签匹配详情样式 */
.tag-matching-details {
  margin-top: 15px;
}

.tag-matching-details h6 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 13px;
}

.matching-section {
  margin-bottom: 10px;
  padding: 10px;
  background: white;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.matching-label {
  font-size: 12px;
  color: #606266;
  font-weight: bold;
  display: inline-block;
  width: 80px;
}

.matching-info {
  font-size: 12px;
  color: #303133;
}

.function-card, .config-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.card-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
}

.card-desc {
  color: #909399;
  font-size: 14px;
}

.function-content {
  padding: 20px 0;
}

.control-item {
  text-align: center;
}

.control-item h4 {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 14px;
}

.config-content {
  padding: 20px 0;
}

@media (max-width: 768px) {
  .text-recommendation {
    padding: 15px;
  }
  
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .status-cards .el-col {
    margin-bottom: 15px;
  }
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
</style>
