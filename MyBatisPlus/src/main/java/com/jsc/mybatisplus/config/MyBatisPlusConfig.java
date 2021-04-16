package com.jsc.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis的分页配置
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/16 15:48
 */
@Configuration
public class MyBatisPlusConfig {
    // 新版废弃
    // @Bean
    // public PaginationInterceptor paginationInterceptor() {
    //     return new PaginationInterceptor();
    // }
    //-------------------------------------------------
    /*
      未测试
      新的分页插件,一缓和二缓遵循mybatis的规则,
      需要设置 MybatisConfiguration#useDeprecatedExecutor = false
      避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    /* @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }*/
    //***************************************************
    /**
     * 注册插件
     *  依赖以下版本+
     *      <dependency>
     *         <groupId>com.baomidou</groupId>
     *         <artifactId>mybatis-plus-boot-starter</artifactId>
     *         <version>3.4.1</version>
     *     </dependency>
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor 拦截器
     * @author 金聖聰
     * @email jinshengcong@163.com
     * Modification History:
     * Date         Author        Description        version
     *--------------------------------------------------------*
     * 2021/04/16 15:56    金聖聰     修改原因            1.0
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 0.创建一个拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1. 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 2. 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 3. 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(500L);
        // 4. 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);
        // 5.添加内部拦截器
        interceptor.addInnerInterceptor(pageInterceptor);

        return interceptor;
    }
}
