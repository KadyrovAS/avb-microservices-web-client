package com.avb.client;

import com.avb.model.CompanyDTO;
import com.avb.model.TransferUserDTO;
import com.avb.model.UserDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Map;

public interface CompanyClient {

    @PostExchange("/transfer-user")
    void transferUser(@RequestBody TransferUserDTO transferUserDTO);

    @GetExchange("/exists/{id}")
    Boolean companyExists(@PathVariable Integer id);

    @GetExchange("/list")
    Map<Integer, CompanyDTO> getListCompaniesById(@RequestParam List<Integer>companiesId);

    @GetExchange("/{id}")
    CompanyDTO getCompany(@PathVariable Integer id);
}