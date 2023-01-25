package ru.kakatya.application.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;
import ru.kakatya.application.services.ApplicationService;

import java.util.List;

@RestController
@Api("Работа с заявками")
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    @ApiOperation("Расчет кредитных предложений")
    public ResponseEntity<List<LoanOfferDTO>> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return ResponseEntity.ok().body(applicationService.getLoanOffers(loanApplicationRequestDTO));
    }

    @PostMapping("/offer")
    @ApiOperation("Утверждение кредитного предложения")
    public ResponseEntity<Void> offerSelection(@RequestBody LoanOfferDTO loanOfferDTO) {
        applicationService.offerSelection(loanOfferDTO);
        return ResponseEntity.ok().build();
    }
}
