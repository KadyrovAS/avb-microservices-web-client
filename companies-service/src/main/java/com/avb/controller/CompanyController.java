package com.avb.controller;

import com.avb.model.CompanyDTO;
import com.avb.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);


    /**
     * Возвращает все компании, зарегистрированные в базе данных
     *
     * @return List<Company>
     */
    @GetMapping
    public List<CompanyDTO> getCompanies() {
        logger.info("getCompanies");

        return null;
    }

    /**
     * Возвращает компанию с заданным id
     *
     * @param id - id Компании
     * @return Company
     */
    @GetMapping("{id}")
    public CompanyDTO getCompany(@PathVariable Integer id) {
        logger.info("getCompany: id = {}", id);
        return null;
    }

    /**
     * Добавление компании в базу данных
     *
     * @return Company
     */
    @PostMapping
    public CompanyDTO addCompany(@RequestBody CompanyDTO company) {
        logger.info("addCompany {}", company);
        return null;
    }

    /**
     * Удаление компании с заданным id из базы данных
     *
     * @return Company
     */
    @DeleteMapping("{id}")
    public CompanyDTO deleteCompany(@PathVariable int id) {
        logger.info("deleteCompany id = {}", id);
        return null;
    }

    /**
     * Редактирование компании в базе данных
     *
     * @return Company
     */
    @PutMapping
    public CompanyDTO editCompany(@RequestBody CompanyDTO company) {
        logger.info("editCompany: {}", company);
        return null;
    }


    /**
     * Перемещение user из одной компании в другую
     */
    @PostMapping("/transferUser")
    public void transferUser(@RequestBody Map<String, Integer> idTransfer) {
        logger.info("transferUser {}", idTransfer);
    }

    /**
     * Добавление работника в компанию
     */
    @PostMapping("/addUser")
    public void addUser(@RequestBody UserDTO user) {
        logger.info("addUser {}", user);
    }

}