package name.marcocirillo.library.checkout.mapper;

import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.ImmutableBookCheckoutRequestDto;
import name.marcocirillo.library.checkout.model.CheckoutApiRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CheckoutApiRequestToBookRequestDtoMapper implements Function<CheckoutApiRequest, List<BookCheckoutRequestDto>> {
    @Override
    public List<BookCheckoutRequestDto> apply(CheckoutApiRequest request) {
        return request.getBooks().stream()
                .map(bookRequest -> ImmutableBookCheckoutRequestDto.builder()
                        .id(bookRequest.getId())
                        .quantity(bookRequest.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}
