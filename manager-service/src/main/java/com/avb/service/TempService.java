package com.avb.service;

import com.avb.client.CompanyClient;
import com.avb.client.UserClient;
import com.avb.model.AVBException;
import com.avb.model.CompanyDTO;
import com.avb.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TempService{
    private CompanyClient companyClient = null;
    private UserClient userClient = null;
    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(TempService.class);


    private Integer nRecords = 50;

    public TempService(ClientService clientService) {
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

    public void createAll() {
        createUsers();
        createCompanies();
    }

    public void createUsers(){
        for (int i = 0; i < nRecords; i++){
            UserDTO userDTO = UserDTO.builder()
                    .fam("Фамилия №" + getNumber(i))
                    .name("Имя №" + getNumber(i))
                    .phoneNumber("Номер телефона: " + getNumber(i))
                    .build();
            getUserClient().createUser(userDTO);
        }
    }

    public void createCompanies(){
        for (int i = 0; i < nRecords; i++) {
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .name("Компания №" + getNumber(i))
                    .budget(100.0 * (i + 1))
                    .build();
            getCompanyClient().createCompany(companyDTO);
        }
    }

    private String getNumber(int i){
        i++;
        if (i < 10){
            return "00" + i;
        }else if (i < 100){
            return "0" + i;
        }
        return String.valueOf(i);
    }

}
