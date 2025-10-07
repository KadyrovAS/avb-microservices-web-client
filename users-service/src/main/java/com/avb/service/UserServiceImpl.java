package com.avb.service;

import com.avb.client.CompanyClient;
import com.avb.model.*;
import com.avb.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{
    private CompanyClient companyClient = null;
    private final ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo repository;

    public UserServiceImpl(UserRepo repository, ClientService clientService) {
        this.repository = repository;
        this.clientService = clientService;
        companyClientInitialization();
    }

    private void companyClientInitialization() {
        if (this.companyClient == null) {
            this.companyClient = clientService.getClient(CompanyClient.class, "companies");
        }
    }

    private CompanyClient getCompanyClient() {
        companyClientInitialization();
        if (this.companyClient == null) {
            throw new AVBException("503", "The companies service is not registered!");
        }
        return companyClient;
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .fam(user.getFam())
                .phoneNumber(user.getPhoneNumber())
                .companyId(user.getCompanyId())
                .build();
    }

    private User fromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setFam(userDTO.getFam());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setCompanyId(userDTO.getCompanyId());
        return user;
    }

    /**
     * Поиск всех пользователей в базе данных
     */
    @Override
    public List<UserFullDTO> findAllUsers(Pageable pageable) {
        Page<User> usersPage = repository.findAll(pageable);
        Page<UserDTO> usersDTOPages = usersPage.map(this::toUserDTO);
        List<Integer> companiesId = usersDTOPages.stream().map(us -> us.getCompanyId()).toList();
        Map<Integer, CompanyDTO> companyDTOMap = getCompanyClient().getListCompaniesById(companiesId);
        List<UserFullDTO> usersFullDTO = new LinkedList<>();
        for (UserDTO user : usersDTOPages.get().toList()) {
            usersFullDTO.add(
                    UserFullDTO.getUserFullDTO(user, companyDTOMap.get(user.getCompanyId()))
            );
        }

        logger.info("The list of users was returned!");
        return usersFullDTO;
    }


    /**
     * Поиск пользователя в базе данных по id
     */
    @Override
    public UserDTO findUserById(Integer id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            logger.info("There is no user with id = {}  in the database!", id);
            throw new AVBException("404", "There is no user with id = " + id + "  in the database!");
        }

        logger.info("A user with id= {} has been found", id);
        return toUserDTO(user.get());
    }

    /**
     * Вернуть пользователя с информацией, включающей компанию
     */
    @Override
    public UserFullDTO findUserFullDTOById(Integer id) {
        UserDTO userDTO = findUserById(id);
        CompanyDTO companyDTO = getCompanyClient().getCompany(id);
        logger.info("find UserFullDTO");
        return UserFullDTO.getUserFullDTO(userDTO, companyDTO);
    }

    /**
     * Добавление пользователя в базу данных
     */
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        logger.info("User {}", userDTO);
        if (userDTO.getCompanyId() == null) {
            userDTO.setCompanyId(0);
        }
        if (userDTO.getCompanyId() != 0) {
            getCompanyClient().transferUser(
                    TransferUserDTO.builder()
                            .userId(userDTO.getId())
                            .companyIdFrom(0)
                            .companyIdTo(userDTO.getCompanyId())
                            .build()
            );
        }
        User user = repository.save(fromUserDTO(userDTO));

        logger.info("User {} was added to database!", user);
        return toUserDTO(user);
    }


    /**
     * Удаление пользователя из базы данных
     */
    @Override
    public UserDTO deleteUser(Integer id) {
        logger.info("delete user");
        UserDTO user = findUserById(id);
        logger.info("before transfer command");
        getCompanyClient().transferUser(
                TransferUserDTO.builder()
                        .userId(id)
                        .companyIdFrom(user.getCompanyId())
                        .companyIdTo(0)
                        .build()
        );

        logger.info("before repository");
        repository.deleteById(id);
        logger.info("The user with id = {} was deleted from the database!", id);
        return user;
    }


    @Override
    public UserDTO editUser(UserDTO userNew) {
        if (userNew.getId() == null) {
            throw new AVBException("404", "You must specify the user's id!");
        }
        UserDTO userOld = findUserById(userNew.getId());
        toFillNullFilds(userOld, userNew);

        if (!Objects.equals(userOld.getCompanyId(), userNew.getCompanyId())) {
            getCompanyClient().transferUser(
                    TransferUserDTO.builder()
                            .userId(userNew.getId())
                            .companyIdFrom(userOld.getCompanyId())
                            .companyIdTo(userNew.getCompanyId())
                            .build()
            );
        }

        repository.save(fromUserDTO(userNew));
        logger.info("User information {} has been updated in the database", userNew);
        return userNew;
    }

    private void toFillNullFilds(UserDTO userOld, UserDTO userNew) {
        if (userNew.getName() == null) {
            userNew.setName(userOld.getName());
        }
        if (userNew.getFam() == null) {
            userNew.setFam(userOld.getFam());
        }
        if (userNew.getPhoneNumber() == null) {
            userNew.setPhoneNumber(userOld.getPhoneNumber());
        }
        if (userNew.getCompanyId() == null) {
            userNew.setCompanyId(userOld.getCompanyId());
        }
    }

    @Override
    public void checkUsers(UsersInCompanyDTO usersInCompanyDTO) {
        for (Integer userId : usersInCompanyDTO.getUsersId()) {
            Optional<User> user = repository.findById(userId);
            if (user.isEmpty()) {
                throw new AVBException("404", "The user with id = " +
                        userId +
                        " is not registered in the database!");
            }
            if (user.get().getCompanyId() == 0) {
                continue;
            }
            if (!Objects.equals(user.get().getCompanyId(), usersInCompanyDTO.getCompanyId())) {
                throw new AVBException("404", "There is no user with id = " +
                        userId +
                        " in the company with id = " +
                        usersInCompanyDTO.getCompanyId());
            }
        }
    }

    @Override
    public void toChangeStatus(UsersInCompanyDTO usersInCompanyDTO) {
        for (Integer userId : usersInCompanyDTO.getUsersId()) {
            Optional<User> userOpt = repository.findById(userId);
            if (userOpt.isEmpty()) {
                throw new AVBException("404", "There is no user with id = " +
                        userId +
                        " in the database!");
            }
            User user = userOpt.get();
            user.setCompanyId(usersInCompanyDTO.getCompanyId());
            repository.save(user);
        }
    }

    @Override
    public Map<Integer, UserDTO> findListUsersById(List<Integer> usersId) {
        Map<Integer, UserDTO> users = new HashMap<>();

        for (Integer id : usersId) {
            users.put(id, findUserById(id));
        }
        return users;
    }
}
