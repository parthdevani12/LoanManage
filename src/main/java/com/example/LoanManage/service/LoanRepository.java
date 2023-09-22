package com.example.LoanManage.service;

import com.example.LoanManage.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(String customerId);

    List<Loan> findByLenderId(String lenderId);

    Optional<Loan> findByLoanId(String loanId);
}
