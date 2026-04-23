package com.example.finalprojectspringboot.service;

import java.time.Duration;
import java.util.Collections;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService {

    private static final long LOCK_EXPIRY_MILLIS = 10_000L;
    private static final String RELEASE_LOCK_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else return 0 end";

    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> releaseScript;

    public RedisLockService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.releaseScript = buildReleaseScript();
    }

    public boolean tryAcquireLock(String lockKey, String requestId) {
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, requestId, Duration.ofMillis(LOCK_EXPIRY_MILLIS));
        return Boolean.TRUE.equals(acquired);
    }

    public boolean releaseLock(String lockKey, String requestId) {
        Long result = stringRedisTemplate.execute(releaseScript, Collections.singletonList(lockKey), requestId);
        return result != null && result > 0;
    }

    private DefaultRedisScript<Long> buildReleaseScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(RELEASE_LOCK_SCRIPT);
        script.setResultType(Long.class);
        return script;
    }
}
