package fun.code4.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class BeanConfiguration {

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("10.61.213.105", 8087);
    config.setDatabase(1);
    config.setPassword("P@s5word");
    LettuceConnectionFactory lcf = new LettuceConnectionFactory(config);
//    lcf.setShareNativeConnection(false);
    return lcf;
  }

  @Bean
  public ArticleService articleService() {
    return new ArticleService();
  }
}
