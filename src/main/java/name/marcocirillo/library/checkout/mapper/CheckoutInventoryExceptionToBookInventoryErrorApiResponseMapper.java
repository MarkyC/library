package name.marcocirillo.library.checkout.mapper;

import name.marcocirillo.library.checkout.exception.CheckoutInventoryException;
import name.marcocirillo.library.checkout.model.BookInventoryErrorApiResponse;
import name.marcocirillo.library.checkout.model.BookInventoryErrorApiResponse.Reason;
import name.marcocirillo.library.checkout.model.ImmutableBookInventoryError;
import name.marcocirillo.library.checkout.model.ImmutableBookInventoryErrorApiResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CheckoutInventoryExceptionToBookInventoryErrorApiResponseMapper implements Function<CheckoutInventoryException, BookInventoryErrorApiResponse> {
    @Override
    public BookInventoryErrorApiResponse apply(CheckoutInventoryException checkoutInventoryException) {
        return ImmutableBookInventoryErrorApiResponse.builder()
                .errors(checkoutInventoryException.getErrors().stream()
                        .map(error -> ImmutableBookInventoryError.builder()
                                .bookId(error.getBookId())
                                .inventoryLevel(error.getInventoryLevel())
                                .reason(Reason.valueOf(error.getReason().name()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
