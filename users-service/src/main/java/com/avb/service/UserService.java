package com.avb.service;

import com.avb.model.UserDTO;
import com.avb.model.UserFullDTO;
import com.avb.model.UsersInCompanyDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService{
    List<UserFullDTO> findAllUsers(Pageable pageable);
    UserDTO findUserById(Integer id);
    UserFullDTO findUserFullDTOById(Integer id);
    UserDTO addUser(UserDTO userDTO);
    UserDTO deleteUser(Integer id);
    UserDTO editUser(UserDTO user);
    void checkUsers(UsersInCompanyDTO checkUsersDTO);
    void toChangeStatus(UsersInCompanyDTO checkUsersDTO);
    Map<Integer, UserDTO> findListUsersById(List<Integer> usersId);
}
