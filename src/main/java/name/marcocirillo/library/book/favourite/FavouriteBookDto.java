package name.marcocirillo.library.book.favourite;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface FavouriteBookDto {
    List<Book> getBooks();

    @Value.Immutable
    interface Book {
        String getTitle();
    }
}
