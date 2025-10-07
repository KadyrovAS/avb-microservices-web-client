package com.avb.controller;

import com.avb.model.UserDTO;
import com.avb.model.UserFullDTO;
import com.avb.model.ValidatedPageable;
import com.avb.service.UserService;
import com.avb.validation.ValidationGroups;
import jakarta.validation.Valid;
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
@Validated
public class UserController{

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService service;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * Выбрать всех пользователей из базы данных
     */
    @GetMapping
    public List<UserFullDTO> getAllUsers(@Valid ValidatedPageable pageable) {
        logger.info("Get users page: page={}, size={}, sort={}",
                pageable.getPage(), pageable.getSize(), pageable.getSort());
        return service.getAllUsers(pageable);
    }

    /**
     * Создать пользователя
     */
    @PostMapping
    public UserDTO createUser(@Validated(ValidationGroups.OnCreate.class) @RequestBody UserDTO userDTO) {
        logger.info("create user {}!", userDTO);
        return service.createUser(userDTO);
    }

    /**
     * Получить пользователя по id
     */
    @GetMapping("/{id}")
    public UserFullDTO getUserById(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("Get user by id {}!", id);
        return service.getUserById(id);
    }

    /**
     * Редактировать пользователя
     */
    @PutMapping
    public UserDTO updateUser(@Validated(ValidationGroups.OnUpdate.class) @RequestBody UserDTO userDTO) {
        logger.info("Update user {}!", userDTO);
        return service.updateUser(userDTO);
    }

    /**
     * Удалить пользователя с заданным id
     */
    @DeleteMapping("/{id}")
    public UserDTO deleteUser(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("Delete user with id = {}!", id);
        return service.deleteUser(id);
    }
}