package com.ishanitech.iaccountingrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private int SN;

    private int companyId;

    private Double amount;

    private String topic;

    private  int billNo;

    private String payTo;

    private Date date;

    private int branchId;

    private boolean status;

}
