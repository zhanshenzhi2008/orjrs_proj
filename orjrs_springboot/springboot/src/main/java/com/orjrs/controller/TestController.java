package com.orjrs.controller;

import com.orjrs.config.GirlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private GirlProperties girlProperties;

    //@RequestMapping(value = "/hello", method = RequestMethod.GET)
    @RequestMapping(value = {"/hello", "/hi"}, method = RequestMethod.GET)
    public String say() {
        return "age" + girlProperties.getAge();
    }

}
