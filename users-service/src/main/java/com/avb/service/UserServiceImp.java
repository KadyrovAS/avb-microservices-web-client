package com.avb.service;

import com.avb.client.CompanyClient;
import com.avb.model.*;
import com.avb.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("UserServiceImp")
public class UserServiceImp implements UserService {
    private CompanyClient companyClient = null;
    private final ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepo repository;

    public UserServiceImp(UserRepo repository, ClientService clientService) {
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
     *
     * @return
     */
    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        Page<User> usersPage = repository.findAll(pageable);
        Page<UserDTO>usersDTOPages = usersPage.map(this::toUserDTO);

        logger.info("The list of users was returned!");
        return usersDTOPages;
    }

    /**
     * Поиск пользователя в базе данных по id
     *
     * @param id
     * @return
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
     * Добавление пользователя в базу данных
     *
     * @param userDTO
     * @return
     */
    @Override
    public UserDTO addUser(UserDTO userDTO) {
        logger.info("User {}", userDTO);
        if (userDTO.getCompanyId() != 0){
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
     *
     * @param id
     * @return
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

    @Override
    public void checkUsers(UsersInCompanyDTO usersInCompanyDTO) {
        for (Integer userId : usersInCompanyDTO.getUsersId()) {
            Optional<User> user = repository.findById(userId);
            if (user.isEmpty()) {
                throw new AVBException("404", "The user with id = " +
                        userId +
                        " is not registered in the database!");
            }
            if (user.get().getCompanyId() == 0){
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
}
