package fun.code4.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component("sub2")
public class Sub2 implements MessageListener {

  @Autowired
  private StringRedisSerializer stringRedisSerializer;
  @Autowired
  private GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    byte[] body = message.getBody();//请使用valueSerializer
    byte[] channel = message.getChannel();
    String topic = stringRedisSerializer.deserialize(channel);
    User msg = jackson2JsonRedisSerializer.deserialize(body, User.class);
    System.out.println("Sub2 received: " + topic + " " + msg);
  }
}
