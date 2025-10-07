package com.avb.controller;

import com.avb.model.UserDTO;
import com.avb.model.UserFullDTO;
import com.avb.model.UsersInCompanyDTO;
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
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService service;

    /**
     * Получить всех пользователей с пагинацией
     */
    @GetMapping
    public List<UserFullDTO> getAllUsers(@Valid ValidatedPageable pageable) {
        logger.info("get all users with pagination: page={}, size={}, sort={}",
                pageable.getPage(), pageable.getSize(), pageable.getSort());
        return service.findAllUsers(pageable.toPageable());
    }

    /**
     * Получить пользователя по ID
     */
    @GetMapping("/{id}")
    public UserDTO getUser(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("getUser: id = {}", id);
        return service.findUserById(id);
    }

    /**
     * Создать нового пользователя
     */
    @PostMapping
    public UserDTO addUser(@Validated(ValidationGroups.OnCreate.class) @RequestBody UserDTO user) {
        logger.info("add user {}", user);
        return service.addUser(user);
    }

    /**
     * Удалить пользователя
     */
    @DeleteMapping("/{id}")
    public UserDTO deleteUser(
            @PathVariable
            @Min(value = 1, message = "User ID must be positive")
            Integer id) {
        logger.info("delete user with id = {}", id);
        return service.deleteUser(id);
    }

    /**
     * Обновить пользователя
     */
    @PutMapping
    public UserDTO editUser(@Validated(ValidationGroups.OnUpdate.class) @RequestBody UserDTO user) {
        logger.info("edit user: {}", user);
        return service.editUser(user);
    }

    /**
     * Проверить пользователей (для внутреннего использования)
     */
    @PostMapping("/check")
    void checkUsers(@Valid @RequestBody UsersInCompanyDTO usersInCompanyDTO) {
        logger.info("check users in company");
        service.checkUsers(usersInCompanyDTO);
    }

    /**
     * Изменить статус пользователей (для внутреннего использования)
     */
    @PostMapping("/change-status")
    void dismissalUsers(@Valid @RequestBody UsersInCompanyDTO usersInCompanyDTO) {
        logger.info("dismissal users from company");
        service.toChangeStatus(usersInCompanyDTO);
    }

    @GetMapping("/list")
    Map<Integer, UserDTO> getListUsersById(@RequestParam List<Integer> usersId){
        logger.info("getListUsersById");
        return service.findListUsersById(usersId);
    }

    @GetMapping("/full/{id}")
    UserFullDTO getUserFullDTOById(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id
    ){
        return service.findUserFullDTOById(id);
    }
}