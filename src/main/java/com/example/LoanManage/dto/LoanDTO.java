package com.example.LoanManage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoanDTO {
    private String loanId;
    private String customerId;
    private String lenderId;
    private double amount;
    private double remainingAmount;
    private Date paymentDate;
    private double interestPerDay;
    private Date dueDate;
    private double penaltyPerDay;
    private boolean cancelled;

}
