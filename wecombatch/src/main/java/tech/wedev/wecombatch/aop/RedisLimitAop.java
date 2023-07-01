package tech.wedev.wecombatch.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tech.wedev.wecombatch.annos.RedisLimit;
import tech.wedev.wecombatch.tool.RedisLimitTool;

@Slf4j
@Component
@Aspect
public class RedisLimitAop {
    @Autowired
    private JedisPool jedisPool;

    @Around("execution(* tech.wedev.wecombatch.controller..*(..)) && @annotation(redisLimit)")
    public Object redisLimiter(ProceedingJoinPoint proceedingJoinPoint, RedisLimit redisLimit) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (RedisLimitTool.limit(jedis, redisLimit.keyPrefix(), redisLimit.limit())) {
                return proceedingJoinPoint.proceed();
            } else {
                log.error("限流：" + redisLimit.keyPrefix());
                return null;
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return null;
    }
}
