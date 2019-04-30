package fun.code4.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component("sub1")
public class Sub1 implements MessageListener {


  @Autowired
  private StringRedisSerializer stringRedisSerializer;
  @Autowired
  private GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    byte[] body = message.getBody();
    byte[] channel = message.getChannel();
    User msg = jackson2JsonRedisSerializer.deserialize(body, User.class);
    String topic = (String) stringRedisSerializer.deserialize(channel);
    System.out.println("Sub1 Received: " + topic + " " + msg);
  }
}
