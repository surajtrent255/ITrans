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
public class ProductDTO {
       private Integer id;
       private String name;
       private String description;
       private Double sellingPrice;
       private Double costPrice;
       private boolean productType; // false for inventory items  true for non inventory items
       private boolean averagePriceStatus;//false for defult if true then rate percentage  should be given
       private Integer ratePercentage;
       private Date createDate;
       private Date updateDate;
       private Integer userId;
       private Integer companyId;
       private Integer branchId;
       private Integer sellerId;
       private Integer categoryId;
       private String barcode;
       private Double discount;
       private Double tax;
       private String unit;
       private  Integer qtyPerUnit;
       private boolean deleted;
       private Integer stock;
       private Integer taxApproach;
}
