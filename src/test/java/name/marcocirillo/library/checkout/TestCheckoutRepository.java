package name.marcocirillo.library.checkout;

import name.marcocirillo.library.account.db.Account;
import name.marcocirillo.library.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class TestCheckoutRepository {
    @Autowired
    private CheckoutRepository checkoutRepository;

    public Checkout createCheckout(Account account, Book book, boolean returned, OffsetDateTime due) {
        return checkoutRepository.save(new Checkout(
                UUID.randomUUID(),
                book.getId(),
                account.getId(),
                returned,
                due
        ));
    }
}