package bank.bangbank.service;

import bank.bangbank.domain.Account;
import bank.bangbank.domain.AccountStatus;
import bank.bangbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//1

@Service
@RequiredArgsConstructor
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    @Transactional
    public void createAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("계좌번호는 empty 일 수 없습니다");
        }

        logger.info("Creating account with account number: {}", accountNumber);

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountStatus(AccountStatus.IN_USE)
                .build();

        accountRepository.save(account);
    }
}
