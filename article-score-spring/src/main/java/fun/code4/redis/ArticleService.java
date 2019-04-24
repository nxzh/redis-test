package fun.code4.redis;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.redis.core.RedisTemplate;

public class ArticleService {

  private static final Integer ONE_WEEK_IN_SECONDS = 7 * 86400;
  private static final Integer VOTE_SCORE = 432;
  private static final Integer ARTICLES_PER_PAGE = 5;

  private RedisTemplate<String, String> redisTemplate;

  public ArticleService(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void test() {
    System.out.println(redisTemplate);
  }

  private boolean checkCanScore(long cutoff, long articleId) {
    Double createTime =
        redisTemplate.opsForZSet().score(RedisKeys.TIME, RedisKeys.articleKey(articleId));
    if (createTime < cutoff) {
      return false;
    }
    return true;
  }

  public void downVote(long userId, long articleId) {
    long cutoff = Instant.now().getEpochSecond() - ONE_WEEK_IN_SECONDS;
    if (!checkCanScore(cutoff, articleId)) {
      return;
    }
    if (redisTemplate.opsForSet().add(RedisKeys.downVotedKey(articleId), RedisKeys.userKey(userId))
        == 1L) {
      redisTemplate
          .opsForZSet()
          .incrementScore(RedisKeys.SCORE, RedisKeys.articleKey(articleId), 0 - VOTE_SCORE);
      redisTemplate
          .opsForHash()
          .increment(RedisKeys.articleKey(articleId), RedisKeys.ARTICLE_DOWN_VOTES, 1L);
    }
  }

  public void upVote(long userId, long articleId) {
    long cutoff = Instant.now().getEpochSecond() - ONE_WEEK_IN_SECONDS;
    if (!checkCanScore(cutoff, articleId)) {
      return;
    }
    if (redisTemplate.opsForSet().add(RedisKeys.votedKey(articleId), RedisKeys.userKey(userId))
        == 1L) {
      redisTemplate
          .opsForZSet()
          .incrementScore(RedisKeys.SCORE, RedisKeys.articleKey(articleId), VOTE_SCORE);
      redisTemplate
          .opsForHash()
          .increment(RedisKeys.articleKey(articleId), RedisKeys.ARTICLE_VOTES, 1L);
    }
  }

  public long post(long userId, Article article) {
    long nextArticleId = redisTemplate.opsForValue().increment(RedisKeys.ARTICLE);
    redisTemplate.opsForSet().add(RedisKeys.votedKey(nextArticleId), RedisKeys.userKey(userId));
    redisTemplate.opsForSet().add(RedisKeys.downVotedKey(nextArticleId), RedisKeys.userKey(userId));
    redisTemplate.expire(RedisKeys.votedKey(nextArticleId), ONE_WEEK_IN_SECONDS, TimeUnit.SECONDS);
    redisTemplate.<String, String>opsForHash()
        .putAll(RedisKeys.articleKey(nextArticleId), article.toMap());
    long now = Instant.now().getEpochSecond();
    redisTemplate.opsForZSet()
        .add(RedisKeys.SCORE, RedisKeys.articleKey(nextArticleId), now + VOTE_SCORE);
    redisTemplate.opsForZSet().add(RedisKeys.TIME, RedisKeys.articleKey(nextArticleId), now);
    return nextArticleId;
  }

  public List<Article> getArticles(int page, int size) {
    return getArticles(RedisKeys.SCORE, page, size);
  }

  private List<Article> getArticles(String order, int page, int size) {
    List<Article> articles = new ArrayList<>(size);
    int start = (page - 1) * ARTICLES_PER_PAGE;
    int end = start + ARTICLES_PER_PAGE - 1;

    Set<String> ids = redisTemplate.opsForZSet().reverseRange(order, start, end);
    ids.forEach(
        id -> {
          Map<String, String> dataMap = redisTemplate.<String, String>opsForHash().entries(id);
          String idVal = id.substring(id.indexOf(':') + 1);
          dataMap.put("id", idVal);
          articles.add(Article.from(dataMap));
        });
    return articles;
  }

  public void addRemoveGroups(long articleId, String[] toAdd, String[] toRemove) {
    String articleKey = RedisKeys.articleKey(articleId);
    if (toAdd != null) {
      Arrays.stream(toAdd).forEach(e ->
          redisTemplate.opsForSet().add(RedisKeys.groupKey(e), articleKey));
    }
    if (toRemove != null) {
      Arrays.stream(toRemove)
          .forEach(e -> redisTemplate.opsForSet().remove(RedisKeys.groupKey(e), articleKey));
    }
  }

  public List<Article> getGroupArticles(String group, int page, int size) {
    return getGroupArticles(group, RedisKeys.SCORE, page, size);
  }

  private List<Article> getGroupArticles(String group, String order, int page, int size) {
    String scoreGroupKey = RedisKeys.scoreGroupKey(group);
    if (redisTemplate.countExistingKeys(Arrays.asList(scoreGroupKey)) == 0L) {
      redisTemplate.opsForZSet()
          .intersectAndStore(RedisKeys.groupKey(group), Arrays.asList(order), scoreGroupKey,
              Aggregate.MAX);
      redisTemplate.expire(scoreGroupKey, 600, TimeUnit.SECONDS);
    }
    return getArticles(scoreGroupKey, page, size);
  }

  interface RedisKeys {

    String TIME = "time:";
    String SCORE = "score:";
    String ARTICLE = "article:";
    String ARTICLE_VOTES = "votes";
    String ARTICLE_DOWN_VOTES = "downVotes";

    static String articleKey(long id) {
      return String.format("%s%d", "article:", id);
    }

    static String votedKey(long id) {
      return String.format("%s%d", "voted:", id);
    }

    static String userKey(long id) {
      return String.format("%s%d", "user:", id);
    }

    static String groupKey(String group) {
      return String.format("%s%s", "group:", group);
    }

    static String scoreGroupKey(String group) {
      return String.format("%s%s", "score:", group);
    }

    static String downVotedKey(long id) {
      return String.format("%s%d", "downVoted:", id);
    }
  }
}
