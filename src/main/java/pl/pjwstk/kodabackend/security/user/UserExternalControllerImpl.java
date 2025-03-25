package pl.pjwstk.kodabackend.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.security.user.model.AuthenticationResponse;
import pl.pjwstk.kodabackend.security.user.model.CreateUserCommand;
import pl.pjwstk.kodabackend.security.user.model.LoginRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/external/users")
class UserExternalControllerImpl implements UserExternalController {

    private final AppUserService appUserService;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(CreateUserCommand command) {
        appUserService.register(command);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {
        return ResponseEntity.ok(appUserService.login(loginRequest));
    }
}
