package com.avb.service;

import com.avb.model.CompanyDTO;
import com.avb.model.CompanyFullDTO;
import com.avb.model.ValidatedPageable;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface CompanyService {
    List<CompanyFullDTO> getAllCompanies(ValidatedPageable pageable);
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO updateCompany(CompanyDTO companyDTO);
    CompanyDTO deleteCompany(Integer id);
    CompanyFullDTO findCompanyFullDTOById(Integer id);
}