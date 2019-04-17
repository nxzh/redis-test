package fun.code4.redis.v1.service;

import static fun.code4.redis.v1.service.ArticleService.RedisKeys.votedKey;

import fun.code4.redis.v1.model.Article;
import fun.code4.redis.v2.model.Article;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.time.Instant;

public class ArticleService {

  private static final Integer ONE_WEEK_IN_SECONDS = 7 * 86400;
  private static final Integer VOTE_SCORE = 432;

  interface RedisKeys {

    String TIME = "time:";
    String SCORE = "score:";
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

  private RedisClient redisClient;

  public ArticleService(RedisClient redisClient) {
    this.redisClient = redisClient;
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

  public void post(long userId, Article article) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();


    }
  }

  public static void main(String[] args) {
    Instant instant = Instant.now();
    System.out.println(instant);
    System.out.println(instant.getEpochSecond());
  }
}
