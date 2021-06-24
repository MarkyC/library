package name.marcocirillo.library.author;

import name.marcocirillo.library.fakedata.FakeDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestAuthorRepository {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private FakeDataGenerator fakeDataGenerator;

    public Author createAuthor() {
        return fakeDataGenerator.generate(1).map(fakeData -> new Author(
                UUID.randomUUID(),
                fakeData.getAuthor()
        ))
                .map(authorRepository::save)
                .findFirst().orElseThrow();
    }
}