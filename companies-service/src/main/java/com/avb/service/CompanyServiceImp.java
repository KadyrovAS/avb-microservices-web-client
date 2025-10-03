package com.avb.service;

import com.avb.client.UserClient;
import com.avb.model.*;
import com.avb.repository.CompanyRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("CompanyServiceImp")
public class CompanyServiceImp implements CompanyService{
    private UserClient userClient = null;
    private final ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImp.class);
    private final CompanyRepo repository;

    public CompanyServiceImp(CompanyRepo repository, ClientService clientService) {
        this.repository = repository;
        this.clientService = clientService;
        userClientInitialization();
    }

    private void userClientInitialization() {
        if (this.userClient == null) {
            this.userClient = clientService.getClient(UserClient.class, "users");
        }
    }

    private UserClient getUserClient() {
        userClientInitialization();
        if (this.userClient == null) {
            throw new AVBException("503", "The users service is not registered!");
        }
        return userClient;

    }

    /**
     * Преобразовать Company в CompanyDTO
     */
    private CompanyDTO toCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .usersId(company.getUsersId())
                .build();
    }

    /**
     * Преобразовать CompanyDTO в Company
     */
    private Company fromCompanyDTO(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setBudget(companyDTO.getBudget());
        company.setUsersId(companyDTO.getUsersId());
        return company;
    }

    /**
     * Получить все компании
     */
    @Override
    public Page<CompanyDTO> findAllCompanies(Pageable pageable) {
        Page<Company> companiesPage = repository.findAll(pageable);
        Page<CompanyDTO>companiesDTOPage = companiesPage.map(this::toCompanyDTO);

        logger.info("The list of companies was returned!");
        return companiesDTOPage;
    }

    /**
     * Получить компанию по id
     */
    public CompanyDTO findCompanyById(Integer id) {
        Optional<Company> company = repository.findById(id);
        if (company.isEmpty()) {
            throw new AVBException("404", "There is no company with id = " + id + "  in the database!");
        }
        logger.info("A company with id= {} has been found", id);
        return toCompanyDTO(company.get());
    }

    /**
     * Создать компанию
     */
    @Override
    @Transactional
    public CompanyDTO addCompany(CompanyDTO companyDTO) {
        if (companyDTO.getUsersId() == null) {
            companyDTO.setUsersId(new HashSet<>());
        }

        if (companyDTO.getId() != 0 && repository.existsById(companyDTO.getId())) {
            throw new AVBException("404", "The operation was rejected! The company with id = " +
                    companyDTO.getId() +
                    " is already registered in the database!");
        }

        checkUsersInCompany(companyDTO);

        Company company = repository.save(fromCompanyDTO(companyDTO));
        logger.info("Company {} was added to database!", company);
        return toCompanyDTO(company);
    }


    /**
     * Удалить компанию
     */
    @Override
    @Transactional
    public CompanyDTO deleteCompany(Integer id) {
        CompanyDTO company = findCompanyById(id);
        toDismissalUsers(company.getUsersId());
        repository.deleteById(id);
        logger.info("The company with id = {} has been deleted from the database!", id);
        return company;
    }

    /**
     * Изменить компанию
     */
    @Override
    @Transactional
    public CompanyDTO editCompany(CompanyDTO companyNew) {
        if (companyNew.getId() == null) {
            throw new AVBException("404", "You must specify the company's id!");
        }
        CompanyDTO companyOld = findCompanyById(companyNew.getId());

        checkUsersInCompany(companyNew);

        toDismissalUsers(
                companyOld.getUsersId().stream()
                        .filter(id -> !companyNew.getUsersId().contains(id))
                        .collect(Collectors.toSet())
        );

        toEmployUsers(
                companyNew.getUsersId().stream()
                        .filter(id -> !companyOld.getUsersId().contains(id))
                        .collect(Collectors.toSet()),
                companyNew.getId()
        );

        repository.save(fromCompanyDTO(companyNew));
        logger.info("The company with id = {} in the database has been updated!", companyNew.getId());
        return companyNew;
    }


    /**
     * Переместить пользователя из одной компании в другую компанию
     */
    @Override
    @Transactional
    public void transferUser(TransferUserDTO transferUser) {
        logger.info("transfer user {}", transferUser);
        Integer userId = transferUser.getUserId();
        Integer companyIdFrom = transferUser.getCompanyIdFrom();
        Integer companyIdTo = transferUser.getCompanyIdTo();

        if (companyIdFrom != 0) {
            CompanyDTO companyFrom = findCompanyById(companyIdFrom);
            companyFrom.setUsersId(
                    companyFrom.getUsersId().stream()
                            .filter(id -> !Objects.equals(id, userId))
                            .collect(Collectors.toSet())
            );
            editCompany(companyFrom);
        }
        if (companyIdTo != 0) {
            CompanyDTO companyTo = findCompanyById(companyIdTo);
            Set<Integer>users = companyTo.getUsersId();
            users.add(userId);
            companyTo.setUsersId(users);
            editCompany(companyTo);
        }
    }

    /**
     * Проверить наличие компании с заданным id в базе данных
     */
    @Override
    public Boolean companyExists(Integer id) {
        Optional<Company> company = repository.findById(id);
        if (company.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Проверить наличие пользователей в данной компании
     */
    private void checkUsersInCompany(CompanyDTO company) {
        if (company.getUsersId().isEmpty()) {
            return;
        }
        UsersInCompanyDTO usersInCompanyDTO = UsersInCompanyDTO.builder()
                .usersId(company.getUsersId())
                .companyId(company.getId())
                .build();
        getUserClient().checkUsers(usersInCompanyDTO);
    }

    /**
     * Уволить пользователя с заданным id из компании
     */
    private void toDismissalUsers(Set<Integer> users) {
        if (users.isEmpty()) {
            return;
        }
        UsersInCompanyDTO usersInCompanyDTO = UsersInCompanyDTO.builder()
                .usersId(users)
                .companyId(0)
                .build();
        getUserClient().toChangeStatusUsers(usersInCompanyDTO);
    }

    private void toEmployUsers(Set<Integer> users, Integer companyId){
        UsersInCompanyDTO usersInCompanyDTO = UsersInCompanyDTO.builder()
                .usersId(users)
                .companyId(companyId)
                .build();
        getUserClient().toChangeStatusUsers(usersInCompanyDTO);
    }
}
