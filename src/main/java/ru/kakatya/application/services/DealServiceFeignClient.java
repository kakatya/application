package ru.kakatya.application.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "${service.name}", url = "${service.url}")
public interface DealServiceFeignClient {
    @PostMapping("/application")
    ResponseEntity<List<LoanOfferDTO>> calculationLoanTerms(@RequestBody LoanApplicationRequestDTO dto);

    @PutMapping("/offer")
    ResponseEntity<Void> chooseOffer(@RequestBody LoanOfferDTO loanOfferDTO);
}
