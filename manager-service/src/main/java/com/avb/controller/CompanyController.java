package com.avb.controller;

import com.avb.model.CompanyDTO;
import com.avb.service.CompanyService;
import com.avb.validation.ValidationGroups;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController{

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
    public CompanyDTO createCompany(@Validated(ValidationGroups.OnCreate.class) @RequestBody CompanyDTO companyDTO) {
        logger.info("CreateCompany {}!", companyDTO);
        return service.createCompany(companyDTO);
    }

    @GetMapping("/{id}")
    public CompanyDTO getCompanyById(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("Get company by id = {}!", id);
        return service.getCompanyById(id);
    }

    @PutMapping
    public CompanyDTO updateCompany(@Validated(ValidationGroups.OnUpdate.class) @RequestBody CompanyDTO companyDTO) {
        logger.info("Update company {}!", companyDTO);
        return service.updateCompany(companyDTO);
    }

    @DeleteMapping("/{id}")
    public CompanyDTO deleteCompany(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("Delete company with id = {}", id);
        return service.deleteCompany(id);
    }
}