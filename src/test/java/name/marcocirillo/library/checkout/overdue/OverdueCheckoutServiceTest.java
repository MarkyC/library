package name.marcocirillo.library.checkout.overdue;

import name.marcocirillo.library.account.AccountToAccountDtoMapper;
import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.AccountRepository;
import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.BookRepository;
import name.marcocirillo.library.checkout.Checkout;
import name.marcocirillo.library.checkout.CheckoutRepository;
import name.marcocirillo.library.notification.NotificationService;
import name.marcocirillo.library.notification.factory.OverdueCheckoutNotificationFactory;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OverdueCheckoutServiceTest {
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final CheckoutRepository checkoutRepository = mock(CheckoutRepository.class);
    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T10:15:30.00Z"), ZoneId.of("UTC"));
    private final NotificationService notificationService = mock(NotificationService.class);
    private final OverdueCheckoutNotificationFactory notificationFactory = mock(OverdueCheckoutNotificationFactory.class);

    private final OverdueCheckoutService overdueCheckoutService = new OverdueCheckoutService(
            new AccountToAccountDtoMapper(),
            accountRepository,
            checkoutRepository,
            new CheckoutsToOverdueCheckoutDtoMapper(bookRepository),
            clock,
            notificationService,
            notificationFactory
    );

    @Test
    void notifyOverdueCheckouts() {
        int numCheckouts = 6;

        List<Account> accounts = IntStream.range(0, 3)
                .mapToObj(i -> UUID.randomUUID())
                .map(id -> new Account(id, "name", "email@doman"))
                .collect(Collectors.toList());
        List<Book> books = IntStream.range(0, numCheckouts)
                .mapToObj(i -> UUID.randomUUID())
                .map(id -> new Book(id, "title", "isbn", UUID.randomUUID(), true, 10))
                .collect(Collectors.toList());

        List<Checkout> checkouts = new ArrayList<>();
        for (int i = 0; i < numCheckouts; i++) {
            Account account;
            if (i == 0) {
                // Account 1: 1 overdue Checkout
                account = accounts.get(0);
            } else if (i < 3) {
                // Account 2: 2 overdue Checkouts
                account = accounts.get(1);
            } else {
                // Account 3: 3 overdue Checkouts
                account = accounts.get(2);
            }

            Book book = books.get(i);

            checkouts.add(new Checkout(
                    UUID.randomUUID(),
                    book.getId(),
                    account.getId(),
                    false,
                    OffsetDateTime.now(clock).minus(1, ChronoUnit.DAYS))
            );

            when(bookRepository.getById(book.getId())).thenReturn(book);
            when(accountRepository.getById(account.getId())).thenReturn(account);
        }
        when(checkoutRepository.getOverdueCheckouts(OffsetDateTime.now(clock))).thenReturn(checkouts);

        overdueCheckoutService.notifyOverdueCheckouts();

        // only 3 accounts have overdue checkouts
        verify(notificationService, times(3)).sendNotification(any());
    }
}