package bank.bangbank.service;

import bank.bangbank.AccountNumberGenerator;
import bank.bangbank.domain.Account;
import bank.bangbank.domain.User;
import bank.bangbank.repository.AccountRepository;
import bank.bangbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccountForUser(Long userNumber) {
        User user = userRepository.findById(userNumber)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 유저 번호입니다."));

        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.findByAccountNumberStr(accountNumber).isPresent());

        Account account = Account.builder()
                .userNumber(userNumber)
                .accountBalance(0.0)
                .accountType("SAVINGS")
                .accountNumberStr(accountNumber)
                .build();

        return accountRepository.save(account);
    }

    public List<Account> getAccountsByUserNumber(Long userNumber) {
        return accountRepository.findByUserNumber(userNumber);
    }

    public Account deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumberStr(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
        account.setAccountBalance(account.getAccountBalance() + amount);
        return accountRepository.save(account);
    }

    public Account withdraw(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumberStr(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
        if (account.getAccountBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        account.setAccountBalance(account.getAccountBalance() - amount);
        return accountRepository.save(account);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, Double amount) {
        Account fromAccount = accountRepository.findByAccountNumberStr(fromAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("보내시는 분의 계좌를 찾을 수 없습니다"));
        Account toAccount = accountRepository.findByAccountNumberStr(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("받는 분의 계좌를 찾을 수 없습니다"));

        if (fromAccount.getAccountBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        fromAccount.setAccountBalance(fromAccount.getAccountBalance() - amount);
        toAccount.setAccountBalance(toAccount.getAccountBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public Double getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumberStr(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
        return account.getAccountBalance();
    }
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumberStr(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
        accountRepository.delete(account);
    }
}
