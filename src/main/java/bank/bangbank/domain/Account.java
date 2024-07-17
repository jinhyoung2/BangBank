package bank.bangbank.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_number")
    private Long userNumber;

    @Column(name = "account_balance")
    private Double accountBalance;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_number_str", unique = true, length = 14)
    private String accountNumberStr;
}
