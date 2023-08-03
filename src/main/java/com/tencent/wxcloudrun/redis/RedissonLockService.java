package com.tencent.wxcloudrun.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedissonLockService {

    private static final long LEASE_TIME = 300L;
    private static final long WAIT_TIME = 180L;

    private RedissonLockService() {
        throw new IllegalStateException("Utility class");
    }

    public static void init(RedisService redisService) {
        redis = redisService;
    }

    private static RedisService redis = null;

    /**
     * 获取lock
     * 使用时注意加锁线程和解锁线程
     *
     * @param lockKey
     * @return
     */
    public static RLock getLock(String lockKey) {
        return redis.getLock(lockKey);
    }

    private static RLock getMultiLock(Collection<RLock> locks) {
        return redis.getMultiLock(locks.toArray(new RLock[]{}));
    }

    /**
     * 获取并且尝试lock
     * 使用时注意加锁线程和解锁线程
     *
     * @param lockKey
     * @return
     */
    public static RLock getAndTryLock(String lockKey) {
        return getAndTryLock(lockKey, WAIT_TIME, LEASE_TIME);
    }

    public static RLock getAndTryLock(String lockKey, long waitTime, long leaseTime) {
        return getAndTryLock(lockKey, waitTime, leaseTime, TimeUnit.SECONDS);
    }

    public static RLock getAndTryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock lock = getLock(lockKey);
        return tryLock(lock, waitTime, leaseTime, timeUnit);
    }

    public static RLock getAndTryMultiLock(Collection<RLock> locks) {
        return getAndTryMultiLock(locks, WAIT_TIME, LEASE_TIME);
    }

    public static RLock getAndTryMultiLock(Collection<RLock> locks, long waitTime, long leaseTime) {
        return getAndTryMultiLock(locks, waitTime, leaseTime, TimeUnit.SECONDS);
    }

    public static RLock getAndTryMultiLock(Collection<RLock> locks, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock lock = getMultiLock(locks);
        return tryLock(lock, waitTime, leaseTime, timeUnit);
    }

    private static RLock tryLock(RLock lock, long waitTime, long leaseTime, TimeUnit timeUnit) {
        try {
            boolean res = lock.tryLock(waitTime, leaseTime, timeUnit);
            return res ? lock : null;
        } catch (Exception e) {
            log.error("RedissonLock tryLock fail", e);
            return null;
        }
    }

    public static void unlockDirectly(RLock lock) {
        try {
            if (lock == null) {
                return;
            }
            if (lock instanceof RedissonLock) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                } else {
                    log.debug("<<<<<<<<<< lock not current <<<{} at {}", lock.getName(), LocalDateTime.now().toString());
                }
            } else {
                lock.unlock();
            }
        } catch (Exception e) {
            log.error("RedissonLock unlock fail", e);
        }
    }

    /**
     * 获取锁key
     *
     * @param subKeys
     * @return
     */
    public static String getLockKey(String... subKeys) {
        return redis.getAppName() + ":lock:" + StringUtils.join(subKeys, ":");
    }

    /**
     * 1.有事务，事务结束释放当前锁
     * 2.无事务，直接释放当前锁
     *
     * @author 3832
     * @date 2023/7/11 10:27
     * @param lock 锁
     **/
    public static void unlock(RLock lock) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    unlockDirectly(lock);
                }
            });
        } else {
            unlockDirectly(lock);
        }
    }
}
