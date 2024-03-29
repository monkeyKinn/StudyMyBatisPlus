package com.jsc.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.rmi.server.UID;
import java.time.LocalDateTime;

/**
 * 实体类
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/15 21:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {
    private static final long serialVersionUID = 7589930312778081895L;

    // auto是自增，同时数据库中也要设置成自增主键
    // none 是根据全局的来
    // idWorkerStr就是雪花算法
    // idWorkerStr就是雪花算法(当数据库是String类型)
    // uuid 是String类型的,数据库中也记得改varchar(32)
    /** 主键 */
    private Long id;
    /** 姓名 */
    private String name;
    /** 年龄 */
    private Integer age;
    /** 邮件 */
    private String email;
    /** 直属上级id */
    private Long managerId;
    /** 创建时间 */
    private LocalDateTime createTime;
}