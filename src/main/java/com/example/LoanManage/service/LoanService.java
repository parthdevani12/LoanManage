package com.example.LoanManage.service;

import com.example.LoanManage.dto.LoanAggregateDTO;
import com.example.LoanManage.entity.Loan;
import com.example.LoanManage.exception.LoanValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final Logger logger = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    /**
     * Retrieve a list of all loans.
     *
     * @return List of all loans.
     */
    public List<Loan> getAllLoans() {
        logger.info("Retrieving all loans.");
        return loanRepository.findAll();
    }

    /**
     * Add a new loan entry.
     *
     * @param loan The Loan object to be added.
     * @return The created Loan object.
     * @throws LoanValidationException if payment date is greater than due date.
     */
    public Loan addLoan(Loan loan) {
        logger.info("Adding a new loan: {}", loan);

        // Validation logic, e.g., check payment date vs. due date
        if (loan.getPaymentDate().after(loan.getDueDate())) {
            logger.error("Payment date is greater than the due date for loan: {}", loan);
            throw new LoanValidationException("Payment date cannot be greater than the due date");
        }

        // Save the loan
        Loan savedLoan = loanRepository.save(loan);
        logger.info("Loan added successfully with ID: {}", savedLoan.getId());
        return savedLoan;
    }

    /**
     * Retrieve a loan by its unique loan ID.
     *
     * @param loanId The loan ID to search for.
     * @return An Optional containing the Loan object if found, empty otherwise.
     */
    public Optional<Loan> getLoanByLoanId(String loanId) {
        logger.info("Retrieving loan by loan ID: {}", loanId);
        return loanRepository.findByLoanId(loanId);
    }

    /**
     * Retrieve loans associated with a specific customer.
     *
     * @param customerId The Customer ID to filter loans.
     * @return List of loans associated with the specified customer.
     */
    public List<Loan> getLoansByCustomerId(String customerId) {
        logger.info("Retrieving loans by customer ID: {}", customerId);
        return loanRepository.findByCustomerId(customerId);
    }

    /**
     * Retrieve loans associated with a specific lender.
     *
     * @param lenderId The Lender ID to filter loans.
     * @return List of loans associated with the specified lender.
     */
    public List<Loan> getLoansByLenderId(String lenderId) {
        logger.info("Retrieving loans by lender ID: {}", lenderId);
        return loanRepository.findByLenderId(lenderId);
    }

    /**
     * Aggregate loans by lender, calculating the total remaining amount, total interest, and total penalty.
     *
     * @return LoanAggregateDTO containing aggregated loan information by lender.
     */
    public LoanAggregateDTO aggregateLoansByLender() {
        logger.info("Aggregating loans by lender.");
        List<Loan> loans = loanRepository.findAll();
        Map<String, List<Loan>> loansByLender = loans.stream()
                .collect(Collectors.groupingBy(Loan::getLenderId));
        LoanAggregateDTO aggregateDTO = createAggregateDTO("Lender", loansByLender);
        logger.info("Aggregated loans by lender: {}", aggregateDTO);
        return aggregateDTO;
    }

    /**
     * Aggregate loans by customer, calculating the total remaining amount, total interest, and total penalty.
     *
     * @return LoanAggregateDTO containing aggregated loan information by customer.
     */
    public LoanAggregateDTO aggregateLoansByCustomerId() {
        logger.info("Aggregating loans by customer.");
        List<Loan> loans = loanRepository.findAll();
        Map<String, List<Loan>> loansByCustomer = loans.stream()
                .collect(Collectors.groupingBy(Loan::getCustomerId));
        LoanAggregateDTO aggregateDTO = createAggregateDTO("Customer", loansByCustomer);
        logger.info("Aggregated loans by customer: {}", aggregateDTO);
        return aggregateDTO;
    }

    /**
     * Aggregate loans by interest rate, calculating the total remaining amount, total interest, and total penalty.
     *
     * @return LoanAggregateDTO containing aggregated loan information by interest rate.
     */
    public LoanAggregateDTO aggregateLoansByInterest() {
        logger.info("Aggregating loans by interest.");
        List<Loan> loans = loanRepository.findAll();
        Map<Double, List<Loan>> loansByInterest = loans.stream()
                .collect(Collectors.groupingBy(Loan::getInterestPerDay));
        LoanAggregateDTO aggregateDTO = createAggregateDTO("Interest", loansByInterest);
        logger.info("Aggregated loans by interest: {}", aggregateDTO);
        return aggregateDTO;
    }

    private LoanAggregateDTO createAggregateDTO(String groupBy, Map<?, List<Loan>> loansByGroup) {
        LoanAggregateDTO aggregateDTO = new LoanAggregateDTO();
        aggregateDTO.setGroupBy(groupBy);

        double totalRemainingAmount = 0.0;
        double totalInterest = 0.0;
        double totalPenalty = 0.0;

        for (List<Loan> loans : loansByGroup.values()) {
            for (Loan loan : loans) {
                totalRemainingAmount += loan.getRemainingAmount();
                totalInterest += (loan.getInterestPerDay() * loan.getAmount());
                totalPenalty += (loan.getPenaltyPerDay() * loan.getAmount());
            }
        }

        aggregateDTO.setTotalRemainingAmount(totalRemainingAmount);
        aggregateDTO.setTotalInterest(totalInterest);
        aggregateDTO.setTotalPenalty(totalPenalty);

        return aggregateDTO;
    }
}
