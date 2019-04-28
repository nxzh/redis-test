package fun.code4.session;

import java.time.Duration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

@Configuration
public class BeanConfiguration {
//
//
//
//  @Bean
//  public GenericObjectPoolConfig genericObjectPoolConfig() {
//    GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
//    genericObjectPoolConfig.setMaxIdle(2);
//    genericObjectPoolConfig.setMaxTotal(2);
//    genericObjectPoolConfig.setMinIdle(1);
//    genericObjectPoolConfig.setMaxWaitMillis(-1);
//    return genericObjectPoolConfig;
//  }
//
//  @Bean
//  public LettucePoolingClientConfiguration poolingClientConfiguration(
//      GenericObjectPoolConfig genericObjectPoolConfig) {
//    return LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
//  }
//
//  @Bean
//  public LettuceConnectionFactory redisConnectionFactory(
//      LettucePoolingClientConfiguration poolingConfig) {
//    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("192.168.1.101", 6379);
//    config.setDatabase(2);
//    config.setPassword("111111");
////    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config);
//    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config, poolingConfig);
//    lcf.setShareNativeConnection(false);
//    lcf.setDatabase(2);
//    return lcf;
//  }
////  @Bean
////  public RedisHttpSessionConfiguration sessionConfiguration(RedisConnectionFactory redisConnectionFactory) {
////    RedisHttpSessionConfiguration sessionConfiguration = new RedisHttpSessionConfiguration();
////    sessionConfiguration.setRedisConnectionFactory(redisConnectionFactory);
////    sessionConfiguration.set
////    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
////        .entryTtl(Duration.ofSeconds(600))
//////        .prefixKeysWith("ttt")
////        .disableCachingNullValues()
////        .serializeKeysWith(SerializationPair.fromSerializer(keySerializer()))
////        .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer()));
////    return cacheConfig;
////  }
//
//  @Bean
//  public RedisSerializer<String> keySerializer() {
//    return new StringRedisSerializer();
//  }
//
//  @Bean
//  public RedisSerializer<Object> valueSerializer() {
//    return new GenericJackson2JsonRedisSerializer();
//  }
}
