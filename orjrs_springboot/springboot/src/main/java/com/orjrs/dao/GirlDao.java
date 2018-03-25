package com.orjrs.dao;

import com.orjrs.model.Girl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "girls")
public interface GirlDAO extends JpaRepository<Girl, Long> {
    /**
     * 通过id查询Girl
     *
     * @param id 主键
     * @return
     */
    @Cacheable(key = "#p0")
    Girl queryAllById(Long id);

    /**
     * 通过年龄+cupSize查询Girl
     *
     * @param age     年龄
     * @param cupSize
     * @return
     */
    Girl findByAgeAndCupSize(Integer age, String cupSize);

    /**
     * 通过年龄+cupSize查询Girl
     *
     * @param age 年龄
     * @return
     */
    @Query("FROM Girl WHERE AGE = :age")
    List<Girl> findGirl(@Param("age") int age);

    /**
     * 更新Girl年龄
     *
     * @param age 年龄
     * @return
     */
    @Modifying
    @Query("update Girl set age = ?1")
    int updateAge(int age);
}
