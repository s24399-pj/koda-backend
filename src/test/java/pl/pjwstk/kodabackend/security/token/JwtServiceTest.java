package pl.pjwstk.kodabackend.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import pl.pjwstk.kodabackend.security.token.model.Token;
import pl.pjwstk.kodabackend.security.token.model.TokenType;
import pl.pjwstk.kodabackend.security.user.persistance.entity.AppUser;
import pl.pjwstk.kodabackend.security.user.persistance.entity.Role;

import java.security.Key;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private JwtService jwtService;

    @Captor
    private ArgumentCaptor<Token> tokenCaptor;

    private final String SECRET_KEY = "testowyKluczTestowyKluczTestowyKluczTestowyKluczTestowyKluczTestowyKlucz";
    private final long ACCESS_TOKEN_EXPIRATION = 3600000;
    private AppUser testUser;
    private String validToken;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "ACCESS_TOKEN_EXPIRATION", ACCESS_TOKEN_EXPIRATION);

        testUser = AppUser.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .role(Role.USER)
                .build();

        validToken = createValidToken(testUser);
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String expected = testUser.getEmail();

        String result = jwtService.extractUsername(validToken);

        assertEquals(expected, result);
    }

    @Test
    void extractClaim_shouldReturnCorrectClaim() {
        String expected = testUser.getEmail();

        String result = jwtService.extractClaim(validToken, Claims::getSubject);

        assertEquals(expected, result);
    }

    @Test
    void generateToken_withoutExtraClaims_shouldCreateValidToken() {
        when(tokenRepository.save(any(Token.class))).thenReturn(new Token());

        String token = jwtService.generateToken(testUser);

        assertNotNull(token);
        verify(tokenRepository).save(tokenCaptor.capture());

        Token capturedToken = tokenCaptor.getValue();
        assertEquals(token, capturedToken.getTokenValue());
        assertEquals(TokenType.BEARER, capturedToken.getTokenType());
        assertEquals(testUser, capturedToken.getAppUser());

        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(testUser.getEmail(), extractedUsername);
    }

    @Test
    void generateToken_withExtraClaims_shouldIncludeExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("customClaim", "customValue");

        when(tokenRepository.save(any(Token.class))).thenReturn(new Token());

        String token = jwtService.generateToken(extraClaims, testUser);

        assertNotNull(token);
        verify(tokenRepository).save(any(Token.class));

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getTestSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("ADMIN", claims.get("role"));
        assertEquals("customValue", claims.get("customClaim"));
    }

    @Test
    void isTokenValid_withValidTokenAndMatchingUser_shouldReturnTrue() {
        Token token = new Token();
        token.setTokenValue(validToken);
        token.setAppUser(testUser);

        when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(token));

        boolean result = jwtService.isTokenValid(validToken, testUser);

        assertTrue(result);
    }

    @Test
    void isTokenValid_withNonExistentToken_shouldReturnFalse() {
        when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.empty());

        boolean result = jwtService.isTokenValid(validToken, testUser);

        assertFalse(result);
    }

    @Test
    void isTokenValid_withDifferentUser_shouldReturnFalse() {
        Token token = new Token();
        token.setTokenValue(validToken);
        token.setAppUser(testUser);

        AppUser differentUser = AppUser.builder()
                .id(UUID.randomUUID())
                .email("different@example.com")
                .password("password")
                .firstName("Different")
                .lastName("User")
                .role(Role.USER)
                .build();

        when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(token));

        boolean result = jwtService.isTokenValid(validToken, differentUser);

        assertFalse(result);
    }


    @Test
    void revokeAllUserTokens_withExistingTokens_shouldDeleteAll() {
        List<Token> userTokens = Arrays.asList(
                new Token(),
                new Token()
        );

        when(tokenRepository.findAllByAppUserId(testUser.getId())).thenReturn(userTokens);

        jwtService.revokeAllUserTokens(testUser);

        verify(tokenRepository).deleteAll(userTokens);
    }

    @Test
    void revokeAllUserTokens_withNoExistingTokens_shouldNotCallDeleteAll() {
        when(tokenRepository.findAllByAppUserId(testUser.getId())).thenReturn(Collections.emptyList());

        jwtService.revokeAllUserTokens(testUser);

        verify(tokenRepository, never()).deleteAll(anyList());
    }

    private String createValidToken(UserDetails userDetails) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);

        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getTestSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getTestSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}