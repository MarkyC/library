package name.marcocirillo.library.checkout.validator;

import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.BookRepository;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.BookInventoryErrorDto;
import name.marcocirillo.library.checkout.dto.ImmutableBookInventoryErrorDto;
import name.marcocirillo.library.checkout.exception.CheckoutInventoryException;
import name.marcocirillo.library.inventory.BookInventory;
import name.marcocirillo.library.inventory.BookInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static name.marcocirillo.library.checkout.dto.BookInventoryErrorDto.Reason.NOT_AVAILABLE;
import static name.marcocirillo.library.checkout.dto.BookInventoryErrorDto.Reason.NOT_ENOUGH_STOCK;
import static name.marcocirillo.library.checkout.dto.BookInventoryErrorDto.Reason.OUT_OF_STOCK;

/**
 * Ensures there is enough inventory to satisfy this Checkout request
 */
@Component
public class InventoryValidator {
    private final BookInventoryRepository bookInventoryRepository;
    private final BookRepository bookRepository;

    @Autowired
    public InventoryValidator(
            BookInventoryRepository bookInventoryRepository,
            BookRepository bookRepository
    ) {
        this.bookInventoryRepository = bookInventoryRepository;
        this.bookRepository = bookRepository;
    }

    public void validate(Collection<BookCheckoutRequestDto> bookDtos) {
        List<BookInventoryErrorDto> errors = new ArrayList<>();

        for (BookCheckoutRequestDto bookRequest : bookDtos) {
            UUID bookId = bookRequest.getId();
            Book book = bookRepository.findById(bookId).orElse(null);

            if (book == null || !book.getAvailable()) {
                errors.add(ImmutableBookInventoryErrorDto.of(bookId, 0, NOT_AVAILABLE));
            } else {
                int inventory = bookInventoryRepository.findById(bookId)
                        .map(BookInventory::getInventoryLevel)
                        .orElse(0);

                if (bookRequest.getQuantity() > inventory) {
                    // there's not enough books to check out
                    errors.add(ImmutableBookInventoryErrorDto
                            .of(bookId, inventory, inventory == 0 ? OUT_OF_STOCK : NOT_ENOUGH_STOCK));
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new CheckoutInventoryException(errors);
        }
    }
}
