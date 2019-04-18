package fun.code4.redis.v1;

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
  public ArticleServiceV1 articleService(RedisClient redisClient) {
    return new ArticleServiceV1(redisClient);
  }
}
