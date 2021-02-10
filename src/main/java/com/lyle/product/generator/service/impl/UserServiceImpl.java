package com.lyle.product.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyle.product.generator.mapper.UserMapper;
import com.lyle.product.generator.model.User;
import com.lyle.product.generator.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
