package fun.code4.redis;

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

  private void setId(long id) {
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
}
