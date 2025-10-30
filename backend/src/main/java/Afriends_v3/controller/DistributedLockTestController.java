package Afriends_v3.controller;

import Afriends_v3.service.UserEntityService_wlq;
import Afriends_v3.service.common.DistributedLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 分布式锁测试控制器
 * 提供测试接口验证分布式锁功能
 */
@RestController
@RequestMapping("/api/distributed-lock")
@CrossOrigin(origins = "*")
public class DistributedLockTestController {

    @Autowired
    private UserEntityService_wlq userEntityService_wlq;

    @Autowired
    private DistributedLockService distributedLockService;

    /**
     * 测试分布式锁的用户注册（使用自动加锁方式）
     */
    @PostMapping("/test/register/auto")
    public ResponseEntity<Map<String, Object>> testRegisterWithAutoLock(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String phone) {
        
        System.out.println("=== 测试分布式锁注册（自动加锁方式）===");
        System.out.println("用户名: " + username + ", 密码: " + password);
        
        try {
            Map<String, Object> result = userEntityService_wlq.registerUserWithLock(username, password, phone);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "测试失败: " + e.getMessage());
            errorResult.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    /**
     * 测试分布式锁的用户注册（使用手动加锁方式）
     */
    @PostMapping("/test/register/manual")
    public ResponseEntity<Map<String, Object>> testRegisterWithManualLock(
            @RequestParam String username,
            @RequestParam String password) {
        
        System.out.println("=== 测试分布式锁注册（手动加锁方式）===");
        System.out.println("用户名: " + username + ", 密码: " + password);
        
        try {
            Map<String, Object> result = userEntityService_wlq.registerUserWithManualLock(username, password);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "测试失败: " + e.getMessage());
            errorResult.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    /**
     * 测试简单的分布式锁获取和释放
     */
    @PostMapping("/test/lock/basic")
    public ResponseEntity<Map<String, Object>> testBasicLock(@RequestParam String lockName) {
        System.out.println("=== 测试基础分布式锁 ===");
        System.out.println("锁名称: " + lockName);
        
        Map<String, Object> result = new HashMap<>();
        
        DistributedLockService.LockContext lockContext = null;
        try {
            // 尝试获取锁
            lockContext = distributedLockService.tryLockWithRetry(lockName, 10, 5);
            
            if (lockContext == null) {
                result.put("success", false);
                result.put("message", "获取锁失败");
                return ResponseEntity.status(429).body(result);
            }
            
            result.put("success", true);
            result.put("message", "获取锁成功");
            result.put("lockKey", lockContext.getLockKey());
            result.put("lockValue", lockContext.getLockValue());
            
            // 模拟业务耗时
            Thread.sleep(1000);
            
            return ResponseEntity.ok(result);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.put("success", false);
            result.put("message", "测试被中断");
            return ResponseEntity.status(500).body(result);
        } finally {
            // 释放锁
            if (lockContext != null) {
                boolean unlocked = distributedLockService.unlock(lockContext);
                result.put("lockReleased", unlocked);
            }
        }
    }

    /**
     * 测试分布式锁的并发场景
     */
    @PostMapping("/test/lock/concurrent")
    public ResponseEntity<Map<String, Object>> testConcurrentLock() {
        System.out.println("=== 测试分布式锁并发场景 ===");
        
        Map<String, Object> result = new HashMap<>();
        
        // 这个接口仅用于演示，实际并发测试应该使用压力测试工具
        result.put("message", "分布式锁并发测试请使用压力测试工具（如 JMeter、Apache Bench）");
        result.put("testEndpoint", "/api/distributed-lock/test/lock/basic");
        result.put("testMethod", "POST");
        result.put("testParam", "lockName=test:concurrent:lock");
        result.put("expectedBehavior", "多个请求同时访问，只有一个会成功，其他会返回429");
        
        return ResponseEntity.ok(result);
    }
}

