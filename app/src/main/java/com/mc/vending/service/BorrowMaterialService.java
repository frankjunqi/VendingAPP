package com.mc.vending.service;

import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.TransactionWrapperData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.db.CustomerEmpLinkDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.ZillionLog;

public class BorrowMaterialService extends BasicService {
    private static BorrowMaterialService instance;

    public static BorrowMaterialService getInstance() {
        if (instance == null) {
            instance = new BorrowMaterialService();
        }
        return instance;
    }

    public ServiceResult<Boolean> checkBorrowStatus(VendingChnData vendingChn, TransactionWrapperData transactionWrapper, String commandType) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            validateBorrowStatus(vendingChn, transactionWrapper, commandType);
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>检查货道借还状态发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>检查货道借还状态发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查货道借还状态发生异常!");
        }
        return result;
    }

    public ServiceResult<Boolean> checkBorrowCustomer(String borrowStatus, String cusEmpId, String commandType, TransactionWrapperData transactionWrapper) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            validateBorrowCustomer(borrowStatus, cusEmpId, commandType, transactionWrapper);
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>检查是否原借出人归还发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>检查是否原借出人归还发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查是否原借出人归还发生异常!");
        }
        return result;
    }

    private void validateBorrowCustomer(String borrowStatus, String cusEmpId, String commandType, TransactionWrapperData transactionWrapper) {
        if (transactionWrapper != null) {
            int transQty = transactionWrapper.getTransQty();
            String createUser = transactionWrapper.getCreateUser();
            if (transQty == -1 && "2".equals(commandType) && !createUser.equals(cusEmpId)) {
                throw new BusinessException("当前用户并非上次借出人,\n上次是由" + transactionWrapper.getName() + " 在 " + transactionWrapper.getCreateTime() + "时间借出,联系电话:\n" + transactionWrapper.getPhone() + "!");
            }
        }
    }

    public void saveStockTransaction(VendingChnData vendingChn, VendingCardPowerWrapperData vendingCardPowerWrapper, String commandType) {
        int qty = 0;
        if ("1".equals(commandType)) {
            qty = -1;
        }
        if ("2".equals(commandType)) {
            qty = 1;
        }
        boolean flag = new StockTransactionDbOper().addStockTransaction(buildStockTransaction(qty, StockTransactionData.BILL_TYPE_BORROW, "", vendingChn, vendingCardPowerWrapper));
    }

    public TransactionWrapperData getStockTransaction(VendingChnData vendingChn) {
        String createUser = "";
        String createTime = "";
        String name = "";
        String phone = "";
        StockTransactionData stockTransaction = new StockTransactionDbOper().getVendingChnByCode(vendingChn.getVc1Vd1Id(), vendingChn.getVc1Code(), StockTransactionData.BILL_TYPE_BORROW);
        if (stockTransaction == null) {
            return null;
        }
        createUser = stockTransaction.getTs1CreateUser();
        createTime = stockTransaction.getTs1CreateTime();
        int transQty = stockTransaction.getTs1TransQty().intValue();
        CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkDbOper().getCustomerEmpLinkByCeId(createUser);
        if (customerEmpLink != null) {
            name = customerEmpLink.getCe1Name();
            phone = customerEmpLink.getCe1Phone();
        }
        TransactionWrapperData transactionWrapper = new TransactionWrapperData();
        transactionWrapper.setCreateUser(createUser);
        transactionWrapper.setName(name);
        transactionWrapper.setCreateTime(createTime);
        transactionWrapper.setPhone(phone);
        transactionWrapper.setTransQty(transQty);
        return transactionWrapper;
    }

    private void validateBorrowStatus(VendingChnData vendingChn, TransactionWrapperData transactionWrapper, String commandType) {
        if (transactionWrapper != null) {
            int transQty = transactionWrapper.getTransQty();
            String vcCode = vendingChn.getVc1Code();
            if (transQty == -1 && "1".equals(commandType)) {
                throw new BusinessException("货道号 " + vcCode + " 的产品,\n已由 " + transactionWrapper.getName() + " 在 " + transactionWrapper.getCreateTime() + " 时间借出,联系电话:\n " + transactionWrapper.getPhone() + " !");
            } else if (transQty == 1 && "2".equals(commandType)) {
                throw new BusinessException("货道号 " + vcCode + " 的产品已归还,请重新输入!");
            }
        } else if ("1".equals(commandType)) {
            throw new BusinessException("借还货道初始状态，请按 '还'键放入库存！");
        }
    }
}
