package com.jsc.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Random;

/**
 * mybatis\Plus 的配置类
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/16 15:48
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 注册插件
     * 依赖以下版本+
     * <dependency>
     * <groupId>com.baomidou</groupId>
     * <artifactId>mybatis-plus-boot-starter</artifactId>
     * <version>3.4.1</version>
     * </dependency>
     *
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor 拦截器
     * @author 金聖聰
     * @email jinshengcong@163.com
     * Modification History:
     * Date         Author        Description        version
     * --------------------------------------------------------*
     * 2021/04/16 15:56    金聖聰     修改原因            1.0
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        // 0.创建一个拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 动态表名
        /*DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();

        HashMap<String, TableNameHandler> map = new HashMap<String, TableNameHandler>(2) {{
            put("user_high", (sql, tableName) -> {
                // 动态表名的主要换表逻辑在这
                String year = "_2018";
                int random = new Random().nextInt(10);
                if (random % 2 == 1) {
                    year = "_2019";
                }
                return tableName + year;
            });
        }};

        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);*/
        // 动态表名结束,.,,,,

       // 1. 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 2. 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 3. 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(500L);
        // 4. 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler(){

            @Override
            public Expression getTenantId() {
                // 租户信息 一般是session或者配置文件中,或者静态变量中
                // 1088248166370832385  王天风的
                return new LongValue(1088248166370832385L);
            }

            @Override
            public String getTenantIdColumn() {
                // 多用户字段是什么,表中的字段名字
                return "manager_id";
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            // 某些表不加租户信息,
            @Override
            public boolean ignoreTable(String tableName) {
                // 如果查是user_high ->false不忽略(除了这个都忽略)
                // !"user_high"就是不过滤,"user_high"就是过滤
                return !"user_high".equalsIgnoreCase(tableName);
            }
        }));

        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        // 5.添加内部拦截器
        interceptor.addInnerInterceptor(pageInterceptor);
        // 6.乐观锁插件 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        // 需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
