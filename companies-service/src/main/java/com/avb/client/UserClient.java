package com.avb.client;

import com.avb.model.CompanyDTO;
import com.avb.model.UserDTO;
import com.avb.model.UsersInCompanyDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Map;

public interface UserClient {

    @PostExchange("/check")
    void checkUsers(@RequestBody UsersInCompanyDTO usersInCompanyDTO);

    @PostExchange("/change-status")
    void toChangeStatusUsers(@RequestBody UsersInCompanyDTO usersInCompanyDTO);

    @GetExchange("/list")
    Map<Integer, UserDTO> getListUsersById(@RequestParam List<Integer> usersId);
}