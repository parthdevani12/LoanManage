package com.example.LoanManage.service;

import com.example.LoanManage.dto.LoanAggregateDTO;
import com.example.LoanManage.entity.Loan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository mockLoanRepository;

    private LoanService loanServiceUnderTest;

    @Before
    public void setUp() {
        loanServiceUnderTest = new LoanService(mockLoanRepository);
    }

    @Test
    public void testGetAllLoans() {
        // Setup
        final Loan loan = createSampleLoan();
        when(mockLoanRepository.findAll()).thenReturn(Collections.singletonList(loan));

        // Run the test
        final List<Loan> result = loanServiceUnderTest.getAllLoans();

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(loan, result.get(0));
    }

    @Test
    public void testGetAllLoans_LoanRepositoryReturnsNoItems() {
        // Setup
        when(mockLoanRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Loan> result = loanServiceUnderTest.getAllLoans();

        // Verify the results
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAddLoan() {
        // Setup
        final Loan loan = createSampleLoan();
        when(mockLoanRepository.save(any(Loan.class))).thenReturn(loan);

        // Run the test
        final Loan result = loanServiceUnderTest.addLoan(loan);

        // Verify the results
        assertEquals(loan, result);
    }

    @Test
    public void testGetLoanByLoanId() {
        // Setup
        final Loan loan = createSampleLoan();
        when(mockLoanRepository.findByLoanId("loanId")).thenReturn(Optional.of(loan));

        // Run the test
        final Optional<Loan> result = loanServiceUnderTest.getLoanByLoanId("loanId");

        // Verify the results
        assertTrue(result.isPresent());
        assertEquals(loan, result.get());
    }

    @Test
    public void testGetLoanByLoanId_LoanRepositoryReturnsAbsent() {
        // Setup
        when(mockLoanRepository.findByLoanId("loanId")).thenReturn(Optional.empty());

        // Run the test
        final Optional<Loan> result = loanServiceUnderTest.getLoanByLoanId("loanId");

        // Verify the results
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetLoansByCustomerId() {
        // Setup
        final Loan loan = createSampleLoan();
        when(mockLoanRepository.findByCustomerId("customerId")).thenReturn(Collections.singletonList(loan));

        // Run the test
        final List<Loan> result = loanServiceUnderTest.getLoansByCustomerId("customerId");

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(loan, result.get(0));
    }

    @Test
    public void testGetLoansByCustomerId_LoanRepositoryReturnsNoItems() {
        // Setup
        when(mockLoanRepository.findByCustomerId("customerId")).thenReturn(Collections.emptyList());

        // Run the test
        final List<Loan> result = loanServiceUnderTest.getLoansByCustomerId("customerId");

        // Verify the results
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLoansByLenderId() {
        // Setup
        final Loan loan = createSampleLoan();
        when(mockLoanRepository.findByLenderId("lenderId")).thenReturn(Collections.singletonList(loan));

        // Run the test
        final List<Loan> result = loanServiceUnderTest.getLoansByLenderId("lenderId");

        // Verify the results
        assertEquals(1, result.size());
        assertEquals(loan, result.get(0));
    }

    @Test
    public void testGetLoansByLenderId_LoanRepositoryReturnsNoItems() {
        // Setup
        when(mockLoanRepository.findByLenderId("lenderId")).thenReturn(Collections.emptyList());

        // Run the test
        final List<Loan> result = loanServiceUnderTest.getLoansByLenderId("lenderId");

        // Verify the results
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAggregateLoansByLender() {
        // Setup
        final Loan loan = createSampleLoan();
        when(mockLoanRepository.findAll()).thenReturn(Collections.singletonList(loan));

        // Run the test
        final LoanAggregateDTO result = loanServiceUnderTest.aggregateLoansByLender();

        // Verify the results
        assertNotNull(result);
        assertEquals("Lender", result.getGroupBy());
        assertEquals(10000.0, result.getTotalRemainingAmount(), 0.001);
        assertEquals(100.0, result.getTotalInterest(), 0.001);
        assertEquals(100.0, result.getTotalPenalty(), 0.001);
    }

    @Test
    public void testAggregateLoansByLender_LoanRepositoryReturnsNoItems() {
        // Setup
        when(mockLoanRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final LoanAggregateDTO result = loanServiceUnderTest.aggregateLoansByLender();

        // Verify the results
        assertNotNull(result);
        assertEquals("Lender", result.getGroupBy());
        assertEquals(0.0, result.getTotalRemainingAmount(), 0.001);
        assertEquals(0.0, result.getTotalInterest(), 0.001);
        assertEquals(0.0, result.getTotalPenalty(), 0.001);
    }

    // Create a sample Loan for testing
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
}
