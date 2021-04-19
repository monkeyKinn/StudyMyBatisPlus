package com.jsc.mybatisplus.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO description
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/19 10:05
 */
@Data
public class DuoBiaoVo {
    private Long Uid;
    private String Uname;
    private Integer Uage;
    private String Uemail;
    private Long UmId;
    private LocalDateTime Uct;
    private String name;
}
