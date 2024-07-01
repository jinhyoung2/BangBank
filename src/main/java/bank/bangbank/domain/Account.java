package bank.bangbank.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//1
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}