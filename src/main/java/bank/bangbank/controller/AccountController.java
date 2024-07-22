package bank.bangbank.controller;

import bank.bangbank.dto.AmountRequestDto;
import bank.bangbank.dto.CreateAccountRequestDto;
import bank.bangbank.entity.Account;
import bank.bangbank.entity.TransferHistory;
import bank.bangbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequestDto createAccountRequest) {
        Account createdAccount = accountService.createAccountForUser(createAccountRequest.getUserNumber(), createAccountRequest.getAccountType());
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userNumber}")
    public ResponseEntity<List<Account>> getAccountsByUserNumber(@PathVariable Long userNumber) {
        List<Account> accounts = accountService.getAccountsByUserNumber(userNumber);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable String accountNumber, @RequestBody AmountRequestDto amountRequest) {
        Account account = accountService.deposit(accountNumber, amountRequest.getAmount());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable String accountNumber, @RequestBody AmountRequestDto amountRequest) {
        Account account = accountService.withdraw(accountNumber, amountRequest.getAmount());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/{fromAccountNumber}/transfer/{toAccountNumber}")
    public ResponseEntity<String> transfer(@PathVariable String fromAccountNumber, @PathVariable String toAccountNumber, @RequestBody AmountRequestDto amountRequest) {
        accountService.transfer(fromAccountNumber, toAccountNumber, amountRequest.getAmount());
        return new ResponseEntity<>("송금이 완료 되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable String accountNumber) {
        Double balance = accountService.getBalance(accountNumber);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return new ResponseEntity<>("계좌가 삭제되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}/transfer-history")
    public ResponseEntity<List<TransferHistory>> getTransferHistory(@PathVariable String accountNumber) {
        List<TransferHistory> transferHistories = accountService.getTransferHistory(accountNumber);
        return new ResponseEntity<>(transferHistories, HttpStatus.OK);
    }

    @PostMapping("/apply-interest")
    public ResponseEntity<String> applyInterest() {
        accountService.applyInterest();
        return new ResponseEntity<>("이자가 적용되었습니다.", HttpStatus.OK);
    }
}