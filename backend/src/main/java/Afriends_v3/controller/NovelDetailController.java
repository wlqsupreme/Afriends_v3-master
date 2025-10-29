package Afriends_v3.controller;

import Afriends_v3.service.NovelDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小说详情页面控制器
 * 提供小说详情数据的API接口
 */
@RestController
@RequestMapping("/api/novel-detail")
@CrossOrigin(origins = "*")
public class NovelDetailController {

    @Autowired
    private NovelDetailService novelDetailService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 小说详情测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "小说详情后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取小说详情数据
     * @param novelId 小说ID
     * @param userId 用户ID，默认为1000100
     * @return 小说详情数据
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getNovelDetail(
            @RequestParam("novelId") Long novelId,
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到获取小说详情请求 ===");
        System.out.println("小说ID: " + novelId);
        System.out.println("用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> novelDetail = novelDetailService.getNovelDetail(novelId, userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取小说详情成功");
            response.put("data", novelDetail);
            response.put("novelId", novelId);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回小说详情成功，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取小说详情失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取小说详情失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("novelId", novelId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取小说章节列表
     * @param novelId 小说ID
     * @return 章节列表
     */
    @GetMapping("/chapters")
    public ResponseEntity<Map<String, Object>> getNovelChapters(
            @RequestParam("novelId") Long novelId) {
        
        System.out.println("=== NovelDetailController: 收到获取小说章节请求 ===");
        System.out.println("小说ID: " + novelId);
        
        try {
            java.util.List<Map<String, Object>> chapters = novelDetailService.getNovelChapters(novelId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取小说章节成功");
            response.put("data", chapters);
            response.put("count", chapters.size());
            response.put("novelId", novelId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回小说章节成功，共 " + chapters.size() + " 章");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取小说章节失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取小说章节失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("novelId", novelId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取指定章节内容
     * @param chapterId 章节ID
     * @return 章节内容
     */
    @GetMapping("/chapter")
    public ResponseEntity<Map<String, Object>> getChapterContent(
            @RequestParam("chapterId") Long chapterId) {
        
        System.out.println("=== NovelDetailController: 收到获取章节内容请求 ===");
        System.out.println("章节ID: " + chapterId);
        
        try {
            Map<String, Object> chapterContent = novelDetailService.getChapterContent(chapterId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取章节内容成功");
            response.put("data", chapterContent);
            response.put("chapterId", chapterId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回章节内容成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取章节内容失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取章节内容失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("chapterId", chapterId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取小说书评列表
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 书评列表
     */
    @GetMapping("/reviews")
    public ResponseEntity<Map<String, Object>> getNovelReviews(
            @RequestParam("novelId") Long novelId,
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到获取小说书评请求 ===");
        System.out.println("小说ID: " + novelId);
        System.out.println("用户ID: " + userId);
        
        try {
            List<Map<String, Object>> reviews = novelDetailService.getNovelReviews(novelId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取小说书评成功");
            response.put("data", reviews);
            response.put("count", reviews.size());
            response.put("novelId", novelId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回小说书评成功，共 " + reviews.size() + " 条");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取小说书评失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取小说书评失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("novelId", novelId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 处理评论点赞/取消点赞
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 操作结果
     */
    @PostMapping("/comment/like")
    public ResponseEntity<Map<String, Object>> handleCommentLike(
            @RequestParam("userId") Long userId,
            @RequestParam("commentId") Long commentId) {
        
        System.out.println("=== NovelDetailController: 收到评论点赞请求 ===");
        System.out.println("用户ID: " + userId);
        System.out.println("评论ID: " + commentId);
        
        try {
            Map<String, Object> result = novelDetailService.handleCommentLike(userId, commentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("userId", userId);
            response.put("commentId", commentId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 评论点赞处理完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理评论点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "处理评论点赞失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("userId", userId);
            errorResponse.put("commentId", commentId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
