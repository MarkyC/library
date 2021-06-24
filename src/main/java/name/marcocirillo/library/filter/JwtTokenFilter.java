package name.marcocirillo.library.filter;

import name.marcocirillo.library.auth.AuthService;
import name.marcocirillo.library.auth.userdetails.AccountPrincipal;
import name.marcocirillo.library.auth.userdetails.AccountUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final AccountUserDetailsService accountUserDetailsService;

    @Autowired
    public JwtTokenFilter(AuthService authService, AccountUserDetailsService accountUserDetailsService) {
        this.authService = authService;
        this.accountUserDetailsService = accountUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        // Get Account and save it to the spring security context
        // We can fetch this principal later with
        AccountPrincipal principal = authService.getAccountPrincipal(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, token);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//        accountUserDetailsService.setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
