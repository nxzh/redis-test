package fun.code4.redis;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application implements CommandLineRunner {

  @Autowired
  private ArticleService articleService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Override
  public void run(String... args) throws Exception {
    Article article = new Article("article1", "link1", "1000001", Instant.now().getEpochSecond(), 0,
        0);
    article = articleService.create(article);
//    articleService.createOnException(article);
    article.setId(1);
    article.setDownVotes(1);
    articleService.update(article);
    articleService.find(2);
    articleService.find(2);
//    articleService.delete(1);
  }
}
