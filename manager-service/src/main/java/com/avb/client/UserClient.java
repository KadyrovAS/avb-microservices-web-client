package com.avb.client;

//import com.avb.model.UserDTO;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.service.annotation.DeleteExchange;
//import org.springframework.web.service.annotation.GetExchange;
//import org.springframework.web.service.annotation.PostExchange;
//import org.springframework.web.service.annotation.PutExchange;
//
//import java.util.List;
//
//public interface UserClient {
//
//    @GetExchange
//    List<UserDTO> getAllUsers();
//
//    @GetExchange("{id}")
//    UserDTO getUserById(@PathVariable Integer id);
//
//    @PostExchange
//    UserDTO createUser(@RequestBody UserDTO user);
//
//    @PutExchange
//    UserDTO editUser(@RequestBody UserDTO user);
//
//    @DeleteExchange("{id}")
//    UserDTO deleteUser(@PathVariable Integer id);
//}