package com.ishanitech.iaccountingrest.service.impl;
import com.ishanitech.iaccountingrest.dao.*;
import com.ishanitech.iaccountingrest.dto.ProductDTO;
import com.ishanitech.iaccountingrest.dto.SalesBillDTO;
import com.ishanitech.iaccountingrest.dto.SalesBillDetailDTO;
import com.ishanitech.iaccountingrest.service.BillService;

import com.ishanitech.iaccountingrest.service.DbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final DbService dbService;
    @Override
    public List<SalesBillDTO> getAllBills() {
        return dbService.getDao(SalesBillDAO.class).getAllBills();
    }

    @Override
    public SalesBillDTO getBillById(int id) {
        return dbService.getDao(SalesBillDAO.class).getBillById(id);
    }

    @Transactional
    @Override
    public void deleteBillById(int id) {
        SalesBillDAO salesBillDAO = dbService.getDao(SalesBillDAO.class);
        SalesBillDetailDAO salesBillDetailDAO = dbService.getDao(SalesBillDetailDAO.class);
        SalesBillDTO salesBillDTO =  salesBillDAO.getBillById(id);
        List<SalesBillDetailDTO> salesBillDetailDTOS= salesBillDetailDAO.getSalesInfoByBillId(id);
        StockDAO stockDAO = dbService.getDao(StockDAO.class);
        salesBillDetailDTOS.forEach((salesBillDetailDTO -> {
            stockDAO.increaseStockQuantity(salesBillDetailDTO.getProductId(), salesBillDTO.getCompanyId(), salesBillDTO.getBranchId(), salesBillDetailDTO.getQty());
        }));
         dbService.getDao(SalesBillDAO.class).deleteBillById(id);
    }

    @Override
    public Integer printTheBill(int billId, int userId) {
        Integer printSuccess = dbService.getDao(SalesBillDAO.class).printTheBillWithBillId(billId, new Date(), userId);
        SalesBillDTO salesBillDTO = dbService.getDao(SalesBillDAO.class).getBillById(billId);
        log.info("Sales Bill with id"+ billId + " has been printed  by user with userId "+userId+ " salesBillDTO"+ salesBillDTO);
       return printSuccess;
    }

    @Override
    public List<SalesBillDTO> getAllBillsByCompId(int compId, int branchId) {
        List<SalesBillDTO> salesBillDTOList = dbService.getDao(SalesBillDAO.class).getSalesBillByCompanyId(compId,  branchId);
        return salesBillDTOList;
    }

    @Override
    public List<SalesBillDTO> getLimitedSalesBillsByCompIdAndBranchId(Integer offset, Integer pageTotalItems, String searchBy, String searchWildCard, String sortBy, Integer compId, Integer branchId) {
//        List<SalesBillDTO> salesBillDTOList;
//        salesBillDTOList = dbService.getDao(SalesBillDAO.class).getLimitedSalesBillByCompanyAndBranchId(offset, pageTotalItems, compId, branchId);
//        return salesBillDTOList;
        List<SalesBillDTO> productList;
        String caseQuery="";
        if(searchBy.equals("id")){
            caseQuery = " company_id = "+compId+" and branch_id = "+branchId+" and "+searchBy+" = '"+searchWildCard+"' order by "+sortBy+" desc "+
                    "limit "+ pageTotalItems+" offset "+(offset-1);
        } else {
            caseQuery = " company_id = "+compId+" and branch_id = "+branchId+" and "+searchBy+" like '"+searchWildCard+"%' order by "+sortBy+" desc "+
                    "limit "+ pageTotalItems+" offset "+(offset-1);
        }
        productList = dbService.getDao(SalesBillDAO.class).getLimitedSalesBillByCompanyAndBranchId(caseQuery);
        return productList;
    }
    @Transactional
    @Override
    public Object approveTheBillById(int billId) {
        SalesBillDAO salesBillDAO =  dbService.getDao(SalesBillDAO.class);
        SalesBillDTO salesBillDTO = salesBillDAO.getBillById(billId);
        List<SalesBillDetailDTO> salesBillDetailDTOS = dbService.getDao(SalesBillDetailDAO.class).getSalesInfoByBillId(billId);
        StockDAO stockDAO = dbService.getDao(StockDAO.class);

        BillNoGeneratorDAO billNoGeneratorDAO = dbService.getDao(BillNoGeneratorDAO.class);
        int bill_no = billNoGeneratorDAO.getBillNoForCurrentFiscalYear(salesBillDTO.getCompanyId(), salesBillDTO.getBranchId(), salesBillDTO.isHasAbbr());

        String billNoToBeUpdated = "B01 "+bill_no;
        salesBillDetailDTOS.forEach((salesBillDetailDTO -> {
            stockDAO.decreaseTheStockQuantity(salesBillDetailDTO.getProductId(), salesBillDTO.getCompanyId(), salesBillDTO.getBranchId(), salesBillDetailDTO.getQty());
        }));
        salesBillDAO.makeDraftFalse(billNoToBeUpdated, billId);
        return salesBillDTO;
    }

}