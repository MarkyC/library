package name.marcocirillo.library.util;

import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.TestAccountRepository;
import name.marcocirillo.library.account.dto.ImmutableSimpleAccountDto;
import name.marcocirillo.library.auth.AuthService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestAuthUtils {
    @Autowired
    private AuthService authService;

    @Autowired
    private TestAccountRepository testAccountRepository;

    public Pair<Account, String> createAccountAndJwt() {
        Account account = testAccountRepository.createAccount();
        String jwt = authService.createJwt(ImmutableSimpleAccountDto.builder()
                .id(account.getId())
                .build());
        return Pair.of(account, jwt);
    }
}
