package fun.code4.redis.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArticleV1 {

  private Long id;
  private String title;
  private String link;
  private String poster;
  private long time;
  private Integer votes;

  public ArticleV1() {}

  public ArticleV1(String title, String link, String poster, long time, Integer votes) {
    this.title = title;
    this.link = link;
    this.poster = poster;
    this.time = time;
    this.votes = votes;
  }

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

  public long getTime() {
    return time;
  }

  private void setTime(long time) {
    this.time = time;
  }

  public Integer getVotes() {
    return votes;
  }

  private void setVotes(Integer votes) {
    this.votes = votes;
  }

  public static ArticleV1 from(Map<String, String> dataMap) {
    ArticleV1 article = new ArticleV1();
    article.setLink(dataMap.get("link"));
    article.setPoster(dataMap.get("poster"));
    article.setTime(Long.parseLong(dataMap.get("time")));
    article.setVotes(Integer.parseInt(dataMap.get("votes")));
    article.setTitle(dataMap.get("title"));
    String idVal = dataMap.get("id");
    if (Objects.nonNull(idVal)) {
      article.setId(Long.parseLong(dataMap.get("id")));
    }
    return article;
  }

  public Map<String, String> toMap() {
    Map<String, String> ret = new HashMap<>();
    ret.put("id", this.getId().toString());
    ret.put("title", this.getTitle());
    ret.put("link", this.getLink());
    ret.put("poster", this.getPoster());
    ret.put("time", Long.toString(this.getTime()));
    ret.put("votes", this.getVotes().toString());
    return ret;
  }
}
