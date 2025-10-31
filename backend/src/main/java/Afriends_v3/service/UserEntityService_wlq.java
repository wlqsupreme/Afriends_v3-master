package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import Afriends_v3.service.common.DistributedLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * U开头_wlq实体类综合服务
 * 负责管理所有U开头_wlq实体类的数据库操作和内存缓存
 */
@Service
public class UserEntityService_wlq {

    @Autowired
    private UserAiRequireMapper userAiRequireMapper;

    @Autowired
    private UserAiRequireFeatureVectorMapper userAiRequireFeatureVectorMapper;

    @Autowired
    private UserAiRespondMapper userAiRespondMapper;

    @Autowired
    private UserAiRespondFeatureVectorMapper userAiRespondFeatureVectorMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private DistributedLockService distributedLockService;

    // UserAiRequire 相关方法
    public void loadUserAiRequireToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI需求数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRequire = userAiRequireMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRequire.size() + " 条记录");

            if (allUserAiRequire.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI需求数据");
                return;
            }

            UserAiRequire_list_wlq.loadFromDatabaseDirectly(allUserAiRequire);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI需求数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRequire_wlq> getAllUserAiRequireFromMemory() {
        return UserAiRequire_list_wlq.getAllUserAiRequire();
    }

    public Map<String, Object> getUserAiRequireStatisticsFromMemory() {
        return UserAiRequire_list_wlq.getStatistics();
    }

    // UserAiRequireFeatureVector 相关方法
    public void loadUserAiRequireFeatureVectorToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI需求特征向量数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRequireFeatureVector = userAiRequireFeatureVectorMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRequireFeatureVector.size() + " 条记录");

            if (allUserAiRequireFeatureVector.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI需求特征向量数据");
                return;
            }

            UserAiRequireFeatureVector_list_wlq.loadFromDatabaseDirectly(allUserAiRequireFeatureVector);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI需求特征向量数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求特征向量数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRequireFeatureVector_wlq> getAllUserAiRequireFeatureVectorFromMemory() {
        return UserAiRequireFeatureVector_list_wlq.getAllUserAiRequireFeatureVector();
    }

    public Map<String, Object> getUserAiRequireFeatureVectorStatisticsFromMemory() {
        return UserAiRequireFeatureVector_list_wlq.getStatistics();
    }

    // UserAiRespond 相关方法
    public void loadUserAiRespondToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI响应数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRespond = userAiRespondMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRespond.size() + " 条记录");

            if (allUserAiRespond.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI响应数据");
                return;
            }

            UserAiRespond_list_wlq.loadFromDatabaseDirectly(allUserAiRespond);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI响应数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRespond_wlq> getAllUserAiRespondFromMemory() {
        return UserAiRespond_list_wlq.getAllUserAiRespond();
    }

    public Map<String, Object> getUserAiRespondStatisticsFromMemory() {
        return UserAiRespond_list_wlq.getStatistics();
    }

    // UserAiRespondFeatureVector 相关方法
    public void loadUserAiRespondFeatureVectorToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI响应特征向量数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRespondFeatureVector = userAiRespondFeatureVectorMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRespondFeatureVector.size() + " 条记录");

            if (allUserAiRespondFeatureVector.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI响应特征向量数据");
                return;
            }

            UserAiRespondFeatureVector_list_wlq.loadFromDatabaseDirectly(allUserAiRespondFeatureVector);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI响应特征向量数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应特征向量数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRespondFeatureVector_wlq> getAllUserAiRespondFeatureVectorFromMemory() {
        return UserAiRespondFeatureVector_list_wlq.getAllUserAiRespondFeatureVector();
    }

    public Map<String, Object> getUserAiRespondFeatureVectorStatisticsFromMemory() {
        return UserAiRespondFeatureVector_list_wlq.getStatistics();
    }

    // UserBase 相关方法
    public void loadUserBaseToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户基础数据...");
            long startTime = System.currentTimeMillis();

            var allUserBase = userBaseMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserBase.size() + " 条记录");

            if (allUserBase.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户基础数据");
                return;
            }

            UserBase_list_wlq.loadFromDatabaseDirectly(allUserBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBase_wlq> getAllUserBaseFromMemory() {
        return UserBase_list_wlq.getAllUserBase();
    }

    public Map<String, Object> getUserBaseStatisticsFromMemory() {
        return UserBase_list_wlq.getStatistics();
    }

    // 加载所有U开头_wlq实体类数据
    public void loadAllUserEntityDataToMemory() {
        System.out.println("UserEntityService_wlq: 开始加载所有U开头_wlq实体类数据...");

        try {
            loadUserAiRequireToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserAiRequireFeatureVectorToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求特征向量数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserAiRespondToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserAiRespondFeatureVectorToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应特征向量数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserBaseToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户基础数据失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("UserEntityService_wlq: 所有U开头_wlq实体类数据加载完成！");
    }

    // ============================
    // 正式用户注册方法
    // ============================

    /**
     * 正式用户注册方法（写入数据库）
     * @param username 用户名
     * @param password 密码
     * @param phone 手机号（可选）
     * @return 注册结果
     */
    public Map<String, Object> registerUser(String username, String password, String phone) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("开始注册用户: username=" + username);
            
            // 1. 检查用户名是否已存在（从数据库查询）
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserBase_wlq> queryWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.eq("login_account", username);
            Long count = userBaseMapper.selectCount(queryWrapper);
            
            if (count > 0) {
                result.put("success", false);
                result.put("message", "用户名已被注册");
                result.put("code", 400);
                return result;
            }
            
            // 2. 生成新的用户ID（从1200000开始）
            Long newUserId = generateNewUserId();
            
            // 3. 创建新用户实体
            UserBase_wlq newUser = new UserBase_wlq();
            newUser.setUserId(newUserId);
            newUser.setLoginAccount(username);
            newUser.setPasswordHash(hashPassword(password)); // 简单hash，实际应使用BCrypt等
            if (phone != null && !phone.trim().isEmpty()) {
                newUser.setLoginTelAccount(phone);
            }
            newUser.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            newUser.setLastActive(new java.sql.Timestamp(System.currentTimeMillis()));
            newUser.setUserKind("真人"); // 默认为真人用户
            
            // 4. 保存到数据库
            int insertCount = userBaseMapper.insert(newUser);
            
            if (insertCount > 0) {
                // 5. 重新加载内存缓存（可选）
                // 注意：实际场景中如果缓存已加载，建议手动添加而非全部重载
                // 这里暂时跳过缓存更新，避免影响性能
                
                result.put("success", true);
                result.put("message", "注册成功");
                result.put("code", 200);
                result.put("userId", newUserId);
                result.put("username", username);
                System.out.println("用户注册成功: userId=" + newUserId + ", username=" + username);
            } else {
                result.put("success", false);
                result.put("message", "注册失败，数据库插入失败");
                result.put("code", 500);
            }
            
        } catch (Exception e) {
            System.err.println("注册过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "注册失败: " + e.getMessage());
            result.put("code", 500);
        }
        
        return result;
    }

    /**
     * 检查用户名是否可用
     */
    public boolean isUsernameAvailable(String username) {
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserBase_wlq> queryWrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.eq("login_account", username);
            Long count = userBaseMapper.selectCount(queryWrapper);
            return count == 0;
        } catch (Exception e) {
            System.err.println("检查用户名可用性失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 生成新的用户ID（从1200000开始）
     * 使用原生SQL避免实体映射问题
     */
    private Long generateNewUserId() {
        try {
            // 方案1：查询 >= 1200000 的最大ID
            Long maxIdAbove1200000 = userBaseMapper.selectObjs(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserBase_wlq>()
                    .select("MAX(user_id) as maxId")
                    .ge("user_id", 1200000L)
            ).stream()
             .filter(obj -> obj != null)
             .map(obj -> Long.valueOf(obj.toString()))
             .findFirst()
             .orElse(null);
            
            if (maxIdAbove1200000 != null && maxIdAbove1200000 >= 1200000L) {
                Long newId = maxIdAbove1200000 + 1;
                System.out.println("查询到 >=1200000 的最大用户ID: " + maxIdAbove1200000 + ", 新ID将为: " + newId);
                return newId;
            }
            
            // 方案2：检查数据库是否有任何记录
            Long globalMaxId = userBaseMapper.selectObjs(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserBase_wlq>()
                    .select("MAX(user_id) as maxId")
            ).stream()
             .filter(obj -> obj != null)
             .map(obj -> Long.valueOf(obj.toString()))
             .findFirst()
             .orElse(null);
            
            if (globalMaxId != null && globalMaxId >= 1200000L) {
                // 全局最大ID已经 >= 1200000，在其基础上+1
                Long newId = globalMaxId + 1;
                System.out.println("全局最大用户ID: " + globalMaxId + " (>=1200000), 新ID将为: " + newId);
                return newId;
            }
            
            // 没有找到 >= 1200000 的记录，使用起始值
            System.out.println("未找到 >=1200000 的用户ID，全局最大ID为: " + globalMaxId + ", 使用起始值: 1200000");
            return 1200000L;
            
        } catch (Exception e) {
            System.err.println("生成用户ID失败，使用默认起始值: " + e.getMessage());
            e.printStackTrace();
            return 1200000L;
        }
    }

    /**
     * 简单密码hash（实际生产环境应使用BCrypt等安全算法）
     */
    private String hashPassword(String password) {
        // 这里使用简单的hash作为演示，实际应使用BCrypt、Argon2等
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            System.err.println("密码hash失败: " + e.getMessage());
            return password; // 降级方案（不推荐）
        }
    }

    // ============================
    // 分布式锁示例方法：用户注册
    // ============================

    /**
     * 使用分布式锁的用户注册方法（示例）
     * 该示例展示如何使用分布式锁来防止并发注册冲突
     * 
     * @param username  用户名
     * @param password  密码
     * @param phone     手机号
     * @return 注册结果 Map
     */
    public Map<String, Object> registerUserWithLock(String username, String password, String phone) {
        Map<String, Object> result = new HashMap<>();
        
        // 定义锁的名称（以用户名作为唯一标识）
        String lockName = "register:username:" + username;
        
        // 使用分布式锁执行业务逻辑
        return distributedLockService.executeWithLock(lockName, () -> {
            System.out.println("开始注册用户: username=" + username + ", phone=" + phone);
            
            // 1. 检查用户名是否已存在（示例检查内存缓存）
            List<UserBase_wlq> existingUsers = UserBase_list_wlq.searchUserBaseByLoginAccount(username);
            if (!existingUsers.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户名已被注册");
                result.put("code", 400);
                return result;
            }
            
            // 2. 创建新用户实体（这里只是示例，实际应该根据业务需求填充完整字段）
            // UserBase_wlq newUser = new UserBase_wlq();
            // newUser.setUserId(generateUserId());
            // newUser.setLoginAccount(username);
            // newUser.setPasswordHash(hashPassword(password));
            // newUser.setLoginTelAccount(phone);
            // ... 其他字段设置
            
            // 3. 保存到数据库
            // int insertCount = userBaseMapper.insert(newUser);
            
            // 4. 刷新内存缓存
            // UserBase_list_wlq.addUser(newUser);
            
            // 模拟数据库操作的耗时
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                result.put("success", false);
                result.put("message", "注册过程被中断");
                result.put("code", 500);
                return result;
            }
            
            // 5. 返回成功结果
            result.put("success", true);
            result.put("message", "注册成功");
            result.put("code", 200);
            result.put("username", username);
            System.out.println("用户注册成功: username=" + username);
            
            return result;
        });
    }

    /**
     * 使用分布式锁的用户注册方法（手动加锁方式示例）
     * 该示例展示如何手动获取和释放锁
     * 
     * @param username  用户名
     * @param password  密码
     * @return 注册结果 Map
     */
    public Map<String, Object> registerUserWithManualLock(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        
        // 定义锁的名称
        String lockName = "register:username:" + username;
        
        // 尝试获取锁
        DistributedLockService.LockContext lockContext = distributedLockService.tryLockWithRetry(lockName);
        
        if (lockContext == null) {
            // 获取锁失败
            result.put("success", false);
            result.put("message", "注册失败，用户名可能已被占用或正在注册，请稍后再试");
            result.put("code", 429); // Too Many Requests
            return result;
        }
        
        try {
            // 获取锁成功，执行业务逻辑
            System.out.println("获取锁成功，开始执行注册数据库操作 for user: " + username);
            
            // 1. 检查用户名是否已存在
            List<UserBase_wlq> existingUsers = UserBase_list_wlq.searchUserBaseByLoginAccount(username);
            if (!existingUsers.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户名已被注册");
                result.put("code", 400);
                return result;
            }
            
            // 2. 执行数据库插入等核心注册操作
            // ... 创建用户实体 ...
            // ... 保存到数据库 ...
            
            // 模拟业务耗时
            Thread.sleep(200);
            
            // 3. 返回成功结果
            result.put("success", true);
            result.put("message", "注册成功");
            result.put("code", 200);
            result.put("username", username);
            System.out.println("注册成功 for user: " + username);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.put("success", false);
            result.put("message", "注册过程被中断");
            result.put("code", 500);
        } catch (Exception e) {
            // 处理其他异常
            System.err.println("注册过程中发生异常 for user: " + username + ", error: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "注册失败，系统异常: " + e.getMessage());
            result.put("code", 500);
        } finally {
            // 释放锁（必须在 finally 块中执行）
            boolean unlocked = distributedLockService.unlock(lockContext);
            if (unlocked) {
                System.out.println("锁已释放 for user: " + username);
            } else {
                System.err.println("锁释放失败 for user: " + username);
            }
        }
        
        return result;
    }
}
