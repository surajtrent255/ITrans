package com.ishanitech.iaccountingrest.dao;

import com.ishanitech.iaccountingrest.dto.BillNoGeneratorDTO;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

public interface BillNoGeneratorDAO {

    @GetGeneratedKeys
    @SqlUpdate("insert into bill_no_generator" +
            "(" +
            " fiscal_year," +
            " bill_no," +
            " active," +
            " company_id,"+
            " branch_id "+
            ") values " +
            "(" +
            " :fiscalYear," +
            " 1," +
            " true" +
            " :companyId, "+
            " :branchId "+
            ")")
    int createNewFiscalYear(@Bind String fiscalYear, int companyId, int branchId);

    @SqlUpdate("update bill_no_generator set active = false where company_id = :companyId and branch_id = :branchId and fiscal_year != :fiscalYear;")
    void disableOtherYears(@Bind String fiscalYear, int companyId, int branchId);

    @Transactional
    default void initilizeBillNoForNewFiscalYear(String fiscalyear, int companyId, int branchId){
        int fiscalYearearId = createNewFiscalYear( fiscalyear, companyId, branchId);
        String fiscalYear = getCurrentFiscalYear(companyId, branchId);
        disableOtherYears(fiscalYear, companyId, branchId);
    }

    @SqlQuery("select bill_no from bill_no_generator where active = true and company_id = :companyId and branch_id = :branchId;")
    int getBillNo(int companyId, int branchId);

    @SqlUpdate("update bill_no_generator set bill_no = bill_no +  1 where active = true and company_id = :companyId and branch_id = :branchId;")
    void increaseBillNo(int companyId, int branchId);

    @Transactional
    default int getBillNoForCurrentFiscalYear(int companyId, int branchId){
        int currentBillNo = getBillNo(companyId, branchId);
        increaseBillNo(companyId, branchId);
        return currentBillNo;
    }

    @SqlQuery("select bg.fiscal_year from bill_no_generator bg where bg.active = true and bg.company_id = :companyId and bg.branch_id = :branchId")
    String getCurrentFiscalYear(int companyId, int branchId);

    @SqlQuery("select bng.id as id, bng.fiscal_year as fiscal_year, " +
            " bng.bill_no as bill_no, bng.active as active " +
            " from bill_no_generator bng" +
            " where active=true;")
    @RegisterBeanMapper(BillNoGeneratorDTO.class)
    BillNoGeneratorDTO getBillNoGeneratorEntityByActive();
}
