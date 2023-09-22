package com.example.LoanManage.mapper;

import com.example.LoanManage.dto.LoanDTO;
import com.example.LoanManage.entity.Loan;
import org.springframework.stereotype.Component;

public class LoanMapper {
    public static LoanDTO toDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setCustomerId(loan.getCustomerId());
        dto.setLenderId(loan.getLenderId());
        dto.setAmount(loan.getAmount());
        dto.setRemainingAmount(loan.getRemainingAmount());
        dto.setPaymentDate(loan.getPaymentDate());
        dto.setInterestPerDay(loan.getInterestPerDay());
        dto.setDueDate(loan.getDueDate());
        dto.setPenaltyPerDay(loan.getPenaltyPerDay());
        dto.setCancelled(loan.isCancelled());
        return dto;
    }

    public static Loan toEntity(LoanDTO dto) {
        Loan loan = new Loan();
        loan.setLoanId(dto.getLoanId());
        loan.setCustomerId(dto.getCustomerId());
        loan.setLenderId(dto.getLenderId());
        loan.setAmount(dto.getAmount());
        loan.setRemainingAmount(dto.getRemainingAmount());
        loan.setPaymentDate(dto.getPaymentDate());
        loan.setInterestPerDay(dto.getInterestPerDay());
        loan.setDueDate(dto.getDueDate());
        loan.setPenaltyPerDay(dto.getPenaltyPerDay());
        loan.setCancelled(dto.isCancelled());
        return loan;
    }
}
