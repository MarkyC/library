package name.marcocirillo.library.notification.factory;

import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.book.favourite.FavouriteBookDto;
import name.marcocirillo.library.notification.ImmutableNotification;
import name.marcocirillo.library.notification.Notification;
import name.marcocirillo.library.notification.template.TemplateService;
import name.marcocirillo.library.util.TimeFormatters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static name.marcocirillo.library.notification.factory.EmailTemplate.FAVOURITE_BOOKS;

@Component
public class FavouriteBooksNotificationFactory {
    private final Clock clock;
    private final TemplateService templateService;

    @Autowired
    public FavouriteBooksNotificationFactory(
            Clock clock, TemplateService templateService
    ) {
        this.clock = clock;
        this.templateService = templateService;
    }

    public Notification create(AccountDto account, FavouriteBookDto favouriteBooks) {
        Map<String, ?> data = Map.of("books", favouriteBooks.getBooks().stream()
                .map(book -> new TemplateFavouriteBook(book.getTitle()))
                .collect(Collectors.toList()));

        return ImmutableNotification.builder()
                .to(Collections.singletonList(account.getEmail()))
                .subject(String.format("Your Favourite Books for the week of %s",
                        TimeFormatters.DAY_OF_WEEK.format(LocalDate.now(clock))))
                .text(templateService.render(FAVOURITE_BOOKS.getTextTemplate(), data))
                .html(templateService.render(FAVOURITE_BOOKS.getHtmlTemplate(), data))
                .build();
    }

    public static class TemplateFavouriteBook {
        private final String title;

        public TemplateFavouriteBook(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "TemplateFavouriteBook{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
