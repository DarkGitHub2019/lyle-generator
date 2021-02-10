package com.lyle.product.generator.model;

import lombok.Data;

@Data
public class Column {
    /*
    column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey,
     */
    String columnName;
    String dataType;
    String columnComment;
    String columnKey;
    String extra;
}
