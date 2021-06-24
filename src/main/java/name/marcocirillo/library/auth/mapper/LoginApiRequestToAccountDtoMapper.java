package name.marcocirillo.library.auth.mapper;

import name.marcocirillo.library.account.dto.SimpleAccountDto;
import name.marcocirillo.library.account.dto.ImmutableSimpleAccountDto;
import name.marcocirillo.library.auth.exception.AuthException;
import name.marcocirillo.library.auth.model.LoginApiRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

import static name.marcocirillo.library.auth.exception.AuthException.ErrorCodes.INVALID_CREDENTIALS;

@Component
public class LoginApiRequestToAccountDtoMapper implements Function<LoginApiRequest, SimpleAccountDto> {
    @Override
    public SimpleAccountDto apply(LoginApiRequest loginApiRequest) {
        try {
            return ImmutableSimpleAccountDto.builder()
                    .id(UUID.fromString(loginApiRequest.getAccountId()))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new AuthException(INVALID_CREDENTIALS, e);
        }
    }
}
