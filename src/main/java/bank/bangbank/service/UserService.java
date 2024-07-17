package bank.bangbank.service;

import bank.bangbank.domain.LoginRequest;
import bank.bangbank.domain.User;
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

    public User registerUser(User user) {
        if (userRepository.findByUserId(user.getUserId()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 userId입니다.");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 email입니다.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    //로그인
    public boolean authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUserId());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return true;
        }
        return false;
    }
}