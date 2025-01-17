package fun.code4.redis.v1;

import static fun.code4.redis.v1.ArticleServiceV1.RedisKeys.downVotedKey;
import static fun.code4.redis.v1.ArticleServiceV1.RedisKeys.votedKey;

import io.lettuce.core.RedisClient;
import io.lettuce.core.ZStoreArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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

  private boolean checkCanScore(long cutoff, long articleId) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      // 获取 article 的发帖时间
      // zset操作: time:
      // 获取 article:article_id 的分数(即创建时间)
      Double createTime = commands.zscore(RedisKeys.TIME, RedisKeys.articleKey(articleId));
      if (createTime < cutoff) {
        return false;
      }
    }
    return true;
  }

  public void downVote(long userId, long articleId) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      long cutoff = Instant.now().getEpochSecond() - ONE_WEEK_IN_SECONDS;
      if (!checkCanScore(cutoff, articleId)) {
        return;
      }
      if (commands.sadd(downVotedKey(articleId), RedisKeys.userKey(userId)) == 1L) {
        commands.zincrby(RedisKeys.SCORE, 0 - VOTE_SCORE, RedisKeys.articleKey(articleId));
        commands.hincrby(RedisKeys.articleKey(articleId), RedisKeys.ARTICLE_DOWN_VOTES, 1L);
      }
    }
  }

  public void upVote(long userId, long articleId) {
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      long cutoff = Instant.now().getEpochSecond() - ONE_WEEK_IN_SECONDS;
      if (!checkCanScore(cutoff, articleId)) {
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
      // 创建帖子得分集合, 帖子创建者不能投票
      // set 操作: voted:article_id
      // 将 user:user_id 放到 voted:article_id 中
      commands.sadd(RedisKeys.votedKey(nextArticleId), RedisKeys.userKey(userId));
      // 帖子创建者不能投反对票
      commands.sadd(RedisKeys.downVotedKey(nextArticleId), RedisKeys.userKey(userId));
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

  public List<ArticleV1> getArticles(String order, int page, int size) {
    if (order == null) {
      order = RedisKeys.SCORE;
    }
    List<ArticleV1> articles = new ArrayList<>(size);
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      int start = (page - 1) * ARTICLES_PER_PAGE;
      int end = start + ARTICLES_PER_PAGE - 1;

      // 查找符合情况的 id 列表
      // zset 操作: score:
      // 按分数倒序返回 id 列表
      List<String> ids = commands.zrevrange(order, start, end);
      ids.forEach(
          id -> {
            // 按 id 查询
            // hash 操作: zset
            // 返回 key 为 article:article_id 的所有数据
            Map<String, String> dataMap = commands.hgetall(id);
            String idVal = id.substring(id.indexOf(':') + 1);
            dataMap.put("id", idVal);
            articles.add(ArticleV1.from(dataMap));
          });
    }
    return articles;
  }

  public void addRemoveGroups(long articleId, String[] toAdd, String[] toRemove) {
    String articleKey = RedisKeys.articleKey(articleId);
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      if (toAdd != null) {
        Arrays.stream(toAdd).forEach(e -> commands.sadd(RedisKeys.groupKey(e), articleKey));
      }
      if (toRemove != null) {
        Arrays.stream(toRemove).forEach(e -> commands.srem(RedisKeys.groupKey(e), articleKey));
      }
    }
  }

  public List<ArticleV1> getGroupArticles(String group, String order, int page, int size) {
    if (order == null) {
      order = RedisKeys.SCORE;
    }
    String scoreGroupKey = RedisKeys.scoreGroupKey(group);
    try (StatefulRedisConnection<String, String> conn = redisClient.connect()) {
      RedisCommands<String, String> commands = conn.sync();
      if (commands.exists(scoreGroupKey) == 0) {
        commands.zinterstore(scoreGroupKey, ZStoreArgs.Builder.max(), RedisKeys.groupKey(group),
            order);
        commands.expire(scoreGroupKey, 600);
      }
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
