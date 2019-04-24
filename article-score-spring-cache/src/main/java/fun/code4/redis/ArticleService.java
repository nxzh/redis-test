package fun.code4.redis;


import java.time.Instant;

public class ArticleService {


  public long create(Article article) {
    return article.getId();
  }

  public void update(Article article) {

  }

  public Article find(long id) {
    Article article = new Article("title" + id, "link" + id,
        "100003", Instant.now().getEpochSecond(), 0, 0);
    article.setId(id);
    return article;
  }

  public void delete(long id) {

  }
}