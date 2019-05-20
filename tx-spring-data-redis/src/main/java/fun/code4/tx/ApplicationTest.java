package fun.code4.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

@SpringBootApplication
public class ApplicationTest implements CommandLineRunner {

    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class);
    }


    private void runInPipeline2() {
        List<Object> results = redisTemplate.executePipelined(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                return null;
            }
        });
    }

    private void runInPipeline() {
        redisTemplate.setScriptExecutor(new ScriptExecutor() {
            @Override
            public Object execute(RedisScript script, List keys, Object... args) {
                return null;
            }

            @Override
            public Object execute(RedisScript script, RedisSerializer argsSerializer, RedisSerializer resultSerializer, List keys, Object... args) {
                return null;
            }
        });
        List<Object> results = redisTemplate.executePipelined(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations)
                    throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment("key");
                operations.opsForValue().increment("key");
//                if (1 == 1)
//                    throw new RuntimeException();
                operations.opsForValue().increment("key");
                operations.opsForValue().increment("key");
                operations.opsForValue().increment("key");
                operations.opsForValue().increment("key");
                operations.exec();
                return null; //!! Must return null here
            }
        });
        System.out.println(results);
    }

    private void runBeared() {
        Object result = redisTemplate.execute(new SessionCallback() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment("key");
                operations.opsForValue().increment("key");
//        if (1==1)
//        throw new RuntimeException();
                Long value = operations.opsForValue().increment("key");
                System.out.println(value);
                operations.opsForValue().increment("key");
                return operations.exec();
            }
        });
        System.out.println(result);
    }

    @Override
    public void run(String... args) throws Exception {
        runBeared();
        // runInPipeline();
    }
}
