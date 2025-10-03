package com.avb.client;

import com.avb.model.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

@HttpExchange("/companies")
public interface CompanyClient {

    @GetExchange
    Page<CompanyDTO> getAllCompanies(Pageable pageable);

    @GetExchange("/{id}")
    CompanyDTO getCompanyById(@PathVariable Integer id);

    @PostExchange
    CompanyDTO createCompany(@RequestBody CompanyDTO company);

    @PutExchange
    CompanyDTO editCompany(@RequestBody CompanyDTO company);

    @DeleteExchange("/{id}")
    CompanyDTO deleteCompany(@PathVariable Integer id);
}