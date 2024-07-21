package bank.bangbank.controller;

import bank.bangbank.dto.LoginRequestDto;
import bank.bangbank.dto.LoginResponseDto;
import bank.bangbank.dto.RegisterUserRequestDto;
import bank.bangbank.dto.UserDto;
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
    public UserDto registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto) {
        return userService.registerUser(registerUserRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        boolean isAuthenticated = userService.authenticateUser(loginRequestDto);
        if (isAuthenticated) {
            return new LoginResponseDto("로그인 성공");
        } else {
            return new LoginResponseDto("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }
    }
}