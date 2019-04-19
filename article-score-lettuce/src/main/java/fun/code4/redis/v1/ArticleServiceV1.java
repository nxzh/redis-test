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
      long cutoff = Instant.now().getEpochSecond() - ONE_WEEK_IN_SECONDS;
      // 获取 article 的发帖时间
      // zset操作: time:
      // 获取 article:article_id 的分数(即创建时间)
      Double createTime = commands.zscore(RedisKeys.TIME, RedisKeys.articleKey(articleId));
      System.out.println("createTime == " + createTime);
      if (createTime < cutoff) {
        return;
      }
      // 添加投票人
      // set 操作: voted:article_id
      // 将 user:user_id 加入到集合
      if (commands.sadd(votedKey(articleId), RedisKeys.userKey(userId)) == 1L) {
        // 加上一票的分数
        // zset 操作: score:
        // 找到 article:article_id 添加一票的分数
        commands.zincrby(RedisKeys.SCORE, VOTE_SCORE, RedisKeys.articleKey(articleId));
        // 更新票数
        // hash 操作: article:article_id
        // 原有票数 + 1
        commands.hincrby(RedisKeys.articleKey(articleId), RedisKeys.ARTICLE_VOTES, 1L);
      }
    }
  }

  public long post(long userId, ArticleV1 article) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      // 获取下一个 article id
      long nextArticleId = commands.incr(RedisKeys.ARTICLE);
      // 创建帖子得分集合, 第一个投票人为创建者
      // set 操作: voted:article_id
      // 将 user:user_id 放到 voted:article_id 中
      commands.sadd(RedisKeys.votedKey(nextArticleId), RedisKeys.userKey(userId));
      // 设置一周后不能投票.
      // voted:article_id
      // 设置过期时间
      commands.expire(RedisKeys.votedKey(nextArticleId), ONE_WEEK_IN_SECONDS);
      // 新建 hash 表
      // hash 操作: article:article_id
      // 存放数据存放到 article:article_id
      commands.hmset(RedisKeys.articleKey(nextArticleId), article.toMap());
      long now = Instant.now().getEpochSecond();
      // 设置初始 score
      // zset 操作: score:
      // 往集合中添加元素, key为article:article_id, 分值为发帖时间 + 1次投票的分数
      commands.zadd(
          RedisKeys.SCORE,
          now + VOTE_SCORE,
          RedisKeys.articleKey(nextArticleId));
      // 记录 article 的发帖时间
      // zset 操作: time:
      // 往集合中添加元素, key 为 article:article_id 分值为发帖时间
      commands.zadd(RedisKeys.TIME, now, RedisKeys.articleKey(nextArticleId));
      return nextArticleId;
    }
  }

  public List<ArticleV1> getArticles(int page, int size) {
    List<ArticleV1> articles = new ArrayList<>(size);
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      int start = (page - 1) * ARTICLES_PER_PAGE;
      int end = start + ARTICLES_PER_PAGE - 1;

      // 查找符合情况的 id 列表
      // zset 操作: score:
      // 按分数倒序返回 id 列表
      List<String> ids = commands.zrevrange(RedisKeys.SCORE, start, end);
      ids.forEach(
          id -> {
            // 按 id 查询
            // hash 操作: zset
            // 返回 key 为 article:article_id 的所有数据
            Map<String, String> dataMap = commands.hgetall(id);
            dataMap.put("id", id);
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
