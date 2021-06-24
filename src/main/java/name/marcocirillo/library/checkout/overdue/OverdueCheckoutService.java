package name.marcocirillo.library.checkout.overdue;

import name.marcocirillo.library.account.AccountToAccountDtoMapper;
import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.AccountRepository;
import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.checkout.Checkout;
import name.marcocirillo.library.checkout.CheckoutRepository;
import name.marcocirillo.library.notification.NotificationService;
import name.marcocirillo.library.notification.factory.OverdueCheckoutNotificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OverdueCheckoutService {
    private final AccountToAccountDtoMapper accountDtoMapper;
    private final AccountRepository accountRepository;
    private final CheckoutRepository checkoutRepository;
    private final CheckoutsToOverdueCheckoutDtoMapper overdueCheckoutDtosMapper;
    private final Clock clock;
    private final NotificationService notificationService;
    private final OverdueCheckoutNotificationFactory overdueCheckoutNotificationFactory;

    @Autowired
    public OverdueCheckoutService(
            AccountToAccountDtoMapper accountDtoMapper,
            AccountRepository accountRepository,
            CheckoutRepository checkoutRepository,
            CheckoutsToOverdueCheckoutDtoMapper overdueCheckoutDtosMapper,
            Clock clock,
            NotificationService notificationService,
            OverdueCheckoutNotificationFactory overdueCheckoutNotificationFactory
    ) {
        this.accountDtoMapper = accountDtoMapper;
        this.accountRepository = accountRepository;
        this.checkoutRepository = checkoutRepository;
        this.overdueCheckoutDtosMapper = overdueCheckoutDtosMapper;
        this.clock = clock;
        this.notificationService = notificationService;
        this.overdueCheckoutNotificationFactory = overdueCheckoutNotificationFactory;
    }

    @Transactional(readOnly = true)
    public void notifyOverdueCheckouts() {
        Map<Account, List<Checkout>> checkoutsByAccountId = checkoutRepository.getOverdueCheckouts(OffsetDateTime.now(clock))
                .stream().collect(Collectors.groupingBy(
                        checkout -> accountRepository.getById(checkout.getAccountId())));

        for (Map.Entry<Account, List<Checkout>> entry : checkoutsByAccountId.entrySet()) {
            AccountDto account = accountDtoMapper.apply(entry.getKey());
            OverdueCheckoutDto overdueCheckoutDto = overdueCheckoutDtosMapper.apply(entry.getValue());

            notificationService.sendNotification(overdueCheckoutNotificationFactory.create(account, overdueCheckoutDto));
        }
    }
}
