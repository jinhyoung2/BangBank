package bank.bangbank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userNumber;
    private Double accountBalance;
    private String accountType;
    private String accountNumberStr;

    private Double interestRate;
    private LocalDateTime lastInterestApplied;
}