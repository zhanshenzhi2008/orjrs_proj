package com.orjrs.dao;

import com.orjrs.model.Girl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GirlDaoTest {
    @Autowired
    GirlDAO girlDao;

    @Test
    @Transactional
    public void save() {
        Girl girl = new Girl();
        girl.setName(" ");
        girl.setAge(20);
        girl.setCupSize("B");
        girl.setAddress("中国大连");
        girlDao.save(girl);

        girl = new Girl();
        girl.setName("Creeds");
        girl.setAge(20);
        girl.setCupSize("C");
        girl.setAddress("美国芝加哥");
        girlDao.save(girl);

        Assert.assertEquals(4, girlDao.findAll().size());
    }


    @Test
    @Transactional
    public void queryAllById() {
        girlDao.queryAllById(1L);
    }

    @Test
    @Transactional
    public void findByAgeAndCupSize() {
        Girl girl = girlDao.findByAgeAndCupSize(20, "B");
        Assert.assertEquals(20, girl.getAge().longValue());
    }

    @Test
    @Transactional
    public void findGirl() {
        List<Girl> girl = girlDao.findGirl(20);
        Assert.assertEquals(2, girl.size());
    }

    @Test
    @Transactional
    public void updateAge() {
    }
}