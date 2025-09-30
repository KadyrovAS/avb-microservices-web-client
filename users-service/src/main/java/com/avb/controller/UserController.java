package com.avb.controller;

import com.avb.model.UserDTO;
import com.avb.model.UsersInCompanyDTO;
import com.avb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("UserServiceImp")
    private UserService service;

    /**
     * Возвращает все компании, зарегистрированные в базе данных
     *
     * @return List<User>
     */
    @GetMapping
    public List<UserDTO> getUsers() {
        logger.info("Get all users");
        return service.findAllUsers();
    }

    /**
     * Возвращает компанию с заданным id
     *
     * @param id - id Компании
     * @return User
     */
    @GetMapping("{id}")
    public UserDTO getUser(@PathVariable Integer id) {
        logger.info("getCompany: id = {}", id);
        return service.findUserById(id);
    }

    /**
     * Добавление компании в базу данных
     *
     * @return User
     */
    @PostMapping
    public UserDTO addUser(@RequestBody UserDTO user) {
        logger.info("add user {}", user);
        return service.addUser(user);
    }

    /**
     * Удаление компании с заданным id из базы данных
     *
     * @return User
     */
    @DeleteMapping("{id}")
    public UserDTO deleteUser(@PathVariable int id) {
        logger.info("delete user with id = {}", id);
        return service.deleteUser(id);
    }

    /**
     * Редактирование компании в базе данных
     *
     * @return User
     */
    @PutMapping
    public UserDTO editUser(@RequestBody UserDTO user) {
        logger.info("edit user: {}", user);
        return service.editUser(user);
    }

    /**
     * Проверка наличия пользователей в списках компании
     */
    @PostMapping("/check")
    void checkUsers(@RequestBody UsersInCompanyDTO usersInCompanyDTO){
        logger.info("check users in company");
        service.checkUsers(usersInCompanyDTO);
    }

    /**
     * Удаление пользователей из списков компании
     */
    @PostMapping("/dismissal")
    void dismissalUsers(@RequestBody UsersInCompanyDTO usersInCompanyDTO){
        logger.info("dismissal users from company");
        service.dismissalUsers(usersInCompanyDTO);
    }
}