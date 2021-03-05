package com.lyle.product.generator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/test")
    public String hello(Model model){

        model.addAttribute("name","张三");

        return "index";
    }
}
