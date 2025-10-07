package com.avb.service;

import com.avb.model.CompanyDTO;
import com.avb.model.CompanyFullDTO;
import com.avb.model.TransferUserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CompanyService{
    List<CompanyFullDTO> findAllCompanies(Pageable pageable);
    CompanyDTO findCompanyById(Integer id);
    CompanyDTO addCompany(CompanyDTO companyDTO);
    CompanyDTO deleteCompany(Integer id);
    CompanyDTO editCompany(CompanyDTO company);
    void transferUser(TransferUserDTO transferUser);
    Boolean companyExists(Integer id);
    Map<Integer, CompanyDTO> findListCompanyById(List<Integer> usersId);
    CompanyFullDTO findCompanyFullDTOById(Integer id);
}
