package name.marcocirillo.library.checkout;

import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.TestAccountRepository;
import name.marcocirillo.library.author.Author;
import name.marcocirillo.library.author.TestAuthorRepository;
import name.marcocirillo.library.base.BaseIntegrationTest;
import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.TestBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private Clock clock;

    @Autowired
    private TestAccountRepository testAccountRepository;

    @Autowired
    private TestAuthorRepository testAuthorRepository;

    @Autowired
    private TestBookRepository testBookRepository;

    @Autowired
    private TestCheckoutRepository testCheckoutRepository;

    @TestConfiguration
    static class TestConfig {
        /**
         * Mock the Clock for this test.
         * This solves a small bug where if the system clock is changed during a test, it /could/ fail
         */
        @Bean
        public Clock clock() {
            return Clock.fixed(Instant.parse("2021-01-01T10:15:30.00Z"), ZoneId.of("UTC"));
        }
    }

    @Test
    void getOverdueCheckouts() {
        OffsetDateTime now = OffsetDateTime.now(clock);

        Account account = testAccountRepository.createAccount();
        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author);

        Checkout overdueCheckout = testCheckoutRepository.createCheckout(
                account, book, false, now.minus(1, ChronoUnit.MILLIS));
        Checkout returnedCheckout = testCheckoutRepository.createCheckout(
                account, book, true, now.minus(1, ChronoUnit.MILLIS));
        Checkout notDueCheckout = testCheckoutRepository.createCheckout(
                account, book, true, now.plus(1, ChronoUnit.MILLIS));

        Collection<Checkout> checkouts = checkoutRepository.getOverdueCheckouts(now);

        assertTrue(checkouts.contains(overdueCheckout), "Contains the overdue Checkout");
        assertFalse(checkouts.contains(returnedCheckout), "Does not contain the Checkout that was returned");
        assertFalse(checkouts.contains(notDueCheckout), "Does not contain the Checkout that isn't due yet");
    }
}