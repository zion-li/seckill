package com.myself.seckill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * redis缓存配置类
 *
 * @author Created by zion
 * @Date 2018/7/23.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//
//    /**
//     * 注解@Cache key生成规则
//     */
//
//    @Override
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return (target, method, params) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getName());
//            sb.append(method.getName());
//            for (Object obj : params) {
//                sb.append(obj.toString());
//            }
//            return sb.toString();
//        };
//    }
//
//    //缓存管理器
//    @Bean
//    @Primary
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        //user信息缓存配置
//        RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(timeout))
//                .disableCachingNullValues()
//                .prefixKeysWith("seckill");
//        //设置一个初始化的缓存空间set集合
//        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
//        // 对每个缓存空间应用不同的配置
//        redisCacheConfigurationMap.put("seckill", userCacheConfiguration);
//        //初始化一个RedisCacheWriter
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
//
//        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
//
//        //设置默认超过期时间是30秒
//        defaultCacheConfig.entryTtl(Duration.ofSeconds(timeout));
//        //初始化RedisCacheManager
//        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig, redisCacheConfigurationMap);
//    }
}
