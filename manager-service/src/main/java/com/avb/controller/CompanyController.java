package com.avb.controller;

import com.avb.model.CompanyDTO;
import com.avb.model.CompanyFullDTO;
import com.avb.model.ValidatedPageable;
import com.avb.service.CompanyService;
import com.avb.validation.ValidationGroups;
import jakarta.validation.Valid;
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
@Validated
public class CompanyController{

    @Autowired
    @Qualifier("CompanyServiceImpl")
    private CompanyService service;

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    /**
     * Получить все компании
     */
    @GetMapping
    public List<CompanyFullDTO> getAllCompanies(@Valid ValidatedPageable pageable) {
        logger.info("Get companies page: page={}, size={}, sort={}",
                pageable.getPage(), pageable.getSize(), pageable.getSort());
        return service.getAllCompanies(pageable);
    }

    /**
     * Создать компанию
     */
    @PostMapping
    public CompanyDTO createCompany(@Validated(ValidationGroups.OnCreate.class) @RequestBody CompanyDTO companyDTO) {
        logger.info("CreateCompany {}!", companyDTO);
        return service.createCompany(companyDTO);
    }

    /**
     * Получить компанию по id
     */
    @GetMapping("/{id}")
    public CompanyFullDTO getCompanyById(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id) {
        logger.info("Get company by id = {}!", id);
        return service.findCompanyFullDTOById(id);
    }

    /**
     * Изменить компанию
     */
    @PutMapping
    public CompanyDTO updateCompany(@Validated(ValidationGroups.OnUpdate.class) @RequestBody CompanyDTO companyDTO) {
        logger.info("Update company {}!", companyDTO);
        return service.updateCompany(companyDTO);
    }

    /**
     * Удалить компанию по id
     */
    @DeleteMapping("/{id}")
    public CompanyDTO deleteCompany(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id) {
        logger.info("Delete company with id = {}", id);
        return service.deleteCompany(id);
    }

    @GetMapping("/full/{id}")
    public CompanyFullDTO getCompanyFullDTOById(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id){
        logger.info("Find company with id = {}", id);
        return service.findCompanyFullDTOById(id);
    }
}