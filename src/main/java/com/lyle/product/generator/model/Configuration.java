package com.lyle.product.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Configuration {
    /*
    #代码生成器相关生成配置

#主路径（即主工程包路径，如将一些工具类放在主包下的common包中，那么mainPath就是common的包的父包路径）
mainPath=com.innodealing

#包名，生成的文件的包名
package=com.innodealing

#模块名，生成的文件的所在模块  将来文件的包名为 package+moduleName+controller/service等等,具体生成规则在模板中
moduleName=

#由于模块名可能为空导致后面产生两个.的问题 所以当模块名为空，后面的符号也为空  模块名不为空则后面符号为 .
moduleSeparator=

#作者
author=lyle

#邮箱
email=lyle@163.com

#表前缀 有则填写，没有则注释掉 比如tb_user那么就填 tb_
tablePrefix=t_
     */
    @TableId(type = IdType.AUTO)
    Long id;
    String configurationName;
    Long userId;
    String mainPath;
    String packageReference;
    String moduleName;
    String moduleSeparator;
    String author;
    String email;
    String tablePrefix;
    String typeMappingText;
    Integer isDefault;
    String modelSpilt;
    Integer ormFrame;
    Integer enableSwagger;
}
