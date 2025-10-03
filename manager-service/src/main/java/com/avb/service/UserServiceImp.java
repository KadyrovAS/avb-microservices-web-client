package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.avb.client.UserClient;

@Service("UserServiceImp")
public class UserServiceImp implements UserService{

    private UserClient userClient = null;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    private final ClientService clientService;

    public UserServiceImp(ClientService clientService) {
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
     * Выбрать всех пользователей
     */
    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        logger.info("Get all users!");
        return getUserClient().getAllUsers(pageable);
    }

    /**
     * Выбрать пользователя по id
     */
    @Override
    public UserDTO getUserById(Integer id) {
        logger.info("Get user by id = {}!", id);
        return getUserClient().getUserById(id);
    }


    /**
     * Создать нового пользователя
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("User '{}' was created!", userDTO);
        return getUserClient().createUser(userDTO);
    }

    /**
     * Изменить пользователя
     */
    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        logger.info("User {} was updated!", userDTO);
        return getUserClient().editUser(userDTO);
    }

    /**
     * Удалить пользователя
     */
    @Override
    public UserDTO deleteUser(Integer id) {
        logger.info("User with id = {} was deleted!", id);
        return getUserClient().deleteUser(id);
    }
}