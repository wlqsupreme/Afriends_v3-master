<template>
  <div class="home">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用 Afriends 管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="feature-card" @click="goToTableSelector">
              <div class="feature-item">
                <el-icon size="48" color="#409eff">
                  <DataBoard />
                </el-icon>
                <h3>数据管理</h3>
                <p>高效管理好友列表数据</p>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="feature-card">
              <div class="feature-item">
                <el-icon size="48" color="#67c23a">
                  <Search />
                </el-icon>
                <h3>智能搜索</h3>
                <p>快速查找和筛选数据</p>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="feature-card">
              <div class="feature-item">
                <el-icon size="48" color="#e6a23c">
                  <Setting />
                </el-icon>
                <h3>系统配置</h3>
                <p>灵活配置系统参数</p>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="feature-card" @click="goToRecommendationAdmin">
              <div class="feature-item">
                <el-icon size="48" color="#f56c6c">
                  <TrendCharts />
                </el-icon>
                <h3>推荐算法管理</h3>
                <p>管理和配置推荐算法</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <el-card class="stats-card">
      <template #header>
        <div class="card-header">
          <span>系统统计</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ stats.totalFriends }}</div>
            <div class="stat-label">总好友数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ stats.onlineFriends }}</div>
            <div class="stat-label">在线好友</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ stats.totalEntityClasses }}</div>
            <div class="stat-label">实体类数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ stats.totalListClasses }}</div>
            <div class="stat-label">List类数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ userStats.totalUsers || 0 }}</div>
            <div class="stat-label">内存用户数</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 实体类概览 -->
    <el-card class="entity-overview-card">
      <template #header>
        <div class="card-header">
          <span>实体类概览</span>
          <div class="header-buttons">
            <el-button type="primary" @click="loadEntityData" :loading="entityLoading">
              刷新实体类信息
            </el-button>
            <el-button type="warning" @click="viewMemoryData" :loading="userLoading">
              查看内存数据
            </el-button>
            <el-button type="info" @click="testDatabaseConnection" :loading="userLoading">
              测试数据库
            </el-button>
          </div>
        </div>
      </template>
      
      <div v-if="entityLoading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else class="entity-overview">
        <!-- List类数据加载器 -->
        <el-card class="list-loader-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>List类数据加载器</span>
              <el-button 
                type="primary" 
                @click="loadSelectedListData" 
                :loading="listDataLoading"
                :disabled="selectedListClasses.length === 0"
              >
                加载选中数据到内存
              </el-button>
            </div>
          </template>
          
          <div class="list-selector">
            <div class="selector-header">
              <el-checkbox 
                v-model="selectAllLists" 
                :indeterminate="isIndeterminate"
                @change="handleSelectAllLists"
              >
                全选 ({{ selectedListClasses.length }}/{{ listClasses.length }})
              </el-checkbox>
              <el-button type="text" @click="clearSelection">清空选择</el-button>
            </div>
            
            <div class="list-checkbox-group">
              <el-checkbox-group v-model="selectedListClasses" @change="handleListSelectionChange">
                <el-checkbox 
                  v-for="listClass in sortedListClasses" 
                  :key="listClass.className"
                  :label="listClass.className"
                  class="list-checkbox-item"
                >
                  <div class="list-item">
                    <span class="list-name">{{ listClass.className }}</span>
                    <el-tag size="small" type="info">{{ listClass.fullName }}</el-tag>
                  </div>
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </div>
        </el-card>

        <el-row :gutter="20">
          <el-col :span="8">
            <div class="entity-section">
              <h4>_njj实体类 ({{ njjEntityList.length }})</h4>
              <div class="entity-list">
                <el-tag 
                  v-for="entity in njjEntityList.slice(0, 8)" 
                  :key="entity.className"
                  class="entity-tag"
                  @click="viewEntityDetail(entity)"
                >
                  {{ entity.className }}
                </el-tag>
                <el-tag v-if="njjEntityList.length > 8" type="info">
                  +{{ njjEntityList.length - 8 }} 更多...
                </el-tag>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="entity-section">
              <h4>_wlq实体类 ({{ wlqEntityList.length }})</h4>
              <div class="entity-list">
                <el-tag 
                  v-for="entity in wlqEntityList.slice(0, 8)" 
                  :key="entity.className"
                  class="entity-tag"
                  type="warning"
                  @click="viewEntityDetail(entity)"
                >
                  {{ entity.className }}
                </el-tag>
                <el-tag v-if="wlqEntityList.length > 8" type="info">
                  +{{ wlqEntityList.length - 8 }} 更多...
                </el-tag>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="entity-section">
              <h4>List类列表 ({{ listClasses.length }})</h4>
              <div class="entity-list">
                <el-tag 
                  v-for="listClass in listClasses.slice(0, 8)" 
                  :key="listClass.className"
                  class="entity-tag"
                  type="success"
                  @click="viewListDetail(listClass)"
                >
                  {{ listClass.className }}
                </el-tag>
                <el-tag v-if="listClasses.length > 8" type="info">
                  +{{ listClasses.length - 8 }} 更多...
                </el-tag>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 内存数据查看对话框 -->
    <el-dialog
      v-model="memoryDataVisible"
      title="内存数据查看"
      width="90%"
      :before-close="handleMemoryDataClose"
    >
      <div v-if="userLoading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <div v-else>
        <!-- 数据源选择器 - 卡片式布局 -->
        <el-card class="data-source-selector">
          <template #header>
            <div class="card-header">
              <span>选择要查看的数据源</span>
              <el-button type="primary" @click="loadSelectedDataSource" :loading="userLoading" :disabled="!selectedDataSource">
                加载数据
              </el-button>
            </div>
          </template>
          
          <!-- 数据源网格 -->
          <div class="data-source-grid">
            <div 
              v-for="dataSource in sortedDataSources" 
              :key="dataSource.value"
              class="data-source-card"
              :class="{ 'selected': selectedDataSource === dataSource.value }"
              @click="selectDataSource(dataSource.value)"
            >
              <div class="data-source-info">
                <h4>{{ dataSource.label.split(' (')[0] }}</h4>
                <p>{{ dataSource.value }}</p>
                <div class="data-source-count" v-if="dataSource.recordCount !== undefined">
                  记录数: {{ dataSource.recordCount.toLocaleString() }}
                </div>
              </div>
              <div class="data-source-status">
                <el-icon v-if="selectedDataSource === dataSource.value" color="#409eff">
                  <Check />
                </el-icon>
              </div>
            </div>
          </div>
          
          <!-- 操作按钮 -->
          <div class="data-source-actions">
            <el-button type="success" @click="loadAllDataWithDetails" :loading="userLoading">
              <el-icon><Refresh /></el-icon>
              加载所有数据
            </el-button>
            <el-button @click="refreshMemoryData" :disabled="!selectedDataSource">
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
            <el-button @click="clearDataSource">
              <el-icon><Close /></el-icon>
              清空选择
            </el-button>
          </div>
        </el-card>

        <!-- 统计信息 -->
        <el-card class="memory-stats-card" v-if="currentMemoryStats">
          <template #header>
            <span>{{ selectedDataSourceLabel }} - 内存统计信息</span>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ currentMemoryStats.totalCount || 0 }}</div>
                <div class="stat-label">总记录数</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ currentMemoryStats.cacheLoaded ? '是' : '否' }}</div>
                <div class="stat-label">缓存已加载</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ formatTime(currentMemoryStats.lastUpdateTime) }}</div>
                <div class="stat-label">最后更新时间</div>
              </div>
            </el-col>
            <el-col :span="6" v-if="currentMemoryStats.completedCount !== undefined">
              <div class="stat-item">
                <div class="stat-number">{{ currentMemoryStats.completedCount || 0 }}</div>
                <div class="stat-label">已完成数</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 数据表格 -->
        <el-card class="memory-data-card" v-if="currentMemoryData.length > 0">
          <template #header>
            <div class="card-header">
              <span>{{ selectedDataSourceLabel }} - 数据列表 ({{ currentMemoryData.length }} 条)</span>
            </div>
          </template>
          
          <el-table 
            :data="currentMemoryData" 
            v-loading="userLoading"
            stripe
            border
            height="500"
            :default-sort="{ prop: getFirstColumnProp(), order: 'ascending' }"
          >
            <el-table-column 
              v-for="column in currentTableColumns" 
              :key="column.prop"
              :prop="column.prop" 
              :label="column.label" 
              :width="column.width"
              :sortable="column.sortable"
            >
              <template #default="{ row }" v-if="column.formatter">
                <el-tag :type="column.formatter(row[column.prop])">
                  {{ formatColumnValue(row[column.prop], column.type) }}
                </el-tag>
              </template>
              <template #default="{ row }" v-else-if="column.type === 'date'">
                {{ formatDate(row[column.prop]) }}
              </template>
              <template #default="{ row }" v-else>
                {{ formatColumnValue(row[column.prop], column.type) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 无数据提示 -->
        <el-card v-else-if="selectedDataSource && !userLoading">
          <el-empty description="暂无数据" />
        </el-card>
      </div>
      
      <template #footer>
        <el-button @click="memoryDataVisible = false">关闭</el-button>
        <el-button type="primary" @click="exportMemoryData" :disabled="!currentMemoryData.length">导出数据</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { DataBoard, Search, Setting, Check, Refresh, Close, TrendCharts } from '@element-plus/icons-vue'
import { useTableStore } from '@/store/table'
import { ElMessage } from 'element-plus'

const router = useRouter()
const tableStore = useTableStore()

const stats = ref({
  totalFriends: 0,
  onlineFriends: 0,
  totalEntityClasses: 0,
  totalListClasses: 0
})

const userStats = ref({
  totalUsers: 0,
  cacheLoaded: false,
  lastUpdateTime: null
})

const userLoading = ref(false)
const memoryDataVisible = ref(false)

// List类选择相关
const selectedListClasses = ref<string[]>([])
const selectAllLists = ref(false)
const isIndeterminate = ref(false)
const listDataLoading = ref(false)

// 内存数据查看相关
const selectedDataSource = ref('')
const currentMemoryData = ref<any[]>([])
const currentMemoryStats = ref<any>(null)
const currentTableColumns = ref<any[]>([])

// 可用的数据源 - 包含所有98个List类（47个_njj + 51个_wlq）
const availableDataSources = ref([
  { value: 'UserInfo_list_njj', label: '用户信息 (UserInfo_list_njj)', recordCount: 0 },
  { value: 'AchievementBase_list_njj', label: '成就基础信息 (AchievementBase_list_njj)', recordCount: 0 },
  { value: 'AchievementRecord_list_njj', label: '成就记录信息 (AchievementRecord_list_njj)', recordCount: 0 },
  { value: 'AgreementBase_list_njj', label: '协议基础信息 (AgreementBase_list_njj)', recordCount: 0 },
  { value: 'AimodelBaseInfo_list_njj', label: 'AI模型基础信息 (AimodelBaseInfo_list_njj)', recordCount: 0 },
  { value: 'AimodelCoinLog_list_njj', label: 'AI模型币日志 (AimodelCoinLog_list_njj)', recordCount: 0 },
  { value: 'AimodelLevelRule_list_njj', label: 'AI模型等级规则 (AimodelLevelRule_list_njj)', recordCount: 0 },
  { value: 'AiTaskLog_list_njj', label: 'AI任务日志 (AiTaskLog_list_njj)', recordCount: 0 },
  { value: 'BlockRecord_list_njj', label: '屏蔽记录 (BlockRecord_list_njj)', recordCount: 0 },
  { value: 'CertBase_list_njj', label: '证书基础信息 (CertBase_list_njj)', recordCount: 0 },
  { value: 'CertJobBase_list_njj', label: '证书工作基础信息 (CertJobBase_list_njj)', recordCount: 0 },
  { value: 'CertRealnameBase_list_njj', label: '实名认证基础信息 (CertRealnameBase_list_njj)', recordCount: 0 },
  { value: 'CertStudentBase_list_njj', label: '学生证认证基础信息 (CertStudentBase_list_njj)', recordCount: 0 },
  { value: 'ChatSettingsBase_list_njj', label: '聊天设置基础信息 (ChatSettingsBase_list_njj)', recordCount: 0 },
  { value: 'EmojiBase_list_njj', label: '表情基础信息 (EmojiBase_list_njj)', recordCount: 0 },
  { value: 'FriendsProfile_list_njj', label: '好友档案信息 (FriendsProfile_list_njj)', recordCount: 0 },
  { value: 'MessageExtraBase_list_njj', label: '消息扩展基础信息 (MessageExtraBase_list_njj)', recordCount: 0 },
  { value: 'ProductsBase_list_njj', label: '产品基础信息 (ProductsBase_list_njj)', recordCount: 0 },
  { value: 'PurchaseRecord_list_njj', label: '购买记录 (PurchaseRecord_list_njj)', recordCount: 0 },
  { value: 'ReportCategory_list_njj', label: '举报类别 (ReportCategory_list_njj)', recordCount: 0 },
  { value: 'ReportRecord_list_njj', label: '举报记录 (ReportRecord_list_njj)', recordCount: 0 },
  { value: 'TaskComment_list_njj', label: '任务评论 (TaskComment_list_njj)', recordCount: 0 },
  { value: 'UserAiComment_list_njj', label: '用户AI评论 (UserAiComment_list_njj)', recordCount: 0 },
  { value: 'UserAiModel_list_njj', label: '用户AI模型 (UserAiModel_list_njj)', recordCount: 0 },
  { value: 'UserBaseDynamic_list_njj', label: '用户动态基础信息 (v2_user_base_dynamic @heng)', recordCount: 0 },
  { value: 'UserBaseImagepostBase_list_njj', label: '用户基础图片帖子 (v2_user_base_imagepost_base @heng)', recordCount: 0 },
  { value: 'UserBaseLikeAction_list_njj', label: '用户基础点赞行为 (v2_user_base_like_action @heng)', recordCount: 0 },
  { value: 'UserBasePicComment_list_njj', label: '用户基础图片评论 (v2_user_base_pic_comment @heng)', recordCount: 0 },
  { value: 'UserBaseSystemMessage_list_njj', label: '用户基础系统消息 (v2_user_base_system_message @heng)', recordCount: 0 },
  { value: 'UserBaseTextComment_list_njj', label: '用户基础文本评论 (v2_user_base_text_comment @heng)', recordCount: 0 },
  { value: 'UserBaseUserCollectioin_list_njj', label: '用户基础用户收藏 (v2_user_base_user_collection @heng)', recordCount: 0 },
  { value: 'UserCertRecord_list_njj', label: '用户认证记录 (UserCertRecord_list_njj)', recordCount: 0 },
  { value: 'UserChatDetail_list_njj', label: '用户聊天详情 (UserChatDetail_list_njj)', recordCount: 0 },
  { value: 'UserChatList_list_njj', label: '用户聊天列表 (UserChatList_list_njj)', recordCount: 0 },
  { value: 'UserContentViewLog_list_njj', label: '用户内容查看日志 (UserContentViewLog_list_njj)', recordCount: 0 },
  { value: 'UserDevice_list_njj', label: '用户设备 (UserDevice_list_njj)', recordCount: 0 },
  { value: 'UserDislikeRelation_list_njj', label: '用户不喜欢关系 (UserDislikeRelation_list_njj)', recordCount: 0 },
  { value: 'UserFriendsRelationship_list_njj', label: '用户好友关系 (UserFriendsRelationship_list_njj)', recordCount: 0 },
  { value: 'UserHardTagRelation_list_njj', label: '用户硬标签关系 (UserHardTagRelation_list_njj)', recordCount: 0 },
  { value: 'UserImageRecommendation_list_njj', label: '用户图片推荐 (UserImageRecommendation_list_njj)', recordCount: 0 },
  { value: 'UserInfoFeatureVector_list_njj', label: '用户信息特征向量 (UserInfoFeatureVector_list_njj)', recordCount: 0 },
  { value: 'UserInfoQuestion_list_njj', label: '用户信息问题 (UserInfoQuestion_list_njj)', recordCount: 0 },
  { value: 'UserLikeRelation_list_njj', label: '用户喜欢关系 (UserLikeRelation_list_njj)', recordCount: 0 },
  { value: 'UserNovelRecommendation_list_njj', label: '用户小说推荐 (UserNovelRecommendation_list_njj)', recordCount: 0 },
  { value: 'UserNovelRelation_list_njj', label: '用户小说关系 (UserNovelRelation_list_njj)', recordCount: 0 },
  { value: 'UserReviewBase_list_njj', label: '用户评论基础信息 (UserReviewBase_list_njj)', recordCount: 0 },
  { value: 'UserSettingRelation_list_njj', label: '用户设置关系 (UserSettingRelation_list_njj)', recordCount: 0 },
  { value: 'UserSoftTagRelation_list_njj', label: '用户软标签关系 (UserSoftTagRelation_list_njj)', recordCount: 0 },
  { value: 'UserSystemMessage_list_njj', label: '用户系统消息 (UserSystemMessage_list_njj)', recordCount: 0 },
  { value: 'UserTaskRelationship_list_njj', label: '用户任务关系 (UserTaskRelationship_list_njj)', recordCount: 0 },
  { value: 'UserTextRecommendation_list_njj', label: '用户文本推荐 (UserTextRecommendation_list_njj)', recordCount: 0 },
  
  // _wlq数据源
  { value: 'ActionExp_list_wlq', label: '行为经验 (ActionExp_list_wlq)', recordCount: 0 },
  { value: 'AiChatListDetailR_list_wlq', label: 'AI聊天列表详情 (AiChatListDetailR_list_wlq)', recordCount: 0 },
  { value: 'AiMatches_list_wlq', label: 'AI匹配 (AiMatches_list_wlq)', recordCount: 0 },
  { value: 'AiTaskRequire_list_wlq', label: 'AI任务需求 (AiTaskRequire_list_wlq)', recordCount: 0 },
  { value: 'AiTaskRespond_list_wlq', label: 'AI任务响应 (AiTaskRespond_list_wlq)', recordCount: 0 },
  { value: 'CommentLikeRelation_list_wlq', label: '评论点赞关系 (CommentLikeRelation_list_wlq)', recordCount: 0 },
  { value: 'ContentDislikeRelation_list_wlq', label: '内容不喜欢关系 (ContentDislikeRelation_list_wlq)', recordCount: 0 },
  { value: 'ContentFavouriteRelation_list_wlq', label: '内容收藏关系 (ContentFavouriteRelation_list_wlq)', recordCount: 0 },
  { value: 'ContentFeedbackRelation_list_wlq', label: '内容反馈关系 (ContentFeedbackRelation_list_wlq)', recordCount: 0 },
  { value: 'ContentLikeRelation_list_wlq', label: '内容点赞关系 (ContentLikeRelation_list_wlq)', recordCount: 0 },
  { value: 'FriendList_list_wlq', label: '好友列表 (FriendList_list_wlq)', recordCount: 0 },
  { value: 'GiftBase_list_wlq', label: '礼物基础 (GiftBase_list_wlq)', recordCount: 0 },
  { value: 'HardTagBase_list_wlq', label: '硬标签基础 (HardTagBase_list_wlq)', recordCount: 0 },
  { value: 'IDislike_list_wlq', label: '我不喜欢 (IDislike_list_wlq)', recordCount: 0 },
  { value: 'IDislikeComment_list_wlq', label: '我不喜欢评论 (IDislikeComment_list_wlq)', recordCount: 0 },
  { value: 'IDislikeFeatureVector_list_wlq', label: '我不喜欢特征向量 (IDislikeFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'IHave_list_wlq', label: '我拥有 (IHave_list_wlq)', recordCount: 0 },
  { value: 'IHaveComment_list_wlq', label: '我拥有评论 (IHaveComment_list_wlq)', recordCount: 0 },
  { value: 'IHaveFeatureVector_list_wlq', label: '我拥有特征向量 (IHaveFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'ILike_list_wlq', label: '我喜欢 (ILike_list_wlq)', recordCount: 0 },
  { value: 'ILikeComment_list_wlq', label: '我喜欢评论 (ILikeComment_list_wlq)', recordCount: 0 },
  { value: 'ILikeFeatureVector_list_wlq', label: '我喜欢特征向量 (ILikeFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'ImageContentBase_list_wlq', label: '图片内容基础 (ImageContentBase_list_wlq)', recordCount: 0 },
  { value: 'ImagePostBase_list_wlq', label: '图片帖子基础 (ImagePostBase_list_wlq)', recordCount: 0 },
  { value: 'ImagePostComment_list_wlq', label: '图片帖子评论 (ImagePostComment_list_wlq)', recordCount: 0 },
  { value: 'ImagePostFeatureVector_list_wlq', label: '图片帖子特征向量 (ImagePostFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'INeed_list_wlq', label: '我需要 (INeed_list_wlq)', recordCount: 0 },
  { value: 'INeedComment_list_wlq', label: '我需要评论 (INeedComment_list_wlq)', recordCount: 0 },
  { value: 'INeedFeatureVector_list_wlq', label: '我需要特征向量 (INeedFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'IntiniallabelChen_list_wlq', label: '初始标签陈 (IntiniallabelChen_list_wlq)', recordCount: 0 },
  { value: 'LikeRobotActionLog_list_wlq', label: '点赞机器人行为日志 (LikeRobotActionLog_list_wlq)', recordCount: 0 },
  { value: 'NovelChapterInfo_list_wlq', label: '小说章节信息 (NovelChapterInfo_list_wlq)', recordCount: 0 },
  { value: 'NovelContentBase_list_wlq', label: '小说内容基础 (NovelContentBase_list_wlq)', recordCount: 0 },
  { value: 'NovelpostBase_list_wlq', label: '小说帖子基础 (NovelpostBase_list_wlq)', recordCount: 0 },
  { value: 'NovelpostComment_list_wlq', label: '小说帖子评论 (NovelpostComment_list_wlq)', recordCount: 0 },
  { value: 'NovelpostFeatureVector_list_wlq', label: '小说帖子特征向量 (NovelpostFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'Recharge_list_wlq', label: '充值 (Recharge_list_wlq)', recordCount: 0 },
  { value: 'RecommendedAvatar_list_wlq', label: '推荐头像 (RecommendedAvatar_list_wlq)', recordCount: 0 },
  { value: 'RecommendedLabel_list_wlq', label: '推荐标签 (RecommendedLabel_list_wlq)', recordCount: 0 },
  { value: 'RecommendedNickname_list_wlq', label: '推荐昵称 (RecommendedNickname_list_wlq)', recordCount: 0 },
  { value: 'SettingBase_list_wlq', label: '设置基础 (SettingBase_list_wlq)', recordCount: 0 },
  { value: 'SoftTagBase_list_wlq', label: '软标签基础 (SoftTagBase_list_wlq)', recordCount: 0 },
  { value: 'SoftTagCategory_list_wlq', label: '软标签类别 (SoftTagCategory_list_wlq)', recordCount: 0 },
  { value: 'TextpostBase_list_wlq', label: '文本帖子基础 (TextpostBase_list_wlq)', recordCount: 0 },
  { value: 'TextpostComment_list_wlq', label: '文本帖子评论 (TextpostComment_list_wlq)', recordCount: 0 },
  { value: 'TextpostFeatureVector_list_wlq', label: '文本帖子特征向量 (TextpostFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'UserAiRequire_list_wlq', label: '用户AI需求 (UserAiRequire_list_wlq)', recordCount: 0 },
  { value: 'UserAiRequireFeatureVector_list_wlq', label: '用户AI需求特征向量 (UserAiRequireFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'UserAiRespond_list_wlq', label: '用户AI响应 (UserAiRespond_list_wlq)', recordCount: 0 },
  { value: 'UserAiRespondFeatureVector_list_wlq', label: '用户AI响应特征向量 (UserAiRespondFeatureVector_list_wlq)', recordCount: 0 },
  { value: 'UserBase_list_wlq', label: '用户基础 (UserBase_list_wlq)', recordCount: 0 }
])

// 表格列配置
const tableColumnConfigs = {
  'UserInfo_list_njj': [
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'username', label: '用户名', width: 150 },
    { prop: 'realName', label: '真实姓名', width: 150 },
    { prop: 'age', label: '年龄', width: 80 },
    { prop: 'gender', label: '性别', width: 100, formatter: (val: any) => getGenderType(val) },
    { prop: 'userKind', label: '用户类型', width: 100, formatter: (val: any) => getUserKindType(val) },
    { prop: 'realNameVerified', label: '实名认证', width: 100, formatter: (val: any) => val === 1 ? 'success' : 'info' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'AchievementBase_list_njj': [
    { prop: 'achievementId', label: '成就ID', width: 100, sortable: true },
    { prop: 'name', label: '成就名称', width: 200 },
    { prop: 'description', label: '描述', width: 300 },
    { prop: 'condition', label: '条件', width: 200 },
    { prop: 'iconUrl', label: '图标URL', width: 150 },
    { prop: 'isView', label: '是否可见', width: 100, formatter: (val: any) => val === 1 ? 'success' : 'info' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'AchievementRecord_list_njj': [
    { prop: 'recordId', label: '记录ID', width: 100, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100 },
    { prop: 'achievementId', label: '成就ID', width: 100 },
    { prop: 'progress', label: '进度', width: 150 },
    { prop: 'isCompleted', label: '是否完成', width: 100, formatter: (val: any) => val === 1 ? 'success' : 'warning' },
    { prop: 'completedAt', label: '完成时间', width: 180, type: 'date' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'AgreementBase_list_njj': [
    { prop: 'fileId', label: '文件ID', width: 100, sortable: true },
    { prop: 'title', label: '标题', width: 200 },
    { prop: 'content', label: '内容', width: 300 },
    { prop: 'version', label: '版本', width: 100 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'AimodelBaseInfo_list_njj': [
    { prop: 'modelId', label: '模型ID', width: 100, sortable: true },
    { prop: 'modelName', label: '模型名称', width: 200 },
    { prop: 'modelDesc', label: '模型描述', width: 300 },
    { prop: 'price', label: '价格', width: 100, type: 'number' },
    { prop: 'score', label: '评分', width: 100, type: 'number' },
    { prop: 'likeCount', label: '点赞数', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'AimodelCoinLog_list_njj': [
    { prop: 'logId', label: '日志ID', width: 100, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'userAiId', label: '用户AI ID', width: 100, sortable: true },
    { prop: 'amount', label: '金额', width: 100, type: 'number' },
    { prop: 'totalAfter', label: '余额', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'AimodelLevelRule_list_njj': [
    { prop: 'levelId', label: '等级ID', width: 100, sortable: true },
    { prop: 'level', label: '等级', width: 100, type: 'number' },
    { prop: 'minRecharge', label: '最小充值', width: 120, type: 'number' },
    { prop: 'powerBonus', label: '力量加成', width: 100, type: 'number' },
    { prop: 'expRequirement', label: '经验要求', width: 100, type: 'number' }
  ],
  'AiTaskLog_list_njj': [
    { prop: 'aiTaskLogId', label: '任务日志ID', width: 120, sortable: true },
    { prop: 'aiTaskName', label: '任务名称', width: 200 },
    { prop: 'aiTaskDesc', label: '任务描述', width: 300 },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '进行中' : '已完成' },
    { prop: 'taskStar', label: '任务星级', width: 100 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'BlockRecord_list_njj': [
    { prop: 'recordId', label: '记录ID', width: 100, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'blockedUserId', label: '被屏蔽用户ID', width: 120, sortable: true },
    { prop: 'blockedAt', label: '屏蔽时间', width: 180, type: 'date' },
    { prop: 'unblockedAt', label: '解屏蔽时间', width: 180, type: 'date' },
    { prop: 'isBlocked', label: '是否屏蔽', width: 100, formatter: (val: any) => val === 1 ? '已屏蔽' : '未屏蔽' }
  ],
  'CertBase_list_njj': [
    { prop: 'postId', label: '证书ID', width: 100, sortable: true },
    { prop: 'certName', label: '证书名称', width: 200 },
    { prop: 'parentId', label: '父级ID', width: 100, sortable: true },
    { prop: 'childName', label: '子项名称', width: 200 },
    { prop: 'childDesc', label: '子项描述', width: 300 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'CertJobBase_list_njj': [
    { prop: 'jobId', label: '工作ID', width: 100, sortable: true },
    { prop: 'parentId', label: '父级ID', width: 100, sortable: true },
    { prop: 'jobType', label: '工作类型', width: 150 },
    { prop: 'jobName', label: '工作名称', width: 200 }
  ],
  'CertRealnameBase_list_njj': [
    { prop: 'id', label: 'ID', width: 100, sortable: true },
    { prop: 'idType', label: '证件类型', width: 150 },
    { prop: 'ruleDesc', label: '规则描述', width: 300 }
  ],
  'CertStudentBase_list_njj': [
    { prop: 'schoolId', label: '学校ID', width: 100, sortable: true },
    { prop: 'region', label: '地区', width: 150 },
    { prop: 'schoolName', label: '学校名称', width: 250 }
  ],
  'ChatSettingsBase_list_njj': [
    { prop: 'chatSettingId', label: '设置ID', width: 100, sortable: true },
    { prop: 'chatSettingName', label: '设置名称', width: 200 },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '启用' : '禁用' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'EmojiBase_list_njj': [
    { prop: 'emojiId', label: '表情ID', width: 100, sortable: true },
    { prop: 'emojiName', label: '表情名称', width: 200 },
    { prop: 'emojiImageUrl', label: '表情图片URL', width: 300 },
    { prop: 'isVisible', label: '是否可见', width: 100, formatter: (val: any) => val === 1 ? '可见' : '隐藏' },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true }
  ],
  'FriendsProfile_list_njj': [
    { prop: 'friendsProfileId', label: '好友资料ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'friendId', label: '好友ID', width: 100, sortable: true },
    { prop: 'friendsProfile', label: '好友资料', width: 300 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'MessageExtraBase_list_njj': [
    { prop: 'extraMessageId', label: '扩展消息ID', width: 120, sortable: true },
    { prop: 'extraMessageName', label: '扩展消息名称', width: 200 },
    { prop: 'extraMessageUrl', label: '扩展消息URL', width: 300 },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '启用' : '禁用' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'ProductsBase_list_njj': [
    { prop: 'coinRechargeId', label: '充值ID', width: 100, sortable: true },
    { prop: 'coinNum', label: '币数', width: 100, type: 'number' },
    { prop: 'expNum', label: '经验数', width: 100, type: 'number' },
    { prop: 'coinExtraNum', label: '额外币数', width: 120, type: 'number' },
    { prop: 'expExtraNum', label: '额外经验数', width: 120, type: 'number' },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '启用' : '禁用' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'PurchaseRecord_list_njj': [
    { prop: 'recordId', label: '记录ID', width: 100, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'itemType', label: '商品类型', width: 120 },
    { prop: 'itemName', label: '商品名称', width: 200 },
    { prop: 'coinsSpent', label: '花费币数', width: 100, type: 'number' },
    { prop: 'coinsBalance', label: '余额', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'ReportCategory_list_njj': [
    { prop: 'categoryId', label: '类别ID', width: 100, sortable: true },
    { prop: 'categoryName', label: '类别名称', width: 200 },
    { prop: 'description', label: '描述', width: 300 },
    { prop: 'isEnabled', label: '是否启用', width: 100, formatter: (val: any) => val === 1 ? '启用' : '禁用' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'ReportRecord_list_njj': [
    { prop: 'reportId', label: '举报ID', width: 100, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'reportedUserId', label: '被举报用户ID', width: 120, sortable: true },
    { prop: 'contentType', label: '内容类型', width: 120 },
    { prop: 'description', label: '描述', width: 300 },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '已处理' : '待处理' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'TaskComment_list_njj': [
    { prop: 'taskCommentId', label: '评论ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'stars', label: '星级', width: 100, type: 'number' },
    { prop: 'commentText', label: '评论内容', width: 300 },
    { prop: 'isVisible', label: '是否可见', width: 100, formatter: (val: any) => val === 1 ? '可见' : '隐藏' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserAiComment_list_njj': [
    { prop: 'userAiCommentId', label: '评论ID', width: 120, sortable: true },
    { prop: 'aimodelId', label: 'AI模型ID', width: 100, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'commentText', label: '评论内容', width: 300 },
    { prop: 'likeCount', label: '点赞数', width: 100, type: 'number' },
    { prop: 'stars', label: '星级', width: 100, type: 'number' },
    { prop: 'isVisible', label: '是否可见', width: 100, formatter: (val: any) => val === 1 ? '可见' : '隐藏' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserAiModel_list_njj': [
    { prop: 'userAiId', label: '模型ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'parentModelId', label: '父模型ID', width: 100, sortable: true },
    { prop: 'modelName', label: '模型名称', width: 200 },
    { prop: 'customDesc', label: '自定义描述', width: 300 },
    { prop: 'tone', label: '语调', width: 100 },
    { prop: 'power', label: '能力值', width: 100, type: 'number' },
    { prop: 'level', label: '等级', width: 100, type: 'number' },
    { prop: 'totalExp', label: '总经验', width: 100, type: 'number' },
    { prop: 'rechargeAmount', label: '充值金额', width: 120, type: 'number' },
    { prop: 'isVisible', label: '是否可见', width: 100, formatter: (val: any) => val === 1 ? '可见' : '隐藏' },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '正常' : '禁用' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserCertRecord_list_njj': [
    { prop: 'recordId', label: '记录ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'certType', label: '认证类型', width: 120, formatter: (val: any) => getCertTypeName(val) },
    { prop: 'certInfo', label: '认证信息', width: 200 },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '已认证' : '待认证' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserBaseDynamic_list_njj': [
    { prop: 'dynamicId', label: '动态ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'content', label: '动态内容', width: 300 },
    { prop: 'contentType', label: '内容类型', width: 100, formatter: (val: any) => {
      const types: { [key: number]: string } = { 1: '文本', 2: '图片', 3: '视频' }
      return types[val as number] || '未知'
    }},
    { prop: 'originalDynamicId', label: '原始动态ID', width: 120, sortable: true },
    { prop: 'forwardCount', label: '转发数', width: 100, type: 'number' },
    { prop: 'commentCount', label: '评论数', width: 100, type: 'number' },
    { prop: 'likeCount', label: '点赞数', width: 100, type: 'number' },
    { prop: 'isVisible', label: '可见性', width: 100, formatter: (val: any) => {
      const visibility: { [key: number]: string } = { 1: '公开', 2: '好友可见', 3: '仅自己可见' }
      return visibility[val as number] || '未知'
    }},
    { prop: 'location', label: '位置', width: 150 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserChatDetail_list_njj': [
    { prop: 'id', label: 'ID', width: 120, sortable: true },
    { prop: 'conversationId', label: '会话ID', width: 150 },
    { prop: 'sessionId', label: '会话ID', width: 150 },
    { prop: 'demandParty', label: '需求方', width: 120 },
    { prop: 'message', label: '消息内容', width: 300 },
    { prop: 'responseParty', label: '响应方', width: 120 },
    { prop: 'senderType', label: '发送者类型', width: 120 },
    { prop: 'createAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserHardTagRelation_list_njj': [
    { prop: 'hardRelationId', label: '关系ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'hardTagId', label: '硬标签ID', width: 120, sortable: true },
    { prop: 'hardRelevanceScore', label: '相关性分数', width: 120, type: 'number' },
    { prop: 'lastUpdated', label: '最后更新', width: 180, type: 'date' },
    { prop: 'createdTime', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserLikeRelation_list_njj': [
    { prop: 'likeId', label: '点赞ID', width: 120, sortable: true },
    { prop: 'likerUserId', label: '点赞用户ID', width: 120, sortable: true },
    { prop: 'likedUserId', label: '被点赞用户ID', width: 120, sortable: true },
    { prop: 'isActive', label: '是否有效', width: 100, formatter: (val: any) => val === 1 ? '有效' : '无效' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserTextRecommendation_list_njj': [
    { prop: 'id', label: '推荐ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'textContentId', label: '文本内容ID', width: 120, sortable: true },
    { prop: 'score', label: '推荐分数', width: 120, type: 'number' },
    { prop: 'rankOrder', label: '排序', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserBaseLikeAction_list_njj': [
    { prop: 'likeId', label: '点赞ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'targetType', label: '目标类型', width: 120, type: 'number' },
    { prop: 'targetId', label: '目标ID', width: 120, sortable: true },
    { prop: 'likeTime', label: '点赞时间', width: 180, type: 'date' },
    { prop: 'isCanceled', label: '是否取消', width: 100, formatter: (val: any) => val === 1 ? '已取消' : '有效' }
  ],
  'UserBasePicComment_list_njj': [
    { prop: 'commentId', label: '评论ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'picId', label: '图片ID', width: 120, sortable: true },
    { prop: 'content', label: '评论内容', width: 300 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserBaseSystemMessage_list_njj': [
    { prop: 'messageId', label: '消息ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'title', label: '标题', width: 200 },
    { prop: 'content', label: '内容', width: 300 },
    { prop: 'isRead', label: '是否已读', width: 100, formatter: (val: any) => val === 1 ? '已读' : '未读' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserBaseTextComment_list_njj': [
    { prop: 'commentId', label: '评论ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'textId', label: '文本ID', width: 120, sortable: true },
    { prop: 'content', label: '评论内容', width: 300 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserBaseUserCollectioin_list_njj': [
    { prop: 'collectionId', label: '收藏ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'targetType', label: '目标类型', width: 120, type: 'number' },
    { prop: 'targetId', label: '目标ID', width: 120, sortable: true },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserChatList_list_njj': [
    { prop: 'chatListId', label: '聊天列表ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'friendId', label: '好友ID', width: 100, sortable: true },
    { prop: 'lastMessage', label: '最后消息', width: 300 },
    { prop: 'lastMessageTime', label: '最后消息时间', width: 180, type: 'date' },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 'active' ? '活跃' : '非活跃' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserContentViewLog_list_njj': [
    { prop: 'logId', label: '日志ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'contentType', label: '内容类型', width: 120, type: 'number' },
    { prop: 'viewType', label: '查看类型', width: 120, type: 'number' },
    { prop: 'contentId', label: '内容ID', width: 120, sortable: true },
    { prop: 'viewTime', label: '查看时间', width: 180, type: 'date' }
  ],
  'UserDevice_list_njj': [
    { prop: 'deviceId', label: '设备ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'deviceType', label: '设备类型', width: 150 },
    { prop: 'deviceToken', label: '设备令牌', width: 200 },
    { prop: 'isActive', label: '是否活跃', width: 100, formatter: (val: any) => val === 1 ? '活跃' : '非活跃' },
    { prop: 'lastActiveTime', label: '最后活跃时间', width: 180, type: 'date' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserDislikeRelation_list_njj': [
    { prop: 'dislikeId', label: '不喜欢ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'dislikedUserId', label: '被不喜欢用户ID', width: 150, sortable: true },
    { prop: 'reason', label: '原因', width: 200 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserFriendsRelationship_list_njj': [
    { prop: 'friendshipId', label: '好友关系ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'friendId', label: '好友ID', width: 100, sortable: true },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '好友' : '非好友' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserImageRecommendation_list_njj': [
    { prop: 'recommendationId', label: '推荐ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'imageId', label: '图片ID', width: 120, sortable: true },
    { prop: 'score', label: '推荐分数', width: 120, type: 'number' },
    { prop: 'rankOrder', label: '排序', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserInfoFeatureVector_list_njj': [
    { prop: 'vectorId', label: '向量ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'featureVector', label: '特征向量', width: 300 },
    { prop: 'vectorType', label: '向量类型', width: 120 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserInfoQuestion_list_njj': [
    { prop: 'questionId', label: '问题ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'question', label: '问题', width: 300 },
    { prop: 'answer', label: '答案', width: 300 },
    { prop: 'isPublic', label: '是否公开', width: 100, formatter: (val: any) => val === 1 ? '公开' : '私有' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserNovelRecommendation_list_njj': [
    { prop: 'recommendationId', label: '推荐ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'novelId', label: '小说ID', width: 120, sortable: true },
    { prop: 'score', label: '推荐分数', width: 120, type: 'number' },
    { prop: 'rankOrder', label: '排序', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserNovelRelation_list_njj': [
    { prop: 'relationId', label: '关系ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'novelId', label: '小说ID', width: 120, sortable: true },
    { prop: 'relationType', label: '关系类型', width: 120 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserReviewBase_list_njj': [
    { prop: 'reviewId', label: '评论ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'targetId', label: '目标ID', width: 120, sortable: true },
    { prop: 'targetType', label: '目标类型', width: 120 },
    { prop: 'rating', label: '评分', width: 100, type: 'number' },
    { prop: 'comment', label: '评论内容', width: 300 },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '已审核' : '待审核' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserSettingRelation_list_njj': [
    { prop: 'settingId', label: '设置ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'settingKey', label: '设置键', width: 150 },
    { prop: 'settingValue', label: '设置值', width: 200 },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  'UserSoftTagRelation_list_njj': [
    { prop: 'softRelationId', label: '软标签关系ID', width: 150, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'softTagId', label: '软标签ID', width: 120, sortable: true },
    { prop: 'weight', label: '权重', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserSystemMessage_list_njj': [
    { prop: 'messageId', label: '消息ID', width: 120, sortable: true },
    { prop: 'recipientUserId', label: '接收用户ID', width: 120, sortable: true },
    { prop: 'senderUserId', label: '发送用户ID', width: 120, sortable: true },
    { prop: 'messageContent', label: '消息内容', width: 300 },
    { prop: 'isRead', label: '是否已读', width: 100, formatter: (val: any) => val === 1 ? '已读' : '未读' },
    { prop: 'relatedEntityType', label: '关联实体类型', width: 150 },
    { prop: 'relatedEntityId', label: '关联实体ID', width: 120, sortable: true },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  'UserTaskRelationship_list_njj': [
    { prop: 'taskRelationId', label: '任务关系ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'taskId', label: '任务ID', width: 120, sortable: true },
    { prop: 'status', label: '状态', width: 100, formatter: (val: any) => val === 1 ? '进行中' : '已完成' },
    { prop: 'progress', label: '进度', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' },
    { prop: 'updatedAt', label: '更新时间', width: 180, type: 'date' }
  ],
  
  // _wlq 后缀的表格列配置
  'SoftTagBase_list_wlq': [
    { prop: 'softTagId', label: '软标签ID', width: 120, sortable: true },
    { prop: 'softTagContent', label: '软标签内容', width: 200 },
    { prop: 'categoryId', label: '分类ID', width: 100, sortable: true },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  
  'HardTagBase_list_wlq': [
    { prop: 'hardTagId', label: '硬标签ID', width: 120, sortable: true },
    { prop: 'categoryId', label: '分类ID', width: 100, sortable: true },
    { prop: 'relevanceValue', label: '关联度', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  
  'TextpostBase_list_wlq': [
    { prop: 'textContentId', label: '文本内容ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'contentText', label: '内容文本', width: 300 },
    { prop: 'likeCount', label: '点赞数', width: 100, type: 'number' },
    { prop: 'favoriteCount', label: '收藏数', width: 100, type: 'number' },
    { prop: 'commentCount', label: '评论数', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  
  'ImagePostBase_list_wlq': [
    { prop: 'imageContentId', label: '图片内容ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'imageUrl', label: '图片URL', width: 200 },
    { prop: 'likeCount', label: '点赞数', width: 100, type: 'number' },
    { prop: 'favoriteCount', label: '收藏数', width: 100, type: 'number' },
    { prop: 'commentCount', label: '评论数', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ],
  
  'NovelpostBase_list_wlq': [
    { prop: 'novelContentId', label: '小说内容ID', width: 120, sortable: true },
    { prop: 'userId', label: '用户ID', width: 100, sortable: true },
    { prop: 'title', label: '标题', width: 200 },
    { prop: 'chapterCount', label: '章节数', width: 100, type: 'number' },
    { prop: 'likeCount', label: '点赞数', width: 100, type: 'number' },
    { prop: 'favoriteCount', label: '收藏数', width: 100, type: 'number' },
    { prop: 'createdAt', label: '创建时间', width: 180, type: 'date' }
  ]
}

// 计算属性
const entityList = computed(() => tableStore.entityList)
const listClasses = computed(() => tableStore.listClasses)
const entityLoading = computed(() => tableStore.entityLoading)

// 分离_njj和_wlq实体类
const njjEntityList = computed(() => {
  return entityList.value.filter(entity => entity.className.endsWith('_njj'))
})

const wlqEntityList = computed(() => {
  return entityList.value.filter(entity => entity.className.endsWith('_wlq'))
})

// 按字母排序的List类
const sortedListClasses = computed(() => {
  return [...listClasses.value].sort((a, b) => a.className.localeCompare(b.className))
})

const selectedDataSourceLabel = computed(() => {
  const source = availableDataSources.value.find(s => s.value === selectedDataSource.value)
  return source ? source.label : '未知数据源'
})

// 按字母排序的数据源 - 按照类名排序，Achievement在前，UserInfo在后
const sortedDataSources = computed(() => {
  return [...availableDataSources.value].sort((a, b) => {
    // 提取类名进行排序（去掉_list_njj后缀）
    const nameA = a.value.replace('_list_njj', '')
    const nameB = b.value.replace('_list_njj', '')
    return nameA.localeCompare(nameB)
  })
})

onMounted(async () => {
  // 获取统计数据
  await loadStats()
  // 加载实体类数据
  await loadEntityData()
  // 加载用户统计信息
  await loadUserStats()
  // 更新数据源记录数
  await updateDataSourceRecordCounts()
})

const loadStats = async () => {
  try {
    // 模拟统计数据
    stats.value = {
      totalFriends: 1250,
      onlineFriends: 89,
      totalEntityClasses: entityList.value.length,
      totalListClasses: listClasses.value.length
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadEntityData = async () => {
  try {
    await tableStore.fetchAllEntities()
    // 更新统计数据
    stats.value.totalEntityClasses = entityList.value.length
    stats.value.totalListClasses = listClasses.value.length
    ElMessage.success('实体类信息加载成功')
  } catch (error) {
    console.error('加载实体类信息失败:', error)
    ElMessage.error('加载实体类信息失败')
  }
}

const viewEntityDetail = (entity: any) => {
  ElMessage.info(`查看实体类: ${entity.className}`)
  // 这里可以跳转到详情页面或打开对话框
}

const viewListDetail = (listClass: any) => {
  ElMessage.info(`查看List类: ${listClass.className}`)
  // 这里可以跳转到详情页面或打开对话框
}

// 导航方法
const goToTableSelector = () => {
  router.push('/table-selector')
}

const goToRecommendationAdmin = () => {
  router.push('/recommendation-admin')
}



const loadUserStats = async () => {
  try {
    const { getUserStatistics } = await import('@/utils/api')
    const response = await getUserStatistics()
    userStats.value = response as any
  } catch (error: any) {
    console.error('加载用户统计信息失败:', error)
  }
}

const viewMemoryData = async () => {
  memoryDataVisible.value = true
  // 清空之前的数据
  selectedDataSource.value = ''
  currentMemoryData.value = []
  currentMemoryStats.value = null
  currentTableColumns.value = []
}

const refreshMemoryData = async () => {
  if (selectedDataSource.value) {
    await loadSelectedDataSource()
  } else {
    await viewMemoryData()
  }
}

const selectDataSource = (dataSource: string) => {
  selectedDataSource.value = dataSource
  // 清空当前数据
  currentMemoryData.value = []
  currentMemoryStats.value = null
  currentTableColumns.value = []
}

const clearDataSource = () => {
  selectedDataSource.value = ''
  currentMemoryData.value = []
  currentMemoryStats.value = null
  currentTableColumns.value = []
}

// 加载所有数据并显示详细结果
const loadAllDataWithDetails = async () => {
  try {
    userLoading.value = true
    console.log('开始加载所有数据...')

    const results = []
    let totalSuccess = 0
    let totalFail = 0

    // 加载A开头实体类
    try {
      const { loadAllAEntityData } = await import('@/utils/api')
      const aResponse = await loadAllAEntityData()
      const aData = aResponse as any
      results.push({
        category: 'A开头实体类',
        successCount: aData.successCount || 0,
        failCount: aData.failCount || 0,
        totalCount: aData.totalCount || 0,
        loadResults: aData.loadResults || []
      })
      totalSuccess += aData.successCount || 0
      totalFail += aData.failCount || 0
    } catch (error: any) {
      console.error('加载A开头实体类失败:', error)
      results.push({
        category: 'A开头实体类',
        successCount: 0,
        failCount: 1,
        totalCount: 1,
        loadResults: [{ entityName: 'A开头实体类', status: 'failed', message: error.message }]
      })
      totalFail++
    }

    // 加载B和C开头实体类
    try {
      const { loadAllBCEntityData } = await import('@/utils/api')
      const bcResponse = await loadAllBCEntityData()
      const bcData = bcResponse as any
      results.push({
        category: 'B和C开头实体类',
        successCount: bcData.successCount || 0,
        failCount: bcData.failCount || 0,
        totalCount: bcData.totalCount || 0,
        loadResults: bcData.loadResults || []
      })
      totalSuccess += bcData.successCount || 0
      totalFail += bcData.failCount || 0
    } catch (error: any) {
      console.error('加载B和C开头实体类失败:', error)
      results.push({
        category: 'B和C开头实体类',
        successCount: 0,
        failCount: 1,
        totalCount: 1,
        loadResults: [{ entityName: 'B和C开头实体类', status: 'failed', message: error.message }]
      })
      totalFail++
    }

    // 加载E、F、M、P、R、T开头实体类
    try {
      const { loadAllEFMPRTEntityData } = await import('@/utils/api')
      const efmprtResponse = await loadAllEFMPRTEntityData()
      const efmprtData = efmprtResponse as any
      results.push({
        category: 'E、F、M、P、R、T开头实体类',
        successCount: efmprtData.successCount || 0,
        failCount: efmprtData.failCount || 0,
        totalCount: efmprtData.totalCount || 0,
        loadResults: efmprtData.loadResults || []
      })
      totalSuccess += efmprtData.successCount || 0
      totalFail += efmprtData.failCount || 0
    } catch (error: any) {
      console.error('加载E、F、M、P、R、T开头实体类失败:', error)
      results.push({
        category: 'E、F、M、P、R、T开头实体类',
        successCount: 0,
        failCount: 1,
        totalCount: 1,
        loadResults: [{ entityName: 'E、F、M、P、R、T开头实体类', status: 'failed', message: error.message }]
      })
      totalFail++
    }

    // 显示详细结果
    console.log('所有数据加载完成:', results)
    console.log(`总计: 成功 ${totalSuccess} 个，失败 ${totalFail} 个`)
    
    // 显示结果消息
    ElMessage.success(`数据加载完成！成功: ${totalSuccess} 个，失败: ${totalFail} 个`)
    
    // 更新数据源记录数
    await updateDataSourceRecordCounts()

  } catch (error: any) {
    console.error('加载所有数据失败:', error)
    ElMessage.error('加载所有数据失败: ' + error.message)
  } finally {
    userLoading.value = false
  }
}

// 更新数据源记录数
const updateDataSourceRecordCounts = async () => {
  try {
    // 为每个数据源获取真实的记录数
    for (let i = 0; i < availableDataSources.value.length; i++) {
      const dataSource = availableDataSources.value[i]
      try {
        // 根据数据源类型调用相应的统计API
        let statsResponse
        switch (dataSource.value) {
          case 'UserInfo_list_njj':
            const { getUserStatistics } = await import('@/utils/api')
            statsResponse = await getUserStatistics()
            dataSource.recordCount = (statsResponse as any).totalUsers || 0
            break
            
          case 'AchievementBase_list_njj':
            const { getAchievementBaseStatistics } = await import('@/utils/api')
            statsResponse = await getAchievementBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalAchievementBase || 0
            break
            
          case 'AchievementRecord_list_njj':
            const { getAchievementRecordStatistics } = await import('@/utils/api')
            statsResponse = await getAchievementRecordStatistics()
            dataSource.recordCount = (statsResponse as any).totalAchievementRecord || 0
            break
            
          case 'AimodelCoinLog_list_njj':
            const { getAimodelCoinLogStatistics } = await import('@/utils/api')
            statsResponse = await getAimodelCoinLogStatistics()
            dataSource.recordCount = (statsResponse as any).totalAimodelCoinLog || 0
            break
            
          case 'AimodelBaseInfo_list_njj':
            const { getAimodelBaseInfoStatistics } = await import('@/utils/api')
            statsResponse = await getAimodelBaseInfoStatistics()
            dataSource.recordCount = (statsResponse as any).totalAimodelBaseInfo || 0
            break
            
          case 'AiTaskLog_list_njj':
            const { getAiTaskLogStatistics } = await import('@/utils/api')
            statsResponse = await getAiTaskLogStatistics()
            dataSource.recordCount = (statsResponse as any).totalAiTaskLog || 0
            break
            
          case 'CertBase_list_njj':
            const { getCertBaseStatistics } = await import('@/utils/api')
            statsResponse = await getCertBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalCertBase || 0
            break
            
          case 'CertJobBase_list_njj':
            const { getCertJobBaseStatistics } = await import('@/utils/api')
            statsResponse = await getCertJobBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalCertJobBase || 0
            break
            
          case 'AgreementBase_list_njj':
            const { getAgreementBaseStatistics } = await import('@/utils/api')
            statsResponse = await getAgreementBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalAgreementBase || 0
            break
            
          case 'AimodelLevelRule_list_njj':
            const { getAimodelLevelRuleStatistics } = await import('@/utils/api')
            statsResponse = await getAimodelLevelRuleStatistics()
            dataSource.recordCount = (statsResponse as any).totalAimodelLevelRule || 0
            break
            
          case 'BlockRecord_list_njj':
            const { getBlockRecordStatistics } = await import('@/utils/api')
            statsResponse = await getBlockRecordStatistics()
            dataSource.recordCount = (statsResponse as any).totalBlockRecord || 0
            break
            
          case 'CertRealnameBase_list_njj':
            const { getCertRealnameBaseStatistics } = await import('@/utils/api')
            statsResponse = await getCertRealnameBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalCertRealnameBase || 0
            break
            
          case 'CertStudentBase_list_njj':
            const { getCertStudentBaseStatistics } = await import('@/utils/api')
            statsResponse = await getCertStudentBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalCertStudentBase || 0
            break
            
          case 'ChatSettingsBase_list_njj':
            const { getChatSettingsBaseStatistics } = await import('@/utils/api')
            statsResponse = await getChatSettingsBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalChatSettingsBase || 0
            break
            
          case 'EmojiBase_list_njj':
            const { getEmojiBaseStatistics } = await import('@/utils/api')
            statsResponse = await getEmojiBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalEmojiBase || 0
            break
            
          case 'FriendsProfile_list_njj':
            const { getFriendsProfileStatistics } = await import('@/utils/api')
            statsResponse = await getFriendsProfileStatistics()
            dataSource.recordCount = (statsResponse as any).totalFriendsProfile || 0
            break
            
          case 'MessageExtraBase_list_njj':
            const { getMessageExtraBaseStatistics } = await import('@/utils/api')
            statsResponse = await getMessageExtraBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalMessageExtraBase || 0
            break
            
          case 'ProductsBase_list_njj':
            const { getProductsBaseStatistics } = await import('@/utils/api')
            statsResponse = await getProductsBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalProductsBase || 0
            break
            
          case 'PurchaseRecord_list_njj':
            const { getPurchaseRecordStatistics } = await import('@/utils/api')
            statsResponse = await getPurchaseRecordStatistics()
            dataSource.recordCount = (statsResponse as any).totalPurchaseRecord || 0
            break
            
          case 'ReportCategory_list_njj':
            const { getReportCategoryStatistics } = await import('@/utils/api')
            statsResponse = await getReportCategoryStatistics()
            dataSource.recordCount = (statsResponse as any).totalReportCategory || 0
            break
            
          case 'ReportRecord_list_njj':
            const { getReportRecordStatistics } = await import('@/utils/api')
            statsResponse = await getReportRecordStatistics()
            dataSource.recordCount = (statsResponse as any).totalReportRecord || 0
            break
            
          case 'TaskComment_list_njj':
            const { getTaskCommentStatistics } = await import('@/utils/api')
            statsResponse = await getTaskCommentStatistics()
            dataSource.recordCount = (statsResponse as any).totalTaskComment || 0
            break
            
          case 'UserAiComment_list_njj':
            const { getUserAiCommentStatistics } = await import('@/utils/api')
            statsResponse = await getUserAiCommentStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserAiComment || 0
            break
            
          case 'UserAiModel_list_njj':
            const { getUserAiModelStatistics } = await import('@/utils/api')
            statsResponse = await getUserAiModelStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserAiModel || 0
            break
            
          case 'UserBaseDynamic_list_njj':
            const { getUserBaseDynamicStatistics } = await import('@/utils/api')
            statsResponse = await getUserBaseDynamicStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBaseDynamic || 0
            break
            
          case 'UserBaseImagepostBase_list_njj':
            const { getUserBaseImagepostBaseStatistics } = await import('@/utils/api')
            statsResponse = await getUserBaseImagepostBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBaseImagepostBase || 0
            break
            
          case 'UserHardTagRelation_list_njj':
            const { getUserHardTagRelationStatistics } = await import('@/utils/api')
            statsResponse = await getUserHardTagRelationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserHardTagRelation || 0
            break
            
          case 'UserLikeRelation_list_njj':
            const { getUserLikeRelationStatistics } = await import('@/utils/api')
            statsResponse = await getUserLikeRelationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserLikeRelation || 0
            break
            
          case 'UserTextRecommendation_list_njj':
            const { getUserTextRecommendationStatistics } = await import('@/utils/api')
            statsResponse = await getUserTextRecommendationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserTextRecommendation || 0
            break
            
          case 'UserBaseLikeAction_list_njj':
            const { getUserBaseLikeActionStatistics } = await import('@/utils/api')
            statsResponse = await getUserBaseLikeActionStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBaseLikeAction || 0
            break
            
          case 'UserBasePicComment_list_njj':
            const { getUserBasePicCommentStatistics } = await import('@/utils/api')
            statsResponse = await getUserBasePicCommentStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBasePicComment || 0
            break
            
          case 'UserBaseSystemMessage_list_njj':
            const { getUserBaseSystemMessageStatistics } = await import('@/utils/api')
            statsResponse = await getUserBaseSystemMessageStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBaseSystemMessage || 0
            break
            
          case 'UserBaseTextComment_list_njj':
            const { getUserBaseTextCommentStatistics } = await import('@/utils/api')
            statsResponse = await getUserBaseTextCommentStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBaseTextComment || 0
            break
            
          case 'UserBaseUserCollectioin_list_njj':
            const { getUserBaseUserCollectioinStatistics } = await import('@/utils/api')
            statsResponse = await getUserBaseUserCollectioinStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserBaseUserCollectioin || 0
            break

          // 新增的8个U开头实体类
          case 'UserCertRecord_list_njj':
            const { getUserCertRecordStatistics } = await import('@/utils/api')
            statsResponse = await getUserCertRecordStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserCertRecord || 0
            break

          case 'UserChatList_list_njj':
            const { getUserChatListStatistics } = await import('@/utils/api')
            statsResponse = await getUserChatListStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserChatList || 0
            break

          case 'UserContentViewLog_list_njj':
            const { getUserContentViewLogStatistics } = await import('@/utils/api')
            statsResponse = await getUserContentViewLogStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserContentViewLog || 0
            break

          case 'UserDevice_list_njj':
            const { getUserDeviceStatistics } = await import('@/utils/api')
            statsResponse = await getUserDeviceStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserDevice || 0
            break

          case 'UserDislikeRelation_list_njj':
            const { getUserDislikeRelationStatistics } = await import('@/utils/api')
            statsResponse = await getUserDislikeRelationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserDislikeRelation || 0
            break

          case 'UserFriendsRelationship_list_njj':
            const { getUserFriendsRelationshipStatistics } = await import('@/utils/api')
            statsResponse = await getUserFriendsRelationshipStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserFriendsRelationship || 0
            break

          case 'UserImageRecommendation_list_njj':
            const { getUserImageRecommendationStatistics } = await import('@/utils/api')
            statsResponse = await getUserImageRecommendationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserImageRecommendation || 0
            break

          // 新增的11个U开头实体类
          case 'UserInfoFeatureVector_list_njj':
            const { getUserInfoFeatureVectorStatistics } = await import('@/utils/api')
            statsResponse = await getUserInfoFeatureVectorStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserInfoFeatureVector || 0
            break

          case 'UserInfoQuestion_list_njj':
            const { getUserInfoQuestionStatistics } = await import('@/utils/api')
            statsResponse = await getUserInfoQuestionStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserInfoQuestion || 0
            break

          case 'UserNovelRecommendation_list_njj':
            const { getUserNovelRecommendationStatistics } = await import('@/utils/api')
            statsResponse = await getUserNovelRecommendationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserNovelRecommendation || 0
            break

          case 'UserNovelRelation_list_njj':
            const { getUserNovelRelationStatistics } = await import('@/utils/api')
            statsResponse = await getUserNovelRelationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserNovelRelation || 0
            break

          case 'UserReviewBase_list_njj':
            const { getUserReviewBaseStatistics } = await import('@/utils/api')
            statsResponse = await getUserReviewBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserReviewBase || 0
            break

          case 'UserSettingRelation_list_njj':
            const { getUserSettingRelationStatistics } = await import('@/utils/api')
            statsResponse = await getUserSettingRelationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserSettingRelation || 0
            break

          case 'UserSoftTagRelation_list_njj':
            const { getUserSoftTagRelationStatistics } = await import('@/utils/api')
            statsResponse = await getUserSoftTagRelationStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserSoftTagRelation || 0
            break

          case 'UserSystemMessage_list_njj':
            const { getUserSystemMessageStatistics } = await import('@/utils/api')
            statsResponse = await getUserSystemMessageStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserSystemMessage || 0
            break

          case 'UserTaskRelationship_list_njj':
            const { getUserTaskRelationshipStatistics } = await import('@/utils/api')
            statsResponse = await getUserTaskRelationshipStatistics()
            dataSource.recordCount = (statsResponse as any).totalUserTaskRelationship || 0
            break

          // _wlq 后缀的数据源统计
          case 'SoftTagBase_list_wlq':
            const { getSoftTagBaseStatistics } = await import('@/utils/api')
            statsResponse = await getSoftTagBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalSoftTagBase || 0
            break

          case 'HardTagBase_list_wlq':
            const { getHardTagBaseStatistics } = await import('@/utils/api')
            statsResponse = await getHardTagBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalHardTagBase || 0
            break

          case 'TextpostBase_list_wlq':
            const { getTextpostBaseStatistics } = await import('@/utils/api')
            statsResponse = await getTextpostBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalTextpostBase || 0
            break

          case 'ImagePostBase_list_wlq':
            const { getImagePostBaseStatistics } = await import('@/utils/api')
            statsResponse = await getImagePostBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalImagePostBase || 0
            break

          case 'NovelpostBase_list_wlq':
            const { getNovelpostBaseStatistics } = await import('@/utils/api')
            statsResponse = await getNovelpostBaseStatistics()
            dataSource.recordCount = (statsResponse as any).totalNovelpostBase || 0
            break
            
          case 'UserInfoFeatureVector_list_njj':
          case 'UserInfoQuestion_list_njj':
          case 'UserNovelRecommendation_list_njj':
          case 'UserNovelRelation_list_njj':
          case 'UserReviewBase_list_njj':
          case 'UserSettingRelation_list_njj':
          case 'UserSystemMessage_list_njj':
          case 'UserTextRecommendation_list_njj':
            // 这些API暂时不存在，设置为0
            dataSource.recordCount = 0
            break
            
          default:
            // 对于其他数据源，暂时设置为0
            dataSource.recordCount = 0
            break
        }
      } catch (error) {
        console.warn(`获取 ${dataSource.value} 记录数失败:`, error)
        dataSource.recordCount = 0
      }
    }
  } catch (error) {
    console.error('更新数据源记录数失败:', error)
  }
}


const loadSelectedDataSource = async () => {
  if (!selectedDataSource.value) {
    ElMessage.warning('请先选择数据源')
    return
  }

  try {
    userLoading.value = true
    ElMessage.info(`正在加载 ${selectedDataSourceLabel.value} 数据...`)
    
    // 根据选择的数据源调用对应的API
    await loadDataSourceData(selectedDataSource.value)
    
    // 设置表格列配置
    currentTableColumns.value = tableColumnConfigs[selectedDataSource.value as keyof typeof tableColumnConfigs] || []
    
    ElMessage.success(`${selectedDataSourceLabel.value} 数据加载成功`)
  } catch (error: any) {
    console.error('加载数据源数据失败:', error)
    ElMessage.error('加载数据源数据失败: ' + error.message)
  } finally {
    userLoading.value = false
  }
}

const loadDataSourceData = async (dataSource: string) => {
  switch (dataSource) {
    case 'UserInfo_list_njj':
      const { getAllUsers, getUserStatistics } = await import('@/utils/api')
      const usersResponse = await getAllUsers()
      const userStatsResponse = await getUserStatistics() as any
      currentMemoryData.value = usersResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userStatsResponse.totalUsers,
        cacheLoaded: userStatsResponse.cacheLoaded,
        lastUpdateTime: userStatsResponse.lastUpdateTime
      }
      break
      
    case 'AchievementBase_list_njj':
      const { getAllAchievementBase, getAchievementBaseStatistics } = await import('@/utils/api')
      const achievementBaseResponse = await getAllAchievementBase()
      const achievementBaseStatsResponse = await getAchievementBaseStatistics() as any
      currentMemoryData.value = achievementBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: achievementBaseStatsResponse.totalAchievementBase,
        cacheLoaded: achievementBaseStatsResponse.cacheLoaded,
        lastUpdateTime: achievementBaseStatsResponse.lastUpdateTime
      }
      break
      
    case 'AchievementRecord_list_njj':
      const { getAllAchievementRecord, getAchievementRecordStatistics } = await import('@/utils/api')
      const achievementRecordResponse = await getAllAchievementRecord()
      const achievementRecordStatsResponse = await getAchievementRecordStatistics() as any
      currentMemoryData.value = achievementRecordResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: achievementRecordStatsResponse.totalAchievementRecord,
        cacheLoaded: achievementRecordStatsResponse.cacheLoaded,
        lastUpdateTime: achievementRecordStatsResponse.lastUpdateTime,
        completedCount: achievementRecordStatsResponse.completedCount
      }
      break

    case 'AgreementBase_list_njj':
      const { getAllAgreementBase, getAgreementBaseStatistics } = await import('@/utils/api')
      const agreementBaseResponse = await getAllAgreementBase()
      const agreementBaseStatsResponse = await getAgreementBaseStatistics() as any
      currentMemoryData.value = agreementBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: agreementBaseStatsResponse.totalAgreementBase,
        cacheLoaded: agreementBaseStatsResponse.cacheLoaded,
        lastUpdateTime: agreementBaseStatsResponse.lastUpdateTime
      }
      break

    case 'AimodelBaseInfo_list_njj':
      const { getAllAimodelBaseInfo, getAimodelBaseInfoStatistics } = await import('@/utils/api')
      const aimodelBaseInfoResponse = await getAllAimodelBaseInfo()
      const aimodelBaseInfoStatsResponse = await getAimodelBaseInfoStatistics() as any
      currentMemoryData.value = aimodelBaseInfoResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: aimodelBaseInfoStatsResponse.totalAimodelBaseInfo,
        cacheLoaded: aimodelBaseInfoStatsResponse.cacheLoaded,
        lastUpdateTime: aimodelBaseInfoStatsResponse.lastUpdateTime
      }
      break

    case 'AimodelCoinLog_list_njj':
      const { getAllAimodelCoinLog, getAimodelCoinLogStatistics } = await import('@/utils/api')
      const aimodelCoinLogResponse = await getAllAimodelCoinLog()
      const aimodelCoinLogStatsResponse = await getAimodelCoinLogStatistics() as any
      currentMemoryData.value = aimodelCoinLogResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: aimodelCoinLogStatsResponse.totalAimodelCoinLog,
        cacheLoaded: aimodelCoinLogStatsResponse.cacheLoaded,
        lastUpdateTime: aimodelCoinLogStatsResponse.lastUpdateTime
      }
      break

    case 'AimodelLevelRule_list_njj':
      const { getAllAimodelLevelRule, getAimodelLevelRuleStatistics } = await import('@/utils/api')
      const aimodelLevelRuleResponse = await getAllAimodelLevelRule()
      const aimodelLevelRuleStatsResponse = await getAimodelLevelRuleStatistics() as any
      currentMemoryData.value = aimodelLevelRuleResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: aimodelLevelRuleStatsResponse.totalAimodelLevelRule,
        cacheLoaded: aimodelLevelRuleStatsResponse.cacheLoaded,
        lastUpdateTime: aimodelLevelRuleStatsResponse.lastUpdateTime
      }
      break

    case 'AiTaskLog_list_njj':
      const { getAllAiTaskLog, getAiTaskLogStatistics } = await import('@/utils/api')
      const aiTaskLogResponse = await getAllAiTaskLog()
      const aiTaskLogStatsResponse = await getAiTaskLogStatistics() as any
      currentMemoryData.value = aiTaskLogResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: aiTaskLogStatsResponse.totalAiTaskLog,
        cacheLoaded: aiTaskLogStatsResponse.cacheLoaded,
        lastUpdateTime: aiTaskLogStatsResponse.lastUpdateTime
      }
      break

    case 'BlockRecord_list_njj':
      const { getAllBlockRecord, getBlockRecordStatistics } = await import('@/utils/api')
      const blockRecordResponse = await getAllBlockRecord()
      const blockRecordStatsResponse = await getBlockRecordStatistics() as any
      currentMemoryData.value = blockRecordResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: blockRecordStatsResponse.totalBlockRecord,
        cacheLoaded: blockRecordStatsResponse.cacheLoaded,
        lastUpdateTime: blockRecordStatsResponse.lastUpdateTime
      }
      break

    case 'CertBase_list_njj':
      const { getAllCertBase, getCertBaseStatistics } = await import('@/utils/api')
      const certBaseResponse = await getAllCertBase()
      const certBaseStatsResponse = await getCertBaseStatistics() as any
      currentMemoryData.value = certBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: certBaseStatsResponse.totalCertBase,
        cacheLoaded: certBaseStatsResponse.cacheLoaded,
        lastUpdateTime: certBaseStatsResponse.lastUpdateTime
      }
      break

    case 'CertJobBase_list_njj':
      const { getAllCertJobBase, getCertJobBaseStatistics } = await import('@/utils/api')
      const certJobBaseResponse = await getAllCertJobBase()
      const certJobBaseStatsResponse = await getCertJobBaseStatistics() as any
      currentMemoryData.value = certJobBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: certJobBaseStatsResponse.totalCertJobBase,
        cacheLoaded: certJobBaseStatsResponse.cacheLoaded,
        lastUpdateTime: certJobBaseStatsResponse.lastUpdateTime
      }
      break

    case 'CertRealnameBase_list_njj':
      const { getAllCertRealnameBase, getCertRealnameBaseStatistics } = await import('@/utils/api')
      const certRealnameBaseResponse = await getAllCertRealnameBase()
      const certRealnameBaseStatsResponse = await getCertRealnameBaseStatistics() as any
      currentMemoryData.value = certRealnameBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: certRealnameBaseStatsResponse.totalCertRealnameBase,
        cacheLoaded: certRealnameBaseStatsResponse.cacheLoaded,
        lastUpdateTime: certRealnameBaseStatsResponse.lastUpdateTime
      }
      break

    case 'CertStudentBase_list_njj':
      const { getAllCertStudentBase, getCertStudentBaseStatistics } = await import('@/utils/api')
      const certStudentBaseResponse = await getAllCertStudentBase()
      const certStudentBaseStatsResponse = await getCertStudentBaseStatistics() as any
      currentMemoryData.value = certStudentBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: certStudentBaseStatsResponse.totalCertStudentBase,
        cacheLoaded: certStudentBaseStatsResponse.cacheLoaded,
        lastUpdateTime: certStudentBaseStatsResponse.lastUpdateTime
      }
      break

    case 'ChatSettingsBase_list_njj':
      const { getAllChatSettingsBase, getChatSettingsBaseStatistics } = await import('@/utils/api')
      const chatSettingsBaseResponse = await getAllChatSettingsBase()
      const chatSettingsBaseStatsResponse = await getChatSettingsBaseStatistics() as any
      currentMemoryData.value = chatSettingsBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: chatSettingsBaseStatsResponse.totalChatSettingsBase,
        cacheLoaded: chatSettingsBaseStatsResponse.cacheLoaded,
        lastUpdateTime: chatSettingsBaseStatsResponse.lastUpdateTime
      }
      break

    case 'EmojiBase_list_njj':
      const { getAllEmojiBase, getEmojiBaseStatistics } = await import('@/utils/api')
      const emojiBaseResponse = await getAllEmojiBase()
      const emojiBaseStatsResponse = await getEmojiBaseStatistics() as any
      currentMemoryData.value = emojiBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: emojiBaseStatsResponse.totalEmojiBase,
        cacheLoaded: emojiBaseStatsResponse.cacheLoaded,
        lastUpdateTime: emojiBaseStatsResponse.lastUpdateTime
      }
      break

    case 'FriendsProfile_list_njj':
      const { getAllFriendsProfile, getFriendsProfileStatistics } = await import('@/utils/api')
      const friendsProfileResponse = await getAllFriendsProfile()
      const friendsProfileStatsResponse = await getFriendsProfileStatistics() as any
      currentMemoryData.value = friendsProfileResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: friendsProfileStatsResponse.totalFriendsProfile,
        cacheLoaded: friendsProfileStatsResponse.cacheLoaded,
        lastUpdateTime: friendsProfileStatsResponse.lastUpdateTime
      }
      break

    case 'MessageExtraBase_list_njj':
      const { getAllMessageExtraBase, getMessageExtraBaseStatistics } = await import('@/utils/api')
      const messageExtraBaseResponse = await getAllMessageExtraBase()
      const messageExtraBaseStatsResponse = await getMessageExtraBaseStatistics() as any
      currentMemoryData.value = messageExtraBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: messageExtraBaseStatsResponse.totalMessageExtraBase,
        cacheLoaded: messageExtraBaseStatsResponse.cacheLoaded,
        lastUpdateTime: messageExtraBaseStatsResponse.lastUpdateTime
      }
      break

    case 'ProductsBase_list_njj':
      const { getAllProductsBase, getProductsBaseStatistics } = await import('@/utils/api')
      const productsBaseResponse = await getAllProductsBase()
      const productsBaseStatsResponse = await getProductsBaseStatistics() as any
      currentMemoryData.value = productsBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: productsBaseStatsResponse.totalProductsBase,
        cacheLoaded: productsBaseStatsResponse.cacheLoaded,
        lastUpdateTime: productsBaseStatsResponse.lastUpdateTime
      }
      break

    case 'PurchaseRecord_list_njj':
      const { getAllPurchaseRecord, getPurchaseRecordStatistics } = await import('@/utils/api')
      const purchaseRecordResponse = await getAllPurchaseRecord()
      const purchaseRecordStatsResponse = await getPurchaseRecordStatistics() as any
      currentMemoryData.value = purchaseRecordResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: purchaseRecordStatsResponse.totalPurchaseRecord,
        cacheLoaded: purchaseRecordStatsResponse.cacheLoaded,
        lastUpdateTime: purchaseRecordStatsResponse.lastUpdateTime
      }
      break

    case 'ReportCategory_list_njj':
      const { getAllReportCategory, getReportCategoryStatistics } = await import('@/utils/api')
      const reportCategoryResponse = await getAllReportCategory()
      const reportCategoryStatsResponse = await getReportCategoryStatistics() as any
      currentMemoryData.value = reportCategoryResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: reportCategoryStatsResponse.totalReportCategory,
        cacheLoaded: reportCategoryStatsResponse.cacheLoaded,
        lastUpdateTime: reportCategoryStatsResponse.lastUpdateTime
      }
      break

    case 'ReportRecord_list_njj':
      const { getAllReportRecord, getReportRecordStatistics } = await import('@/utils/api')
      const reportRecordResponse = await getAllReportRecord()
      const reportRecordStatsResponse = await getReportRecordStatistics() as any
      currentMemoryData.value = reportRecordResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: reportRecordStatsResponse.totalReportRecord,
        cacheLoaded: reportRecordStatsResponse.cacheLoaded,
        lastUpdateTime: reportRecordStatsResponse.lastUpdateTime
      }
      break

    case 'TaskComment_list_njj':
      const { getAllTaskComment, getTaskCommentStatistics } = await import('@/utils/api')
      const taskCommentResponse = await getAllTaskComment()
      const taskCommentStatsResponse = await getTaskCommentStatistics() as any
      currentMemoryData.value = taskCommentResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: taskCommentStatsResponse.totalTaskComment,
        cacheLoaded: taskCommentStatsResponse.cacheLoaded,
        lastUpdateTime: taskCommentStatsResponse.lastUpdateTime
      }
      break

    case 'UserAiComment_list_njj':
      const { getAllUserAiComment, getUserAiCommentStatistics } = await import('@/utils/api')
      const userAiCommentResponse = await getAllUserAiComment()
      const userAiCommentStatsResponse = await getUserAiCommentStatistics() as any
      currentMemoryData.value = userAiCommentResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userAiCommentStatsResponse.totalUserAiComment,
        cacheLoaded: userAiCommentStatsResponse.cacheLoaded,
        lastUpdateTime: userAiCommentStatsResponse.lastUpdateTime
      }
      break

    case 'UserAiModel_list_njj':
      const { getAllUserAiModel, getUserAiModelStatistics } = await import('@/utils/api')
      const userAiModelResponse = await getAllUserAiModel()
      const userAiModelStatsResponse = await getUserAiModelStatistics() as any
      currentMemoryData.value = userAiModelResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userAiModelStatsResponse.totalUserAiModel,
        cacheLoaded: userAiModelStatsResponse.cacheLoaded,
        lastUpdateTime: userAiModelStatsResponse.lastUpdateTime
      }
      break

    case 'UserBaseDynamic_list_njj':
      const { getAllUserBaseDynamic, getUserBaseDynamicStatistics } = await import('@/utils/api')
      const userBaseDynamicResponse = await getAllUserBaseDynamic()
      const userBaseDynamicStatsResponse = await getUserBaseDynamicStatistics() as any
      currentMemoryData.value = userBaseDynamicResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userBaseDynamicStatsResponse.totalUserBaseDynamic,
        cacheLoaded: userBaseDynamicStatsResponse.cacheLoaded,
        lastUpdateTime: userBaseDynamicStatsResponse.lastUpdateTime
      }
      break

    case 'UserChatDetail_list_njj':
      const { getAllUserChatDetail, getUserChatDetailStatistics } = await import('@/utils/api')
      const userChatDetailResponse = await getAllUserChatDetail()
      const userChatDetailStatsResponse = await getUserChatDetailStatistics() as any
      currentMemoryData.value = userChatDetailResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userChatDetailStatsResponse.totalUserChatDetail,
        cacheLoaded: userChatDetailStatsResponse.cacheLoaded,
        lastUpdateTime: userChatDetailStatsResponse.lastUpdateTime
      }
      break

    case 'UserHardTagRelation_list_njj':
      const { getAllUserHardTagRelation, getUserHardTagRelationStatistics } = await import('@/utils/api')
      const userHardTagRelationResponse = await getAllUserHardTagRelation()
      const userHardTagRelationStatsResponse = await getUserHardTagRelationStatistics() as any
      currentMemoryData.value = userHardTagRelationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userHardTagRelationStatsResponse.totalUserHardTagRelation,
        cacheLoaded: userHardTagRelationStatsResponse.cacheLoaded,
        lastUpdateTime: userHardTagRelationStatsResponse.lastUpdateTime
      }
      break

    case 'UserLikeRelation_list_njj':
      const { getAllUserLikeRelation, getUserLikeRelationStatistics } = await import('@/utils/api')
      const userLikeRelationResponse = await getAllUserLikeRelation()
      const userLikeRelationStatsResponse = await getUserLikeRelationStatistics() as any
      currentMemoryData.value = userLikeRelationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userLikeRelationStatsResponse.totalUserLikeRelation,
        cacheLoaded: userLikeRelationStatsResponse.cacheLoaded,
        lastUpdateTime: userLikeRelationStatsResponse.lastUpdateTime
      }
      break

    case 'UserTextRecommendation_list_njj':
      const { getAllUserTextRecommendation, getUserTextRecommendationStatistics } = await import('@/utils/api')
      const userTextRecommendationResponse = await getAllUserTextRecommendation()
      const userTextRecommendationStatsResponse = await getUserTextRecommendationStatistics() as any
      currentMemoryData.value = userTextRecommendationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userTextRecommendationStatsResponse.totalUserTextRecommendation,
        cacheLoaded: userTextRecommendationStatsResponse.cacheLoaded,
        lastUpdateTime: userTextRecommendationStatsResponse.lastUpdateTime
      }
      break

    case 'UserBaseLikeAction_list_njj':
      const { getAllUserBaseLikeAction, getUserBaseLikeActionStatistics } = await import('@/utils/api')
      const userBaseLikeActionResponse = await getAllUserBaseLikeAction()
      const userBaseLikeActionStatsResponse = await getUserBaseLikeActionStatistics() as any
      currentMemoryData.value = userBaseLikeActionResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userBaseLikeActionStatsResponse.totalUserBaseLikeAction,
        cacheLoaded: userBaseLikeActionStatsResponse.cacheLoaded,
        lastUpdateTime: userBaseLikeActionStatsResponse.lastUpdateTime
      }
      break

    case 'UserBasePicComment_list_njj':
      const { getAllUserBasePicComment, getUserBasePicCommentStatistics } = await import('@/utils/api')
      const userBasePicCommentResponse = await getAllUserBasePicComment()
      const userBasePicCommentStatsResponse = await getUserBasePicCommentStatistics() as any
      currentMemoryData.value = userBasePicCommentResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userBasePicCommentStatsResponse.totalUserBasePicComment,
        cacheLoaded: userBasePicCommentStatsResponse.cacheLoaded,
        lastUpdateTime: userBasePicCommentStatsResponse.lastUpdateTime
      }
      break

    case 'UserBaseSystemMessage_list_njj':
      const { getAllUserBaseSystemMessage, getUserBaseSystemMessageStatistics } = await import('@/utils/api')
      const userBaseSystemMessageResponse = await getAllUserBaseSystemMessage()
      const userBaseSystemMessageStatsResponse = await getUserBaseSystemMessageStatistics() as any
      currentMemoryData.value = userBaseSystemMessageResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userBaseSystemMessageStatsResponse.totalUserBaseSystemMessage,
        cacheLoaded: userBaseSystemMessageStatsResponse.cacheLoaded,
        lastUpdateTime: userBaseSystemMessageStatsResponse.lastUpdateTime
      }
      break

    case 'UserBaseTextComment_list_njj':
      const { getAllUserBaseTextComment, getUserBaseTextCommentStatistics } = await import('@/utils/api')
      const userBaseTextCommentResponse = await getAllUserBaseTextComment()
      const userBaseTextCommentStatsResponse = await getUserBaseTextCommentStatistics() as any
      currentMemoryData.value = userBaseTextCommentResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userBaseTextCommentStatsResponse.totalUserBaseTextComment,
        cacheLoaded: userBaseTextCommentStatsResponse.cacheLoaded,
        lastUpdateTime: userBaseTextCommentStatsResponse.lastUpdateTime
      }
      break

    case 'UserBaseUserCollectioin_list_njj':
      const { getAllUserBaseUserCollectioin, getUserBaseUserCollectioinStatistics } = await import('@/utils/api')
      const userBaseUserCollectioinResponse = await getAllUserBaseUserCollectioin()
      const userBaseUserCollectioinStatsResponse = await getUserBaseUserCollectioinStatistics() as any
      currentMemoryData.value = userBaseUserCollectioinResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userBaseUserCollectioinStatsResponse.totalUserBaseUserCollectioin,
        cacheLoaded: userBaseUserCollectioinStatsResponse.cacheLoaded,
        lastUpdateTime: userBaseUserCollectioinStatsResponse.lastUpdateTime
      }
      break

    // 新增的8个U开头实体类
    case 'UserCertRecord_list_njj':
      const { getAllUserCertRecord, getUserCertRecordStatistics } = await import('@/utils/api')
      const userCertRecordResponse = await getAllUserCertRecord()
      const userCertRecordStatsResponse = await getUserCertRecordStatistics() as any
      currentMemoryData.value = userCertRecordResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userCertRecordStatsResponse.totalUserCertRecord,
        cacheLoaded: userCertRecordStatsResponse.cacheLoaded,
        lastUpdateTime: userCertRecordStatsResponse.lastUpdateTime
      }
      break

    case 'UserChatList_list_njj':
      const { getAllUserChatList, getUserChatListStatistics } = await import('@/utils/api')
      const userChatListResponse = await getAllUserChatList()
      const userChatListStatsResponse = await getUserChatListStatistics() as any
      currentMemoryData.value = userChatListResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userChatListStatsResponse.totalUserChatList,
        cacheLoaded: userChatListStatsResponse.cacheLoaded,
        lastUpdateTime: userChatListStatsResponse.lastUpdateTime
      }
      break

    case 'UserContentViewLog_list_njj':
      const { getAllUserContentViewLog, getUserContentViewLogStatistics } = await import('@/utils/api')
      const userContentViewLogResponse = await getAllUserContentViewLog()
      const userContentViewLogStatsResponse = await getUserContentViewLogStatistics() as any
      currentMemoryData.value = userContentViewLogResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userContentViewLogStatsResponse.totalUserContentViewLog,
        cacheLoaded: userContentViewLogStatsResponse.cacheLoaded,
        lastUpdateTime: userContentViewLogStatsResponse.lastUpdateTime
      }
      break

    case 'UserDevice_list_njj':
      const { getAllUserDevice, getUserDeviceStatistics } = await import('@/utils/api')
      const userDeviceResponse = await getAllUserDevice()
      const userDeviceStatsResponse = await getUserDeviceStatistics() as any
      currentMemoryData.value = userDeviceResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userDeviceStatsResponse.totalUserDevice,
        cacheLoaded: userDeviceStatsResponse.cacheLoaded,
        lastUpdateTime: userDeviceStatsResponse.lastUpdateTime
      }
      break

    case 'UserDislikeRelation_list_njj':
      const { getAllUserDislikeRelation, getUserDislikeRelationStatistics } = await import('@/utils/api')
      const userDislikeRelationResponse = await getAllUserDislikeRelation()
      const userDislikeRelationStatsResponse = await getUserDislikeRelationStatistics() as any
      currentMemoryData.value = userDislikeRelationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userDislikeRelationStatsResponse.totalUserDislikeRelation,
        cacheLoaded: userDislikeRelationStatsResponse.cacheLoaded,
        lastUpdateTime: userDislikeRelationStatsResponse.lastUpdateTime
      }
      break

    case 'UserFriendsRelationship_list_njj':
      const { getAllUserFriendsRelationship, getUserFriendsRelationshipStatistics } = await import('@/utils/api')
      const userFriendsRelationshipResponse = await getAllUserFriendsRelationship()
      const userFriendsRelationshipStatsResponse = await getUserFriendsRelationshipStatistics() as any
      currentMemoryData.value = userFriendsRelationshipResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userFriendsRelationshipStatsResponse.totalUserFriendsRelationship,
        cacheLoaded: userFriendsRelationshipStatsResponse.cacheLoaded,
        lastUpdateTime: userFriendsRelationshipStatsResponse.lastUpdateTime
      }
      break

    case 'UserImageRecommendation_list_njj':
      const { getAllUserImageRecommendation, getUserImageRecommendationStatistics } = await import('@/utils/api')
      const userImageRecommendationResponse = await getAllUserImageRecommendation()
      const userImageRecommendationStatsResponse = await getUserImageRecommendationStatistics() as any
      currentMemoryData.value = userImageRecommendationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userImageRecommendationStatsResponse.totalUserImageRecommendation,
        cacheLoaded: userImageRecommendationStatsResponse.cacheLoaded,
        lastUpdateTime: userImageRecommendationStatsResponse.lastUpdateTime
      }
      break

    // 新增的11个U开头实体类
    case 'UserInfoFeatureVector_list_njj':
      const { getAllUserInfoFeatureVector, getUserInfoFeatureVectorStatistics } = await import('@/utils/api')
      const userInfoFeatureVectorResponse = await getAllUserInfoFeatureVector()
      const userInfoFeatureVectorStatsResponse = await getUserInfoFeatureVectorStatistics() as any
      currentMemoryData.value = userInfoFeatureVectorResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userInfoFeatureVectorStatsResponse.totalUserInfoFeatureVector,
        cacheLoaded: userInfoFeatureVectorStatsResponse.cacheLoaded,
        lastUpdateTime: userInfoFeatureVectorStatsResponse.lastUpdateTime
      }
      break

    case 'UserInfoQuestion_list_njj':
      const { getAllUserInfoQuestion, getUserInfoQuestionStatistics } = await import('@/utils/api')
      const userInfoQuestionResponse = await getAllUserInfoQuestion()
      const userInfoQuestionStatsResponse = await getUserInfoQuestionStatistics() as any
      currentMemoryData.value = userInfoQuestionResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userInfoQuestionStatsResponse.totalUserInfoQuestion,
        cacheLoaded: userInfoQuestionStatsResponse.cacheLoaded,
        lastUpdateTime: userInfoQuestionStatsResponse.lastUpdateTime
      }
      break

    case 'UserNovelRecommendation_list_njj':
      const { getAllUserNovelRecommendation, getUserNovelRecommendationStatistics } = await import('@/utils/api')
      const userNovelRecommendationResponse = await getAllUserNovelRecommendation()
      const userNovelRecommendationStatsResponse = await getUserNovelRecommendationStatistics() as any
      currentMemoryData.value = userNovelRecommendationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userNovelRecommendationStatsResponse.totalUserNovelRecommendation,
        cacheLoaded: userNovelRecommendationStatsResponse.cacheLoaded,
        lastUpdateTime: userNovelRecommendationStatsResponse.lastUpdateTime
      }
      break

    case 'UserNovelRelation_list_njj':
      const { getAllUserNovelRelation, getUserNovelRelationStatistics } = await import('@/utils/api')
      const userNovelRelationResponse = await getAllUserNovelRelation()
      const userNovelRelationStatsResponse = await getUserNovelRelationStatistics() as any
      currentMemoryData.value = userNovelRelationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userNovelRelationStatsResponse.totalUserNovelRelation,
        cacheLoaded: userNovelRelationStatsResponse.cacheLoaded,
        lastUpdateTime: userNovelRelationStatsResponse.lastUpdateTime
      }
      break

    case 'UserReviewBase_list_njj':
      const { getAllUserReviewBase, getUserReviewBaseStatistics } = await import('@/utils/api')
      const userReviewBaseResponse = await getAllUserReviewBase()
      const userReviewBaseStatsResponse = await getUserReviewBaseStatistics() as any
      currentMemoryData.value = userReviewBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userReviewBaseStatsResponse.totalUserReviewBase,
        cacheLoaded: userReviewBaseStatsResponse.cacheLoaded,
        lastUpdateTime: userReviewBaseStatsResponse.lastUpdateTime
      }
      break

    case 'UserSettingRelation_list_njj':
      const { getAllUserSettingRelation, getUserSettingRelationStatistics } = await import('@/utils/api')
      const userSettingRelationResponse = await getAllUserSettingRelation()
      const userSettingRelationStatsResponse = await getUserSettingRelationStatistics() as any
      currentMemoryData.value = userSettingRelationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userSettingRelationStatsResponse.totalUserSettingRelation,
        cacheLoaded: userSettingRelationStatsResponse.cacheLoaded,
        lastUpdateTime: userSettingRelationStatsResponse.lastUpdateTime
      }
      break

    case 'UserSoftTagRelation_list_njj':
      const { getAllUserSoftTagRelation, getUserSoftTagRelationStatistics } = await import('@/utils/api')
      const userSoftTagRelationResponse = await getAllUserSoftTagRelation()
      const userSoftTagRelationStatsResponse = await getUserSoftTagRelationStatistics() as any
      currentMemoryData.value = userSoftTagRelationResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userSoftTagRelationStatsResponse.totalUserSoftTagRelation,
        cacheLoaded: userSoftTagRelationStatsResponse.cacheLoaded,
        lastUpdateTime: userSoftTagRelationStatsResponse.lastUpdateTime
      }
      break

    case 'UserSystemMessage_list_njj':
      const { getAllUserSystemMessage, getUserSystemMessageStatistics } = await import('@/utils/api')
      const userSystemMessageResponse = await getAllUserSystemMessage()
      const userSystemMessageStatsResponse = await getUserSystemMessageStatistics() as any
      currentMemoryData.value = userSystemMessageResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userSystemMessageStatsResponse.totalUserSystemMessage,
        cacheLoaded: userSystemMessageStatsResponse.cacheLoaded,
        lastUpdateTime: userSystemMessageStatsResponse.lastUpdateTime
      }
      break

    case 'UserTaskRelationship_list_njj':
      const { getAllUserTaskRelationship, getUserTaskRelationshipStatistics } = await import('@/utils/api')
      const userTaskRelationshipResponse = await getAllUserTaskRelationship()
      const userTaskRelationshipStatsResponse = await getUserTaskRelationshipStatistics() as any
      currentMemoryData.value = userTaskRelationshipResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: userTaskRelationshipStatsResponse.totalUserTaskRelationship,
        cacheLoaded: userTaskRelationshipStatsResponse.cacheLoaded,
        lastUpdateTime: userTaskRelationshipStatsResponse.lastUpdateTime
      }
      break

    // _wlq 后缀的数据源
    case 'SoftTagBase_list_wlq':
      const { getAllSoftTagBase, getSoftTagBaseStatistics } = await import('@/utils/api')
      const softTagBaseResponse = await getAllSoftTagBase()
      const softTagBaseStatsResponse = await getSoftTagBaseStatistics() as any
      currentMemoryData.value = softTagBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: softTagBaseStatsResponse.totalSoftTagBase,
        cacheLoaded: softTagBaseStatsResponse.cacheLoaded,
        lastUpdateTime: softTagBaseStatsResponse.lastUpdateTime
      }
      break

    case 'HardTagBase_list_wlq':
      const { getAllHardTagBase, getHardTagBaseStatistics } = await import('@/utils/api')
      const hardTagBaseResponse = await getAllHardTagBase()
      const hardTagBaseStatsResponse = await getHardTagBaseStatistics() as any
      currentMemoryData.value = hardTagBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: hardTagBaseStatsResponse.totalHardTagBase,
        cacheLoaded: hardTagBaseStatsResponse.cacheLoaded,
        lastUpdateTime: hardTagBaseStatsResponse.lastUpdateTime
      }
      break

    case 'TextpostBase_list_wlq':
      const { getAllTextpostBase, getTextpostBaseStatistics } = await import('@/utils/api')
      const textpostBaseResponse = await getAllTextpostBase()
      const textpostBaseStatsResponse = await getTextpostBaseStatistics() as any
      currentMemoryData.value = textpostBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: textpostBaseStatsResponse.totalTextpostBase,
        cacheLoaded: textpostBaseStatsResponse.cacheLoaded,
        lastUpdateTime: textpostBaseStatsResponse.lastUpdateTime
      }
      break

    case 'ImagePostBase_list_wlq':
      const { getAllImagePostBase, getImagePostBaseStatistics } = await import('@/utils/api')
      const imagePostBaseResponse = await getAllImagePostBase()
      const imagePostBaseStatsResponse = await getImagePostBaseStatistics() as any
      currentMemoryData.value = imagePostBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: imagePostBaseStatsResponse.totalImagePostBase,
        cacheLoaded: imagePostBaseStatsResponse.cacheLoaded,
        lastUpdateTime: imagePostBaseStatsResponse.lastUpdateTime
      }
      break

    case 'NovelpostBase_list_wlq':
      const { getAllNovelpostBase, getNovelpostBaseStatistics } = await import('@/utils/api')
      const novelpostBaseResponse = await getAllNovelpostBase()
      const novelpostBaseStatsResponse = await getNovelpostBaseStatistics() as any
      currentMemoryData.value = novelpostBaseResponse as unknown as any[]
      currentMemoryStats.value = {
        totalCount: novelpostBaseStatsResponse.totalNovelpostBase,
        cacheLoaded: novelpostBaseStatsResponse.cacheLoaded,
        lastUpdateTime: novelpostBaseStatsResponse.lastUpdateTime
      }
      break
      
    default:
      ElMessage.warning(`暂不支持 ${dataSource} 数据源`)
      break
  }
}

const getFirstColumnProp = () => {
  if (currentTableColumns.value.length > 0) {
    return currentTableColumns.value[0].prop
  }
  return ''
}

const formatColumnValue = (value: any, type?: string) => {
  if (value === null || value === undefined) {
    return '-'
  }
  
  if (type === 'boolean') {
    return value ? '是' : '否'
  }
  
  if (type === 'number') {
    return Number(value).toLocaleString()
  }
  
  return String(value)
}

const handleMemoryDataClose = () => {
  memoryDataVisible.value = false
}

const getGenderType = (gender: string) => {
  switch (gender) {
    case 'MALE':
      return 'primary'
    case 'FEMALE':
      return 'danger'
    default:
      return 'info'
  }
}

const getUserKindType = (userKind: string) => {
  switch (userKind) {
    case 'REAL':
      return 'success'
    case 'ACTOR':
      return 'warning'
    default:
      return 'info'
  }
}

const getCertTypeName = (certType: string) => {
  switch (certType) {
    case 'REALNAME':
      return '实名认证'
    case 'STUDENT':
      return '学生认证'
    case 'JOB':
      return '工作认证'
    default:
      return certType
  }
}

const formatTime = (timestamp: any) => {
  if (!timestamp) return '未知'
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

const formatDate = (dateString: string) => {
  if (!dateString) return '未知'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

const exportMemoryData = () => {
  ElMessage.info('导出功能开发中...')
}

const testDatabaseConnection = async () => {
  try {
    userLoading.value = true
    ElMessage.info('正在测试数据库连接...')
    
    const { testDatabase } = await import('@/utils/api')
    const response = await testDatabase() as any
    
    if (response.success) {
      ElMessage.success(`数据库连接正常！用户总数: ${response.userCount}，耗时: ${response.duration}ms`)
    } else {
      ElMessage.error(`数据库连接失败: ${response.error}`)
    }
  } catch (error: any) {
    console.error('测试数据库连接失败:', error)
    ElMessage.error('测试数据库连接失败: ' + error.message)
  } finally {
    userLoading.value = false
  }
}

// List类选择相关方法
const handleSelectAllLists = (val: boolean) => {
  if (val) {
    selectedListClasses.value = listClasses.value.map(item => item.className)
  } else {
    selectedListClasses.value = []
  }
  updateSelectAllStatus()
}

const handleListSelectionChange = () => {
  updateSelectAllStatus()
}

const updateSelectAllStatus = () => {
  const selectedCount = selectedListClasses.value.length
  const totalCount = listClasses.value.length
  
  selectAllLists.value = selectedCount === totalCount
  isIndeterminate.value = selectedCount > 0 && selectedCount < totalCount
}

const clearSelection = () => {
  selectedListClasses.value = []
  selectAllLists.value = false
  isIndeterminate.value = false
}

const loadSelectedListData = async () => {
  if (selectedListClasses.value.length === 0) {
    ElMessage.warning('请先选择要加载的List类')
    return
  }

  try {
    listDataLoading.value = true
    ElMessage.info(`正在加载 ${selectedListClasses.value.length} 个List类的数据到内存...`)
    
    // 这里调用对应的API来加载数据
    // 目前先模拟加载过程
    await loadListDataFromAPI()
    
    // 数据加载成功后，更新记录数显示
    await updateDataSourceRecordCounts()
    
    ElMessage.success(`成功加载 ${selectedListClasses.value.length} 个List类的数据到内存`)
  } catch (error: any) {
    console.error('加载List类数据失败:', error)
    ElMessage.error('加载List类数据失败: ' + error.message)
  } finally {
    listDataLoading.value = false
  }
}

const loadListDataFromAPI = async () => {
  // 根据选中的List类调用对应的API
  const apiCalls = []
  
  for (const className of selectedListClasses.value) {
    switch (className) {
      case 'UserInfo_list_njj':
        const { loadUserData } = await import('@/utils/api')
        apiCalls.push(loadUserData())
        break
      case 'AchievementBase_list_njj':
        const { loadAchievementBaseData } = await import('@/utils/api')
        apiCalls.push(loadAchievementBaseData())
        break
      case 'AchievementRecord_list_njj':
        const { loadAchievementRecordData } = await import('@/utils/api')
        apiCalls.push(loadAchievementRecordData())
        break

      case 'AgreementBase_list_njj':
        const { loadAgreementBaseData } = await import('@/utils/api')
        apiCalls.push(loadAgreementBaseData())
        break

      case 'AimodelBaseInfo_list_njj':
        const { loadAimodelBaseInfoData } = await import('@/utils/api')
        apiCalls.push(loadAimodelBaseInfoData())
        break

      case 'AimodelCoinLog_list_njj':
        const { loadAimodelCoinLogData } = await import('@/utils/api')
        apiCalls.push(loadAimodelCoinLogData())
        break

      case 'AimodelLevelRule_list_njj':
        const { loadAimodelLevelRuleData } = await import('@/utils/api')
        apiCalls.push(loadAimodelLevelRuleData())
        break

      case 'AiTaskLog_list_njj':
        const { loadAiTaskLogData } = await import('@/utils/api')
        apiCalls.push(loadAiTaskLogData())
        break

      case 'BlockRecord_list_njj':
        const { loadBlockRecordData } = await import('@/utils/api')
        apiCalls.push(loadBlockRecordData())
        break

      case 'CertBase_list_njj':
        const { loadCertBaseData } = await import('@/utils/api')
        apiCalls.push(loadCertBaseData())
        break

      case 'CertJobBase_list_njj':
        const { loadCertJobBaseData } = await import('@/utils/api')
        apiCalls.push(loadCertJobBaseData())
        break

      case 'CertRealnameBase_list_njj':
        const { loadCertRealnameBaseData } = await import('@/utils/api')
        apiCalls.push(loadCertRealnameBaseData())
        break

      case 'CertStudentBase_list_njj':
        const { loadCertStudentBaseData } = await import('@/utils/api')
        apiCalls.push(loadCertStudentBaseData())
        break

      case 'ChatSettingsBase_list_njj':
        const { loadChatSettingsBaseData } = await import('@/utils/api')
        apiCalls.push(loadChatSettingsBaseData())
        break

      case 'EmojiBase_list_njj':
        const { loadEmojiBaseData } = await import('@/utils/api')
        apiCalls.push(loadEmojiBaseData())
        break

      case 'FriendsProfile_list_njj':
        const { loadFriendsProfileData } = await import('@/utils/api')
        apiCalls.push(loadFriendsProfileData())
        break

      case 'MessageExtraBase_list_njj':
        const { loadMessageExtraBaseData } = await import('@/utils/api')
        apiCalls.push(loadMessageExtraBaseData())
        break

      case 'ProductsBase_list_njj':
        const { loadProductsBaseData } = await import('@/utils/api')
        apiCalls.push(loadProductsBaseData())
        break

      case 'PurchaseRecord_list_njj':
        const { loadPurchaseRecordData } = await import('@/utils/api')
        apiCalls.push(loadPurchaseRecordData())
        break

      case 'ReportCategory_list_njj':
        const { loadReportCategoryData } = await import('@/utils/api')
        apiCalls.push(loadReportCategoryData())
        break

      case 'ReportRecord_list_njj':
        const { loadReportRecordData } = await import('@/utils/api')
        apiCalls.push(loadReportRecordData())
        break

      case 'TaskComment_list_njj':
        const { loadTaskCommentData } = await import('@/utils/api')
        apiCalls.push(loadTaskCommentData())
        break

      case 'UserAiComment_list_njj':
        const { loadUserAiCommentData } = await import('@/utils/api')
        apiCalls.push(loadUserAiCommentData())
        break

      case 'UserAiModel_list_njj':
        const { loadUserAiModelData } = await import('@/utils/api')
        apiCalls.push(loadUserAiModelData())
        break

      case 'UserBaseDynamic_list_njj':
        const { loadUserBaseDynamicData } = await import('@/utils/api')
        apiCalls.push(loadUserBaseDynamicData())
        break

      case 'UserChatDetail_list_njj':
        const { loadUserChatDetailData } = await import('@/utils/api')
        apiCalls.push(loadUserChatDetailData())
        break

      case 'UserHardTagRelation_list_njj':
        const { loadUserHardTagRelationData } = await import('@/utils/api')
        apiCalls.push(loadUserHardTagRelationData())
        break

      case 'UserLikeRelation_list_njj':
        const { loadUserLikeRelationData } = await import('@/utils/api')
        apiCalls.push(loadUserLikeRelationData())
        break

      case 'UserTextRecommendation_list_njj':
        const { loadUserTextRecommendationData } = await import('@/utils/api')
        apiCalls.push(loadUserTextRecommendationData())
        break

      case 'UserBaseLikeAction_list_njj':
        const { loadUserBaseLikeActionData } = await import('@/utils/api')
        apiCalls.push(loadUserBaseLikeActionData())
        break

      case 'UserBasePicComment_list_njj':
        const { loadUserBasePicCommentData } = await import('@/utils/api')
        apiCalls.push(loadUserBasePicCommentData())
        break

      case 'UserBaseSystemMessage_list_njj':
        const { loadUserBaseSystemMessageData } = await import('@/utils/api')
        apiCalls.push(loadUserBaseSystemMessageData())
        break

      case 'UserBaseTextComment_list_njj':
        const { loadUserBaseTextCommentData } = await import('@/utils/api')
        apiCalls.push(loadUserBaseTextCommentData())
        break

      case 'UserBaseUserCollectioin_list_njj':
        const { loadUserBaseUserCollectioinData } = await import('@/utils/api')
        apiCalls.push(loadUserBaseUserCollectioinData())
        break

      // 新增的8个U开头实体类
      case 'UserCertRecord_list_njj':
        const { loadUserCertRecordData } = await import('@/utils/api')
        apiCalls.push(loadUserCertRecordData())
        break

      case 'UserChatList_list_njj':
        const { loadUserChatListData } = await import('@/utils/api')
        apiCalls.push(loadUserChatListData())
        break

      case 'UserContentViewLog_list_njj':
        const { loadUserContentViewLogData } = await import('@/utils/api')
        apiCalls.push(loadUserContentViewLogData())
        break

      case 'UserDevice_list_njj':
        const { loadUserDeviceData } = await import('@/utils/api')
        apiCalls.push(loadUserDeviceData())
        break

      case 'UserDislikeRelation_list_njj':
        const { loadUserDislikeRelationData } = await import('@/utils/api')
        apiCalls.push(loadUserDislikeRelationData())
        break

      case 'UserFriendsRelationship_list_njj':
        const { loadUserFriendsRelationshipData } = await import('@/utils/api')
        apiCalls.push(loadUserFriendsRelationshipData())
        break

      case 'UserImageRecommendation_list_njj':
        const { loadUserImageRecommendationData } = await import('@/utils/api')
        apiCalls.push(loadUserImageRecommendationData())
        break

      // 新增的11个U开头实体类
      case 'UserInfoFeatureVector_list_njj':
        const { loadUserInfoFeatureVectorData } = await import('@/utils/api')
        apiCalls.push(loadUserInfoFeatureVectorData())
        break

      case 'UserInfoQuestion_list_njj':
        const { loadUserInfoQuestionData } = await import('@/utils/api')
        apiCalls.push(loadUserInfoQuestionData())
        break

      case 'UserNovelRecommendation_list_njj':
        const { loadUserNovelRecommendationData } = await import('@/utils/api')
        apiCalls.push(loadUserNovelRecommendationData())
        break

      case 'UserNovelRelation_list_njj':
        const { loadUserNovelRelationData } = await import('@/utils/api')
        apiCalls.push(loadUserNovelRelationData())
        break

      case 'UserReviewBase_list_njj':
        const { loadUserReviewBaseData } = await import('@/utils/api')
        apiCalls.push(loadUserReviewBaseData())
        break

      case 'UserSettingRelation_list_njj':
        const { loadUserSettingRelationData } = await import('@/utils/api')
        apiCalls.push(loadUserSettingRelationData())
        break

      case 'UserSoftTagRelation_list_njj':
        const { loadUserSoftTagRelationData } = await import('@/utils/api')
        apiCalls.push(loadUserSoftTagRelationData())
        break

      case 'UserSystemMessage_list_njj':
        const { loadUserSystemMessageData } = await import('@/utils/api')
        apiCalls.push(loadUserSystemMessageData())
        break

      case 'UserTaskRelationship_list_njj':
        const { loadUserTaskRelationshipData } = await import('@/utils/api')
        apiCalls.push(loadUserTaskRelationshipData())
        break

      // _wlq 后缀的List类
      case 'SoftTagBase_list_wlq':
        const { loadSoftTagBaseData } = await import('@/utils/api')
        apiCalls.push(loadSoftTagBaseData())
        break

      case 'HardTagBase_list_wlq':
        const { loadHardTagBaseData } = await import('@/utils/api')
        apiCalls.push(loadHardTagBaseData())
        break

      case 'TextpostBase_list_wlq':
        const { loadTextpostBaseData } = await import('@/utils/api')
        apiCalls.push(loadTextpostBaseData())
        break

      case 'ImagePostBase_list_wlq':
        const { loadImagePostBaseData } = await import('@/utils/api')
        apiCalls.push(loadImagePostBaseData())
        break

      case 'NovelpostBase_list_wlq':
        const { loadNovelpostBaseData } = await import('@/utils/api')
        apiCalls.push(loadNovelpostBaseData())
        break

      // 可以继续添加其他List类的API调用
      default:
        console.warn(`未找到 ${className} 对应的API`)
        break
    }
  }
  
  // 并行执行所有API调用
  await Promise.all(apiCalls)
}

</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card,
.stats-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 18px;
}

.header-buttons {
  display: flex;
  gap: 10px;
}

.welcome-content {
  padding: 20px 0;
}

.feature-card {
  text-align: center;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.feature-item h3 {
  margin: 16px 0 8px;
  color: #303133;
}

.feature-item p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.entity-overview-card {
  margin-bottom: 20px;
}

.loading-container {
  padding: 20px;
}

.entity-overview {
  padding: 10px 0;
}

.entity-section {
  margin-bottom: 20px;
}

.entity-section h4 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.entity-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.entity-tag {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 8px;
}

.entity-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.memory-stats-card,
.memory-data-card {
  margin-bottom: 20px;
}

.memory-stats-card .stat-item {
  text-align: center;
  padding: 15px;
}

.memory-stats-card .stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.memory-stats-card .stat-label {
  color: #909399;
  font-size: 14px;
}

.memory-data-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

/* List类选择器样式 */
.list-loader-card {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
}

.list-selector {
  padding: 10px 0;
}

.selector-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.list-checkbox-group {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 10px;
  background-color: #fafafa;
}

.list-checkbox-item {
  display: block;
  margin-bottom: 8px;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.list-checkbox-item:hover {
  background-color: #f5f7fa;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.list-name {
  font-weight: 500;
  color: #303133;
  margin-right: 10px;
}

.list-checkbox-item .el-checkbox__label {
  width: 100%;
  padding-left: 8px;
}

/* 数据源选择器样式 */
.data-source-selector {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
}

.data-source-selector .card-header {
  font-size: 16px;
  font-weight: 600;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.data-source-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 12px;
  margin-bottom: 20px;
}

.data-source-card {
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fafafa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.data-source-card:hover {
  border-color: #409eff;
  background: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.data-source-card.selected {
  border-color: #409eff;
  background: #e6f7ff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.data-source-info h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.data-source-info p {
  margin: 0;
  font-size: 12px;
  color: #909399;
  font-family: monospace;
}

.data-source-status {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

.data-source-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.data-source-count {
  font-size: 12px;
  color: #67c23a;
  font-weight: 500;
  margin-top: 4px;
}

</style>
