package com.avb.client;

import com.avb.model.CompanyDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface CompanyClient {

    @GetExchange
    List<CompanyDTO> getAllCompanies();

    @GetExchange("{id}")
    CompanyDTO getCompanyById(@PathVariable Integer id);

    @PostExchange("/")
    CompanyDTO createCompany(@RequestBody CompanyDTO user);

    @PutExchange("/")
    CompanyDTO editCompany(@RequestBody CompanyDTO user);

    @DeleteExchange("{id}")
    CompanyDTO deleteCompany(@PathVariable Integer id);
}