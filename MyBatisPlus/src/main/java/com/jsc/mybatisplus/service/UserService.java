package com.jsc.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsc.mybatisplus.entity.DuoBiaoVo;
import com.jsc.mybatisplus.entity.User;

import java.util.List;

/**
 * 通用service
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/16 22:26
 */
public interface UserService extends IService<User> {
    DuoBiaoVo selectBossById(Long id);
}
