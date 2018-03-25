package com.orjrs.controller;

import com.orjrs.config.GirlProperties;
import com.orjrs.dao.GirlDAO;
import com.orjrs.model.Girl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GirlController {
    @Autowired
    private GirlProperties girlProperties;

    @Autowired
    private GirlDAO girlDAO;

    //@RequestMapping(value = "/hello", method = RequestMethod.GET)
    @RequestMapping(value = {"/hello", "/hi"}, method = RequestMethod.GET)
    public String say() {
        return "age" + girlProperties.getAge();
    }

    @PostMapping(value = "/girl/{id}")
    public Girl queryGirlByAge(@PathVariable("id") Long id){
        return girlDAO.queryAllById(id);
    }


}
