package fun.code4.redis.v1.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Article {

  private Long id;
  private String title;
  private String link;
  private String poster;
  private String time;
  private Integer votes;

  public Long getId() {
    return id;
  }

  private void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  private void setTitle(String title) {
    this.title = title;
  }

  public String getLink() {
    return link;
  }

  private void setLink(String link) {
    this.link = link;
  }

  public String getPoster() {
    return poster;
  }

  private void setPoster(String poster) {
    this.poster = poster;
  }

  public String getTime() {
    return time;
  }

  private void setTime(String time) {
    this.time = time;
  }

  public Integer getVotes() {
    return votes;
  }

  private void setVotes(Integer votes) {
    this.votes = votes;
  }

  public static Article from(Map<String, String> dataMap) {
    Article article = new Article();
    article.setLink(dataMap.get("link"));
    article.setPoster(dataMap.get("poster"));
    article.setTime(dataMap.get("time"));
    article.setVotes(Integer.parseInt(dataMap.get("votes")));
    article.setTitle(dataMap.get("title"));
    String idVal = dataMap.get("id");
    if (Objects.nonNull(idVal)) {
      article.setId(Long.parseLong(dataMap.get("id")));
    }
    return article;
  }

  public static Map<String, String> to(Article article) {
    Map<String, String> ret = new HashMap<>();
    ret.put("id", article.getId().toString());
    ret.put("title", article.getTitle());
    ret.put("link", article.getLink());
    ret.put("poster", article.getPoster());
    ret.put("time", article.getTime());
    ret.put("votes", article.getVotes().toString());
    return ret;
  }
}
