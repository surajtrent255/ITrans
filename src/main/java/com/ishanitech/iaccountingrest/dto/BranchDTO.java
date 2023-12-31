package com.ishanitech.iaccountingrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {

    private int id;
    private int companyId;
    private String name;
    private String abbrv;
    private String description;
    private Integer state;
    private String district;
    private String munVdc;
    private Integer wardNo;
    private Long phone;

}
