package Afriends_v3.controller;

import Afriends_v3.service.UserEntityService_wlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户注册控制器
 * 提供正式的用户注册接口
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserRegisterController {

    @Autowired
    private UserEntityService_wlq userEntityService_wlq;

    /**
     * 用户注册接口
     * @param username 用户名（必填）
     * @param password 密码（必填）
     * @param phone 手机号（可选）
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String phone) {
        
        System.out.println("=== 用户注册接口被调用 ===");
        System.out.println("用户名: " + username + ", 手机号: " + (phone != null ? phone : "未提供"));
        
        try {
            Map<String, Object> result = userEntityService_wlq.registerUser(username, password, phone);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                int code = (Integer) result.get("code");
                return ResponseEntity.status(code).body(result);
            }
        } catch (Exception e) {
            System.err.println("注册失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "注册失败: " + e.getMessage(),
                "code", 500
            ));
        }
    }

    /**
     * 检查用户名是否可用
     * @param username 用户名
     * @return 是否可用
     */
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        try {
            boolean available = userEntityService_wlq.isUsernameAvailable(username);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "available", available,
                "message", available ? "用户名可用" : "用户名已被占用"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "检查失败: " + e.getMessage()
            ));
        }
    }
}

