package bank.bangbank.repository;

import bank.bangbank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserNumber(Long userNumber);
    Optional<Account> findByAccountNumberStr(String accountNumberStr);
}