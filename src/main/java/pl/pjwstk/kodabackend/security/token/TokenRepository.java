package pl.pjwstk.kodabackend.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjwstk.kodabackend.security.token.model.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token> findAllByAppUserId(UUID id);
    Optional<Token> findByTokenValue(String tokenValue);
}
