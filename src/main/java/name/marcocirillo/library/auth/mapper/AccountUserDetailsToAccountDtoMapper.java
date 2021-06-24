package name.marcocirillo.library.auth.mapper;

import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.account.dto.ImmutableAccountDto;
import name.marcocirillo.library.auth.userdetails.AccountUserDetails;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountUserDetailsToAccountDtoMapper implements Function<AccountUserDetails, AccountDto> {
    @Override
    public AccountDto apply(AccountUserDetails accountUserDetails) {
        return ImmutableAccountDto.builder()
                .id(accountUserDetails.getId())
                .name(accountUserDetails.getName())
                .email(accountUserDetails.getEmail())
                .build();
    }
}
