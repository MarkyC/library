package name.marcocirillo.library.filter;

import name.marcocirillo.library.auth.AuthService;
import name.marcocirillo.library.auth.exception.AuthException;
import name.marcocirillo.library.auth.userdetails.AccountPrincipal;
import name.marcocirillo.library.auth.userdetails.JwtAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static name.marcocirillo.library.auth.exception.AuthException.ErrorCodes.INVALID_CREDENTIALS;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    private final static String TOKEN_HEADER = "Authorization";

    private final AuthService authService;

    public JwtAuthenticationTokenFilter(
            AuthService authService,
            RequestMatcher requestMatcher
    ) {
        super(requestMatcher);
        this.authService = authService;
        setAuthenticationSuccessHandler((request, response, authentication) -> {});
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            throw new AuthException(INVALID_CREDENTIALS);
        }

        final String token = header.split(" ")[1].trim();
        // Get Account and save it to the spring security context
        // We can fetch this principal later with
        AccountPrincipal principal = authService.getAccountPrincipal(token);

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(principal, token);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response); // continue with the rest of the filters
    }
}
