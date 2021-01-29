package com.lyle.product.generator.util;

import com.lyle.product.generator.model.Page;

public class PageHelper {

    private static int offset;
    private static int size;

    public static String startPage(String sql, Page page) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(sql);

        size = page.getLimit();

        if (page.getPage() <= 0) {
            offset = 0;
        } else {
            offset = (page.getPage() - 1) * size;
        }

        stringBuffer.append(" LIMIT " + offset + "," + size);

        return stringBuffer.toString();
    }
}
