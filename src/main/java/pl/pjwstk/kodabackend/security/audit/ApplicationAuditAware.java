package pl.pjwstk.kodabackend.security.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides current user information for JPA auditing.
 * <p>
 * Automatically populates audit fields (createdBy, modifiedBy) in entities
 * with the ID of the currently authenticated user.
 * </p>
 */
public class ApplicationAuditAware implements AuditorAware<UUID> {

    /**
     * Retrieves the ID of the currently authenticated user for auditing purposes.
     * <p>
     * Returns empty if:
     * <ul>
     *   <li>No authentication context exists</li>
     *   <li>User is not authenticated</li>
     *   <li>User is anonymous</li>
     * </ul>
     *
     * @return Optional containing the current user's ID, or empty if not authenticated
     */
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        AppUser userPrincipal = (AppUser) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}