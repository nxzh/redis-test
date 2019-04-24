package fun.code4.redis;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Article {

  private long id;
  private String title;
  private String link;
  private String poster;
  private long time;
  private int votes;
  private int downVotes;

  public Article() {
  }
  public Article(String title, String link, String poster, long time, int votes, int dVotes) {
    this.title = title;
    this.link = link;
    this.poster = poster;
    this.time = time;
    this.votes = votes;
    this.downVotes = dVotes;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  public int getVotes() {
    return votes;
  }

  private void setVotes(int votes) {
    this.votes = votes;
  }

  public int getDownVotes() {
    return downVotes;
  }

  public void setDownVotes(int downVotes) {
    this.downVotes = downVotes;
  }


  public static Article from(Map<String, String> dataMap) {
    Article article = new Article();
    article.setLink(dataMap.get("link"));
    article.setPoster(dataMap.get("poster"));
    article.setTime(Long.parseLong(dataMap.get("time")));
    article.setVotes(Integer.parseInt(dataMap.get("votes")));
    article.setTitle(dataMap.get("title"));
    article.setDownVotes(Integer.parseInt(dataMap.get("downVotes")));
    String idVal = dataMap.get("id");
    if (Objects.nonNull(idVal)) {
      article.setId(Long.parseLong(dataMap.get("id")));
    }
    return article;
  }

  public Map<String, String> toMap() {
    Map<String, String> ret = new HashMap<>();
    ret.put("title", this.getTitle());
    ret.put("link", this.getLink());
    ret.put("poster", this.getPoster());
    ret.put("time", Long.toString(this.getTime()));
    ret.put("votes", Integer.toString(this.getVotes()));
    ret.put("downVotes", Integer.toString(this.getDownVotes()));
    return ret;
  }

  @Override
  public String toString() {
    return "Article{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", link='" + link + '\'' +
        ", poster='" + poster + '\'' +
        ", time=" + time +
        ", votes=" + votes +
        ", downVotes=" + downVotes +
        '}';
  }
}