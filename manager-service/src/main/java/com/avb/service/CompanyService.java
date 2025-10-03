package com.avb.service;

import com.avb.model.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Page<CompanyDTO> getAllCompanies(Pageable pageable);
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO getCompanyById(Integer id);
    CompanyDTO updateCompany(CompanyDTO companyDTO);
    CompanyDTO deleteCompany(Integer id);
}