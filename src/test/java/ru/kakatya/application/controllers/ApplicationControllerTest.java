package ru.kakatya.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;
import ru.kakatya.application.services.ApplicationService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationControllerTest {

    @Test
    void getLoanOffers() {
        ApplicationController applicationController = new ApplicationController();
        ApplicationService applicationService = mock(ApplicationService.class);
        ReflectionTestUtils.setField(applicationController, "applicationService", applicationService);
        List<LoanOfferDTO> listResponseEntity = new ArrayList<>();
        when(applicationService.getLoanOffers(any())).thenReturn(ResponseEntity.ok(listResponseEntity).getBody());
        HttpStatus httpStatus = applicationController.getLoanOffers(createLoanApplicationRequestDTO()).getStatusCode();
        verify(applicationService, times(1))
                .getLoanOffers(createLoanApplicationRequestDTO());
        Assertions.assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    void offerSelection() {
        ApplicationController applicationController = new ApplicationController();
        ApplicationService applicationService = mock(ApplicationService.class);
        ReflectionTestUtils.setField(applicationController, "applicationService", applicationService);
        applicationController.offerSelection(createLoanOfferDto());
        verify(applicationService,times(1))
                .offerSelection(createLoanOfferDto());
    }

    private LoanApplicationRequestDTO createLoanApplicationRequestDTO() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LoanApplicationRequestDTO loanApplicationRequestDTO;
        try {
            File file = new File("src/test/resources/loanApplicationRequestDTO.json");
            loanApplicationRequestDTO = objectMapper.readValue(file, LoanApplicationRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loanApplicationRequestDTO;
    }
    private LoanOfferDTO createLoanOfferDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LoanOfferDTO loanOfferDTO;
        try {
            File file = new File("src/test/resources/loanOffer.json");
            loanOfferDTO = objectMapper.readValue(file, LoanOfferDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loanOfferDTO;
    }

}