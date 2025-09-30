package com.avb.service;

import com.avb.model.UserDTO;
import com.avb.model.UsersInCompanyDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService{
    List<UserDTO> findAllUsers();
    UserDTO findUserById(Integer id);
    UserDTO addUser(UserDTO userDTO);
    UserDTO deleteUser(Integer id);
    UserDTO editUser(UserDTO user);
    void checkUsers(UsersInCompanyDTO checkUsersDTO);
    void dismissalUsers(UsersInCompanyDTO checkUsersDTO);
}
