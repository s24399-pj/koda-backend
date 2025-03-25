package pl.pjwstk.kodabackend.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.pjwstk.kodabackend.security.token.model.Token;
import pl.pjwstk.kodabackend.security.token.model.TokenType;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    @Value("${koda.security.jwt.secret-key}")
    private String secretKey;

    @Value("${koda.security.jwt.access-expiration:1209600000}")
    private long ACCESS_TOKEN_EXPIRATION;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        AppUser user = (AppUser) userDetails;

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);

        String jwtToken = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        Token token = Token.builder()
                .tokenValue(jwtToken)
                .tokenType(TokenType.BEARER)
                .appUser(user)
                .build();

        tokenRepository.save(token);

        return jwtToken;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        // Check if token exists in DB
        var tokenOptional = tokenRepository.findByTokenValue(token);
        if (tokenOptional.isEmpty()) {
            return false;
        }

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllByAppUserId(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }

        // Usuwamy wszystkie istniejące tokeny
        tokenRepository.deleteAll(validUserTokens);
    }
}