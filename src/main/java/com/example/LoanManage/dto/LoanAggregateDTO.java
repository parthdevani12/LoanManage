package com.example.LoanManage.dto;

import lombok.Data;

@Data
public class LoanAggregateDTO {
    private String groupBy; // Indicates the grouping (e.g., "lender", "customer", "interest")
    private double totalRemainingAmount;
    private double totalInterest;
    private double totalPenalty;

}
