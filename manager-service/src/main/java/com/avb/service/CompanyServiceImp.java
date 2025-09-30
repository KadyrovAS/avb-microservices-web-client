package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.CompanyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.avb.client.CompanyClient;

@Service("CompanyServiceImp")
public class CompanyServiceImp implements CompanyService {

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

    private CompanyClient getCompanyClient(){
        companyClientInitialization();
        if (this.companyClient == null) {
            throw new AVBException("503", "The companies service is not registered!");
        }
        return companyClient;

    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        logger.info("Get all companies!");
        return getCompanyClient().getAllCompanies();
    }

    @Override
    public CompanyDTO getCompanyById(Integer id) {
        return getCompanyClient().getCompanyById(id);
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        logger.info("Company '{}' was created!", companyDTO);
        return getCompanyClient().createCompany(companyDTO);
    }

    @Override
    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        logger.info("Company {} was updated!", companyDTO);
        return getCompanyClient().editCompany(companyDTO);
    }

    @Override
    public CompanyDTO deleteCompany(Integer id) {
        logger.info("Company with id = {} was deleted", id);
        return getCompanyClient().deleteCompany(id);
    }
}