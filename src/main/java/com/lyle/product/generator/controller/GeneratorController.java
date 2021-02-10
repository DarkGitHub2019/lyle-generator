package com.lyle.product.generator.controller;

import com.lyle.product.generator.dao.MySqlDAO;
import com.lyle.product.generator.model.ComResponse;
import com.lyle.product.generator.model.GeneratorQuery;
import com.lyle.product.generator.model.Table;
import com.lyle.product.generator.service.GeneratorService;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/generate")
public class GeneratorController {

    @Resource
    MySqlDAO mySqlDAO;

    @Resource
    GeneratorService generatorService;


    @GetMapping("/table/list/{connectionId}/{schema}/{page}/{limit}")
    public List<Table> getTableList(@PathVariable("connectionId") Long connectionId, @PathVariable("schema") String schema, @PathVariable("page") Integer page, @PathVariable("limit") Integer limit, @RequestParam(value = "searchTable",required = false) String searchTable) throws GeneralSecurityException, UnsupportedEncodingException {

        JdbcTemplate jdbcTemplate = generatorService.getJdbcTemplateByConnectionId(connectionId);

        if (null == jdbcTemplate) {
            return null;
        }
        return mySqlDAO.getTableList(jdbcTemplate, new com.lyle.product.generator.model.Page(page, limit), schema, searchTable);
    }

    @PostMapping("/table/generator")
    public ComResponse<String> generatorFiles(@RequestBody GeneratorQuery query, HttpServletResponse response) throws IOException, GeneralSecurityException {

        byte[] data = generatorService.generatorFiles(query.getConnectionId(), query.getSchema(), query.getConfigurationId(), query.getTables());

        if (null==data){
            return new ComResponse<String>().noConnection("没有连接");
        }

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"lyleGenerator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());

        return new ComResponse<String>().success("下载成功");
    }


}
