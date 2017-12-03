package com.orjrs.dao;

import com.orjrs.model.Girl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GirlDaoTest {
    @Autowired
    GirlDao girlDao;

    @Test
    public void save() {
        Girl girl = new Girl();
        girl.setAge(20);
        girl.setCupSize("B");
        girl.setAddress("中国大连");
        girlDao.save(girl);

        girl = new Girl();
        girl.setAge(20);
        girl.setCupSize("C");
        girl.setAddress("美国芝加哥");
        girlDao.save(girl);

        Assert.assertEquals(1, girlDao.findAll().size());
    }


    @Test
    public void queryAllById() {
    }

    @Test
    public void findByAgeAndCupSize() {
        Girl girl = girlDao.findByAgeAndCupSize(20,"A");
        Assert.assertEquals(20, girl.getAge().longValue());
    }

    @Test
    public void findGirl() {
        List<Girl> girl = girlDao.findGirl(20);
        Assert.assertEquals(2, girl.size());
    }

    @Test
    public void updateAge() {
    }
}