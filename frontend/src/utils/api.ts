import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 60000, // 增加到60秒
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加token等认证信息
    console.log('发送请求:', config.url)
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    console.log('响应数据:', response.data)
    return response.data
  },
  (error) => {
    console.error('响应错误:', error)

    let message = '请求失败'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求资源不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = `请求失败: ${error.response.status}`
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时'
    } else if (error.message) {
      message = error.message
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// API接口定义
export const apiService = {
  // 获取表列表
  getTableList: () => api.get('/tables'),

  // 获取表数据
  getTableData: (tableName: string) => api.get(`/tables/${tableName}/data`),

  // 获取所有实体类信息
  getAllEntities: () => api.get('/entities/list'),

  // 用户数据管理
  loadUserData: () => api.post('/users/load'),
  getAllUsers: () => api.get('/users/all'),
  getUserById: (id: number) => api.get(`/users/${id}`),
  getUserStatistics: () => api.get('/users/statistics'),
  refreshUserData: () => api.post('/users/refresh'),

  // 测试接口
  testDatabase: () => api.get('/users/test-db'),
  loadUserDataPage: (page: number, size: number) => api.get(`/users/load-page?page=${page}&size=${size}`),

  // 获取好友列表
  getFriendList: () => api.get('/friends'),

  // 根据状态获取好友
  getFriendsByStatus: (status: string) => api.get(`/friends/status/${status}`),

  // 搜索好友
  searchFriends: (name: string) => api.get(`/friends/search?name=${name}`),

  // 添加好友
  addFriend: (data: any) => api.post('/friends', data),

  // 更新好友
  updateFriend: (id: number, data: any) => api.put(`/friends/${id}`, data),

  // 删除好友
  deleteFriend: (id: number) => api.delete(`/friends/${id}`),

  // 成就相关接口
  // 加载所有成就数据到内存
  loadAllAchievementData: () => api.post('/achievements/load'),

  // 加载成就基础数据到内存
  loadAchievementBaseData: () => api.post('/achievements/load-base'),

  // 加载成就记录数据到内存
  loadAchievementRecordData: () => api.post('/achievements/load-record'),

  // 获取所有成就基础数据
  getAllAchievementBase: () => api.get('/achievements/base/all'),

  // 获取所有成就记录数据
  getAllAchievementRecord: () => api.get('/achievements/record/all'),

  // 根据ID获取成就基础数据
  getAchievementBaseById: (id: number) => api.get(`/achievements/base/${id}`),

  // 根据ID获取成就记录数据
  getAchievementRecordById: (id: number) => api.get(`/achievements/record/${id}`),

  // 根据用户ID获取成就记录
  getAchievementRecordByUserId: (userId: number) => api.get(`/achievements/record/user/${userId}`),

  // 获取成就基础数据统计信息
  getAchievementBaseStatistics: () => api.get('/achievements/base/statistics'),

  // 获取成就记录数据统计信息
  getAchievementRecordStatistics: () => api.get('/achievements/record/statistics'),

  // 刷新所有成就数据缓存
  refreshAllAchievementData: () => api.post('/achievements/refresh'),

  // 刷新成就基础数据缓存
  refreshAchievementBaseData: () => api.post('/achievements/refresh-base'),

  // 刷新成就记录数据缓存
  refreshAchievementRecordData: () => api.post('/achievements/refresh-record'),

  // A开头实体类相关接口
  // 协议基础数据
  loadAgreementBaseData: () => api.post('/a-entities/agreement-base/load'),
  getAllAgreementBase: () => api.get('/a-entities/agreement-base/all'),
  getAgreementBaseStatistics: () => api.get('/a-entities/agreement-base/statistics'),

  // AI模型基础信息
  loadAimodelBaseInfoData: () => api.post('/a-entities/aimodel-base-info/load'),
  getAllAimodelBaseInfo: () => api.get('/a-entities/aimodel-base-info/all'),
  getAimodelBaseInfoStatistics: () => api.get('/a-entities/aimodel-base-info/statistics'),

  // AI模型币日志
  loadAimodelCoinLogData: () => api.post('/a-entities/aimodel-coin-log/load'),
  getAllAimodelCoinLog: () => api.get('/a-entities/aimodel-coin-log/all'),
  getAimodelCoinLogStatistics: () => api.get('/a-entities/aimodel-coin-log/statistics'),

  // AI模型等级规则
  loadAimodelLevelRuleData: () => api.post('/a-entities/aimodel-level-rule/load'),
  getAllAimodelLevelRule: () => api.get('/a-entities/aimodel-level-rule/all'),
  getAimodelLevelRuleStatistics: () => api.get('/a-entities/aimodel-level-rule/statistics'),

  // AI任务日志
  loadAiTaskLogData: () => api.post('/a-entities/ai-task-log/load'),
  getAllAiTaskLog: () => api.get('/a-entities/ai-task-log/all'),
  getAiTaskLogStatistics: () => api.get('/a-entities/ai-task-log/statistics'),

  // 加载所有A开头实体类数据
  loadAllAEntityData: () => api.post('/a-entities/load-all'),

  // B和C开头实体类相关接口
  // 屏蔽记录
  loadBlockRecordData: () => api.post('/bc-entities/block-record/load'),
  getAllBlockRecord: () => api.get('/bc-entities/block-record/all'),
  getBlockRecordStatistics: () => api.get('/bc-entities/block-record/statistics'),

  // 证书基础信息
  loadCertBaseData: () => api.post('/bc-entities/cert-base/load'),
  getAllCertBase: () => api.get('/bc-entities/cert-base/all'),
  getCertBaseStatistics: () => api.get('/bc-entities/cert-base/statistics'),

  // 证书工作基础信息
  loadCertJobBaseData: () => api.post('/bc-entities/cert-job-base/load'),
  getAllCertJobBase: () => api.get('/bc-entities/cert-job-base/all'),
  getCertJobBaseStatistics: () => api.get('/bc-entities/cert-job-base/statistics'),

  // 实名认证基础信息
  loadCertRealnameBaseData: () => api.post('/bc-entities/cert-realname-base/load'),
  getAllCertRealnameBase: () => api.get('/bc-entities/cert-realname-base/all'),
  getCertRealnameBaseStatistics: () => api.get('/bc-entities/cert-realname-base/statistics'),

  // 学生证认证基础信息
  loadCertStudentBaseData: () => api.post('/bc-entities/cert-student-base/load'),
  getAllCertStudentBase: () => api.get('/bc-entities/cert-student-base/all'),
  getCertStudentBaseStatistics: () => api.get('/bc-entities/cert-student-base/statistics'),

  // 聊天设置基础信息
  loadChatSettingsBaseData: () => api.post('/bc-entities/chat-settings-base/load'),
  getAllChatSettingsBase: () => api.get('/bc-entities/chat-settings-base/all'),
  getChatSettingsBaseStatistics: () => api.get('/bc-entities/chat-settings-base/statistics'),

  // 加载所有B和C开头实体类数据
  loadAllBCEntityData: () => api.post('/bc-entities/load-all'),

  // E、F、M、P、R、T开头实体类相关接口
  // 表情基础信息
  loadEmojiBaseData: () => api.post('/efmprt-entities/emoji-base/load'),
  getAllEmojiBase: () => api.get('/efmprt-entities/emoji-base/all'),
  getEmojiBaseStatistics: () => api.get('/efmprt-entities/emoji-base/statistics'),

  // 好友资料
  loadFriendsProfileData: () => api.post('/efmprt-entities/friends-profile/load'),
  getAllFriendsProfile: () => api.get('/efmprt-entities/friends-profile/all'),
  getFriendsProfileStatistics: () => api.get('/efmprt-entities/friends-profile/statistics'),

  // 消息扩展基础信息
  loadMessageExtraBaseData: () => api.post('/efmprt-entities/message-extra-base/load'),
  getAllMessageExtraBase: () => api.get('/efmprt-entities/message-extra-base/all'),
  getMessageExtraBaseStatistics: () => api.get('/efmprt-entities/message-extra-base/statistics'),

  // 产品基础信息
  loadProductsBaseData: () => api.post('/efmprt-entities/products-base/load'),
  getAllProductsBase: () => api.get('/efmprt-entities/products-base/all'),
  getProductsBaseStatistics: () => api.get('/efmprt-entities/products-base/statistics'),

  // 购买记录
  loadPurchaseRecordData: () => api.post('/efmprt-entities/purchase-record/load'),
  getAllPurchaseRecord: () => api.get('/efmprt-entities/purchase-record/all'),
  getPurchaseRecordStatistics: () => api.get('/efmprt-entities/purchase-record/statistics'),

  // 举报类别
  loadReportCategoryData: () => api.post('/efmprt-entities/report-category/load'),
  getAllReportCategory: () => api.get('/efmprt-entities/report-category/all'),
  getReportCategoryStatistics: () => api.get('/efmprt-entities/report-category/statistics'),

  // 举报记录
  loadReportRecordData: () => api.post('/efmprt-entities/report-record/load'),
  getAllReportRecord: () => api.get('/efmprt-entities/report-record/all'),
  getReportRecordStatistics: () => api.get('/efmprt-entities/report-record/statistics'),

  // 任务评论
  loadTaskCommentData: () => api.post('/efmprt-entities/task-comment/load'),
  getAllTaskComment: () => api.get('/efmprt-entities/task-comment/all'),
  getTaskCommentStatistics: () => api.get('/efmprt-entities/task-comment/statistics'),

  // 加载所有E、F、M、P、R、T开头实体类数据
  loadAllEFMPRTEntityData: () => api.post('/efmprt-entities/load-all'),

  // U开头实体类相关接口
  // 用户AI评论
  loadUserAiCommentData: () => api.post('/u-entities/user-ai-comment/load'),
  getAllUserAiComment: () => api.get('/u-entities/user-ai-comment/all'),
  getUserAiCommentStatistics: () => api.get('/u-entities/user-ai-comment/statistics'),

  // 用户AI模型
  loadUserAiModelData: () => api.post('/user-ai-model/load'),
  getAllUserAiModel: () => api.get('/user-ai-model/all'),
  getUserAiModelStatistics: () => api.get('/user-ai-model/statistics'),

  // 用户动态基础信息
  loadUserBaseDynamicData: () => api.post('/user-base-dynamic/load'),
  getAllUserBaseDynamic: () => api.get('/user-base-dynamic/all'),
  getUserBaseDynamicStatistics: () => api.get('/user-base-dynamic/statistics'),

  // 用户聊天详情
  loadUserChatDetailData: () => api.post('/user-chat-detail/load'),
  getAllUserChatDetail: () => api.get('/user-chat-detail/all'),
  getUserChatDetailStatistics: () => api.get('/user-chat-detail/statistics'),

  // 加载所有U开头实体类数据
  loadAllUEntityData: () => api.post('/u-entities/load-all'),

  // UserHardTagRelation 相关接口
  loadUserHardTagRelationData: () => api.post('/u-entities/user-hard-tag-relation/load'),
  getAllUserHardTagRelation: () => api.get('/u-entities/user-hard-tag-relation/all'),
  getUserHardTagRelationStatistics: () => api.get('/u-entities/user-hard-tag-relation/statistics'),

  // UserLikeRelation 相关接口
  loadUserLikeRelationData: () => api.post('/u-entities/user-like-relation/load'),
  getAllUserLikeRelation: () => api.get('/u-entities/user-like-relation/all'),
  getUserLikeRelationStatistics: () => api.get('/u-entities/user-like-relation/statistics'),

  // UserTextRecommendation 相关接口
  loadUserTextRecommendationData: () => api.post('/u-entities/user-text-recommendation/load'),
  getAllUserTextRecommendation: () => api.get('/u-entities/user-text-recommendation/all'),
  getUserTextRecommendationStatistics: () => api.get('/u-entities/user-text-recommendation/statistics'),

  // UserBaseLikeAction 相关接口
  loadUserBaseLikeActionData: () => api.post('/u-entities/user-base-like-action/load'),
  getAllUserBaseLikeAction: () => api.get('/u-entities/user-base-like-action/all'),
  getUserBaseLikeActionStatistics: () => api.get('/u-entities/user-base-like-action/statistics'),

  // UserBasePicComment 相关接口
  loadUserBasePicCommentData: () => api.post('/u-entities/user-base-pic-comment/load'),
  getAllUserBasePicComment: () => api.get('/u-entities/user-base-pic-comment/all'),
  getUserBasePicCommentStatistics: () => api.get('/u-entities/user-base-pic-comment/statistics'),

  // UserBaseSystemMessage 相关接口
  loadUserBaseSystemMessageData: () => api.post('/u-entities/user-base-system-message/load'),
  getAllUserBaseSystemMessage: () => api.get('/u-entities/user-base-system-message/all'),
  getUserBaseSystemMessageStatistics: () => api.get('/u-entities/user-base-system-message/statistics'),

  // UserBaseTextComment 相关接口
  loadUserBaseTextCommentData: () => api.post('/u-entities/user-base-text-comment/load'),
  getAllUserBaseTextComment: () => api.get('/u-entities/user-base-text-comment/all'),
  getUserBaseTextCommentStatistics: () => api.get('/u-entities/user-base-text-comment/statistics'),

  // UserBaseUserCollectioin 相关接口
  loadUserBaseUserCollectioinData: () => api.post('/u-entities/user-base-user-collection/load'),
  getAllUserBaseUserCollectioin: () => api.get('/u-entities/user-base-user-collection/all'),
  getUserBaseUserCollectioinStatistics: () => api.get('/u-entities/user-base-user-collection/statistics'),
  getUserBaseImagepostBaseStatistics: () => api.get('/user-base-imagepost-base/statistics'),

  // 新增的8个U开头实体类API
  // UserCertRecord 相关接口
  loadUserCertRecordData: () => api.post('/u-entities/user-cert-record/load'),
  getAllUserCertRecord: () => api.get('/u-entities/user-cert-record/all'),
  getUserCertRecordStatistics: () => api.get('/u-entities/user-cert-record/statistics'),

  // UserChatList 相关接口
  loadUserChatListData: () => api.post('/u-entities/user-chat-list/load'),
  getAllUserChatList: () => api.get('/u-entities/user-chat-list/all'),
  getUserChatListStatistics: () => api.get('/u-entities/user-chat-list/statistics'),

  // UserContentViewLog 相关接口
  loadUserContentViewLogData: () => api.post('/u-entities/user-content-view-log/load'),
  getAllUserContentViewLog: () => api.get('/u-entities/user-content-view-log/all'),
  getUserContentViewLogStatistics: () => api.get('/u-entities/user-content-view-log/statistics'),

  // UserDevice 相关接口
  loadUserDeviceData: () => api.post('/u-entities/user-device/load'),
  getAllUserDevice: () => api.get('/u-entities/user-device/all'),
  getUserDeviceStatistics: () => api.get('/u-entities/user-device/statistics'),

  // UserDislikeRelation 相关接口
  loadUserDislikeRelationData: () => api.post('/u-entities/user-dislike-relation/load'),
  getAllUserDislikeRelation: () => api.get('/u-entities/user-dislike-relation/all'),
  getUserDislikeRelationStatistics: () => api.get('/u-entities/user-dislike-relation/statistics'),

  // UserFriendsRelationship 相关接口
  loadUserFriendsRelationshipData: () => api.post('/u-entities/user-friends-relationship/load'),
  getAllUserFriendsRelationship: () => api.get('/u-entities/user-friends-relationship/all'),
  getUserFriendsRelationshipStatistics: () => api.get('/u-entities/user-friends-relationship/statistics'),

  // UserImageRecommendation 相关接口
  loadUserImageRecommendationData: () => api.post('/u-entities/user-image-recommendation/load'),
  getAllUserImageRecommendation: () => api.get('/u-entities/user-image-recommendation/all'),
  getUserImageRecommendationStatistics: () => api.get('/u-entities/user-image-recommendation/statistics'),

  // 新增的11个U开头实体类API
  // UserInfoFeatureVector 相关接口
  loadUserInfoFeatureVectorData: () => api.post('/u-entities/user-info-feature-vector/load'),
  getAllUserInfoFeatureVector: () => api.get('/u-entities/user-info-feature-vector/all'),
  getUserInfoFeatureVectorStatistics: () => api.get('/u-entities/user-info-feature-vector/statistics'),

  // UserInfoQuestion 相关接口
  loadUserInfoQuestionData: () => api.post('/u-entities/user-info-question/load'),
  getAllUserInfoQuestion: () => api.get('/u-entities/user-info-question/all'),
  getUserInfoQuestionStatistics: () => api.get('/u-entities/user-info-question/statistics'),

  // UserNovelRecommendation 相关接口
  loadUserNovelRecommendationData: () => api.post('/u-entities/user-novel-recommendation/load'),
  getAllUserNovelRecommendation: () => api.get('/u-entities/user-novel-recommendation/all'),
  getUserNovelRecommendationStatistics: () => api.get('/u-entities/user-novel-recommendation/statistics'),

  // UserNovelRelation 相关接口
  loadUserNovelRelationData: () => api.post('/u-entities/user-novel-relation/load'),
  getAllUserNovelRelation: () => api.get('/u-entities/user-novel-relation/all'),
  getUserNovelRelationStatistics: () => api.get('/u-entities/user-novel-relation/statistics'),

  // UserReviewBase 相关接口
  loadUserReviewBaseData: () => api.post('/u-entities/user-review-base/load'),
  getAllUserReviewBase: () => api.get('/u-entities/user-review-base/all'),
  getUserReviewBaseStatistics: () => api.get('/u-entities/user-review-base/statistics'),

  // UserSettingRelation 相关接口
  loadUserSettingRelationData: () => api.post('/u-entities/user-setting-relation/load'),
  getAllUserSettingRelation: () => api.get('/u-entities/user-setting-relation/all'),
  getUserSettingRelationStatistics: () => api.get('/u-entities/user-setting-relation/statistics'),

  // UserSoftTagRelation 相关接口
  loadUserSoftTagRelationData: () => api.post('/u-entities/user-soft-tag-relation/load'),
  getAllUserSoftTagRelation: () => api.get('/u-entities/user-soft-tag-relation/all'),
  getUserSoftTagRelationStatistics: () => api.get('/u-entities/user-soft-tag-relation/statistics'),

  // UserSystemMessage 相关接口
  loadUserSystemMessageData: () => api.post('/u-entities/user-system-message/load'),
  getAllUserSystemMessage: () => api.get('/u-entities/user-system-message/all'),
  getUserSystemMessageStatistics: () => api.get('/u-entities/user-system-message/statistics'),

  // UserTaskRelationship 相关接口
  loadUserTaskRelationshipData: () => api.post('/u-entities/user-task-relationship/load'),
  getAllUserTaskRelationship: () => api.get('/u-entities/user-task-relationship/all'),
  getUserTaskRelationshipStatistics: () => api.get('/u-entities/user-task-relationship/statistics'),

  // A开头_wlq实体类相关接口
  loadActionExpData: () => api.post('/a-entities-wlq/action-exp/load'),
  getAllActionExp: () => api.get('/a-entities-wlq/action-exp/all'),
  getActionExpStatistics: () => api.get('/a-entities-wlq/action-exp/statistics'),

  loadAiChatListDetailRData: () => api.post('/a-entities-wlq/ai-chat-list-detail-r/load'),
  getAllAiChatListDetailR: () => api.get('/a-entities-wlq/ai-chat-list-detail-r/all'),
  getAiChatListDetailRStatistics: () => api.get('/a-entities-wlq/ai-chat-list-detail-r/statistics'),

  loadAiMatchesData: () => api.post('/a-entities-wlq/ai-matches/load'),
  getAllAiMatches: () => api.get('/a-entities-wlq/ai-matches/all'),
  getAiMatchesStatistics: () => api.get('/a-entities-wlq/ai-matches/statistics'),

  loadAiTaskRequireData: () => api.post('/a-entities-wlq/ai-task-require/load'),
  getAllAiTaskRequire: () => api.get('/a-entities-wlq/ai-task-require/all'),
  getAiTaskRequireStatistics: () => api.get('/a-entities-wlq/ai-task-require/statistics'),

  loadAiTaskRespondData: () => api.post('/a-entities-wlq/ai-task-respond/load'),
  getAllAiTaskRespond: () => api.get('/a-entities-wlq/ai-task-respond/all'),
  getAiTaskRespondStatistics: () => api.get('/a-entities-wlq/ai-task-respond/statistics'),

  // 加载所有A开头_wlq实体类数据
  loadAllAEntityData_wlq: () => api.post('/a-entities-wlq/load-all'),

  // B和C开头_wlq实体类相关接口
  loadCommentLikeRelationData: () => api.post('/bc-entities-wlq/comment-like-relation/load'),
  getAllCommentLikeRelation: () => api.get('/bc-entities-wlq/comment-like-relation/all'),
  getCommentLikeRelationStatistics: () => api.get('/bc-entities-wlq/comment-like-relation/statistics'),

  loadContentDislikeRelationData: () => api.post('/bc-entities-wlq/content-dislike-relation/load'),
  getAllContentDislikeRelation: () => api.get('/bc-entities-wlq/content-dislike-relation/all'),
  getContentDislikeRelationStatistics: () => api.get('/bc-entities-wlq/content-dislike-relation/statistics'),

  loadContentFavouriteRelationData: () => api.post('/bc-entities-wlq/content-favourite-relation/load'),
  getAllContentFavouriteRelation: () => api.get('/bc-entities-wlq/content-favourite-relation/all'),
  getContentFavouriteRelationStatistics: () => api.get('/bc-entities-wlq/content-favourite-relation/statistics'),

  loadContentFeedbackRelationData: () => api.post('/bc-entities-wlq/content-feedback-relation/load'),
  getAllContentFeedbackRelation: () => api.get('/bc-entities-wlq/content-feedback-relation/all'),
  getContentFeedbackRelationStatistics: () => api.get('/bc-entities-wlq/content-feedback-relation/statistics'),

  loadContentLikeRelationData: () => api.post('/bc-entities-wlq/content-like-relation/load'),
  getAllContentLikeRelation: () => api.get('/bc-entities-wlq/content-like-relation/all'),
  getContentLikeRelationStatistics: () => api.get('/bc-entities-wlq/content-like-relation/statistics'),

  // 加载所有B和C开头_wlq实体类数据
  loadAllBCEntityData_wlq: () => api.post('/bc-entities-wlq/load-all'),

  // E、F、M、P、R、T开头_wlq实体类相关接口
  loadFriendListData: () => api.post('/efmprt-entities-wlq/friend-list/load'),
  getAllFriendList: () => api.get('/efmprt-entities-wlq/friend-list/all'),
  getFriendListStatistics: () => api.get('/efmprt-entities-wlq/friend-list/statistics'),

  loadGiftBaseData: () => api.post('/efmprt-entities-wlq/gift-base/load'),
  getAllGiftBase: () => api.get('/efmprt-entities-wlq/gift-base/all'),
  getGiftBaseStatistics: () => api.get('/efmprt-entities-wlq/gift-base/statistics'),

  // 其他E、F、M、P、R、T开头_wlq实体类接口按照相同模式添加...

  // 加载所有E、F、M、P、R、T开头_wlq实体类数据
  loadAllEFMPRTEntityData_wlq: () => api.post('/efmprt-entities-wlq/load-all'),

  // E、F、M、P、R、T开头_wlq实体类相关接口（补充软标签和帖子基础）
  loadSoftTagBaseData: () => api.post('/efmprt-entities-wlq/soft-tag-base/load'),
  getAllSoftTagBase: () => api.get('/efmprt-entities-wlq/soft-tag-base/all'),
  getSoftTagBaseStatistics: () => api.get('/efmprt-entities-wlq/soft-tag-base/statistics'),

  loadTextpostBaseData: () => api.post('/efmprt-entities-wlq/textpost-base/load'),
  getAllTextpostBase: () => api.get('/efmprt-entities-wlq/textpost-base/all'),
  getTextpostBaseStatistics: () => api.get('/efmprt-entities-wlq/textpost-base/statistics'),

  // H、I、L、N开头_wlq实体类相关接口
  loadHardTagBaseData: () => api.post('/hiln-entities-wlq/hard-tag-base/load'),
  getAllHardTagBase: () => api.get('/hiln-entities-wlq/hard-tag-base/all'),
  getHardTagBaseStatistics: () => api.get('/hiln-entities-wlq/hard-tag-base/statistics'),

  loadImagePostBaseData: () => api.post('/hiln-entities-wlq/image-post-base/load'),
  getAllImagePostBase: () => api.get('/hiln-entities-wlq/image-post-base/all'),
  getImagePostBaseStatistics: () => api.get('/hiln-entities-wlq/image-post-base/statistics'),

  loadNovelpostBaseData: () => api.post('/hiln-entities-wlq/novelpost-base/load'),
  getAllNovelpostBase: () => api.get('/hiln-entities-wlq/novelpost-base/all'),
  getNovelpostBaseStatistics: () => api.get('/hiln-entities-wlq/novelpost-base/statistics'),

  loadIDislikeData: () => api.post('/hiln-entities-wlq/i-dislike/load'),
  getAllIDislike: () => api.get('/hiln-entities-wlq/i-dislike/all'),
  getIDislikeStatistics: () => api.get('/hiln-entities-wlq/i-dislike/statistics'),

  // 加载所有H、I、L、N开头_wlq实体类数据
  loadAllHILNEntityData_wlq: () => api.post('/hiln-entities-wlq/load-all'),

  // R、S、T开头_wlq实体类相关接口
  loadRechargeData: () => api.post('/rst-entities-wlq/recharge/load'),
  getAllRecharge: () => api.get('/rst-entities-wlq/recharge/all'),
  getRechargeStatistics: () => api.get('/rst-entities-wlq/recharge/statistics'),

  loadRecommendedAvatarData: () => api.post('/rst-entities-wlq/recommended-avatar/load'),
  getAllRecommendedAvatar: () => api.get('/rst-entities-wlq/recommended-avatar/all'),
  getRecommendedAvatarStatistics: () => api.get('/rst-entities-wlq/recommended-avatar/statistics'),

  // 加载所有R、S、T开头_wlq实体类数据
  loadAllRSTEntityData_wlq: () => api.post('/rst-entities-wlq/load-all'),

  // U开头_wlq实体类相关接口
  loadUserAiRequireData: () => api.post('/u-entities-wlq/user-ai-require/load'),
  getAllUserAiRequire: () => api.get('/u-entities-wlq/user-ai-require/all'),
  getUserAiRequireStatistics: () => api.get('/u-entities-wlq/user-ai-require/statistics'),

  loadUserAiRequireFeatureVectorData: () => api.post('/u-entities-wlq/user-ai-require-feature-vector/load'),
  getAllUserAiRequireFeatureVector: () => api.get('/u-entities-wlq/user-ai-require-feature-vector/all'),
  getUserAiRequireFeatureVectorStatistics: () => api.get('/u-entities-wlq/user-ai-require-feature-vector/statistics'),

  loadUserAiRespondData: () => api.post('/u-entities-wlq/user-ai-respond/load'),
  getAllUserAiRespond: () => api.get('/u-entities-wlq/user-ai-respond/all'),
  getUserAiRespondStatistics: () => api.get('/u-entities-wlq/user-ai-respond/statistics'),

  loadUserAiRespondFeatureVectorData: () => api.post('/u-entities-wlq/user-ai-respond-feature-vector/load'),
  getAllUserAiRespondFeatureVector: () => api.get('/u-entities-wlq/user-ai-respond-feature-vector/all'),
  getUserAiRespondFeatureVectorStatistics: () => api.get('/u-entities-wlq/user-ai-respond-feature-vector/statistics'),

  loadUserBaseData: () => api.post('/u-entities-wlq/user-base/load'),
  getAllUserBase: () => api.get('/u-entities-wlq/user-base/all'),
  getUserBaseStatistics: () => api.get('/u-entities-wlq/user-base/statistics'),

  // 加载所有U开头_wlq实体类数据
  loadAllUserEntityData_wlq: () => api.post('/u-entities-wlq/load-all')
}

// 导出具体的API函数
export const getTableList = apiService.getTableList
export const getTableData = apiService.getTableData
export const getAllEntities = apiService.getAllEntities
export const loadUserData = apiService.loadUserData
export const getAllUsers = apiService.getAllUsers
export const getUserById = apiService.getUserById
export const getUserStatistics = apiService.getUserStatistics
export const refreshUserData = apiService.refreshUserData
export const testDatabase = apiService.testDatabase
export const loadUserDataPage = apiService.loadUserDataPage
export const getFriendList = apiService.getFriendList
export const getFriendsByStatus = apiService.getFriendsByStatus
export const searchFriends = apiService.searchFriends
export const addFriend = apiService.addFriend
export const updateFriend = apiService.updateFriend
export const deleteFriend = apiService.deleteFriend

// 导出成就相关API函数
export const loadAllAchievementData = apiService.loadAllAchievementData
export const loadAchievementBaseData = apiService.loadAchievementBaseData
export const loadAchievementRecordData = apiService.loadAchievementRecordData
export const getAllAchievementBase = apiService.getAllAchievementBase
export const getAllAchievementRecord = apiService.getAllAchievementRecord
export const getAchievementBaseById = apiService.getAchievementBaseById
export const getAchievementRecordById = apiService.getAchievementRecordById
export const getAchievementRecordByUserId = apiService.getAchievementRecordByUserId
export const getAchievementBaseStatistics = apiService.getAchievementBaseStatistics
export const getAchievementRecordStatistics = apiService.getAchievementRecordStatistics
export const refreshAllAchievementData = apiService.refreshAllAchievementData
export const refreshAchievementBaseData = apiService.refreshAchievementBaseData
export const refreshAchievementRecordData = apiService.refreshAchievementRecordData

// 导出A开头实体类相关API函数
export const loadAgreementBaseData = apiService.loadAgreementBaseData
export const getAllAgreementBase = apiService.getAllAgreementBase
export const getAgreementBaseStatistics = apiService.getAgreementBaseStatistics

export const loadAimodelBaseInfoData = apiService.loadAimodelBaseInfoData
export const getAllAimodelBaseInfo = apiService.getAllAimodelBaseInfo
export const getAimodelBaseInfoStatistics = apiService.getAimodelBaseInfoStatistics

export const loadAimodelCoinLogData = apiService.loadAimodelCoinLogData
export const getAllAimodelCoinLog = apiService.getAllAimodelCoinLog
export const getAimodelCoinLogStatistics = apiService.getAimodelCoinLogStatistics

export const loadAimodelLevelRuleData = apiService.loadAimodelLevelRuleData
export const getAllAimodelLevelRule = apiService.getAllAimodelLevelRule
export const getAimodelLevelRuleStatistics = apiService.getAimodelLevelRuleStatistics

export const loadAiTaskLogData = apiService.loadAiTaskLogData
export const getAllAiTaskLog = apiService.getAllAiTaskLog
export const getAiTaskLogStatistics = apiService.getAiTaskLogStatistics

export const loadAllAEntityData = apiService.loadAllAEntityData

// 导出B和C开头实体类相关API函数
export const loadBlockRecordData = apiService.loadBlockRecordData
export const getAllBlockRecord = apiService.getAllBlockRecord
export const getBlockRecordStatistics = apiService.getBlockRecordStatistics

export const loadCertBaseData = apiService.loadCertBaseData
export const getAllCertBase = apiService.getAllCertBase
export const getCertBaseStatistics = apiService.getCertBaseStatistics

export const loadCertJobBaseData = apiService.loadCertJobBaseData
export const getAllCertJobBase = apiService.getAllCertJobBase
export const getCertJobBaseStatistics = apiService.getCertJobBaseStatistics

export const loadCertRealnameBaseData = apiService.loadCertRealnameBaseData
export const getAllCertRealnameBase = apiService.getAllCertRealnameBase
export const getCertRealnameBaseStatistics = apiService.getCertRealnameBaseStatistics

export const loadCertStudentBaseData = apiService.loadCertStudentBaseData
export const getAllCertStudentBase = apiService.getAllCertStudentBase
export const getCertStudentBaseStatistics = apiService.getCertStudentBaseStatistics

export const loadChatSettingsBaseData = apiService.loadChatSettingsBaseData
export const getAllChatSettingsBase = apiService.getAllChatSettingsBase
export const getChatSettingsBaseStatistics = apiService.getChatSettingsBaseStatistics

export const loadAllBCEntityData = apiService.loadAllBCEntityData

// 导出E、F、M、P、R、T开头实体类相关API函数
export const loadEmojiBaseData = apiService.loadEmojiBaseData
export const getAllEmojiBase = apiService.getAllEmojiBase
export const getEmojiBaseStatistics = apiService.getEmojiBaseStatistics

export const loadFriendsProfileData = apiService.loadFriendsProfileData
export const getAllFriendsProfile = apiService.getAllFriendsProfile
export const getFriendsProfileStatistics = apiService.getFriendsProfileStatistics

export const loadMessageExtraBaseData = apiService.loadMessageExtraBaseData
export const getAllMessageExtraBase = apiService.getAllMessageExtraBase
export const getMessageExtraBaseStatistics = apiService.getMessageExtraBaseStatistics

export const loadProductsBaseData = apiService.loadProductsBaseData
export const getAllProductsBase = apiService.getAllProductsBase
export const getProductsBaseStatistics = apiService.getProductsBaseStatistics

export const loadPurchaseRecordData = apiService.loadPurchaseRecordData
export const getAllPurchaseRecord = apiService.getAllPurchaseRecord
export const getPurchaseRecordStatistics = apiService.getPurchaseRecordStatistics

export const loadReportCategoryData = apiService.loadReportCategoryData
export const getAllReportCategory = apiService.getAllReportCategory
export const getReportCategoryStatistics = apiService.getReportCategoryStatistics

export const loadReportRecordData = apiService.loadReportRecordData
export const getAllReportRecord = apiService.getAllReportRecord
export const getReportRecordStatistics = apiService.getReportRecordStatistics

export const loadTaskCommentData = apiService.loadTaskCommentData
export const getAllTaskComment = apiService.getAllTaskComment
export const getTaskCommentStatistics = apiService.getTaskCommentStatistics

export const loadAllEFMPRTEntityData = apiService.loadAllEFMPRTEntityData

// 导出U开头实体类相关API函数
export const loadUserAiCommentData = apiService.loadUserAiCommentData
export const getAllUserAiComment = apiService.getAllUserAiComment
export const getUserAiCommentStatistics = apiService.getUserAiCommentStatistics

export const loadUserAiModelData = apiService.loadUserAiModelData
export const getAllUserAiModel = apiService.getAllUserAiModel
export const getUserAiModelStatistics = apiService.getUserAiModelStatistics

export const loadUserBaseDynamicData = apiService.loadUserBaseDynamicData
export const getAllUserBaseDynamic = apiService.getAllUserBaseDynamic
export const getUserBaseDynamicStatistics = apiService.getUserBaseDynamicStatistics

export const loadUserChatDetailData = apiService.loadUserChatDetailData
export const getAllUserChatDetail = apiService.getAllUserChatDetail
export const getUserChatDetailStatistics = apiService.getUserChatDetailStatistics

export const loadAllUEntityData = apiService.loadAllUEntityData

// U开头实体类API
export const loadUserHardTagRelationData = apiService.loadUserHardTagRelationData
export const getAllUserHardTagRelation = apiService.getAllUserHardTagRelation
export const getUserHardTagRelationStatistics = apiService.getUserHardTagRelationStatistics

export const loadUserLikeRelationData = apiService.loadUserLikeRelationData
export const getAllUserLikeRelation = apiService.getAllUserLikeRelation
export const getUserLikeRelationStatistics = apiService.getUserLikeRelationStatistics

export const loadUserTextRecommendationData = apiService.loadUserTextRecommendationData
export const getAllUserTextRecommendation = apiService.getAllUserTextRecommendation
export const getUserTextRecommendationStatistics = apiService.getUserTextRecommendationStatistics

// UserBase开头实体类API
export const loadUserBaseLikeActionData = apiService.loadUserBaseLikeActionData
export const getAllUserBaseLikeAction = apiService.getAllUserBaseLikeAction
export const getUserBaseLikeActionStatistics = apiService.getUserBaseLikeActionStatistics

export const loadUserBasePicCommentData = apiService.loadUserBasePicCommentData
export const getAllUserBasePicComment = apiService.getAllUserBasePicComment
export const getUserBasePicCommentStatistics = apiService.getUserBasePicCommentStatistics

export const loadUserBaseSystemMessageData = apiService.loadUserBaseSystemMessageData
export const getAllUserBaseSystemMessage = apiService.getAllUserBaseSystemMessage
export const getUserBaseSystemMessageStatistics = apiService.getUserBaseSystemMessageStatistics

export const loadUserBaseTextCommentData = apiService.loadUserBaseTextCommentData
export const getAllUserBaseTextComment = apiService.getAllUserBaseTextComment
export const getUserBaseTextCommentStatistics = apiService.getUserBaseTextCommentStatistics

export const loadUserBaseUserCollectioinData = apiService.loadUserBaseUserCollectioinData
export const getAllUserBaseUserCollectioin = apiService.getAllUserBaseUserCollectioin
export const getUserBaseUserCollectioinStatistics = apiService.getUserBaseUserCollectioinStatistics
export const getUserBaseImagepostBaseStatistics = apiService.getUserBaseImagepostBaseStatistics

// 新增的8个U开头实体类API
export const loadUserCertRecordData = apiService.loadUserCertRecordData
export const getAllUserCertRecord = apiService.getAllUserCertRecord
export const getUserCertRecordStatistics = apiService.getUserCertRecordStatistics

export const loadUserChatListData = apiService.loadUserChatListData
export const getAllUserChatList = apiService.getAllUserChatList
export const getUserChatListStatistics = apiService.getUserChatListStatistics

export const loadUserContentViewLogData = apiService.loadUserContentViewLogData
export const getAllUserContentViewLog = apiService.getAllUserContentViewLog
export const getUserContentViewLogStatistics = apiService.getUserContentViewLogStatistics

export const loadUserDeviceData = apiService.loadUserDeviceData
export const getAllUserDevice = apiService.getAllUserDevice
export const getUserDeviceStatistics = apiService.getUserDeviceStatistics

export const loadUserDislikeRelationData = apiService.loadUserDislikeRelationData
export const getAllUserDislikeRelation = apiService.getAllUserDislikeRelation
export const getUserDislikeRelationStatistics = apiService.getUserDislikeRelationStatistics

export const loadUserFriendsRelationshipData = apiService.loadUserFriendsRelationshipData
export const getAllUserFriendsRelationship = apiService.getAllUserFriendsRelationship
export const getUserFriendsRelationshipStatistics = apiService.getUserFriendsRelationshipStatistics

export const loadUserImageRecommendationData = apiService.loadUserImageRecommendationData
export const getAllUserImageRecommendation = apiService.getAllUserImageRecommendation
export const getUserImageRecommendationStatistics = apiService.getUserImageRecommendationStatistics

// 新增的11个U开头实体类API导出
export const loadUserInfoFeatureVectorData = apiService.loadUserInfoFeatureVectorData
export const getAllUserInfoFeatureVector = apiService.getAllUserInfoFeatureVector
export const getUserInfoFeatureVectorStatistics = apiService.getUserInfoFeatureVectorStatistics

export const loadUserInfoQuestionData = apiService.loadUserInfoQuestionData
export const getAllUserInfoQuestion = apiService.getAllUserInfoQuestion
export const getUserInfoQuestionStatistics = apiService.getUserInfoQuestionStatistics

export const loadUserNovelRecommendationData = apiService.loadUserNovelRecommendationData
export const getAllUserNovelRecommendation = apiService.getAllUserNovelRecommendation
export const getUserNovelRecommendationStatistics = apiService.getUserNovelRecommendationStatistics

export const loadUserNovelRelationData = apiService.loadUserNovelRelationData
export const getAllUserNovelRelation = apiService.getAllUserNovelRelation
export const getUserNovelRelationStatistics = apiService.getUserNovelRelationStatistics

export const loadUserReviewBaseData = apiService.loadUserReviewBaseData
export const getAllUserReviewBase = apiService.getAllUserReviewBase
export const getUserReviewBaseStatistics = apiService.getUserReviewBaseStatistics

export const loadUserSettingRelationData = apiService.loadUserSettingRelationData
export const getAllUserSettingRelation = apiService.getAllUserSettingRelation
export const getUserSettingRelationStatistics = apiService.getUserSettingRelationStatistics

export const loadUserSoftTagRelationData = apiService.loadUserSoftTagRelationData
export const getAllUserSoftTagRelation = apiService.getAllUserSoftTagRelation
export const getUserSoftTagRelationStatistics = apiService.getUserSoftTagRelationStatistics

export const loadUserSystemMessageData = apiService.loadUserSystemMessageData
export const getAllUserSystemMessage = apiService.getAllUserSystemMessage
export const getUserSystemMessageStatistics = apiService.getUserSystemMessageStatistics

export const loadUserTaskRelationshipData = apiService.loadUserTaskRelationshipData
export const getAllUserTaskRelationship = apiService.getAllUserTaskRelationship
export const getUserTaskRelationshipStatistics = apiService.getUserTaskRelationshipStatistics

// A开头_wlq实体类API
export const loadActionExpData = apiService.loadActionExpData
export const getAllActionExp = apiService.getAllActionExp
export const getActionExpStatistics = apiService.getActionExpStatistics

export const loadAiChatListDetailRData = apiService.loadAiChatListDetailRData
export const getAllAiChatListDetailR = apiService.getAllAiChatListDetailR
export const getAiChatListDetailRStatistics = apiService.getAiChatListDetailRStatistics

export const loadAiMatchesData = apiService.loadAiMatchesData
export const getAllAiMatches = apiService.getAllAiMatches
export const getAiMatchesStatistics = apiService.getAiMatchesStatistics

export const loadAiTaskRequireData = apiService.loadAiTaskRequireData
export const getAllAiTaskRequire = apiService.getAllAiTaskRequire
export const getAiTaskRequireStatistics = apiService.getAiTaskRequireStatistics

export const loadAiTaskRespondData = apiService.loadAiTaskRespondData
export const getAllAiTaskRespond = apiService.getAllAiTaskRespond
export const getAiTaskRespondStatistics = apiService.getAiTaskRespondStatistics

export const loadAllAEntityData_wlq = apiService.loadAllAEntityData_wlq

// B和C开头_wlq实体类API
export const loadCommentLikeRelationData = apiService.loadCommentLikeRelationData
export const getAllCommentLikeRelation = apiService.getAllCommentLikeRelation
export const getCommentLikeRelationStatistics = apiService.getCommentLikeRelationStatistics

export const loadContentDislikeRelationData = apiService.loadContentDislikeRelationData
export const getAllContentDislikeRelation = apiService.getAllContentDislikeRelation
export const getContentDislikeRelationStatistics = apiService.getContentDislikeRelationStatistics

export const loadContentFavouriteRelationData = apiService.loadContentFavouriteRelationData
export const getAllContentFavouriteRelation = apiService.getAllContentFavouriteRelation
export const getContentFavouriteRelationStatistics = apiService.getContentFavouriteRelationStatistics

export const loadContentFeedbackRelationData = apiService.loadContentFeedbackRelationData
export const getAllContentFeedbackRelation = apiService.getAllContentFeedbackRelation
export const getContentFeedbackRelationStatistics = apiService.getContentFeedbackRelationStatistics

export const loadContentLikeRelationData = apiService.loadContentLikeRelationData
export const getAllContentLikeRelation = apiService.getAllContentLikeRelation
export const getContentLikeRelationStatistics = apiService.getContentLikeRelationStatistics

export const loadAllBCEntityData_wlq = apiService.loadAllBCEntityData_wlq

// E、F、M、P、R、T开头_wlq实体类API
export const loadFriendListData = apiService.loadFriendListData
export const getAllFriendList = apiService.getAllFriendList
export const getFriendListStatistics = apiService.getFriendListStatistics

export const loadGiftBaseData = apiService.loadGiftBaseData
export const getAllGiftBase = apiService.getAllGiftBase
export const getGiftBaseStatistics = apiService.getGiftBaseStatistics

export const loadAllEFMPRTEntityData_wlq = apiService.loadAllEFMPRTEntityData_wlq

// E、F、M、P、R、T开头_wlq实体类API（补充软标签和帖子基础）
export const loadSoftTagBaseData = apiService.loadSoftTagBaseData
export const getAllSoftTagBase = apiService.getAllSoftTagBase
export const getSoftTagBaseStatistics = apiService.getSoftTagBaseStatistics

export const loadTextpostBaseData = apiService.loadTextpostBaseData
export const getAllTextpostBase = apiService.getAllTextpostBase
export const getTextpostBaseStatistics = apiService.getTextpostBaseStatistics

// H、I、L、N开头_wlq实体类API
export const loadHardTagBaseData = apiService.loadHardTagBaseData
export const getAllHardTagBase = apiService.getAllHardTagBase
export const getHardTagBaseStatistics = apiService.getHardTagBaseStatistics

export const loadImagePostBaseData = apiService.loadImagePostBaseData
export const getAllImagePostBase = apiService.getAllImagePostBase
export const getImagePostBaseStatistics = apiService.getImagePostBaseStatistics

export const loadNovelpostBaseData = apiService.loadNovelpostBaseData
export const getAllNovelpostBase = apiService.getAllNovelpostBase
export const getNovelpostBaseStatistics = apiService.getNovelpostBaseStatistics

export const loadIDislikeData = apiService.loadIDislikeData
export const getAllIDislike = apiService.getAllIDislike
export const getIDislikeStatistics = apiService.getIDislikeStatistics

export const loadAllHILNEntityData_wlq = apiService.loadAllHILNEntityData_wlq

// R、S、T开头_wlq实体类API
export const loadRechargeData = apiService.loadRechargeData
export const getAllRecharge = apiService.getAllRecharge
export const getRechargeStatistics = apiService.getRechargeStatistics

export const loadRecommendedAvatarData = apiService.loadRecommendedAvatarData
export const getAllRecommendedAvatar = apiService.getAllRecommendedAvatar
export const getRecommendedAvatarStatistics = apiService.getRecommendedAvatarStatistics

export const loadAllRSTEntityData_wlq = apiService.loadAllRSTEntityData_wlq

// U开头_wlq实体类API
export const loadUserAiRequireData = apiService.loadUserAiRequireData
export const getAllUserAiRequire = apiService.getAllUserAiRequire
export const getUserAiRequireStatistics = apiService.getUserAiRequireStatistics

export const loadUserAiRequireFeatureVectorData = apiService.loadUserAiRequireFeatureVectorData
export const getAllUserAiRequireFeatureVector = apiService.getAllUserAiRequireFeatureVector
export const getUserAiRequireFeatureVectorStatistics = apiService.getUserAiRequireFeatureVectorStatistics

export const loadUserAiRespondData = apiService.loadUserAiRespondData
export const getAllUserAiRespond = apiService.getAllUserAiRespond
export const getUserAiRespondStatistics = apiService.getUserAiRespondStatistics

export const loadUserAiRespondFeatureVectorData = apiService.loadUserAiRespondFeatureVectorData
export const getAllUserAiRespondFeatureVector = apiService.getAllUserAiRespondFeatureVector
export const getUserAiRespondFeatureVectorStatistics = apiService.getUserAiRespondFeatureVectorStatistics

export const loadUserBaseData = apiService.loadUserBaseData
export const getAllUserBase = apiService.getAllUserBase
export const getUserBaseStatistics = apiService.getUserBaseStatistics

export const loadAllUserEntityData_wlq = apiService.loadAllUserEntityData_wlq

// ========== 推荐算法 API ==========
// 文字推荐算法
export const textRecGetUserTags = (userId: number) => api.get(`/text-recommendation/user-tags/${userId}`)
export const textRecGenerate = (data: any) => api.post('/text-recommendation/generate', data)
export const textRecBatchGenerate = (data: any) => api.post('/text-recommendation/batch-generate', data)
export const textRecHealth = () => api.get('/text-recommendation/health')

// 图片推荐算法
export const imageRecGetUserTags = (userId: number) => api.get(`/image-recommendation/user-tags/${userId}`)
export const imageRecGenerate = (data: any) => api.post('/image-recommendation/generate', data)
export const imageRecBatchGenerate = (data: any) => api.post('/image-recommendation/batch-generate', data)
export const imageRecHealth = () => api.get('/image-recommendation/health')

// 小说推荐算法
export const novelRecGetUserTags = (userId: number) => api.get(`/novel-recommendation/user-tags/${userId}`)
export const novelRecGenerate = (data: any) => api.post('/novel-recommendation/generate', data)
export const novelRecBatchGenerate = (data: any) => api.post('/novel-recommendation/batch-generate', data)
export const novelRecHealth = () => api.get('/novel-recommendation/health')

export default api
