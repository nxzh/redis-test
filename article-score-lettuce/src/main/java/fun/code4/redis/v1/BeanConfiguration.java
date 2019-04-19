package fun.code4.redis.v1;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean(destroyMethod = "shutdown")
  public RedisClient redisClient() {
    RedisURI redisURI = RedisURI.Builder.redis("10.61.213.105", 8087).withPassword("P@s5word").withDatabase(0).build();
    RedisClient redisClient = RedisClient.create(redisURI);
    return redisClient;
  }

  @Bean
  public ArticleServiceV1 articleService(RedisClient redisClient) {
    return new ArticleServiceV1(redisClient);
  }
}
