package Afriends_v3.controller;

import Afriends_v3.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户个人主页控制器
 * 提供用户个人主页数据的API接口
 */
@RestController
@RequestMapping("/api/user-profile")
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== UserProfileController: 测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "用户个人主页后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户个人主页数据
     * @param userId 用户ID，默认为1000100
     * @return 用户个人主页数据
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getUserProfileData(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== UserProfileController: 收到获取用户个人主页数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> data = userProfileService.getUserProfileData(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取用户个人主页数据成功");
            response.put("data", data);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("UserProfileController: 返回用户个人主页数据成功，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("UserProfileController: 获取用户个人主页数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取用户个人主页数据失败: " + e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取用户动态数据
     * @param userId 用户ID
     * @return 用户动态数据
     */
    @GetMapping("/dynamic")
    public ResponseEntity<Map<String, Object>> getUserDynamicData(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== UserProfileController: 收到获取用户动态数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        
        try {
            Map<String, Object> data = userProfileService.getUserDynamicData(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取用户动态数据成功");
            response.put("data", data);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("UserProfileController: 返回用户动态数据成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("UserProfileController: 获取用户动态数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取用户动态数据失败: " + e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取用户收藏数据
     * @param userId 用户ID
     * @return 用户收藏数据
     */
    @GetMapping("/collections")
    public ResponseEntity<Map<String, Object>> getUserCollectionsData(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== UserProfileController: 收到获取用户收藏数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        
        try {
            Map<String, Object> data = userProfileService.getUserCollectionsData(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取用户收藏数据成功");
            response.put("data", data);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("UserProfileController: 返回用户收藏数据成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("UserProfileController: 获取用户收藏数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取用户收藏数据失败: " + e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
