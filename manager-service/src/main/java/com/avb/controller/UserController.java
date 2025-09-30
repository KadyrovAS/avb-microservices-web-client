package com.avb.controller;

import com.avb.model.UserDTO;
import com.avb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("UserServiceImp")
    private UserService service;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<UserDTO> getAllCompanies() {
        logger.info("Get all companies!");
        return service.getAllUsers();
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        logger.info("create user {}!", userDTO);
        return service.createUser(userDTO);
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        logger.info("Get user by id {}!", id);
        return service.getUserById(id);
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        logger.info("Update user {}!", userDTO);
        return service.updateUser(userDTO);
    }

    @DeleteMapping("{id}")
    public UserDTO deleteUser(@PathVariable Integer id) {
        logger.info("Delete user with id = {}!", id);
        return service.deleteUser(id);
    }
}