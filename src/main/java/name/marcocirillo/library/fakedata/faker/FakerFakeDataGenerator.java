package name.marcocirillo.library.fakedata.faker;

import com.github.javafaker.Faker;
import name.marcocirillo.library.fakedata.FakeData;
import name.marcocirillo.library.fakedata.FakeDataGenerator;
import name.marcocirillo.library.fakedata.faker.mapper.BookToFakeDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class FakerFakeDataGenerator implements FakeDataGenerator {
    /**
     * Faker doesn't quite have support for 300 languages, but it has ~50 locales.
     * We'll chunk the books across these locales to simulate international titles
     */
    private static final List<Faker> FAKERS = FakerConfig.FAKER_LOCALES.stream()
            .map(locale -> new Faker(new Locale(locale)))
            .collect(Collectors.toUnmodifiableList());

    /**
     * We use this one to generate ISBNs and Stock amounts
     */
    private static final Faker FAKER = new Faker();

    private final Clock clock;
    private final BookToFakeDataMapper bookMapper;

    @Autowired
    public FakerFakeDataGenerator(
            Clock clock,
            BookToFakeDataMapper bookMapper
    ) {
        this.clock = clock;
        this.bookMapper = bookMapper;
    }

    @Override
    public Stream<FakeData> generate(int numBooks) {
        Set<String> seenIsbns = Collections.newSetFromMap(new ConcurrentHashMap<>(numBooks));
        Random random = new Random();

        return IntStream.range(0, numBooks)
                .mapToObj(i -> {
                    // round-robin go through the fakers with different locales to generate international books
                    Faker faker = FAKERS.get(i % FAKERS.size());
                    return faker.book();
                })
                .map(book -> {
                    // generate next ISBN number, must be unique
                    String isbn = FAKER.code().isbn13();
                    while (seenIsbns.contains(isbn)) {
                        isbn = FAKER.code().isbn13();
                    }
                    seenIsbns.add(isbn);

                    String name = FAKER.name().fullName();
                    String email = FAKER.internet().emailAddress();

                    int stock = FAKER.number().numberBetween(0, 25);

                    // if there's stock, there's a 70% chance the book is available
                    boolean available = stock > 0 && Math.random() > 0.3;

                    // if there's no stock, we consider the book returned already
                    // otherwise, it's a coin-flip
                    boolean returned = stock <= 0 || random.nextBoolean();

                    // due date is between now and 14 days from now
                    OffsetDateTime due = OffsetDateTime.now(clock)
                            .plus(random.nextInt(14), ChronoUnit.DAYS)
                            .truncatedTo(ChronoUnit.DAYS);

                    return bookMapper.apply(book, isbn, name, email, stock, available, returned, due);
                });
    }
}
