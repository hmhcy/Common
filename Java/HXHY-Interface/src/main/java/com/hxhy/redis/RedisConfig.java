package com.hxhy.redis;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig {

	public static Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private Integer redisPort;

	@Value("${spring.redis.timeout}")
	private Integer redisTimeout;

	@Value("${spring.redis.maxIdle}")
	private Integer redisMaxIdle;

	@Value("${spring.redis.maxWait}")
	private Integer redisMaxWait;

	@Value("${spring.redis.maxTotal}")
	private Integer redisMaxTotal;

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(redisMaxTotal);
		poolConfig.setMaxIdle(redisMaxIdle);
		poolConfig.setMaxWaitMillis(redisMaxWait);
		poolConfig.setMinEvictableIdleTimeMillis(60000);
		poolConfig.setTimeBetweenEvictionRunsMillis(30000);
		poolConfig.setNumTestsPerEvictionRun(-1);

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisHost);
		redisStandaloneConfiguration.setPort(redisPort);
//		redisStandaloneConfiguration.setDatabase(0);
//		redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));

		JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
		jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));// 60s connection timeout

		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
				jedisClientConfiguration.build());

		return jedisConFactory;
	}


	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		try {
			template.setConnectionFactory(redisConnectionFactory());
			template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
			template.setKeySerializer(new StringRedisSerializer());
			template.setHashKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new JdkSerializationRedisSerializer());
			template.setHashValueSerializer(new JdkSerializationRedisSerializer());
		}catch(Throwable t) {
				LOGGER.warn("Error executing cache operation: {}", t.getMessage());
              return null;
		}
		
		return template;
	}
	
	@Bean
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
	    //user信息缓存配置
	    RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().
	    		entryTtl(Duration.ofMinutes(30)).
	    		disableCachingNullValues().prefixKeysWith("user");
	    //product信息缓存配置
	    RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().
	    		entryTtl(Duration.ofMinutes(10)).disableCachingNullValues().prefixKeysWith("product");
	    Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
	    redisCacheConfigurationMap.put("user", userCacheConfiguration);
	    redisCacheConfigurationMap.put("product", productCacheConfiguration);
	    //初始化一个RedisCacheWriter
	    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
	    
	    
	    //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
	    //ClassLoader loader = this.getClass().getClassLoader();
	    //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
	    //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
	    //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
	    
	    
	    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(30));
	    //初始化RedisCacheManager
	    RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig, redisCacheConfigurationMap);
	    return cacheManager;
	}
	
}