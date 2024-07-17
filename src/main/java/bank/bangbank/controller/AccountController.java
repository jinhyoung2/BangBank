package bank.bangbank.controller;

import bank.bangbank.domain.Account;
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

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestParam Long userNumber) {
        Account createdAccount = accountService.createAccountForUser(userNumber);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{userNumber}")
    public ResponseEntity<List<Account>> getAccountsByUserNumber(@PathVariable Long userNumber) {
        List<Account> accounts = accountService.getAccountsByUserNumber(userNumber);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        Account account = accountService.deposit(accountNumber, amount);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestParam String accountNumber, @RequestParam Double amount) {
        Account account = accountService.withdraw(accountNumber, amount);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam Double amount) {
        accountService.transfer(fromAccountNumber, toAccountNumber, amount);
        return new ResponseEntity<>("송금이 완료 됐습니다.", HttpStatus.OK);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Double> getBalance(@PathVariable String accountNumber) {
        Double balance = accountService.getBalance(accountNumber);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return new ResponseEntity<>("계좌가 삭제되었습니다.", HttpStatus.OK);
    }

}
