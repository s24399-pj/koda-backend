package pl.pjwstk.kodabackend.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.security.user.model.AppUserDto;
import pl.pjwstk.kodabackend.security.user.model.UserMiniDto;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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

    @Override
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/{userId}/profile")
    public ResponseEntity<AppUserDto> getUserProfileById(@PathVariable UUID userId) {
        try {
            AppUser user = appUserService.getUserById(userId);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            AppUserDto userDto = AppUserDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .profilePicture(Optional.ofNullable(user.getProfilePicture()).orElse(new byte[0]))
                    .build();

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            log.error("Error fetching user profile for ID: " + userId + ", error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/search")
    public ResponseEntity<List<UserMiniDto>> searchUsers(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<UserMiniDto> users = appUserService.searchUsers(query.trim());
        return ResponseEntity.ok(users);
    }

}
