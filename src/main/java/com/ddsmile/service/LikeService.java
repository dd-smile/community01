package com.ddsmile.service;

import com.ddsmile.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 点赞业务逻辑层
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param userId 是谁点的赞
     * @param entityType 点赞实体类型
     * @param entityId 点赞实体id
     版本1 :
    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId); // 生成实体对应的key
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);  // 判断 userId是否在 entityLikeKey 集合里
        if (isMember) {  // 若已存在，说明已经点过赞，将uerId从集合中移除（取消赞）
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {   // 没点过赞，所以添加到集合中
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }
     */

    /**
     *
     * @param userId 点赞的那个人的id(是谁点的赞)
     * @param entityType 点赞实体类型
     * @param entityId 点赞实体id
     * @param entityUserId 被赞的那个人的id
     */
    public void like(int userId,int entityType,int entityId, int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);   // 生成实体对应的key
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId); // 实体用户Id对应的Key

                // 查询操作要放在事务外
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);  // 判断 userId是否在 entityLikeKey 集合里 有没有被点过赞

                operations.multi(); // 启用事务
                // 查询操作不能放在里面

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId); // 若已存在，说明已经点过赞，将uerId从集合中移除（取消赞）
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId); // 没点过赞，所以添加到集合中
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec(); // 提交事务
            }
        });
    }

    /**
     * 查询某实体点赞的数量
     * @param entityType 点赞实体类型
     * @param entityId 点赞实体id
     * @return
     */
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey); // 统计数量
    }

    /**
     * 查询某人对某实体的点赞状态
     * @param userId 是谁点的赞
     * @param entityType 点赞实体类型
     * @param entityId 点赞实体id
     * @return 点赞状态 1-点赞, 0-没有点赞
     */
    public int findEntityLikeStatus(int userId, int entityType, int entityId) { // 返回int可以表示多个状态, 比如可以点赞, 踩, 没有动作 这三种状态, 布尔值无法实现
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0; //1-点赞, 0-没有点赞
    }

    /**
     * 查询某个用户获得的赞
     * @param userId 用户id
     * @return
     */
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey); // 返回的是Object类型所以要强转
        return count == null ? 0 : count.intValue();
    }


}
