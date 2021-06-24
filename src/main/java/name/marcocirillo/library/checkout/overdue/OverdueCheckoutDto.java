package name.marcocirillo.library.checkout.overdue;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface OverdueCheckoutDto {
    List<Book> getBooks();

    @Value.Immutable
    interface Book {
        String getTitle();
        int getQuantity();
    }
}
