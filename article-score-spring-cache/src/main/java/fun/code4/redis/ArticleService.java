package fun.code4.redis;


import java.time.Instant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class ArticleService {


  @CachePut(value = "article", key = "#article.id")
  public Article create(Article article) {
    article.setId(1);
    System.out.println("create article");
    return article;
  }

  @CachePut(value = "article", key = "#article.id")
  public long createOnException(Article article) {
    System.out.println("createOnException");
    throw new RuntimeException("!!!!!!!!!!!!!");
  }

  @CachePut(value = "article", key = "#article.id")
  public Article update(Article article) {
    System.out.println("Updating article");
    return article;
  }

  @CachePut(value = "article", key = "#article.id")
  public Article updateOnException(Article article) {
    System.out.println("updateOnException");
    throw new RuntimeException("!!!!!!!!!!!!!");
  }

  @Cacheable(value = "article", key = "#id")
  public Article find(long id) {
    System.out.println("find id:" + id);
    Article article = new Article("title" + id, "link" + id,
        "100003", Instant.now().getEpochSecond(), 0, 0);
    article.setId(id);
    return article;
  }

  @CacheEvict(value = "article", allEntries = true)
  public void delete(long id) {
    System.out.println("delete id:" + id);
  }
}