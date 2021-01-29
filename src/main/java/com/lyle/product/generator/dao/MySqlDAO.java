package com.lyle.product.generator.dao;

import com.lyle.product.generator.model.Connection;
import com.lyle.product.generator.model.Page;
import com.lyle.product.generator.model.Table;
import com.lyle.product.generator.util.PageHelper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Service
@Getter
@Setter
public class MySqlDAO {

    @Autowired
    JdbcTemplate defaultJdbcTemplate;

    public MySqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MySqlDAO() {
        jdbcTemplate = defaultJdbcTemplate;
    }

    public static JdbcTemplate jdbcTemplate;

    public List<String> showDatabases() {
        List<String> databaseNames = jdbcTemplate.queryForList("SHOW databases", String.class);

        if (CollectionUtils.isEmpty(databaseNames)) {
            return new LinkedList<>();
        }

        return databaseNames;
    }

    public List<Table> getTableList(Page page, String schema, String searchTable) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT table_name tableName, engine, table_comment tableComment, create_time createTime FROM information_schema.tables");
        stringBuilder.append(" WHERE table_schema='" + schema + "'");
        if (StringUtils.isBlank(searchTable)) {
            stringBuilder.append(" AND table_name LIKE CONCAT('%','" + searchTable + "','%'");
        }

        List<Table> tables = jdbcTemplate.query(PageHelper.startPage(stringBuilder.toString(), page), new BeanPropertyRowMapper<>(Table.class));

        if (CollectionUtils.isEmpty(tables)) {
            return new LinkedList<>();
        }
        return tables;
    }

    @PostConstruct
    private void buildDefaultTemplate(){
        jdbcTemplate=defaultJdbcTemplate;
    }

    public boolean createConnection(Connection connection){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(connection.getDiver());
        dataSource.setUsername(connection.getUser());
        dataSource.setPassword(connection.getPassword());
        dataSource.setJdbcUrl("jdbc:mysql://"+connection.getServer()+connection.getPort());


        JdbcTemplate jdbcTemplate2 = new JdbcTemplate(dataSource);

        return  true;
    }


}
