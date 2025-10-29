package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * U开头实体类控制器
 * 提供所有U开头实体类数据的API接口
 */
@RestController
@RequestMapping("/api/u-entities")
@CrossOrigin(origins = "*")
public class UserEntityController {

    @Autowired
    private UserHardTagRelationService userHardTagRelationService;

    @Autowired
    private UserLikeRelationService userLikeRelationService;

    @Autowired
    private UserTextRecommendationService userTextRecommendationService;

    @Autowired
    private UserBaseEntityService userBaseEntityService;

    @Autowired
    private UserEntityService userEntityService;

    // UserHardTagRelation 相关接口
    @PostMapping("/user-hard-tag-relation/load")
    public ResponseEntity<Map<String, String>> loadUserHardTagRelationToMemory() {
        try {
            System.out.println("开始加载用户硬标签关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userHardTagRelationService.loadUserHardTagRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户硬标签关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户硬标签关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户硬标签关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户硬标签关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-hard-tag-relation/all")
    public ResponseEntity<List<UserHardTagRelation_njj>> getAllUserHardTagRelation() {
        try {
            List<UserHardTagRelation_njj> relations = userHardTagRelationService.getAllUserHardTagRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-hard-tag-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserHardTagRelationStatistics() {
        try {
            Map<String, Object> stats = userHardTagRelationService.getUserHardTagRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserLikeRelation 相关接口
    @PostMapping("/user-like-relation/load")
    public ResponseEntity<Map<String, String>> loadUserLikeRelationToMemory() {
        try {
            System.out.println("开始加载用户喜欢关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userLikeRelationService.loadUserLikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户喜欢关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户喜欢关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户喜欢关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-like-relation/all")
    public ResponseEntity<List<UserLikeRelation_njj>> getAllUserLikeRelation() {
        try {
            List<UserLikeRelation_njj> relations = userLikeRelationService.getAllUserLikeRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-like-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserLikeRelationStatistics() {
        try {
            Map<String, Object> stats = userLikeRelationService.getUserLikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserTextRecommendation 相关接口
    @PostMapping("/user-text-recommendation/load")
    public ResponseEntity<Map<String, String>> loadUserTextRecommendationToMemory() {
        try {
            System.out.println("开始加载用户文本推荐数据到内存...");
            long startTime = System.currentTimeMillis();

            userTextRecommendationService.loadUserTextRecommendationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户文本推荐数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户文本推荐数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户文本推荐数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户文本推荐数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-text-recommendation/all")
    public ResponseEntity<List<UserTextRecommendation_njj>> getAllUserTextRecommendation() {
        try {
            List<UserTextRecommendation_njj> recommendations = userTextRecommendationService
                    .getAllUserTextRecommendationFromMemory();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-text-recommendation/statistics")
    public ResponseEntity<Map<String, Object>> getUserTextRecommendationStatistics() {
        try {
            Map<String, Object> stats = userTextRecommendationService.getUserTextRecommendationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseLikeAction 相关接口
    @PostMapping("/user-base-like-action/load")
    public ResponseEntity<Map<String, String>> loadUserBaseLikeActionToMemory() {
        try {
            System.out.println("开始加载用户基础点赞行为数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseLikeActionToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础点赞行为数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础点赞行为数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础点赞行为数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-like-action/all")
    public ResponseEntity<List<UserBaseLikeAction_njj>> getAllUserBaseLikeAction() {
        try {
            List<UserBaseLikeAction_njj> actions = userBaseEntityService.getAllUserBaseLikeActionFromMemory();
            return ResponseEntity.ok(actions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-like-action/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseLikeActionStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseLikeActionStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBasePicComment 相关接口
    @PostMapping("/user-base-pic-comment/load")
    public ResponseEntity<Map<String, String>> loadUserBasePicCommentToMemory() {
        try {
            System.out.println("开始加载用户基础图片评论数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBasePicCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础图片评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础图片评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础图片评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-pic-comment/all")
    public ResponseEntity<List<UserBasePicComment_njj>> getAllUserBasePicComment() {
        try {
            List<UserBasePicComment_njj> comments = userBaseEntityService.getAllUserBasePicCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-pic-comment/statistics")
    public ResponseEntity<Map<String, Object>> getUserBasePicCommentStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBasePicCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseSystemMessage 相关接口
    @PostMapping("/user-base-system-message/load")
    public ResponseEntity<Map<String, String>> loadUserBaseSystemMessageToMemory() {
        try {
            System.out.println("开始加载用户基础系统消息数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseSystemMessageToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础系统消息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础系统消息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础系统消息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-system-message/all")
    public ResponseEntity<List<UserBaseSystemMessage_njj>> getAllUserBaseSystemMessage() {
        try {
            List<UserBaseSystemMessage_njj> messages = userBaseEntityService.getAllUserBaseSystemMessageFromMemory();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-system-message/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseSystemMessageStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseSystemMessageStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseTextComment 相关接口
    @PostMapping("/user-base-text-comment/load")
    public ResponseEntity<Map<String, String>> loadUserBaseTextCommentToMemory() {
        try {
            System.out.println("开始加载用户基础文本评论数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseTextCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础文本评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础文本评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础文本评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-text-comment/all")
    public ResponseEntity<List<UserBaseTextComment_njj>> getAllUserBaseTextComment() {
        try {
            List<UserBaseTextComment_njj> comments = userBaseEntityService.getAllUserBaseTextCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-text-comment/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseTextCommentStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseTextCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseUserCollectioin 相关接口
    @PostMapping("/user-base-user-collection/load")
    public ResponseEntity<Map<String, String>> loadUserBaseUserCollectioinToMemory() {
        try {
            System.out.println("开始加载用户基础用户收藏数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseUserCollectioinToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础用户收藏数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础用户收藏数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础用户收藏数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-user-collection/all")
    public ResponseEntity<List<UserBaseUserCollectioin_njj>> getAllUserBaseUserCollectioin() {
        try {
            List<UserBaseUserCollectioin_njj> collections = userBaseEntityService
                    .getAllUserBaseUserCollectioinFromMemory();
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-user-collection/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseUserCollectioinStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseUserCollectioinStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserCertRecord 相关接口
    @PostMapping("/user-cert-record/load")
    public ResponseEntity<Map<String, String>> loadUserCertRecordToMemory() {
        try {
            System.out.println("开始加载用户认证记录数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserCertRecordToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户认证记录数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户认证记录数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户认证记录数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-cert-record/all")
    public ResponseEntity<List<UserCertRecord_njj>> getAllUserCertRecord() {
        try {
            List<UserCertRecord_njj> records = userEntityService.getAllUserCertRecordFromMemory();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-cert-record/user/{userId}")
    public ResponseEntity<List<UserCertRecord_njj>> getUserCertRecordByUserId(@PathVariable Long userId) {
        try {
            List<UserCertRecord_njj> records = userEntityService.getUserCertRecordByUserIdFromMemory(userId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-cert-record/statistics")
    public ResponseEntity<Map<String, Object>> getUserCertRecordStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserCertRecordStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-cert-record/save")
    public ResponseEntity<Map<String, Object>> saveUserCertRecord(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> result = userEntityService.saveUserCertRecord(requestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存认证记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // UserChatDetail 相关接口
    @PostMapping("/user-chat-detail/load")
    public ResponseEntity<Map<String, String>> loadUserChatDetailToMemory() {
        try {
            System.out.println("开始加载用户聊天详情数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserChatDetailToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-chat-detail/all")
    public ResponseEntity<List<UserChatDetail_njj>> getAllUserChatDetail() {
        try {
            List<UserChatDetail_njj> details = userEntityService.getAllUserChatDetailFromMemory();
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-chat-detail/statistics")
    public ResponseEntity<Map<String, Object>> getUserChatDetailStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserChatDetailStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserChatList 相关接口
    @PostMapping("/user-chat-list/load")
    public ResponseEntity<Map<String, String>> loadUserChatListToMemory() {
        try {
            System.out.println("开始加载用户聊天列表数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserChatListToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户聊天列表数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天列表数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-chat-list/all")
    public ResponseEntity<List<UserChatList_njj>> getAllUserChatList() {
        try {
            List<UserChatList_njj> lists = userEntityService.getAllUserChatListFromMemory();
            return ResponseEntity.ok(lists);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-chat-list/statistics")
    public ResponseEntity<Map<String, Object>> getUserChatListStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserChatListStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-chat-list/user/{userId}")
    public ResponseEntity<List<UserChatList_njj>> getUserChatListByUserId(@PathVariable Long userId) {
        try {
            List<UserChatList_njj> lists = userEntityService.getUserChatListByUserIdFromMemory(userId);
            return ResponseEntity.ok(lists);
        } catch (Exception e) {
            System.err.println("获取用户聊天列表失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-chat-list/clear-unread/{chatId}")
    public ResponseEntity<Map<String, String>> clearUnreadCount(@PathVariable Long chatId) {
        try {
            boolean success = userEntityService.clearUnreadCount(chatId);
            if (success) {
                Map<String, String> result = Map.of(
                        "message", "未读消息数已清零",
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.ok(result);
            } else {
                Map<String, String> result = Map.of(
                        "message", "清零未读消息数失败",
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("清零未读消息数失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> result = Map.of(
                    "message", "清零未读消息数失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    // UserContentViewLog 相关接口
    @PostMapping("/user-content-view-log/load")
    public ResponseEntity<Map<String, String>> loadUserContentViewLogToMemory() {
        try {
            System.out.println("开始加载用户内容查看日志数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserContentViewLogToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户内容查看日志数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户内容查看日志数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户内容查看日志数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-content-view-log/all")
    public ResponseEntity<List<UserContentViewLog_njj>> getAllUserContentViewLog() {
        try {
            List<UserContentViewLog_njj> logs = userEntityService.getAllUserContentViewLogFromMemory();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-content-view-log/statistics")
    public ResponseEntity<Map<String, Object>> getUserContentViewLogStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserContentViewLogStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserDevice 相关接口
    @PostMapping("/user-device/load")
    public ResponseEntity<Map<String, String>> loadUserDeviceToMemory() {
        try {
            System.out.println("开始加载用户设备数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserDeviceToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户设备数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户设备数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户设备数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-device/all")
    public ResponseEntity<List<UserDevice_njj>> getAllUserDevice() {
        try {
            List<UserDevice_njj> devices = userEntityService.getAllUserDeviceFromMemory();
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-device/statistics")
    public ResponseEntity<Map<String, Object>> getUserDeviceStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserDeviceStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserDislikeRelation 相关接口
    @PostMapping("/user-dislike-relation/load")
    public ResponseEntity<Map<String, String>> loadUserDislikeRelationToMemory() {
        try {
            System.out.println("开始加载用户不喜欢关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserDislikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户不喜欢关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户不喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户不喜欢关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-dislike-relation/all")
    public ResponseEntity<List<UserDislikeRelation_njj>> getAllUserDislikeRelation() {
        try {
            List<UserDislikeRelation_njj> relations = userEntityService.getAllUserDislikeRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-dislike-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserDislikeRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserDislikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserFriendsRelationship 相关接口
    @PostMapping("/user-friends-relationship/load")
    public ResponseEntity<Map<String, String>> loadUserFriendsRelationshipToMemory() {
        try {
            System.out.println("开始加载用户好友关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserFriendsRelationshipToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户好友关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户好友关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户好友关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-friends-relationship/all")
    public ResponseEntity<List<UserFriendsRelationship_njj>> getAllUserFriendsRelationship() {
        try {
            List<UserFriendsRelationship_njj> relationships = userEntityService
                    .getAllUserFriendsRelationshipFromMemory();
            return ResponseEntity.ok(relationships);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-friends-relationship/statistics")
    public ResponseEntity<Map<String, Object>> getUserFriendsRelationshipStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserFriendsRelationshipStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-friends-relationship/save-settings")
    public ResponseEntity<Map<String, String>> saveUserFriendsSettings(@RequestBody Map<String, Object> settingsData) {
        try {
            String result = userEntityService.saveUserFriendsSettings(settingsData);
            Map<String, String> response = new HashMap<>();
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("保存用户好友设置失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "保存设置失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/user-friends-relationship/settings/{userId}/{friendId}")
    public ResponseEntity<Map<String, Object>> getUserFriendsSettings(@PathVariable Long userId,
            @PathVariable Long friendId) {
        try {
            Map<String, Object> settings = userEntityService.getUserFriendsSettings(userId, friendId);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            System.err.println("获取用户好友设置失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserImageRecommendation 相关接口
    @PostMapping("/user-image-recommendation/load")
    public ResponseEntity<Map<String, String>> loadUserImageRecommendationToMemory() {
        try {
            System.out.println("开始加载用户图片推荐数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserImageRecommendationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户图片推荐数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户图片推荐数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户图片推荐数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-image-recommendation/all")
    public ResponseEntity<List<UserImageRecommendation_njj>> getAllUserImageRecommendation() {
        try {
            List<UserImageRecommendation_njj> recommendations = userEntityService
                    .getAllUserImageRecommendationFromMemory();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-image-recommendation/statistics")
    public ResponseEntity<Map<String, Object>> getUserImageRecommendationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserImageRecommendationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserInfoFeatureVector 相关接口
    @PostMapping("/user-info-feature-vector/load")
    public ResponseEntity<Map<String, String>> loadUserInfoFeatureVectorToMemory() {
        try {
            System.out.println("开始加载用户信息特征向量数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserInfoFeatureVectorToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户信息特征向量数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户信息特征向量数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户信息特征向量数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-info-feature-vector/all")
    public ResponseEntity<List<UserInfoFeatureVector_njj>> getAllUserInfoFeatureVector() {
        try {
            List<UserInfoFeatureVector_njj> vectors = userEntityService.getAllUserInfoFeatureVectorFromMemory();
            return ResponseEntity.ok(vectors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-info-feature-vector/statistics")
    public ResponseEntity<Map<String, Object>> getUserInfoFeatureVectorStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserInfoFeatureVectorStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserInfoQuestion 相关接口
    @PostMapping("/user-info-question/load")
    public ResponseEntity<Map<String, String>> loadUserInfoQuestionToMemory() {
        try {
            System.out.println("开始加载用户信息问题数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserInfoQuestionToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户信息问题数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户信息问题数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户信息问题数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-info-question/all")
    public ResponseEntity<List<UserInfoQuestion_njj>> getAllUserInfoQuestion() {
        try {
            List<UserInfoQuestion_njj> questions = userEntityService.getAllUserInfoQuestionFromMemory();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-info-question/statistics")
    public ResponseEntity<Map<String, Object>> getUserInfoQuestionStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserInfoQuestionStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserNovelRecommendation 相关接口
    @PostMapping("/user-novel-recommendation/load")
    public ResponseEntity<Map<String, String>> loadUserNovelRecommendationToMemory() {
        try {
            System.out.println("开始加载用户小说推荐数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserNovelRecommendationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户小说推荐数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户小说推荐数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户小说推荐数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-novel-recommendation/all")
    public ResponseEntity<List<UserNovelRecommendation_njj>> getAllUserNovelRecommendation() {
        try {
            List<UserNovelRecommendation_njj> recommendations = userEntityService
                    .getAllUserNovelRecommendationFromMemory();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-novel-recommendation/statistics")
    public ResponseEntity<Map<String, Object>> getUserNovelRecommendationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserNovelRecommendationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserNovelRelation 相关接口
    @PostMapping("/user-novel-relation/load")
    public ResponseEntity<Map<String, String>> loadUserNovelRelationToMemory() {
        try {
            System.out.println("开始加载用户小说关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserNovelRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户小说关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户小说关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户小说关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-novel-relation/all")
    public ResponseEntity<List<UserNovelRelation_njj>> getAllUserNovelRelation() {
        try {
            List<UserNovelRelation_njj> relations = userEntityService.getAllUserNovelRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-novel-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserNovelRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserNovelRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserReviewBase 相关接口
    @PostMapping("/user-review-base/load")
    public ResponseEntity<Map<String, String>> loadUserReviewBaseToMemory() {
        try {
            System.out.println("开始加载用户评论基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserReviewBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户评论基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户评论基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户评论基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-review-base/all")
    public ResponseEntity<List<UserReviewBase_njj>> getAllUserReviewBase() {
        try {
            List<UserReviewBase_njj> reviews = userEntityService.getAllUserReviewBaseFromMemory();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-review-base/statistics")
    public ResponseEntity<Map<String, Object>> getUserReviewBaseStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserReviewBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserSettingRelation 相关接口
    @PostMapping("/user-setting-relation/load")
    public ResponseEntity<Map<String, String>> loadUserSettingRelationToMemory() {
        try {
            System.out.println("开始加载用户设置关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserSettingRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户设置关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户设置关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户设置关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-setting-relation/all")
    public ResponseEntity<List<UserSettingRelation_njj>> getAllUserSettingRelation() {
        try {
            List<UserSettingRelation_njj> relations = userEntityService.getAllUserSettingRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-setting-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserSettingRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserSettingRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserSoftTagRelation 相关接口
    @PostMapping("/user-soft-tag-relation/load")
    public ResponseEntity<Map<String, String>> loadUserSoftTagRelationToMemory() {
        try {
            System.out.println("开始加载用户软标签关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserSoftTagRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户软标签关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户软标签关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户软标签关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-soft-tag-relation/all")
    public ResponseEntity<List<UserSoftTagRelation_njj>> getAllUserSoftTagRelation() {
        try {
            List<UserSoftTagRelation_njj> relations = userEntityService.getAllUserSoftTagRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-soft-tag-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserSoftTagRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserSoftTagRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserSystemMessage 相关接口
    @PostMapping("/user-system-message/load")
    public ResponseEntity<Map<String, String>> loadUserSystemMessageToMemory() {
        try {
            System.out.println("开始加载用户系统消息数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserSystemMessageToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户系统消息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户系统消息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户系统消息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-system-message/all")
    public ResponseEntity<List<UserSystemMessage_njj>> getAllUserSystemMessage() {
        try {
            List<UserSystemMessage_njj> messages = userEntityService.getAllUserSystemMessageFromMemory();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-system-message/statistics")
    public ResponseEntity<Map<String, Object>> getUserSystemMessageStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserSystemMessageStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserTaskRelationship 相关接口
    @PostMapping("/user-task-relationship/load")
    public ResponseEntity<Map<String, String>> loadUserTaskRelationshipToMemory() {
        try {
            System.out.println("开始加载用户任务关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserTaskRelationshipToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户任务关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户任务关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户任务关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-task-relationship/all")
    public ResponseEntity<List<UserTaskRelationship_njj>> getAllUserTaskRelationship() {
        try {
            List<UserTaskRelationship_njj> relationships = userEntityService.getAllUserTaskRelationshipFromMemory();
            return ResponseEntity.ok(relationships);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-task-relationship/statistics")
    public ResponseEntity<Map<String, Object>> getUserTaskRelationshipStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserTaskRelationshipStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserAiComment 相关接口
    @PostMapping("/user-ai-comment/load")
    public ResponseEntity<Map<String, String>> loadUserAiCommentToMemory() {
        try {
            System.out.println("开始加载用户AI评论数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserAiCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-comment/all")
    public ResponseEntity<List<UserAiComment_njj>> getAllUserAiComment() {
        try {
            List<UserAiComment_njj> comments = userEntityService.getAllUserAiCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-comment/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiCommentStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserAiCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-comment/aimodel/{aimodelId}")
    public ResponseEntity<List<UserAiComment_njj>> getUserAiCommentByAimodelId(@PathVariable Long aimodelId) {
        try {
            List<UserAiComment_njj> comments = userEntityService.getUserAiCommentByAimodelIdFromMemory(aimodelId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-ai-comment/save")
    public ResponseEntity<Map<String, Object>> saveUserAiComment(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> result = userEntityService.saveUserAiComment(requestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存AI评价失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // UserAiModel 相关接口
    @PostMapping("/user-ai-model/load")
    public ResponseEntity<Map<String, String>> loadUserAiModelToMemory() {
        try {
            System.out.println("开始加载用户AI模型数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserAiModelToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI模型数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-model/all")
    public ResponseEntity<List<UserAiModel_njj>> getAllUserAiModel() {
        try {
            List<UserAiModel_njj> models = userEntityService.getAllUserAiModelFromMemory();
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-model/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiModelStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserAiModelStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-ai-model/purchase")
    public ResponseEntity<Map<String, Object>> purchaseAiModel(@RequestBody Map<String, Object> requestData) {
        try {
            // 获取请求参数
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long parentModelId = Long.valueOf(requestData.get("parentModelId").toString());
            String modelName = requestData.get("modelName").toString();
            String modelDesc = requestData.get("modelDesc").toString();
            String modelImageUrl = requestData.get("modelImageUrl").toString();
            Double price = Double.valueOf(requestData.get("price").toString());

            // 调用服务层购买方法
            Long userAiId = userEntityService.purchaseAiModel(
                    userId,
                    parentModelId,
                    modelName,
                    modelDesc,
                    modelImageUrl,
                    java.math.BigDecimal.valueOf(price));

            Map<String, Object> result = new HashMap<>();
            if (userAiId != null) {
                result.put("success", true);
                result.put("userAiId", userAiId);
                result.put("message", "AI模型购买成功");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "AI模型购买失败");
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("购买AI模型失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "购买失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // FriendsProfile 相关接口
    @GetMapping("/friends-profile/{userId}/{friendId}")
    public ResponseEntity<Map<String, Object>> getFriendsProfile(@PathVariable Long userId,
            @PathVariable Long friendId) {
        try {
            Map<String, Object> profile = userEntityService.getFriendsProfile(userId, friendId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            System.err.println("获取朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/friends-profile/save")
    public ResponseEntity<Map<String, Object>> saveFriendsProfile(@RequestBody Map<String, Object> profileData) {
        try {
            Map<String, Object> result = userEntityService.saveFriendsProfile(profileData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("保存朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存朋友资料失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }
}
