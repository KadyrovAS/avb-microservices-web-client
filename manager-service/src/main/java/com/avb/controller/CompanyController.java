package com.avb.controller;

import com.avb.model.CompanyDTO;
import com.avb.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<CompanyDTO> getAllCompanies() {
        logger.info("getAllCompanies");
        return companyService.getAllCompanies();
    }

    @PostMapping
    public CompanyDTO createCompany(@RequestBody CompanyDTO companyDTO) {
        logger.info("createCompany {}", companyDTO);
        return companyService.createCompany(companyDTO);
    }

    @GetMapping("{id}")
    public CompanyDTO getCompanyById(@PathVariable Integer id) {
        logger.info("getCompanyById {}", id);
        return companyService.getCompanyById(id);
    }

    @PutMapping
    public CompanyDTO updateCompany(@RequestBody CompanyDTO companyDTO) {
        logger.info("updateCompany {}", companyDTO);
        return companyService.updateCompany(companyDTO);
    }

    @DeleteMapping("{id}")
    public void deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
    }
}