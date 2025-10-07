package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.UserDTO;
import com.avb.model.UserFullDTO;
import com.avb.model.ValidatedPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import com.avb.client.UserClient;

import java.util.List;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{

    private UserClient userClient = null;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ClientService clientService;

    public UserServiceImpl(ClientService clientService) {
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
    public List<UserFullDTO> getAllUsers(ValidatedPageable pageable) {
        String direction = pageable.getDirection().name().toLowerCase();
        logger.info("Get all users - page: {}, size: {}, sort: {}, direction: {}",
                pageable.getPage(), pageable.getSize(), pageable.getSort(), direction);

        return getUserClient().getAllUsers(
                pageable.getPage(),
                pageable.getSize(),
                pageable.getSort(),
                direction
        );
    }

    /**
     * Выбрать пользователя по id
     */
    @Override
    public UserFullDTO getUserById(Integer id) {
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