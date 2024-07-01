package bank.bangbank.controller;

import bank.bangbank.service.AccountService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestParam @NotBlank String accountNumber) {
        accountService.createAccount(accountNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body("계좌 생성에 성공했습니다.");
    }
}