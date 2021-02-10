package com.lyle.product.generator;

import com.google.gson.Gson;
import com.lyle.product.generator.model.GeneratorQuery;
import com.lyle.product.generator.model.Table;
import com.lyle.product.generator.service.ConnectionService;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class GeneratorApplicationTests {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ConnectionService connectionService;

    @Test
    void contextLoads() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setJdbcUrl("jdbc:mysql://edgclearlove.top:3306");

        JdbcTemplate jdbcTemplate2 = new JdbcTemplate(dataSource);

        jdbcTemplate2.getDataSource().getConnection();

    }

    @Test
    void test1()  {

        GeneratorQuery generatorQuery=new GeneratorQuery();

        Table table=new Table();
        table.setTableName("person");
        table.setCreateTime(new Timestamp(System.currentTimeMillis()));
        table.setEngine("InnoDB");

        List<Table> list=new LinkedList<>();
        list.add(table);

        generatorQuery.setConfigurationId(1359383383843999747L);
        generatorQuery.setSchema("test");
        generatorQuery.setConnectionId(3L);
        generatorQuery.setTables(list);

        Gson gson=new Gson();
        System.out.println(gson.toJson(generatorQuery));
    }



}
