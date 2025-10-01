package com.avb.service;

import com.avb.client.UserClient;
import com.avb.model.*;
import com.avb.repository.CompanyRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

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


    private CompanyDTO toCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .usersId(company.getUsersId())
                .build();
    }

    private Company fromCompanyDTO(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setBudget(companyDTO.getBudget());
        company.setUsersId(companyDTO.getUsersId());
        return company;
    }

    /**
     * Поиск всех компаний в базе данных
     *
     * @return
     */
    @Override
    public List<CompanyDTO> findAllCompanies() {
        List<CompanyDTO> companiesDTO = repository.findAll().stream()
                .map(this::toCompanyDTO)
                .toList();

        logger.info("The list of companies was returned!");
        return companiesDTO;
    }

    /**
     * Поиск компании в базе данных по id
     *
     * @param id
     * @return
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
     * Добавление компании в базу данных
     *
     * @param companyDTO
     * @return
     */
    @Override
    public CompanyDTO addCompany(CompanyDTO companyDTO) {
        if (companyDTO.getUsersId() == null) {
            companyDTO.setUsersId(new LinkedList<>());
        }

        checkCompany(companyDTO);
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
     * Удаление компании из базы данных
     *
     * @param id
     * @return
     */
    @Override
    public CompanyDTO deleteCompany(Integer id) {
        CompanyDTO company = findCompanyById(id);
        toDismissalUsers(company.getUsersId());
        repository.deleteById(id);
        logger.info("The company with id = {} has been deleted from the database!", id);
        return company;
    }


    @Override
    public CompanyDTO editCompany(CompanyDTO companyNew) {
        if (companyNew.getId() == null) {
            throw new AVBException("404", "You must specify the company's id!");
        }
        CompanyDTO companyOld = findCompanyById(companyNew.getId());

        copyNullFieldsToNewValue(companyOld, companyNew);
        checkCompany(companyNew);
        checkUsersInCompany(companyNew);

        toDismissalUsers(
                companyOld.getUsersId().stream()
                        .filter(id -> !companyNew.getUsersId().contains(id))
                        .toList()
        );

        repository.save(fromCompanyDTO(companyNew));
        logger.info("The company with id = {} in the database has been updated!", companyNew.getId());
        return companyNew;
    }


    /**
     * Перемещает пользователя из списков одной компании в списки другой компании
     *
     * @param transferUser
     */
    @Override
    @Transactional
    public void transferUser(TransferUserDTO transferUser) {
        Integer userId = transferUser.getUserId();
        Integer companyIdFrom = transferUser.getCompanyIdFrom();
        Integer companyIdTo = transferUser.getCompanyIdTo();

        if (companyIdFrom != 0) {
            CompanyDTO companyFrom = findCompanyById(companyIdFrom);
            companyFrom.setUsersId(
                    companyFrom.getUsersId().stream()
                            .filter(id -> !Objects.equals(id, userId))
                            .toList()
            );
            editCompany(companyFrom);
        }
        if (companyIdTo != 0) {
            CompanyDTO companyTo = findCompanyById(companyIdTo);
            companyTo.getUsersId().add(userId);
            editCompany(companyTo);
        }
    }

    /**
     * Проверяет наличие компании с заданным id в базе данных
     *
     * @param id
     * @return
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
     * Проверяет соответствие пользователей данной компании
     *
     * @param company
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
     * Увольнение пользователей из компании по списку
     *
     * @param users
     */
    private void toDismissalUsers(List<Integer> users) {
        logger.info("toDismissalUsers users {}", users);
        if (users.isEmpty()) {
            return;
        }
        logger.info("toDismissalUsers continue ...");
        UsersInCompanyDTO usersInCompanyDTO = UsersInCompanyDTO.builder()
                .usersId(users)
                .companyId(0)
                .build();
        logger.info("getUserClient.dismissalUsers({})", usersInCompanyDTO);
        getUserClient().dismissalUsers(usersInCompanyDTO);
    }

    private void checkCompany(CompanyDTO companyDTO) {
        if (companyDTO.getId() == null) {
            companyDTO.setId(0);
        }
        if (companyDTO.getBudget() == null) {
            companyDTO.setBudget(0.0);
        }
        if (companyDTO.getName() == null || companyDTO.getName().isBlank()) {
            throw new AVBException("404", "You must specify the company name!");
        }

        if (companyDTO.getBudget() == 0) {
            throw new AVBException("404", "you must specify the company's budget!");
        }
    }


    /**
     * В случае, если при редактировании сущности не были указаны новые значения полей,
     * то сохраняются старые значения
     * @param companyOld
     * @param companyNew
     */
    private void copyNullFieldsToNewValue(CompanyDTO companyOld, CompanyDTO companyNew) {
        if (companyNew.getName() == null || companyNew.getName().isBlank()){
            companyNew.setName(companyOld.getName());
        }
        if (companyNew.getBudget() == null || companyNew.getBudget() == 0){
            companyNew.setBudget(companyOld.getBudget());
        }
        if (companyNew.getUsersId() == null || companyNew.getUsersId().isEmpty()){
            companyNew.setUsersId(companyOld.getUsersId());
        }
    }
}
