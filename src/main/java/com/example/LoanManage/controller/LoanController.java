package com.example.LoanManage.controller;

import com.example.LoanManage.dto.LoanAggregateDTO;
import com.example.LoanManage.dto.LoanDTO;
import com.example.LoanManage.entity.Loan;
import com.example.LoanManage.exception.ResourceNotFoundException;
import com.example.LoanManage.mapper.LoanMapper;
import com.example.LoanManage.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final Logger logger = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Retrieve a list of all loans.
     *
     * @return ResponseEntity containing a list of LoanDTOs representing all loans.
     */
    @GetMapping("/all")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        logger.info("Received request to retrieve all loans.");
        List<LoanDTO> loans = loanService.getAllLoans()
                .stream()
                .map(LoanMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} loans.", loans.size());
        return ResponseEntity.ok(loans);
    }

    /**
     * Create a new loan entry.
     *
     * @param loanDTO The LoanDTO representing the loan to be created.
     * @return ResponseEntity containing the created LoanDTO.
     */
    @PostMapping("/add")
    public ResponseEntity<LoanDTO> addLoan(@RequestBody LoanDTO loanDTO) {
        logger.info("Received request to create a new loan: {}", loanDTO);
        Loan loan = LoanMapper.toEntity(loanDTO);
        Loan savedLoan = loanService.addLoan(loan);
        logger.info("Created loan with ID: {}", savedLoan.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(LoanMapper.toDTO(savedLoan));
    }

    /**
     * Get Load Details by loan Id
     *
     * @param loanId
     * @return
     */
    @GetMapping("/{loanId}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable String loanId) {
        logger.info("Received request to retrieve loan details by ID: {}", loanId);
        Optional<Loan> loan = loanService.getLoanByLoanId(loanId);

        if (loan.isPresent()) {
            logger.info("Retrieved loan details for ID {}: {}", loanId, loan.get());
            return ResponseEntity.ok(LoanMapper.toDTO(loan.get()));
        } else {
            logger.warn("Loan with ID {} not found.", loanId);
            throw new ResourceNotFoundException("Loan with Loan ID " + loanId + " not found");
        }
    }

    /**
     * Retrieve loan details for loans associated with a specific customer.
     *
     * @param customerId The Customer ID for which loan details are requested.
     * @return ResponseEntity containing a list of LoanDTOs representing loans associated with the specified customer.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanDTO>> getLoansByCustomerId(@PathVariable String customerId) {
        logger.info("Received request to retrieve loans for customer with ID: {}", customerId);
        List<LoanDTO> loans = loanService.getLoansByCustomerId(customerId)
                .stream()
                .map(LoanMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} loans for customer with ID: {}", loans.size(), customerId);
        return ResponseEntity.ok(loans);
    }

    /**
     * Retrieve loan details for loans associated with a specific lender.
     *
     * @param lenderId The Lender ID for which loan details are requested.
     * @return ResponseEntity containing a list of LoanDTOs representing loans associated with the specified lender.
     */
    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<List<LoanDTO>> getLoansByLenderId(@PathVariable String lenderId) {
        logger.info("Received request to retrieve loans for lender with ID: {}", lenderId);
        List<LoanDTO> loans = loanService.getLoansByLenderId(lenderId)
                .stream()
                .map(LoanMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} loans for lender with ID: {}", loans.size(), lenderId);
        return ResponseEntity.ok(loans);
    }

    /**
     * Retrieves and aggregates loans by lender, calculating the total remaining amount, total interest, and total penalty.
     *
     * @return A ResponseEntity containing a LoanAggregateDTO with aggregated loan information by lender.
     */
    @GetMapping("/aggregate/lender")
    public ResponseEntity<LoanAggregateDTO> aggregateLoansByLender() {
        logger.info("Received request to aggregate loans by lender.");
        LoanAggregateDTO result = loanService.aggregateLoansByLender();
        logger.info("Aggregated loans by lender: {}", result);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves and aggregates loans by customer, calculating the total remaining amount, total interest, and total penalty.
     *
     * @return A ResponseEntity containing a LoanAggregateDTO with aggregated loan information by customer.
     */
    @GetMapping("/aggregate/customer")
    public ResponseEntity<LoanAggregateDTO> aggregateLoansByCustomerId() {
        logger.info("Received request to aggregate loans by customer.");
        LoanAggregateDTO result = loanService.aggregateLoansByCustomerId();
        logger.info("Aggregated loans by customer: {}", result);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves and aggregates loans by interest rate, calculating the total remaining amount, total interest, and total penalty.
     *
     * @return A ResponseEntity containing a LoanAggregateDTO with aggregated loan information by interest rate.
     */
    @GetMapping("/aggregate/interest")
    public ResponseEntity<LoanAggregateDTO> aggregateLoansByInterest() {
        logger.info("Received request to aggregate loans by interest.");
        LoanAggregateDTO result = loanService.aggregateLoansByInterest();
        logger.info("Aggregated loans by interest: {}", result);
        return ResponseEntity.ok(result);
    }
}
