package com.lyle.product.generator.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LyleQueryWrapper<T>  extends QueryWrapper<T> {


    public QueryWrapper<T> select(Predicate<TableFieldInfo>... predicates) {
        this.entityClass = entityClass;

        List<String> columns=new ArrayList<>();

        for (Predicate<TableFieldInfo> predicate:predicates){
            columns.add(TableInfoHelper.getTableInfo(this.getCheckEntityClass()).chooseSelect(predicate));
        }

        super.select((String [])columns.toArray());
        return (com.baomidou.mybatisplus.core.conditions.query.QueryWrapper)this.typedThis;
    }
}
