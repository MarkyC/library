package name.marcocirillo.library.checkout.validator;

import name.marcocirillo.library.auth.userdetails.AccountUserDetailsService;
import name.marcocirillo.library.checkout.CheckoutRepository;
import name.marcocirillo.library.checkout.dto.BookCheckoutRequestDto;
import name.marcocirillo.library.checkout.exception.CheckoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

import static name.marcocirillo.library.checkout.exception.CheckoutException.ErrorCodes.TOO_MANY_CHECKOUTS;

/**
 * Ensures an Account doesn't have more Checkouts than (both requested and pre-existing) than our max configured amount.
 *
 * @see #MAX_CHECKOUTS
 */
@Component
public class MaxCheckoutsValidator {
    /**
     * Maximum number of unreturned Checkouts per Account
     */
    public static final int MAX_CHECKOUTS = 10;

    private final AccountUserDetailsService accountUserDetailsService;
    private final CheckoutRepository checkoutRepository;

    @Autowired
    public MaxCheckoutsValidator(AccountUserDetailsService accountUserDetailsService, CheckoutRepository checkoutRepository) {
        this.accountUserDetailsService = accountUserDetailsService;
        this.checkoutRepository = checkoutRepository;
    }

    public void validate(Collection<BookCheckoutRequestDto> bookDtos) {
        UUID accountId = accountUserDetailsService.getUserDetails().getId();

        int numBooksRequested = bookDtos.stream().mapToInt(BookCheckoutRequestDto::getQuantity).sum();
        int numBooksAlreadyCheckedOut = checkoutRepository.getUnreturnedBooksCheckedOutByAccount(accountId);

        if (numBooksRequested + numBooksAlreadyCheckedOut > MAX_CHECKOUTS) {
            // This is also enforced by a DB trigger (using the function enforce_checkout_count())
            // This approach here isn't thread safe (2 requests for 10 books could succeed if sent at the same time),
            // it's just here to catch low-hanging fruit without an expensive DB exception + row locking
            throw new CheckoutException(TOO_MANY_CHECKOUTS, String.format(
                    "Too many checkouts for account '%s'.%nRequested: %d%nAlready checked out: %d%n",
                    accountId, numBooksRequested, numBooksAlreadyCheckedOut));
        }
    }
}
