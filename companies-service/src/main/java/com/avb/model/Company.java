package com.avb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;


@Entity
public class Company {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private double budget;
    private List<Integer> usersId;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public List<Integer> getUsersId() {
        return usersId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setUsersId(List<Integer> usersId) {
        this.usersId = usersId;
    }
}
