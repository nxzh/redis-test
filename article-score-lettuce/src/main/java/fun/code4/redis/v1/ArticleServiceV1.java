package fun.code4.redis.v1;

import static fun.code4.redis.v1.ArticleServiceV1.RedisKeys.votedKey;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleServiceV1 {

  private static final Integer ONE_WEEK_IN_SECONDS = 7 * 86400;
  private static final Integer VOTE_SCORE = 432;
  private static final Integer ARTICLES_PER_PAGE = 5;

  private RedisClient redisClient;

  public ArticleServiceV1(RedisClient redisClient) {
    this.redisClient = redisClient;
  }

  public void shutdown() {
    if (redisClient != null) {
      redisClient.shutdown();
    }
  }

  public void vote(long userId, long articleId) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      Long cutoff = Instant.now().getEpochSecond() - ONE_WEEK_IN_SECONDS;
      Double createTime = commands.zscore(RedisKeys.TIME, RedisKeys.articleKey(articleId));
      System.out.println("createTime == " + createTime);
      if (createTime < cutoff) {
        return;
      }
      if (commands.sadd(votedKey(articleId), RedisKeys.userKey(userId)) == 1L) {
        commands.zincrby(RedisKeys.SCORE, VOTE_SCORE, RedisKeys.articleKey(articleId));
        commands.hincrby(RedisKeys.articleKey(articleId), RedisKeys.ARTICLE_VOTES, 1L);
      }
    }
  }

  public long post(long userId, ArticleV1 article) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      // 获取下一个 article id
      long nextArticleId = commands.incr(RedisKeys.ARTICLE);
      // 将 user:user_id 放到 voted:article_id 中
      commands.sadd(RedisKeys.votedKey(nextArticleId), RedisKeys.userKey(userId));
      commands.expire(RedisKeys.votedKey(nextArticleId), ONE_WEEK_IN_SECONDS);
      commands.hmset(RedisKeys.articleKey(nextArticleId), article.toMap());
      commands.zadd(
          RedisKeys.SCORE,
          RedisKeys.articleKey(nextArticleId),
          Instant.now().getEpochSecond() + VOTE_SCORE);
      commands.zadd(RedisKeys.TIME, article, Instant.now().getEpochSecond());
      return nextArticleId;
    }
  }

  public List<ArticleV1> getArticles(int page, int size) {
    List<ArticleV1> articles = new ArrayList<>(size);
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      int start = (page - 1) * ARTICLES_PER_PAGE;
      int end = start + ARTICLES_PER_PAGE - 1;

      List<String> ids = commands.zrevrange(RedisKeys.SCORE, start, end);
      ids.forEach(
          id -> {
            Map<String, String> dataMap = commands.hgetall(id);
            articles.add(ArticleV1.from(dataMap));
          });
    }
    return articles;
  }

  interface RedisKeys {

    String TIME = "time:";
    String SCORE = "score:";
    String ARTICLE = "article:";
    String ARTICLE_VOTES = "votes";

    static String articleKey(long id) {
      return String.format("%s%d", "article:", id);
    }

    static String votedKey(long id) {
      return String.format("%s%d", "voted:", id);
    }

    static String userKey(long id) {
      return String.format("%s%d", "user:", id);
    }
  }
}
