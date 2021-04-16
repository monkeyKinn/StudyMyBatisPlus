package com.jsc.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsc.mybatisplus.entity.User;
import com.jsc.mybatisplus.mapper.UserMapper;
import com.jsc.mybatisplus.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class MyBatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;
    private QueryWrapper<User> query = Wrappers.query();
    private UpdateWrapper<User> update = Wrappers.update();

    @Test
    void selectAllTest() {
        List<User> users = userMapper.selectList(null);
        // Assertions.assertEquals(5, users.size());
        users.forEach(System.out::println);
    }

    @Test
    void insertTest() {
        User user = new User();
        user.setName("小金");
        user.setAge(18);
        // 没有这只email 默认不给插入 没有id 是因为默认雪花id
        user.setManagerId(1087982257332887553L);
        user.setCreateTime(LocalDateTime.now());

        int rows = userMapper.insert(user);
        System.out.println("影响行数: " + rows);
    }

    @Test
    void selectByIdTest() {
        // 根据id查询
        User user = userMapper.selectById(1382699085670719489L);
        // User(id=1382699085670719489, name=老金, age=18, email=null, managerId=1087982257332887553, createTime=2021-04-15T22:15:26)
        System.out.println(user);
    }

    @Test
    void selectBatchIdsTest() {
        // 根据id批量查询
        List<Long> ids = Arrays.asList(1382699085670719489L, 1094592041087729666L, 1094590409767661570L);
        List<User> users = userMapper.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    void selectByMapTest() {
        Map<String, Object> map = new HashMap<>();
        // map.put("name", "老金");
        // map的key是数据库中的字段
        map.put("age", 27);
        // WHERE name = ? AND age = ?
        // WHERE age = ?
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
    }

    @Test
    void selectByWrapper0() {
        /*
         * 1、名字中包含雨并且年龄小于40
         * name like '%雨%' and age<40
         * 条件构造器
         */

        query.like("name", "雨").lt("age", 40);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper1() {
        /*
         * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
         * name like '%雨%' and age between 20 and 40 and email is not null
         */
        query.like("name", "雨").between("age", 20, 40).isNotNull("email");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper2() {
        /*
         * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
         *  name like '王%' or age>=25 order by age desc,id asc
         */
        query.like("name", "王").or().ge("age", 18).orderByDesc("age").orderByAsc("id");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper3() {
        /*
         * 创建日期为2019年2月14日并且直属上级为名字为王姓
         * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
         */
        query.apply("date_format(create_time,'%Y-%m-%d')={0}", "2019-02-14")
                .inSql("manager_id", "select id from user where name like '王%'");

        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper4() {
        /*
         * 名字为王姓并且（年龄小于40或邮箱不为空）
         * name like '王%' and (age<40 or email is not null)
         */
        query.likeRight("name", "王").and(qw -> qw.lt("age", 40).or().isNotNull("email"));

        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper5() {
        /*
         * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
         * name like '王%' or (age<40 and age>20 and email is not null)
         */
        query.likeRight("name", "王").or(qw ->
                qw.lt("age", 40)
                        .gt("age", 20).isNotNull("email"));
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper6() {
        /*
         * （年龄小于40或邮箱不为空）并且名字为王姓,nested 正常嵌套 不带 AND 或者 OR
         * (age<40 or email is not null) and name like '王%'
         */
        query.nested(qw ->
                qw.lt("age", 40).or().isNotNull("email")).likeRight("name", "王");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper7() {
        /*
         * 年龄为30、31、34、35
         *   age in (30、31、34、35)
         */
        query.in("age", 18, 30, 31, 34, 35);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper8() {
        /*
         * 10、名字中包含雨并且年龄小于40(需求1加强版)
                    第一种情况：  select id,name -- 就用select("")
	                            from user
	                            where name like '%雨%' and age<40
                    第二种情况：  select id,name,age,email
	                            from user
	                            where name like '%雨%' and age<40
         */

        query.like("name", "雨").lt("age", 40).select("name", "age");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapper9() {
        /*
         * 10、名字中包含雨并且年龄小于40(需求1加强版)
                    第一种情况：  select id,name -- 就用select("")
	                            from user
	                            where name like '%雨%' and age<40
                    第二种情况：  select id,name,age,email
	                            from user
	                            where name like '%雨%' and age<40
         */

        query.like("name", "雨").lt("age", 40).select(User.class, info ->
                !info.getColumn().equals("create_time")
                        && !info.getColumn().equals("manager_id"));
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByCondition() {
        // 根据参数条件查询
        // String name = "金";
        String name = "玉";
        String age = "18";
        // 如果不为空,就有后面的条件加入到sql中
        query.like(StringUtils.isNotBlank(name), "name", name)
                .eq(StringUtils.isNotBlank(age), "age", age);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapperEntity() {
        /*
         * 使用场景:
         *  1、通过实体传过来数据
         *  2.不想永理科这样的构造器,默认是等值的
         *          如果不想等值,在实体类中加上注解
         *          @TableField(condition=SqlCondition.like)
         *                     小于的话就是="%s&lt;#{%s}"
         *                                 列名<列值
         */
        User whereUser = new User();
        whereUser.setName("老金");
        whereUser.setAge(18);
        query = Wrappers.query(whereUser);
        // query.like("name", "雨").lt("age", 40);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapperAllEq() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);

        // 后面的false,是忽略null值的列
        query.allEq(params, false);
        // 前面一个函数是过滤用的  比如这里就是不包含age列
        // query.allEq((k,v)->!"age".equals(k),params);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapperMaps() {
        /*
         * 使用场景:
         *  1.当表特别的多色时候只要查少数 几列,返回一个map,而不是属性大部分都为空的实体
         *  2.返回的是统计结果
         *       按照直属上级分组，查询每组的 平均年龄、最小龄、最大年龄。
         *           并且只取年龄总和小于500的组。
         *               select
         *                   avg(age) avg_age,
         *                   min(age) min_age,
         *                   max(age) max_age
         *               from user
         *               group by manager_id --上级id分组
         *               having sum(age) <500 --只有总和小于500
         * */
        // 第一种情况
        // query.like("name","雨").lt("age",40).select("id","name");

        // 第二种情况
        query.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id")
                .having("sum(age) < {0}", 500);
        List<Map<String, Object>> users = userMapper.selectMaps(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapperObjs() {
        query.select("id", "name").like("name", "雨").lt("age", 40);
        // 返回第一列的值 只返回一列的时候用
        List<Object> users = userMapper.selectObjs(query);
        users.forEach(System.out::println);
    }

    @Test
    void selectByWrapperCounts() {
        // 不能设置查询的列名了
        query.like("name", "雨").lt("age", 40);
        // 查总记录数
        Integer count = userMapper.selectCount(query);
        System.out.println(count);
    }

    @Test
    void selectByWrapperOne() {
        // 不能设置查询的列名了
        query.like("name", "老金").lt("age", 40);
        // 只返回一条数据
        User user = userMapper.selectOne(query);
        System.out.println(user);
    }

    @Test
    void selectLambda() {
        // 创建方式
        // QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // LambdaQueryWrapper<User> userLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();

        // 好处: 防止误写字段名

        // where name like '%雨%' and age<40
        // 前面是类名 后面是get方法,表示属性
        lambdaQueryWrapper.like(User::getName, "雨").lt(User::getAge, 40);

        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void selectLambda1() {
        /*
         * 名字为王姓并且（年龄小于40或邮箱不为空）
         * name like '王%' and (age<40 or email is not null)
         */
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        // 好处: 防止误写字段名
        // 前面是类名 后面是get方法,表示属性
        lambdaQueryWrapper.likeRight(User::getName, "王").and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void selectLambda2() {
        /*
         * 名字为王姓并且（年龄小于40或邮箱不为空）
         * name like '王%' and (age<40 or email is not null)
         */
        // 3.0.7新增创建方式
        List<User> users = new LambdaQueryChainWrapper<>(userMapper)
                .like(User::getName, "雨").ge(User::getAge, 20).list();

        users.forEach(System.out::println);
    }

    @Test
    void selectAllCustomize() {
        // 自定义的方法

        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.likeRight(User::getName, "王").and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> users = userMapper.selectAll(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    void selectPage() {
        //分页查询
        /*
         * 1、名字中包含雨并且年龄小于40
         * name like '%雨%' and age<40
         * 条件构造器
         */
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.ge(User::getAge, 18);

        // 泛型是实体类 , 当前页数 默认是1，从1开始，不是0。每页最多多少条

        // 第一种用selectPage方法,返回的是 Page<User> userPage
        /*Page<User> page = new Page<>(1, 2);
        Page<User> userPage = userMapper.selectPage(page, lambdaQueryWrapper);

        System.out.println("总页数: "+userPage.getPages());
        System.out.println("总记录数: "+userPage.getTotal());
        userPage.getRecords().forEach(System.out::println);*/

        // 第二种用 selectMapsPage方法,返回的是 IPage<Map<String, Object>>
        // 如果第一个参数还是用的上面的page,此时page胡报错
        // 解决方案:
        //      把page 转成对应的类型IPage<Map<String, Object>>
        //          因为新版后(3.4.1+), 更改了源码 给他设定了具体类型
        // 第三个参数是是否查询总记录数,false不查,这时候就没有总页数和总记录数了,用在滚动查很多很多的时候吧
        IPage<Map<String, Object>> page1 = new Page<>(4, 2, false);
        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page1, lambdaQueryWrapper);

        System.out.println("总页数: " + mapIPage.getPages());
        System.out.println("总记录数: " + mapIPage.getTotal());
        List<Map<String, Object>> records = mapIPage.getRecords();
        records.forEach(System.out::println);
    }

    @Test
    void selectPageCustomize() {
        //分页查询
        /*
         * 1、名字中包含雨并且年龄小于40
         * name like '%雨%' and age<40
         * 条件构造器
         */
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.ge(User::getAge, 18);

        // 泛型是实体类 , 当前页数 默认是1，从1开始，不是0。每页最多多少条

        // 第一种用selectPage方法,返回的是 Page<User> userPage
        Page<User> page = new Page<>(1, 2);
        IPage<User> userIPage = userMapper.selectUserPage(page, lambdaQueryWrapper);

        System.out.println("总页数: " + userIPage.getPages());
        System.out.println("总记录数: " + userIPage.getTotal());
        userIPage.getRecords().forEach(System.out::println);
    }

    //更新测试
    @Test
    void UpdateById() {
        User user = new User();
        user.setId(1382713714941648898L);
        user.setName("小陈");
        int i = userMapper.updateById(user);
        System.out.println("影响记录数: " + i);
    }

    @Test
    void UpdateByWrapper() {
        User user = new User();
        // user.setId(1382713714941648898L);
        user.setName("小金");

        update.eq("name", "老金");
        int i = userMapper.update(user, update);
        System.out.println("影响记录数: " + i);
    }

    @Test
    void UpdateByWrapper1() {
        User whereUser = new User();
        // user.setId(1382713714941648898L);
        whereUser.setName("小金");
        // 可以直接把实体传进去
        update = new UpdateWrapper<>(whereUser);
        update.eq("age", 18);

        User user = new User();
        user.setAge(21);
        int i = userMapper.update(user, update);
        System.out.println("影响记录数: " + i);
    }

    @Test
    void UpdateByWrapper2() {
        // 不创建实体传入,直接在条件中set
        update.eq("name", "小金").set("name", "老金");

        int i = userMapper.update(null, update);
        System.out.println("影响记录数: " + i);
    }

    @Test
    void UpdateByLambda() {
        LambdaUpdateWrapper<User> updateLambda = Wrappers.lambdaUpdate();
        updateLambda.eq(User::getName, "老金").set(User::getName, "小金");
        int i = userMapper.update(null, updateLambda);
        System.out.println("影响记录数: " + i);
    }

    @Test
    void UpdateByLambdaChain() {
        // 链式调用
        boolean update = new LambdaUpdateChainWrapper<User>(userMapper).eq(User::getName, "小金").set(User::getName, "老金").update();
        System.out.println(update ? "成功" : "失败");
    }

    @Test
    void deleteById() {
        int i = userMapper.deleteById(1382699085670719489L);
        System.out.println("影响行数: " + i);
    }

    @Test
    void deleteByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "小金");
        columnMap.put("age", "18");
        // WHERE name = ? AND age = ?
        int i = userMapper.deleteByMap(columnMap);

        System.out.println("影响行数: " + i);
    }

    @Test
    void deleteByBatchIds() {
        List<Long> longs = Arrays.asList(1383035889074692097L, 1383035840634646530L);
        int i = userMapper.deleteBatchIds(longs);
        // WHERE id IN ( ? , ? )
        // 根据id批量删除
        System.out.println("影响行数: " + i);
    }

    @Test
    void deleteByWrapper() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(User::getId, "1383037094597267457");
        int delete = userMapper.delete(lambdaQuery);
        System.out.println("影响行数: " + delete);
    }

    @Test
    void ARInsertTest() {
        User user = new User();
        user.setName("xx");
        user.setAge(0);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());

        // 直接insert,自己插自己可还行
        boolean insert = user.insert();
        System.out.println(insert?"成功":"失败");
    }

    @Test
    void ARSelectByIdTest() {

        // 直接查,自己查自己可还行
        User user = new User();
        User user1 = user.selectById(1383044106274050049L);
        System.out.println(user1);
    }

    @Test
    void ARSelectByIdTest1() {
        // 不需要参数,直接实体上设置
        // 直接查,自己查自己可还行
        User user = new User();
        user.setId(1383044106274050049L);

        User user1 = user.selectById();
        System.out.println(user1);
    }

    @Test
    void ARUpdateTest() {
        User user = new User();
        user.setId(1383044106274050049L);
        user.setName("xxoo");
        user.setAge(16);
        user.setManagerId(1383035808669888513L);
        user.setCreateTime(LocalDateTime.now());

        // 自己操作
        boolean b = user.updateById();
        System.out.println(b?"success":"fail");

    }

    @Test
    void ARDeleteTest() {
        User user = new User();
        user.setId(1383044106274050049L);

        // 自己操作
        boolean b = user.deleteById();
        System.out.println(b?"success":"fail");

    }

    @Test
    void ARInsetOrUpdateTest() {
        User user = new User();
        user.setId(1383048652618551298L);
        user.setName("x");
        user.setAge(100);
        user.setCreateTime(LocalDateTime.now());

        // 根据id 有就更新没有就插入
        user.insertOrUpdate();

    }

}
