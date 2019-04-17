package fun.code4.redis.v1.config;

import fun.code4.redis.v1.service.ArticleService;
import io.lettuce.core.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean(destroyMethod = "shutdown")
  public RedisClient redisClient() {
    RedisClient redisClient = RedisClient.create("redis://10.61.213.105:8087/0");
    return redisClient;
  }

  @Bean
  public ArticleService articleService(RedisClient redisClient) {
    return new ArticleService(redisClient);
  }
}
