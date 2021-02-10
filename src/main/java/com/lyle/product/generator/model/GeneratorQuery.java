package com.lyle.product.generator.model;

import lombok.Data;

import java.util.List;

@Data
public class GeneratorQuery {

    Long connectionId;
    String schema;
    Long configurationId;
    List<Table> tables;
}
