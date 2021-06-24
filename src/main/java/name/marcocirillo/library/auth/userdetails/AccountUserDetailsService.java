package name.marcocirillo.library.auth.userdetails;

import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AccountUserDetailsService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * @return  The currently logged in Account, as a UserDetails (does not hit the DB)
     * @see #getCurrentAccount() for a similar method that does hit the DB to pull the account
     */
    public AccountUserDetails getUserDetails() {
        return (AccountUserDetails) getAuthentication().getPrincipal();
    }

    /**
     * @return  The currently logged in Account, pulled from the DB
     * @see #getUserDetails() for a similar method that doesn't pull the whole Account from the DB
     */
    @Transactional
    public Account getCurrentAccount() {
        return accountRepository.getById(getUserDetails().getId());
    }
}
