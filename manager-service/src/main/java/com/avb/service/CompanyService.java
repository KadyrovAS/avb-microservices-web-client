package com.avb.service;

import com.avb.model.CompanyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import com.avb.client.CompanyClient;

@Service
public class CompanyService {

    private final CompanyClient companyClient;
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    public CompanyService(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    public List<CompanyDTO> getAllCompanies() {
        logger.info("getAllCompanies");
        return companyClient.getAllCompanies();
    }

    public CompanyDTO getCompanyById(Integer id) {
        return companyClient.getCompanyById(id);
    }

    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        if (companyDTO.getName() == null || companyDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        logger.info("createCompany {}", companyDTO);
        CompanyDTO companyToReturn = companyClient.createCompany(companyDTO);
        logger.info("companyToReturn {}", companyToReturn);
        return companyToReturn;
    }

    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        return companyClient.editCompany(companyDTO);
    }

    public void deleteCompany(Integer id) {
        companyClient.deleteCompany(id);
    }
}