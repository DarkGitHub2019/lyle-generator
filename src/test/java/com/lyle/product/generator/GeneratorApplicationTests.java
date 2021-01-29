package com.lyle.product.generator;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.SQLException;

@SpringBootTest
class GeneratorApplicationTests {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setJdbcUrl("jdbc:mysql://edgclearlove.top:3306");

        JdbcTemplate jdbcTemplate2 = new JdbcTemplate(dataSource);

        jdbcTemplate2.getDataSource().getConnection();

//        List<String> idList = jdbcTemplate2.queryForList("SELECT id FROM test2.user", String.class);
//
//        idList.forEach(System.out::println);


    }
}
