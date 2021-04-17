package com.jsc.mybatisplus;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsc.mybatisplus.entity.User;
import com.jsc.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class MyBatisPlusApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Test
    void delByIdLogic() {
        // 加了前面的配置后这里就是逻辑删除了
        int i = userMapper.deleteById(1383415177636483073L);
        System.out.println("影响行数: " + i);
    }

    @Test
    void selectAll() {
        // 查询全部,这时候把deleted为1的过滤了
        userMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    void updateById() {
        User user = new User();
        // 更新我逻辑删掉的数据,英雄行数为0 更新失败
        // user.setId(1087982257332887553L);
        // 更新没删除的别人,就可以
        user.setId(1383352467997671425L);
        user.setAge(20);
        // user.setUpdateTime(LocalDateTime.now().plusDays(1));
        int i = userMapper.updateById(user);
        System.out.println("影响行数: " + i);
    }


    @Test
    void selectMyOwn() {
        // 查询全部,这时候把deleted为1的过滤了
        userMapper.mySelectList(
                Wrappers.<User>lambdaQuery()
                        .gt(User::getAge, 18)
                        // 加上就过滤了
                        .eq(User::getDeleted, 0)
        )
                .forEach(System.out::println);
    }

    @Test
    void insertAutoFilled() {
        User user = new User();
        user.setName("testIgnore");
        user.setAge(18);
        // 自动填充了创建时间
        int insert = userMapper.insert(user);
        System.out.println("影响行数: " + insert);
    }

    @Test
    void updateAutoFilled() {
        User user = new User();
        user.setId(1383349447410843650L);
        user.setName("夹心");
        user.setAge(16);
        // 自动填充了更新时间
        int insert = userMapper.updateById(user);
        System.out.println("影响行数: " + insert);
    }


    @Test
    void updateUseLock() {
        // 假设是从db中取出来了
        int version = 3;
        User user = new User();
        user.setId(1383352467997671425L);
        user.setName("饼干");
        user.setAge(16);
        // 自动更新成2
        user.setVersion(version);
        // 自动填充了更新时间
        int insert = userMapper.updateById(user);
        System.out.println("影响行数: " + insert);
    }

    @Test
    void updateLockWrapperTwice() {
        int version = 5;

        // 乐观锁,wrapper复用
        User user = new User();
        user.setName("❤❤❤❤");
        user.setAge(18);
        user.setVersion(version);

        QueryWrapper<User> query = Wrappers.query();
        query.eq("name","❤❤");
        // 自动填充了创建时间
        int insert = userMapper.update(user,query);
        System.out.println("影响行数: " + insert);

        // 复用query
        User user2 = new User();
        user2.setName("❤❤❤");
        user2.setAge(18);
        user2.setVersion(6);
        query.eq("name","❤❤❤❤");
        // 自动填充了创建时间
        System.out.println(userMapper.update(user, query));


    }
}
