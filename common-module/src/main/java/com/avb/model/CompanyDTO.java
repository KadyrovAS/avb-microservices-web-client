package com.avb.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CompanyDTO {
    private Integer id;
    private String name;
    private double budget;
    private List<Integer> usersId;
}
