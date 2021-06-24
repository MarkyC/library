package name.marcocirillo.library.checkout.mapper;

import name.marcocirillo.library.checkout.Checkout;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BookCheckoutRequestDtosToCheckoutsMapper {
    public Collection<Checkout> apply(Collection<BookCheckoutRequestDto> books, UUID accountId, OffsetDateTime due) {
        return books.stream()
                .flatMap(book -> IntStream.range(0, book.getQuantity())
                        .mapToObj(__ -> new Checkout(book.getId(), accountId, due)))
                .collect(Collectors.toList());
    }
}
