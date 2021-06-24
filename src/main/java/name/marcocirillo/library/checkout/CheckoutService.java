package name.marcocirillo.library.checkout;

import name.marcocirillo.library.auth.mapper.AccountUserDetailsToAccountDtoMapper;
import name.marcocirillo.library.auth.userdetails.AccountUserDetails;
import name.marcocirillo.library.auth.userdetails.AccountUserDetailsService;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.BookCheckoutResponseDto;
import name.marcocirillo.library.checkout.mapper.BookCheckoutRequestDtosToCheckoutsMapper;
import name.marcocirillo.library.checkout.mapper.CheckoutsToBookCheckoutResponseDtosMapper;
import name.marcocirillo.library.checkout.validator.CheckoutValidators;
import name.marcocirillo.library.notification.NotificationService;
import name.marcocirillo.library.notification.factory.CheckoutCreatedNotificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    /**
     * The Checkout will be due after this Duration.
     *
     * Hardcoded here for now: it could be made a config (env var/properties file),
     * but it's also possible certain types of items have different Checkout Durations (ie: best sellers: 7 days, etc).
     * At that point you might just put the Checkout Duration in the DB with the Book
     */
    public static final Duration CHECKOUT_DURATION = Duration.of(14, ChronoUnit.DAYS);

    private final AccountUserDetailsService accountUserDetailsService;
    private final AccountUserDetailsToAccountDtoMapper accountDtoMapper;
    private final BookCheckoutRequestDtosToCheckoutsMapper bookCheckoutRequestDtosToCheckoutsMapper;
    private final CheckoutCreatedNotificationFactory checkoutCreatedNotificationFactory;
    private final CheckoutRepository checkoutRepository;
    private final CheckoutsToBookCheckoutResponseDtosMapper checkoutResponseDtosMapper;
    private final CheckoutValidators checkoutValidators;
    private final Clock clock;
    private final NotificationService notificationService;

    @Autowired
    public CheckoutService(
            AccountUserDetailsService accountUserDetailsService,
            AccountUserDetailsToAccountDtoMapper accountDtoMapper,
            BookCheckoutRequestDtosToCheckoutsMapper bookCheckoutRequestDtosToCheckoutsMapper,
            CheckoutCreatedNotificationFactory checkoutCreatedNotificationFactory,
            CheckoutRepository checkoutRepository,
            CheckoutsToBookCheckoutResponseDtosMapper checkoutResponseDtosMapper,
            CheckoutValidators checkoutValidators,
            Clock clock,
            NotificationService notificationService
    ) {
        this.accountUserDetailsService = accountUserDetailsService;
        this.accountDtoMapper = accountDtoMapper;
        this.bookCheckoutRequestDtosToCheckoutsMapper = bookCheckoutRequestDtosToCheckoutsMapper;
        this.checkoutCreatedNotificationFactory = checkoutCreatedNotificationFactory;
        this.checkoutRepository = checkoutRepository;
        this.checkoutResponseDtosMapper = checkoutResponseDtosMapper;
        this.checkoutValidators = checkoutValidators;
        this.clock = clock;
        this.notificationService = notificationService;
    }

    public Collection<BookCheckoutResponseDto> checkout(Collection<BookCheckoutRequestDto> bookDtos) {
        checkoutValidators.maxCheckoutsValidator().validate(bookDtos);
        checkoutValidators.inventoryValidator().validate(bookDtos);

        OffsetDateTime due = OffsetDateTime.now(clock)
                .plus(CHECKOUT_DURATION)
                // truncating to hours saves us from UTC <-> Timezone bugs
                // if we truncate to days, checkouts at 11pm EST would get an extra day of checkout time
                // since 2021-01-01T23:00-0500 = 2021-01-02T04:00Z
                .truncatedTo(ChronoUnit.HOURS);

        AccountUserDetails accountUserDetails = accountUserDetailsService.getUserDetails();

        Collection<Checkout> checkouts = checkoutRepository.saveAll(bookCheckoutRequestDtosToCheckoutsMapper
                .apply(bookDtos, accountUserDetails.getId(), due));

        Collection<BookCheckoutResponseDto> bookCheckoutResponseDtos = checkoutResponseDtosMapper.apply(checkouts);

        // this is probably better done with aspects (would save 3 class dependencies)
        notificationService.sendNotification(checkoutCreatedNotificationFactory.create(
                accountDtoMapper.apply(accountUserDetails),
                bookCheckoutResponseDtos.stream()
                        .map(BookCheckoutResponseDto::getBookId)
                        .collect(Collectors.toSet()))
        );

        return bookCheckoutResponseDtos;

    }
}
