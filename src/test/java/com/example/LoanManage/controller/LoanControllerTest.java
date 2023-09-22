package com.example.LoanManage.controller;

import com.example.LoanManage.dto.LoanAggregateDTO;
import com.example.LoanManage.dto.LoanDTO;
import com.example.LoanManage.entity.Loan;
import com.example.LoanManage.exception.ResourceNotFoundException;
import com.example.LoanManage.service.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {

    @Mock
    private LoanService mockLoanService;

    private LoanController loanControllerUnderTest;

    @Before
    public void setUp() {
        loanControllerUnderTest = new LoanController(mockLoanService);
    }

    @Test
    public void testGetAllLoans() {
        Loan loan = createSampleLoan();
        final List<Loan> loans = List.of(loan);
        when(mockLoanService.getAllLoans()).thenReturn(loans);

        // Run the test
        final ResponseEntity<List<LoanDTO>> result = loanControllerUnderTest.getAllLoans();

        // Verify the results
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        assertEquals(loan.getLoanId(), result.getBody().get(0).getLoanId());
    }

    @Test
    public void testGetAllLoans_LoanServiceReturnsNoItems() {
        // Setup
        when(mockLoanService.getAllLoans()).thenReturn(Collections.emptyList());

        // Run the test
        final ResponseEntity<List<LoanDTO>> result = loanControllerUnderTest.getAllLoans();

        // Verify the results
        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
    }

    @Test
    public void testAddLoan() {
        // Mock data
        LoanDTO loanDTO = createSampleLoanDTO();
        Loan loan = createSampleLoan();
        when(mockLoanService.addLoan(any(Loan.class))).thenReturn(loan);

        // Run the test
        final ResponseEntity<LoanDTO> result = loanControllerUnderTest.addLoan(loanDTO);

        // Verify the results
        assertEquals(loanDTO.getLoanId(), Objects.requireNonNull(result.getBody()).getLoanId());
    }

    @Test
    public void testGetLoanById() {
        // Mock data
        Loan loan = createSampleLoan();
        Optional<Loan> loanOptional = Optional.of(loan);
        when(mockLoanService.getLoanByLoanId("loanId")).thenReturn(loanOptional);

        // Run the test
        final ResponseEntity<LoanDTO> result = loanControllerUnderTest.getLoanById("loanId");

        // Verify the results
        assertEquals(loan.getLoanId(), Objects.requireNonNull(result.getBody()).getLoanId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetLoanById_LoanServiceReturnsAbsent() {
        // Setup
        when(mockLoanService.getLoanByLoanId("loanId")).thenReturn(Optional.empty());

        // Run the test
        loanControllerUnderTest.getLoanById("loanId");
    }

    @Test
    public void testGetLoansByCustomerId() {
        // Mock data
        Loan loan = createSampleLoan();
        final List<Loan> loans = List.of(loan);
        when(mockLoanService.getLoansByCustomerId("customerId")).thenReturn(loans);

        // Run the test
        final ResponseEntity<List<LoanDTO>> result = loanControllerUnderTest.getLoansByCustomerId("customerId");

        // Verify the results
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        assertEquals(loan.getLoanId(), result.getBody().get(0).getLoanId());
    }

    @Test
    public void testGetLoansByCustomerId_LoanServiceReturnsNoItems() {
        // Setup
        when(mockLoanService.getLoansByCustomerId("customerId")).thenReturn(Collections.emptyList());

        // Run the test
        final ResponseEntity<List<LoanDTO>> result = loanControllerUnderTest.getLoansByCustomerId("customerId");

        // Verify the results
        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
    }

    @Test
    public void testGetLoansByLenderId() {
        // Mock data
        Loan loan = createSampleLoan();
        final List<Loan> loans = List.of(loan);
        when(mockLoanService.getLoansByLenderId("lenderId")).thenReturn(loans);

        // Run the test
        final ResponseEntity<List<LoanDTO>> result = loanControllerUnderTest.getLoansByLenderId("lenderId");

        // Verify the results
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        assertEquals(loan.getLoanId(), result.getBody().get(0).getLoanId());
    }

    @Test
    public void testGetLoansByLenderId_LoanServiceReturnsNoItems() {
        // Setup
        when(mockLoanService.getLoansByLenderId("lenderId")).thenReturn(Collections.emptyList());

        // Run the test
        final ResponseEntity<List<LoanDTO>> result = loanControllerUnderTest.getLoansByLenderId("lenderId");

        // Verify the results
        assertEquals(ResponseEntity.ok(Collections.emptyList()), result);
    }

    @Test
    public void testAggregateLoansByLender() {
        // Mock data
        LoanAggregateDTO loanAggregateDTO = createSampleLoanAggregateDTO("Lender");
        when(mockLoanService.aggregateLoansByLender()).thenReturn(loanAggregateDTO);

        // Run the test
        final ResponseEntity<LoanAggregateDTO> result = loanControllerUnderTest.aggregateLoansByLender();
        double delta = 0.0001;

        // Verify the results
        assertEquals(loanAggregateDTO.getGroupBy(), Objects.requireNonNull(result.getBody()).getGroupBy());
        assertEquals(loanAggregateDTO.getTotalRemainingAmount(), result.getBody().getTotalRemainingAmount(), delta);
        assertEquals(loanAggregateDTO.getTotalInterest(), result.getBody().getTotalInterest(), delta);
        assertEquals(loanAggregateDTO.getTotalPenalty(), result.getBody().getTotalPenalty(), delta);
    }

    @Test
    public void testAggregateLoansByCustomerId() {
        // Mock data
        LoanAggregateDTO loanAggregateDTO = createSampleLoanAggregateDTO("Customer");
        when(mockLoanService.aggregateLoansByCustomerId()).thenReturn(loanAggregateDTO);

        // Run the test
        final ResponseEntity<LoanAggregateDTO> result = loanControllerUnderTest.aggregateLoansByCustomerId();
        double delta = 0.0001;

        // Verify the results
        assertEquals(loanAggregateDTO.getGroupBy(), Objects.requireNonNull(result.getBody()).getGroupBy());
        assertEquals(loanAggregateDTO.getTotalRemainingAmount(), result.getBody().getTotalRemainingAmount(), delta);
        assertEquals(loanAggregateDTO.getTotalInterest(), result.getBody().getTotalInterest(), delta);
        assertEquals(loanAggregateDTO.getTotalPenalty(), result.getBody().getTotalPenalty(), delta);
    }

    @Test
    public void testAggregateLoansByInterest() {
        // Mock data
        LoanAggregateDTO loanAggregateDTO = createSampleLoanAggregateDTO("Interest");
        when(mockLoanService.aggregateLoansByInterest()).thenReturn(loanAggregateDTO);

        // Run the test
        final ResponseEntity<LoanAggregateDTO> result = loanControllerUnderTest.aggregateLoansByInterest();

        // Verify the results
        assertEquals(loanAggregateDTO.getGroupBy(), Objects.requireNonNull(result.getBody()).getGroupBy());
        assertEquals(loanAggregateDTO.getTotalRemainingAmount(), result.getBody().getTotalRemainingAmount(), 0.001); // Adjust delta as needed
        assertEquals(loanAggregateDTO.getTotalInterest(), result.getBody().getTotalInterest(), 0.001); // Adjust delta as needed
        assertEquals(loanAggregateDTO.getTotalPenalty(), result.getBody().getTotalPenalty(), 0.001);
    }

    private Loan createSampleLoan() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanId("L1");
        loan.setCustomerId("C1");
        loan.setLenderId("LEN1");
        loan.setAmount(10000.0);
        loan.setRemainingAmount(10000.0);
        loan.setPaymentDate(new java.sql.Date(1234567890123L)); // Replace with your desired date in milliseconds
        loan.setInterestPerDay(0.01);
        loan.setDueDate(new java.sql.Date(1234567890123L)); // Replace with your desired date in milliseconds
        loan.setPenaltyPerDay(0.01);
        loan.setCancelled(false);
        return loan;
    }

    private LoanDTO createSampleLoanDTO() {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId("L1");
        loanDTO.setCustomerId("C1");
        loanDTO.setLenderId("LEN1");
        loanDTO.setAmount(10000.0);
        loanDTO.setRemainingAmount(10000.0);
        loanDTO.setPaymentDate(new java.sql.Date(1234567890123L));
        loanDTO.setInterestPerDay(0.01);
        loanDTO.setDueDate(new java.sql.Date(1234567890123L));
        loanDTO.setPenaltyPerDay(0.01);
        loanDTO.setCancelled(false);
        return loanDTO;
    }

    private LoanAggregateDTO createSampleLoanAggregateDTO(String groupBy) {
        LoanAggregateDTO loanAggregateDTO = new LoanAggregateDTO();
        loanAggregateDTO.setGroupBy(groupBy);
        loanAggregateDTO.setTotalRemainingAmount(1000.0);
        loanAggregateDTO.setTotalInterest(500.0);
        loanAggregateDTO.setTotalPenalty(200.0);
        return loanAggregateDTO;
    }
}

