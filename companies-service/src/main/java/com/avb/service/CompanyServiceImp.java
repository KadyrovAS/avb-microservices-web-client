package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.Company;
import com.avb.model.CompanyDTO;
import com.avb.repository.CompanyRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("CompanyServiceImp")
public class CompanyServiceImp implements CompanyService{
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImp.class);
    private final CompanyRepo repository;

    public CompanyServiceImp(CompanyRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<CompanyDTO> findAllCompanies() {
        List<Company> companies = repository.findAll();
        List<CompanyDTO> companiesDTO = companies.stream()
                .map(this::toCompanyDTO)
                .toList();
        return companiesDTO;

    }

    private CompanyDTO toCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .usersId(company.getUsersId())
                .build();
    }

    private Company fromCompanyDTO(CompanyDTO companyDTO){
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setBudget(companyDTO.getBudget());
        company.setUsersId(companyDTO.getUsersId());
        return company;
    }

    public CompanyDTO findCompanyById(Integer id) {
        Optional<Company> company = repository.findById(id);
        if (company.isEmpty()) {
            throw new AVBException("404", "There is no company with id = " + id + "  in the database!");
        }
        return toCompanyDTO(company.get());
    }

    @Override
    public CompanyDTO addCompany(CompanyDTO companyDTO) {
        if (companyDTO.getId() != null && repository.existsById(companyDTO.getId())){
            throw new AVBException("403", "The operation was rejected! The company with id = " +
                    companyDTO.getId() +
                    " is already registered in the database!");
        }
        if (companyDTO.getUsersId() == null){
            companyDTO.setUsersId(new LinkedList<>());
        }

        Company company = repository.save(fromCompanyDTO(companyDTO));
        return toCompanyDTO(company);
    }
}
