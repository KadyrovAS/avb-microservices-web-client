package com.avb.client;

import com.avb.model.CompanyDTO;
import com.avb.model.CompanyFullDTO;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import java.util.List;

public interface CompanyClient {

    @GetExchange
    List<CompanyFullDTO> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    );

    @GetExchange("/{id}")
    CompanyDTO getCompanyById(@PathVariable Integer id);

    @PostExchange
    CompanyDTO createCompany(@RequestBody CompanyDTO company);

    @PutExchange
    CompanyDTO editCompany(@RequestBody CompanyDTO company);

    @DeleteExchange("/{id}")
    CompanyDTO deleteCompany(@PathVariable Integer id);

    @GetExchange("/full/{id}")
    CompanyFullDTO getCompanyFullDTOById(
            @PathVariable
            @Min(value = 1, message = "Company ID must be positive")
            Integer id);
}