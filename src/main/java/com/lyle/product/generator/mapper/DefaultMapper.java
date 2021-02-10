package com.lyle.product.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyle.product.generator.model.Connection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DefaultMapper extends BaseMapper<Connection> {
}
