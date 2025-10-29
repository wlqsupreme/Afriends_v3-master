package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import Afriends_v3.mapper.PurchaseRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * U开头实体类服务类
 */
@Service
public class UserEntityService {

    @Autowired
    private UserCertRecordMapper userCertRecordMapper;

    @Autowired
    private UserChatDetailMapper userChatDetailMapper;

    @Autowired
    private UserChatListMapper userChatListMapper;

    @Autowired
    private UserContentViewLogMapper userContentViewLogMapper;

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Autowired
    private UserDislikeRelationMapper userDislikeRelationMapper;

    @Autowired
    private UserFriendsRelationshipMapper userFriendsRelationshipMapper;

    @Autowired
    private UserImageRecommendationMapper userImageRecommendationMapper;

    @Autowired
    private UserInfoFeatureVectorMapper userInfoFeatureVectorMapper;

    @Autowired
    private UserInfoQuestionMapper userInfoQuestionMapper;

    @Autowired
    private UserNovelRecommendationMapper userNovelRecommendationMapper;

    @Autowired
    private UserNovelRelationMapper userNovelRelationMapper;

    @Autowired
    private UserReviewBaseMapper userReviewBaseMapper;

    @Autowired
    private UserSettingRelationMapper userSettingRelationMapper;

    @Autowired
    private UserSoftTagRelationMapper userSoftTagRelationMapper;

    @Autowired
    private UserSystemMessageMapper userSystemMessageMapper;

    @Autowired
    private UserTaskRelationshipMapper userTaskRelationshipMapper;

    @Autowired
    private UserAiCommentMapper userAiCommentMapper;

    @Autowired
    private UserAiModelMapper userAiModelMapper;

    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FriendsProfileMapper friendsProfileMapper;

    // UserCertRecord 相关方法
    public void loadUserCertRecordToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户认证记录数据...");
            long startTime = System.currentTimeMillis();

            var allUserCertRecord = userCertRecordMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserCertRecord.size() + " 条记录");

            if (allUserCertRecord.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户认证记录数据");
                return;
            }

            UserCertRecord_list_njj.loadFromDatabaseDirectly(allUserCertRecord);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户认证记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户认证记录数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserCertRecord_njj> getAllUserCertRecordFromMemory() {
        return UserCertRecord_list_njj.getAllUserCertRecord();
    }

    public Map<String, Object> getUserCertRecordStatisticsFromMemory() {
        return UserCertRecord_list_njj.getStatistics();
    }

    public List<UserCertRecord_njj> getUserCertRecordByUserIdFromMemory(Long userId) {
        return UserCertRecord_list_njj.getUserCertRecordByUserId(userId);
    }

    public Map<String, Object> saveUserCertRecord(Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取请求参数
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String certType = requestData.get("certType").toString();
            String certInfoJson = requestData.get("certInfo").toString();

            // 生成新的recordId（读取最大ID然后加1）
            Long maxRecordId = userCertRecordMapper.selectMaxRecordId();
            Long recordId = (maxRecordId != null) ? maxRecordId + 1 : 10000001L; // 如果表为空，从10000001开始

            // 创建认证记录对象
            UserCertRecord_njj record = new UserCertRecord_njj();
            record.setRecordId(recordId);
            record.setUserId(userId);
            record.setCertType(UserCertRecord_njj.CertType.valueOf(certType));
            record.setCertInfo(certInfoJson);
            record.setStatus((byte) 0); // 0表示待审核
            record.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            record.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int insertResult = userCertRecordMapper.insert(record);

            if (insertResult > 0) {
                // 同时更新内存缓存
                UserCertRecord_list_njj.addToCache(record);

                result.put("success", true);
                result.put("message", "认证记录保存成功");
                result.put("recordId", recordId);
                System.out.println("认证记录保存成功: recordId=" + recordId + ", userId=" + userId + ", certType=" + certType);
            } else {
                result.put("success", false);
                result.put("message", "认证记录保存失败");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存认证记录时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // UserChatDetail 相关方法
    public void loadUserChatDetailToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户聊天详情数据...");
            long startTime = System.currentTimeMillis();

            var allUserChatDetail = userChatDetailMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserChatDetail.size() + " 条记录");

            if (allUserChatDetail.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户聊天详情数据");
                return;
            }

            UserChatDetail_list_njj.loadFromDatabaseDirectly(allUserChatDetail);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户聊天详情数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户聊天详情数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserChatDetail_njj> getAllUserChatDetailFromMemory() {
        return UserChatDetail_list_njj.getAllUserChatDetail();
    }

    public Map<String, Object> getUserChatDetailStatisticsFromMemory() {
        return UserChatDetail_list_njj.getStatistics();
    }

    // UserChatList 相关方法
    public void loadUserChatListToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户聊天列表数据...");
            long startTime = System.currentTimeMillis();

            var allUserChatList = userChatListMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserChatList.size() + " 条记录");

            if (allUserChatList.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户聊天列表数据");
                return;
            }

            UserChatList_list_njj.loadFromDatabaseDirectly(allUserChatList);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户聊天列表数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户聊天列表数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserChatList_njj> getAllUserChatListFromMemory() {
        return UserChatList_list_njj.getAllUserChatList();
    }

    public Map<String, Object> getUserChatListStatisticsFromMemory() {
        return UserChatList_list_njj.getStatistics();
    }

    public List<UserChatList_njj> getUserChatListByUserIdFromMemory(Long userId) {
        List<UserChatList_njj> chatList = UserChatList_list_njj.getUserChatListByUserId(userId);
        System.out.println("获取到用户 " + userId + " 的聊天列表，共 " + chatList.size() + " 条记录");

        // 为每个聊天记录补充好友的详细信息
        for (UserChatList_njj chat : chatList) {
            if (chat.getFriendId() != null) {
                System.out.println("处理聊天记录: ID=" + chat.getId() + ", friendId=" + chat.getFriendId());

                UserInfo_njj friendInfo = UserInfo_list_njj.getUserById(chat.getFriendId());
                if (friendInfo != null) {
                    System.out.println("找到好友信息: friendId=" + chat.getFriendId() +
                            ", realName=" + friendInfo.getRealName() +
                            ", username=" + friendInfo.getUsername() +
                            ", profilePicUrl=" + friendInfo.getProfilePicUrl());

                    // 强制使用user_info表中的用户名，完全忽略数据库中的friend_name字段
                    String friendName = friendInfo.getRealName() != null && !friendInfo.getRealName().trim().isEmpty()
                            ? friendInfo.getRealName()
                            : friendInfo.getUsername() != null && !friendInfo.getUsername().trim().isEmpty()
                                    ? friendInfo.getUsername()
                                    : "未知用户";

                    // 强制设置好友名字，覆盖数据库中的friend_name字段
                    chat.setFriendName(friendName);
                    System.out.println("强制设置好友名字: " + friendName + " (来自user_info表)");

                    // 强制使用user_info表中的头像，覆盖数据库中的avatar_url字段
                    String avatarUrl = friendInfo.getProfilePicUrl() != null ? friendInfo.getProfilePicUrl() : "";
                    chat.setAvatarUrl(avatarUrl);
                    System.out.println("强制设置好友头像: " + avatarUrl + " (来自user_info表)");
                } else {
                    System.out.println("未找到好友信息: friendId=" + chat.getFriendId() + "，设置为未知用户");
                    // 如果找不到好友信息，设置为未知用户
                    chat.setFriendName("未知用户");
                }
            } else {
                System.out.println("聊天记录的friendId为空: ID=" + chat.getId());
                chat.setFriendName("未知用户");
            }
        }

        System.out.println("聊天列表处理完成，返回 " + chatList.size() + " 条记录");
        return chatList;
    }

    public boolean clearUnreadCount(Long chatId) {
        try {
            // 从内存缓存中获取聊天记录
            UserChatList_njj chat = UserChatList_list_njj.getUserChatListById(chatId);
            if (chat == null) {
                System.err.println("未找到聊天记录: chatId=" + chatId);
                return false;
            }

            // 更新未读消息数为0
            chat.setUnreadCount(0);

            // 更新数据库
            int result = userChatListMapper.updateById(chat);
            if (result > 0) {
                System.out.println("未读消息数已清零: chatId=" + chatId);
                return true;
            } else {
                System.err.println("更新数据库失败: chatId=" + chatId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("清零未读消息数异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // UserContentViewLog 相关方法
    public void loadUserContentViewLogToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户内容查看日志数据...");
            long startTime = System.currentTimeMillis();

            var allUserContentViewLog = userContentViewLogMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserContentViewLog.size() + " 条记录");

            if (allUserContentViewLog.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户内容查看日志数据");
                return;
            }

            UserContentViewLog_list_njj.loadFromDatabaseDirectly(allUserContentViewLog);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户内容查看日志数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户内容查看日志数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserContentViewLog_njj> getAllUserContentViewLogFromMemory() {
        return UserContentViewLog_list_njj.getAllUserContentViewLog();
    }

    public Map<String, Object> getUserContentViewLogStatisticsFromMemory() {
        return UserContentViewLog_list_njj.getStatistics();
    }

    // UserDevice 相关方法
    public void loadUserDeviceToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户设备数据...");
            long startTime = System.currentTimeMillis();

            var allUserDevice = userDeviceMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserDevice.size() + " 条记录");

            if (allUserDevice.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户设备数据");
                return;
            }

            UserDevice_list_njj.loadFromDatabaseDirectly(allUserDevice);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户设备数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户设备数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserDevice_njj> getAllUserDeviceFromMemory() {
        return UserDevice_list_njj.getAllUserDevice();
    }

    public Map<String, Object> getUserDeviceStatisticsFromMemory() {
        return UserDevice_list_njj.getStatistics();
    }

    // UserDislikeRelation 相关方法
    public void loadUserDislikeRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户不喜欢关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserDislikeRelation = userDislikeRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserDislikeRelation.size() + " 条记录");

            if (allUserDislikeRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户不喜欢关系数据");
                return;
            }

            UserDislikeRelation_list_njj.loadFromDatabaseDirectly(allUserDislikeRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户不喜欢关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户不喜欢关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserDislikeRelation_njj> getAllUserDislikeRelationFromMemory() {
        return UserDislikeRelation_list_njj.getAllUserDislikeRelation();
    }

    public Map<String, Object> getUserDislikeRelationStatisticsFromMemory() {
        return UserDislikeRelation_list_njj.getStatistics();
    }

    // UserFriendsRelationship 相关方法
    public void loadUserFriendsRelationshipToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户好友关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserFriendsRelationship = userFriendsRelationshipMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserFriendsRelationship.size() + " 条记录");

            if (allUserFriendsRelationship.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户好友关系数据");
                return;
            }

            UserFriendsRelationship_list_njj.loadFromDatabaseDirectly(allUserFriendsRelationship);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户好友关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户好友关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserFriendsRelationship_njj> getAllUserFriendsRelationshipFromMemory() {
        return UserFriendsRelationship_list_njj.getAllUserFriendsRelationship();
    }

    public Map<String, Object> getUserFriendsRelationshipStatisticsFromMemory() {
        return UserFriendsRelationship_list_njj.getStatistics();
    }

    public String saveUserFriendsSettings(Map<String, Object> settingsData) {
        try {
            Long userId = Long.valueOf(settingsData.get("userId").toString());
            Long friendId = Long.valueOf(settingsData.get("friendId").toString());
            Long settingId = Long.valueOf(settingsData.get("settingId").toString());
            String settingValue = (String) settingsData.get("settingValue");

            // 查找现有的设置记录
            List<UserFriendsRelationship_njj> relationships = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId);
            UserFriendsRelationship_njj existingRelationship = null;

            for (UserFriendsRelationship_njj relationship : relationships) {
                if (relationship.getFriendsId().equals(friendId) && relationship.getFunctionId().equals(settingId)) {
                    existingRelationship = relationship;
                    break;
                }
            }

            if (existingRelationship != null) {
                // 更新现有记录
                existingRelationship.setFunctionSettings(settingValue);
                existingRelationship.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                userFriendsRelationshipMapper.updateById(existingRelationship);

                // 更新内存缓存
                UserFriendsRelationship_list_njj.updateInCache(existingRelationship);

                System.out.println(
                        "更新用户好友设置成功: userId=" + userId + ", friendId=" + friendId + ", settingId=" + settingId);
                return "设置保存成功";
            } else {
                // 创建新记录
                UserFriendsRelationship_njj newRelationship = new UserFriendsRelationship_njj();
                newRelationship.setUserFriendsInfoId(generateNextUserFriendsId());
                newRelationship.setUserId(userId);
                newRelationship.setFriendsId(friendId);
                newRelationship.setFunctionId(settingId); // 使用设置项ID
                newRelationship.setFunctionSettings(settingValue);
                newRelationship.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newRelationship.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                userFriendsRelationshipMapper.insert(newRelationship);

                // 添加到内存缓存
                UserFriendsRelationship_list_njj.addToCache(newRelationship);

                System.out.println(
                        "创建用户好友设置成功: userId=" + userId + ", friendId=" + friendId + ", settingId=" + settingId);
                return "设置保存成功";
            }
        } catch (Exception e) {
            System.err.println("保存用户好友设置异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("保存设置失败: " + e.getMessage());
        }
    }

    public Map<String, Object> getUserFriendsSettings(Long userId, Long friendId) {
        try {
            List<UserFriendsRelationship_njj> relationships = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId);

            Map<String, Object> settings = new HashMap<>();
            settings.put("userId", userId);
            settings.put("friendId", friendId);

            // 初始化所有设置项为默认值
            settings.put("doNotDisturb", "[\"0\"]"); // 消息免打扰
            settings.put("pinChat", "[\"0\"]"); // 置顶聊天
            settings.put("reminders", "[\"0\"]"); // 提醒
            settings.put("chatBackground", "[\"\"]"); // 聊天背景

            // 遍历所有关系记录，找到与指定好友相关的设置
            for (UserFriendsRelationship_njj relationship : relationships) {
                if (relationship.getFriendsId().equals(friendId)) {
                    Long settingId = relationship.getFunctionId();
                    String settingValue = relationship.getFunctionSettings();

                    // 根据设置ID更新对应的设置值
                    switch (settingId.intValue()) {
                        case 10000002: // 消息免打扰
                            settings.put("doNotDisturb", settingValue);
                            break;
                        case 10000003: // 置顶聊天
                            settings.put("pinChat", settingValue);
                            break;
                        case 10000004: // 提醒
                            settings.put("reminders", settingValue);
                            break;
                        case 10000005: // 设置聊天背景
                            settings.put("chatBackground", settingValue);
                            break;
                    }
                }
            }

            return settings;
        } catch (Exception e) {
            System.err.println("获取用户好友设置异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取设置失败: " + e.getMessage());
        }
    }

    private Long generateNextUserFriendsId() {
        try {
            Long maxId = userFriendsRelationshipMapper.selectMaxId();
            return maxId != null ? maxId + 1 : 10000001L;
        } catch (Exception e) {
            System.err.println("生成用户好友关系ID失败: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    // UserImageRecommendation 相关方法
    public void loadUserImageRecommendationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户图片推荐数据...");
            long startTime = System.currentTimeMillis();

            var allUserImageRecommendation = userImageRecommendationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserImageRecommendation.size() + " 条记录");

            if (allUserImageRecommendation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户图片推荐数据");
                return;
            }

            UserImageRecommendation_list_njj.loadFromDatabaseDirectly(allUserImageRecommendation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户图片推荐数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户图片推荐数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserImageRecommendation_njj> getAllUserImageRecommendationFromMemory() {
        return UserImageRecommendation_list_njj.getAllUserImageRecommendation();
    }

    public Map<String, Object> getUserImageRecommendationStatisticsFromMemory() {
        return UserImageRecommendation_list_njj.getStatistics();
    }

    // UserInfoFeatureVector 相关方法
    public void loadUserInfoFeatureVectorToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户信息特征向量数据...");
            long startTime = System.currentTimeMillis();

            var allUserInfoFeatureVector = userInfoFeatureVectorMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserInfoFeatureVector.size() + " 条记录");

            if (allUserInfoFeatureVector.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户信息特征向量数据");
                return;
            }

            UserInfoFeatureVector_list_njj.loadFromDatabaseDirectly(allUserInfoFeatureVector);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户信息特征向量数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户信息特征向量数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserInfoFeatureVector_njj> getAllUserInfoFeatureVectorFromMemory() {
        return UserInfoFeatureVector_list_njj.getAllUserInfoFeatureVector();
    }

    public Map<String, Object> getUserInfoFeatureVectorStatisticsFromMemory() {
        return UserInfoFeatureVector_list_njj.getStatistics();
    }

    // UserInfoQuestion 相关方法
    public void loadUserInfoQuestionToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户信息问题数据...");
            long startTime = System.currentTimeMillis();

            var allUserInfoQuestion = userInfoQuestionMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserInfoQuestion.size() + " 条记录");

            if (allUserInfoQuestion.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户信息问题数据");
                return;
            }

            UserInfoQuestion_list_njj.loadFromDatabaseDirectly(allUserInfoQuestion);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户信息问题数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户信息问题数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserInfoQuestion_njj> getAllUserInfoQuestionFromMemory() {
        return UserInfoQuestion_list_njj.getAllUserInfoQuestion();
    }

    public Map<String, Object> getUserInfoQuestionStatisticsFromMemory() {
        return UserInfoQuestion_list_njj.getStatistics();
    }

    // UserNovelRecommendation 相关方法
    public void loadUserNovelRecommendationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户小说推荐数据...");
            long startTime = System.currentTimeMillis();

            var allUserNovelRecommendation = userNovelRecommendationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserNovelRecommendation.size() + " 条记录");

            if (allUserNovelRecommendation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户小说推荐数据");
                return;
            }

            UserNovelRecommendation_list_njj.loadFromDatabaseDirectly(allUserNovelRecommendation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户小说推荐数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户小说推荐数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserNovelRecommendation_njj> getAllUserNovelRecommendationFromMemory() {
        return UserNovelRecommendation_list_njj.getAllUserNovelRecommendation();
    }

    public Map<String, Object> getUserNovelRecommendationStatisticsFromMemory() {
        return UserNovelRecommendation_list_njj.getStatistics();
    }

    // UserNovelRelation 相关方法
    public void loadUserNovelRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户小说关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserNovelRelation = userNovelRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserNovelRelation.size() + " 条记录");

            if (allUserNovelRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户小说关系数据");
                return;
            }

            UserNovelRelation_list_njj.loadFromDatabaseDirectly(allUserNovelRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户小说关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户小说关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserNovelRelation_njj> getAllUserNovelRelationFromMemory() {
        return UserNovelRelation_list_njj.getAllUserNovelRelation();
    }

    public Map<String, Object> getUserNovelRelationStatisticsFromMemory() {
        return UserNovelRelation_list_njj.getStatistics();
    }

    // UserReviewBase 相关方法
    public void loadUserReviewBaseToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户评论基础信息数据...");
            long startTime = System.currentTimeMillis();

            var allUserReviewBase = userReviewBaseMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserReviewBase.size() + " 条记录");

            if (allUserReviewBase.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户评论基础信息数据");
                return;
            }

            UserReviewBase_list_njj.loadFromDatabaseDirectly(allUserReviewBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户评论基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户评论基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserReviewBase_njj> getAllUserReviewBaseFromMemory() {
        return UserReviewBase_list_njj.getAllUserReviewBase();
    }

    public Map<String, Object> getUserReviewBaseStatisticsFromMemory() {
        return UserReviewBase_list_njj.getStatistics();
    }

    // UserSettingRelation 相关方法
    public void loadUserSettingRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户设置关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserSettingRelation = userSettingRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserSettingRelation.size() + " 条记录");

            if (allUserSettingRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户设置关系数据");
                return;
            }

            UserSettingRelation_list_njj.loadFromDatabaseDirectly(allUserSettingRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户设置关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户设置关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserSettingRelation_njj> getAllUserSettingRelationFromMemory() {
        return UserSettingRelation_list_njj.getAllUserSettingRelation();
    }

    public Map<String, Object> getUserSettingRelationStatisticsFromMemory() {
        return UserSettingRelation_list_njj.getStatistics();
    }

    // UserSoftTagRelation 相关方法
    public void loadUserSoftTagRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户软标签关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserSoftTagRelation = userSoftTagRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserSoftTagRelation.size() + " 条记录");

            if (allUserSoftTagRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户软标签关系数据");
                return;
            }

            UserSoftTagRelation_list_njj.loadFromDatabaseDirectly(allUserSoftTagRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户软标签关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户软标签关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserSoftTagRelation_njj> getAllUserSoftTagRelationFromMemory() {
        return UserSoftTagRelation_list_njj.getAllUserSoftTagRelation();
    }

    public Map<String, Object> getUserSoftTagRelationStatisticsFromMemory() {
        return UserSoftTagRelation_list_njj.getStatistics();
    }

    // UserSystemMessage 相关方法
    public void loadUserSystemMessageToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户系统消息数据...");
            long startTime = System.currentTimeMillis();

            var allUserSystemMessage = userSystemMessageMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserSystemMessage.size() + " 条记录");

            if (allUserSystemMessage.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户系统消息数据");
                return;
            }

            UserSystemMessage_list_njj.loadFromDatabaseDirectly(allUserSystemMessage);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户系统消息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户系统消息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserSystemMessage_njj> getAllUserSystemMessageFromMemory() {
        return UserSystemMessage_list_njj.getAllUserSystemMessage();
    }

    public Map<String, Object> getUserSystemMessageStatisticsFromMemory() {
        return UserSystemMessage_list_njj.getStatistics();
    }

    // UserTaskRelationship 相关方法
    public void loadUserTaskRelationshipToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户任务关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserTaskRelationship = userTaskRelationshipMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserTaskRelationship.size() + " 条记录");

            if (allUserTaskRelationship.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户任务关系数据");
                return;
            }

            UserTaskRelationship_list_njj.loadFromDatabaseDirectly(allUserTaskRelationship);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户任务关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户任务关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserTaskRelationship_njj> getAllUserTaskRelationshipFromMemory() {
        return UserTaskRelationship_list_njj.getAllUserTaskRelationship();
    }

    public Map<String, Object> getUserTaskRelationshipStatisticsFromMemory() {
        return UserTaskRelationship_list_njj.getStatistics();
    }

    // UserAiComment 相关方法
    public void loadUserAiCommentToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户AI评论数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiComment = userAiCommentMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserAiComment.size() + " 条记录");

            if (allUserAiComment.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户AI评论数据");
                return;
            }

            UserAiComment_list_njj.loadFromDatabaseDirectly(allUserAiComment);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户AI评论数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户AI评论数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiComment_njj> getAllUserAiCommentFromMemory() {
        return UserAiComment_list_njj.getAllUserAiComment();
    }

    public Map<String, Object> getUserAiCommentStatisticsFromMemory() {
        return UserAiComment_list_njj.getStatistics();
    }

    public List<UserAiComment_njj> getUserAiCommentByAimodelIdFromMemory(Long aimodelId) {
        return UserAiComment_list_njj.getUserAiCommentByAimodelId(aimodelId);
    }

    public Map<String, Object> saveUserAiComment(Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long aimodelId = Long.valueOf(requestData.get("aimodelId").toString());
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String commentText = requestData.get("commentText").toString();
            Byte stars = Byte.valueOf(requestData.get("stars").toString());

            System.out.println("UserEntityService: 开始保存AI评价，aimodelId: " + aimodelId + ", userId: " + userId);

            // 创建评价对象
            UserAiComment_njj comment = new UserAiComment_njj();
            comment.setUserAiCommentId(generateNextUserAiCommentId());
            comment.setAimodelId(aimodelId);
            comment.setUserId(userId);
            comment.setCommentText(commentText);
            comment.setStars(stars);
            comment.setLikeCount(0L);
            comment.setIsVisible((byte) 1);
            comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int insertResult = userAiCommentMapper.insert(comment);
            if (insertResult > 0) {
                // 更新内存缓存
                UserAiComment_list_njj.addToCache(comment);

                result.put("success", true);
                result.put("message", "评价保存成功");
                result.put("commentId", comment.getUserAiCommentId());

                System.out.println("UserEntityService: AI评价保存成功，commentId: " + comment.getUserAiCommentId());
            } else {
                result.put("success", false);
                result.put("message", "评价保存失败，请重试");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存AI评价时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成下一个用户AI评价ID
     */
    private Long generateNextUserAiCommentId() {
        try {
            Long maxId = userAiCommentMapper.selectMaxUserAiCommentId();
            if (maxId == null) {
                return 10000001L;
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成用户AI评价ID失败: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    // UserAiModel 相关方法
    public void loadUserAiModelToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户AI模型数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiModel = userAiModelMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserAiModel.size() + " 条记录");

            if (allUserAiModel.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户AI模型数据");
                return;
            }

            UserAiModel_list_njj.loadFromDatabaseDirectly(allUserAiModel);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户AI模型数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户AI模型数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiModel_njj> getAllUserAiModelFromMemory() {
        return UserAiModel_list_njj.getAllUserAiModel();
    }

    public Map<String, Object> getUserAiModelStatisticsFromMemory() {
        return UserAiModel_list_njj.getStatistics();
    }

    /**
     * 购买AI模型
     */
    public Long purchaseAiModel(Long userId, Long parentModelId, String modelName, String modelDesc,
            String modelImageUrl, BigDecimal price) {
        try {
            // 1. 检查用户金币余额
            UserInfo_njj user = userInfoService.getUserByIdFromMemory(userId);
            if (user == null) {
                System.err.println("用户不存在: userId=" + userId);
                return null;
            }

            Long currentGold = user.getGold() != null ? user.getGold() : 0L;
            Long priceInCoins = Math.round(price.doubleValue() * 100); // 转换为金币

            if (currentGold < priceInCoins) {
                System.err.println(
                        "用户金币不足: userId=" + userId + ", currentGold=" + currentGold + ", required=" + priceInCoins);
                return null;
            }

            // 2. 生成新的userAiId
            Long userAiId = generateNextUserAiId();

            // 3. 创建用户AI模型记录
            UserAiModel_njj userAiModel = new UserAiModel_njj();
            userAiModel.setUserAiId(userAiId);
            userAiModel.setUserId(userId);
            userAiModel.setParentModelId(parentModelId);
            userAiModel.setParentModelVersion("1.0"); // 默认版本
            userAiModel.setModelName(modelName);
            userAiModel.setCustomDesc(modelDesc);
            userAiModel.setModelImageUrl(modelImageUrl);
            userAiModel.setTone("友好"); // 默认语调
            userAiModel.setQuestionWeight("1.0"); // 默认权重
            userAiModel.setQuestionContent(""); // 默认问题内容
            userAiModel.setPower(1); // 默认能力值
            userAiModel.setLevel(1); // 默认等级
            userAiModel.setTotalExp(0); // 默认经验值
            userAiModel.setRechargeAmount(price); // 充值金额
            userAiModel.setIsVisible((byte) 1); // 可见
            userAiModel.setStatus((byte) 1); // 正常状态
            userAiModel.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userAiModel.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userAiModel.setDeletedAt(null);
            userAiModel.setTryOutAt(null);
            userAiModel.setTryOutNum(0L);

            // 4. 保存AI模型到数据库
            int result = userAiModelMapper.insert(userAiModel);
            if (result <= 0) {
                System.err.println("AI模型购买失败，数据库插入失败");
                return null;
            }

            // 5. 扣除用户金币
            Long newGoldAmount = currentGold - priceInCoins;
            boolean goldUpdated = userInfoService.updateUserGold(userId, newGoldAmount);
            if (!goldUpdated) {
                System.err.println("扣除用户金币失败，回滚AI模型购买");
                // 回滚：删除已插入的AI模型记录
                userAiModelMapper.deleteById(userAiId);
                return null;
            }

            // 6. 创建购买记录
            Long recordId = generateNextPurchaseRecordId();
            PurchaseRecord_njj purchaseRecord = new PurchaseRecord_njj();
            purchaseRecord.setRecordId(recordId);
            purchaseRecord.setUserId(userId);
            purchaseRecord.setItemType("AI_MODEL");
            purchaseRecord.setItemId(userAiId);
            purchaseRecord.setItemName(modelName);
            purchaseRecord.setCoinsSpent(priceInCoins.intValue());
            purchaseRecord.setCoinsBalance(newGoldAmount.intValue());
            purchaseRecord.setPurchasePath("AI_STORE");
            purchaseRecord.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            int purchaseRecordResult = purchaseRecordMapper.insert(purchaseRecord);
            if (purchaseRecordResult <= 0) {
                System.err.println("保存购买记录失败，但购买已完成");
            }

            // 7. 添加到内存缓存
            UserAiModel_list_njj.addToCache(userAiModel);

            System.out.println("AI模型购买成功: userAiId=" + userAiId + ", userId=" + userId +
                    ", price=" + priceInCoins + ", newGold=" + newGoldAmount);
            return userAiId;

        } catch (Exception e) {
            System.err.println("购买AI模型失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成下一个userAiId
     */
    private Long generateNextUserAiId() {
        try {
            Long maxId = userAiModelMapper.selectMaxUserAiId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成userAiId失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 生成下一个购买记录ID
     */
    private Long generateNextPurchaseRecordId() {
        try {
            Long maxId = purchaseRecordMapper.selectMaxRecordId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成购买记录ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 获取朋友资料
     */
    public Map<String, Object> getFriendsProfile(Long userId, Long friendId) {
        try {
            // 从内存中查找朋友资料
            List<FriendsProfile_njj> profiles = FriendsProfile_list_njj.getFriendsProfileByUserId(userId);
            for (FriendsProfile_njj profile : profiles) {
                if (profile.getFriendId().equals(friendId)) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("friendsProfile", profile.getFriendsProfile());
                    result.put("createdAt", profile.getCreatedAt());
                    return result;
                }
            }

            // 如果内存中没有，返回空对象
            Map<String, Object> result = new HashMap<>();
            result.put("friendsProfile", "");
            return result;
        } catch (Exception e) {
            System.err.println("获取朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 保存朋友资料
     */
    public Map<String, Object> saveFriendsProfile(Map<String, Object> profileData) {
        try {
            Long userId = Long.valueOf(profileData.get("userId").toString());
            Long friendId = Long.valueOf(profileData.get("friendId").toString());
            String friendsProfile = profileData.get("friendsProfile").toString();

            // 检查是否已存在记录
            List<FriendsProfile_njj> existingProfiles = FriendsProfile_list_njj.getFriendsProfileByUserId(userId);
            FriendsProfile_njj existingProfile = null;
            for (FriendsProfile_njj profile : existingProfiles) {
                if (profile.getFriendId().equals(friendId)) {
                    existingProfile = profile;
                    break;
                }
            }

            if (existingProfile != null) {
                // 更新现有记录
                existingProfile.setFriendsProfile(friendsProfile);
                existingProfile.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                // 更新数据库
                int result = friendsProfileMapper.updateById(existingProfile);
                if (result > 0) {
                    // 更新内存缓存
                    FriendsProfile_list_njj.addToCache(existingProfile);
                    System.out.println("朋友资料更新成功: userId=" + userId + ", friendId=" + friendId);
                }
            } else {
                // 创建新记录
                Long friendsProfileId = generateNextFriendsProfileId();
                FriendsProfile_njj newProfile = new FriendsProfile_njj();
                newProfile.setFriendsProfileId(friendsProfileId);
                newProfile.setUserId(userId);
                newProfile.setFriendId(friendId);
                newProfile.setFriendsProfile(friendsProfile);
                newProfile.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                // 保存到数据库
                int result = friendsProfileMapper.insert(newProfile);
                if (result > 0) {
                    // 添加到内存缓存
                    FriendsProfile_list_njj.addToCache(newProfile);
                    System.out.println("朋友资料创建成功: userId=" + userId + ", friendId=" + friendId);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "朋友资料保存成功");
            return result;

        } catch (Exception e) {
            System.err.println("保存朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "保存朋友资料失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 生成下一个朋友资料ID
     */
    private Long generateNextFriendsProfileId() {
        try {
            Long maxId = friendsProfileMapper.selectMaxFriendsProfileId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成朋友资料ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }
}
