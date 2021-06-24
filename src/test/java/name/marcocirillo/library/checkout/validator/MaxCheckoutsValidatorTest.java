package name.marcocirillo.library.checkout.validator;

import name.marcocirillo.library.auth.userdetails.AccountUserDetails;
import name.marcocirillo.library.auth.userdetails.AccountUserDetailsService;
import name.marcocirillo.library.checkout.CheckoutRepository;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.dto.ImmutableBookCheckoutRequestDto;
import name.marcocirillo.library.checkout.exception.CheckoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static name.marcocirillo.library.checkout.exception.CheckoutException.ErrorCodes.TOO_MANY_CHECKOUTS;
import static name.marcocirillo.library.checkout.validator.MaxCheckoutsValidator.MAX_CHECKOUTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaxCheckoutsValidatorTest {

    private final AccountUserDetailsService accountUserDetailsService = mock(AccountUserDetailsService.class);
    private final CheckoutRepository checkoutRepository = mock(CheckoutRepository.class);

    private final MaxCheckoutsValidator validator = new MaxCheckoutsValidator(
            accountUserDetailsService,
            checkoutRepository
    );

    @BeforeEach
    void setUp() {
        // return a mock account for all tests
        when(accountUserDetailsService.getUserDetails()).thenReturn(new AccountUserDetails(
                UUID.randomUUID(), "name", "email"));
    }

    @Test
    void validate() {
        validator.validate(Collections.singleton(createBook(10)));
        // no exception thrown!
    }

    @Test
    void validate_tooMany_atOnce() {
        when(checkoutRepository.getUnreturnedBooksCheckedOutByAccount(any())).thenReturn(0);

        CheckoutException exception = assertThrows(CheckoutException.class,
                () -> validator.validate(Collections.singleton(createBook(MAX_CHECKOUTS + 1))));

        assertEquals(TOO_MANY_CHECKOUTS, exception.getErrorCode(), "Correct error is thrown");
    }

    @Test
    void validate_tooMany_existingCheckouts() {
        when(checkoutRepository.getUnreturnedBooksCheckedOutByAccount(any())).thenReturn(MAX_CHECKOUTS);

        CheckoutException exception = assertThrows(CheckoutException.class,
                () -> validator.validate(Collections.singleton(createBook(1))));

        assertEquals(TOO_MANY_CHECKOUTS, exception.getErrorCode(), "Correct error is thrown");
    }


    private static BookCheckoutRequestDto createBook(int quantity) {
        return ImmutableBookCheckoutRequestDto.builder()
                .id(UUID.randomUUID())
                .quantity(quantity)
                .build();
    }
}