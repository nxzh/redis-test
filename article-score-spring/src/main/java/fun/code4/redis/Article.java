package fun.code4.redis;

import java.util.ArrayList;
import java.util.List;

public class Article extends IdentifiedArticle {

  private String title;
  private String link;
  private String poster;
  private long time;
  private int votes;
  private int downVotes;
  private List<String> tags = new ArrayList<>();

  public Article() {
    initTags();
  }

  public Article(String title, String link, String poster, long time, int votes, int dVotes) {
    this.title = title;
    this.link = link;
    this.poster = poster;
    this.time = time;
    this.votes = votes;
    this.downVotes = dVotes;
    initTags();
  }

  private void initTags() {
    tags.add("A");
    tags.add("B");
    tags.add("C");
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
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

  @Override
  public String toString() {
    return "Article{" +
        "title='" + title + '\'' +
        ", link='" + link + '\'' +
        ", poster='" + poster + '\'' +
        ", time=" + time +
        ", votes=" + votes +
        ", downVotes=" + downVotes +
        ", tags=" + tags +
        '}';
  }
}
