//package com.avb.controller;
//
//import com.avb.model.UserDTO;
//import com.avb.service.UserService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    private final UserService userService;  // ← Используем Service вместо Client
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public List<UserDTO> getAllUsers() {
//        return userService.getAllUsers();  // ← Вызов через сервис
//    }
//
//    @PostMapping
//    public UserDTO createUser(@RequestBody UserDTO userDTO) {
//        return userService.createUser(userDTO);
//    }
//
//    @GetMapping("/{id}")
//    public UserDTO getUserById(@PathVariable Integer id) {
//        return userService.getUserById(id);
//    }
//
//    @PutMapping
//    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
//        return userService.updateUser(userDTO);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Integer id) {
//        userService.deleteUser(id);
//    }
//
//    @GetMapping("/company/{companyId}")
//    public List<UserDTO> getUsersByCompany(@PathVariable Integer companyId) {
//        return userService.getUsersByCompanyId(companyId);
//    }
//}