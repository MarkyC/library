package name.marcocirillo.library.checkout.validator;

import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.BookRepository;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.BookInventoryErrorDto;
import name.marcocirillo.library.checkout.dto.ImmutableBookCheckoutRequestDto;
import name.marcocirillo.library.checkout.exception.CheckoutInventoryException;
import name.marcocirillo.library.inventory.BookInventory;
import name.marcocirillo.library.inventory.BookInventoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static name.marcocirillo.library.checkout.dto.BookInventoryErrorDto.Reason.NOT_AVAILABLE;
import static name.marcocirillo.library.checkout.dto.BookInventoryErrorDto.Reason.NOT_ENOUGH_STOCK;
import static name.marcocirillo.library.checkout.dto.BookInventoryErrorDto.Reason.OUT_OF_STOCK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryValidatorTest {
    private final BookInventoryRepository bookInventoryRepository = mock(BookInventoryRepository.class);
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final InventoryValidator validator = new InventoryValidator(
            bookInventoryRepository,
            bookRepository
    );

    @Test
    void validate() {
        UUID bookId = UUID.randomUUID();
        int inventoryLevel = 4;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book(
                bookId, "title", "1234567890123", UUID.randomUUID(), true, inventoryLevel)));
        when(bookInventoryRepository.findById(bookId)).thenReturn(Optional.of(new BookInventory(
                bookId, inventoryLevel)));
        validator.validate(Collections.singleton(createBook(bookId, 4)));
    }

    @Test
    void validate_notAvailable() {
        UUID bookId = UUID.randomUUID();
        int inventoryLevel = 4;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book(
                bookId, "title", "1234567890123", UUID.randomUUID(), false, inventoryLevel)));
        when(bookInventoryRepository.findById(bookId)).thenReturn(Optional.of(new BookInventory(
                bookId, inventoryLevel)));

        CheckoutInventoryException exception = assertThrows(CheckoutInventoryException.class,
                () -> validator.validate(Collections.singleton(createBook(bookId, 4))),
                "Should throw CheckoutInventoryException since book is not available");

        assertEquals(1, exception.getErrors().size(), "1 error is thrown");
        for (BookInventoryErrorDto error : exception.getErrors()) {
            assertEquals(NOT_AVAILABLE, error.getReason(), "Correct reason is thrown");
            assertEquals(0, error.getInventoryLevel(), "Current inventory returned");
            assertEquals(bookId, error.getBookId(), "Correct book returned");
        }
    }

    @Test
    void validate_outOfStock() {
        UUID bookId = UUID.randomUUID();
        int inventoryLevel = 0;
        int requestedQuantity = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book(
                bookId, "title", "1234567890123", UUID.randomUUID(), true, inventoryLevel)));
        when(bookInventoryRepository.findById(bookId)).thenReturn(Optional.of(new BookInventory(
                bookId, inventoryLevel)));

        CheckoutInventoryException exception = assertThrows(CheckoutInventoryException.class,
                () -> validator.validate(Collections.singleton(createBook(bookId, requestedQuantity))),
                "Should throw CheckoutInventoryException since book is not available");

        assertEquals(1, exception.getErrors().size(), "1 error is thrown");
        for (BookInventoryErrorDto error : exception.getErrors()) {
            assertEquals(OUT_OF_STOCK, error.getReason(), "Correct reason is thrown");
            assertEquals(0, error.getInventoryLevel(), "Current inventory returned");
            assertEquals(bookId, error.getBookId(), "Correct book returned");
        }
    }
    @Test
    void validate_notEnoughStock() {
        UUID bookId = UUID.randomUUID();
        int inventoryLevel = 4;
        int requestedQuantity = 5; // one more than is in stock!

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book(
                bookId, "title", "1234567890123", UUID.randomUUID(), true, inventoryLevel)));
        when(bookInventoryRepository.findById(bookId)).thenReturn(Optional.of(new BookInventory(
                bookId, inventoryLevel)));

        CheckoutInventoryException exception = assertThrows(CheckoutInventoryException.class,
                () -> validator.validate(Collections.singleton(createBook(bookId, requestedQuantity))),
                "Should throw CheckoutInventoryException since book is not available");

        assertEquals(1, exception.getErrors().size(), "1 error is thrown");
        for (BookInventoryErrorDto error : exception.getErrors()) {
            assertEquals(NOT_ENOUGH_STOCK, error.getReason(), "Correct reason is thrown");
            assertEquals(4, error.getInventoryLevel(), "Current inventory returned");
            assertEquals(bookId, error.getBookId(), "Correct book returned");
        }
    }

    private static BookCheckoutRequestDto createBook(UUID bookId, int quantity) {
        return ImmutableBookCheckoutRequestDto.builder()
                .id(bookId)
                .quantity(quantity)
                .build();
    }
}