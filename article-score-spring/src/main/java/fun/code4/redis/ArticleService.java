package fun.code4.redis;

import java.time.Instant;
import java.util.List;
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
    long nextArticleId =
    return 1L;
  }

  public List<Article> getArticles(String order, int page, int size) {

    return null;
  }

  public void addRemoveGroups(long articleId, String[] toAdd, String[] toRemove) {}

  public List<Article> getGroupArticles(String group, String order, int page, int size) {

    return null;
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
