package name.marcocirillo.library.account;

import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.account.dto.ImmutableAccountDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountToAccountDtoMapper implements Function<Account, AccountDto> {
    @Override
    public AccountDto apply(Account account) {
        return ImmutableAccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .build();
    }
}
