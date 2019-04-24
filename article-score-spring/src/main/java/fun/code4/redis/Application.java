package fun.code4.redis;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  private ArticleService articleService;


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

  private static Map<Long, Article> getArticleMap() {
    Map<Long, Article> articleV1Map = new HashMap<>();
    long now = Instant.now().getEpochSecond();
    articleV1Map.put(
        100001L, new Article("title1", "link1", "100001", now++, 0, 0));
    articleV1Map.put(
        100002L, new Article("title2", "link2", "100002", now++, 0, 0));
    articleV1Map.put(
        100003L, new Article("title3", "link3", "100003", now++, 0, 0));
    articleV1Map.put(
        100004L, new Article("title4", "link4", "100004", now++, 0, 0));
    articleV1Map.put(
        100005L, new Article("title5", "link5", "100005", now++, 0, 0));
    articleV1Map.put(
        100006L, new Article("title6", "link6", "100006", now++, 0, 0));
    return articleV1Map;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Override
  public void run(String... args) throws Exception {
    // 创建帖子
    Map<Long, Article> map = getArticleMap();
    List<Long> ids = new ArrayList<>();
    for (Entry<Long, Article> entry : map.entrySet()) {
      long id = articleService.post(entry.getKey(), entry.getValue());
      Thread.sleep(1000);
      ids.add(id);
      System.out.println("New Post with id: " + id);
    }
    // 获取帖子
    List<Article> articleV1s = articleService.getArticles(1, 10);
    for (Article articleV1 : articleV1s) {
      System.out.println(articleV1);
    }
    // 投票
    articleService.upVote(100002, 6);
    articleService.upVote(100003, 6);
    articleService.upVote(100004, 6);
    articleService.upVote(100006, 6);

//    // 投反对票
    articleService.downVote(100005, 6);
    articleService.downVote(100006, 6);

    articleService.addRemoveGroups(1L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(2L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(3L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(4L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(5L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(6L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(3L, null, new String[]{"java"});
    List<Article> groupArticles = articleService.getGroupArticles("java", 1, 10);
    for (Article articleV1 : groupArticles) {
      System.out.println(articleV1);
    }
  }
}
