package name.marcocirillo.library.auth;

import name.marcocirillo.library.account.dto.SimpleAccountDto;
import name.marcocirillo.library.auth.mapper.LoginApiRequestToAccountDtoMapper;
import name.marcocirillo.library.auth.model.LoginApiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * A simple controller to facilitate getting a JWT to make requests to this app with.
 *
 * In the real world™, you'd have a separate auth service and this API would only need to validate JWTs.
 * We asssume that's outside the scope of this project.
 */
@RestController
public class AuthController {
    private final LoginApiRequestToAccountDtoMapper loginApiRequestToAccountDtoMapper;
    private final AuthService authService;

    @Autowired
    public AuthController(
            LoginApiRequestToAccountDtoMapper loginApiRequestToAccountDtoMapper,
            AuthService authService
    ) {
        this.loginApiRequestToAccountDtoMapper = loginApiRequestToAccountDtoMapper;
        this.authService = authService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginApiRequest request) {
        SimpleAccountDto account = loginApiRequestToAccountDtoMapper.apply(request);
        String jwt = authService.createJwt(account);
        return ResponseEntity.ok(jwt);
    }

    /**
     * A simple method to test auth
     */
    @RequestMapping(path = "/auth", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> auth() {
        return ResponseEntity.ok(Map.of("auth", "OK!"));
    }
}
