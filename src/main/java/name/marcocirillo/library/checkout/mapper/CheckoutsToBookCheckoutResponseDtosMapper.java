package name.marcocirillo.library.checkout.mapper;

import name.marcocirillo.library.checkout.Checkout;
import name.marcocirillo.library.checkout.dto.BookCheckoutResponseDto;
import name.marcocirillo.library.checkout.dto.ImmutableBookCheckoutResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CheckoutsToBookCheckoutResponseDtosMapper implements Function<Collection<Checkout>, Collection<BookCheckoutResponseDto>> {
    @Override
    public Collection<BookCheckoutResponseDto> apply(Collection<Checkout> checkouts) {
        return checkouts.stream()
                .map(checkout -> ImmutableBookCheckoutResponseDto.builder()
                        .id(checkout.getId())
                        .bookId(checkout.getBookId())
                        .due(checkout.getDue())
                        .build())
                .collect(Collectors.toList());
    }
}
