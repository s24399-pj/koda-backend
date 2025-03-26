package pl.pjwstk.kodabackend.security.user.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
}
