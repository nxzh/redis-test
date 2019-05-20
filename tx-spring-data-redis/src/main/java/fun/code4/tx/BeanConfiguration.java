package fun.code4.tx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BeanConfiguration {

    @Bean
    public ArticleService articleService(RedisTemplate redisTemplate) {
        return new ArticleService(redisTemplate);
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory rcf) {
        RedisTemplate srt = new RedisTemplate();
        srt.setConnectionFactory(rcf);
        srt.setKeySerializer(keySerializer());
        srt.setHashKeySerializer(keySerializer());
        srt.setValueSerializer(valueSerializer());
        srt.setHashValueSerializer(valueSerializer());
//        srt.setEnableTransactionSupport(true);
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
}
