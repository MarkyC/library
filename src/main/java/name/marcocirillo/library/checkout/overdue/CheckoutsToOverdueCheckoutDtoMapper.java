package name.marcocirillo.library.checkout.overdue;

import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.BookRepository;
import name.marcocirillo.library.checkout.Checkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CheckoutsToOverdueCheckoutDtoMapper implements Function<Collection<Checkout>, OverdueCheckoutDto> {
    private final BookRepository bookRepository;

    @Autowired
    public CheckoutsToOverdueCheckoutDtoMapper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public OverdueCheckoutDto apply(Collection<Checkout> checkouts) {
        Map<UUID, List<Checkout>> bookCheckouts = checkouts.stream()
                .collect(Collectors.groupingBy(Checkout::getBookId));

        List<OverdueCheckoutDto.Book> overdueBooks = bookCheckouts.entrySet().stream()
                .map(bookCheckout -> {
                    Book book = bookRepository.getById(bookCheckout.getKey());
                    return ImmutableBook.builder()
                            .title(book.getTitle())
                            .quantity(bookCheckout.getValue().size())
                            .build();
                })
                .collect(Collectors.toList());

        return ImmutableOverdueCheckoutDto.builder()
                .books(overdueBooks)
                .build();
    }
}
