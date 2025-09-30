package com.avb.service;

import com.avb.model.CompanyDTO;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> getAllCompanies();
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO getCompanyById(Integer id);
    CompanyDTO updateCompany(CompanyDTO companyDTO);
    CompanyDTO deleteCompany(Integer id);
}