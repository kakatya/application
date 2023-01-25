package ru.kakatya.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;
import ru.kakatya.application.exceptions.PrescoringException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {DealServiceFeignClient.class, ApplicationService.class})
class ApplicationServiceTest {
    @Autowired
    private ApplicationService applicationService;
    @MockBean
    private DealServiceFeignClient dealServiceFeignClient;
    private static LoanOfferDTO loanOfferDTO;
    private static LoanApplicationRequestDTO loanApplicationRequestDTO;
    private static LoanApplicationRequestDTO loanApplicationRequestDTOanCorrect;

    @BeforeAll
    public static void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        File loanApplicationRequestDTOfile = new File("src/test/resources/loanApplicationRequestDTO.json");
        File loanOfferDTOfile = new File("src/test/resources/loanOffer.json");
        File loanApplicationRequestDTOanCorrectFile = new File("src/test/resources/loanApplicationRequestDTOanCorrect.json");
        try {
            loanApplicationRequestDTO = objectMapper.readValue(loanApplicationRequestDTOfile, LoanApplicationRequestDTO.class);
            loanOfferDTO = objectMapper.readValue(loanOfferDTOfile, LoanOfferDTO.class);
            loanApplicationRequestDTOanCorrect = objectMapper.readValue(loanApplicationRequestDTOanCorrectFile, LoanApplicationRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test()
    void getLoanOffers() {
        ResponseEntity<List<LoanOfferDTO>> listResponseEntity = ResponseEntity.ok(new ArrayList<>());
        when(dealServiceFeignClient.calculationLoanTerms(any())).thenReturn(listResponseEntity);
        Assertions.assertTrue(applicationService.getLoanOffers(loanApplicationRequestDTO).isEmpty());
        try {
            applicationService.getLoanOffers(loanApplicationRequestDTOanCorrect);
        } catch (PrescoringException e) {
        Assertions.assertEquals("[Client email is incorrect ]",e.getMessage());
        }
    }

    @Test
    void offerSelection() {
        when(dealServiceFeignClient.chooseOffer(any())).thenReturn(ResponseEntity.ok().build());
        applicationService.offerSelection(loanOfferDTO);
        verify(dealServiceFeignClient, times(1)).chooseOffer(any());
    }


}