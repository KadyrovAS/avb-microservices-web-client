package com.avb.controller;

import com.avb.client.CompanyClient;
import com.avb.client.UserClient;
import com.avb.model.AVBException;
import com.avb.model.CompanyDTO;
import com.avb.model.UserDTO;
import com.avb.service.ClientService;
import com.avb.service.CompanyServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create")
public class TempController{

    private CompanyClient companyClient = null;
    private UserClient userClient = null;
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImp.class);
    private final ClientService clientService;

    public TempController(ClientService clientService) {
        this.clientService = clientService;
        companyClientInitialization();
        userClientInitialization();
    }

    private void companyClientInitialization() {
        if (this.companyClient == null) {
            this.companyClient = clientService.getClient(CompanyClient.class, "companies");
        }
    }

    private void userClientInitialization() {
        if (this.userClient == null) {
            this.userClient = clientService.getClient(UserClient.class, "users");
        }
    }

    private CompanyClient getCompanyClient() {
        companyClientInitialization();
        if (this.companyClient == null) {
            throw new AVBException("503", "The companies service is not registered!");
        }
        return companyClient;
    }

    private UserClient getUserClient() {
        userClientInitialization();
        if (this.userClient == null) {
            throw new AVBException("503", "The users service is not registered!");
        }
        return userClient;
    }

    @GetMapping("/all")
    public void createDataBase() {
        CompanyDTO companyDTO = CompanyDTO.builder()
                .name("Рога и копыта")
                .budget(100.0)
                .build();
        getCompanyClient().createCompany(companyDTO);

        companyDTO = CompanyDTO.builder()
                .name("НИЛ ЭФК")
                .budget(200.0)
                .build();
        getCompanyClient().createCompany(companyDTO);

        UserDTO userDTO = UserDTO.builder()
                .fam("Иванов")
                .name("Сергей")
                .phoneNumber("123456")
                .build();
        getUserClient().createUser(userDTO);

        userDTO = UserDTO.builder()
                .fam("Степанов")
                .name("Андрей")
                .phoneNumber("2222222")
                .build();
        getUserClient().createUser(userDTO);
    }

    @GetMapping("/user")
    public void createDataBaseUser() {
        UserDTO userDTO = UserDTO.builder()
                .fam("Иванов")
                .name("Сергей")
                .phoneNumber("123456")
                .build();
        getUserClient().createUser(userDTO);

        userDTO = UserDTO.builder()
                .fam("Степанов")
                .name("Андрей")
                .phoneNumber("2222222")
                .build();
        getUserClient().createUser(userDTO);
    }

    @GetMapping("/company")
    public void createDataBaseCompany() {
        CompanyDTO companyDTO = CompanyDTO.builder()
                .name("Рога и копыта")
                .budget(100.0)
                .build();
        getCompanyClient().createCompany(companyDTO);

        companyDTO = CompanyDTO.builder()
                .name("НИЛ ЭФК")
                .budget(200.0)
                .build();
        getCompanyClient().createCompany(companyDTO);
    }

}
