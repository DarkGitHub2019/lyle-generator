package com.lyle.product.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyle.product.generator.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
