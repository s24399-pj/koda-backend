package pl.pjwstk.kodabackend.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.security.user.model.AppUserDto;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/users")
@PreAuthorize("hasRole('USER')")
class UserInternalControllerImpl implements UserInternalController {

    private final AppUserService appUserService;

    @Override
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/profile")
    public AppUserDto getUserDetails(Principal principal) {
        String email = principal.getName();
        AppUser user = appUserService.getUserByEmail(email);

        return AppUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(Optional.ofNullable(user.getProfilePicture()).orElse(new byte[0]))
                .build();
    }
}
