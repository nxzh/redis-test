package fun.code4.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BeanConfiguration {

  @Autowired
  private Sub1 sub1;
  @Autowired
  private Sub2 sub2;

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
    config.setDatabase(13);
    config.setPassword("P@s5word");
//    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config);
    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config, poolingConfig);
    lcf.setShareNativeConnection(false);
    return lcf;
  }

  @Bean
  public RedisMessageListenerContainer messageListenerContainer(
      RedisConnectionFactory connectionFactory) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(sub1, new ChannelTopic("ddd"));
    container.addMessageListener(sub2, new ChannelTopic("eee"));
    return container;
  }

//  @Bean
//  public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    return new StringRedisTemplate(redisConnectionFactory);
//  }

  @Bean
  public RedisTemplate redisTemplate(RedisConnectionFactory rcf) {
    RedisTemplate srt = new RedisTemplate();
    srt.setConnectionFactory(rcf);
    srt.setKeySerializer(stringRedisSerializer());
    srt.setHashKeySerializer(stringRedisSerializer());
    srt.setValueSerializer(jackson2JsonRedisSerializer());
    srt.setHashValueSerializer(jackson2JsonRedisSerializer());
    return srt;
  }

  @Bean
  public StringRedisSerializer stringRedisSerializer() {
    return new StringRedisSerializer();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(new AfterburnerModule());
    return objectMapper;
  }

  @Bean
  public GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(
        objectMapper());
    return serializer;
  }

}
