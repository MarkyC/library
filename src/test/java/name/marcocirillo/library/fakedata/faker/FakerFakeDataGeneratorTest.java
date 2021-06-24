package name.marcocirillo.library.fakedata.faker;

import name.marcocirillo.library.fakedata.faker.mapper.BookToFakeDataMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FakerFakeDataGeneratorTest {
    private static final FakerFakeDataGenerator GENERATOR = new FakerFakeDataGenerator(Clock.systemUTC(), new BookToFakeDataMapper());

    @Test
    void generate() {
        int numBooks = 123;

        AtomicInteger numGenerated = new AtomicInteger(0);
        GENERATOR.generate(numBooks)
                .forEach(fakeBook -> {
                    numGenerated.addAndGet(1);
                    assertNotNull(fakeBook, "Generated book can't be null");
                    assertTrue(StringUtils.isNotBlank(fakeBook.getAuthor()), String.format("author is empty:%n%s", fakeBook));
                    assertTrue(StringUtils.isNotBlank(fakeBook.getTitle()), String.format("title is empty:%n%s", fakeBook));
                    assertTrue(StringUtils.isNotBlank(fakeBook.getIsbn()), String.format("isbn is empty:%n%s", fakeBook));
                    assertEquals(13, fakeBook.getIsbn().length(), String.format("isbn is malformed:%n%s", fakeBook));
                    assertTrue(StringUtils.isNotBlank(fakeBook.getGenre()), String.format("genre is empty:%n%s", fakeBook));
                    assertTrue(fakeBook.getStock() >= 0, String.format("stock is malformed:%n%s", fakeBook));
                });

        assertEquals(numBooks, numGenerated.get(), "Correct number of books generated");
    }
}