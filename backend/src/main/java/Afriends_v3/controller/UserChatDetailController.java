package Afriends_v3.controller;

import Afriends_v3.entity.UserChatDetail_njj;
import Afriends_v3.service.UserChatDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户聊天详情控制器
 * 提供用户聊天详情数据的API接口
 */
@RestController
@RequestMapping("/api/user-chat-detail")
@CrossOrigin(origins = "*")
public class UserChatDetailController {

    @Autowired
    private UserChatDetailService userChatDetailService;

    /**
     * 加载用户聊天详情数据到内存
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadUserChatDetailToMemory() {
        try {
            System.out.println("开始加载用户聊天详情数据到内存...");
            long startTime = System.currentTimeMillis();

            userChatDetailService.loadUserChatDetailToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户聊天详情数据加载完成，耗时: " + duration + "ms");

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

    /**
     * 获取所有用户聊天详情数据
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserChatDetail_njj>> getAllUserChatDetail() {
        try {
            List<UserChatDetail_njj> details = userChatDetailService.getAllUserChatDetailFromMemory();
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据ID获取用户聊天详情数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserChatDetail_njj> getUserChatDetailById(@PathVariable Long id) {
        try {
            UserChatDetail_njj detail = userChatDetailService.getUserChatDetailByIdFromMemory(id);
            if (detail != null) {
                return ResponseEntity.ok(detail);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据会话ID获取用户聊天详情数据
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<UserChatDetail_njj>> getUserChatDetailBySessionId(@PathVariable Long sessionId) {
        try {
            List<UserChatDetail_njj> details = userChatDetailService.getUserChatDetailBySessionIdFromMemory(sessionId);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            System.err.println("根据会话ID获取聊天详情失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据发送者类型获取用户聊天详情数据
     */
    @GetMapping("/sender-type/{senderType}")
    public ResponseEntity<List<UserChatDetail_njj>> getUserChatDetailBySenderType(@PathVariable String senderType) {
        try {
            List<UserChatDetail_njj> details = userChatDetailService
                    .getUserChatDetailBySenderTypeFromMemory(senderType);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取用户聊天详情统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserChatDetailStatistics() {
        try {
            Map<String, Object> stats = userChatDetailService.getUserChatDetailStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 保存聊天详情
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveChatDetail(@RequestBody Map<String, Object> chatData) {
        try {
            Long chatId = userChatDetailService.saveChatDetail(chatData);
            if (chatId != null) {
                Map<String, String> result = Map.of(
                        "message", "聊天记录保存成功",
                        "chatId", chatId.toString(),
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.ok(result);
            } else {
                Map<String, String> result = Map.of(
                        "message", "聊天记录保存失败",
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("保存聊天记录失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> result = Map.of(
                    "message", "保存聊天记录失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 强制刷新用户聊天详情数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshUserChatDetailData() {
        try {
            System.out.println("开始刷新用户聊天详情数据...");
            long startTime = System.currentTimeMillis();

            userChatDetailService.refreshUserChatDetailData();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户聊天详情数据刷新完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据刷新成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("刷新用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }
}
