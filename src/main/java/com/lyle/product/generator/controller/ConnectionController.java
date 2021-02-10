package com.lyle.product.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyle.product.generator.dao.MySqlDAO;
import com.lyle.product.generator.exception.EncryptException;
import com.lyle.product.generator.model.Connection;
import com.lyle.product.generator.pool.JdbcTemplatePool;
import com.lyle.product.generator.service.ConnectionService;
import com.lyle.product.generator.service.GeneratorService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/connection")
public class ConnectionController {
    @Resource
    MySqlDAO mySqlDAO;

    @Resource
    GeneratorService generatorService;

    @Resource
    ConnectionService connectionService;

    @Resource
    JdbcTemplatePool jdbcTemplatePool;

    @GetMapping("schemas/{connectionId}")
    public List<String> getSchemas(@PathVariable("connectionId") Long connectionId) throws GeneralSecurityException, UnsupportedEncodingException {
        JdbcTemplate jdbcTemplate = generatorService.getJdbcTemplateByConnectionId(connectionId);

        if (null == jdbcTemplate) {
            return null;
        }

        return mySqlDAO.showDatabases(jdbcTemplate);
    }

    @PostMapping("/create")
    public String createNewConnection(Connection connection) throws EncryptException {

        switch (connection.getDbType()) {
            case 1:
                connectionService.createConnection(connection);
                break;
            default:
                return "错误 没有匹配的数据库类型";
        }
        return "success";
    }

    @GetMapping("get/{connectionId}")
    public Connection getConnectionById(@PathVariable("connectionId") Long connectionId){
        return connectionService.getById(connectionId);
    }

    @GetMapping("/list/{page}/{limit}")
    public List<Connection> getConnectionList(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit, @CookieValue("userId") Long userId) {
        QueryWrapper<Connection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.select("id", "connection_name");
        return connectionService.page(new Page<>(page, limit), queryWrapper).getRecords();
    }

    @PutMapping("/update")
    public boolean updateConnection(Connection connection) {
        return connectionService.updateById(connection);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteConnection(@PathVariable("id") Long id) {
        return connectionService.removeById(id);
    }
}
