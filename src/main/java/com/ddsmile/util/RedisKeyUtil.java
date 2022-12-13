package com.ddsmile.util;

/**
 * 用于生成Redis中的键,
 * 以便复用
 */
public class RedisKeyUtil {

    //key的格式
    private static final String SPLIT=":";  //用来拼接key, 中间使用当前常量来拼接
    private static final String PREFIX_ENTITY_LIKE="like:entity";   //key的前缀, 实体的赞
    private static final String PREFIX_USER_LIKE="like:user";       //key的前缀, 用户的赞
    private static final String PREFIX_FOLLOWEE="followee";         //key的前缀, 粉丝的目标
    private static final String PREFIX_FOLLOWER="follower";         //key的前缀, 粉丝
    private static final String PREFIX_KAPTCHA="kaptcha";
    private static final String PREFIX_TICKET="ticket";
    private static final String PREFIX_USER="user";

    /**
     * 某个实体的赞
     *      like:entity:entityType:entityId -> set(userId)
     *      如果单纯存的是数字, 那么我们想要看到是哪个用户点的赞就无法实现, 所以使用set集合
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return
     */
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 某个用户的赞
     *       like:user:userId->int
     * @param userId 用户id
     * @return
     */
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 某个用户关注的实体
     *      followee:userId:entityType -> zset(entityId,now)
     *      若A关注了B，则A是B的Follower（粉丝），B是A的Followee（目标）
     *      使用 zset(有序集合) 储存，可以排序，排序所用的分数是关注时的时间。
     * @param userId 用户id
     * @param entityType 实体类型
     * @return
     */
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体拥有的粉丝
     *       follower:entityType:entityId -> zset(userId,now)
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return
     */
    public static String getFollowerKey(int entityType,int entityId){
        return PREFIX_FOLLOWER+SPLIT + entityType + SPLIT + entityId;
    }

    //登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //登录凭证
    // ticket: ticket
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    //用户
    // user : userId
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

}
