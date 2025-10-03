package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.CompanyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.avb.client.CompanyClient;

@Service("CompanyServiceImp")
public class CompanyServiceImp implements CompanyService{

    private CompanyClient companyClient = null;
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImp.class);
    private final ClientService clientService;

    public CompanyServiceImp(ClientService clientService) {
        this.clientService = clientService;
        companyClientInitialization();
    }

    private void companyClientInitialization() {
        if (this.companyClient == null) {
            this.companyClient = clientService.getClient(CompanyClient.class, "companies");
        }
    }

    private CompanyClient getCompanyClient() {
        companyClientInitialization();
        if (this.companyClient == null) {
            throw new AVBException("503", "The companies service is not registered!");
        }
        return companyClient;
    }

    /**
     * Получить все компании
     */
    @Override
    public Page<CompanyDTO> getAllCompanies(Pageable pageable) {
        logger.info("Get all companies - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return getCompanyClient().getAllCompanies(pageable);
    }

    /**
     * Получить компанию по id
     */
    @Override
    public CompanyDTO getCompanyById(Integer id) {
        logger.info("Get company by id: {}", id);
        return getCompanyClient().getCompanyById(id);
    }

    /**
     * Создать компанию
     */
    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        logger.info("Company '{}' was created!", companyDTO);
        return getCompanyClient().createCompany(companyDTO);
    }

    /**
     * Изменить компанию
     */
    @Override
    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        logger.info("Company {} was updated!", companyDTO);
        return getCompanyClient().editCompany(companyDTO);
    }

    /**
     * Удалить компанию
     */
    @Override
    public CompanyDTO deleteCompany(Integer id) {
        logger.info("Company with id = {} was deleted", id);
        return getCompanyClient().deleteCompany(id);
    }
}