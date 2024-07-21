package bank.bangbank;

import bank.bangbank.controller.AccountController;
import bank.bangbank.dto.AmountRequestDto;
import bank.bangbank.dto.CreateAccountRequestDto;
import bank.bangbank.entity.Account;
import bank.bangbank.entity.TransferHistory;
import bank.bangbank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount() {
        Account account = new Account();
        when(accountService.createAccountForUser(anyLong())).thenReturn(account);

        CreateAccountRequestDto request = new CreateAccountRequestDto();
        request.setUserNumber(1L);

        ResponseEntity<Account> response = accountController.createAccount(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account, response.getBody());
        verify(accountService, times(1)).createAccountForUser(anyLong());
    }

    @Test
    void getAccountsByUserNumber() {
        Account account = new Account();
        List<Account> accounts = Collections.singletonList(account);
        when(accountService.getAccountsByUserNumber(anyLong())).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAccountsByUserNumber(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        verify(accountService, times(1)).getAccountsByUserNumber(anyLong());
    }

    @Test
    void deposit() {
        Account account = new Account();
        when(accountService.deposit(anyString(), anyDouble())).thenReturn(account);

        AmountRequestDto request = new AmountRequestDto();
        request.setAmount(100.0);

        ResponseEntity<Account> response = accountController.deposit("1234567890", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
        verify(accountService, times(1)).deposit(anyString(), anyDouble());
    }

    @Test
    void withdraw() {
        Account account = new Account();
        when(accountService.withdraw(anyString(), anyDouble())).thenReturn(account);

        AmountRequestDto request = new AmountRequestDto();
        request.setAmount(50.0);

        ResponseEntity<Account> response = accountController.withdraw("1234567890", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
        verify(accountService, times(1)).withdraw(anyString(), anyDouble());
    }

    @Test
    void transfer() {
        doNothing().when(accountService).transfer(anyString(), anyString(), anyDouble());

        AmountRequestDto request = new AmountRequestDto();
        request.setAmount(50.0);

        ResponseEntity<String> response = accountController.transfer("1234567890", "0987654321", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("송금이 완료 되었습니다.", response.getBody());
        verify(accountService, times(1)).transfer(anyString(), anyString(), anyDouble());
    }

    @Test
    void getBalance() {
        when(accountService.getBalance(anyString())).thenReturn(100.0);

        ResponseEntity<Double> response = accountController.getBalance("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody());
        verify(accountService, times(1)).getBalance(anyString());
    }

    @Test
    void deleteAccount() {
        doNothing().when(accountService).deleteAccount(anyString());

        ResponseEntity<String> response = accountController.deleteAccount("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("계좌가 삭제되었습니다.", response.getBody());
        verify(accountService, times(1)).deleteAccount(anyString());
    }

    @Test
    void createAccount_withInvalidUserNumber() {
        when(accountService.createAccountForUser(anyLong()))
                .thenThrow(new IllegalArgumentException("잘못된 유저 번호입니다."));

        CreateAccountRequestDto request = new CreateAccountRequestDto();
        request.setUserNumber(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.createAccount(request);
        });

        assertEquals("잘못된 유저 번호입니다.", exception.getMessage());
        verify(accountService, times(1)).createAccountForUser(anyLong());
    }

    @Test
    void deposit_withInvalidAccountNumber() {
        when(accountService.deposit(anyString(), anyDouble()))
                .thenThrow(new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        AmountRequestDto request = new AmountRequestDto();
        request.setAmount(100.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.deposit("invalid", request);
        });

        assertEquals("계좌를 찾을 수 없습니다.", exception.getMessage());
        verify(accountService, times(1)).deposit(anyString(), anyDouble());
    }

    @Test
    void withdraw_withInsufficientBalance() {
        when(accountService.withdraw(anyString(), anyDouble()))
                .thenThrow(new IllegalArgumentException("잔액이 부족합니다."));

        AmountRequestDto request = new AmountRequestDto();
        request.setAmount(1000.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.withdraw("1234567890", request);
        });

        assertEquals("잔액이 부족합니다.", exception.getMessage());
        verify(accountService, times(1)).withdraw(anyString(), anyDouble());
    }

    @Test
    void transfer_withInsufficientBalance() {
        doThrow(new IllegalArgumentException("잔액이 부족합니다."))
                .when(accountService).transfer(anyString(), anyString(), anyDouble());

        AmountRequestDto request = new AmountRequestDto();
        request.setAmount(1000.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.transfer("1234567890", "0987654321", request);
        });

        assertEquals("잔액이 부족합니다.", exception.getMessage());
        verify(accountService, times(1)).transfer(anyString(), anyString(), anyDouble());
    }

    @Test
    void deleteAccount_withInvalidAccountNumber() {
        doThrow(new IllegalArgumentException("계좌를 찾을 수 없습니다."))
                .when(accountService).deleteAccount(anyString());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountController.deleteAccount("invalid");
        });

        assertEquals("계좌를 찾을 수 없습니다.", exception.getMessage());
        verify(accountService, times(1)).deleteAccount(anyString());
    }

    @Test
    void getTransferHistory() {
        TransferHistory history = new TransferHistory();
        List<TransferHistory> histories = Collections.singletonList(history);
        when(accountService.getTransferHistory(anyString())).thenReturn(histories);

        ResponseEntity<List<TransferHistory>> response = accountController.getTransferHistory("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(histories, response.getBody());
        verify(accountService, times(1)).getTransferHistory(anyString());
    }
}