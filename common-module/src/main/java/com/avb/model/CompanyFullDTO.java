package com.avb.model;

import com.avb.validation.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompanyFullDTO{
    @Null(groups = ValidationGroups.OnCreate.class, message = "ID must be null for new company")
    @NotNull(groups = ValidationGroups.OnUpdate.class, message = "ID is required for update")
    @Min(value = 1, groups = ValidationGroups.OnUpdate.class, message = "ID must be positive")
    private Integer id;

    @NotBlank(message = "Company name is required")
    @Size(min = 1, max = 100, message = "Company name must be between 1 and 100 characters")
    private String name;

    @DecimalMin(value = "0.0", message = "Budget cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Budget must have max 10 integer and 2 fraction digits")
    private Double budget;

    private List<UserDTO> users;

    public static CompanyFullDTO getCompanyFullDTO(CompanyDTO company, List<UserDTO>users){
        return CompanyFullDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .users(users)
                .build();
    }
}
