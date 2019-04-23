package fun.code4.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class BeanConfiguration {

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("10.61.213.105", 6379);
    config.setDatabase(0);
    config.setPassword("P@s5word");
    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config);
    return lcf;
  }

  //  @Bean
  //  public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
  //    return new StringRedisTemplate(redisConnectionFactory);
  //  }

  @Bean
  public ArticleService articleService(RedisTemplate redisTemplate) {
    return new ArticleService(redisTemplate);
  }
}
