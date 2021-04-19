package com.jsc.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsc.mybatisplus.entity.DuoBiaoVo;
import com.jsc.mybatisplus.entity.User;
import com.jsc.mybatisplus.mapper.UserMapper;
import com.jsc.mybatisplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO description
 *
 * @author 金聖聰
 * @version v1.0
 * @email jinshengcong@163.com
 * @date Created in 2021/04/16 22:29
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public DuoBiaoVo selectBossById(Long id) {
        QueryWrapper<User> query = Wrappers.query();
        query.eq(ObjectUtils.isNotEmpty(id),"u.id",id).eq("u.name","小金");
        return userMapper.selectBoss(query);
    }
}
