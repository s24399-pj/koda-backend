package pl.pjwstk.kodabackend.security.util;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.security.Principal;
import java.util.UUID;

/**
 * Utility class for extracting user information from authentication context
 */
public class UserUtils {

    /**
     * Extracts user ID from Principal object
     * Handles WebSocket authentication context properly
     *
     * @param principal The authenticated user principal
     * @return UUID of the authenticated user
     * @throws AuthenticationCredentialsNotFoundException if principal is not properly authenticated
     */
    public static UUID extractUserIdFromPrincipal(Principal principal) {
        if (principal instanceof Authentication authentication) {
            if (authentication.getPrincipal() instanceof AppUser appUser) {
                return appUser.getId();
            }
        }
        throw new AuthenticationCredentialsNotFoundException("Cannot extract user ID from principal: " + principal);
    }

    /**
     * Extracts AppUser from Principal object
     *
     * @param principal The authenticated user principal
     * @return AppUser object
     * @throws AuthenticationCredentialsNotFoundException if principal is not properly authenticated
     */
    public static AppUser extractUserFromPrincipal(Principal principal) {
        if (principal instanceof Authentication authentication) {
            if (authentication.getPrincipal() instanceof AppUser appUser) {
                return appUser;
            }
        }
        throw new AuthenticationCredentialsNotFoundException("Cannot extract user from principal: " + principal);
    }
}