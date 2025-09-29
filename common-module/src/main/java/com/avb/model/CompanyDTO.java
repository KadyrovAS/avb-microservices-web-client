package com.avb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {
    private Integer id;
    private String name;
    private double budget;
    private List<Integer> usersId;
}
