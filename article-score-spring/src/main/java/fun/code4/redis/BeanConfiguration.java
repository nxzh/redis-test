package fun.code4.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BeanConfiguration {

  @Bean
  public GenericObjectPoolConfig genericObjectPoolConfig() {
    GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
    genericObjectPoolConfig.setMaxIdle(2);
    genericObjectPoolConfig.setMaxTotal(2);
    genericObjectPoolConfig.setMinIdle(1);
    genericObjectPoolConfig.setMaxWaitMillis(-1);
    return genericObjectPoolConfig;
  }

  @Bean
  public LettucePoolingClientConfiguration poolingClientConfiguration(
      GenericObjectPoolConfig genericObjectPoolConfig) {
    return LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(
      LettucePoolingClientConfiguration poolingConfig) {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("10.61.213.105", 8087);
    config.setDatabase(1);
    config.setPassword("P@s5word");
//    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config);
    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config, poolingConfig);
    lcf.setShareNativeConnection(false);
    lcf.setDatabase(2);
    return lcf;
  }

//  @Bean
//  public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    return new StringRedisTemplate(redisConnectionFactory);
//  }

  @Bean
  public RedisTemplate redisTemplate(RedisConnectionFactory rcf) {
    RedisTemplate srt = new RedisTemplate();
    srt.setConnectionFactory(rcf);
    srt.setKeySerializer(keySerializer());
    srt.setHashKeySerializer(keySerializer());
    srt.setValueSerializer(valueSerializer());
    srt.setHashValueSerializer(valueSerializer());
    return srt;
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
  public ArticleService articleService(RedisTemplate redisTemplate) {
    return new ArticleService(redisTemplate);
  }
}
