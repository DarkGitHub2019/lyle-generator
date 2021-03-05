package com.lyle.product.generator.util;

import com.lyle.product.generator.exception.RRException;
import com.lyle.product.generator.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class GenUtils {

    static String currentTableName = "";

    public void generatorCode(Configuration config, Table table, List<Column> columns, ZipOutputStream zip) {

        //将映射的关系的文本形式转换为map
        Map<String, String> typeMappingMaps = covertTypeTextToMap(config.getTypeMappingText());

        boolean hasBigDecimal = false;
        boolean hasList = false;

        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.getTableName());
        tableEntity.setComments(table.getTableComment());

        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), new String[]{config.getTablePrefix()});
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columnsList = new ArrayList<>();
        for (Column column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.getColumnName());
            columnEntity.setDataType(column.getDataType());
            columnEntity.setComments(column.getColumnComment());
            columnEntity.setExtra(column.getExtra());

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = typeMappingMaps.get(columnEntity.getDataType());
            columnEntity.setAttrType(attrType);


            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if (!hasList && "array".equals(columnEntity.getExtra())) {
                hasList = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columnsList.add(columnEntity);
        }
        tableEntity.setColumns(columnsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getMainPath();

        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        if (org.apache.commons.lang3.StringUtils.isBlank(tableEntity.getComments())) {
            map.put("comments", "");
        } else {
            map.put("comments", tableEntity.getComments());
        }
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasList", hasList);
        map.put("mainPath", mainPath);
        map.put("package", config.getPackageReference());
        map.put("moduleName", config.getModuleName());
        map.put("author", config.getAuthor());
        map.put("email", config.getEmail());
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("modelSpilt", config.getModelSpilt());
        map.put("moduleSeparator", config.getModuleSeparator());
        map.put("ormFrame", config.getOrmFrame());
        map.put("enableSwagger", config.getEnableSwagger());
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();

        //非mybatis框架不生成mapper文件
        if (config.getOrmFrame()!=1&&config.getOrmFrame()!=2){
            templates.remove("template/Mapper.java.vm");
            templates.remove("template/Mapper.xml.vm");
        }

        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getPackageReference(), config.getModuleName(), config.getModuleSeparator())));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }

    private Map<String, String> covertTypeTextToMap(String typeMappingText) {

        Map<String, String> typeMaps = new HashMap<>(32);

        String[] textMappings = typeMappingText.split(",");

        for (String textMapping : textMappings) {
            typeMaps.put(textMapping.substring(0, textMapping.indexOf("=")), textMapping.substring(textMapping.indexOf("=") + 1));
        }

        return typeMaps;

    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                tableName = tableName.replace(tablePrefix, "");
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();

        //添加需要生成的模板
        //   templates.add("template/Entity.java.vm");
        //      templates.add("template/Dao.xml.vm");
        templates.add("template/DTO.java.vm");
        //  templates.add("template/menu.sql.vm");
        templates.add("template/Mapper.java.vm");
        templates.add("template/Mapper.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        //    templates.add("template/Dao.java.vm");
        templates.add("template/DO.java.vm");
        //    templates.add("template/index.vue.vm");
        //   templates.add("template/add-or-update.vue.vm");
        if (MongoManager.isMongo()) {
            // mongo不需要mapper、sql   实体类需要替换
            templates.remove(0);
            templates.remove(1);
            templates.remove(2);
            templates.add("template/MongoEntity.java.vm");
        }
        return templates;
    }


    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName, String moduleSeparator) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        if (template.contains("MongoChildrenEntity.java.vm")) {
            return packagePath + "entity" + File.separator + "inner" + File.separator + currentTableName + File.separator + splitInnerName(className) + "InnerEntity.java";
        }
        if (template.contains("Entity.java.vm") || template.contains("MongoEntity.java.vm")) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Dao.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.xml";
        }

        if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (template.contains("index.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        }

        if (template.contains("add-or-update.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
        }

        if (template.contains("DTO.java.vm")) {
            return packagePath + "model" + File.separator + "dto" + File.separator + className + "DTO.java";
        }

        if (template.contains("DO.java.vm")) {
            return packagePath + "model" + File.separator + "dto" + File.separator + className + "DO.java";
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("template/Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + moduleSeparator + className + "Mapper.xml";
        }
        return null;
    }

    private static String splitInnerName(String name) {
        name = name.replaceAll("\\.", "_");
        return name;
    }
}
