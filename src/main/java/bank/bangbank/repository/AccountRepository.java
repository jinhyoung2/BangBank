package bank.bangbank.repository;

import bank.bangbank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//1

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}