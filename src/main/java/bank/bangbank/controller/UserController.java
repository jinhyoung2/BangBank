package bank.bangbank.controller;

import bank.bangbank.domain.LoginRequest;
import bank.bangbank.domain.User;
import bank.bangbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        if (userService.authenticateUser(loginRequest)) {
            return "로그인 성공";
        } else {
            return "로그인 실패: 아이디 또는 비밀번호를 확인하세요.";
        }
    }
}