package com.tencent.wxcloudrun.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Value("${spring.application.name:vote}")
    private String appName;

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    private void initIt() {
        RedissonLockService.init(this);
    }

    public String getAppName() {
        return appName;
    }

    RLock getLock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    RLock getMultiLock(RLock... locks) {
        return redissonClient.getMultiLock(locks);
    }

    /**
     * 设置超时时间
     *
     * @param key
     * @param seconds
     * @return
     */
    public Boolean expire(String key, long seconds) {
        return redissonClient.getBucket(key, JsonJacksonCodec.INSTANCE).expire(seconds, TimeUnit.SECONDS);
    }

    /**
     * 判断元素是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return redissonClient.getBucket(key, JsonJacksonCodec.INSTANCE).isExists();
    }

    /**
     * 符合给定模式的 key 列表
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Iterable<String> iterable = redissonClient.getKeys().getKeysByPattern(pattern);
        Set<String> keySet = new HashSet<>();
        for (String key : iterable) {
            keySet.add(key);
        }
        return keySet;
    }

    /**
     * 获取列表中所有元素
     *
     * @param key
     * @return
     */
    public List<Object> lgetAll(String key) {
        return redissonClient.getList(key, JsonJacksonCodec.INSTANCE).readAll();
    }

    /**
     * 获取列表中指定位置的元素
     *
     * @param key
     * @param indexs
     * @return
     */
    public List<Object> lget(String key, int... indexs) {
        return redissonClient.getList(key, JsonJacksonCodec.INSTANCE).get(indexs);
    }

    /**
     * 返回列表中指定区间内的元素
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推
     *
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public List<Object> lrange(String key, int start, int stop) {
        return redissonClient.getList(key, JsonJacksonCodec.INSTANCE).subList(start, stop).readAll();
    }

    /**
     * 返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0
     *
     * @param key
     * @return
     */
    public Integer llen(String key) {
        return redissonClient.getList(key, JsonJacksonCodec.INSTANCE).size();
    }

    /**
     * 将一个值添加到列表
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean ladd(String key, Object value) {
        return redissonClient.getList(key, JsonJacksonCodec.INSTANCE).add(value);
    }

    /**
     * 将多个值添加到列表
     *
     * @param key
     * @param values
     * @return
     */
    public Boolean laddAll(String key, List<Object> values) {
        return redissonClient.getList(key, JsonJacksonCodec.INSTANCE).addAll(values);
    }

    /**
     * 将一个值插入到列表头部
     * key 不存在，一个空列表会被创建并执行 LPUSH 操作
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean lpush(String key, Object value) {
        return redissonClient.getDeque(key, JsonJacksonCodec.INSTANCE).offerFirst(value);
    }

    /**
     * 将一个值插入到列表尾部
     * key 不存在，一个空列表会被创建并执行 RPUSH 操作
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean rpush(String key, Object value) {
        return redissonClient.getDeque(key, JsonJacksonCodec.INSTANCE).offerLast(value);
    }

    /**
     * 移除并返回列表的第一个元素。
     *
     * @param key
     * @return
     */
    public Object lpop(String key) {
        return redissonClient.getDeque(key, JsonJacksonCodec.INSTANCE).pollFirst();
    }

    /**
     * 移除并返回列表的最后一个元素。
     *
     * @param key
     * @return
     */
    public Object rpop(String key) {
        return redissonClient.getDeque(key, JsonJacksonCodec.INSTANCE).pollLast();
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param key1
     * @param key2
     * @return
     */
    public Object rpoplpush(String key1, String key2) {
        return redissonClient.getDeque(key1).pollLastAndOfferFirstTo(key2);
    }

    /**
     * 为哈希表中的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     *
     * @param key
     * @param fieldKey
     * @param value
     * @return
     */
    public Object hset(String key, String fieldKey, Object value) {
        return redissonClient.getMap(key, JsonJacksonCodec.INSTANCE).put(fieldKey, value);
    }

    /**
     * 返回哈希表中，所有的字段和值
     *
     * @param key
     * @return
     */
    public Map hgetAll(String key) {
        return redissonClient.getMap(key, JsonJacksonCodec.INSTANCE).readAllMap();
    }

    /**
     * 返回哈希表中指定字段的值
     *
     * @param key
     * @param fieldKey
     * @return
     */
    public Object hget(String key, String fieldKey) {
        return redissonClient.getMap(key, JsonJacksonCodec.INSTANCE).get(fieldKey);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        return redissonClient.getAtomicLong(key).get();
    }

    /**
     * 设置给定 key 的值
     *
     * @param key
     * @param value
     */
    public void setLong(String key, Long value) {
        redissonClient.getAtomicLong(key).set(value);
    }

    /**
     * 将 key 中储存的数字加上指定的增量值
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key, long integer) {
        return redissonClient.getAtomicLong(key).addAndGet(integer);
    }

    /**
     * 将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

    /**
     * 获取指定 key 的值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redissonClient.getBucket(key, JsonJacksonCodec.INSTANCE).get();
    }

    /**
     * 设置给定 key 的值
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redissonClient.getBucket(key, JsonJacksonCodec.INSTANCE).set(value);
    }

    /**
     * 删除给定 key 的值
     *
     * @param keys
     * @return
     */
    public Long del(String... keys) {
        return redissonClient.getKeys().delete(keys);
    }

    /**
     * 删除符合给定模式的 key 的值
     *
     * @param pattern
     * @return
     */
    public Long delByPattern(String pattern) {
        return redissonClient.getKeys().deleteByPattern(pattern);
    }
}
