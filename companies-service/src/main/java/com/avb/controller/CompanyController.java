package com.avb.controller;

import com.avb.model.CompanyDTO;
import com.avb.model.CompanyFullDTO;
import com.avb.model.TransferUserDTO;
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
import java.util.Map;


@RestController
@RequestMapping("/api")
@Validated
public class CompanyController{
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    @Qualifier("CompanyServiceImpl")
    private CompanyService service;

    /**
     * Получить все компании с пагинацией
     */
    @GetMapping
    public List<CompanyFullDTO> getAllCompanies(
            @Valid ValidatedPageable pageable){

        logger.info("get all companies with pagination: page={}, size={}, sort={}",
                pageable.getPage(), pageable.getSize(), pageable.getSort());
        return service.findAllCompanies(pageable.toPageable());
    }

    /**
     * Получить компанию по ID
     */
    @GetMapping("/{id}")
    public CompanyDTO getCompany(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id) {
        logger.info("getCompany: id = {}", id);
        return service.findCompanyById(id);
    }

    /**
     * Создать новую компанию
     */
    @PostMapping
    public CompanyDTO addCompany(@Validated(ValidationGroups.OnCreate.class) @RequestBody CompanyDTO company) {
        logger.info("addCompany {}", company);
        return service.addCompany(company);
    }

    /**
     * Удалить компанию
     */
    @DeleteMapping("/{id}")
    public CompanyDTO deleteCompany(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id) {
        logger.info("deleteCompany id = {}", id);
        return service.deleteCompany(id);
    }

    /**
     * Обновить компанию
     */
    @PutMapping
    public CompanyDTO editCompany(@Validated(ValidationGroups.OnUpdate.class) @RequestBody CompanyDTO company) {
        logger.info("editCompany: {}", company);
        return service.editCompany(company);
    }


    /**
     * Переместить пользователя между компаниями
     */
    @PostMapping("/transfer-user")
    public void transferUser(@Valid @RequestBody TransferUserDTO transferUserDTO) {
        logger.info("transferUser");
        service.transferUser(transferUserDTO);
    }


    /**
     * Проверить существование компании
     */
    @GetMapping("/exists/{id}")
    public Boolean companyExists(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id) {
        logger.info("companyExists");
        return service.companyExists(id);
    }

    @GetMapping("/list")
    public Map<Integer, CompanyDTO> getListCompaniesById(@RequestParam List<Integer>companiesId){
        logger.info("getListCompaniesById");
        return service.findListCompanyById(companiesId);
    }

    @GetMapping("/full/{id}")
    public CompanyFullDTO getCompanyFullDTOById(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id){
        logger.info("getCompanyFullDTOById");
        return service.findCompanyFullDTOById(id);
    }

}