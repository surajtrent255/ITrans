package com.ishanitech.iaccountingrest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ishanitech.iaccountingrest.service.DbService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {


    private int SN;
    private int companyId;

    private int partyId;

    private Double amount;

    private int paymentModeId;

    private Double tdsDeducted;

    private boolean postDateCheck;

    private int branchId;

    private Date date;

    private Date postCheckDate ;

    private Long checkNo;

    private boolean postDateCheckStatus;

    private boolean paymentStatus;

    private PaymentModeDTO paymentModeDTO;


}
