package com.lyle.product.generator.dao;

import com.lyle.product.generator.model.Column;
import com.lyle.product.generator.model.Page;
import com.lyle.product.generator.model.Table;
import com.lyle.product.generator.util.PageHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@Slf4j
public class MySqlDAO {

    public static Map<String, JdbcTemplate> jdbcTemplateHashMap = new HashMap<>(16);

    public List<String> showDatabases(JdbcTemplate jdbcTemplate) {
        List<String> databaseNames = jdbcTemplate.queryForList("SHOW databases", String.class);

        if (CollectionUtils.isEmpty(databaseNames)) {
            return new LinkedList<>();
        }

        return databaseNames;
    }

    public List<Table> getTableList(JdbcTemplate jdbcTemplate, Page page, String schema, String searchTable) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT table_name tableName, engine, table_comment tableComment, create_time createTime FROM information_schema.tables");
        stringBuilder.append(" WHERE table_schema='" + schema + "'");
        if (StringUtils.isNoneBlank(searchTable)) {
            stringBuilder.append(" AND table_name LIKE CONCAT('%','" + searchTable + "','%'");
        }

        List<Table> tables = jdbcTemplate.query(PageHelper.startPage(stringBuilder.toString(), page), new BeanPropertyRowMapper<>(Table.class));

        if (CollectionUtils.isEmpty(tables)) {
            return new LinkedList<>();
        }
        return tables;
    }

    public List<Column> getColumns(JdbcTemplate jdbcTemplate, String schema, String table) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra ");
        stringBuilder.append("FROM information_schema.columns ");
        stringBuilder.append("WHERE table_name ='" + table + "' AND table_schema ='" + schema + "' order by ordinal_position");

        log.info(stringBuilder.toString());
        return jdbcTemplate.query(stringBuilder.toString(), new BeanPropertyRowMapper<>(Column.class));
    }

}
