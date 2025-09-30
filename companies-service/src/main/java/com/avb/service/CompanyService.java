package com.avb.service;

import com.avb.model.CompanyDTO;
import com.avb.model.TransferUserDTO;
import com.avb.model.UserDTO;

import java.util.List;
import java.util.Map;

public interface CompanyService{
    List<CompanyDTO> findAllCompanies();
    CompanyDTO findCompanyById(Integer id);
    CompanyDTO addCompany(CompanyDTO companyDTO);
    CompanyDTO deleteCompany(Integer id);
    CompanyDTO editCompany(CompanyDTO company);
    void transferUser(TransferUserDTO transferUser);
    Boolean companyExists(Integer id);
}
