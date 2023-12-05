package com.example.redis_demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = RedisDemoApplication.class)
@RunWith(SpringRunner.class)
class RedisDemoApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
    }

    /**
     * 操作string类型数据
     */
    @Test
    public void testString() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        redisTemplate.opsForValue().set("city", "beijing");

        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);

        redisTemplate.opsForValue().set("key1", "value1", 10l, TimeUnit.SECONDS);

        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
        System.out.println(ifAbsent);
    }

    /**
     * 操作Hash类型数据
     */
    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("002", "name", "xiaoming");
        hashOperations.put("002", "age", "20");

        String age = (String) hashOperations.get("002", "age");
        System.out.println(age);

        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        // 获得hash的所有值
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作list的数据
     */
    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        // 存值
        listOperations.leftPush("mylist", "a");

        listOperations.leftPushAll("mylist", "b", "c", "d");

        List<String> mylist = listOperations.range("mylist", 0, -1);

        for (String o : mylist) {
            System.out.println(o);
        }

        // 获取列表长度 len
        Long size = listOperations.size("mylist");

        int intSize = size.intValue();

        for (int i = 0; i < intSize; i++) {
            // 出队列
            String value = (String) listOperations.rightPop("mylist");

            System.out.println(value);
        }
    }

    /**
     * 操作Set类型的数据
     */
    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        // 存值
        setOperations.add("myset", "a", "b", "c", "d");

        // 取值
        Set<String> myset = setOperations.members("myset");
        for (String object : myset) {
            System.out.println(object);
        }

        // 删除成员
        setOperations.remove("myset", "a", "b");

        // 取值
        myset = setOperations.members("myset");
        for (String s : myset) {
            System.out.println(s);
        }

    }

    /**
     * 操作Zset类型的数据
     */
    @Test
    public void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //存值
        zSetOperations.add("myZset", "a", 10.0);
        zSetOperations.add("myZset", "b", 11.0);
        zSetOperations.add("myZset", "c", 12.0);
        zSetOperations.add("myZset", "a", 13.0);

        // 取值
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }

        // 修改分数
        zSetOperations.incrementScore("myZset", "b", 20.0);

        zSetOperations.remove("myZset", "a", "b");

        myZset = zSetOperations.range("myZset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }

    }

    /**
     * 通用操作
     */
    @Test
    public void testCommon() {
        // 获取Redis中的所有key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        // 判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);

        // 删除指定key
        redisTemplate.delete("myZset");

        // 获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("myset");
    }

}
