package Afriends_v3.controller;

import Afriends_v3.entity.UserInfo_njj;
import Afriends_v3.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户信息控制器
 * 提供用户数据的API接口
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 加载数据到内存（默认100条）
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadDataToMemory() {
        try {
            System.out.println("开始加载用户数据到内存（默认100条）...");
            long startTime = System.currentTimeMillis();

            userInfoService.loadDataToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 加载指定数量的数据到内存
     */
    @PostMapping("/load/{limit}")
    public ResponseEntity<Map<String, String>> loadDataToMemoryWithLimit(@PathVariable int limit) {
        try {
            System.out.println("开始加载用户数据到内存（限制" + limit + "条）...");
            long startTime = System.currentTimeMillis();

            userInfoService.loadDataToMemory(limit);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "数据加载到内存成功，实际加载: " + limit + " 条，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration),
                    "limit", String.valueOf(limit));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有用户（从内存）
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserInfo_njj>> getAllUsers() {
        try {
            List<UserInfo_njj> users = userInfoService.getAllUsersFromMemory();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据ID获取用户（从内存）
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo_njj> getUserById(@PathVariable Long id) {
        try {
            UserInfo_njj user = userInfoService.getUserByIdFromMemory(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取统计信息（从内存）
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = userInfoService.getStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 测试数据库连接
     */
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        try {
            System.out.println("开始测试数据库连接...");
            long startTime = System.currentTimeMillis();

            // 测试数据库连接
            long count = userInfoService.count();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("数据库连接测试完成，用户总数: " + count + "，耗时: " + duration + "ms");

            Map<String, Object> result = Map.of(
                    "success", true,
                    "userCount", count,
                    "duration", duration,
                    "message", "数据库连接正常");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "error", e.getMessage(),
                    "message", "数据库连接失败");
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 分页加载用户数据（测试用）
     */
    @GetMapping("/load-page")
    public ResponseEntity<Map<String, Object>> loadUserDataPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("开始分页加载用户数据，页码: " + page + "，大小: " + size);
            long startTime = System.currentTimeMillis();

            // 使用MyBatis-Plus的分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserInfo_njj> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                    page, size);

            com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserInfo_njj> result = userInfoService
                    .page(pageParam);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("分页查询完成，耗时: " + duration + "ms");

            Map<String, Object> response = Map.of(
                    "success", true,
                    "records", result.getRecords(),
                    "total", result.getTotal(),
                    "current", result.getCurrent(),
                    "size", result.getSize(),
                    "pages", result.getPages(),
                    "duration", duration);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("分页查询失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "error", e.getMessage(),
                    "message", "分页查询失败");
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 刷新内存数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshMemoryData() {
        try {
            userInfoService.forceRefreshMemory();
            Map<String, String> result = Map.of(
                    "message", "内存数据刷新成功",
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> result = Map.of(
                    "message", "内存数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }
}