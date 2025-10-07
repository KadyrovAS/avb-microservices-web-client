package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.CompanyDTO;
import com.avb.model.CompanyFullDTO;
import com.avb.model.ValidatedPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avb.client.CompanyClient;

import java.util.List;

@Service("CompanyServiceImpl")
public class CompanyServiceImpl implements CompanyService{

    private CompanyClient companyClient = null;
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final ClientService clientService;

    public CompanyServiceImpl(ClientService clientService) {
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
    public List<CompanyFullDTO> getAllCompanies(ValidatedPageable pageable) {
        String direction = pageable.getDirection().name().toLowerCase();
        logger.info("Get all companies - page: {}, size: {}, sort: {}, direction: {}",
                pageable.getPage(), pageable.getSize(), pageable.getSort(), direction);

        return getCompanyClient().getAllCompanies(
                pageable.getPage(),
                pageable.getSize(),
                pageable.getSort(),
                direction
        );
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

    /**
     * Получить компанию по id
     */
    @Override
    public CompanyFullDTO findCompanyFullDTOById(Integer id) {
        logger.info("findCompanyFullDTOById: {}", id);
        return getCompanyClient().getCompanyFullDTOById(id);
    }
}