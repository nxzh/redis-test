package fun.code4.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  private Pub pub;

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Override
  public void run(String... args) throws Exception {

    pub.sendMessage("ddd", new User("gdsgds", Gender.MALE));
    pub.sendMessage("eee", new User("sdfs", Gender.FEMALE));

    Thread.sleep(1000);
  }
}
