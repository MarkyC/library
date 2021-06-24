package name.marcocirillo.library.checkout;

import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.BookCheckoutResponseDto;
import name.marcocirillo.library.checkout.exception.CheckoutInventoryException;
import name.marcocirillo.library.checkout.mapper.CheckoutApiRequestToBookRequestDtoMapper;
import name.marcocirillo.library.checkout.mapper.CheckoutInventoryExceptionToBookInventoryErrorApiResponseMapper;
import name.marcocirillo.library.checkout.mapper.CheckoutResponseDtosToBookCheckoutApiResponseMapper;
import name.marcocirillo.library.checkout.model.CheckoutApiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class CheckoutController {
    private final CheckoutApiRequestToBookRequestDtoMapper bookRequestDtoMapper;
    private final CheckoutInventoryExceptionToBookInventoryErrorApiResponseMapper bookInventoryErrorApiResponseMapper;
    private final CheckoutResponseDtosToBookCheckoutApiResponseMapper checkoutApiResponseMapper;
    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(
            CheckoutApiRequestToBookRequestDtoMapper bookRequestDtoMapper,
            CheckoutInventoryExceptionToBookInventoryErrorApiResponseMapper bookInventoryErrorApiResponseMapper,
            CheckoutResponseDtosToBookCheckoutApiResponseMapper checkoutApiResponseMapper,
            CheckoutService checkoutService
    ) {
        this.bookRequestDtoMapper = bookRequestDtoMapper;
        this.bookInventoryErrorApiResponseMapper = bookInventoryErrorApiResponseMapper;
        this.checkoutApiResponseMapper = checkoutApiResponseMapper;
        this.checkoutService = checkoutService;
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public ResponseEntity<?> checkout(@RequestBody CheckoutApiRequest request) {
        List<BookCheckoutRequestDto> books = bookRequestDtoMapper.apply(request);

        try {
            Collection<BookCheckoutResponseDto> checkouts = checkoutService.checkout(books);
            return ResponseEntity.ok(checkoutApiResponseMapper.apply(checkouts));
        } catch (CheckoutInventoryException e) {
            return ResponseEntity.badRequest().body(bookInventoryErrorApiResponseMapper.apply(e));
        }
    }
}
