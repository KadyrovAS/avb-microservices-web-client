package com.avb.controller;

import com.avb.service.TempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create")
public class TempController{


    private static final Logger logger = LoggerFactory.getLogger(TempController.class);
    private final TempService service;

    public TempController(TempService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public void createAll() {
        logger.info("createAll");
        service.createAll();
    }

    @GetMapping("/user")
    public void createUser() {
        logger.info("createUser");
        service.createUsers();
    }


    @GetMapping("/company")
    public void createCompany() {
        logger.info("createCompany");
        service.createCompanies();
    }

}
