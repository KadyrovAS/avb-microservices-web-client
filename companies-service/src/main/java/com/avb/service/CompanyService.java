package com.avb.service;

import com.avb.model.CompanyDTO;
import com.avb.model.TransferUserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface CompanyService{
    Page<CompanyDTO> findAllCompanies(Pageable pageable);
    CompanyDTO findCompanyById(Integer id);
    CompanyDTO addCompany(CompanyDTO companyDTO);
    CompanyDTO deleteCompany(Integer id);
    CompanyDTO editCompany(CompanyDTO company);
    void transferUser(TransferUserDTO transferUser);
    Boolean companyExists(Integer id);
}
