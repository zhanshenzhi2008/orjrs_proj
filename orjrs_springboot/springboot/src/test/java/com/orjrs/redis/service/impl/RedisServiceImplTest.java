package com.orjrs.redis.service.impl;

import com.orjrs.model.Girl;
import com.orjrs.redis.service.RedisService;
import org.junit.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

/**
 * ${todo}
 *
 * @author orjrs
 * @date 2018-04-0118:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceImplTest {
    /***/
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImplTest.class);
    @Autowired
    private RedisService redisService;

    @Test
    public void test() {
        Girl girl = new Girl();
        girl.setId(33L);
        girl.setName("张的 的");
        girl.setAge(33);
        girl.setCupSize("B");
        redisService.addGirl(girl);

        redisService.getGirl(girl.getId()).getId();
    }
}