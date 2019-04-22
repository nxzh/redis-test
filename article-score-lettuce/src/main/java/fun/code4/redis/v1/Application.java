package fun.code4.redis.v1;

import fun.code4.redis.v1.ArticleServiceV1.RedisKeys;
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
    long now = Instant.now().getEpochSecond();
    articleV1Map.put(
        100001L, new ArticleV1("title1", "link1", "100001", now++, 0, 0));
    articleV1Map.put(
        100002L, new ArticleV1("title2", "link2", "100002", now++, 0, 0));
    articleV1Map.put(
        100003L, new ArticleV1("title3", "link3", "100003", now++, 0, 0));
    articleV1Map.put(
        100004L, new ArticleV1("title4", "link4", "100004", now++, 0, 0));
    articleV1Map.put(
        100005L, new ArticleV1("title5", "link5", "100005", now++, 0, 0));
    articleV1Map.put(
        100006L, new ArticleV1("title6", "link6", "100006", now++, 0, 0));
    return articleV1Map;
  }

  public static void main(String[] args) throws InterruptedException {
    ApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    ArticleServiceV1 articleService = ac.getBean("articleService", ArticleServiceV1.class);
    System.out.println(articleService);
    // 创建帖子
    Map<Long, ArticleV1> map = getArticleMap();
    List<Long> ids = new ArrayList<>();
    for (Entry<Long, ArticleV1> entry : map.entrySet()) {
      long id = articleService.post(entry.getKey(), entry.getValue());
      Thread.sleep(1000);
      ids.add(id);
      System.out.println("New Post with id: " + id);
    }
    // 获取帖子
    List<ArticleV1> articleV1s = articleService.getArticles(null, 1, 10);
    for (ArticleV1 articleV1 : articleV1s) {
      System.out.println(articleV1);
    }
    // 投票
    articleService.upVote(100002, 6);
    articleService.upVote(100003, 6);
    articleService.upVote(100004, 6);
    articleService.upVote(100006, 6);
    // 投反对票
    articleService.downVote(100005, 6);
    articleService.downVote(100006, 6);

    articleService.addRemoveGroups(1L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(2L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(3L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(4L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(5L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(6L, new String[]{"programing", "java"}, null);
    articleService.addRemoveGroups(3L, null, new String[]{"java"});
    List<ArticleV1> groupArticles = articleService.getGroupArticles("java", null, 1, 10);
    for (ArticleV1 articleV1 : groupArticles) {
      System.out.println(articleV1);
    }
  }
}