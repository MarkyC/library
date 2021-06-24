package name.marcocirillo.library.book.favourite;

import name.marcocirillo.library.account.AccountToAccountDtoMapper;
import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.AccountRepository;
import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.BookRepository;
import name.marcocirillo.library.notification.NotificationService;
import name.marcocirillo.library.notification.factory.FavouriteBooksNotificationFactory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavouriteBookServiceTest {
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final FavouriteBooksNotificationFactory notificationFactory = mock(FavouriteBooksNotificationFactory.class);
    private final FavouriteBookRepository favouriteBookRepository = mock(FavouriteBookRepository.class);
    private final NotificationService notificationService = mock(NotificationService.class);

    private final FavouriteBookService favouriteBookService = new FavouriteBookService(
            new AccountToAccountDtoMapper(),
            accountRepository,
            notificationFactory,
            favouriteBookRepository,
            new FavouriteBooksToFavouriteBookDtoMapper(bookRepository),
            notificationService
    );

    @Test
    void notifyFavouriteBooks() {
        int numFavouriteBooks = 5;
        int numAccounts = 9;

        when(accountRepository.getById(any()))
                .thenReturn(new Account(UUID.randomUUID(), "name", "email@domain"));

        when(bookRepository.getById(any()))
                .thenReturn(new Book(UUID.randomUUID(), "title", "isbn", UUID.randomUUID(), true, 100));

        when(favouriteBookRepository.getAllAccountIds())
                .thenReturn(IntStream.range(0, numAccounts)
                        .mapToObj(__ -> UUID.randomUUID()));

        when(favouriteBookRepository.getFavouriteBooksForAccount(any()))
                .thenReturn(IntStream.range(0, numFavouriteBooks)
                        .mapToObj(__ -> new FavouriteBook(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2))
                        .collect(Collectors.toList()));

        favouriteBookService.notifyFavouriteBooks();

        verify(notificationService, times(numAccounts)).sendNotification(any());

        ArgumentCaptor<FavouriteBookDto> argument = ArgumentCaptor.forClass(FavouriteBookDto.class);
        verify(notificationFactory, times(numAccounts)).create(any(), argument.capture());
        assertEquals(numFavouriteBooks, argument.getValue().getBooks().size(),
                "Correct number of favourite books returned");
    }
}