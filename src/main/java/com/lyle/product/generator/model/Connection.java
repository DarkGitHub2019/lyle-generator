package com.lyle.product.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Connection {
    @TableId(type = IdType.AUTO)
    Long id;
    Long userId;
    Integer dbType;
    String connectionName;
    String host;
    Integer port;
    String user;
    String password;
    String driver;
}
