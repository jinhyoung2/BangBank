package bank.bangbank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequestDto {
    private Long userNumber;
    private String accountType;
}
