package fun.code4.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Pub {

  @Autowired
  private RedisTemplate redisTemplate;

  public void sendMessage(String channel, User message) {
    redisTemplate.convertAndSend(channel, message);
  }
}
