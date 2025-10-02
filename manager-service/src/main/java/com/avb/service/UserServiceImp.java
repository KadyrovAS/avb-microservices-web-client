package com.avb.service;

import com.avb.model.AVBException;
import com.avb.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.avb.client.UserClient;

@Service("UserServiceImp")
public class UserServiceImp implements UserService {

    private UserClient userClient = null;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    private final ClientService clientService;
    private Integer userPagination = 1;

    public UserServiceImp(ClientService clientService) {
        this.clientService = clientService;
        userClientInitialization();
    }

    private void userClientInitialization() {
        if (this.userClient == null) {
            this.userClient = clientService.getClient(UserClient.class, "users");
        }
    }

    private UserClient getUserClient(){
        userClientInitialization();
        if (this.userClient == null) {
            throw new AVBException("503", "The users service is not registered!");
        }
        return userClient;
    }
    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Get all users!");
        return getUserClient().getAllUsers(userPagination);
    }

    @Override
    public UserDTO getUserById(Integer id) {
        logger.info("Get user by id = {}!", id);
        return getUserClient().getUserById(id);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("User '{}' was created!", userDTO);
        return getUserClient().createUser(userDTO);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        logger.info("User {} was updated!", userDTO);
        return getUserClient().editUser(userDTO);
    }

    @Override
    public UserDTO deleteUser(Integer id) {
        logger.info("User with id = {} was deleted!", id);
        return getUserClient().deleteUser(id);
    }
}