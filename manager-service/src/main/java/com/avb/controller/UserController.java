package com.avb.controller;

import com.avb.model.UserDTO;
import com.avb.service.UserService;
import com.avb.validation.ValidationGroups;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
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
    public List<UserDTO> getAllUsers() {
        logger.info("Get all companies!");
        return service.getAllUsers();
    }

    @PostMapping
    public UserDTO createUser(@Validated(ValidationGroups.OnCreate.class) @RequestBody UserDTO userDTO) {
        logger.info("create user {}!", userDTO);
        return service.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("Get user by id {}!", id);
        return service.getUserById(id);
    }

    @PutMapping
    public UserDTO updateUser(@Validated(ValidationGroups.OnUpdate.class) @RequestBody UserDTO userDTO) {
        logger.info("Update user {}!", userDTO);
        return service.updateUser(userDTO);
    }

    @DeleteMapping("/{id}")
    public UserDTO deleteUser(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("Delete user with id = {}!", id);
        return service.deleteUser(id);
    }
}