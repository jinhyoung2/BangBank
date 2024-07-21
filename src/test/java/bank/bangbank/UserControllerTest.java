package bank.bangbank;

import bank.bangbank.controller.UserController;
import bank.bangbank.dto.LoginRequestDto;
import bank.bangbank.dto.LoginResponseDto;
import bank.bangbank.dto.RegisterUserRequestDto;
import bank.bangbank.dto.UserDto;
import bank.bangbank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto("testUser", "testPassword", "testEmail", "testUserId");
        UserDto userDto = new UserDto(1L, "testUserId", "testUser", "testEmail");

        when(userService.registerUser(any(RegisterUserRequestDto.class))).thenReturn(userDto);

        UserDto response = userController.registerUser(requestDto);

        assertEquals(userDto, response);
        verify(userService, times(1)).registerUser(any(RegisterUserRequestDto.class));
    }

    @Test
    void registerUser_duplicateUserId() {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto("testUser", "testPassword", "testEmail", "testUserId");

        when(userService.registerUser(any(RegisterUserRequestDto.class)))
                .thenThrow(new IllegalArgumentException("이미 사용 중인 Id입니다."));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.registerUser(requestDto);
        });

        assertEquals("이미 사용 중인 Id입니다.", exception.getMessage());
        verify(userService, times(1)).registerUser(any(RegisterUserRequestDto.class));
    }

    @Test
    void registerUser_duplicateEmail() {
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto("testUser", "testPassword", "testEmail", "testUserId");

        when(userService.registerUser(any(RegisterUserRequestDto.class)))
                .thenThrow(new IllegalArgumentException("이미 사용 중인 email입니다."));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.registerUser(requestDto);
        });

        assertEquals("이미 사용 중인 email입니다.", exception.getMessage());
        verify(userService, times(1)).registerUser(any(RegisterUserRequestDto.class));
    }

    @Test
    void loginUser_success() {
        LoginRequestDto requestDto = new LoginRequestDto("testUserId", "testPassword");
        LoginResponseDto responseDto = new LoginResponseDto("로그인 성공");

        when(userService.authenticateUser(any(LoginRequestDto.class))).thenReturn(true);

        LoginResponseDto response = userController.loginUser(requestDto);

        assertEquals(responseDto.getMessage(), response.getMessage());
        verify(userService, times(1)).authenticateUser(any(LoginRequestDto.class));
    }

    @Test
    void loginUser_failure() {
        LoginRequestDto requestDto = new LoginRequestDto("testUserId", "wrongPassword");
        LoginResponseDto responseDto = new LoginResponseDto("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");

        when(userService.authenticateUser(any(LoginRequestDto.class))).thenReturn(false);

        LoginResponseDto response = userController.loginUser(requestDto);

        assertEquals(responseDto.getMessage(), response.getMessage());
        verify(userService, times(1)).authenticateUser(any(LoginRequestDto.class));
    }
}