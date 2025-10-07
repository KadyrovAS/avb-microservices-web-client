package com.avb.model;

import com.avb.validation.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class UserFullDTO{
    @Null(groups = ValidationGroups.OnCreate.class, message = "ID must be null for new user")
    @NotNull(groups = ValidationGroups.OnUpdate.class, message = "ID is required for update")
    @Min(value = 1, groups = ValidationGroups.OnUpdate.class, message = "ID must be positive")
    private Integer id;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$", message = "First name can only contain letters, spaces and hyphens")
    private String name;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s-]+$", message = "Last name can only contain letters, spaces and hyphens")
    private String fam;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\s-()]{10,20}$", message = "Phone number format is invalid")
    private String phoneNumber;

    private CompanyDTO company;

    public static UserFullDTO getUserFullDTO(UserDTO user, CompanyDTO company) {
        return UserFullDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .fam(user.getFam())
                .phoneNumber(user.getPhoneNumber())
                .company(company)
                .build();
    }
}
