package com.graduation.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientForwardController {

    @GetMapping("/**/{path:[^\\.]*}")
    String index() {
        return "index";
    }

}
