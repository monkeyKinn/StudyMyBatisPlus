package com.jsc.mybatisplus.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 我的元数据处理器
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/17 17:00
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入的时候的填充方法
     *
     * @param metaObject 元数据
     * @return void 空
     * @author 金聖聰
     * @email jinshengcong@163.com
     * Modification History:
     * Date         Author        Description        version
     * --------------------------------------------------------*
     * 2021/04/17 17:02    金聖聰     修改原因            1.0
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 3.3.0过期了 替代方法是 strictInsertFill
        // setInsertFieldValByName("createTime", LocalDateTime.now(),metaObject);
        // 等效于
        // setFieldValByName("createTime", LocalDateTime.now(),metaObject);

        // 自动填充优化: 实体类中有没有这个字段,有的话才自动填充
        boolean createTime = metaObject.hasSetter("createTime1");
        if (createTime) {
            strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    /**
     * 更新时候的填充方法
     *
     * @param metaObject 元数据
     * @return void 空
     * @author 金聖聰
     * @email jinshengcong@163.com
     * Modification History:
     * Date         Author        Description        version
     * --------------------------------------------------------*
     * 2021/04/17 17:02    金聖聰     修改原因            1.0
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 3.3.0过期了 替代方法是 strictUpdateFill
        // setUpdateFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        // 等效于
        // setFieldValByName("updateTime",LocalDateTime.now(),metaObject);

        // 优化:  只有这个为null的时候才进行自动填充
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (ObjectUtils.isEmpty(updateTime)) {
            strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }

    }
}
