package ru.kakatya.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;
import ru.kakatya.application.exceptions.PrescoringException;
import ru.kakatya.application.services.ApplicationService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ApplicationController.class, ApplicationService.class})
class ApplicationControllerTest {
    @MockBean
    private ApplicationService applicationService;
    @Autowired
    private ApplicationController applicationController;
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

    @Test
    void getLoanOffers() {
        List<LoanOfferDTO> listResponseEntity = new ArrayList<>();
        when(applicationService.getLoanOffers(any())).thenReturn(ResponseEntity.ok(listResponseEntity).getBody());
        HttpStatus httpStatus = applicationController.getLoanOffers(loanApplicationRequestDTO).getStatusCode();
        verify(applicationService, times(1))
                .getLoanOffers(loanApplicationRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, httpStatus);
        when(applicationService.getLoanOffers(any())).thenThrow(PrescoringException.class);
        Assertions.assertThrows(PrescoringException.class, () -> {
            applicationController.getLoanOffers(loanApplicationRequestDTOanCorrect);
        });
    }

    @Test
    void offerSelection() {
        applicationController.offerSelection(loanOfferDTO);
        verify(applicationService, times(1))
                .offerSelection(loanOfferDTO);
    }


}