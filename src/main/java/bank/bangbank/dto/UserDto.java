package bank.bangbank.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userNumber;
    private String userId;
    private String username;
    private String email;
}