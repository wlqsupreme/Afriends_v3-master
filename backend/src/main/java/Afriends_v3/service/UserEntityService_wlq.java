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
