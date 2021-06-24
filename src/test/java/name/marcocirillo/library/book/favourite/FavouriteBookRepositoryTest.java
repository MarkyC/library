package name.marcocirillo.library.book.favourite;

import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.account.db.TestAccountRepository;
import name.marcocirillo.library.author.Author;
import name.marcocirillo.library.author.TestAuthorRepository;
import name.marcocirillo.library.base.BaseIntegrationTest;
import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.TestBookRepository;
import name.marcocirillo.library.checkout.CheckoutRepository;
import name.marcocirillo.library.checkout.TestCheckoutRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static name.marcocirillo.library.book.favourite.FavouriteBookRepository.MAX_FAVOURITE_BOOKS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FavouriteBookRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private FavouriteBookRepository favouriteBookRepository;

    @Autowired
    private TestAccountRepository testAccountRepository;

    @Autowired
    private TestAuthorRepository testAuthorRepository;

    @Autowired
    private TestBookRepository testBookRepository;

    @Autowired
    private TestCheckoutRepository testCheckoutRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Test
    void getAllAccountIds() {
        int numAccounts = 3;
        int numBooks = 10;

        List<Account> accounts = IntStream.range(0, numAccounts)
                .mapToObj(__ -> testAccountRepository.createAccount())
                .collect(Collectors.toList());

        Author author = testAuthorRepository.createAuthor(); // Pretend all books are by the same author

        List<Book> books = IntStream.range(0, numBooks)
                .mapToObj(i -> testBookRepository.createBook(author))
                .collect(Collectors.toList());

        for (int bookIndex = 0; bookIndex < numBooks; bookIndex++) {
            Book book = books.get(bookIndex);
            for (int accountIndex = 0; accountIndex < numAccounts; accountIndex++) {
                Account account = accounts.get(accountIndex);

                // first book is checked out once
                // second book is checked out twice, etc
                // this means the last book is the most favourite!
                for (int numCheckouts = 0; numCheckouts < bookIndex + 1; numCheckouts++) {
                    testCheckoutRepository.createCheckout(
                            account,
                            book,
                            // always returned so we don't go over MAX_CHECKOUTS
                            true,
                            // due date doesn't matter
                            OffsetDateTime.now());
                }
            }
        }

        // We need to ping CheckoutRepository otherwise the view that backs FavouriteBookRepository doesn't get updated,
        // making these tests fail. One more assertion never hurts anyways!
        accounts.forEach(account -> assertFalse(checkoutRepository.getAllByAccountIdAndReturned(account.getId(), true).isEmpty(),
                "Account has Checkouts"));

        List<UUID> accountIds = favouriteBookRepository.getAllAccountIds().collect(Collectors.toList());
        accounts.forEach(account -> assertTrue(accountIds.contains(account.getId()),
                "Contains the Accounts we created Checkouts for"));
    }

    @Test
    void getFavouriteBooksForAccount() {
        int numAccounts = 3;
        int numBooks = 10;

        List<Account> accounts = IntStream.range(0, numAccounts)
                .mapToObj(__ -> testAccountRepository.createAccount())
                .collect(Collectors.toList());

        Author author = testAuthorRepository.createAuthor(); // Pretend all books are by the same author

        List<Book> books = IntStream.range(0, numBooks)
                .mapToObj(i -> testBookRepository.createBook(author))
                .collect(Collectors.toList());

        for (int bookIndex = 0; bookIndex < numBooks; bookIndex++) {
            Book book = books.get(bookIndex);
            for (int accountIndex = 0; accountIndex < numAccounts; accountIndex++) {
                Account account = accounts.get(accountIndex);

                // first book is checked out once
                // second book is checked out twice, etc
                // this means the last book is the most favourite!
                for (int numCheckouts = 0; numCheckouts < bookIndex + 1; numCheckouts++) {
                    testCheckoutRepository.createCheckout(
                            account,
                            book,
                            // always returned so we don't go over MAX_CHECKOUTS
                            true,
                            // due date doesn't matter
                            OffsetDateTime.now());
                }
            }
        }

        // We need to ping CheckoutRepository otherwise the view that backs FavouriteBookRepository doesn't get updated,
        // making these tests fail. One more assertion never hurts anyways!
        accounts.forEach(account -> assertFalse(checkoutRepository.getAllByAccountIdAndReturned(account.getId(), true).isEmpty(),
                "Account has Checkouts"));

        accounts.forEach(account -> {
            List<FavouriteBook> faves = List.copyOf(favouriteBookRepository.getFavouriteBooksForAccount(account.getId()));
            assertEquals(MAX_FAVOURITE_BOOKS, faves.size(), "Account has the max amount of Favourite Books");
            assertEquals(books.get(numBooks - 1).getId(), faves.get(0).getBookId(), "The last book is the most Favourite");
            assertEquals(books.get(numBooks - 2).getId(), faves.get(1).getBookId(), "The second last book is the second most Favourite");
        });
    }

}