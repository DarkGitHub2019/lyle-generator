package com.lyle.product.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    Long id;
    String name;
    String email;
    String password;
    String imageUrl;
    Date createTime;
    Date updateTime;
}
