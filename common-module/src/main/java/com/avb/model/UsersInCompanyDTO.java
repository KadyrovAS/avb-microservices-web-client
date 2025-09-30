package com.avb.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UsersInCompanyDTO {
    private List<Integer> usersId;
    private Integer companyId;
}
