package com.avb.client;

import com.avb.model.TransferUserDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface CompanyClient {

    @PostExchange("/transfer-user")
    void transferUser(@RequestBody TransferUserDTO transferUserDTO);

    @GetExchange("/exists/{id}")
    Boolean companyExists(@PathVariable Integer id);

}