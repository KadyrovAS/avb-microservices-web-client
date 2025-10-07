package com.avb.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferUserDTO{
    private Integer userId;
    private Integer companyIdFrom;
    private Integer companyIdTo;
}
