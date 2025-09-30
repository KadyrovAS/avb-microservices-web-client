package com.avb.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private String fam;
    private String phoneNumber;
    private Integer companyId;
}
