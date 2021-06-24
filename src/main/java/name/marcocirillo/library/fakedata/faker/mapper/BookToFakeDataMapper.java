package name.marcocirillo.library.fakedata.faker.mapper;

import com.github.javafaker.Book;
import name.marcocirillo.library.fakedata.FakeData;
import name.marcocirillo.library.fakedata.ImmutableFakeData;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class BookToFakeDataMapper {
    public FakeData apply(Book book, String isbn, String name, String email, int stock, boolean available, boolean returned, OffsetDateTime due) {
        return ImmutableFakeData.builder()
                .author(book.author())
                .title(book.title())
                .isbn(isbn)
                .genre(book.genre())
                .stock(stock)
                .accountName(name)
                .accountEmail(email)
                .isAvailable(available)
                .isReturned(returned)
                .due(due)
                .build();
    }
}
