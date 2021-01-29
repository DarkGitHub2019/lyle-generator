package com.lyle.product.generator.model;

import lombok.Data;

@Data
public class Connection {
    Integer dbType;
    String connectName;
    String server;
    String port;
    String user;
    String password;
    String diver;
}
