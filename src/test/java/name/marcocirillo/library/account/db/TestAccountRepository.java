package name.marcocirillo.library.account.db;

import name.marcocirillo.library.fakedata.FakeDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestAccountRepository {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FakeDataGenerator fakeDataGenerator;

    public Account createAccount() {
        return fakeDataGenerator.generate(1).map(fakeData -> new Account(
                UUID.randomUUID(),
                fakeData.getAccountName(),
                fakeData.getAccountEmail()
        ))
                .map(accountRepository::save)
                .findFirst().orElseThrow();
    }
}