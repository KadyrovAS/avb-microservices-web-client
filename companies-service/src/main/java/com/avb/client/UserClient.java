package com.avb.client;

import com.avb.model.UsersInCompanyDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface UserClient {

    @PostExchange("/check")
    void checkUsers(@RequestBody UsersInCompanyDTO usersInCompanyDTO);

    @PostExchange("/dismissal")
    void dismissalUsers(@RequestBody UsersInCompanyDTO usersInCompanyDTO);

}