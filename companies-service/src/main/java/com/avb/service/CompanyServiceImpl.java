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

@Service("CompanyServiceImpl")
public class CompanyServiceImpl implements CompanyService{
    private UserClient userClient = null;
    private final ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepo repository;

    public CompanyServiceImpl(CompanyRepo repository, ClientService clientService) {
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
    public List<CompanyFullDTO> findAllCompanies(Pageable pageable) {
        Page<Company> companiesPage = repository.findAll(pageable);
        Page<CompanyDTO> companiesDTOPage = companiesPage.map(this::toCompanyDTO);

        List<Integer> usersId = new LinkedList<>();
        for (CompanyDTO company : companiesDTOPage) {
            for (Integer id : company.getUsersId()) {
                usersId.add(id);
            }
        }

        Map<Integer, UserDTO> userDTOMap = getUserClient().getListUsersById(usersId);
        List<CompanyFullDTO> companiesFullDTO = new LinkedList<>();

        for (CompanyDTO company : companiesDTOPage) {
            CompanyFullDTO companyFullDTO = CompanyFullDTO.getCompanyFullDTO(company, new LinkedList<>());
            for (Integer id : company.getUsersId()) {
                UserDTO user = userDTOMap.get(id);
                companyFullDTO.getUsers().add(user);
            }
            companiesFullDTO.add(companyFullDTO);
        }

        logger.info("The list of companies was returned!");
        return companiesFullDTO;
    }

    /**
     * Получить компанию по id
     */
    public CompanyDTO findCompanyById(Integer id) {
        Optional<Company> company = repository.findById(id);
        if (company.isEmpty()) {
            throw new AVBException("404", "There is no company with id = " + id + "  in the database!");
        }
        logger.info("A company with id = {} has been found", id);
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

        if (companyDTO.getId() != null &&
                companyDTO.getId() != 0 &&
                repository.existsById(companyDTO.getId())) {
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
        logger.info("A company with id = {} has been deleted from the database!", id);
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

        toFillNullFilds(companyOld, companyNew);
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
     * Скопировать в нулевые поля ранее сохраненные данные
     */
    private void toFillNullFilds(CompanyDTO companyOld, CompanyDTO companyNew) {
        if (companyNew.getName() == null) {
            companyNew.setName(companyOld.getName());
        }
        if (companyNew.getBudget() == null) {
            companyNew.setBudget(companyOld.getBudget());
        }
        if (companyNew.getUsersId() == null) {
            companyNew.setUsersId(companyOld.getUsersId());
        }
    }


    /**
     * Переместить пользователя из одной компании в другую компанию
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
                            .collect(Collectors.toSet())
            );
            editCompany(companyFrom);
        }
        if (companyIdTo != 0) {
            CompanyDTO companyTo = findCompanyById(companyIdTo);
            Set<Integer> users = companyTo.getUsersId();
            users.add(userId);
            companyTo.setUsersId(users);
            editCompany(companyTo);
        }
        logger.info("transfer user {}", transferUser);
    }

    /**
     * Проверить наличие компании с заданным id в базе данных
     */
    @Override
    public Boolean companyExists(Integer id) {
        Optional<Company> company = repository.findById(id);
        return company.isPresent();
    }

    /**
     * Вернуть список компаний по списку
     */
    @Override
    public Map<Integer, CompanyDTO> findListCompanyById(List<Integer> companiesId) {
        Map<Integer, CompanyDTO> companies = new HashMap<>();
        for (Integer id : companiesId) {
            if (id == 0) {
                continue;
            }
            companies.put(id, findCompanyById(id));
        }
        logger.info("A list of companies has been created!");
        return companies;
    }

    @Override
    public CompanyFullDTO findCompanyFullDTOById(Integer id) {
        Map<Integer, UserDTO> userDTOMap;

        CompanyDTO companyDTO = findCompanyById(id);
        CompanyFullDTO companyFullDTO = CompanyFullDTO.getCompanyFullDTO(companyDTO, new ArrayList<>());
        if (!companyDTO.getUsersId().isEmpty()) {
            userDTOMap = getUserClient().getListUsersById(new ArrayList(companyDTO.getUsersId()));
            companyFullDTO.setUsers(new ArrayList(userDTOMap.values()));
        }
        return companyFullDTO;
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

    /**
     * Изменить статус пользователя в сервисе users
     */
    private void toEmployUsers(Set<Integer> users, Integer companyId) {
        UsersInCompanyDTO usersInCompanyDTO = UsersInCompanyDTO.builder()
                .usersId(users)
                .companyId(companyId)
                .build();
        getUserClient().toChangeStatusUsers(usersInCompanyDTO);
    }
}
