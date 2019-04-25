package fun.code4.redis;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BeanConfiguration {

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(600))
//        .prefixKeysWith("ttt")
        .disableCachingNullValues()
        .serializeKeysWith(SerializationPair.fromSerializer(keySerializer()))
        .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer()));
    return cacheConfig;
  }

  @Bean
  public RedisSerializer<String> keySerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public RedisSerializer<Object> valueSerializer() {
    return new GenericJackson2JsonRedisSerializer();
  }

  @Bean
  public ArticleService articleService() {
    return new ArticleService();
  }
}
