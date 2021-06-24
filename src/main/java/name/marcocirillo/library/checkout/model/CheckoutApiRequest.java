package name.marcocirillo.library.checkout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;


@Value.Immutable
@JsonSerialize(as = ImmutableCheckoutApiRequest.class)
@JsonDeserialize(as = ImmutableCheckoutApiRequest.class)
public abstract class CheckoutApiRequest {
    public abstract List<BookCheckoutRequest> getBooks();
}
