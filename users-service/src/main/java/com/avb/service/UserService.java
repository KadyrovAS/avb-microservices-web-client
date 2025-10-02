package com.avb.service;

import com.avb.model.UserDTO;
import com.avb.model.UsersInCompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService{
    Page<UserDTO> findAllUsers(Pageable pageable);
    UserDTO findUserById(Integer id);
    UserDTO addUser(UserDTO userDTO);
    UserDTO deleteUser(Integer id);
    UserDTO editUser(UserDTO user);
    void checkUsers(UsersInCompanyDTO checkUsersDTO);
    void toChangeStatus(UsersInCompanyDTO checkUsersDTO);
}
