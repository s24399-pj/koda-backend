package pl.pjwstk.kodabackend.security.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pjwstk.kodabackend.exception.EntityConflictException;
import pl.pjwstk.kodabackend.security.token.JwtService;
import pl.pjwstk.kodabackend.security.user.model.AuthenticationResponse;
import pl.pjwstk.kodabackend.security.user.model.CreateUserCommand;
import pl.pjwstk.kodabackend.security.user.model.LoginRequest;
import pl.pjwstk.kodabackend.security.user.model.UserMiniDto;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.entity.Role;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AppUserService appUserService;

    private CreateUserCommand createUserCommand;
    private AppUser testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        createUserCommand = CreateUserCommand.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+48123456789")
                .build();

        testUser = AppUser.builder()
                .id(userId)
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+48123456789")
                .enabled(true)
                .build();
    }

    @Test
    void register_WhenEmailNotTaken_CreatesNewUser() {
        // given
        when(appUserRepository.findByEmail(createUserCommand.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createUserCommand.getPassword())).thenReturn("encodedPassword");

        // when
        appUserService.register(createUserCommand);

        // then
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).saveAndFlush(userCaptor.capture());

        AppUser savedUser = userCaptor.getValue();
        assertEquals(createUserCommand.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());
        assertEquals(createUserCommand.getFirstName(), savedUser.getFirstName());
        assertEquals(createUserCommand.getLastName(), savedUser.getLastName());
        assertEquals(createUserCommand.getPhoneNumber(), savedUser.getPhoneNumber());
        assertTrue(savedUser.isEnabled());
    }

    @Test
    void register_WhenEmailAlreadyExists_ThrowsEntityConflictException() {
        // given
        when(appUserRepository.findByEmail(createUserCommand.getEmail())).thenReturn(Optional.of(testUser));

        // when & then
        EntityConflictException exception = assertThrows(
                EntityConflictException.class,
                () -> appUserService.register(createUserCommand)
        );

        assertEquals("User with provided email already exists!", exception.getMessage());
        verify(appUserRepository, never()).saveAndFlush(any());
    }

    @Test
    void login_WhenCredentialsAreValid_ReturnsAuthenticationResponse() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        String jwtToken = "jwt.token.here";

        when(appUserRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn(jwtToken);

        // when
        AuthenticationResponse response = appUserService.login(loginRequest);

        // then
        assertNotNull(response);
        assertEquals(jwtToken, response.getAccessToken());
        verify(jwtService).revokeAllUserTokens(testUser);
        verify(jwtService).generateToken(testUser);
    }

    @Test
    void login_WhenEmailNotFound_ThrowsBadCredentialsException() {
        // given
        LoginRequest loginRequest = new LoginRequest("wrong@example.com", "password123");
        when(appUserRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> appUserService.login(loginRequest)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void login_WhenPasswordIsIncorrect_ThrowsBadCredentialsException() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPassword");

        when(appUserRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).thenReturn(false);

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> appUserService.login(loginRequest)
        );

        assertEquals("Invalid email or password", exception.getMessage());
        verifyNoInteractions(jwtService);
    }

    @Test
    void getUserByEmail_WhenUserExists_ReturnsUser() {
        // given
        when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // when
        AppUser result = appUserService.getUserByEmail("test@example.com");

        // then
        assertEquals(testUser, result);
        verify(appUserRepository).findByEmail("test@example.com");
    }

    @Test
    void getUserByEmail_WhenUserNotFound_ThrowsBadCredentialsException() {
        // given
        when(appUserRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> appUserService.getUserByEmail("notfound@example.com")
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getUserById_WhenUserExists_ReturnsUser() {
        // given
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // when
        AppUser result = appUserService.getUserById(userId);

        // then
        assertEquals(testUser, result);
        verify(appUserRepository).findById(userId);
    }

    @Test
    void getUserById_WhenUserNotFound_ThrowsBadCredentialsException() {
        // given
        UUID notFoundId = UUID.randomUUID();
        when(appUserRepository.findById(notFoundId)).thenReturn(Optional.empty());

        // when & then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> appUserService.getUserById(notFoundId)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void searchUsers_WhenUsersFound_ReturnsUserMiniDtos() {
        // given
        AppUser user1 = AppUser.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .build();

        AppUser user2 = AppUser.builder()
                .id(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Smith")
                .build();

        List<AppUser> users = Arrays.asList(user1, user2);
        when(appUserRepository.searchUsers("john")).thenReturn(users);

        // when
        List<UserMiniDto> result = appUserService.searchUsers("john");

        // then
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals("Jane Smith", result.get(1).getFullName());
    }

    @Test
    void searchUsers_WhenNoUsersFound_ReturnsEmptyList() {
        // given
        when(appUserRepository.searchUsers("xyz")).thenReturn(Collections.emptyList());

        // when
        List<UserMiniDto> result = appUserService.searchUsers("xyz");

        // then
        assertTrue(result.isEmpty());
        verify(appUserRepository).searchUsers("xyz");
    }
}