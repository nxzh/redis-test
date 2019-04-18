package fun.code4.redis.v1;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

  private List<Long> getUsers() {
    List<Long> users = new ArrayList<>();
    users.add(100001L);
    users.add(100002L);
    users.add(100003L);
    users.add(100004L);
    users.add(100005L);
    users.add(100006L);
    return users;
  }

  private static Map<Long, ArticleV1> getArticleMap() {
    Map<Long, ArticleV1> articleV1Map = new HashMap<>();
    articleV1Map.put(
        100001L, new ArticleV1("title1", "link1", "user1", Instant.now().getEpochSecond(), 0));
    articleV1Map.put(
        100002L, new ArticleV1("title2", "link2", "user2", Instant.now().getEpochSecond(), 0));
    articleV1Map.put(
        100003L, new ArticleV1("title3", "link3", "user3", Instant.now().getEpochSecond(), 0));
    articleV1Map.put(
        100004L, new ArticleV1("title4", "link4", "user4", Instant.now().getEpochSecond(), 0));
    articleV1Map.put(
        100005L, new ArticleV1("title5", "link5", "user5", Instant.now().getEpochSecond(), 0));
    articleV1Map.put(
        100006L, new ArticleV1("title6", "link6", "user6", Instant.now().getEpochSecond(), 0));
    return articleV1Map;
  }

  public static void main(String[] args) {
    ApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    ArticleServiceV1 articleService = ac.getBean("articleService", ArticleServiceV1.class);
    System.out.println(articleService);
    Map<Long, ArticleV1> map = getArticleMap();
    List<Long> ids = new ArrayList<>();
    for (Entry<Long, ArticleV1> entry : map.entrySet()) {
      long id = articleService.post(entry.getKey(), entry.getValue());
      ids.add(id);
      System.out.println("New Post with id: " + id);
    }
  }
}
