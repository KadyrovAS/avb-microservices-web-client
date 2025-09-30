package com.avb.controller;

import com.avb.model.CompanyDTO;
import com.avb.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    @Qualifier("CompanyServiceImp")
    private CompanyService service;

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @GetMapping
    public List<CompanyDTO> getAllCompanies() {
        logger.info("Get all companies!");
        return service.getAllCompanies();
    }

    @PostMapping
    public CompanyDTO createCompany(@RequestBody CompanyDTO companyDTO) {
        logger.info("CreateCompany {}!", companyDTO);
        return service.createCompany(companyDTO);
    }

    @GetMapping("{id}")
    public CompanyDTO getCompanyById(@PathVariable Integer id) {
        logger.info("Get company by id = {}!", id);
        return service.getCompanyById(id);
    }

    @PutMapping
    public CompanyDTO updateCompany(@RequestBody CompanyDTO companyDTO) {
        logger.info("Update company {}!", companyDTO);
        return service.updateCompany(companyDTO);
    }

    @DeleteMapping("{id}")
    public CompanyDTO deleteCompany(@PathVariable Integer id) {
        logger.info("Delete company with id = {}", id);
        return service.deleteCompany(id);
    }
}