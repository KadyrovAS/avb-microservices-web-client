package com.avb.service;

import com.avb.model.CompanyDTO;
import java.util.List;

public interface CompanyService{
    List<CompanyDTO> findAllCompanies();
    CompanyDTO findCompanyById(Integer id);
    CompanyDTO addCompany(CompanyDTO companyDTO);
}
