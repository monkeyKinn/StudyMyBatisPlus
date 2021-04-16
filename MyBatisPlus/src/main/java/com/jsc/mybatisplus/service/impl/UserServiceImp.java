package com.jsc.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsc.mybatisplus.entity.User;
import com.jsc.mybatisplus.mapper.UserMapper;
import com.jsc.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;

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
}
