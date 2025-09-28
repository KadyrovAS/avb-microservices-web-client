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
}
