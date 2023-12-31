package com.ishanitech.iaccountingrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesBillDetailDTO {
    private int id;
    private int productId;
    private int qty;
    private String date;
    private double discountPerUnit;
    private double rate;
    private int billId;
    private int companyId;
    private int branchId;
    private int taxRate;
    private long rowTotal;
}
