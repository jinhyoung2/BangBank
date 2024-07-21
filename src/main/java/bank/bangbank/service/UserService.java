package bank.bangbank.service;

import bank.bangbank.dto.LoginRequestDto;
import bank.bangbank.dto.RegisterUserRequestDto;
import bank.bangbank.dto.UserDto;
import bank.bangbank.entity.User;
import bank.bangbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(RegisterUserRequestDto registerUserRequestDto) {
        if (userRepository.findByUserId(registerUserRequestDto.getUserId()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 Id입니다.");
        }

        if (userRepository.findByEmail(registerUserRequestDto.getEmail()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 email입니다.");
        }

        String encodedPassword = passwordEncoder.encode(registerUserRequestDto.getPassword());

        User user = User.builder()
                .userId(registerUserRequestDto.getUserId())
                .username(registerUserRequestDto.getUsername())
                .password(encodedPassword)
                .email(registerUserRequestDto.getEmail())
                .build();

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getUserNumber(), savedUser.getUserId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public boolean authenticateUser(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserId(loginRequestDto.getUserId());
        return user != null && passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
    }
}