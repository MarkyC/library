package name.marcocirillo.library.checkout;

import name.marcocirillo.library.auth.mapper.AccountUserDetailsToAccountDtoMapper;
import name.marcocirillo.library.auth.userdetails.AccountUserDetails;
import name.marcocirillo.library.auth.userdetails.AccountUserDetailsService;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.BookCheckoutResponseDto;
import name.marcocirillo.library.checkout.dto.ImmutableBookCheckoutRequestDto;
import name.marcocirillo.library.checkout.mapper.BookCheckoutRequestDtosToCheckoutsMapper;
import name.marcocirillo.library.checkout.mapper.CheckoutsToBookCheckoutResponseDtosMapper;
import name.marcocirillo.library.checkout.validator.CheckoutValidators;
import name.marcocirillo.library.checkout.validator.InventoryValidator;
import name.marcocirillo.library.checkout.validator.MaxCheckoutsValidator;
import name.marcocirillo.library.notification.NotificationService;
import name.marcocirillo.library.notification.factory.CheckoutCreatedNotificationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckoutServiceTest {
    private final AccountUserDetailsService accountUserDetailsService = mock(AccountUserDetailsService.class);
    private final CheckoutValidators checkoutValidators = new CheckoutValidators(
            mock(InventoryValidator.class),
            mock(MaxCheckoutsValidator.class)
    );
    private final CheckoutRepository checkoutRepository = mock(CheckoutRepository.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    private final CheckoutService checkoutService = new CheckoutService(
            accountUserDetailsService,
            new AccountUserDetailsToAccountDtoMapper(),
            new BookCheckoutRequestDtosToCheckoutsMapper(),
            mock(CheckoutCreatedNotificationFactory.class),
            checkoutRepository,
            new CheckoutsToBookCheckoutResponseDtosMapper(),
            checkoutValidators,
            Clock.fixed(Instant.parse("2021-01-01T10:15:30.00Z"), ZoneId.of("UTC")),
            notificationService
    );

    @BeforeEach
    void setUp() {
        // the input of the repository.save*() method becomes the output
        when(checkoutRepository.saveAll(any())).thenAnswer((Answer<List<Checkout>>) invocationOnMock -> invocationOnMock.getArgument(0));

        // mock account
        when(accountUserDetailsService.getUserDetails())
                .thenReturn(new AccountUserDetails(UUID.randomUUID(), "name", "email@domain.com"));
    }

    @Test
    void checkout() {
        int quantity = 2;
        BookCheckoutRequestDto book = createBook(quantity);
        Collection<BookCheckoutResponseDto> result = checkoutService.checkout(Collections.singleton(book));

        assertEquals(quantity, result.size(), "Multiple Book quantities creats multiple Checkouts");

        OffsetDateTime expectedDueDate = OffsetDateTime.of(2021, 1, 15, 10, 0, 0, 0, ZoneOffset.UTC);
        for (BookCheckoutResponseDto checkout : result) {
            assertEquals(book.getId(), checkout.getBookId(), "Correct Book was checked out");
            assertEquals(expectedDueDate, checkout.getDue(), "Due date is 14 days later, truncated to the hour");
        }

        // a notification is sent
        verify(notificationService, times(1)).sendNotification(any());
    }

    private static BookCheckoutRequestDto createBook(int quantity) {
        return ImmutableBookCheckoutRequestDto.builder()
                .id(UUID.randomUUID())
                .quantity(quantity)
                .build();
    }
}