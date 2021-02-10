package com.lyle.product.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyle.product.generator.mapper.ConfigurationMapper;
import com.lyle.product.generator.model.Configuration;
import com.lyle.product.generator.service.ConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, Configuration> implements ConfigurationService {
}
