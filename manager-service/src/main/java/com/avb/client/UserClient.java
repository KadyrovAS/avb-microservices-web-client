package com.avb.client;

import com.avb.model.UserDTO;
import com.avb.model.UserFullDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import java.util.List;

public interface UserClient {

    @GetExchange
    List<UserFullDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    );

    @GetExchange("/full/{id}")
    UserFullDTO getUserById(@PathVariable Integer id);

    @PostExchange
    UserDTO createUser(@RequestBody UserDTO user);

    @PutExchange
    UserDTO editUser(@RequestBody UserDTO user);

    @DeleteExchange("/{id}")
    UserDTO deleteUser(@PathVariable Integer id);
}