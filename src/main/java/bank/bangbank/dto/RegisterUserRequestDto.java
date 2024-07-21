package bank.bangbank.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequestDto {
    private String userId;
    private String username;
    private String password;
    private String email;
}