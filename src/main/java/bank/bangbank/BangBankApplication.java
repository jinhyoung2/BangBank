package bank.bangbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

public class BangBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BangBankApplication.class, args);
	}

}
