package com.lyle.product.generator.controller;

import com.lyle.product.generator.dao.MySqlDAO;
import com.lyle.product.generator.model.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeneratorController {

    @Autowired
    MySqlDAO mySqlDAO;

    @GetMapping("schemas")
    public List<String> getSchemas() {
        return mySqlDAO.showDatabases();
    }

    @PostMapping("/connection/new")
    public String createNewConnection(@RequestBody Connection connection){

        if (1==connection.getDbType()){

        }
        return  "success";
    }

}
