package name.marcocirillo.library.auth.someauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import name.marcocirillo.library.account.db.AccountRepository;
import name.marcocirillo.library.account.dto.SimpleAccountDto;
import name.marcocirillo.library.auth.AuthService;
import name.marcocirillo.library.auth.exception.AuthException;
import name.marcocirillo.library.auth.userdetails.AccountPrincipal;
import name.marcocirillo.library.auth.userdetails.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static name.marcocirillo.library.auth.exception.AuthException.ErrorCodes.INVALID_CREDENTIALS;
import static name.marcocirillo.library.auth.exception.AuthException.ErrorCodes.UNAUTHORIZED;
import static name.marcocirillo.library.auth.exception.AuthException.ErrorCodes.UNKNOWN;

/**
 * An AuthProvider for SomeAuth, the auth service you've never used!
 */
@Service
public class SomeAuthService implements AuthService {
    private static final String ISSUER = "SomeAuth";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("notsosecret");
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM)
            .withIssuer(ISSUER)
            .build();

    private final Clock clock;
    private final AccountRepository accountRepository;

    @Autowired
    public SomeAuthService(Clock clock, AccountRepository accountRepository) {
        this.clock = clock;
        this.accountRepository = accountRepository;
    }

    public String createJwt(SimpleAccountDto accountDto) {
        try {
            Instant now = Instant.now(clock);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(accountDto.getId().toString())
                    .withExpiresAt(Date.from(now.plus(1, ChronoUnit.HOURS)))
                    .withIssuedAt(Date.from(now))
                    .sign(ALGORITHM);
        } catch (JWTCreationException e) {
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new AuthException(UNKNOWN, e);
        }
    }

    public void verifyJwt(String token) {
        decodeJwt(token);
    }

    private DecodedJWT decodeJwt(String token) {
        try {
            return VERIFIER.verify(token);
        } catch (JWTVerificationException e){
            throw new AuthException(UNAUTHORIZED, e);
        }
    }

    @Transactional
    @Override
    public AccountPrincipal getAccountPrincipal(String token) {
        return new AccountPrincipal(accountRepository.getById(UUID.fromString(decodeJwt(token).getSubject())));
    }

    @Transactional
    @Override
    public AccountUserDetails getUserDetails(String email) {
        return accountRepository.findByEmail(email)
                .map(AccountUserDetails::new)
                .orElseThrow(() -> new AuthException(INVALID_CREDENTIALS));
    }


}
