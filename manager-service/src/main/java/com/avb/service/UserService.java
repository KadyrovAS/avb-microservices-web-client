package com.avb.service;

import com.avb.model.UserDTO;
import com.avb.model.UserFullDTO;
import com.avb.model.ValidatedPageable;

import java.util.List;

public interface UserService {
    List<UserFullDTO> getAllUsers(ValidatedPageable pageable);
    UserFullDTO getUserById(Integer id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    UserDTO deleteUser(Integer id);
}