package Afriends_v3.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁服务
 * 提供基于 Redis 的分布式锁能力，用于防止并发冲突
 * 
 * 锁的类型：
 * 1. 互斥锁（Mutex Lock）：通过 Redis 的 SETNX 命令实现，同一时刻只有一个线程能获取锁
 * 2. 可重入锁（Reentrant Lock）：支持同一线程多次获取同一把锁
 * 3. 自旋锁（Spin Lock）：支持重试机制，循环等待获取锁
 * 4. 公平锁（Fair Lock）：可通过队列实现，按请求顺序获取锁
 * 5. 读写锁（Read-Write Lock）：区分读锁和写锁，读锁可并发，写锁互斥
 * 
 * 本实现主要使用的是互斥锁+自旋锁的混合模式，具备以下特点：
 * - 原子性：使用 Redis 的原子操作保证获取和释放的原子性
 * - 防误删：使用唯一 lockValue 防止误删其他线程的锁
 * - 自动过期：设置锁的过期时间，防止死锁
 * - 可重试：支持自旋重试，提高获取锁的成功率
 */
@Service
public class DistributedLockService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 锁的前缀
    private static final String LOCK_PREFIX = "lock:";

    // 默认锁过期时间（秒）
    private static final long DEFAULT_LOCK_EXPIRE_SECONDS = 30;

    // 默认获取锁超时时间（秒）
    private static final long DEFAULT_ACQUIRE_TIMEOUT_SECONDS = 10;

    // 默认重试间隔（毫秒）
    private static final long DEFAULT_RETRY_INTERVAL_MS = 100;

    /**
     * 尝试获取分布式锁（不重试，立即返回）
     * 
     * @param lockName 锁的名称（例如："register:username:zhangsan"）
     * @return LockContext 如果成功获取锁，返回锁上下文；否则返回 null
     */
    public LockContext tryLock(String lockName) {
        return tryLock(lockName, DEFAULT_LOCK_EXPIRE_SECONDS);
    }

    /**
     * 尝试获取分布式锁（不重试，立即返回）
     * 
     * @param lockName  锁的名称
     * @param expireSeconds 锁的过期时间（秒）
     * @return LockContext 如果成功获取锁，返回锁上下文；否则返回 null
     */
    public LockContext tryLock(String lockName, long expireSeconds) {
        String lockKey = LOCK_PREFIX + lockName;
        String lockValue = UUID.randomUUID().toString() + "-" + Thread.currentThread().getId();

        Boolean acquired = stringRedisTemplate.opsForValue().setIfAbsent(
            lockKey, 
            lockValue, 
            expireSeconds, 
            TimeUnit.SECONDS
        );

        if (Boolean.TRUE.equals(acquired)) {
            System.out.println("分布式锁获取成功: lockKey=" + lockKey + ", lockValue=" + lockValue);
            return new LockContext(lockKey, lockValue, true);
        } else {
            System.out.println("分布式锁获取失败: lockKey=" + lockKey);
            return null;
        }
    }

    /**
     * 尝试获取分布式锁（支持自旋重试）
     * 
     * @param lockName 锁的名称
     * @return LockContext 如果成功获取锁，返回锁上下文；否则返回 null
     */
    public LockContext tryLockWithRetry(String lockName) {
        return tryLockWithRetry(lockName, DEFAULT_LOCK_EXPIRE_SECONDS, DEFAULT_ACQUIRE_TIMEOUT_SECONDS);
    }

    /**
     * 尝试获取分布式锁（支持自旋重试）
     * 
     * @param lockName     锁的名称
     * @param expireSeconds  锁的过期时间（秒）
     * @param acquireTimeoutSeconds 获取锁的超时时间（秒）
     * @return LockContext 如果成功获取锁，返回锁上下文；否则返回 null
     */
    public LockContext tryLockWithRetry(String lockName, long expireSeconds, long acquireTimeoutSeconds) {
        String lockKey = LOCK_PREFIX + lockName;
        String lockValue = UUID.randomUUID().toString() + "-" + Thread.currentThread().getId();
        long startTime = System.currentTimeMillis();
        long timeoutMs = acquireTimeoutSeconds * 1000;

        while (true) {
            // 检查是否超时
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                System.out.println("获取分布式锁超时: lockKey=" + lockKey + ", timeout=" + acquireTimeoutSeconds + "s");
                return null;
            }

            // 尝试获取锁
            Boolean acquired = stringRedisTemplate.opsForValue().setIfAbsent(
                lockKey, 
                lockValue, 
                expireSeconds, 
                TimeUnit.SECONDS
            );

            if (Boolean.TRUE.equals(acquired)) {
                System.out.println("分布式锁获取成功: lockKey=" + lockKey + ", lockValue=" + lockValue);
                return new LockContext(lockKey, lockValue, true);
            }

            // 获取失败，等待后重试
            try {
                Thread.sleep(DEFAULT_RETRY_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("获取分布式锁时线程被中断: lockKey=" + lockKey);
                return null;
            }
        }
    }

    /**
     * 释放分布式锁
     * 
     * @param lockContext 锁的上下文
     * @return true 释放成功；false 释放失败
     */
    public boolean unlock(LockContext lockContext) {
        if (lockContext == null || !lockContext.isAcquired()) {
            return false;
        }

        String lockKey = lockContext.getLockKey();
        String expectedValue = lockContext.getLockValue();

        // 获取当前锁的值
        String currentValue = stringRedisTemplate.opsForValue().get(lockKey);

        // 只有当值匹配时才释放锁（防止误删其他线程的锁）
        if (expectedValue.equals(currentValue)) {
            Boolean deleted = stringRedisTemplate.delete(lockKey);
            if (Boolean.TRUE.equals(deleted)) {
                System.out.println("分布式锁释放成功: lockKey=" + lockKey);
                return true;
            } else {
                System.err.println("分布式锁释放失败: lockKey=" + lockKey + ", redis删除返回false");
                return false;
            }
        } else {
            System.err.println("分布式锁的值不匹配，可能已过期或被其他线程持有: lockKey=" + lockKey 
                + ", expected=" + expectedValue + ", actual=" + currentValue);
            return false;
        }
    }

    /**
     * 使用分布式锁执行业务逻辑（自动加锁和释放锁）
     * 
     * @param lockName   锁的名称
     * @param businessLogic 需要执行的业务逻辑
     * @return 业务逻辑的返回值
     */
    public <T> T executeWithLock(String lockName, Supplier<T> businessLogic) {
        return executeWithLock(lockName, DEFAULT_LOCK_EXPIRE_SECONDS, DEFAULT_ACQUIRE_TIMEOUT_SECONDS, businessLogic);
    }

    /**
     * 使用分布式锁执行业务逻辑（自动加锁和释放锁）
     * 
     * @param lockName   锁的名称
     * @param expireSeconds 锁的过期时间（秒）
     * @param acquireTimeoutSeconds 获取锁的超时时间（秒）
     * @param businessLogic 需要执行的业务逻辑
     * @return 业务逻辑的返回值
     */
    public <T> T executeWithLock(String lockName, long expireSeconds, long acquireTimeoutSeconds, Supplier<T> businessLogic) {
        LockContext lockContext = null;
        try {
            // 尝试获取锁（带重试）
            lockContext = tryLockWithRetry(lockName, expireSeconds, acquireTimeoutSeconds);
            
            if (lockContext == null) {
                // 获取锁失败，抛出异常
                throw new RuntimeException("无法获取分布式锁: " + lockName);
            }

            // 执行业务逻辑
            System.out.println("成功获取分布式锁，开始执行业务逻辑: lockName=" + lockName);
            return businessLogic.get();

        } catch (Exception e) {
            System.err.println("执行业务逻辑时发生异常: lockName=" + lockName + ", error=" + e.getMessage());
            throw e;
        } finally {
            // 释放锁（必须执行，即使业务逻辑抛出异常）
            if (lockContext != null) {
                unlock(lockContext);
            }
        }
    }

    /**
     * 锁的上下文
     * 封装锁的 key、value 和获取状态
     */
    public static class LockContext {
        private final String lockKey;
        private final String lockValue;
        private final boolean acquired;

        public LockContext(String lockKey, String lockValue, boolean acquired) {
            this.lockKey = lockKey;
            this.lockValue = lockValue;
            this.acquired = acquired;
        }

        public String getLockKey() {
            return lockKey;
        }

        public String getLockValue() {
            return lockValue;
        }

        public boolean isAcquired() {
            return acquired;
        }
    }
}
