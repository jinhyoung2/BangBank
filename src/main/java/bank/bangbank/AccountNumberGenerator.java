package bank.bangbank;

import java.security.SecureRandom;

public class AccountNumberGenerator {
    private static final int ACCOUNT_NUMBER_LENGTH = 14;
    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder(ACCOUNT_NUMBER_LENGTH);
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }
}