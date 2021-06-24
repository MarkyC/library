package name.marcocirillo.library.auth.userdetails;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JwtAuthenticationToken(AccountPrincipal principal, String token) {
        super(principal, token);
    }

    public AccountPrincipal getAccountPrincipal() {
        return (AccountPrincipal) this.getPrincipal();
    }

    public String getJwt() {
        return (String) this.getCredentials();
    }
}
