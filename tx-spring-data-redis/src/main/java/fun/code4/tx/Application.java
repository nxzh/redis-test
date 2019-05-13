package fun.code4.tx;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired private RedisTemplate redisTemplate;

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Override
  public void run(String... args) throws Exception {
    List<Object> results = redisTemplate.executePipelined(new SessionCallback<List<Object>>() {
      @Override
      public List<Object> execute(RedisOperations operations)
          throws DataAccessException {
        operations.multi();
        operations.opsForValue().increment("key");
        return operations.exec();
      }
    });
    System.out.println(results);
  }
}
