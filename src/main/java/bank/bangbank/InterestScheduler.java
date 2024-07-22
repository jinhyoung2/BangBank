package bank.bangbank;

import bank.bangbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class InterestScheduler {

    private final AccountService accountService;

    @Autowired
    public InterestScheduler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void applyInterest() {
        accountService.applyInterest();
    }
}
