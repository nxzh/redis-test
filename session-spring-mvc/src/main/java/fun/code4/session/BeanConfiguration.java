package fun.code4.session;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(redisNamespace = "XXX")
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
    config.setDatabase(12);
    config.setPassword("P@s5word");
//    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config);
    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config, poolingConfig);
    lcf.setShareNativeConnection(false);
    return lcf;
  }

  @Bean
  public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    return new GenericJackson2JsonRedisSerializer();
  }
}
