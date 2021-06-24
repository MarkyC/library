package name.marcocirillo.library.book.favourite;

import name.marcocirillo.library.account.AccountToAccountDtoMapper;
import name.marcocirillo.library.account.db.AccountRepository;
import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.notification.NotificationService;
import name.marcocirillo.library.notification.factory.FavouriteBooksNotificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class FavouriteBookService {
    private final AccountToAccountDtoMapper accountDtoMapper;
    private final AccountRepository accountRepository;
    private final FavouriteBooksNotificationFactory favouriteBooksNotificationFactory;
    private final FavouriteBookRepository favouriteBookRepository;
    private final FavouriteBooksToFavouriteBookDtoMapper favouriteBookDtoMapper;
    private final NotificationService notificationService;

    @Autowired
    public FavouriteBookService(
            AccountToAccountDtoMapper accountDtoMapper,
            AccountRepository accountRepository,
            FavouriteBooksNotificationFactory favouriteBooksNotificationFactory,
            FavouriteBookRepository favouriteBookRepository,
            FavouriteBooksToFavouriteBookDtoMapper favouriteBookDtoMapper,
            NotificationService notificationService
    ) {
        this.accountDtoMapper = accountDtoMapper;
        this.accountRepository = accountRepository;
        this.favouriteBooksNotificationFactory = favouriteBooksNotificationFactory;
        this.favouriteBookRepository = favouriteBookRepository;
        this.favouriteBookDtoMapper = favouriteBookDtoMapper;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public void notifyFavouriteBooks() {
        favouriteBookRepository.getAllAccountIds().forEach(accountId -> {
            AccountDto account = accountDtoMapper.apply(accountRepository.getById(accountId));
            Collection<FavouriteBook> favouriteBooks = favouriteBookRepository.getFavouriteBooksForAccount(accountId);
            FavouriteBookDto faves = favouriteBookDtoMapper.apply(favouriteBooks);

            notificationService.sendNotification(favouriteBooksNotificationFactory.create(account, faves));
        });
    }
}
