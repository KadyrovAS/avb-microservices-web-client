package com.avb.client;

import com.avb.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

public interface UserClient {

    @GetExchange
    Page<UserDTO> getAllUsers(Pageable pageable);

    @GetExchange("/{id}")
    UserDTO getUserById(@PathVariable Integer id);

    @PostExchange
    UserDTO createUser(@RequestBody UserDTO user);

    @PutExchange
    UserDTO editUser(@RequestBody UserDTO user);

    @DeleteExchange("/{id}")
    UserDTO deleteUser(@PathVariable Integer id);
}