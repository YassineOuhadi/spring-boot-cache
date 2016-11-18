package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching(mode = AdviceMode.PROXY, proxyTargetClass = false)
public class CacheConfiguration implements CachingConfigurer {
	/*
	 * redis.host=localhost
redis.port=6379
redis.cache.ttl=67676
	 */
	//@Value("${redis.host.name}")
    private String redisHostName="localhost";
//    @Value("${redis.port}")
    private  int redisPort=6379;
//    @Value("${redis.cache.ttl}")
    private  Long ttl=6379988L;

    @Bean
    public JedisConnectionFactory lruJedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHostName);
        factory.setPort(redisPort);
        factory.setUsePool(true);
        return factory;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public JdkSerializationRedisSerializer jdkSerializationRedisSerializer() {
        return new JdkSerializationRedisSerializer();
    }

    @Bean
    AnnotationCacheOperationSource annotationCacheOperationSource() {
        return new AnnotationCacheOperationSource();
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new CacheTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(lruJedisConnectionFactory());
        return redisTemplate;
    }

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
        cacheManager.setDefaultExpiration(ttl);
        return (CacheManager) cacheManager;
    }

    @Bean(name = "hourlyCacheManager")
    public CacheManager hourlyCacheManager() {
        final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
        cacheManager.setDefaultExpiration(3600);
        return (CacheManager) cacheManager;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Bean
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }
}
