package com.avb.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UsersInCompanyDTO{
    private Set<Integer> usersId;
    private Integer companyId;
}
