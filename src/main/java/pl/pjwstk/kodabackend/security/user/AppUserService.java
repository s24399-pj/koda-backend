package pl.pjwstk.kodabackend.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pjwstk.kodabackend.exception.EntityConflictException;
import pl.pjwstk.kodabackend.security.token.JwtService;
import pl.pjwstk.kodabackend.security.user.model.AuthenticationResponse;
import pl.pjwstk.kodabackend.security.user.model.CreateUserCommand;
import pl.pjwstk.kodabackend.security.user.model.LoginRequest;
import pl.pjwstk.kodabackend.security.user.model.UserMiniDto;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.entity.Role;
import pl.pjwstk.kodabackend.security.user.persistance.repository.AppUserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(CreateUserCommand command) {
        if (appUserRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new EntityConflictException("User with provided email already exists!");
        }

        AppUser user = AppUser.builder()
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .role(Role.USER)
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .phoneNumber(command.getPhoneNumber())
                .enabled(true)
                .build();

        appUserRepository.saveAndFlush(user);
    }


    public AuthenticationResponse login(LoginRequest loginRequest) {
        AppUser user = appUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        jwtService.revokeAllUserTokens(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .build();
    }

    @Transactional(readOnly = true)
    public AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Transactional(readOnly = true)
    public AppUser getUserById(UUID userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<UserMiniDto> searchUsers(String query) {
        List<AppUser> users = appUserRepository.searchUsers(query);

        return users.stream()
                .map(user -> UserMiniDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .fullName(user.getFirstName() + " " + user.getLastName())
                        .build())
                .toList();
    }

}
