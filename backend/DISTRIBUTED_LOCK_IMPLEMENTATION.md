# 分布式锁实现文档

## 📋 概述

本文档说明了 Afriends_v3 项目中分布式锁的实现细节、锁的类型以及文件修改总结。

## 🔒 分布式锁类型详解

### 1. 互斥锁（Mutex Lock）
**特点：**
- 通过 Redis 的 SETNX 命令实现
- 同一时刻只有一个线程能获取锁
- 其他线程必须等待当前线程释放锁后才能获取

**实现方式：**
```java
stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireSeconds, TimeUnit.SECONDS)
```

### 2. 自旋锁（Spin Lock）
**特点：**
- 支持重试机制，循环等待获取锁
- 在一定时间范围内持续尝试获取锁
- 避免线程阻塞，提高资源利用率

**实现方式：**
```java
public LockContext tryLockWithRetry(String lockName, long expireSeconds, long acquireTimeoutSeconds)
```

### 3. 防误删锁（Safe Release Lock）
**特点：**
- 使用唯一 lockValue（UUID + 线程ID）防止误删其他线程的锁
- 释放锁前先验证值是否匹配
- 防止因业务超时导致的锁误删问题

**实现方式：**
```java
String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
if (expectedValue.equals(currentValue)) {
    stringRedisTemplate.delete(lockKey);
}
```

### 4. 自动过期锁（Auto-Expire Lock）
**特点：**
- 设置锁的过期时间，防止死锁
- 即使业务异常退出，锁也会自动过期释放
- 提高系统容错能力

**默认配置：**
- 锁过期时间：30秒
- 获取锁超时时间：10秒
- 重试间隔：100毫秒

## 📁 文件修改总结

### 1. 新增文件

#### `backend/src/main/java/Afriends_v3/service/common/DistributedLockService.java`
**功能：** 分布式锁核心服务类
**主要方法：**
- `tryLock(String lockName)` - 尝试获取锁（不重试）
- `tryLockWithRetry(String lockName, long expireSeconds, long acquireTimeoutSeconds)` - 尝试获取锁（带重试）
- `unlock(LockContext lockContext)` - 释放锁
- `executeWithLock(String lockName, Supplier<T> businessLogic)` - 使用锁执行业务逻辑（自动加锁和释放）

**核心特性：**
- 原子性操作：使用 Redis 的原子操作保证线程安全
- 防误删机制：使用唯一 lockValue 验证
- 自动过期：防止死锁
- 支持 Lambda 表达式：简化业务代码

#### `backend/src/main/java/Afriends_v3/controller/DistributedLockTestController.java`
**功能：** 分布式锁测试控制器
**测试接口：**
- `POST /api/distributed-lock/test/register/auto` - 测试自动加锁方式注册
- `POST /api/distributed-lock/test/register/manual` - 测试手动加锁方式注册
- `POST /api/distributed-lock/test/lock/basic` - 测试基础锁获取和释放
- `POST /api/distributed-lock/test/lock/concurrent` - 测试并发场景说明

### 2. 修改文件

#### `backend/src/main/java/Afriends_v3/service/UserEntityService_wlq.java`
**修改内容：**
- 添加 `@Autowired DistributedLockService distributedLockService;` 依赖注入
- 添加示例方法 `registerUserWithLock(String username, String password, String phone)` - 使用自动加锁方式
- 添加示例方法 `registerUserWithManualLock(String username, String password)` - 使用手动加锁方式

**代码示例：**
```java
// 自动加锁方式（推荐）
Map<String, Object> result = distributedLockService.executeWithLock(lockName, () -> {
    // 执行业务逻辑
    return businessResult;
});

// 手动加锁方式
LockContext lockContext = distributedLockService.tryLockWithRetry(lockName);
try {
    // 执行业务逻辑
} finally {
    distributedLockService.unlock(lockContext);
}
```

## 🎯 使用场景

### 1. 用户注册防并发
**场景：** 防止多个用户同时注册相同的用户名
**实现：**
```java
String lockName = "register:username:" + username;
distributedLockService.executeWithLock(lockName, () -> {
    // 检查用户名是否存在
    // 创建新用户
    // 保存到数据库
});
```

### 2. 库存扣减防超卖
**场景：** 防止商品库存被超量扣减
**实现：**
```java
String lockName = "inventory:" + productId;
distributedLockService.executeWithLock(lockName, () -> {
    // 检查库存
    // 扣减库存
    // 更新数据库
});
```

### 3. 分布式事务协调
**场景：** 协调多个服务的分布式事务
**实现：**
```java
String lockName = "transaction:" + transactionId;
distributedLockService.executeWithLock(lockName, () -> {
    // 执行多服务协调逻辑
});
```

## 🔧 技术实现细节

### 锁的 Key 命名规范
```
lock:{业务}:{资源标识}
例如：
- lock:register:username:zhangsan
- lock:inventory:product:12345
- lock:transaction:order:abc123
```

### 锁的唯一值生成
```java
String lockValue = UUID.randomUUID().toString() + "-" + Thread.currentThread().getId();
```
**优势：**
- UUID 保证全局唯一性
- 线程ID 便于调试和追踪
- 防止误删其他线程的锁

### Redis 命令
**获取锁：**
```redis
SET lock:register:username:zhangsan "uuid-threadId" EX 30 NX
```

**释放锁（Lua 脚本）：**
```lua
if redis.call("get", KEYS[1]) == ARGV[1] then
    return redis.call("del", KEYS[1])
else
    return 0
end
```

## 📊 性能优化建议

### 1. 减少锁的持有时间
- 尽量缩短业务逻辑的执行时间
- 避免在锁内执行网络请求、文件IO等耗时操作

### 2. 合理设置锁的过期时间
- 根据实际业务耗时动态调整
- 避免设置过短导致业务未完成就过期
- 避免设置过长导致锁释放不及时

### 3. 优化重试策略
- 根据业务场景调整重试间隔
- 对于不重要的操作可以快速失败
- 对于关键操作可以增加重试次数

## 🧪 测试建议

### 1. 单线程测试
验证锁的基本获取和释放功能

### 2. 并发测试
使用 JMeter 或 Apache Bench 模拟多个并发请求，验证只有一个请求成功

### 3. 异常测试
- 测试业务逻辑抛出异常时锁是否能正确释放
- 测试锁过期后的自动释放
- 测试网络中断等异常场景

## 📝 注意事项

### 1. 死锁预防
- 始终设置锁的过期时间
- 避免在锁内获取另一个锁
- 锁的获取顺序要保持一致

### 2. 数据一致性
- 释放锁失败时的处理策略
- 锁过期后的数据回滚机制
- 重入锁的实现（如需支持）

### 3. Redis 可用性
- 确保 Redis 高可用配置
- 监控 Redis 连接状态
- 实现 Redis 故障降级方案

## 🔗 相关依赖

已在 `backend/pom.xml` 中配置：
- `spring-boot-starter-data-redis` - Redis 客户端
- `spring-boot-starter-cache` - 缓存抽象
- `jackson-databind` - JSON 序列化

## 📚 参考资料

- [Redis 官方文档 - Distributed Locks](https://redis.io/topics/distlock)
- [Spring Data Redis 文档](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)
- 《Redis in Action》 Chapter 6: Distributed Locking

## 📅 更新记录

- **2024-01-XX**: 初始实现分布式锁功能
- 添加 `DistributedLockService` 核心服务
- 添加用户注册示例方法
- 添加测试控制器

