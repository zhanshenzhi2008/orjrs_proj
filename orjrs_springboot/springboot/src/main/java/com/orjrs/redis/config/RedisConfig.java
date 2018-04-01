package com.orjrs.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getPassword() {
        return password;
    }

    public int getDatabase() {
        return database;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    /**
     * 自定义缓存key生成策略:注解@Cache key生成规则
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                Stream.of(params).forEach(sb::append);
                return sb.toString();
            }
        };
    }

    /**
     * 缓存管理器:注解@Cache的管理器，设置过期时间的单位是秒
     *
     * @param redisTemplate redis模板
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间
        cacheManager.setDefaultExpiration(10000);//设置key-value超时时间
        return cacheManager;
    }

    /**
     * @param factory
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        /*Map<String, Long> expires=new HashMap<String, Long>();
        expires.put("user", 6000L);
        expires.put("city", 600L);
        cacheManager.setExpires(expires);*/
        // 如果方法上有Long等非String类型的话，会报类型转换错误，故设置序列化工具
        setSerializer(redisTemplate);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 序列化StringRedisTemplate
     *
     * @param redisTemplate redis模板
     */
    private void setSerializer(RedisTemplate<?, ?> redisTemplate) {
        Jackson2JsonRedisSerializer jackson2JsonSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"));// 处理日期格式
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(jackson2JsonSerializer);
        redisTemplate.setValueSerializer(jackson2JsonSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonSerializer);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setStringSerializer(redisSerializer);

        //key序列化方式;但是如果方法上有Long等非String类型的话，会报类型转换错误；
       /* RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);*/

        //JdkSerializationRedisSerializer序列化方式;
//        JdkSerializationRedisSerializer jdkRedisSerializer = new JdkSerializationRedisSerializer();
//        redisTemplate.setValueSerializer(jdkRedisSerializer);
//        redisTemplate.setHashValueSerializer(jdkRedisSerializer);

    }

    /**
     * redis连接的基础设置
     *
     * @return JedisConnectionFactory
     */
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setDatabase(this.database);
        factory.setHostName(this.host);
        factory.setPort(this.port);
        factory.setTimeout(this.timeout);
        factory.setUsePool(true);
        factory.setPoolConfig(jedisPoolConfig());
        return factory;
    }

    /**
     * redis连接的基础设置
     *
     * @return JedisPoolConfig
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMinIdle(this.minIdle);
        return jedisPoolConfig;
    }

    /**
     * redis数据操作异常处理
     * 这里的处理：在日志中打印出错误信息，但是放行
     * 保证redis服务器出现连接等问题的时候不影响程序的正常运行，使得能够出问题时不用缓存
     *
     * @return
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                LOGGER.error("redis异常：key=[{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                LOGGER.error("redis异常：key=[{}]", key, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                LOGGER.error("redis异常：key=[{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                LOGGER.error("redis异常：", e);
            }
        };
        return cacheErrorHandler;
    }
}
