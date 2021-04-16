package com.jsc.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsc.mybatisplus.entity.User;
import com.jsc.mybatisplus.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


/**
 * TODO description
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/16 22:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testService() {
        User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge,25),false);
        System.out.println(one);
    }

    @Test
    public void batchTest() {
        // 批量
        User user = new User();
        user.setName("犀利");
        user.setAge(12);

        User user1 = new User();
        user1.setName("犀利1");
        user1.setAge(121);

        List<User> users = Arrays.asList(user1, user);
        userService.saveBatch(users);
    }

    @Test
    public void chainTest() {
        List<User> list = userService.lambdaQuery().gt(User::getAge, 25).list();
        list.forEach(System.out::println);
    }

    @Test
    public void chainTest1() {
        boolean update = userService.lambdaUpdate().eq(User::getName, "小陈").set(User::getName, "小玉").update();
        System.out.println(update?"success":"fail");
    }

    @Test
    public void chainTest2() {
        // 直接删除
        boolean update = userService.lambdaUpdate().eq(User::getName, "xx").remove();
        System.out.println(update?"success":"fail");
    }
}
