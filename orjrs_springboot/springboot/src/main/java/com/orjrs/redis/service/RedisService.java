package com.orjrs.redis.service;

import com.orjrs.model.Girl;

/**
 * Redis 接口
 *
 * @author orjrs
 * @create 2018-03-18
 */
public interface RedisService {

    /***
     *  存放girl缓存
     * @param girl 女孩
     * @return Girl
     */
    Girl addGirl(Girl girl);

    /***
     *  根据id 名字 年龄存放girl缓存
     * @param girl 女孩
     * @return Girl
     */
    Girl addGirl(long id, String name, int age);

    /***
     *  根据id获取缓存中的Girl
     * @param id
     * @return Girl
     */
    Girl getGirl(long id);
}
