package name.marcocirillo.library.fakedata.mapper;

import name.marcocirillo.library.fakedata.FakeData;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import static name.marcocirillo.library.util.Strings.escapeSql;

@Component
public class FakeDataToSqlMapper implements Function<FakeData, String> {

    @Override
    public String apply(FakeData fakeData) {
        String author = escapeSql(fakeData.getAuthor());
        String title = escapeSql(fakeData.getTitle());
        String isbn = escapeSql(fakeData.getIsbn());
        String genre = escapeSql(fakeData.getGenre());
        int stock = fakeData.getStock();
        String accountName = escapeSql(fakeData.getAccountName());
        String accountEmail = escapeSql(fakeData.getAccountEmail());
        boolean available = fakeData.isAvailable();
        boolean returned = fakeData.isReturned();
        String due = fakeData.getDue().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return String.format("INSERT INTO author (name) SELECT '%1$s' WHERE NOT EXISTS (SELECT author FROM author WHERE name = '%1$s');%n", author)
                + String.format("INSERT INTO book (title, author_id, isbn, available, stock) VALUES ('%s', (SELECT id FROM author WHERE name = '%s'), '%s', %b, %d);%n", title, author, isbn, available, stock)
                + String.format("INSERT INTO category (name) SELECT '%1$s' WHERE NOT EXISTS (SELECT name FROM category WHERE name = '%1$s');%n", genre)
                + String.format("INSERT INTO book_category (book_id, category_id) VALUES ((SELECT id FROM book WHERE isbn = '%s'), (SELECT id FROM category WHERE name = '%s'));%n", isbn, genre)
                + String.format("INSERT INTO account (name, email) SELECT '%1$s', '%2$s' WHERE NOT EXISTS (SELECT email FROM account WHERE email = '%2$s');%n", accountName, accountEmail)
                + String.format("INSERT INTO checkout (book_id, account_id, returned, due) VALUES ((SELECT id FROM book WHERE isbn = '%s'), (SELECT id FROM account WHERE email = '%s'), %b, '%s');%n", isbn, accountEmail, returned, due)
        ;
    }
}
