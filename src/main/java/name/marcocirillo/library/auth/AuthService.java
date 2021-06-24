package name.marcocirillo.library.auth;

import name.marcocirillo.library.account.dto.SimpleAccountDto;
import name.marcocirillo.library.auth.userdetails.AccountPrincipal;
import name.marcocirillo.library.auth.userdetails.AccountUserDetails;

public interface AuthService {
    String createJwt(SimpleAccountDto accountDto);
    void verifyJwt(String token);
    AccountPrincipal getAccountPrincipal(String token);
    AccountUserDetails getUserDetails(String username);
}
