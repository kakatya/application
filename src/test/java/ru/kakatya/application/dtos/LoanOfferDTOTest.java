package ru.kakatya.application.dtos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class LoanOfferDTOTest {

    @Test
    void setRequestedAmount() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setRequestedAmount(new BigDecimal("200000"));
        Assertions.assertEquals(new BigDecimal("200000"), loanOfferDTO.getRequestedAmount());
    }

    @Test
    void setTerm() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setTerm(20);
        Assertions.assertEquals(20,loanOfferDTO.getTerm());
    }

    @Test
    void setRate() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setRate(new BigDecimal("20"));
        Assertions.assertEquals(new BigDecimal("20"),loanOfferDTO.getRate());
    }
}