package name.marcocirillo.library.notification.factory;

import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.book.BookRepository;
import name.marcocirillo.library.notification.ImmutableNotification;
import name.marcocirillo.library.notification.Notification;
import name.marcocirillo.library.notification.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static name.marcocirillo.library.notification.factory.EmailTemplate.CHECKOUT_CREATED;

@Component
public class CheckoutCreatedNotificationFactory {
    private final BookRepository bookRepository;
    private final TemplateService templateService;

    @Autowired
    public CheckoutCreatedNotificationFactory(
            BookRepository bookRepository,
            TemplateService templateService
    ) {
        this.bookRepository = bookRepository;
        this.templateService = templateService;
    }

    public Notification create(AccountDto account, Collection<UUID> bookIds) {
        Collection<TemplateBook> books = bookRepository.findAllById(bookIds).stream()
                .map(book -> new TemplateBook(book.getId(), book.getTitle()))
                .collect(Collectors.toList());

        Map<String, ?> data = Map.of("books", books);
        return ImmutableNotification.builder()
                .to(Collections.singletonList(account.getEmail()))
                .subject("Checkout Summary")
                .text(templateService.render(CHECKOUT_CREATED.getTextTemplate(), data))
                .html(templateService.render(CHECKOUT_CREATED.getHtmlTemplate(), data))
                .build();
    }

    public static class TemplateBook {
        private final UUID id;
        private final String title;

        public TemplateBook(UUID id, String title) {
            this.id = id;
            this.title = title;
        }

        public UUID getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "TemplateBook{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
