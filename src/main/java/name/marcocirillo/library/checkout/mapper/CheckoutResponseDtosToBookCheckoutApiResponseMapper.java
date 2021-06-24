package name.marcocirillo.library.checkout.mapper;

import name.marcocirillo.library.checkout.dto.BookCheckoutResponseDto;
import name.marcocirillo.library.checkout.model.CheckoutApiResponse;
import name.marcocirillo.library.checkout.model.ImmutableBookCheckoutResponse;
import name.marcocirillo.library.checkout.model.ImmutableCheckoutApiResponse;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CheckoutResponseDtosToBookCheckoutApiResponseMapper implements Function<Collection<BookCheckoutResponseDto>, CheckoutApiResponse> {
    @Override
    public CheckoutApiResponse apply(Collection<BookCheckoutResponseDto> bookCheckoutResponseDtos) {
        return ImmutableCheckoutApiResponse.builder()
                .checkouts(bookCheckoutResponseDtos.stream()
                        .map(checkout -> ImmutableBookCheckoutResponse.builder()
                                .id(checkout.getId())
                                .bookId(checkout.getBookId())
                                .due(checkout.getDue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
