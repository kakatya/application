package ru.kakatya.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;
import ru.kakatya.application.exceptions.PrescoringException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationServiceTest {

    @Test()
    void getLoanOffers() {
        ApplicationService applicationService = new ApplicationService();
        DealServiceFeignClient dealServiceFeignClient = mock(DealServiceFeignClient.class);
        ReflectionTestUtils.setField(applicationService, "dealServiceFeignClient", dealServiceFeignClient);
        ResponseEntity<List<LoanOfferDTO>> listResponseEntity = ResponseEntity.ok(new ArrayList<>());
        when(dealServiceFeignClient.calculationLoanTerms(any())).thenReturn(listResponseEntity);

        Assertions.assertTrue(applicationService.getLoanOffers(createLoanApplicationRequestDTO()).isEmpty());

        Assertions.assertThrows(PrescoringException.class, () -> {
            applicationService.getLoanOffers(createLoanApplicationRequestDTOanCorrect());
        });
    }
    @Test
    void offerSelection() {
        ApplicationService applicationService = new ApplicationService();
        DealServiceFeignClient dealServiceFeignClient = mock(DealServiceFeignClient.class);
        ReflectionTestUtils.setField(applicationService, "dealServiceFeignClient", dealServiceFeignClient);
        when(dealServiceFeignClient.chooseOffer(any())).thenReturn(ResponseEntity.ok().build());
        applicationService.offerSelection(createLoanOfferDto());
        verify(dealServiceFeignClient,times(1)).chooseOffer(any());
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

    private LoanApplicationRequestDTO createLoanApplicationRequestDTOanCorrect() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LoanApplicationRequestDTO loanApplicationRequestDTO;
        try {
            File file = new File("src/test/resources/loanApplicationRequestDTOanCorrect.json");
            loanApplicationRequestDTO = objectMapper.readValue(file, LoanApplicationRequestDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loanApplicationRequestDTO;
    }



}