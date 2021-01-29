package com.lyle.product.generator.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Table {

    private String tableName;
    private String engine;
    private String tableComment;
    private Timestamp createTime;
}
