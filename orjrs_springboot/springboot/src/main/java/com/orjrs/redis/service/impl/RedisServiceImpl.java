package com.orjrs.redis.service.impl;

import com.orjrs.model.Girl;
import com.orjrs.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Redis 服务实现类
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private Set<Girl> girlSet = new HashSet<>();

    /***
     *  存放girl缓存
     * @param girl 女孩
     * @return Girl
     */
    @Override
    @CachePut(value = "Girl", key = "'Girl:'+#girl.id")
    public Girl addGirl(Girl girl) {
        girlSet.add(girl);
        return girl;
    }

    /***
     *  根据id 名字 年龄存放girl缓存
     * @param id 流水号
     * @param name 名字
     * @param age 年龄
     * @return Girl
     */
    @Override
    @Cacheable(value = "Girl", key = "'Girl:'+#id")
    public Girl addGirl(long id, String name, int age) {
        Girl girl = new Girl();
        girl.setId(id);
        girl.setName(name);
        girl.setAge(age);
        girlSet.add(girl);
        return girl;
    }

    /***
     *  根据id获取缓存中的Girl
     * @param id 流水号
     * @return Girl
     */
    @Override
    @Cacheable(value = "user", key = "'User:'+#id")
    public Girl getGirl(long id) {
        System.out.println("not in redis cache");
        /*for (Girl g : girlSet) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;*/
        return girlSet.stream().filter(it -> it.getId().equals(id)).findFirst().get();
    }
}

