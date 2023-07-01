package tech.wedev.wecombatch.tool;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * 同样利用 Redis+LUA的组合  可以对 任意方法限制一秒内 任意次数的访问
 * 以 指定的前缀和当前时间的秒数组合为 KEY
 * @author developer1@wedev.tech
 * @create 2022/10/25
 */
public class RedisLimitTool {
    private static final String LUA_LIMIT_SCRIPT = "local key = KEYS[1]\n" +
            "local limit = tonumber(ARGV[1])\n" +
            "local current = tonumber(redis.call('get', key) or \"0\")\n" +
            "if current + 1 > limit then\n" +
            "   return 0\n" +
            "else\n" +
            "   redis.call(\"INCRBY\", key,\"1\")\n" +
            "   redis.call(\"expire\", key,\"2\")\n" +
            "   return 1\n" +
            "end";

    private static final Long SUCCESS_CODE = 1L;

    public static Boolean limit(Jedis jedis, String keyPrefix, String limit) {
        String key = keyPrefix + ":" + System.currentTimeMillis() / 1000;
        Long res = (Long) jedis.evalsha("b8059ba43af6ffe8bed3db65bac35d452f8115d8", Collections.singletonList(key), Collections.singletonList(limit));
        return SUCCESS_CODE.equals(res);
    }
}
