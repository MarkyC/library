package name.marcocirillo.library.book;

import name.marcocirillo.library.author.Author;
import name.marcocirillo.library.fakedata.FakeDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestBookRepository {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FakeDataGenerator fakeDataGenerator;

    public Book createBook(Author author) {
        return createBook(author, true);
    }

    public Book createBook(Author author, boolean available) {
        return createBook(author, available, 10);
    }

    public Book createBook(Author author, boolean available, int stock) {
        return fakeDataGenerator.generate(1).map(fakeData -> new Book(
                UUID.randomUUID(),
                fakeData.getTitle(),
                fakeData.getIsbn(),
                author.getId(),
                available,
                stock
        ))
                .map(bookRepository::save)
                .findFirst().orElseThrow();
    }

}