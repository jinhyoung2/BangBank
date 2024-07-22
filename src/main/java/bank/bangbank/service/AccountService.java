package bank.bangbank.service;

import bank.bangbank.AccountNumberGenerator;
import bank.bangbank.entity.Account;
import bank.bangbank.entity.TransferHistory;
import bank.bangbank.entity.User;
import bank.bangbank.repository.AccountRepository;
import bank.bangbank.repository.TransferHistoryRepository;
import bank.bangbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransferHistoryRepository transferHistoryRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransferHistoryRepository transferHistoryRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transferHistoryRepository = transferHistoryRepository;
    }

    public Account createAccountForUser(Long userNumber, String accountType) {
        User user = userRepository.findById(userNumber)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 유저 번호입니다."));

        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.findByAccountNumberStr(accountNumber).isPresent());

        Account account = Account.builder()
                .userNumber(userNumber)
                .accountBalance(0.0)
                .accountType(accountType)
                .accountNumberStr(accountNumber)
                .build();

        if (accountType.equals("DEPOSIT")) {
            account.setInterestRate(0.05);
            account.setLastInterestApplied(LocalDateTime.now());
        }

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

        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setFromAccountNumber(fromAccountNumber);
        transferHistory.setToAccountNumber(toAccountNumber);
        transferHistory.setAmount(amount);
        transferHistory.setTimestamp(LocalDateTime.now());
        transferHistoryRepository.save(transferHistory);
    }

    public List<TransferHistory> getTransferHistory(String accountNumber) {
        return transferHistoryRepository.findByFromAccountNumberOrToAccountNumber(accountNumber, accountNumber);
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

    public void applyInterest() {
        List<Account> depositAccounts = accountRepository.findByAccountType("DEPOSIT");
        for (Account account : depositAccounts) {
            if (account.getInterestRate() != null && account.getLastInterestApplied() != null) {
                long months = ChronoUnit.MONTHS.between(account.getLastInterestApplied(), LocalDateTime.now());
                if (months > 0) {
                    double newBalance = account.getAccountBalance() * Math.pow(1 + account.getInterestRate(), months);
                    account.setAccountBalance(newBalance);
                    account.setLastInterestApplied(account.getLastInterestApplied().plusMonths(months));
                    accountRepository.save(account);
                }
            }
        }
    }
}