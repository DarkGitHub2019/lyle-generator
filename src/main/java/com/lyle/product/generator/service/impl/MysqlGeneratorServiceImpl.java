package com.lyle.product.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyle.product.generator.dao.MySqlDAO;
import com.lyle.product.generator.mapper.DefaultMapper;
import com.lyle.product.generator.model.Column;
import com.lyle.product.generator.model.Configuration;
import com.lyle.product.generator.model.Connection;
import com.lyle.product.generator.model.Table;
import com.lyle.product.generator.pool.JdbcTemplatePool;
import com.lyle.product.generator.service.ConfigurationService;
import com.lyle.product.generator.service.ConnectionService;
import com.lyle.product.generator.service.GeneratorService;
import com.lyle.product.generator.util.GenUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipOutputStream;

@Service
public class MysqlGeneratorServiceImpl extends ServiceImpl<DefaultMapper, Connection> implements GeneratorService {

    @Resource
    MySqlDAO mySqlDAO;

    @Resource
    GenUtils genUtils;

    @Resource
    ConfigurationService configurationService;

    @Resource
    JdbcTemplatePool jdbcTemplatePool;

    @Resource
    ConnectionService connectionService;

    @Override
    public JdbcTemplate createJdbcTemplateByConnectionId(Long connectionId) throws GeneralSecurityException, UnsupportedEncodingException {

        Connection connection = this.getById(connectionId);
        if (null == connection) {
            return null;
        }
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(connection.getDriver());
        dataSource.setUsername(connection.getUser());
        dataSource.setPassword(connectionService.passwordDecrypt(connection.getPassword()));
        if (1 == connection.getDbType()) {
            dataSource.setJdbcUrl("jdbc:mysql://" + connection.getHost() + ":"+connection.getPort());
        }
        return new JdbcTemplate(dataSource);

    }

    @Override
    public JdbcTemplate getJdbcTemplateByConnectionId(Long connectionId) throws GeneralSecurityException, UnsupportedEncodingException {
        JdbcTemplate jdbcTemplate = jdbcTemplatePool.getJdbcTemplateByConnectionId(connectionId);
        if (null == jdbcTemplate) {
            jdbcTemplate = createJdbcTemplateByConnectionId(connectionId);
            return jdbcTemplatePool.addJdbcTemplate(connectionId, jdbcTemplate, 5L, TimeUnit.MINUTES);
        }

        return jdbcTemplate;
    }

    @Override
    public byte[] generatorFiles(Long connectionId, String schema, Long configurationId, List<Table> tables) throws GeneralSecurityException, UnsupportedEncodingException {

        JdbcTemplate jdbcTemplate = this.getJdbcTemplateByConnectionId(connectionId);

        if (null == jdbcTemplate) {
            return null;
        }


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (Table table : tables) {
            List<Column> columns = mySqlDAO.getColumns(jdbcTemplate, schema, table.getTableName());
            Configuration configuration = configurationService.getById(configurationId);
            genUtils.generatorCode(configuration, table, columns, zip);
        }

        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
