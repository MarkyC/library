package name.marcocirillo.library.notification.factory;

import name.marcocirillo.library.account.dto.AccountDto;
import name.marcocirillo.library.checkout.overdue.OverdueCheckoutDto;
import name.marcocirillo.library.notification.ImmutableNotification;
import name.marcocirillo.library.notification.Notification;
import name.marcocirillo.library.notification.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static name.marcocirillo.library.notification.factory.EmailTemplate.OVERDUE_CHECKOUT;

@Component
public class OverdueCheckoutNotificationFactory {
    private final TemplateService templateService;

    @Autowired
    public OverdueCheckoutNotificationFactory(
            TemplateService templateService
    ) {
        this.templateService = templateService;
    }

    public Notification create(AccountDto account, OverdueCheckoutDto overDueCheckouts) {
        Map<String, ?> data = Map.of("books", overDueCheckouts.getBooks().stream()
                .map(book -> new TemplateOverdueBook(book.getTitle(), book.getQuantity()))
                .collect(Collectors.toList()));

        return ImmutableNotification.builder()
                .to(Collections.singletonList(account.getEmail()))
                .subject("You have overdue books!")
                .text(templateService.render(OVERDUE_CHECKOUT.getTextTemplate(), data))
                .html(templateService.render(OVERDUE_CHECKOUT.getHtmlTemplate(), data))
                .build();
    }

    public static class TemplateOverdueBook {
        private final String title;
        private final int quantity;

        public TemplateOverdueBook(String title, int quantity) {
            this.title = title;
            this.quantity = quantity;
        }

        public String getTitle() {
            return title;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return "TemplateOverdueBook{" +
                    "title='" + title + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
