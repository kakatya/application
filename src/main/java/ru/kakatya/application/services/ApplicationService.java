package ru.kakatya.application.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kakatya.application.dtos.LoanApplicationRequestDTO;
import ru.kakatya.application.dtos.LoanOfferDTO;
import ru.kakatya.application.exceptions.PrescoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ApplicationService {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationService.class);
    @Autowired
    private DealServiceFeignClient dealServiceFeignClient;

    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PrescoringException {
        prescoring(loanApplicationRequestDTO);
        LOGGER.info("Create loan offers");
        return dealServiceFeignClient.calculationLoanTerms(loanApplicationRequestDTO).getBody();
    }

    public void offerSelection(LoanOfferDTO loanOfferDTO) {
        LOGGER.info("Loan offer selection");
        dealServiceFeignClient.chooseOffer(loanOfferDTO);
    }

    private void prescoring(LoanApplicationRequestDTO loanApplicationRequestDTO) throws PrescoringException {
        LOGGER.info("Prescoring");
        boolean result = true;
        List<String> message = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String emailPattern = "[\\w\\.]{2,50}@[\\w\\.]{2,20}";
        String clientNamePattern = "^[A-Z][a-z]{1,29}";
        String passportNumberPattern = "^\\d{6}$";
        String passportSeriesPattern = "^\\d{4}$";
        Pattern email = Pattern.compile(emailPattern);
        Pattern name = Pattern.compile(clientNamePattern);
        Pattern passportNumber = Pattern.compile(passportNumberPattern);
        Pattern passportSeries = Pattern.compile(passportSeriesPattern);

        if (!name.matcher(loanApplicationRequestDTO.getFirstName()).matches() &&
                !name.matcher(loanApplicationRequestDTO.getLastName()).matches()) {
            result = false;
            message.add("Client name is incorrect ");
        }
        if (loanApplicationRequestDTO.getMiddleName() != null &&
                !name.matcher(loanApplicationRequestDTO.getMiddleName()).matches()) {
            result = false;
            message.add("Client middle name is incorrect ");
        }


        if (!email.matcher(loanApplicationRequestDTO.getEmail()).matches()) {
            result = false;
            message.add("Client email is incorrect ");
        }


        if (ChronoUnit.YEARS.between(loanApplicationRequestDTO.getBirthdate(), today) < 18) {
            result = false;
            message.add("Client does not pass by age ");
        }

        if (!passportSeries.matcher(loanApplicationRequestDTO.getPassportSeries()).matches() &&
                !passportNumber.matcher(loanApplicationRequestDTO.getPassportNumber()).matches()) {
            result = false;
            message.add("Passport's date is incorrect ");
        }
        if (loanApplicationRequestDTO.getAmount().compareTo(new BigDecimal("10000")) < 0) {
            result = false;
            message.add("Request amount is incorrect ");
        }

        if (loanApplicationRequestDTO.getTerm() < 6) {
            result = false;
            message.add("Term is incorrect");
        }
        if (!result) {
            LOGGER.error(message);
            throw new PrescoringException(message.toString());
        }
    }
}
