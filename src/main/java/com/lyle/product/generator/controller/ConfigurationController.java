package com.lyle.product.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyle.product.generator.model.Configuration;
import com.lyle.product.generator.service.ConfigurationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    @Resource
    ConfigurationService configurationService;

    @PostMapping("/create")
    public String createNewConfiguration(Configuration configuration,@CookieValue("userId") Long userId) {
        configuration.setUserId(userId);
        configurationService.save(configuration);

        return "添加成功";
    }

    @DeleteMapping("/delete/{configurationId}")
    public String deleteConfiguration(@PathVariable("configurationId") Long configurationId) {
        configurationService.removeById(configurationId);
        return "删除成功";
    }

    @GetMapping("select/one/{id}")
    public Configuration selectConfigurationOne(@PathVariable("id") Long id) {
        return configurationService.getById(id);
    }

    @GetMapping("select/all")
    public List<Configuration> selectConfigurationList(@CookieValue("userId") Long userId) {

        QueryWrapper<Configuration> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "configuration_name");
        queryWrapper.eq("user_id", userId);
        return configurationService.list(queryWrapper);
    }

    @PutMapping("update/echo")
    public Configuration updateAndEcho(Configuration configuration) {
        configuration.setUserId(null);
        configurationService.updateById(configuration);
        return configurationService.getById(configuration.getId());
    }

    @PutMapping("update")
    public String update(Configuration configuration) {
        configuration.setUserId(null);
        configurationService.updateById(configuration);
        return "更新成功";
    }

}
