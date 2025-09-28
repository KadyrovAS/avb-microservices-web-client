//package com.avb.service;
//
//import com.avb.model.UserDTO;
//import org.springframework.stereotype.Service;
//import com.avb.client.UserClient;
//import java.util.List;
//
//@Service
//public class UserService {
//
//    private final UserClient userClient;
//
//    public UserService(UserClient userClient) {
//        this.userClient = userClient;
//    }
//
//    public List<UserDTO> getAllUsers() {
//        return userClient.getAllUsers();
//    }
//
//    public UserDTO getUserById(Integer id) {
//        return userClient.getUserById(id);
//    }
//
//    public UserDTO createUser(UserDTO userDTO) {
//        return userClient.createUser(userDTO);
//    }
//
//    public UserDTO updateUser(UserDTO userDTO) {
//        return userClient.editUser(userDTO);
//    }
//
//    public void deleteUser(Integer id) {
//        userClient.deleteUser(id);
//    }
//
//    public List<UserDTO> getUsersByCompanyId(Integer companyId) {
//        List<UserDTO> allUsers = getAllUsers();
//        return allUsers.stream()
//                .filter(user -> companyId.equals(user.getCompanyId()))
//                .toList();
//    }
//}