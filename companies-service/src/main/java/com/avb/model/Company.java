package com.avb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Set;


@Entity
public class Company {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private double budget;
    private Set<Integer> usersId;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public Set<Integer> getUsersId() {
        return usersId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setUsersId(Set<Integer> usersId) {
        this.usersId = usersId;
    }

    public static Company fromCompanyDTO(CompanyDTO companyDTO){
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setBudget(companyDTO.getBudget());
        company.setUsersId(companyDTO.getUsersId());
        return company;
    }

    public static CompanyDTO toCompanyDTO(Company company){
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .usersId(company.getUsersId())
                .build();
    }
}
